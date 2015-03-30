package com.owo.mtplease.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.owo.mtplease.Activity.MainActivity;
import com.owo.mtplease.R;
import com.owo.mtplease.RoomInfoModelController;
import com.owo.mtplease.RoomListRecyclerViewAdapter;
import com.owo.mtplease.ScrollTabHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ResultFragment extends Fragment {

	private static final String TAG = "ResultFragment";

	private static final String ARG_JSONSTRING_OF_ROOMS = "jsonstring_of_rooms";
	private static final String ARG_DATE_OF_MT = "date_of_mt";
	private static final String ARG_TYPE_OF_LIST = "type_of_list";

	private static final int DEFAULT_FIRST_ITEM_POSITION = 1;

	public static final int LIST_OF_ROOMS_AFTER_SEARCH = 1;
	public static final int LIST_OF_ROOMS_OF_PENSION = 2;

	// View: User Interface Views
	private RecyclerView _mRecyclerView;
	private LinearLayoutManager _mLayoutManager;
	// End of User Interface Views

	// Controller: Adapters for User Interface Views
	private RecyclerView.Adapter _mAdapter;
	// End of Controller;

	// Model: Data variables for User Interface Views
	private String _jsonStringRoomList;
	private JSONArray _roomArray;
	private String _mtDate;
	private int _listType;
	// End of the Model

	// Flags
	// End of the Flags
	// Counters

	// Listeners
	private OnResultFragmentListener _mOnResultFragmentListener;
	protected ScrollTabHolder mScrollTabHolder;
	// End of the Listeners

	// Others
	private ActionBarActivity _actionBarActivity;
	//private FragmentManager mFragmentManager;
	// End of the Others

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param jsonString response data(received data which is the list of rooms) from the server
	 * @param mtDate date of the mt to go
	 * @param listType Type of list to show in the result page
	 * @return A new instance of fragment MainFragment.
	 */
	public static ResultFragment newInstance(String jsonString, String mtDate, int listType) {
		ResultFragment fragment = new ResultFragment();
		Bundle args = new Bundle();
		args.putString(ARG_JSONSTRING_OF_ROOMS, jsonString);
		args.putString(ARG_DATE_OF_MT, mtDate);
		args.putInt(ARG_TYPE_OF_LIST, listType);
		fragment.setArguments(args);
		return fragment;
	}

	public ResultFragment() {
		// Required empty public constructor
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		Log.d(TAG, "onViewCreated");
		_mLayoutManager = new LinearLayoutManager(getActivity());
		_mRecyclerView.setLayoutManager(_mLayoutManager);
		_mRecyclerView.setHasFixedSize(true);
		_mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		_mAdapter = new RoomListRecyclerViewAdapter(getActivity(),
				_roomArray, _mtDate, _mOnResultFragmentListener, _listType);
		_mRecyclerView.setAdapter(_mAdapter);

		_mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if(mScrollTabHolder != null) {
					mScrollTabHolder.onScroll(recyclerView, _mLayoutManager.findFirstVisibleItemPosition(), 0, MainActivity.RESULT_FRAGMENT_VISIBLE);
				}
			}
		});

		super.onViewCreated(view, savedInstanceState);
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			_jsonStringRoomList = getArguments().getString(ARG_JSONSTRING_OF_ROOMS);
			_mtDate = getArguments().getString(ARG_DATE_OF_MT);
			_listType = getArguments().getInt(ARG_TYPE_OF_LIST);
			Log.i(TAG, _jsonStringRoomList);

			try {
				JSONObject roomObject = new JSONObject(_jsonStringRoomList);
				_roomArray = new JSONArray(roomObject.getString("roomResultList"));
			} catch(JSONException e) {
				Toast.makeText(getActivity(), R.string.fail_loading_room_results, Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		// Inflate the layout for this fragment
		View resultView = inflater.inflate(R.layout.fragment_result, container, false);
		_mRecyclerView = (RecyclerView) resultView.findViewById(R.id.list_room);

		if(_mOnResultFragmentListener != null && _roomArray != null)
			_mOnResultFragmentListener.onCreateResultFragmentView(_roomArray.length(), _listType);

		return resultView;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		_mOnResultFragmentListener.onResumeResultFragmentView(_mLayoutManager, DEFAULT_FIRST_ITEM_POSITION);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			_actionBarActivity = (ActionBarActivity) activity;
			mScrollTabHolder = (ScrollTabHolder) activity;
			_mOnResultFragmentListener = (OnResultFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
//		Log.d(TAG, "ResultFragmentView detached");
		_mOnResultFragmentListener = null;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
//		Log.d(TAG, "ResultFragmentView destroyed");
		_mOnResultFragmentListener.onDestroyResultFragmentView();
	}

	public interface OnResultFragmentListener {
		public void onCreateResultFragmentView(int numRoom, int listType);
		public void onPreLoadSpecificInfoFragment();
		public void onLoadSpecificInfoFragment(RoomInfoModelController roomInfoModelController, JSONArray _roomArray);
		public void onPostLoadSpecificInfoFragment();
		public void onDestroyResultFragmentView();
		public void onResumeResultFragmentView(LinearLayoutManager linearLayoutManager, int defaultPosition);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated");
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		Log.d(TAG, "onHiddenChanged");
	}

	@Override
	public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
		super.onInflate(activity, attrs, savedInstanceState);
		Log.d(TAG, "onInflate");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");
	}

	@Override
	public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		Log.d(TAG, "onViewStateRestored");
	}
}
