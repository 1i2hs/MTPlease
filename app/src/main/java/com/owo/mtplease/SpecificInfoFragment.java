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
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String SPECIFIC_ROOM_DATA = "param1";
	private static final String NUMBER_OF_ROOMS_FOUND = "param2";

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
	private RoomInfoModel mRoomInfoModel;
	private int numRoom;
	// End of the Model

	//Listeners
	private OnSpecificInfoFragmentInteractionListener mOnSpecificInfoFragmentInteratcionListener;
	protected ScrollTabHolder mScrollTabHolder;

	public SpecificInfoFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param roomInfoModel Parameter 1.
	 * @param numRoom Parameter 2.
	 * @return A new instance of fragment SpecificInfoFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static SpecificInfoFragment newInstance(RoomInfoModel roomInfoModel, int numRoom) {
		SpecificInfoFragment fragment = new SpecificInfoFragment();
		Bundle args = new Bundle();
		args.putParcelable(SPECIFIC_ROOM_DATA, roomInfoModel);
		args.putInt(NUMBER_OF_ROOMS_FOUND, numRoom);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate()");
		if (getArguments() != null) {
			this.mRoomInfoModel = getArguments().getParcelable(SPECIFIC_ROOM_DATA);
			this.numRoom = getArguments().getInt(NUMBER_OF_ROOMS_FOUND);
		}

		Log.d(TAG, mRoomInfoModel.getPen_homepage());
		Log.d(TAG, mRoomInfoModel.getPen_phone1());
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		mLayoutManager = new LinearLayoutManager(getActivity());
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		mAdapter = new SpecificInfoRoomRecyclerViewAdapter(getActivity(), mRoomInfoModel);
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
				mOnSpecificInfoFragmentInteratcionListener.onClickAddRoomToPlanButton(mRoomInfoModel);
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
		if(mOnSpecificInfoFragmentInteratcionListener != null)
			mOnSpecificInfoFragmentInteratcionListener
					.onCreateSpecificInfoFragmentView(mRoomInfoModel.getRoom_name(), mRoomInfoModel.getPen_name());

		return specificInfoView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mScrollTabHolder = (ScrollTabHolder) activity;
			mOnSpecificInfoFragmentInteratcionListener = (OnSpecificInfoFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(TAG, "onDetach");
		mOnSpecificInfoFragmentInteratcionListener = null;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	public interface OnSpecificInfoFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onCreateSpecificInfoFragmentView(String roomName, String pensionName);
		public void onClickAddRoomToPlanButton(RoomInfoModel roomInfoModel);
	}

}
