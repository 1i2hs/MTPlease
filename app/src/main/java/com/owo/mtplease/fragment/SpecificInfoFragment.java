package com.owo.mtplease.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.owo.mtplease.Activity.MainActivity;
import com.owo.mtplease.R;
import com.owo.mtplease.RoomInfoModelController;
import com.owo.mtplease.ScrollTabHolder;
import com.owo.mtplease.SpecificInfoRoomRecyclerViewAdapter;


public class SpecificInfoFragment extends Fragment {

	private static final String TAG = "SpecificInfoFragment";

	private static final String ARG_SPECIFIC_ROOM_DATA = "param1";
	private static final String ARG_NUMBER_OF_ROOMS_FOUND = "param2";

	// View: User Interface Views
	private RecyclerView _mRecyclerView;
	private LinearLayoutManager _mLayoutManager;
	private FrameLayout _grayFrameLayout;
	private Drawable _grayBackground;
	private ImageView _actionButton;
	private ImageView _otherRoomsButton;
	private Button _compareButton;
	private Button _planButton;
	private Button _mineButton;
	// End of User Interface Views

	// Controller: Adapters for User Interface Views
	private RecyclerView.Adapter _mAdapter;
	// End of the Controller

	// Model: Data variables for User Interface Views
	private RoomInfoModelController _mRoomInfoModelController;
	private int _numRoom;
	// End of the Model

	//Listeners
	private  OnSpecificInfoFragmentListener _mOnSpecificInfoFragmentListener;
	protected ScrollTabHolder _mScrollTabHolder;

	// Others
	private boolean _isActionButtonClicked = false;

	public SpecificInfoFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param roomInfoModelController Parameter 1.
	 * @param _numRoom Parameter 2.
	 * @return A new instance of fragment SpecificInfoFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static SpecificInfoFragment newInstance(RoomInfoModelController roomInfoModelController, int _numRoom) {
		SpecificInfoFragment fragment = new SpecificInfoFragment();
		Bundle args = new Bundle();
		args.putParcelable(ARG_SPECIFIC_ROOM_DATA, roomInfoModelController);
		args.putInt(ARG_NUMBER_OF_ROOMS_FOUND, _numRoom);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate()");
		if (getArguments() != null) {
			this._mRoomInfoModelController = getArguments().getParcelable(ARG_SPECIFIC_ROOM_DATA);
			this._numRoom = getArguments().getInt(ARG_NUMBER_OF_ROOMS_FOUND);
		}

		Log.d(TAG, _mRoomInfoModelController.getPen_homepage());
		Log.d(TAG, _mRoomInfoModelController.getPen_phone1());
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		_mLayoutManager = new LinearLayoutManager(getActivity());
		_mRecyclerView.setLayoutManager(_mLayoutManager);
		_mRecyclerView.setHasFixedSize(true);
		_mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		_mAdapter = new SpecificInfoRoomRecyclerViewAdapter(getActivity(), _mRoomInfoModelController, _mOnSpecificInfoFragmentListener);
		Log.d(TAG, "before setting adapter");
		_mRecyclerView.setAdapter(_mAdapter);

		_mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				if (_mScrollTabHolder != null)
					_mScrollTabHolder.onScroll(recyclerView,
							_mLayoutManager.findFirstVisibleItemPosition(), 0,
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
		_mRecyclerView = (RecyclerView) specificInfoView.findViewById(R.id.list_room_info);

		_grayFrameLayout = (FrameLayout) specificInfoView.findViewById(R.id.framelayout_foreground);
		_grayFrameLayout.setVisibility(View.GONE);
		_grayBackground = _grayFrameLayout.getBackground();
		_grayBackground.setAlpha(100);

		_actionButton = (ImageView) specificInfoView.findViewById(R.id.imageView_btn_action_info_specific);
		_actionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!_isActionButtonClicked) {
					v.animate().rotation(90.0F);
					_grayFrameLayout.setVisibility(View.VISIBLE);
					_grayFrameLayout.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							_actionButton.animate().rotation(360.0F);
							_grayFrameLayout.setVisibility(View.GONE);
							_grayFrameLayout.setOnClickListener(null);
							_animateActionButtons(0.0F, _convertDpToPx(80, v.getContext()), 0.1F, 0.1F);
							_isActionButtonClicked = false;
						}
					});
					_animateActionButtons(1.0F, -_convertDpToPx(80, v.getContext()), 1.0F, 1.0F);
					_isActionButtonClicked = true;
				} else {
					v.animate().rotation(360.0F);
					_grayFrameLayout.setVisibility(View.GONE);
					_grayFrameLayout.setOnClickListener(null);
					_animateActionButtons(0.0F, _convertDpToPx(80, v.getContext()), 0.1F, 0.1F);
					_isActionButtonClicked = false;
				}
			}
		});

		_otherRoomsButton = (ImageView) specificInfoView.findViewById(R.id.imageView_btn_other_rooms);
		_otherRoomsButton.setScaleX(0.1F);
		_otherRoomsButton.setScaleY(0.1F);
		_otherRoomsButton.setAlpha(0.0F);
		_otherRoomsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_mOnSpecificInfoFragmentListener.onClickSeeOtherRoomsButton(_mRoomInfoModelController.getPen_name());
				_grayFrameLayout.setVisibility(View.GONE);
				_grayFrameLayout.setOnClickListener(null);
				_animateActionButtons(0.0F, _convertDpToPx(80, v.getContext()), 0.1F, 0.1F);
				_isActionButtonClicked = false;
			}
		});

		// configure compare button
		/*_compareButton = (Button) specificInfoView.findViewById(R.id.button_compare);
		_compareButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), R.string.added_to_compare, Toast.LENGTH_LONG).show();
			}
		});*/

		// configure plan button
		/*_planButton = (Button) specificInfoView.findViewById(R.id.button_add_to_plan);
		_planButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_mOnSpecificInfoFragmentListener.onClickAddRoomToPlanButton(_mRoomInfoModelController);
			}
		});*/

		/*_mineButton = (Button) specificInfoView.findViewById(R.id.button_mine);
		_mineButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), R.string.mine, Toast.LENGTH_SHORT).show();
			}
		});*/

		if(_mOnSpecificInfoFragmentListener != null)
			_mOnSpecificInfoFragmentListener
					.onCreateSpecificInfoFragmentView(_mRoomInfoModelController.getRoom_name(), _mRoomInfoModelController.getPen_name());

		return specificInfoView;
	}

	private void _animateActionButtons(final float alpha, final int translation, final float scaleX, final float scaleY) {
		if(!_isActionButtonClicked) {
			_otherRoomsButton.animate().scaleX(scaleX);
			_otherRoomsButton.animate().scaleY(scaleY);
			_otherRoomsButton.animate().alpha(alpha);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					_otherRoomsButton.animate().translationXBy(translation);
				}
			}, 200);
		} else {
			_otherRoomsButton.animate().translationXBy(translation);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					_otherRoomsButton.animate().scaleX(scaleX);
					_otherRoomsButton.animate().scaleY(scaleY);
					_otherRoomsButton.animate().alpha(alpha);
				}
			}, 200);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			_mScrollTabHolder = (ScrollTabHolder) activity;
			_mOnSpecificInfoFragmentListener = ( OnSpecificInfoFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(TAG, "onDetach");
		_mOnSpecificInfoFragmentListener = null;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		_mOnSpecificInfoFragmentListener.onDestroySpecificInfoFragmentView();
	}

	public interface OnSpecificInfoFragmentListener {
		public void onCreateSpecificInfoFragmentView(String roomName, String pensionName);
		public void onClickAddRoomToPlanButton(RoomInfoModelController roomInfoModelController);
		public void onClickSeeOtherRoomsButton(String pensionName);
		public void onDestroySpecificInfoFragmentView();
	}

	private static int _convertDpToPx(int dp, Context context) {
		float screenDensity = context.getResources().getDisplayMetrics().density;
		int px = (int)(dp * screenDensity);
		return px;
	}
}
