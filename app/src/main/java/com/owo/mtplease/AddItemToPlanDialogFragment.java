package com.owo.mtplease;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by In-Ho on 2015-01-27.
 */
public class AddItemToPlanDialogFragment extends DialogFragment {
	private static final String TAG = "AddItemToPlanDialogFragment";

	private static final String ARG_SHOPPING_ITEM_TYPE = "param1";
	private static final String ARG_NAME_OF_ITEM = "param2";
	private static final String ARG_UNIT_OF_ITEM = "param3";
	private static final String ARG_COUNT_UNIT_OF_ITEM = "param4";
	private static final String ARG_PRICE_OF_ITEM = "param5";
	private static final String ARG_INPUT_MODE = "param6";

	private int itemType;
	private String itemName;
	private String itemUnit;
	private String itemCountUnit;
	private int itemCount = 0;
	private int itemUnitPrice;

	// User Interface Views
	private TextView itemNameTextView;
	private TextView itemUnitTextView;
	private NumberPicker itemNumberPicker;
	private TextView itemUnitPriceTextView;
	private TextView itemCountUnitTextView;
	private TextView itemTotalPriceTextView;
	private Button addItemButton;
	private Button cancelItemButton;
	private EditText customItemNameEditText;
	private EditText customItemUnitEditText;
	private EditText customItemUnitPriceEditText;
	private EditText customItemCountUnitEditText;
	private TextView customItemTotalPriceTextView;
	// End of the UI views

	// Controllers
	private TextWatcher editTextWatcherForCustomItemPrice = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) throws NumberFormatException {
			try {
				if(!s.toString().equals("")) {
					itemUnitPrice = Integer.parseInt(s.toString());
					customItemTotalPriceTextView.setText(castItemPriceToString(itemNumberPicker.getValue() * itemUnitPrice));
				} else {
					switch(itemType) {
						case MEAT_ITEM:
							customItemTotalPriceTextView.setHint(R.string.meat_price_hint);
							break;
						case ALCOHOL_ITEM:
							customItemTotalPriceTextView.setHint(R.string.alcohol_price_hint);
							break;
						case OTHERS_ITEM:
							customItemTotalPriceTextView.setHint(R.string.others_price_hint);
							break;
					}
				}
			} catch (NumberFormatException e) {
				Toast.makeText(getActivity(), R.string.please_type_only_number_for_price, Toast.LENGTH_SHORT).show();
				switch(itemType) {
					case MEAT_ITEM:
						customItemTotalPriceTextView.setHint(R.string.meat_price_hint);
						break;
					case ALCOHOL_ITEM:
						customItemTotalPriceTextView.setHint(R.string.alcohol_price_hint);
						break;
					case OTHERS_ITEM:
						customItemTotalPriceTextView.setHint(R.string.others_price_hint);
						break;
				}
				e.printStackTrace();
			}
		}
	};
	// End of the controllers

	// Listeners
	private OnAddItemToPlanDialogFragmentListener mOnAddItemToPlanDialogFragmentListener;
	// End of the listeners

	// Flags
	private static final int CUSTOM_SHOPPING_ITEM_ARG_INPUT_MODE = 1;
	private static final int SHOPPING_ITEM_ARG_INPUT_MODE = 2;
	private static final int MEAT_ITEM = 1;
	private static final int ALCOHOL_ITEM = 2;
	private static final int OTHERS_ITEM = 3;
	// End of the flags

	// Others
	private int inputMode;
	// End of the others
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param itemType       Parameter 1.
	 * @param itemName       Parameter 2.
	 * @param itemUnit       Parameter 3.
	 * @param itemCountUnit  Parameter 4.
	 * @param itemUnitPrice      Parameter 5.
	 * @param inputMode      Parameter 6.
	 * @return A new instance of fragment AddItemToPlanDialogFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static AddItemToPlanDialogFragment newInstance(int itemType, String itemName, String itemUnit, String itemCountUnit, int itemUnitPrice, int inputMode) {
		AddItemToPlanDialogFragment fragment = new AddItemToPlanDialogFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SHOPPING_ITEM_TYPE, itemType);
		args.putString(ARG_NAME_OF_ITEM, itemName);
		args.putString(ARG_UNIT_OF_ITEM, itemUnit);
		args.putString(ARG_COUNT_UNIT_OF_ITEM, itemCountUnit);
		args.putInt(ARG_PRICE_OF_ITEM, itemUnitPrice);
		args.putInt(ARG_INPUT_MODE, inputMode);
		fragment.setArguments(args);
		return fragment;
	}
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param inputMode    Parameter 1.
	 * @return A new instance of fragment AddToPlanDialogFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static AddItemToPlanDialogFragment newInstance(int itemType, int inputMode) {
		AddItemToPlanDialogFragment fragment = new AddItemToPlanDialogFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SHOPPING_ITEM_TYPE, itemType);
		args.putInt(ARG_INPUT_MODE, inputMode);
		fragment.setArguments(args);
		return fragment;
	}

	public AddItemToPlanDialogFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			itemType = getArguments().getInt(ARG_SHOPPING_ITEM_TYPE);
			itemName = getArguments().getString(ARG_NAME_OF_ITEM);
			itemUnit = getArguments().getString(ARG_UNIT_OF_ITEM);
			itemCountUnit = getArguments().getString(ARG_COUNT_UNIT_OF_ITEM);
			itemUnitPrice = getArguments().getInt(ARG_PRICE_OF_ITEM);
			inputMode = getArguments().getInt(ARG_INPUT_MODE);
		} else {
			itemType = getArguments().getInt(ARG_SHOPPING_ITEM_TYPE);
			inputMode = getArguments().getInt(ARG_INPUT_MODE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View addItemToPlanDialogFragmentView;

		if(inputMode == SHOPPING_ITEM_ARG_INPUT_MODE) {
			// Inflate the layout for this fragment
			addItemToPlanDialogFragmentView = inflater.
					inflate(R.layout.fragment_add_item_to_plan_dialog, container, false);

			getDialog().setTitle(R.string.please_configure_number_of_item);

			itemNameTextView = (TextView) addItemToPlanDialogFragmentView.
					findViewById(R.id.textView_name_item_dialog);
			itemNameTextView.setText(itemName);

			itemUnitTextView = (TextView) addItemToPlanDialogFragmentView.
					findViewById(R.id.textView_unit_item_dialog);
			itemUnitTextView.setText("(" + itemUnit + ")");

			itemUnitPriceTextView = (TextView) addItemToPlanDialogFragmentView.
					findViewById(R.id.textView_unit_price_item_dialog);
			itemUnitPriceTextView.setText(castItemPriceToString(itemUnitPrice));

			itemTotalPriceTextView = (TextView) addItemToPlanDialogFragmentView.
					findViewById(R.id.textView_price_total_item_dialog);
			itemTotalPriceTextView.setText(castItemPriceToString(0));

			itemNumberPicker = (NumberPicker) addItemToPlanDialogFragmentView.
					findViewById(R.id.numberPicker_number_item_dialog);
			itemNumberPicker.setMaxValue(500);
			itemNumberPicker.setMinValue(0);
			itemNumberPicker.setWrapSelectorWheel(true);
			itemNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
				@Override
				public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
					itemTotalPriceTextView.setText(castItemPriceToString(itemUnitPrice * newVal));
					itemCount = newVal;
				}
			});
			itemNumberPicker.setValue(0);

			itemCountUnitTextView = (TextView) addItemToPlanDialogFragmentView.
					findViewById(R.id.textView_count_unit_item_dialog);
			itemCountUnitTextView.setText(itemCountUnit);

			addItemButton = (Button) addItemToPlanDialogFragmentView.
					findViewById(R.id.btn_add_item_dialog);
			addItemButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(itemCount > 0) {
						mOnAddItemToPlanDialogFragmentListener.
								onClickAddItemToPlanButton(itemType, itemName, itemUnitPrice, itemCount, itemUnit, itemCountUnit);
						dismiss();
					} else {
						Toast.makeText(getActivity(), R.string.please_type_more_than_zero, Toast.LENGTH_SHORT).show();
					}
				}
			});

			cancelItemButton = (Button) addItemToPlanDialogFragmentView.findViewById(R.id.btn_cancel_item_dialog);
			cancelItemButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
		} else {
			// Inflate the layout for this fragment
			addItemToPlanDialogFragmentView = inflater.
					inflate(R.layout.fragment_add_custom_item_to_plan_dialog, container, false);

			getDialog().setTitle(R.string.please_type_by_yourself);

			customItemNameEditText = (EditText) addItemToPlanDialogFragmentView.
					findViewById(R.id.editText_name_custom_item_dialog);
			customItemNameEditText.setNextFocusDownId(R.id.editText_unit_custom_item_dialog);

			customItemUnitEditText = (EditText) addItemToPlanDialogFragmentView.
					findViewById(R.id.editText_unit_custom_item_dialog);
			customItemUnitEditText.setNextFocusDownId(R.id.editText_price_custom_dialog);

			customItemTotalPriceTextView = (TextView) addItemToPlanDialogFragmentView.
					findViewById(R.id.textView_price_total_item_custom_dialog);
			customItemTotalPriceTextView.setText(castItemPriceToString(0));

			itemNumberPicker = (NumberPicker) addItemToPlanDialogFragmentView.
					findViewById(R.id.numberPicker_number_custom_item_dialog);
			itemNumberPicker.setMaxValue(500);
			itemNumberPicker.setMinValue(0);
			itemNumberPicker.setWrapSelectorWheel(true);
			itemNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
				@Override
				public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
					try {
						customItemTotalPriceTextView.setText(
								castItemPriceToString(Integer.parseInt(customItemUnitPriceEditText.getText().toString()) * newVal));
						itemCount = newVal;
					} catch (NumberFormatException e) {
						Toast.makeText(getActivity(), R.string.please_type_price_first, Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				}
			});
			itemNumberPicker.setValue(0);

			customItemUnitPriceEditText = (EditText) addItemToPlanDialogFragmentView.
					findViewById(R.id.editText_price_custom_dialog);
			customItemUnitPriceEditText.addTextChangedListener(editTextWatcherForCustomItemPrice);
			customItemUnitPriceEditText.setNextFocusDownId(R.id.editText_count_unit_custom_item_dialog);

			customItemCountUnitEditText = (EditText) addItemToPlanDialogFragmentView.
					findViewById(R.id.editText_count_unit_custom_item_dialog);

			switch(itemType) {
				case MEAT_ITEM:
					setHintsForEditText(R.string.meat_name_hint, R.string.meat_unit_hint,
							R.string.meat_unit_count_hint, R.string.meat_price_hint);
					break;
				case ALCOHOL_ITEM:
					setHintsForEditText(R.string.alcohol_name_hint, R.string.alcohol_unit_hint,
							R.string.alcohol_unit_count_hint, R.string.alcohol_price_hint);
					break;
				case OTHERS_ITEM:
					setHintsForEditText(R.string.others_name_hint, R.string.others_unit_hint,
							R.string.others_unit_count_hint, R.string.others_price_hint);
					break;
			}

			addItemButton = (Button) addItemToPlanDialogFragmentView.findViewById(R.id.btn_add_custom_item_dialog);
			addItemButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(itemCount > 0) {
						itemName = customItemNameEditText.getText().toString();
						itemUnit = customItemUnitEditText.getText().toString();
						itemCountUnit = customItemCountUnitEditText.getText().toString();
						String tempPrice = customItemUnitPriceEditText.getText().toString();

						if (!itemName.equals("") && !itemUnit.equals("") && !itemCountUnit.equals("")
								&& !tempPrice.equals("")) {
							mOnAddItemToPlanDialogFragmentListener.onClickAddItemToPlanButton(itemType, itemName,
									itemUnitPrice, itemCount, itemUnit, itemCountUnit);
							dismiss();
						} else {
							Toast.makeText(getActivity(), R.string.please_type_every_blanks, Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getActivity(), R.string.please_type_more_than_zero, Toast.LENGTH_SHORT).show();
					}
				}
			});

			cancelItemButton = (Button) addItemToPlanDialogFragmentView.findViewById(R.id.btn_cancel_custom_item_dialog);
			cancelItemButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});

		}

		return addItemToPlanDialogFragmentView;
	}

	private String castItemPriceToString(int itemPrice) {
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
		return (getResources().getString(R.string.currency_unit) + new StringBuffer(itemPriceStringChanged).reverse().toString());
	}

	private void setHintsForEditText(int itemNameHintStringResId, int itemUnitHintStringResId,
									 int itemCountUnitHintStringResId, int itemPriceHintStringResId) {
		customItemNameEditText.setHint(itemNameHintStringResId);
		customItemUnitEditText.setHint(itemUnitHintStringResId);
		customItemCountUnitEditText.setHint(itemCountUnitHintStringResId);
		customItemUnitPriceEditText.setHint(itemPriceHintStringResId);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOnAddItemToPlanDialogFragmentListener = (OnAddItemToPlanDialogFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mOnAddItemToPlanDialogFragmentListener = null;
	}

	public interface OnAddItemToPlanDialogFragmentListener {
		// TODO: Update argument type and name
		public void onClickAddItemToPlanButton(int itemType, String itemName, int itemUnitPrice, int itemCount, String itemUnit, String itemCountUnit);
	}

}
