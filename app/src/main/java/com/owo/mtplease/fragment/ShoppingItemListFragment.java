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

import com.owo.mtplease.R;
import com.owo.mtplease.ScrollTabHolder;
import com.owo.mtplease.ShoppingItemListRecyclerViewAdapter;
import com.owo.mtplease.activity.MainActivity;

;


public class ShoppingItemListFragment extends Fragment {

	private static final String TAG = "ShoppingItemListFragment";

	private static final String ARG_SHOPPING_ITEM_TYPE = "param1";

	private int _shoppingItemType;

	// View: User Interface Views
	private RecyclerView _mRecyclerView;
	private LinearLayoutManager _mLayoutManager;
	// End of User Interface Views

	// Controller: Adapters for User Interface Views
	private RecyclerView.Adapter _mAdapter;
	// End of Controller;

	// Listeners
	private OnShoppingItemListFragmentListener _mOnShoppingItemListFragmentListener;
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
	 * @param _shoppingItemType Parameter 1.
	 * @return A new instance of fragment ShoppingItemListFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ShoppingItemListFragment newInstance(int _shoppingItemType) {
		ShoppingItemListFragment fragment = new ShoppingItemListFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SHOPPING_ITEM_TYPE, _shoppingItemType);
		fragment.setArguments(args);
		return fragment;
	}

	public ShoppingItemListFragment() {
		// Required empty public constructor
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.d(TAG, "onViewCreated");
		_mLayoutManager = new LinearLayoutManager(getActivity());
		_mRecyclerView.setLayoutManager(_mLayoutManager);
		_mRecyclerView.setHasFixedSize(true);
		_mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		switch(_shoppingItemType) {
			case MEAT_ITEM:
				_mAdapter = new ShoppingItemListRecyclerViewAdapter
						(MEAT_ITEM, _mOnShoppingItemListFragmentListener, getActivity());
				break;
			case ALCOHOL_ITEM:
				_mAdapter = new ShoppingItemListRecyclerViewAdapter
						(ALCOHOL_ITEM, _mOnShoppingItemListFragmentListener, getActivity());
				break;
			case OTHERS_ITEM:
				_mAdapter = new ShoppingItemListRecyclerViewAdapter
						(OTHERS_ITEM, _mOnShoppingItemListFragmentListener, getActivity());
				break;
		}
		_mRecyclerView.setAdapter(_mAdapter);

		_mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if(mScrollTabHolder != null)
					mScrollTabHolder.onScroll(recyclerView, _mLayoutManager.findFirstVisibleItemPosition(),
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
			_shoppingItemType = getArguments().getInt(ARG_SHOPPING_ITEM_TYPE);
			Log.d(TAG, _shoppingItemType + "");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		Log.d(TAG, "onCreateView");
		View shoppingItemListView = inflater.inflate(R.layout.fragment_shopping_item_list, container, false);
		_mRecyclerView = (RecyclerView) shoppingItemListView.findViewById(R.id.list_item);

		if(_mOnShoppingItemListFragmentListener != null) {
			switch(_shoppingItemType) {
				case MEAT_ITEM:
					_mOnShoppingItemListFragmentListener.onCreateShoppingItemListFragmentView(_shoppingItemType, 9);
					break;
				case ALCOHOL_ITEM:
					_mOnShoppingItemListFragmentListener.onCreateShoppingItemListFragmentView(_shoppingItemType, 9);
					break;
				case OTHERS_ITEM:
					_mOnShoppingItemListFragmentListener.onCreateShoppingItemListFragmentView(_shoppingItemType, 9);
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
			_mOnShoppingItemListFragmentListener = (OnShoppingItemListFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		_mOnShoppingItemListFragmentListener = null;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d(TAG, "onDestroyView");
		_mOnShoppingItemListFragmentListener.onDestroyShoppingItemListFragmentView();
	}

	public void switchRecyclerViewAdpater(int _shoppingItemType) {
		switch(_shoppingItemType) {
			case MEAT_ITEM:
				_mOnShoppingItemListFragmentListener.onCreateShoppingItemListFragmentView(_shoppingItemType, 9);
				_mAdapter = new ShoppingItemListRecyclerViewAdapter
						(MEAT_ITEM, _mOnShoppingItemListFragmentListener, getActivity());
				break;
			case ALCOHOL_ITEM:
				_mOnShoppingItemListFragmentListener.onCreateShoppingItemListFragmentView(_shoppingItemType, 9);
				_mAdapter = new ShoppingItemListRecyclerViewAdapter
						(ALCOHOL_ITEM, _mOnShoppingItemListFragmentListener, getActivity());
				break;
			case OTHERS_ITEM:
				_mOnShoppingItemListFragmentListener.onCreateShoppingItemListFragmentView(_shoppingItemType, 9);
				_mAdapter = new ShoppingItemListRecyclerViewAdapter
						(OTHERS_ITEM, _mOnShoppingItemListFragmentListener, getActivity());
				break;
		}
		_mRecyclerView.setAdapter(_mAdapter);
	}

	public interface OnShoppingItemListFragmentListener {
		public void onCreateShoppingItemListFragmentView(int itemType, int numItem);
		public void onDestroyShoppingItemListFragmentView();
		public void onClickItem(int itemType, String itemName, String itemUnit, String itemUnitCount, int itemPrice);
		public void onClickItem(int itemType);
	}
}
