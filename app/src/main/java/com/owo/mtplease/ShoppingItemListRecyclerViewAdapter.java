package com.owo.mtplease;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by In-Ho on 2015-01-26.
 */
public class ShoppingItemListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final String TAG = "ShoppingITemListRecyclerViewAdapter";

	private static final int TYPE_BLANK_HEADER = 0;
	private static final int TYPE_CUSTOM_INPUT_SHOPPING_ITEM_CARD = 1;
	private static final int TYPE_SHOPPING_ITEM_CARD = 2;

	private static final int MEAT_ITEM = 1;
	private static final int ALCOHOL_ITEM = 2;
	private static final int OTHERS_ITEM = 3;

	private int shoppingItemType;
	private ShoppingItemListFragment.OnShoppingItemListFragmentListener mOnShoppingItemListFragmentListener;
	private Context mContext;

	public ShoppingItemListRecyclerViewAdapter(int shoppingItemType,
											   ShoppingItemListFragment.OnShoppingItemListFragmentListener
													   onShoppingItemListFragmentListener, Context context) {
		this.shoppingItemType = shoppingItemType;
		mOnShoppingItemListFragmentListener = onShoppingItemListFragmentListener;
		mContext = context;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == TYPE_BLANK_HEADER) {
			View headerView = LayoutInflater.from(parent.getContext()).
					inflate(R.layout.view_default_header_placeholder, parent, false);

			return new BlankHeader(headerView);
		} else if (viewType == TYPE_CUSTOM_INPUT_SHOPPING_ITEM_CARD) {
			View customShoppingItemView = LayoutInflater.from(parent.getContext()).
					inflate(R.layout.card_custom_shopping_item_input, parent, false);

			return new CustomInputShoppingItemCard(customShoppingItemView, mOnShoppingItemListFragmentListener);
		} else if (viewType == TYPE_SHOPPING_ITEM_CARD) {
			View shoppingItemView = LayoutInflater.from(parent.getContext()).
					inflate(R.layout.card_shopping_item, parent, false);

			return new ShoppingItemCard(shoppingItemView, mOnShoppingItemListFragmentListener, mContext);
		}

		throw new RuntimeException("there is no type that matches the type "
				+ viewType + " make sure your using types correctly");
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof CustomInputShoppingItemCard) {
			switch (shoppingItemType) {
				case MEAT_ITEM:
					((CustomInputShoppingItemCard) holder).setItemType(MEAT_ITEM);
					break;
				case ALCOHOL_ITEM:
					((CustomInputShoppingItemCard) holder).setItemType(ALCOHOL_ITEM);
					break;
				case OTHERS_ITEM:
					((CustomInputShoppingItemCard) holder).setItemType(OTHERS_ITEM);
					break;
			}
			((CustomInputShoppingItemCard) holder).getLayout()
					.setOnClickListener((CustomInputShoppingItemCard) holder);
		} else if (holder instanceof ShoppingItemCard) {
			switch (shoppingItemType) {
				case MEAT_ITEM:
					((ShoppingItemCard) holder).setItem(MEAT_ITEM, "삽겹살", "200g", "인분", 4000);
					break;
				case ALCOHOL_ITEM:
					((ShoppingItemCard) holder).setItem(ALCOHOL_ITEM, "참이슬", "1병", "병", 1000);
					break;
				case OTHERS_ITEM:
					((ShoppingItemCard) holder).setItem(OTHERS_ITEM, "종이접시", "50개 set", "set", 2000);
					break;
			}
			((ShoppingItemCard) holder).getLayout()
					.setOnClickListener((ShoppingItemCard) holder);
		}
	}

	@Override
	public int getItemCount() {
		return 10;
	}

	@Override
	public int getItemViewType(int position) {
		switch (position) {
			case 0:
				return TYPE_BLANK_HEADER;
			case 1:
				return TYPE_CUSTOM_INPUT_SHOPPING_ITEM_CARD;
			default:
				return TYPE_SHOPPING_ITEM_CARD;
		}
	}

	private static class ShoppingItemCard extends RecyclerView.ViewHolder implements View.OnClickListener {

		private RelativeLayout shoppingItemCardLayout;
		private TextView itemNameTextView;
		private TextView itemUnitTextView;
		private TextView itemPriceTextView;
		private ShoppingItemListFragment.OnShoppingItemListFragmentListener mOnShoppingItemListFragmentListener;
		private Context mContext;

		private int itemType;
		private String itemName;
		private String itemUnit;
		private String itemUnitCount;
		private int itemPrice;

		public ShoppingItemCard(View cardView,
								ShoppingItemListFragment.OnShoppingItemListFragmentListener
										onShoppingItemListFragmentListener,
								Context context) {
			super(cardView);
			shoppingItemCardLayout = (RelativeLayout) cardView.findViewById(R.id.RelativeLayout_card_shopping_item);
			itemNameTextView = (TextView) cardView.findViewById(R.id.textView_name_item);
			itemUnitTextView = (TextView) cardView.findViewById(R.id.textView_unit_item);
			itemPriceTextView = (TextView) cardView.findViewById(R.id.textView_price_item);
			mOnShoppingItemListFragmentListener = onShoppingItemListFragmentListener;
			mContext = context;
		}

		public void setItem(int itemType, String itemName, String itemUnit, String itemUnitCount,int itemPrice) {
			this.itemNameTextView.setText(itemName);
			this.itemUnitTextView.setText(itemUnit);
			String itemPriceString = String.valueOf(itemPrice);
			String itemPriceStringChanged = "";
			if(itemPriceString.length() > 0) {
				int charCounter = 0;
				for (int i = itemPriceString.length() - 1; i >= 0; i--) {
					if(charCounter != 0 && charCounter % 3 == 0)
						itemPriceStringChanged += "," + itemPriceString.charAt(i);
					else
						itemPriceStringChanged += itemPriceString.charAt(i) ;
					charCounter++;
				}
			}
			this.itemPriceTextView.setText(mContext.getResources().getString(R.string.currency_unit)
					+ new StringBuffer(itemPriceStringChanged).reverse().toString());

			this.itemType = itemType;
			this.itemName = itemName;
			this.itemUnit = itemUnit;
			this.itemUnitCount = itemUnitCount;
			this.itemPrice = itemPrice;
		}

		public RelativeLayout getLayout() {
			return shoppingItemCardLayout;
		}

		@Override
		public void onClick(View v) {
			mOnShoppingItemListFragmentListener.onClickItem(itemType, itemName, itemUnit, itemUnitCount, itemPrice);
		}
	}

	private static class CustomInputShoppingItemCard extends RecyclerView.ViewHolder implements View.OnClickListener {

		private FrameLayout customInputShoppingItemCardLayout;
		private ShoppingItemListFragment.OnShoppingItemListFragmentListener mOnShoppingItemListFragmentListener;
		private int itemType;

		public CustomInputShoppingItemCard(View cardView,
										   ShoppingItemListFragment.OnShoppingItemListFragmentListener
												   onShoppingItemListFragmentListener) {
			super(cardView);
			customInputShoppingItemCardLayout = (FrameLayout) cardView.findViewById(R.id.FrameLayout_card_custom_input_shopping_item);
			mOnShoppingItemListFragmentListener = onShoppingItemListFragmentListener;
		}

		public void setItemType(int itemType) {
			this.itemType = itemType;
		}

		public FrameLayout getLayout() {
			return customInputShoppingItemCardLayout;
		}

		@Override
		public void onClick(View v) {
			mOnShoppingItemListFragmentListener.onClickItem(itemType);
		}
	}

	private static class BlankHeader extends RecyclerView.ViewHolder {
		public BlankHeader(View itemView) {
			super(itemView);
		}
	}
}
