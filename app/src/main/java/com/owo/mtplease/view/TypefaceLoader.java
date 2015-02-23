package com.owo.mtplease.view;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Type;

/**
 * Created by In-Ho on 2015-02-10.
 */
public class TypefaceLoader {

	private static TypefaceLoader typefaceLoader;
	private Typeface mTypeface;
	private static Context context;
	private static final String TYPEFACE_NAME = "fonts/main_font.ttf";

	private TypefaceLoader(Context context) {
		this.context = context;
	}

	public static TypefaceLoader getInstance(Context context) {
		if(typefaceLoader == null)
			typefaceLoader = new TypefaceLoader(context);

		return typefaceLoader;
	}

	public Typeface getTypeface() {
		if(mTypeface == null)
			mTypeface = Typeface.createFromAsset(context.getAssets(), TYPEFACE_NAME);

		return mTypeface;
	}
}
