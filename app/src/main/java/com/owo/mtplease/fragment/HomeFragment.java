package com.owo.mtplease.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.owo.mtplease.HomePostListRecyclerViewAdapter;
import com.owo.mtplease.R;
import com.owo.mtplease.ScrollTabHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

	private static final String TAG = "HomeFragment";

	private static final String ARG_JSONSTRING_POST_LIST = "param1";

	// View: User Interface Views
	private RecyclerView _mRecyclerView;
	private LinearLayoutManager _mLayoutManager;
	private SwipeRefreshLayout _mSwipeRefreshLayout;
	// End of User Interface Views

	// Model: Data variables for User Interface Views
	private JSONObject _postObject;
	private JSONArray _postArray;

	// Listeners
	private OnHomeFragmentListener _mHomeFragmentListener;
	protected ScrollTabHolder _mScrollTabHolder;

	// others
	private Activity _mActivity;

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param jsonString Parameter 1.
	 * @return A new instance of fragment HomeFragement.
	 */
	public static HomeFragment newInstance(String jsonString) {
		HomeFragment fragment = new HomeFragment();
		Bundle args = new Bundle();
		args.putString(ARG_JSONSTRING_POST_LIST, jsonString);
		fragment.setArguments(args);
		return fragment;
	}

	public HomeFragment() {
		// Required empty public constructor
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		_mLayoutManager = new LinearLayoutManager(getActivity());
		_mRecyclerView.setLayoutManager(_mLayoutManager);
		_mRecyclerView.setHasFixedSize(true);
		_mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		HomePostListRecyclerViewAdapter adapter = new HomePostListRecyclerViewAdapter(getActivity(), _postArray);
		_mRecyclerView.setAdapter(adapter);
		_mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if (_mScrollTabHolder != null)
					_mScrollTabHolder.onScroll(recyclerView, _mLayoutManager.findFirstVisibleItemPosition(), 0, MainActivity.HOME_FRAGMENT_VISIBLE);
			}
		});


		_mSwipeRefreshLayout.setProgressViewOffset(false, _convertDpToPx(260, getActivity()), _convertDpToPx(280, getActivity()));
		_mSwipeRefreshLayout.setColorSchemeResources(R.color.mtplease_color_primary, R.color.mtplease_date_select_button_color, R.color.mtplease_call_subbutton_color);
		_mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				_mHomeFragmentListener.onRefreshHomeFragment(_mSwipeRefreshLayout);
			}
		});

		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			String jsonStringPostList = getArguments().getString(ARG_JSONSTRING_POST_LIST);
			Log.i(TAG, jsonStringPostList + "");

			try {
				_postObject = new JSONObject(jsonStringPostList);
				_postArray = new JSONArray(_postObject.getString("timeline"));
				Log.d(TAG, "Latest Application Version: " + _postObject.optInt("version"));
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
		Log.d(TAG, "HomeFragmentView created");
		View HomeView = inflater.inflate(R.layout.fragment_home, container, false);
		_mRecyclerView = (RecyclerView) HomeView.findViewById(R.id.list_home);

		_mSwipeRefreshLayout = (SwipeRefreshLayout) HomeView.findViewById(R.id.swiperefreshlayout_home);

		if(_mHomeFragmentListener != null) {
			try {
				if(_postObject != null)
					_mHomeFragmentListener.onCreateHomeFragmentView(_postObject.getString("roomCount"));
				else
					_mHomeFragmentListener.onCreateHomeFragmentView("0");
			} catch(JSONException e) {
				e.printStackTrace();
			}
		}

		if(_mScrollTabHolder == null)
			_mScrollTabHolder = (ScrollTabHolder) _mActivity;

		return HomeView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(TAG, "onAttach()");
		try {
			_mScrollTabHolder = (ScrollTabHolder) activity;
			_mHomeFragmentListener = (OnHomeFragmentListener) activity;

			Tracker t = ((Analytics) getActivity().getApplication()).getTracker();
			t.setScreenName("Home Page View");
			t.send(new HitBuilders.AppViewBuilder().build());

		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(TAG, "onDetach()");
		_mHomeFragmentListener = null;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d(TAG, "HomeFragmentView destroyed");
		_mScrollTabHolder = null;
		_mHomeFragmentListener.onDestroyHomeFragmentView();
	}

	private static int _convertDpToPx(int dp, Context context) {
		float screenDensity = context.getResources().getDisplayMetrics().density;
		int px = (int)(dp * screenDensity);
		return px;
	}

	public interface OnHomeFragmentListener {
		public void onCreateHomeFragmentView(String roomCount);
		public void onResumeHomeFragmentView();
		public void onDestroyHomeFragmentView();
		public void onRefreshHomeFragment(SwipeRefreshLayout swipeRefreshLayout);
	}
}
