package com.owo.mtplease;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SpecificInfoFragment extends Fragment {

	private static final String TAG = "SpecificInfoFragment";

	private static final String ARG_SPECIFIC_ROOM_DATA = "param1";
	private static final String ARG_NUMBER_OF_ROOMS_FOUND = "param2";

	// View: User Interface Views
	private RecyclerView mRecyclerView;
	private LinearLayoutManager mLayoutManager;
	private Button compareButton;
	private Button planButton;
	private Button mineButton;
	// End of User Interface Views

	// Controller: Adapters for User Interface Views
	private RecyclerView.Adapter mAdapter;
	// End of the Controller

	// Model: Data variables for User Interface Views
	private RoomInfoModelController mRoomInfoModelController;
	private int numRoom;
	// End of the Model

	//Listeners
	private  OnSpecificInfoFragmentListener mOnSpecificInfoFragmentListener;
	protected ScrollTabHolder mScrollTabHolder;

	public SpecificInfoFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param roomInfoModelController Parameter 1.
	 * @param numRoom Parameter 2.
	 * @return A new instance of fragment SpecificInfoFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static SpecificInfoFragment newInstance(RoomInfoModelController roomInfoModelController, int numRoom) {
		SpecificInfoFragment fragment = new SpecificInfoFragment();
		Bundle args = new Bundle();
		args.putParcelable(ARG_SPECIFIC_ROOM_DATA, roomInfoModelController);
		args.putInt(ARG_NUMBER_OF_ROOMS_FOUND, numRoom);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate()");
		if (getArguments() != null) {
			this.mRoomInfoModelController = getArguments().getParcelable(ARG_SPECIFIC_ROOM_DATA);
			this.numRoom = getArguments().getInt(ARG_NUMBER_OF_ROOMS_FOUND);
		}

		Log.d(TAG, mRoomInfoModelController.getPen_homepage());
		Log.d(TAG, mRoomInfoModelController.getPen_phone1());
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		mLayoutManager = new LinearLayoutManager(getActivity());
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		mAdapter = new SpecificInfoRoomRecyclerViewAdapter(getActivity(), mRoomInfoModelController);
		Log.d(TAG, "before setting adapter");
		mRecyclerView.setAdapter(mAdapter);

		mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				if (mScrollTabHolder != null)
					mScrollTabHolder.onScroll(recyclerView,
							mLayoutManager.findFirstVisibleItemPosition(), 0,
							MainActivity.SPECIFIC_INFO_FRAGMENT_VISIBLE);
			}
		});
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		// Inflate the layout for this fragment
		View specificInfoView = inflater.inflate(R.layout.fragment_specific_info, container, false);
		mRecyclerView = (RecyclerView) specificInfoView.findViewById(R.id.list_room_info);
		// configure compare button
		/*compareButton = (Button) specificInfoView.findViewById(R.id.button_compare);
		compareButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), R.string.added_to_compare, Toast.LENGTH_LONG).show();
			}
		});*/
		// configure plan button
		planButton = (Button) specificInfoView.findViewById(R.id.button_add_to_plan);
		planButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnSpecificInfoFragmentListener.onClickAddRoomToPlanButton(mRoomInfoModelController);
			}
		});

		/*mineButton = (Button) specificInfoView.findViewById(R.id.button_mine);
		mineButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), R.string.mine, Toast.LENGTH_SHORT).show();
			}
		});
*/
		if(mOnSpecificInfoFragmentListener != null)
			mOnSpecificInfoFragmentListener
					.onCreateSpecificInfoFragmentView(mRoomInfoModelController.getRoom_name(), mRoomInfoModelController.getPen_name());

		return specificInfoView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mScrollTabHolder = (ScrollTabHolder) activity;
			mOnSpecificInfoFragmentListener = ( OnSpecificInfoFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(TAG, "onDetach");
		mOnSpecificInfoFragmentListener = null;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	public interface OnSpecificInfoFragmentListener {
		// TODO: Update argument type and name
		public void onCreateSpecificInfoFragmentView(String roomName, String pensionName);
		public void onClickAddRoomToPlanButton(RoomInfoModelController roomInfoModelController);
	}

}
