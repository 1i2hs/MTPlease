package com.owo.mtplease;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment {

    private static final String TAG = "ResultFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String JSONSTRING_OF_ROOMS = "jsonStringRoomList";
    private static final String DATE_OF_MT = "dateMT";

    // View: User Interface Views
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    // End of User Interface Views

    // Controller: Adapters for User Interface Views
    private RecyclerView.Adapter mAdapter;
    // End of Controller;

    // Model: Data variables for User Interface Views
    private String jsonStringRoomList;
    private JSONObject roomObject;
    private JSONArray roomArray;
    private String dateMT;
    // End of the Model

    // Flags

    // Listeners
    private OnFragmentInteractionListener mListener;
    protected ScrollTabHolder mScrollTabHolder;

    // Others
    private FragmentManager mFragmentManager;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param jsonString Parameter 1.
     * @param dateMT Parameter 1.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultFragment newInstance(String jsonString, String dateMT) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(JSONSTRING_OF_ROOMS, jsonString);
        args.putString(DATE_OF_MT, dateMT);
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

        mAdapter = new RoomListRecyclerViewAdapter(getActivity(), mFragmentManager, roomArray, dateMT);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mScrollTabHolder != null)
                    mScrollTabHolder.onScroll(recyclerView, mLayoutManager.findFirstVisibleItemPosition(), 0);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jsonStringRoomList = getArguments().getString(JSONSTRING_OF_ROOMS);
            dateMT = getArguments().getString(DATE_OF_MT);
            Log.i(TAG, jsonStringRoomList);

            try {
                roomObject = new JSONObject(jsonStringRoomList);
                roomArray = new JSONArray(roomObject.getString("results"));
            } catch(Exception e) {
                Toast.makeText(getActivity(), R.string.notify_fail_loading_room_results, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View resultView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) resultView.findViewById(R.id.list_room);
        return resultView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onResultFragmentInteraction(uri);
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
        public void onResultFragmentInteraction(Uri uri);
    }

    public void setScrollTabHolder(ScrollTabHolder scrollTabHolder) {
       this.mScrollTabHolder = scrollTabHolder;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }
}
