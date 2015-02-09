package com.owo.mtplease;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by In-Ho on 2015-02-05.
 */
public class SignUpModelController {

	private static final String TAG = "SignUpModelController";
	private static final String MTPLEASE_URL = "http://mtplease.herokuapp.com/";
	public static final int SIGN_UP_SUCCESS = 1;
	public static final int SIGN_UP_FAILED = 0;
	public static final int SERVER_ERROR = -1;

	public SignUpModelControllerListener mSignUpModelControllerListener;

	private String emailAddress;
	private String password;

	private String userName;
	private String schoolName;
	private String nickname;

	public SignUpModelController(Activity activity) {
		try {
			mSignUpModelControllerListener = (SignUpModelControllerListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement SignUpModelControllerListener");
		}
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	private String getJSONObjectString() {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("user_id", emailAddress);
			jsonObject.put("user_password", password);
			jsonObject.put("user_nickname", nickname);
			jsonObject.put("user_name", userName);
			jsonObject.put("user_school", schoolName);

			return jsonObject.toString();
		} catch(JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void signUp() {
		new HttpPutRequestTask().execute(MTPLEASE_URL + "member/");
	}

	private class HttpPutRequestTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... urls) {
			try {
				Log.d(TAG, getJSONObjectString());
				HttpClient httpClient = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 5000);
				HttpPut httpPut = new HttpPut(urls[0]);
				StringEntity stringEntity = new StringEntity(getJSONObjectString());
				stringEntity.setContentType("application/json;charset=UTF-8");
				httpPut.setHeader("Content-Type", "application/json");
				httpPut.setHeader("Access", "application/json");
				httpPut.setEntity(stringEntity);

				HttpResponse responsePost = httpClient.execute(httpPut);
				HttpEntity resEntityPost = responsePost.getEntity();

				if (resEntityPost != null) {
					return EntityUtils.toString(resEntityPost);
				}
			} catch(UnsupportedEncodingException e) {
				e.printStackTrace();
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
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String jsonString) {
			super.onPostExecute(jsonString);

			try {
				Log.d(TAG, jsonString);
				JSONObject responseJSONObject = new JSONObject(jsonString);
				String status = responseJSONObject.optString("status");

				if (status.equals("success")) {
					mSignUpModelControllerListener.onFinishSignUp(SIGN_UP_SUCCESS);
					Log.d(TAG, "sign up success");
				} else {
					mSignUpModelControllerListener.onFinishSignUp(SIGN_UP_FAILED);
					Log.d(TAG, "sign up failed");
				}
			} catch (JSONException e) {
				mSignUpModelControllerListener.onFinishSignUp(SERVER_ERROR);
				e.printStackTrace();
			}
		}
	}

	public interface SignUpModelControllerListener {
		public void onFinishSignUp(int statusCode);
	}

}
