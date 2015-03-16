package com.owo.mtplease;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by In-Ho on 2015-02-17.
 */
public class DiskLruImageCache implements ImageLoader.ImageCache {

	private static final String TAG = "DiskLruImageCache";

	private DiskLruCache _mDiskCache;
	private Bitmap.CompressFormat _mCompressFormat = Bitmap.CompressFormat.JPEG;
	private static int IO_BUFFER_SIZE = 8 * 1024;
	private int _mCompressQuality = 70;
	private static final int APP_VERSION = 1;
	private static final int VALUE_COUNT = 1;

	public DiskLruImageCache(Context context, String uniqueName, int diskCacheSize,
							 Bitmap.CompressFormat compressFormat, int quality) {
		try {
			final File diskCacheDir = _getDiskCacheDir(context, uniqueName);
			_mDiskCache = DiskLruCache.open(diskCacheDir, APP_VERSION, VALUE_COUNT, diskCacheSize);
			_mCompressFormat = compressFormat;
			_mCompressQuality = quality;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean _writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor)
			throws IOException {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(editor.newOutputStream(0), IO_BUFFER_SIZE);
			return bitmap.compress(_mCompressFormat, _mCompressQuality, out);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	private File _getDiskCacheDir(Context context, String uniqueName) {

		final String cachePath = context.getExternalFilesDir(null).getPath();
		File imageFile = new File(cachePath + File.separator + uniqueName);
		Log.d(TAG, "File Path: " + imageFile.getPath());
		if(!imageFile.mkdirs())
			Log.e(TAG, "Fail to create directory of the cache");
		return imageFile;
	}

	@Override
	public void putBitmap(String key, Bitmap data) {

		key = _createKey(key);
//		Log.d(TAG, "putBitmap() called with " + key);

		DiskLruCache.Editor editor = null;
		try {
			editor = _mDiskCache.edit(key);
			if (editor == null) {
				return;
			}

			if (_writeBitmapToFile(data, editor)) {
				_mDiskCache.flush();
				editor.commit();
				if (BuildConfig.DEBUG) {
					Log.d("cache_test_DISK_", "image put on disk cache " + key);
				}
			} else {
				editor.abort();
				if (BuildConfig.DEBUG) {
					Log.d("cache_test_DISK_", "ERROR on: image put on disk cache " + key);
				}
			}
		} catch (IOException e) {
			if (BuildConfig.DEBUG) {
				Log.d("cache_test_DISK_", "ERROR on: image put on disk cache " + key);
			}
			try {
				if (editor != null) {
					editor.abort();
				}
			} catch (IOException ignored) {
			}
		}

	}

	@Override
	public Bitmap getBitmap(String key) {

		key = _createKey(key);
//		Log.d(TAG, "getBitmap() called with " + key);

		Bitmap bitmap = null;
		DiskLruCache.Snapshot snapshot = null;
		try {

			snapshot = _mDiskCache.get(key);
			if (snapshot == null) {
				return null;
			}
			final InputStream in = snapshot.getInputStream(0);
			if (in != null) {
				final BufferedInputStream buffIn =
						new BufferedInputStream(in, IO_BUFFER_SIZE);
				bitmap = BitmapFactory.decodeStream(buffIn);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (snapshot != null) {
				snapshot.close();
			}
		}

		if (BuildConfig.DEBUG) {
			Log.d("cache_test_DISK_", bitmap == null ? "" : "image read from disk " + key);
		}

		return bitmap;

	}

	public boolean containsKey(String key) {

		key = _createKey(key);
//		Log.d(TAG, "containsKey() called with " + key);

		boolean contained = false;
		DiskLruCache.Snapshot snapshot = null;
		try {
			snapshot = _mDiskCache.get(key);
			contained = snapshot != null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (snapshot != null) {
				snapshot.close();
			}
		}

		return contained;

	}

	public void clearCache() {
		if (BuildConfig.DEBUG) {
			Log.d("cache_test_DISK_", "disk cache CLEARED");
		}
		try {
			_mDiskCache.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File getCacheFolder() {
		return _mDiskCache.getDirectory();
	}

	/**
	 * Creates a unique cache key based on a url value
	 *
	 * @param url url to be used in key creation
	 * @return cache key value
	 */
	private String _createKey(String url) {
		Log.d(TAG, "creating unique key for each img file");
		return String.valueOf(url.hashCode());
	}
}
