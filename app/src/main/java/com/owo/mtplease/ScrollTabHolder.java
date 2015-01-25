package com.owo.mtplease;

import android.support.v7.widget.RecyclerView;

/**
 * Created by In-Ho on 2014-12-27.
 */
public interface ScrollTabHolder {
	void adjustScroll(int scrollHeight);

	void onScroll(RecyclerView recyclerView, int firstVisibleItem, int pagePosition, int visibleFragment);
}
