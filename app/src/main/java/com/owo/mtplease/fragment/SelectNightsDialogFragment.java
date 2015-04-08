package com.owo.mtplease.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.owo.mtplease.R;

/**
 * A simple {@link DialogFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectNightsDialogFragment.OnNightsConfirmedListener} interface
 * to handle interaction events.
 * Use the {@link SelectNightsDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectNightsDialogFragment extends DialogFragment {
	private static final String TAG = "SelectNightsDialogFragment";
	private static final String DIALOG_CALLER_FLAG = "param1";

	private int _callerFlag;

	private OnNightsConfirmedListener  _mOnNightsConfirmedNListener;

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param callerFlag the flag value that discriminate what activity or fragment is the caller of this dialog.
	 * @return A new instance of fragment SelectNightsFragment.
	 */
	public static SelectNightsDialogFragment newInstance(int callerFlag) {
		SelectNightsDialogFragment fragment = new SelectNightsDialogFragment();
		Bundle args = new Bundle();
		args.putInt(DIALOG_CALLER_FLAG, callerFlag);
		fragment.setArguments(args);
		return fragment;
	}

	public SelectNightsDialogFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			_callerFlag = getArguments().getInt(DIALOG_CALLER_FLAG);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View selectNightsDialogView = inflater.inflate(R.layout.dialogfragment_select_nights, container, false);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

/*
		final TextView daysTextView = (TextView) selectNightsDialogView.findViewById(R.id.textView_day);
		daysTextView.setText("2");
*/

		final TextView nightsTextView = (TextView) selectNightsDialogView.findViewById(R.id.textView_header_nights);
		nightsTextView.setText(1 + "");
		final TextView daysTextView = (TextView) selectNightsDialogView.findViewById(R.id.textView_header_day);
		daysTextView.setText(2 + "");

		final NumberPicker numberPicker = (NumberPicker) selectNightsDialogView.findViewById(R.id.numberPicker_select_nights);
		numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				nightsTextView.setText(newVal + "");
				daysTextView.setText(newVal + 1 + "");
			}
		});
		numberPicker.setValue(1);
		numberPicker.setMinValue(1);
		numberPicker.setMaxValue(31);


		Button confirmButton = (Button) selectNightsDialogView.findViewById(R.id.btn_confirm);
		confirmButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_mOnNightsConfirmedNListener.onClickNightsConfirmButton(numberPicker.getValue());
				dismiss();
			}
		});

		Button cancelButton = (Button) selectNightsDialogView.findViewById(R.id.btn_cancel);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		return selectNightsDialogView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			_mOnNightsConfirmedNListener = (OnNightsConfirmedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		_mOnNightsConfirmedNListener = null;
	}

	public interface OnNightsConfirmedListener {
		public void onClickNightsConfirmButton(int nights);
	}
}
