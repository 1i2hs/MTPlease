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

	private int _pageNumber;

	// View instances for first page of the sign up process
	private CheckBox _agreeTermsOfUseCheckBox;
	// end of the view instances

	// View instances for second page of the sign up process
	private EditText __emailAddressEditText;
	private Button _authenticateEmailAddressButton;
	private EditText __emailAddressAuthenticationCodeEditText;
	private Button _confirmEmailAddressAuthenticationCodeButton;
	private EditText _passwordEditText;
	private EditText _passwordConfirmEditText;
	// end of the view instances

	// View instances for third page of the sign up process
	private EditText _userNameEditText;
	private EditText _schoolNameEditText;
	private EditText _nicknameEditText;
	private Button _checkNicknameDuplicationButton;
	private Button  _signUpButton;
	// end of the view instances

	// View instances for all pages of the sign up process
	private Button _goToNextPageButton;
	private Button _goToPreviousPageButton;
	// end of the view instances

	private String _base64EncodedEmailAddressAuthenticationCode = "";
	private String _emailAddress;
	private boolean _isEmailAddressAuthenticated = false;

	private OnSignUpFragmentListener _mSignUpFragmentListener;

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment SignUpFragment.
	 */
	public static SignUpFragment newInstance(int _pageNumber) {
		Log.d(TAG, "SignUpFragment" + _pageNumber);
		SignUpFragment fragment = new SignUpFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE_NUMBER, _pageNumber);
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
			_pageNumber = getArguments().getInt(ARG_PAGE_NUMBER);
			Log.d(TAG, "onCreate" + _pageNumber);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView" + _pageNumber);
		View signUpFragmentView;

		switch(_pageNumber) {
			case FIRST_SIGN_UP_PAGE:
				signUpFragmentView = inflater.inflate(R.layout.fragment_sign_up_first, container, false);

				_agreeTermsOfUseCheckBox = (CheckBox) signUpFragmentView.findViewById(R.id.checkBox_agree_terms_of_use);
				_agreeTermsOfUseCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(isChecked)
							_goToNextPageButton.setEnabled(true);
					}
				});

				_goToNextPageButton = (Button) signUpFragmentView.findViewById(R.id.btn_go_page_next);
				_goToNextPageButton.setEnabled(false);
				_goToNextPageButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(_agreeTermsOfUseCheckBox.isChecked()) {
							_mSignUpFragmentListener.onClickGoToNextPageButton();
						}
					}
				});

				break;
			case SECOND_SIGN_UP_PAGE:
				signUpFragmentView = inflater.inflate(R.layout.fragment_sign_up_second, container, false);

				__emailAddressEditText = (EditText) signUpFragmentView.findViewById(R.id.editText_email_sign_up);

				_authenticateEmailAddressButton = (Button) signUpFragmentView.findViewById(R.id.btn_authenticate_email_address);
				_authenticateEmailAddressButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!isValidEmail(__emailAddressEditText.getText().toString()))
							Toast.makeText(getActivity(), R.string.invalid_email_pattern, Toast.LENGTH_SHORT).show();
						else {
							// send email to user's email address
							_emailAddress = __emailAddressEditText.getText().toString();
							String temp[] = _emailAddress.split("@");
							_base64EncodedEmailAddressAuthenticationCode = Base64.encodeToString(temp[0].getBytes(), Base64.NO_WRAP);
							new HttpPostRequestTask(_emailAddress).execute(MTPLEASE_URL + "member/");
						}
					}
				});

				__emailAddressAuthenticationCodeEditText = (EditText) signUpFragmentView.findViewById(R.id.editText_email_authentication_code);

				_confirmEmailAddressAuthenticationCodeButton = (Button) signUpFragmentView.findViewById(R.id.btn_confirm_email_address_authentication_code);
				_confirmEmailAddressAuthenticationCodeButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(_base64EncodedEmailAddressAuthenticationCode.equals(__emailAddressAuthenticationCodeEditText.getText().toString())) {
							Toast.makeText(getActivity(), R.string.email_address_authenticated, Toast.LENGTH_SHORT).show();
							_confirmEmailAddressAuthenticationCodeButton.setText(R.string.authentication_complete);
							_confirmEmailAddressAuthenticationCodeButton.setEnabled(false);
							_isEmailAddressAuthenticated = true;
						}
						else {
							Toast.makeText(getActivity(), R.string.wrong_authentication_code, Toast.LENGTH_SHORT).show();
							_isEmailAddressAuthenticated = false;
						}
					}
				});

				_passwordEditText = (EditText) signUpFragmentView.findViewById(R.id.editText_password_sign_up);

				_passwordConfirmEditText = (EditText) signUpFragmentView.findViewById(R.id.editText_confirm_password_sign_up);

				_goToNextPageButton = (Button) signUpFragmentView.findViewById(R.id.btn_go_page_next);
				_goToNextPageButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (_isEmailAddressAuthenticated) {
							String password = _passwordEditText.getText().toString();
							String passwordConfirm = _passwordConfirmEditText.getText().toString();

							if(password.equals(passwordConfirm)) {
								_mSignUpFragmentListener.onClickGoToNextPageButton(_emailAddress, password);
							}
							else {
								Toast.makeText(getActivity(), R.string.password_does_not_match, Toast.LENGTH_SHORT).show();
								_passwordEditText.requestFocus();
								_passwordEditText.setText("");
								_passwordConfirmEditText.setText("");
							}
						} else {
							Toast.makeText(getActivity(), R.string.email_address_is_not_authenticated, Toast.LENGTH_SHORT).show();
							__emailAddressEditText.requestFocus();
						}
					}
				});

				_goToPreviousPageButton = (Button) signUpFragmentView.findViewById(R.id.btn_go_page_previous);
				_goToPreviousPageButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						_mSignUpFragmentListener.onClickGoToPreviousPageButton(SECOND_SIGN_UP_PAGE);
					}
				});

				break;
			case THIRD_SIGN_UP_PAGE:
				signUpFragmentView = inflater.inflate(R.layout.fragment_sign_up_third, container, false);

				_userNameEditText = (EditText) signUpFragmentView.findViewById(R.id.editText_name_user);

				_schoolNameEditText = (EditText) signUpFragmentView.findViewById(R.id.editText_name_school);

				_nicknameEditText = (EditText) signUpFragmentView.findViewById(R.id.editText_nickname);

				/*_checkNicknameDuplicationButton = (Button) signUpFragmentView.findViewById(R.id.btn_check_duplication_nickname);
				_checkNicknameDuplicationButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});*/

				_signUpButton = (Button) signUpFragmentView.findViewById(R.id.btn_sign_up);
				_signUpButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String userName = _userNameEditText.getText().toString();
						String schoolName = _schoolNameEditText.getText().toString();
						String nickname = _nicknameEditText.getText().toString();

						if(userName.equals("")) {
							Toast.makeText(getActivity(), R.string.please_type_name, Toast.LENGTH_SHORT).show();
							_userNameEditText.requestFocus();
						} else if(schoolName.equals("")) {
							Toast.makeText(getActivity(), R.string.please_type_school_name, Toast.LENGTH_SHORT).show();
							_schoolNameEditText.requestFocus();
						} else if(nickname.equals("")) {
							Toast.makeText(getActivity(), R.string.please_type_nickname, Toast.LENGTH_SHORT).show();
							_nicknameEditText.requestFocus();
						} else {
							_mSignUpFragmentListener.onClickSignUpButton(userName, schoolName, nickname);
						}
					}
				});

				_goToPreviousPageButton = (Button) signUpFragmentView.findViewById(R.id.btn_go_page_previous);
				_goToPreviousPageButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						_mSignUpFragmentListener.onClickGoToPreviousPageButton(THIRD_SIGN_UP_PAGE);
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
			_mSignUpFragmentListener = (OnSignUpFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		_mSignUpFragmentListener = null;
	}

	private class HttpPostRequestTask extends AsyncTask<String, Integer, String> {

		private String userEmailAddress;

		public HttpPostRequestTask(String userEmailAddress) {
			this.userEmailAddress = userEmailAddress;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			_mSignUpFragmentListener.onStartSendingAuthenticationCode(_authenticateEmailAddressButton);
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

			_mSignUpFragmentListener.onEndSendingAuthenticationCode();

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
		public void onClickGoToNextPageButton(String _emailAddress, String password);
		public void onClickSignUpButton(String userName, String schoolName, String nickname);
		public void onClickGoToPreviousPageButton(int _pageNumber);
	}

}
