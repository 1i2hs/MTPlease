package com.owo.mtplease;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SpecificInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SpecificInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpecificInfoFragment extends Fragment {

    private static final String TAG = "SpecificInfoFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PENSION_ID = "pen_id";
	private static final String NUMBER_OF_ROOMS_FOUND = "num_room_found";
	private static final String SPECIFIC_ROOM_DATA = "specific_room_data";

    // TODO: Rename and change types of parameters
    private int pen_id;
    private String dateMT;
    private String room_name;
    private String pen_name;
	private int roomCount;

    // View: User Interface Views
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ActionBar mActionBar;
    private ColorDrawable actionBarBackgroundColor;
	private Button compareButton;
	private Button estimateButton;
	private Button mineButton;
    // End of User Interface Views

    // Controller: Adapters for User Interface Views
    private RecyclerView.Adapter mAdapter;
    // End of the Controller

    // Model: Data variables for User Interface Views
	private RoomInfoModel mRoomInfoModel;
    // End of the Model

    //Listeners
    private OnFragmentInteractionListener mListener;
    protected ScrollTabHolder mScrollTabHolder;

    public SpecificInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pen_id Parameter 1.
     * @param room_count Parameter 2.
     * @param roomInfoModel Parameter 3.
     * @return A new instance of fragment SpecificInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpecificInfoFragment newInstance(int pen_id, int room_count, RoomInfoModel roomInfoModel) {
        SpecificInfoFragment fragment = new SpecificInfoFragment();
        Bundle args = new Bundle();
		args.putInt(PENSION_ID, pen_id);
		args.putInt(NUMBER_OF_ROOMS_FOUND, room_count);
		args.putParcelable(SPECIFIC_ROOM_DATA, roomInfoModel);
		fragment.setArguments(args);
		return fragment;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        if (getArguments() != null) {
            this.pen_id = getArguments().getInt(PENSION_ID);
			this.roomCount = getArguments().getInt(NUMBER_OF_ROOMS_FOUND);
			this.mRoomInfoModel = getArguments().getParcelable(SPECIFIC_ROOM_DATA);
        }

		Log.d(TAG, mRoomInfoModel.getPen_homepage());
		Log.d(TAG, mRoomInfoModel.getPen_phone1());

        // configure actionbar for result page
        actionBarBackgroundColor = new ColorDrawable(Color.BLACK);
        mActionBar.setBackgroundDrawable(actionBarBackgroundColor);

        mActionBar.setTitle(mRoomInfoModel.getRoom_name());
        mActionBar.setSubtitle(mRoomInfoModel.getPen_name());
        // end of the configuration
        // **********************actionbar button issue...........
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
                    mScrollTabHolder.onScroll(recyclerView, mLayoutManager.findFirstVisibleItemPosition(), 0, MainActivity.SPECIFIC_INFO_FRAGMENT_VISIBLE);
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
		compareButton = (Button) specificInfoView.findViewById(R.id.button_compare);
		compareButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), R.string.add_compare, Toast.LENGTH_LONG).show();
			}
		});

		estimateButton = (Button) specificInfoView.findViewById(R.id.button_estimate);
		estimateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), R.string.add_estimate, Toast.LENGTH_LONG).show();
			}
		});

		mineButton = (Button) specificInfoView.findViewById(R.id.button_mine);
		mineButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), R.string.mine, Toast.LENGTH_SHORT).show();
			}
		});

        return specificInfoView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSpecificInfoFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

		// configure actionbar for result page
		actionBarBackgroundColor = new ColorDrawable(Color.BLACK);
		mActionBar.setBackgroundDrawable(actionBarBackgroundColor);

        mActionBar.setTitle(R.string.results);
        mActionBar.setSubtitle(this.roomCount + "개의 방");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onSpecificInfoFragmentInteraction(Uri uri);
    }

    public void setActionBar(ActionBar actionBar) {
        this.mActionBar = actionBar;
    }
    public void setScrollTabHolder(ScrollTabHolder scrollTabHolder) {
        this.mScrollTabHolder = scrollTabHolder;
    }
}
