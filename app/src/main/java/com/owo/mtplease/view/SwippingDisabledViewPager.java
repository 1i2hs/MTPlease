package com.owo.mtplease.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by In-Ho on 2015-02-05.
 */
public class SwippingDisabledViewPager extends ViewPager {

	public SwippingDisabledViewPager(Context context) {
		super(context);
	}

	public SwippingDisabledViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		// Never allow swiping to switch between pages
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Never allow swiping to switch between pages
		return false;
	}
}
