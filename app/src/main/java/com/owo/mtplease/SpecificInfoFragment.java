package com.owo.mtplease;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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
    private static final String DATE_OF_MT = "date";
    private static final String NAME_OF_ROOM = "room_name";
    private static final String NAME_OF_PENSION = "pen_name";

    // TODO: Rename and change types of parameters
    private int pen_id;
    private String dateMT;
    private String room_name;
    private String pen_name;

    // View: User Interface Views
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutMangaer;
    // End of User Interface Views

    // Controller: Adapters for User Interface Views
    private RecyclerView.Adapter mAdpater;
    // End of the Controller

    // Model: Data variables for User Interface Views
    // End of the Model

    //Listeners
    private OnFragmentInteractionListener mListener;

    public SpecificInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pen_id Parameter 1.
     * @param dateMT Parameter 2.
     * @param room_name Parameter 3.
     * @param pen_name Parameter 4.
     * @return A new instance of fragment SpecificInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpecificInfoFragment newInstance(int pen_id, String dateMT, String room_name, String pen_name) {
        SpecificInfoFragment fragment = new SpecificInfoFragment();
        Bundle args = new Bundle();
        args.putInt(PENSION_ID, pen_id);
        args.putString(DATE_OF_MT, dateMT);
        args.putString(NAME_OF_ROOM, room_name);
        args.putString(NAME_OF_PENSION, pen_name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.pen_id = getArguments().getInt(PENSION_ID);
            this.dateMT = getArguments().getString(DATE_OF_MT);
            this.room_name = getArguments().getString(NAME_OF_ROOM);
            this.pen_name = getArguments().getString(NAME_OF_PENSION);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mLayoutMangaer = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutMangaer);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdpater = new SpecificInfoRoomRecyclerViewAdapter(this.pen_id, this.dateMT, this.room_name);
        // Fading out Actionbar???
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View specificInfoView = inflater.inflate(R.layout.fragment_specific_info, container, false);
        mRecyclerView = (RecyclerView) specificInfoView.findViewById(R.id.list_room_info);
        getActivity().getActionBar().setTitle(room_name);
        getActivity().getActionBar().setSubtitle(pen_name);
        // **********************actionbar button issue...........

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

}
