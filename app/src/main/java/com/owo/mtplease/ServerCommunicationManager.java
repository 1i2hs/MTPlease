package com.owo.mtplease;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.io.File;

/**
 * Created by In-Ho on 2015-02-05.
 */
public class ServerCommunicationManager {

	private static final String TAG = "ServerCommunicationManager";

	private static ServerCommunicationManager serverCommunicationInstance;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	private DiskLruImageCache mImageCache;
	private static Context mContext;

	private ServerCommunicationManager(Context context) {
		mContext = context;
		mRequestQueue = getRequestQueue();
	}

	public static synchronized ServerCommunicationManager getInstance(Context context) {
		if (serverCommunicationInstance == null) {
			serverCommunicationInstance = new ServerCommunicationManager(context);
		}
		return serverCommunicationInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			// getApplicationContext() is key, it keeps you from leaking the
			// Activity or BroadcastReceiver if someone passes one in.
			mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req) {
		getRequestQueue().add(req);
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	/**
	 * Initializer for the manager. Must be called prior to use.
	 *
	 * @param uniqueName     name for the cache location
	 * @param cacheSize      max size for the cache
	 * @param compressFormat file type compression format.
	 * @param quality
	 */
	public void initImageLoader(String uniqueName, int cacheSize, Bitmap.CompressFormat compressFormat, int quality) {

		mImageCache = new DiskLruImageCache(mContext, uniqueName, cacheSize, compressFormat, quality);

		mImageLoader = new ImageLoader(getRequestQueue(), mImageCache);

		Log.d(TAG, "initialization of ImageLoader completed");
	}

	public Bitmap getBitmap(String url) {
		try {
			return mImageCache.getBitmap(url);
		} catch (NullPointerException e) {
			throw new IllegalStateException("Disk Cache Not initialized");
		}
	}

	public void putBitmap(String url, Bitmap bitmap) {
		try {
			if(!mImageCache.containsKey(url))
				mImageCache.putBitmap(url, bitmap);
		} catch (NullPointerException e) {
			throw new IllegalStateException("Disk Cache Not initialized");
		}
	}

	public boolean containsImage(String url) {
		return mImageCache.containsKey(url);
	}


	/**
	 * Executes and image load
	 *
	 * @param url      location of image
	 * @param listener Listener for completion
	 */
	public void getImage(String url, ImageLoader.ImageListener listener) {
//		Log.d(TAG, "loading img from the server");
		mImageLoader.get(url, listener);
	}

	public void clearCache() {
//		Log.d(TAG, "Clearing cached imgs inside the cached img directory");
		mImageCache.clearCache();
		Toast.makeText(mContext, R.string.clear_cache_complete, Toast.LENGTH_SHORT).show();
	}

	public File getCacheFolder() {
		return mImageCache.getCacheFolder();
	}
}
