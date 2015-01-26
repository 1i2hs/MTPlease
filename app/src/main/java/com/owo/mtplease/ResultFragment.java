package com.owo.mtplease;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ResultFragment extends Fragment {

	private static final String TAG = "ResultFragment";
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String JSONSTRING_OF_ROOMS = "param1";
	private static final String DATE_OF_MT = "param2";

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
	private String dateOfMt;
	// End of the Model

	// Flags
	// End of the Flags
	// Counters

	// Listeners
	private OnResultFragmentInteractionListener mOnResultFragmentInteractionListener;
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
	 * @param dateOfMt Parameter 1.
	 * @return A new instance of fragment MainFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ResultFragment newInstance(String jsonString, String dateOfMt) {
		ResultFragment fragment = new ResultFragment();
		Bundle args = new Bundle();
		args.putString(JSONSTRING_OF_ROOMS, jsonString);
		args.putString(DATE_OF_MT, dateOfMt);
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

		mAdapter = new RoomListRecyclerViewAdapter(getActivity(), actionBarActivity.getSupportFragmentManager(),
				roomArray, dateOfMt, mScrollTabHolder, mOnResultFragmentInteractionListener);
		mRecyclerView.setAdapter(mAdapter);

		mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if(mScrollTabHolder != null)
					mScrollTabHolder.onScroll(recyclerView, mLayoutManager.findFirstVisibleItemPosition(), 0, MainActivity.RESULT_FRAGMENT_VISIBLE);
			}
		});

		//mLayoutManager.scrollToPositionWithOffset(2, R.dimen.actionbar_height);
		mRecyclerView.scrollToPosition(1);

		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			jsonStringRoomList = getArguments().getString(JSONSTRING_OF_ROOMS);
			dateOfMt = getArguments().getString(DATE_OF_MT);
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
				Toast.makeText(getActivity(), R.string.notify_fail_loading_room_results, Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View resultView = inflater.inflate(R.layout.fragment_result, container, false);
		mRecyclerView = (RecyclerView) resultView.findViewById(R.id.list_room);

		if(mOnResultFragmentInteractionListener != null)
			mOnResultFragmentInteractionListener.onResultFragmentViewResumed(noResults, roomArray.length());

		return resultView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			actionBarActivity = (ActionBarActivity) activity;
			mOnResultFragmentInteractionListener = (OnResultFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mOnResultFragmentInteractionListener = null;
	}

	public interface OnResultFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onResultFragmentViewResumed(boolean noResults, int numRoom);
		public void onSpecificInfoFragmentLoad();
		public void onSpecificInfoFragmentLoadDone();
	}

	public void setScrollTabHolder(ScrollTabHolder scrollTabHolder) {
		this.mScrollTabHolder = scrollTabHolder;
	}

	/*public void setFragmentManager(FragmentManager fragmentManager) {
		this.mFragmentManager = fragmentManager;
	}*/
}
