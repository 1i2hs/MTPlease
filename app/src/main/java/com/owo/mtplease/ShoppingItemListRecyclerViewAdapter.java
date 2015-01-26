package com.owo.mtplease;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
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

	private static final int TYPE_CUSTOM_INPUT_SHOPPING_ITEM_CARD = 0;
	private static final int TYPE_SHOPPING_ITEM_CARD = 1;

	private static final int MEAT_ITEM = 1;
	private static final int ALCOHOL_ITEM = 2;
	private static final int OTHERS_ITEM = 3;

	private int shoppingItemType;
	private FragmentManager mFragmentManager;

	public ShoppingItemListRecyclerViewAdapter(int shoppingItemType, FragmentManager fragmentManager) {
		this.shoppingItemType = shoppingItemType;
		mFragmentManager = fragmentManager;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if(viewType == TYPE_CUSTOM_INPUT_SHOPPING_ITEM_CARD) {
			View customShoppingItemView = LayoutInflater.from(parent.getContext()).
					inflate(R.layout.card_custom_shopping_item_input, parent, false);

			return new CustomInputShoppingItemCard(customShoppingItemView);
		} else if(viewType == TYPE_SHOPPING_ITEM_CARD){
			View shoppingItemView = LayoutInflater.from(parent.getContext()).
					inflate(R.layout.card_shopping_item, parent, false);

			return new ShoppingItemCard(shoppingItemView);
		}

		throw new RuntimeException("there is no type that matches the type"
				+ viewType + "make sure your using types correctly");
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if(holder instanceof CustomInputShoppingItemCard) {
			((CustomInputShoppingItemCard) holder).getLayout()
					.setOnClickListener((CustomInputShoppingItemCard) holder);
		} else if(holder instanceof  ShoppingItemCard) {
			switch (shoppingItemType) {
				case MEAT_ITEM:
					((ShoppingItemCard) holder).setItem("삽겹살", "200g", "4,000원");
					break;
				case ALCOHOL_ITEM:
					((ShoppingItemCard) holder).setItem("참이슬", "1병", "1,000원");
					break;
				case OTHERS_ITEM:
					((ShoppingItemCard) holder).setItem("종이접시", "50개 set", "2,000원");
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
		if (isPositionHeader(position))
			return TYPE_CUSTOM_INPUT_SHOPPING_ITEM_CARD;

		return TYPE_SHOPPING_ITEM_CARD;
	}

	private boolean isPositionHeader(int position) {
		return position == 0;
	}

	public static class ShoppingItemCard extends RecyclerView.ViewHolder implements View.OnClickListener {

		private RelativeLayout shoppingItemCardLayout;
		private TextView itemName;
		private TextView itemUnit;
		private TextView itemPrice;

		public ShoppingItemCard(View cardView) {
			super(cardView);
			shoppingItemCardLayout = (RelativeLayout) cardView.findViewById(R.id.RelativeLayout_card_shopping_item);
			itemName = (TextView) cardView.findViewById(R.id.textView_name_item);
			itemUnit = (TextView) cardView.findViewById(R.id.textView_unit_item);
			itemPrice = (TextView) cardView.findViewById(R.id.textView_price_item);
		}

		public void setItem(String itemName, String itemUnit, String itemPrice) {
			this.itemName.setText(itemName);
			this.itemUnit.setText(itemUnit);
			this.itemPrice.setText(itemPrice);
		}

		public RelativeLayout getLayout() {
			return shoppingItemCardLayout;
		}

		@Override
		public void onClick(View v) {

		}
	}

	public static class CustomInputShoppingItemCard extends RecyclerView.ViewHolder implements View.OnClickListener {

		private FrameLayout customInputShoppingItemCardLayout;

		public CustomInputShoppingItemCard(View cardView) {
			super(cardView);
			customInputShoppingItemCardLayout = (FrameLayout) cardView.findViewById(R.id.FrameLayout_card_custom_input_shopping_item);
		}

		public FrameLayout getLayout() {
			return customInputShoppingItemCardLayout;
		}

		@Override
		public void onClick(View v) {

		}
	}
}
