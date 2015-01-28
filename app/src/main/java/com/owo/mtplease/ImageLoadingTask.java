package com.owo.mtplease;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by In-Ho on 2015-01-27.
 */
public class ImageLoadingTask extends AsyncTask<String, Integer, Bitmap> {
	private static final String TAG = "ImageLoadingTask";

	private static final int IMAGE_LOADING_SUCCEEDED = 1;
	private static final int IMAGE_LOADING_FAILED = -1;
	private final WeakReference imageViewReference;
	private ProgressBar imageLoadingProgressBar;
	private int isImageLoaded;

	public ImageLoadingTask(ImageView roomImageView, ProgressBar loadingProgressBar) {
		imageViewReference = new WeakReference(roomImageView);
		this.imageLoadingProgressBar = loadingProgressBar;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// configure progress bar
		imageLoadingProgressBar.setVisibility(View.VISIBLE);
		imageLoadingProgressBar.setProgress(0);
		// end of the configuration of the progress bar
	}

	@Override
	protected Bitmap doInBackground(String... urls) {
		try {
			isImageLoaded = IMAGE_LOADING_FAILED;

			HttpEntity resEntityGet = getHttpEntityFromServer(urls[0]);

			if(resEntityGet != null) {
				Log.i(TAG, "Receiving room image from the server succeeded!");
				InputStream inputStream = null;
				try {
					inputStream = resEntityGet.getContent();
					final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					isImageLoaded = IMAGE_LOADING_SUCCEEDED;
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					resEntityGet.consumeContent();
				}
			}
		} catch(IOException e) {
			Log.i(TAG,"Receiving room image from the server failed....");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Bitmap roomImage) {
		super.onPostExecute(roomImage);
		if(isImageLoaded == IMAGE_LOADING_FAILED) {
			roomImage = null;
		}

		if(imageViewReference != null) {
			ImageView roomImageView = (ImageView) imageViewReference.get();

			if(roomImageView != null) {
				if(roomImage != null) {
					roomImageView.setImageBitmap(roomImage);
					roomImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				}
				else {
					// image for replacement when there is no image for the room or when the loading of the image
					// fails.
					roomImageView.setImageBitmap(null);
				}
			}
		}

		// configure progress bar
		imageLoadingProgressBar.setVisibility(View.GONE);
		imageLoadingProgressBar.setProgress(100);
		// end of the configuration of the progress bar
	}

	private HttpEntity getHttpEntityFromServer(String url) throws IOException {
		try {
			HttpClient mHttpClient = new DefaultHttpClient();
			HttpGet mHttpGet = new HttpGet(url);
			HttpConnectionParams.setConnectionTimeout(mHttpClient.getParams(), 5000);
			HttpResponse mHttpResponseGet = mHttpClient.execute(mHttpGet);
			HttpEntity resEntityGet = mHttpResponseGet.getEntity();
			return resEntityGet;
		} catch(ClientProtocolException e) {
			Log.e(TAG, "ClientProtocolException");
			e.printStackTrace();
		}
		return null;
	}
}
