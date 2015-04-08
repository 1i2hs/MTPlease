package com.owo.mtplease.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.owo.mtplease.R;


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

	private int _itemType;
	private String _itemName;
	private String _itemUnit;
	private int _itemUnitPrice;
	private int _itemCount;	// number of items after being changed
	private String _itemCountUnit;

	private TextView _itemNameTextView;
	private TextView _itemUnitTextView;
	private NumberPicker itemNumberPicker;
	private TextView _itemUnitPriceTextView;
	private TextView _itemCountUnitTextView;
	private TextView _itemTotalPriceTextView;
	private Button _changeItemButton;
	private Button cancelItemButton;

	private OnChangeItemNumberInPlanDialogFragmentListener
			_mOnChangeItemNumberInPlanDialogFragmentListener;

	public ChangeItemNumberInPlanDialogFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param _itemType      Type of the item
	 * @param _itemName      Name of the item
	 * @param _itemUnit      Unit of the item
	 * @param _itemUnitPrice Unit Price of the item
	 * @param _itemCount     Number of items
	 * @param _itemCountUnit Count of the item
	 * @return A new instance of fragment ChangeItemCountInPlanDialogFragment.
	 */
	public static ChangeItemNumberInPlanDialogFragment newInstance(int _itemType, String _itemName, String _itemUnit, int _itemUnitPrice,
																   int _itemCount, String _itemCountUnit) {
		ChangeItemNumberInPlanDialogFragment fragment = new ChangeItemNumberInPlanDialogFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_ITEM_TYPE, _itemType);
		args.putString(ARG_ITEM_NAME, _itemName);
		args.putString(ARG_UNIT_OF_ITEM, _itemUnit);
		args.putInt(ARG_UNIT_PRICE_OF_ITEM, _itemUnitPrice);
		args.putInt(ARG_NUMBER_OF_ITEMS, _itemCount);
		args.putString(ARG_UNIT_COUNT_OF_ITEM, _itemCountUnit);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			_itemType = getArguments().getInt(ARG_ITEM_TYPE);
			_itemName = getArguments().getString(ARG_ITEM_NAME);
			_itemUnit = getArguments().getString(ARG_UNIT_OF_ITEM);
			_itemUnitPrice = getArguments().getInt(ARG_UNIT_PRICE_OF_ITEM);
			_itemCount = getArguments().getInt(ARG_NUMBER_OF_ITEMS);
			_itemCountUnit = getArguments().getString(ARG_UNIT_COUNT_OF_ITEM);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View changeItemNumberInPlanDialogFragmentView = inflater.
				inflate(R.layout.fragment_change_item_number_in_plan_dialog, container, false);

		_itemNameTextView = (TextView) changeItemNumberInPlanDialogFragmentView.findViewById(R.id.textView_name_item_dialog_plan);
		_itemNameTextView.setText(_itemName);

		_itemUnitTextView = (TextView) changeItemNumberInPlanDialogFragmentView.findViewById(R.id.textView_unit_item_dialog_plan);
		_itemUnitTextView.setText(_itemUnit);

		_itemUnitPriceTextView = (TextView) changeItemNumberInPlanDialogFragmentView.findViewById(R.id.textView_unit_price_item_dialog_plan);
		_itemUnitPriceTextView.setText(_castItemPriceToString(_itemUnitPrice));

		_itemTotalPriceTextView = (TextView) changeItemNumberInPlanDialogFragmentView.findViewById(R.id.textView_price_total_item_dialog_plan);
		_itemTotalPriceTextView.setText(_castItemPriceToString(_itemCount * _itemUnitPrice));

		itemNumberPicker = (NumberPicker) changeItemNumberInPlanDialogFragmentView.findViewById(R.id.numberPicker_number_item_dialog_plan);
		itemNumberPicker.setMaxValue(500);
		itemNumberPicker.setMinValue(0);
		itemNumberPicker.setValue(_itemCount);
		itemNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				_itemTotalPriceTextView.setText(_castItemPriceToString(_itemUnitPrice * newVal));
				_itemCount = newVal;
			}
		});

		_itemCountUnitTextView = (TextView) changeItemNumberInPlanDialogFragmentView.findViewById(R.id.textView_count_unit_item_dialog_plan);
		_itemCountUnitTextView.setText(_itemCountUnit);

		cancelItemButton = (Button) changeItemNumberInPlanDialogFragmentView.findViewById(R.id.btn_cancel_item_dialog_plan);
		cancelItemButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		_changeItemButton = (Button) changeItemNumberInPlanDialogFragmentView.findViewById(R.id.btn_change_item_dialog_plan);
		_changeItemButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_mOnChangeItemNumberInPlanDialogFragmentListener.onClickChangeButton(_itemType, _itemName, _itemUnitPrice, _itemCount);
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
			_mOnChangeItemNumberInPlanDialogFragmentListener
					= (OnChangeItemNumberInPlanDialogFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		_mOnChangeItemNumberInPlanDialogFragmentListener = null;
	}

	private String _castItemPriceToString(int price) {
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
		public void onClickChangeButton(int _itemType, String _itemName, int _itemUnitPrice, int newItemCount);
	}

}
