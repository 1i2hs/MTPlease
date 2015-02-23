package com.owo.mtplease.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

;import com.owo.mtplease.R;
import com.owo.mtplease.ScrollTabHolder;
import com.owo.mtplease.ShoppingItemListRecyclerViewAdapter;
import com.owo.mtplease.Activity.MainActivity;


public class ShoppingItemListFragment extends Fragment {

	private static final String TAG = "ShoppingItemListFragment";

	private static final String ARG_SHOPPING_ITEM_TYPE = "param1";

	private int shoppingItemType;

	// View: User Interface Views
	private RecyclerView mRecyclerView;
	private LinearLayoutManager mLayoutManager;
	// End of User Interface Views

	// Controller: Adapters for User Interface Views
	private RecyclerView.Adapter mAdapter;
	// End of Controller;

	// Listeners
	private OnShoppingItemListFragmentListener mOnShoppingItemListFragmentListener;
	protected ScrollTabHolder mScrollTabHolder;
	// End of the Listeners

	// Flags
	public static final int MEAT_ITEM = 1;
	public static final int ALCOHOL_ITEM = 2;
	public static final int OTHERS_ITEM = 3;
	// End of flags

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
		args.putInt(ARG_SHOPPING_ITEM_TYPE, shoppingItemType);
		fragment.setArguments(args);
		return fragment;
	}

	public ShoppingItemListFragment() {
		// Required empty public constructor
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.d(TAG, "onViewCreated");
		mLayoutManager = new LinearLayoutManager(getActivity());
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		switch(shoppingItemType) {
			case MEAT_ITEM:
				mAdapter = new ShoppingItemListRecyclerViewAdapter
						(MEAT_ITEM, mOnShoppingItemListFragmentListener, getActivity());
				break;
			case ALCOHOL_ITEM:
				mAdapter = new ShoppingItemListRecyclerViewAdapter
						(ALCOHOL_ITEM, mOnShoppingItemListFragmentListener, getActivity());
				break;
			case OTHERS_ITEM:
				mAdapter = new ShoppingItemListRecyclerViewAdapter
						(OTHERS_ITEM, mOnShoppingItemListFragmentListener, getActivity());
				break;
		}
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
					mScrollTabHolder.onScroll(recyclerView, mLayoutManager.findFirstVisibleItemPosition(),
							0, MainActivity.SHOPPINGITEMLIST_FRAGMENT_VISIBLE);
			}
		});

		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		if (getArguments() != null) {
			shoppingItemType = getArguments().getInt(ARG_SHOPPING_ITEM_TYPE);
			Log.d(TAG, shoppingItemType + "");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		Log.d(TAG, "onCreateView");
		View shoppingItemListView = inflater.inflate(R.layout.fragment_shopping_item_list, container, false);
		mRecyclerView = (RecyclerView) shoppingItemListView.findViewById(R.id.list_item);

		if(mOnShoppingItemListFragmentListener != null) {
			switch(shoppingItemType) {
				case MEAT_ITEM:
					mOnShoppingItemListFragmentListener.onCreateShoppingItemListFragmentView(shoppingItemType, 9);
					break;
				case ALCOHOL_ITEM:
					mOnShoppingItemListFragmentListener.onCreateShoppingItemListFragmentView(shoppingItemType, 9);
					break;
				case OTHERS_ITEM:
					mOnShoppingItemListFragmentListener.onCreateShoppingItemListFragmentView(shoppingItemType, 9);
					break;
			}
		}

		return shoppingItemListView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mScrollTabHolder = (ScrollTabHolder) activity;
			mOnShoppingItemListFragmentListener = (OnShoppingItemListFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mOnShoppingItemListFragmentListener = null;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d(TAG, "onDestroyView");
		mOnShoppingItemListFragmentListener.onDestroyShoppingItemListFragmentView();
	}

	public void switchRecyclerViewAdpater(int shoppingItemType) {
		switch(shoppingItemType) {
			case MEAT_ITEM:
				mOnShoppingItemListFragmentListener.onCreateShoppingItemListFragmentView(shoppingItemType, 9);
				mAdapter = new ShoppingItemListRecyclerViewAdapter
						(MEAT_ITEM, mOnShoppingItemListFragmentListener, getActivity());
				break;
			case ALCOHOL_ITEM:
				mOnShoppingItemListFragmentListener.onCreateShoppingItemListFragmentView(shoppingItemType, 9);
				mAdapter = new ShoppingItemListRecyclerViewAdapter
						(ALCOHOL_ITEM, mOnShoppingItemListFragmentListener, getActivity());
				break;
			case OTHERS_ITEM:
				mOnShoppingItemListFragmentListener.onCreateShoppingItemListFragmentView(shoppingItemType, 9);
				mAdapter = new ShoppingItemListRecyclerViewAdapter
						(OTHERS_ITEM, mOnShoppingItemListFragmentListener, getActivity());
				break;
		}
		mRecyclerView.setAdapter(mAdapter);
	}

	public interface OnShoppingItemListFragmentListener {
		// TODO: Update argument type and name
		public void onCreateShoppingItemListFragmentView(int itemType, int numItem);
		public void onDestroyShoppingItemListFragmentView();
		public void onClickItem(int itemType, String itemName, String itemUnit, String itemUnitCount, int itemPrice);
		public void onClickItem(int itemType);
	}
}
