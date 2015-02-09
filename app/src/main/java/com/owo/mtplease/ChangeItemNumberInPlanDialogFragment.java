package com.owo.mtplease;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;


/**
 * A simple {@link DialogFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangeItemNumberInPlanDialogFragment.OnChangeItemNumberInPlanDialogFragmentListener} interface
 * to handle interaction events.
 * Use the {@link ChangeItemNumberInPlanDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeItemNumberInPlanDialogFragment extends DialogFragment {

	private static final String ARG_ITEM_TYPE = "param1";
	private static final String ARG_ITEM_NAME = "param2";
	private static final String ARG_UNIT_OF_ITEM = "param3";
	private static final String ARG_UNIT_PRICE_OF_ITEM = "param4";
	private static final String ARG_NUMBER_OF_ITEMS = "param5";
	private static final String ARG_UNIT_COUNT_OF_ITEM = "param6";

	private int itemType;
	private String itemName;
	private String itemUnit;
	private int itemUnitPrice;
	private int itemCount;	// number of items after being changed
	private String itemCountUnit;

	private TextView itemNameTextView;
	private TextView itemUnitTextView;
	private NumberPicker itemNumberPicker;
	private TextView itemUnitPriceTextView;
	private TextView itemCountUnitTextView;
	private TextView itemTotalPriceTextView;
	private Button changeItemButton;
	private Button cancelItemButton;

	private OnChangeItemNumberInPlanDialogFragmentListener
			mOnChangeItemNumberInPlanDialogFragmentListener;

	public ChangeItemNumberInPlanDialogFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param itemType      Type of the item
	 * @param itemName      Name of the item
	 * @param itemUnit      Unit of the item
	 * @param itemUnitPrice Unit Price of the item
	 * @param itemCount     Number of items
	 * @param itemCountUnit Count of the item
	 * @return A new instance of fragment ChangeItemCountInPlanDialogFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ChangeItemNumberInPlanDialogFragment newInstance(int itemType, String itemName, String itemUnit, int itemUnitPrice,
																   int itemCount, String itemCountUnit) {
		ChangeItemNumberInPlanDialogFragment fragment = new ChangeItemNumberInPlanDialogFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_ITEM_TYPE, itemType);
		args.putString(ARG_ITEM_NAME, itemName);
		args.putString(ARG_UNIT_OF_ITEM, itemUnit);
		args.putInt(ARG_UNIT_PRICE_OF_ITEM, itemUnitPrice);
		args.putInt(ARG_NUMBER_OF_ITEMS, itemCount);
		args.putString(ARG_UNIT_COUNT_OF_ITEM, itemCountUnit);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			itemType = getArguments().getInt(ARG_ITEM_TYPE);
			itemName = getArguments().getString(ARG_ITEM_NAME);
			itemUnit = getArguments().getString(ARG_UNIT_OF_ITEM);
			itemUnitPrice = getArguments().getInt(ARG_UNIT_PRICE_OF_ITEM);
			itemCount = getArguments().getInt(ARG_NUMBER_OF_ITEMS);
			itemCountUnit = getArguments().getString(ARG_UNIT_COUNT_OF_ITEM);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View changeItemNumberInPlanDialogFragmentView = inflater.
				inflate(R.layout.fragment_change_item_number_in_plan_dialog, container, false);

		itemNameTextView = (TextView) changeItemNumberInPlanDialogFragmentView.findViewById(R.id.textView_name_item_dialog_plan);
		itemNameTextView.setText(itemName);

		itemUnitTextView = (TextView) changeItemNumberInPlanDialogFragmentView.findViewById(R.id.textView_unit_item_dialog_plan);
		itemUnitTextView.setText(itemUnit);

		itemUnitPriceTextView = (TextView) changeItemNumberInPlanDialogFragmentView.findViewById(R.id.textView_unit_price_item_dialog_plan);
		itemUnitPriceTextView.setText(castItemPriceToString(itemUnitPrice));

		itemTotalPriceTextView = (TextView) changeItemNumberInPlanDialogFragmentView.findViewById(R.id.textView_price_total_item_dialog_plan);
		itemTotalPriceTextView.setText(castItemPriceToString(itemCount * itemUnitPrice));

		itemNumberPicker = (NumberPicker) changeItemNumberInPlanDialogFragmentView.findViewById(R.id.numberPicker_number_item_dialog_plan);
		itemNumberPicker.setMaxValue(500);
		itemNumberPicker.setMinValue(0);
		itemNumberPicker.setValue(itemCount);
		itemNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				itemTotalPriceTextView.setText(castItemPriceToString(itemUnitPrice * newVal));
				itemCount = newVal;
			}
		});

		itemCountUnitTextView = (TextView) changeItemNumberInPlanDialogFragmentView.findViewById(R.id.textView_count_unit_item_dialog_plan);
		itemCountUnitTextView.setText(itemCountUnit);

		cancelItemButton = (Button) changeItemNumberInPlanDialogFragmentView.findViewById(R.id.btn_cancel_item_dialog_plan);
		cancelItemButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		changeItemButton = (Button) changeItemNumberInPlanDialogFragmentView.findViewById(R.id.btn_change_item_dialog_plan);
		changeItemButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnChangeItemNumberInPlanDialogFragmentListener.onClickChangeButton(itemType, itemName, itemUnitPrice, itemCount);
				dismiss();
			}
		});

		getDialog().setTitle(R.string.please_change_number_of_items);

		return changeItemNumberInPlanDialogFragmentView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOnChangeItemNumberInPlanDialogFragmentListener
					= (OnChangeItemNumberInPlanDialogFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mOnChangeItemNumberInPlanDialogFragmentListener = null;
	}

	private String castItemPriceToString(int price) {
		String totalRoomCostString = String.valueOf(price);
		String totalRoomCostStringChanged = "";

		if (totalRoomCostString.length() > 0) {
			int charCounter = 0;
			for (int i = totalRoomCostString.length() - 1; i >= 0; i--) {
				if (charCounter != 0 && charCounter % 3 == 0) {
					totalRoomCostStringChanged += "," + totalRoomCostString.charAt(i);
				} else {
					totalRoomCostStringChanged += totalRoomCostString.charAt(i);
				}
				charCounter++;
			}
		}
		return getResources().getString(R.string.currency_unit) +
				new StringBuffer(totalRoomCostStringChanged).reverse().toString();
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 */
	public interface OnChangeItemNumberInPlanDialogFragmentListener {
		// TODO: Update argument type and name
		public void onClickChangeButton(int itemType, String itemName, int itemUnitPrice, int newItemCount);
	}

}
