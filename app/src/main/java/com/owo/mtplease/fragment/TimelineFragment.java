package com.owo.mtplease.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.owo.mtplease.Activity.MainActivity;
import com.owo.mtplease.Analytics;
import com.owo.mtplease.R;
import com.owo.mtplease.ScrollTabHolder;
import com.owo.mtplease.TimelinePostListRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TimelineFragment extends Fragment {

	private static final String TAG = "TimelineFragment";
	
	private static final String ARG_JSONSTRING_POST_LIST = "param1";

	// View: User Interface Views
	private RecyclerView _mRecyclerView;
	private LinearLayoutManager _mLayoutManager;
	// End of User Interface Views

	// Controller: Adapters for User Interface Views
	private TimelinePostListRecyclerViewAdapter _mAdapter;
	// End of User Interface Views;

	// Model: Data variables for User Interface Views
	private String _jsonStringPostList;
	private JSONObject _postObject;
	private JSONArray _postArray;

	// Listeners
	private OnTimelineFragmentListener _mTimelineFragmentListener;
	protected ScrollTabHolder _mScrollTabHolder;

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param jsonString Parameter 1.
	 * @return A new instance of fragment TimelineFragement.
	 */
	public static TimelineFragment newInstance(String jsonString) {
		TimelineFragment fragment = new TimelineFragment();
		Bundle args = new Bundle();
		args.putString(ARG_JSONSTRING_POST_LIST, jsonString);
		fragment.setArguments(args);
		return fragment;
	}

	public TimelineFragment() {
		// Required empty public constructor
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		_mLayoutManager = new LinearLayoutManager(getActivity());
		_mRecyclerView.setLayoutManager(_mLayoutManager);
		_mRecyclerView.setHasFixedSize(true);
		_mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		_mAdapter = new TimelinePostListRecyclerViewAdapter(getActivity(), _postArray);
		_mRecyclerView.setAdapter(_mAdapter);
		_mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if(_mScrollTabHolder != null)
					_mScrollTabHolder.onScroll(recyclerView, _mLayoutManager.findFirstVisibleItemPosition(), 0, MainActivity.TIMELINE_FRAGMENT_VISIBLE);
			}
		});

		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			_jsonStringPostList = getArguments().getString(ARG_JSONSTRING_POST_LIST);
			Log.i(TAG, _jsonStringPostList + "");

			try {
				_postObject = new JSONObject(_jsonStringPostList);
				_postArray = new JSONArray(_postObject.getString("timeline"));
			} catch(Exception e) {
				Toast.makeText(getActivity(), R.string.notify_fail_loading_timeline, Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		Log.d(TAG, "TimelineFragmentView created");
		View timelineView = inflater.inflate(R.layout.fragment_timeline, container, false);
		_mRecyclerView = (RecyclerView) timelineView.findViewById(R.id.list_timeline);

		if(_mTimelineFragmentListener != null) {
			try {
				_mTimelineFragmentListener.onCreateTimelineFragmentView(_postObject.getString("roomCount"));
			} catch(JSONException e) {
				e.printStackTrace();
			}
		}

		return timelineView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(TAG, "onAttach()");
		try {
			_mScrollTabHolder = (ScrollTabHolder) activity;
			_mTimelineFragmentListener = (OnTimelineFragmentListener) activity;

			Tracker t = ((Analytics) getActivity().getApplication()).getTracker();
			t.setScreenName("Timeline Page View");
			t.send(new HitBuilders.AppViewBuilder().build());

		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(TAG, "onDetach()");
		_mTimelineFragmentListener = null;
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d(TAG, "TimelineFragmentView destroyed");
		_mTimelineFragmentListener.onDestroyTimelineFragmentView();
	}

	public interface OnTimelineFragmentListener {
		public void onCreateTimelineFragmentView(String roomCount);
		public void onDestroyTimelineFragmentView();
	}
}
