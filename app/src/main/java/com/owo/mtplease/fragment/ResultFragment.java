package com.owo.mtplease.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.owo.mtplease.R;
import com.owo.mtplease.RoomInfoModelController;
import com.owo.mtplease.RoomListRecyclerViewAdapter;
import com.owo.mtplease.ScrollTabHolder;
import com.owo.mtplease.Activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ResultFragment extends Fragment {

	private static final String TAG = "ResultFragment";

	private static final String ARG_JSONSTRING_OF_ROOMS = "param1";
	private static final String ARG_DATE_OF_MET = "param2";

	private static final int DEFAULT_FIRST_ITEM_POSITION = 1;

	// View: User Interface Views
	private RecyclerView mRecyclerView;
	private LinearLayoutManager mLayoutManager;
	// End of User Interface Views

	// Controller: Adapters for User Interface Views
	private RecyclerView.Adapter mAdapter;
	// End of Controller;

	// Model: Data variables for User Interface Views
	private String jsonStringRoomList;
	private JSONArray roomArray;
	private String mtDate;
	// End of the Model

	// Flags
	// End of the Flags
	// Counters

	// Listeners
	private OnResultFragmentListener mOnResultFragmentListener;
	protected ScrollTabHolder mScrollTabHolder;
	// End of the Listeners

	// Others
	private ActionBarActivity actionBarActivity;
	//private FragmentManager mFragmentManager;
	private boolean noResults;
	// End of the Others

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param jsonString Parameter 1.
	 * @param mtDate Parameter 1.
	 * @return A new instance of fragment MainFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ResultFragment newInstance(String jsonString, String mtDate) {
		ResultFragment fragment = new ResultFragment();
		Bundle args = new Bundle();
		args.putString(ARG_JSONSTRING_OF_ROOMS, jsonString);
		args.putString(ARG_DATE_OF_MET, mtDate);
		fragment.setArguments(args);
		return fragment;
	}

	public ResultFragment() {
		// Required empty public constructor
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		mLayoutManager = new LinearLayoutManager(getActivity());
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		mAdapter = new RoomListRecyclerViewAdapter(getActivity(),
				roomArray, mtDate, mOnResultFragmentListener);
		mRecyclerView.setAdapter(mAdapter);

		mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if(mScrollTabHolder != null) {
					int position = mLayoutManager.findFirstVisibleItemPosition();
					mScrollTabHolder.onScroll(recyclerView, mLayoutManager.findFirstVisibleItemPosition(), 0, MainActivity.RESULT_FRAGMENT_VISIBLE);
				}
			}
		});

		super.onViewCreated(view, savedInstanceState);
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			jsonStringRoomList = getArguments().getString(ARG_JSONSTRING_OF_ROOMS);
			mtDate = getArguments().getString(ARG_DATE_OF_MET);
			Log.i(TAG, jsonStringRoomList);

			try {
				JSONObject roomObject = new JSONObject(jsonStringRoomList);
				roomArray = new JSONArray(roomObject.getString("roomResultList"));
				if(roomArray.length() > 0)
					noResults = false;
				else
					noResults = true;
			} catch(JSONException e) {
				noResults = true;
				Toast.makeText(getActivity(), R.string.fail_loading_room_results, Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		Log.d(TAG, "ResultFragmentView created");
		View resultView = inflater.inflate(R.layout.fragment_result, container, false);
		mRecyclerView = (RecyclerView) resultView.findViewById(R.id.list_room);

		if(mOnResultFragmentListener != null && roomArray != null)
			mOnResultFragmentListener.onCreateResultFragmentView(noResults, roomArray.length());

		return resultView;
	}

	@Override
	public void onResume() {
		super.onResume();
		//mRecyclerView.scrollToPosition(1);
		mOnResultFragmentListener.onResumeResultFragmentView(mLayoutManager,
				mLayoutManager.findFirstVisibleItemPosition(), DEFAULT_FIRST_ITEM_POSITION);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			actionBarActivity = (ActionBarActivity) activity;
			mScrollTabHolder = (ScrollTabHolder) activity;
			mOnResultFragmentListener = (OnResultFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(TAG, "ResultFragmentView detached");
		mOnResultFragmentListener = null;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d(TAG, "ResultFragmentView destroyed");
		mOnResultFragmentListener.onDestroyResultFragmentView();
	}

	public interface OnResultFragmentListener {
		// TODO: Update argument type and name
		public void onCreateResultFragmentView(boolean noResults, int numRoom);
		public void onPreLoadSpecificInfoFragment();
		public void onLoadSpecificInfoFragment(RoomInfoModelController roomInfoModelController, JSONArray roomArray);
		public void onPostLoadSpecificInfoFragment();
		public void onDestroyResultFragmentView();
		public void onResumeResultFragmentView(LinearLayoutManager linearLayoutManager, int firstVisibleViewPosition, int defaultPosition);
	}
}
