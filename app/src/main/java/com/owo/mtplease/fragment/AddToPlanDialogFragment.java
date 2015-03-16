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

	private String _dateOfMT;
	private int _regionOfMT;
	private int _numberOfPeople;
	private int _searchMode;

	// User Interface Views
	private Button _dateSelectDialogButton;
	private Spinner _regionSelectDialogButton;
	private EditText _numberOfPeopleSelectDialogEditText;
	private TextView _numberOfMaleTextView;
	private TextView _numberOfFemaleTextView;
	private SeekBar _sexRatioDialogSeekBar;
	private Button _confirmButton;
	private Button _cancelButton;
	// End of the UI views

	private OnAddToPlanDialogFragmentInteractionListener _mOnAddToPlanDialogFragmentInteractionListener;

	private Calendar _calendar = Calendar.getInstance();

	private int _sexRatioProgress;

	private TextWatcher _editTextWatcherForNumberOfPeopleSelectDialog = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) throws NumberFormatException {
			try {
				_sexRatioDialogSeekBar.setMax(Integer.parseInt(s.toString()));
			} catch (NumberFormatException e) {
				_sexRatioDialogSeekBar.setMax(0);
				e.printStackTrace();
			}
		}
	};

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param _dateOfMT       Parameter 1.
	 * @param _regionOfMT     Parameter 2.
	 * @param _numberOfPeople Parameter 3.
	 * @param _searchMode     Parameter 4.
	 * @return A new instance of fragment AddToPlanDialogFragment.
	 */
	public static AddToPlanDialogFragment newInstance(String _dateOfMT, int _regionOfMT, int _numberOfPeople, int _searchMode) {
		AddToPlanDialogFragment fragment = new AddToPlanDialogFragment();
		Bundle args = new Bundle();
		args.putString(DATE_OF_MT, _dateOfMT);
		args.putInt(REGION_OF_MT, _regionOfMT);
		args.putInt(NUMBER_OF_PEOPLE, _numberOfPeople);
		args.putInt(SEARCH_MODE, _searchMode);
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
			_dateOfMT = getArguments().getString(DATE_OF_MT);
			_regionOfMT = getArguments().getInt(REGION_OF_MT);
			_numberOfPeople = getArguments().getInt(NUMBER_OF_PEOPLE);
			_searchMode = getArguments().getInt(SEARCH_MODE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View addToPlanDialogFragmentView = inflater.inflate(R.layout.fragment_add_to_plan_dialog, container, false);

		getDialog().setTitle(R.string.input_basic_data);

		_dateSelectDialogButton = (Button) addToPlanDialogFragmentView.findViewById(R.id.btn_select_date_dialog);
		_dateSelectDialogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CalendarDialogFragment _calendarDialogFragment =
						CalendarDialogFragment.newInstance(CALL_FROM_ADDTOPLANDIALOG);
				_calendarDialogFragment
						.show(getActivity().getSupportFragmentManager(), "_calendar_dialog_popped");
			}
		});

		_regionSelectDialogButton =
				(Spinner) addToPlanDialogFragmentView.findViewById(R.id.spinner_select_region_dialog);
		ArrayAdapter<CharSequence> regionSpinnerDialogAdapter =
				ArrayAdapter.createFromResource(getActivity(), R.array.array_region, R.layout.spinner_region);
		regionSpinnerDialogAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_regionSelectDialogButton.setAdapter(regionSpinnerDialogAdapter);

		_numberOfPeopleSelectDialogEditText =
				(EditText) addToPlanDialogFragmentView.findViewById(R.id.editText_number_people_dialog);
		_numberOfPeopleSelectDialogEditText.addTextChangedListener(_editTextWatcherForNumberOfPeopleSelectDialog);

		_numberOfMaleTextView =
				(TextView) addToPlanDialogFragmentView.findViewById(R.id.textView_number_male_dialog);
		_numberOfMaleTextView.setText("0" + getResources().getString(R.string.people_unit));
		_numberOfFemaleTextView =
				(TextView) addToPlanDialogFragmentView.findViewById(R.id.textView_number_female_dialog);
		_numberOfFemaleTextView.setText("0" + getResources().getString(R.string.people_unit));

		_sexRatioDialogSeekBar = (SeekBar) addToPlanDialogFragmentView.findViewById(R.id.seekBar_ratio_sex_dialog);
		_sexRatioDialogSeekBar.setMax(0);

		_sexRatioDialogSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				_sexRatioProgress = progress;
				// set number of male to the TextView
				_numberOfMaleTextView.setText(progress + getResources().getString(R.string.people_unit));
				// set number of female to the TextView
				_numberOfFemaleTextView.setText(seekBar.getMax() - progress + getResources().getString(R.string.people_unit));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		if(_searchMode == CONDITION_SEARCH_MODE) {
			_dateSelectDialogButton.setText(_dateOfMT);

			_regionSelectDialogButton.setSelection(_regionOfMT);

			Log.d(TAG, _numberOfPeople + "");
			_numberOfPeopleSelectDialogEditText.setText(String.valueOf(_numberOfPeople));
		} else {
			_dateSelectDialogButton.setText(_calendar.get(Calendar.YEAR) + "년 " + (_calendar.get(Calendar.MONTH) + 1)
					+ "월 " + _calendar.get(Calendar.DATE) + "일");
		}


		_confirmButton = (Button) addToPlanDialogFragmentView.findViewById(R.id.btn_confirm);
		_confirmButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*_mOnAddToPlanDialogFragmentInteractionListener.
						onAddToPlanDialogFragmentViewDetached(_dateOfMT,
								_regionSelectDialogButton.getSelectedItemPosition(),
								_sexRatioDialogSeekBar.getMax(), _sexRatioProgress);*/
				dismiss();
			}
		});

		_cancelButton = (Button) addToPlanDialogFragmentView.findViewById(R.id.btn_cancel);
		_cancelButton.setOnClickListener(new View.OnClickListener() {
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
			_mOnAddToPlanDialogFragmentInteractionListener = (OnAddToPlanDialogFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		_mOnAddToPlanDialogFragmentInteractionListener = null;
	}

	public void updateDate(String dateChanged) {
		_dateSelectDialogButton.setText(dateChanged);
		_dateOfMT = dateChanged;
	}

	public interface OnAddToPlanDialogFragmentInteractionListener {
		// TODO: Update argument type and name
//		public void onAddToPlanDialogFragmentViewDetached(String _dateOfMT, int _regionOfMT, int _numberOfPeople, int _sexRatioProgress);
	}

}
