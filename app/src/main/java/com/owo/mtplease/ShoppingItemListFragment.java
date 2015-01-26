package com.owo.mtplease;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ShoppingItemListFragment extends Fragment {

	private static final String TAG = "ShoppingItemListFragment";
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String SHOPPING_ITEM_TYPE = "param1";

	// TODO: Rename and change types of parameters
	private int shoppingItemType;

	private static final int MEAT_ITEM = 1;
	private static final int ALCOHOL_ITEM = 2;
	private static final int OTHERS_ITEM = 3;
	// View: User Interface Views
	private RecyclerView mRecyclerView;
	private LinearLayoutManager mLayoutManager;
	// End of User Interface Views

	// Controller: Adapters for User Interface Views
	private RecyclerView.Adapter mAdapter;
	// End of Controller;

	// Listeners
	private ActionBarActivity actionBarActivity;
	private OnShoppingItemListFragmentInteractionListener mOnShoppingItemListFragmentInteractionListener;
	protected ScrollTabHolder mScrollTabHolder;
	// End of the Listeners

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param shoppingItemType Parameter 1.
	 * @return A new instance of fragment ShoppingItemListFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ShoppingItemListFragment newInstance(int shoppingItemType) {
		ShoppingItemListFragment fragment = new ShoppingItemListFragment();
		Bundle args = new Bundle();
		args.putInt(SHOPPING_ITEM_TYPE, shoppingItemType);
		fragment.setArguments(args);
		return fragment;
	}

	public ShoppingItemListFragment() {
		// Required empty public constructor
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mLayoutManager = new LinearLayoutManager(getActivity());
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		switch(shoppingItemType) {
			case MEAT_ITEM:
				mAdapter = new ShoppingItemListRecyclerViewAdapter
						(MEAT_ITEM, actionBarActivity.getSupportFragmentManager());
				break;
			case ALCOHOL_ITEM:
				mAdapter = new ShoppingItemListRecyclerViewAdapter
						(ALCOHOL_ITEM, actionBarActivity.getSupportFragmentManager());
				break;
			case OTHERS_ITEM:
				mAdapter = new ShoppingItemListRecyclerViewAdapter
						(OTHERS_ITEM, actionBarActivity.getSupportFragmentManager());
				break;
		}

		mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if(mScrollTabHolder != null)
					mScrollTabHolder.onScroll(recyclerView, mLayoutManager.findFirstVisibleItemPosition(),
							0, MainActivity.SHOPPINGITEMLIST_FRAGMENT_VISIBLE);
			}
		});

		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			shoppingItemType = getArguments().getInt(SHOPPING_ITEM_TYPE);
			Log.d(TAG, shoppingItemType + "");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View shoppingItemListView = inflater.inflate(R.layout.fragment_shopping_item_list, container, false);
		mRecyclerView = (RecyclerView) shoppingItemListView.findViewById(R.id.list_item);

		return shoppingItemListView;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mOnShoppingItemListFragmentInteractionListener != null) {
			mOnShoppingItemListFragmentInteractionListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			actionBarActivity = (ActionBarActivity) activity;
			mOnShoppingItemListFragmentInteractionListener = (OnShoppingItemListFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mOnShoppingItemListFragmentInteractionListener = null;
	}

	public interface OnShoppingItemListFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

	public void setScrollTabHolder(ScrollTabHolder scrollTabHolder) {
		this.mScrollTabHolder = scrollTabHolder;
	}
}
