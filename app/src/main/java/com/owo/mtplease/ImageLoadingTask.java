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
	private final WeakReference _imageViewReference;
	private ProgressBar _imageLoadingProgressBar;
	private int _isImageLoaded;

	public ImageLoadingTask(ImageView roomImageView, ProgressBar loadingProgressBar) {
		_imageViewReference = new WeakReference(roomImageView);
		this._imageLoadingProgressBar = loadingProgressBar;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// configure progress bar
		_imageLoadingProgressBar.setVisibility(View.VISIBLE);
		_imageLoadingProgressBar.setProgress(0);
		// end of the configuration of the progress bar
	}

	@Override
	protected Bitmap doInBackground(String... urls) {
		try {
			_isImageLoaded = IMAGE_LOADING_FAILED;

			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(urls[0]);
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 5000);
			HttpResponse mHttpResponseGet = httpClient.execute(httpGet);
			HttpEntity resEntityGet = mHttpResponseGet.getEntity();

			if(resEntityGet != null) {
				Log.i(TAG, "Receiving room image from the server succeeded!");
				InputStream inputStream = null;
				try {
					inputStream = resEntityGet.getContent();
					final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					_isImageLoaded = IMAGE_LOADING_SUCCEEDED;
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					resEntityGet.consumeContent();
				}
			}
		} catch(ClientProtocolException e) {
			Log.e(TAG, "ClientProtocolException");
			e.printStackTrace();
		} catch(IOException e) {
			Log.i(TAG, "Receiving room image from the server failed....");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Bitmap roomImage) {
		super.onPostExecute(roomImage);
		if(_isImageLoaded == IMAGE_LOADING_FAILED) {
			roomImage = null;
		}

		if(_imageViewReference != null) {
			ImageView roomImageView = (ImageView) _imageViewReference.get();

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
		_imageLoadingProgressBar.setVisibility(View.GONE);
		_imageLoadingProgressBar.setProgress(100);
		// end of the configuration of the progress bar
	}

}
