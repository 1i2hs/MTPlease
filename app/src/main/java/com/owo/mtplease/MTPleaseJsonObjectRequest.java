package com.owo.mtplease;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by In-Ho on 2015-02-09.
 */
public class MTPleaseJsonObjectRequest extends JsonObjectRequest {

	private static final String TAG = "MTPleaseJsonObjectRequest";

	private Context _mContext;

	public MTPleaseJsonObjectRequest(int method, String url, JSONObject jsonRequest,
									  Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
	}

	public MTPleaseJsonObjectRequest(String url, JSONObject jsonRequest,
									 Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
		super(url, jsonRequest, listener, errorListener);
	}

	public void setContext(Context context) {
		_mContext = context;
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		Map<String, String> headers = response.headers;
		if (headers.containsKey("Set-Cookie")) {
			Log.d(TAG , headers.toString());
			Log.d(TAG + "[parseNetworkResponse()]", headers.get("Set-Cookie"));
			PreferenceManager.getDefaultSharedPreferences(_mContext).edit()
					.putString(_mContext.getResources().getString(R.string.pref_saved_cookie),
							headers.get("Set-Cookie")).commit();
		}
		return super.parseNetworkResponse(response);
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> customHeaders = super.getHeaders();
		Map<String, String> newHeaders = new HashMap<String, String>();
		newHeaders.putAll(customHeaders);
		String cookieString = PreferenceManager.getDefaultSharedPreferences(_mContext)
				.getString(_mContext.getResources().getString(R.string.pref_saved_cookie), "");
		Log.d(TAG + "[getHeaders()]", cookieString);
		if (cookieString.length() > 0) {
			newHeaders.put("cookie", cookieString);
		}
		return newHeaders;
	}
}
