package com.owo.mtplease;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BootingActivity extends ActionBarActivity {

	private SignUpFragment mSignUpFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_booting);
		if (savedInstanceState == null) {
			mSignUpFragment = SignUpFragment.newInstance();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, mSignUpFragment).commit();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_booting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class SignUpFragment extends Fragment {

		private static final String TAG_CONFIRM_CODE = "SIGNUP_FRAGMENT_CONFIRM_CODE";
		private static final String MTPLEASE_URL = "";

		private EditText subDirectoryChangeEditText;
		private Button subDirectoryChangeButton;
		private EditText emailEditText;
		private Button sendCodeToSchoolButton;
		private Button sendCodeToHomeButton;
		private TextView confirmCodeTextView;
		private EditText confirmCodeEditText;
		private Button confirmCodeButton;

		private String serverAddressSchool = "http://192.168.0.38:9999";
		private String serverAddressHome = "http://192.168.0.9:9999";
		private String serverDefaultSubDirectory = "/members/email/";
		private String serverSubDirectory = "";
		private String emailAddress;
		private String base64EncodedCode;
		private String receivedConfirmCode;

		public static SignUpFragment newInstance() {
			SignUpFragment fragment = new SignUpFragment();
			Bundle args = new Bundle();
			return fragment;
		}

		public SignUpFragment() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			View signUpFragmentView = inflater.inflate(R.layout.fragment_booting, container, false);

			subDirectoryChangeEditText = (EditText) signUpFragmentView.findViewById(R.id.editText_subdirectory_server);

			subDirectoryChangeButton = (Button) signUpFragmentView.findViewById(R.id.btn_change_subdirectory);
			subDirectoryChangeButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(!subDirectoryChangeEditText.getText().toString().equals("")) {
						serverSubDirectory = subDirectoryChangeEditText.getText().toString();
						Toast.makeText(getActivity(), "현재 주소(학교):" + serverAddressSchool + serverSubDirectory + "\n현재 주소(집):"
								+ serverAddressHome + serverSubDirectory, Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getActivity(), "현재 주소(학교):" + serverAddressSchool + serverDefaultSubDirectory + "\n현재 주소(집):"
								+ serverAddressHome + serverDefaultSubDirectory, Toast.LENGTH_SHORT).show();
					}
				}
			});

			emailEditText = (EditText) signUpFragmentView.findViewById(R.id.editText_input_email_for_looking_for_password);

			confirmCodeTextView = (TextView) signUpFragmentView.findViewById(R.id.textView_confirm_code);

			sendCodeToSchoolButton = (Button) signUpFragmentView.findViewById(R.id.btn_send_school);
			sendCodeToSchoolButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					emailAddress = emailEditText.getText().toString();
					String temp[] = emailAddress.split("@");
					base64EncodedCode = Base64.encodeToString(temp[0].getBytes(), 0);
					Toast.makeText(getActivity(), "Encoded Code:" + base64EncodedCode, Toast.LENGTH_SHORT).show();
					Log.d(TAG_CONFIRM_CODE, temp[0] + " / " + base64EncodedCode);
					if(!serverSubDirectory.equals(""))
						new HttpPostRequestTask().execute(serverAddressSchool + serverSubDirectory);
					else
						new HttpPostRequestTask().execute(serverAddressSchool + serverDefaultSubDirectory);
				}
			});

			sendCodeToHomeButton = (Button) signUpFragmentView.findViewById(R.id.btn_send_home);
			sendCodeToHomeButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					emailAddress = emailEditText.getText().toString();
					String temp[] = emailAddress.split("@");
					base64EncodedCode = Base64.encodeToString(temp[0].getBytes(), 0);
					Toast.makeText(getActivity(), "Encoded Code:" + base64EncodedCode, Toast.LENGTH_SHORT).show();
					Log.d(TAG_CONFIRM_CODE, temp[0] + " / " + base64EncodedCode);
					if(!serverSubDirectory.equals(""))
						new HttpPostRequestTask().execute(serverAddressHome + serverSubDirectory);
					else
						new HttpPostRequestTask().execute(serverAddressHome + serverDefaultSubDirectory);
				}
			});


			confirmCodeEditText = (EditText) signUpFragmentView.findViewById(R.id.editText_code_confirm);

			confirmCodeButton = (Button) signUpFragmentView.findViewById(R.id.btn_confirm_code);
			confirmCodeButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(base64EncodedCode.equals(confirmCodeEditText.getText().toString())) {
						Toast.makeText(getActivity(), R.string.confirm_code_checked, Toast.LENGTH_SHORT).show();
						Intent mainActivtyIntent = new Intent(getActivity(), MainActivity.class);
						startActivity(mainActivtyIntent);
					} else if(confirmCodeEditText.getText().toString().equals("")) {
						Toast.makeText(getActivity(), R.string.please_type_confirm_code,Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getActivity(), R.string.wrong_confirm_code, Toast.LENGTH_SHORT).show();
					}
				}
			});

			return signUpFragmentView;
		}

		private class HttpPostRequestTask extends AsyncTask<String, Integer, String> {

			private String error = "연결 문제 없음";

			@Override
			protected String doInBackground(String... urls) {
				try {
					HttpClient mHttpClient = new DefaultHttpClient();
					HttpConnectionParams.setConnectionTimeout(mHttpClient.getParams(), 5000);
					HttpPost mHttpPost = new HttpPost(urls[0]);
					List params = new ArrayList();
					params.add(new BasicNameValuePair("user_id", emailAddress));
					UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
					mHttpPost.setEntity(ent);
					HttpResponse responsePost = mHttpClient.execute(mHttpPost);
					HttpEntity resEntityPost = responsePost.getEntity();

					if (resEntityPost != null) {
						return EntityUtils.toString(resEntityPost);
					}
				} catch(ClientProtocolException e) {
					error = e.toString();
					e.printStackTrace();
				} catch(IOException e) {
					error = e.toString();
					e.printStackTrace();
				} catch(Exception e) {
					error = e.toString();
					e.printStackTrace();
				}
				return "";
			}

			@Override
			protected void onPostExecute(String jsonString) {
				super.onPostExecute(jsonString);

				Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();

				try {
					receivedConfirmCode = new JSONObject(jsonString).getString("confirm_code");
					confirmCodeTextView.setText(receivedConfirmCode);
				} catch(Exception e) {
					Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		}
	}

}
