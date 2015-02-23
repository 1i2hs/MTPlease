package com.owo.mtplease.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.owo.mtplease.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpFragment.OnSignUpFragmentListener} interface
 * to handle interaction events.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

	private static final String TAG = "SignUpFragment";
	private static final String ARG_PAGE_NUMBER = "param1";
	private static final String MTPLEASE_URL = "http://mtplease.herokuapp.com/";

	public static final int FIRST_SIGN_UP_PAGE = 1;
	public static final int SECOND_SIGN_UP_PAGE = 2;
	public static final int THIRD_SIGN_UP_PAGE = 3;

	private int pageNumber;

	// View instances for first page of the sign up process
	private CheckBox agreeTermsOfUseCheckBox;
	// end of the view instances

	// View instances for second page of the sign up process
	private EditText emailAddressEditText;
	private Button authenticateEmailAddressButton;
	private EditText emailAddressAuthenticationCodeEditText;
	private Button confirmEmailAddressAuthenticationCodeButton;
	private EditText passwordEditText;
	private EditText passwordConfirmEditText;
	// end of the view instances

	// View instances for third page of the sign up process
	private EditText userNameEditText;
	private EditText schoolNameEditText;
	private EditText nicknameEditText;
	private Button checkNicknameDuplicationButton;
	private Button  signUpButton;
	// end of the view instances

	// View instances for all pages of the sign up process
	private Button goToNextPageButton;
	private Button goToPreviousPageButton;
	// end of the view instances

	private String base64EncodedEmailAddressAuthenticationCode = "";
	private String emailAddress;
	private boolean isEmailAddressAuthenticated = false;

	private OnSignUpFragmentListener mSignUpFragmentListener;

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment SignUpFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static SignUpFragment newInstance(int pageNumber) {
		Log.d(TAG, "SignUpFragment" + pageNumber);
		SignUpFragment fragment = new SignUpFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE_NUMBER, pageNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public SignUpFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getArguments() != null) {
			pageNumber = getArguments().getInt(ARG_PAGE_NUMBER);
			Log.d(TAG, "onCreate" + pageNumber);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView" + pageNumber);
		View signUpFragmentView;

		switch(pageNumber) {
			case FIRST_SIGN_UP_PAGE:
				signUpFragmentView = inflater.inflate(R.layout.fragment_sign_up_first, container, false);

				agreeTermsOfUseCheckBox = (CheckBox) signUpFragmentView.findViewById(R.id.checkBox_agree_terms_of_use);
				agreeTermsOfUseCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(isChecked)
							goToNextPageButton.setEnabled(true);
					}
				});

				goToNextPageButton = (Button) signUpFragmentView.findViewById(R.id.btn_go_page_next);
				goToNextPageButton.setEnabled(false);
				goToNextPageButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(agreeTermsOfUseCheckBox.isChecked()) {
							mSignUpFragmentListener.onClickGoToNextPageButton();
						}
					}
				});

				break;
			case SECOND_SIGN_UP_PAGE:
				signUpFragmentView = inflater.inflate(R.layout.fragment_sign_up_second, container, false);

				emailAddressEditText = (EditText) signUpFragmentView.findViewById(R.id.editText_email_sign_up);

				authenticateEmailAddressButton = (Button) signUpFragmentView.findViewById(R.id.btn_authenticate_email_address);
				authenticateEmailAddressButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!isValidEmail(emailAddressEditText.getText().toString()))
							Toast.makeText(getActivity(), R.string.invalid_email_pattern, Toast.LENGTH_SHORT).show();
						else {
							// send email to user's email address
							emailAddress = emailAddressEditText.getText().toString();
							String temp[] = emailAddress.split("@");
							base64EncodedEmailAddressAuthenticationCode = Base64.encodeToString(temp[0].getBytes(), Base64.NO_WRAP);
							new HttpPostRequestTask(emailAddress).execute(MTPLEASE_URL + "member/");
						}
					}
				});

				emailAddressAuthenticationCodeEditText = (EditText) signUpFragmentView.findViewById(R.id.editText_email_authentication_code);

				confirmEmailAddressAuthenticationCodeButton = (Button) signUpFragmentView.findViewById(R.id.btn_confirm_email_address_authentication_code);
				confirmEmailAddressAuthenticationCodeButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(base64EncodedEmailAddressAuthenticationCode.equals(emailAddressAuthenticationCodeEditText.getText().toString())) {
							Toast.makeText(getActivity(), R.string.email_address_authenticated, Toast.LENGTH_SHORT).show();
							confirmEmailAddressAuthenticationCodeButton.setText(R.string.authentication_complete);
							confirmEmailAddressAuthenticationCodeButton.setEnabled(false);
							isEmailAddressAuthenticated = true;
						}
						else {
							Toast.makeText(getActivity(), R.string.wrong_authentication_code, Toast.LENGTH_SHORT).show();
							isEmailAddressAuthenticated = false;
						}
					}
				});

				passwordEditText = (EditText) signUpFragmentView.findViewById(R.id.editText_password_sign_up);

				passwordConfirmEditText = (EditText) signUpFragmentView.findViewById(R.id.editText_confirm_password_sign_up);

				goToNextPageButton = (Button) signUpFragmentView.findViewById(R.id.btn_go_page_next);
				goToNextPageButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isEmailAddressAuthenticated) {
							String password = passwordEditText.getText().toString();
							String passwordConfirm = passwordConfirmEditText.getText().toString();

							if(password.equals(passwordConfirm)) {
								mSignUpFragmentListener.onClickGoToNextPageButton(emailAddress, password);
							}
							else {
								Toast.makeText(getActivity(), R.string.password_does_not_match, Toast.LENGTH_SHORT).show();
								passwordEditText.requestFocus();
								passwordEditText.setText("");
								passwordConfirmEditText.setText("");
							}
						} else {
							Toast.makeText(getActivity(), R.string.email_address_is_not_authenticated, Toast.LENGTH_SHORT).show();
							emailAddressEditText.requestFocus();
						}
					}
				});

				goToPreviousPageButton = (Button) signUpFragmentView.findViewById(R.id.btn_go_page_previous);
				goToPreviousPageButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mSignUpFragmentListener.onClickGoToPreviousPageButton(SECOND_SIGN_UP_PAGE);
					}
				});

				break;
			case THIRD_SIGN_UP_PAGE:
				signUpFragmentView = inflater.inflate(R.layout.fragment_sign_up_third, container, false);

				userNameEditText = (EditText) signUpFragmentView.findViewById(R.id.editText_name_user);

				schoolNameEditText = (EditText) signUpFragmentView.findViewById(R.id.editText_name_school);

				nicknameEditText = (EditText) signUpFragmentView.findViewById(R.id.editText_nickname);

				/*checkNicknameDuplicationButton = (Button) signUpFragmentView.findViewById(R.id.btn_check_duplication_nickname);
				checkNicknameDuplicationButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});*/

				signUpButton = (Button) signUpFragmentView.findViewById(R.id.btn_sign_up);
				signUpButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String userName = userNameEditText.getText().toString();
						String schoolName = schoolNameEditText.getText().toString();
						String nickname = nicknameEditText.getText().toString();

						if(userName.equals("")) {
							Toast.makeText(getActivity(), R.string.please_type_name, Toast.LENGTH_SHORT).show();
							userNameEditText.requestFocus();
						} else if(schoolName.equals("")) {
							Toast.makeText(getActivity(), R.string.please_type_school_name, Toast.LENGTH_SHORT).show();
							schoolNameEditText.requestFocus();
						} else if(nickname.equals("")) {
							Toast.makeText(getActivity(), R.string.please_type_nickname, Toast.LENGTH_SHORT).show();
							nicknameEditText.requestFocus();
						} else {
							mSignUpFragmentListener.onClickSignUpButton(userName, schoolName, nickname);
						}
					}
				});

				goToPreviousPageButton = (Button) signUpFragmentView.findViewById(R.id.btn_go_page_previous);
				goToPreviousPageButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mSignUpFragmentListener.onClickGoToPreviousPageButton(THIRD_SIGN_UP_PAGE);
					}
				});

				break;
			default:
				signUpFragmentView = null;
				break;
		}

		return signUpFragmentView;
	}

	public static boolean isValidEmail(String email) {
		boolean err = false;

		String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(email);

		if(m.matches()) {
			err = true;
		}
		return err;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mSignUpFragmentListener = (OnSignUpFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mSignUpFragmentListener = null;
	}

	private class HttpPostRequestTask extends AsyncTask<String, Integer, String> {

		private String userEmailAddress;

		public HttpPostRequestTask(String userEmailAddress) {
			this.userEmailAddress = userEmailAddress;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			mSignUpFragmentListener.onStartSendingAuthenticationCode(authenticateEmailAddressButton);
		}

		@Override
		protected String doInBackground(String... urls) {
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 5000);
				HttpPost httpPost = new HttpPost(urls[0]);
				List params = new ArrayList();
				params.add(new BasicNameValuePair("user_id", userEmailAddress));
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
				httpPost.setEntity(ent);
				HttpResponse responsePost = httpClient.execute(httpPost);
				HttpEntity resEntityPost = responsePost.getEntity();

				if (resEntityPost != null) {
					return EntityUtils.toString(resEntityPost);
				}
			} catch(ClientProtocolException e) {
				e.printStackTrace();
			} catch(IOException e) {
				e.printStackTrace();
			} catch(Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String jsonString) {
			super.onPostExecute(jsonString);

			mSignUpFragmentListener.onEndSendingAuthenticationCode();

			try {
				JSONObject responseJSONObject = new JSONObject(jsonString);
				String status = responseJSONObject.optString("status");

				if(status.equals("success")) {
					Toast.makeText(getActivity(), R.string.authentication_code_sent_to_email_address, Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(getActivity(), R.string.email_address_already_exists, Toast.LENGTH_SHORT).show();
				}
			} catch(JSONException e) {
				//		Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch(NullPointerException e) {
				Toast.makeText(getActivity(), R.string.fail_to_send_authentication_code, Toast.LENGTH_SHORT).show();
			}
		}
	}

	public interface OnSignUpFragmentListener {
		public void onStartSendingAuthenticationCode(View view);
		public void onEndSendingAuthenticationCode();
		public void onClickGoToNextPageButton();
		public void onClickGoToNextPageButton(String emailAddress, String password);
		public void onClickSignUpButton(String userName, String schoolName, String nickname);
		public void onClickGoToPreviousPageButton(int pageNumber);
	}

}
