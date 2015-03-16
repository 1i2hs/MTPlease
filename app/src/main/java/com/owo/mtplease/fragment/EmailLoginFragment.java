package com.owo.mtplease.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.owo.mtplease.MTPleaseStringRequest;
import com.owo.mtplease.R;
import com.owo.mtplease.ServerCommunicationManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


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

	private boolean _isAutoLoginChecked;

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

		_isAutoLoginChecked = _mSharedPreferences.getBoolean(getResources().getString(R.string.pref_auto_login_key), false);

		_emailInputEditText = (EditText) emailLoginFragmentView.findViewById(R.id.editText_login_email);
		if(_isAutoLoginChecked)
			_emailInputEditText.setText(_mSharedPreferences.getString(getResources().getString(R.string.pref_user_id_key), ""));

		_passwordInputEditText = (EditText) emailLoginFragmentView.findViewById(R.id.editText_login_email_password);
		if(_isAutoLoginChecked)
			_passwordInputEditText.setText(_mSharedPreferences.getString(getResources().getString(R.string.pref_user_password_key), ""));

		_autoLoginCheckBox = (CheckBox) emailLoginFragmentView.findViewById(R.id.checkBox_login_auto);
		if(_isAutoLoginChecked)
			_autoLoginCheckBox.setChecked(_isAutoLoginChecked);

		_autoLoginCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				_isAutoLoginChecked = isChecked;

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
/*				new HttpPostRequestTask(_emailInputEditText.getText().toString(),
						_passwordInputEditText.getText().toString()).execute(MTPLEASE_LOGIN_URL);*/

				MTPleaseStringRequest postRequest = new MTPleaseStringRequest(Request.Method.POST, MTPLEASE_LOGIN_URL, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d(TAG, response);
						try {
							JSONObject responseJSONObject = new JSONObject(response);
							String status = responseJSONObject.optString("status");

							if(status.equals("success")) {
								Toast.makeText(getActivity(), R.string.login_successful, Toast.LENGTH_SHORT).show();
								_mEmailLoginFragmentListener.onEmailLoginSuccess();
							} else {
								Toast.makeText(getActivity(), R.string.login_fail_due_to_inconsistent_user_info, Toast.LENGTH_SHORT).show();
								_mEmailLoginFragmentListener.onEmailLoginFailed();
							}
						} catch(JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d(TAG, error.toString());
						Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
						_mEmailLoginFragmentListener.onEmailLoginFailed();
					}
				}) {
					@Override
					protected Map<String, String> getParams() {
						Map<String, String> params = new HashMap<String, String>();
						params.put("user_id", _emailInputEditText.getText().toString());
						params.put("user_password", _passwordInputEditText.getText().toString());

						return params;
					}
				};
				postRequest.setContext(getActivity());

				_mEmailLoginFragmentListener.onStartLogin();
				ServerCommunicationManager.getInstance(getActivity()).addToRequestQueue(postRequest);

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

	public interface OnEmailLoginFragmentListener {
		public void onStartLogin();
		public void onEmailLoginSuccess();
		public void onEmailLoginFailed();
		public void onClickLookForPasswordButton();
	}

}
