package com.owo.mtplease;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmailLoginFragment.OnEmailLoginFragmentListener} interface
 * to handle interaction events.
 * Use the {@link EmailLoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmailLoginFragment extends Fragment {

	private static final String TAG = "EmailLoginFragment";
	private static final String MTPLEASE_LOGIN_URL = "http://mtplease.herokuapp.com/member/session";
	private static final int USER_ID = 0;
	private static final int USER_PASSWORD = 1;

	private EditText _emailInputEditText;
	private EditText _passwordInputEditText;
	private FrameLayout _emailLoginButton;
	private CheckBox _autoLoginCheckBox;
	private TextView _lookForPasswordTextView;

	private OnEmailLoginFragmentListener _mEmailLoginFragmentListener;

	private SharedPreferences _mSharedPreferences;
	private SharedPreferences.Editor _mSharedPreferencesEditor;

	private boolean isAutoLoginChecked;

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment EmailLoginFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static EmailLoginFragment newInstance() {
		EmailLoginFragment fragment = new EmailLoginFragment();
		return fragment;
	}

	public EmailLoginFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View emailLoginFragmentView = inflater.inflate(R.layout.fragment_email_login, container, false);

		_mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

		_mSharedPreferencesEditor = _mSharedPreferences.edit();

		isAutoLoginChecked = _mSharedPreferences.getBoolean(getResources().getString(R.string.pref_auto_login_key), false);

		_emailInputEditText = (EditText) emailLoginFragmentView.findViewById(R.id.editText_login_email);
		if(isAutoLoginChecked)
			_emailInputEditText.setText(_mSharedPreferences.getString(getResources().getString(R.string.pref_user_id_key), ""));

		_passwordInputEditText = (EditText) emailLoginFragmentView.findViewById(R.id.editText_login_email_password);
		if(isAutoLoginChecked)
			_passwordInputEditText.setText(_mSharedPreferences.getString(getResources().getString(R.string.pref_user_password_key), ""));

		_autoLoginCheckBox = (CheckBox) emailLoginFragmentView.findViewById(R.id.checkBox_login_auto);
		if(isAutoLoginChecked)
			_autoLoginCheckBox.setChecked(isAutoLoginChecked);

		_autoLoginCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isAutoLoginChecked = isChecked;

				_mSharedPreferencesEditor.putBoolean(getResources().getString(R.string.pref_auto_login_key), isChecked);
				_mSharedPreferencesEditor.commit();
			}
		});

		_emailLoginButton = (FrameLayout) emailLoginFragmentView.findViewById(R.id.frameLayout_btn_login_email);
		_emailLoginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// save email address and password in the SharedPreferences if checkbox is checked
				_mSharedPreferencesEditor.putString(getResources().getString(R.string.pref_user_id_key),
						_emailInputEditText.getText().toString());
				_mSharedPreferencesEditor.putString(getResources().getString(R.string.pref_user_password_key),
						_passwordInputEditText.getText().toString());
				_mSharedPreferencesEditor.commit();

				// do login
				new HttpPostRequestTask(_emailInputEditText.getText().toString(),
						_passwordInputEditText.getText().toString()).execute(MTPLEASE_LOGIN_URL);

			}
		});

		_lookForPasswordTextView = (TextView) emailLoginFragmentView.findViewById(R.id.textView_forgot_password);
		_lookForPasswordTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// call find password fragment
				_mEmailLoginFragmentListener.onClickLookForPasswordButton();
			}
		});

		return emailLoginFragmentView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			_mEmailLoginFragmentListener = (OnEmailLoginFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnEmailLoginFragmentListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		_mEmailLoginFragmentListener = null;
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p/>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnEmailLoginFragmentListener {
		// TODO: Update argument type and name
		public void onStartLogin();
		public void onEmailLoginSuccess();
		public void onEmailLoginFailed();
		public void onClickLookForPasswordButton();
	}

	private class HttpPostRequestTask extends AsyncTask<String, Integer, String> {

		private String userId;
		private String userPassword;

		public HttpPostRequestTask(String userId, String userPassword) {
			this.userId = userId;
			this.userPassword = userPassword;
		}

		@Override
		protected void onPreExecute() {
			_mEmailLoginFragmentListener.onStartLogin();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... urls) {
			try {
				Log.d(TAG, "1");
				HttpClient mHttpClient = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(mHttpClient.getParams(), 5000);
				HttpPost mHttpPost = new HttpPost(urls[0]);
				List params = new ArrayList();
				params.add(new BasicNameValuePair("user_id", userId));
				params.add(new BasicNameValuePair("user_password", userPassword));
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
				mHttpPost.setEntity(ent);
				HttpResponse responsePost = mHttpClient.execute(mHttpPost);
				HttpEntity resEntityPost = responsePost.getEntity();
				Log.d(TAG, "2");

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
			return "";
		}

		@Override
		protected void onPostExecute(String jsonString) {
			super.onPostExecute(jsonString);
			try {
				Log.d(TAG, "3");

				Log.d(TAG, jsonString);

				JSONObject responseJSONObject = new JSONObject(jsonString);
				String status = responseJSONObject.optString("status");

				Log.d(TAG, status.toString());

				if(status.equals("success")) {
					Log.d(TAG, "4");
					Toast.makeText(getActivity(), R.string.login_successful, Toast.LENGTH_SHORT).show();
					_mEmailLoginFragmentListener.onEmailLoginSuccess();
				} else {
					Log.d(TAG, "5");
					Toast.makeText(getActivity(), R.string.login_fail_due_to_inconsistent_user_info, Toast.LENGTH_SHORT).show();
					_mEmailLoginFragmentListener.onEmailLoginFailed();
				}

			} catch(JSONException e) {
				//////////////////////////////////////////////////////////////////
				_mEmailLoginFragmentListener.onEmailLoginFailed();
				Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}

}
