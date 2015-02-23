package com.owo.mtplease.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.owo.mtplease.R;

import java.util.Calendar;


public class AddToPlanDialogFragment extends DialogFragment {

	private static final String TAG = "AddToPlanDialogFragment";
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String DATE_OF_MT = "param1";
	private static final String REGION_OF_MT = "param2";
	private static final String NUMBER_OF_PEOPLE = "param3";
	private static final String SEARCH_MODE = "param4";
	private static final int CALL_FROM_ADDTOPLANDIALOG = 3;
	private static final int CONDITION_SEARCH_MODE = 1;
	private static final int KEYWORD_SEARCH_MODE = 2;

	// TODO: Rename and change types of parameters
	private String dateOfMT;
	private int regionOfMT;
	private int numberOfPeople;
	private int searchMode;

	// User Interface Views
	private Button dateSelectDialogButton;
	private Spinner regionSelectDialogButton;
	private EditText numberOfPeopleSelectDialogEditText;
	private TextView numberOfMaleTextView;
	private TextView numberOfFemaleTextView;
	private SeekBar sexRatioDialogSeekBar;
	private Button confirmButton;
	private Button cancelButton;
	// End of the UI views

	private OnAddToPlanDialogFragmentInteractionListener mOnAddToPlanDialogFragmentInteractionListener;

	private Calendar calendar = Calendar.getInstance();

	private int sexRatioProgress;

	private TextWatcher editTextWatcherForNumberOfPeopleSelectDialog = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) throws NumberFormatException {
			try {
				sexRatioDialogSeekBar.setMax(Integer.parseInt(s.toString()));
			} catch (NumberFormatException e) {
				sexRatioDialogSeekBar.setMax(0);
				e.printStackTrace();
			}
		}
	};

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param dateOfMT       Parameter 1.
	 * @param regionOfMT     Parameter 2.
	 * @param numberOfPeople Parameter 3.
	 * @param searchMode     Parameter 4.
	 * @return A new instance of fragment AddToPlanDialogFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static AddToPlanDialogFragment newInstance(String dateOfMT, int regionOfMT, int numberOfPeople, int searchMode) {
		AddToPlanDialogFragment fragment = new AddToPlanDialogFragment();
		Bundle args = new Bundle();
		args.putString(DATE_OF_MT, dateOfMT);
		args.putInt(REGION_OF_MT, regionOfMT);
		args.putInt(NUMBER_OF_PEOPLE, numberOfPeople);
		args.putInt(SEARCH_MODE, searchMode);
		fragment.setArguments(args);
		return fragment;
	}

	public AddToPlanDialogFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			dateOfMT = getArguments().getString(DATE_OF_MT);
			regionOfMT = getArguments().getInt(REGION_OF_MT);
			numberOfPeople = getArguments().getInt(NUMBER_OF_PEOPLE);
			searchMode = getArguments().getInt(SEARCH_MODE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View addToPlanDialogFragmentView = inflater.inflate(R.layout.fragment_add_to_plan_dialog, container, false);

		getDialog().setTitle(R.string.input_basic_data);

		dateSelectDialogButton = (Button) addToPlanDialogFragmentView.findViewById(R.id.btn_select_date_dialog);
		dateSelectDialogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CalendarDialogFragment calendarDialogFragment =
						CalendarDialogFragment.newInstance(CALL_FROM_ADDTOPLANDIALOG);
				calendarDialogFragment
						.show(getActivity().getSupportFragmentManager(), "calendar_dialog_popped");
			}
		});

		regionSelectDialogButton =
				(Spinner) addToPlanDialogFragmentView.findViewById(R.id.spinner_select_region_dialog);
		ArrayAdapter<CharSequence> regionSpinnerDialogAdapter =
				ArrayAdapter.createFromResource(getActivity(), R.array.array_region, R.layout.spinner_region);
		regionSpinnerDialogAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		regionSelectDialogButton.setAdapter(regionSpinnerDialogAdapter);

		numberOfPeopleSelectDialogEditText =
				(EditText) addToPlanDialogFragmentView.findViewById(R.id.editText_number_people_dialog);
		numberOfPeopleSelectDialogEditText.addTextChangedListener(editTextWatcherForNumberOfPeopleSelectDialog);

		numberOfMaleTextView =
				(TextView) addToPlanDialogFragmentView.findViewById(R.id.textView_number_male_dialog);
		numberOfMaleTextView.setText("0" + getResources().getString(R.string.people_unit));
		numberOfFemaleTextView =
				(TextView) addToPlanDialogFragmentView.findViewById(R.id.textView_number_female_dialog);
		numberOfFemaleTextView.setText("0" + getResources().getString(R.string.people_unit));

		sexRatioDialogSeekBar = (SeekBar) addToPlanDialogFragmentView.findViewById(R.id.seekBar_ratio_sex_dialog);
		sexRatioDialogSeekBar.setMax(0);

		sexRatioDialogSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				sexRatioProgress = progress;
				// set number of male to the TextView
				numberOfMaleTextView.setText(progress + getResources().getString(R.string.people_unit));
				// set number of female to the TextView
				numberOfFemaleTextView.setText(seekBar.getMax() - progress + getResources().getString(R.string.people_unit));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		if(searchMode == CONDITION_SEARCH_MODE) {
			dateSelectDialogButton.setText(dateOfMT);

			regionSelectDialogButton.setSelection(regionOfMT);

			Log.d(TAG, numberOfPeople + "");
			numberOfPeopleSelectDialogEditText.setText(String.valueOf(numberOfPeople));
		} else {
			dateSelectDialogButton.setText(calendar.get(Calendar.YEAR) + "년 " + (calendar.get(Calendar.MONTH) + 1)
					+ "월 " + calendar.get(Calendar.DATE) + "일");
		}


		confirmButton = (Button) addToPlanDialogFragmentView.findViewById(R.id.btn_confirm);
		confirmButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*mOnAddToPlanDialogFragmentInteractionListener.
						onAddToPlanDialogFragmentViewDetached(dateOfMT,
								regionSelectDialogButton.getSelectedItemPosition(),
								sexRatioDialogSeekBar.getMax(), sexRatioProgress);*/
				dismiss();
			}
		});

		cancelButton = (Button) addToPlanDialogFragmentView.findViewById(R.id.btn_cancel);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog);
		return addToPlanDialogFragmentView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOnAddToPlanDialogFragmentInteractionListener = (OnAddToPlanDialogFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mOnAddToPlanDialogFragmentInteractionListener = null;
	}

	public void updateDate(String dateChanged) {
		dateSelectDialogButton.setText(dateChanged);
		dateOfMT = dateChanged;
	}

	public interface OnAddToPlanDialogFragmentInteractionListener {
		// TODO: Update argument type and name
//		public void onAddToPlanDialogFragmentViewDetached(String dateOfMT, int regionOfMT, int numberOfPeople, int sexRatioProgress);
	}

}
