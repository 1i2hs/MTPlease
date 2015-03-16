package com.owo.mtplease;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;


/**
 * Created by In-Ho on 2015-02-22.
 * An extension to Application class to provide tracker for analytics purposes. Having the tracker
 * instances here allows all the activities to access the same tracker instances. The trackers can
 * be initialised on startup or when they are required based on performance requirements.
 */
public class Analytics extends Application {
	// The following line should be changed to include the correct property id.
	private static final String PROPERTY_ID = "UA-59976665-1";

	private Tracker _mTracker;

	public Analytics() {
		super();
	}

	public synchronized Tracker getTracker() {
		if (_mTracker == null) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
			_mTracker = analytics.newTracker(PROPERTY_ID);
		}
		return _mTracker;
	}
}
