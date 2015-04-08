package com.owo.mtplease.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.owo.mtplease.R;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

public class CalendarDialogFragment extends DialogFragment {

	private static final String TAG = "CalendarDialogFragment";
	private static final String DIALOG_CALLER_FLAG = "param1";

	private OnDateConfirmedListener _mOnDateConfirmedListener;

	// flag that defines which of two callers call this fragment
	// two callers are distinguished with two int type variable:
	// CALL_FROM_CONDITIONAL_QUERY = 1; CALL_FROM_PLAN = 2;
	private int _callerFlag;

	public static CalendarDialogFragment newInstance(int callerFlag) {
		CalendarDialogFragment fragment = new CalendarDialogFragment();
		Bundle args = new Bundle();
		args.putInt(DIALOG_CALLER_FLAG, callerFlag);
		fragment.setArguments(args);
		return fragment;
	}

	public CalendarDialogFragment() {
	}

	@Override
	public void onDetach() {
		super.onDetach();
		_mOnDateConfirmedListener = null;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			_mOnDateConfirmedListener = (OnDateConfirmedListener) activity;
		} catch(ClassCastException e) {
			throw new ClassCastException(activity.toString() + "must implement OnDateConfirmedListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null)
			_callerFlag = getArguments().getInt(DIALOG_CALLER_FLAG);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View calendarDialogView = inflater.inflate(R.layout.dialogfragment_calendar, container, false);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

		//TextView dialogTitle = (TextView) calendarDialogView.findViewById(R.id.textView_title_dialog_calendar);
		//dialogTitle.setTypeface(TypefaceLoader.getInstance(getActivity()).getTypeface());

		Calendar nextYear = Calendar.getInstance();
		nextYear.add(Calendar.YEAR, 1);

		final CalendarPickerView calendar = (CalendarPickerView) calendarDialogView.findViewById(R.id.calendarView);
		Date today = new Date();
		calendar.init(today, nextYear.getTime()).withSelectedDate(today);

		Calendar todayCalendar = Calendar.getInstance();
		todayCalendar.setTime(today);

		final TextView dayOfWeekTextView = (TextView) calendarDialogView.findViewById(R.id.textView_day_of_week);
		_setDayOfWeek(dayOfWeekTextView, todayCalendar.get(Calendar.DAY_OF_WEEK));

		final TextView yearTextView = (TextView) calendarDialogView.findViewById(R.id.textView_year);
		yearTextView.setText(todayCalendar.get(Calendar.YEAR) + "");

		final TextView monthTextView = (TextView) calendarDialogView.findViewById(R.id.textView_month);
		_setMonth(monthTextView ,todayCalendar.get(Calendar.MONTH ) + 1);

		final TextView dayTextView = (TextView) calendarDialogView.findViewById(R.id.textView_day);
		dayTextView.setText(todayCalendar.get(Calendar.DATE) + "");

		calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
			@Override
			public void onDateSelected(Date date) {
				Calendar selectedDateCalendar = Calendar.getInstance();
				selectedDateCalendar.setTime(calendar.getSelectedDate());
				_setDayOfWeek(dayOfWeekTextView, selectedDateCalendar.get(Calendar.DAY_OF_WEEK));
				yearTextView.setText(selectedDateCalendar.get(Calendar.YEAR) + "");
				_setMonth(monthTextView, selectedDateCalendar.get(Calendar.MONTH ) + 1);
				dayTextView.setText(selectedDateCalendar.get(Calendar.DATE) + "");
			}

			@Override
			public void onDateUnselected(Date date) {

			}
		});

		Button wheneverButton = (Button) calendarDialogView.findViewById(R.id.btn_whenever);
		wheneverButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_mOnDateConfirmedListener.onClickDateConfirmButton(getActivity().getResources().getString(R.string.whenever), _callerFlag);
				dismiss();
			}
		});

		Button confirmButton = (Button) calendarDialogView.findViewById(R.id.btn_confirm);
		confirmButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar selectedDateCalendar = Calendar.getInstance();
				selectedDateCalendar.setTime(calendar.getSelectedDate());
				String modifiedDate = selectedDateCalendar.get(Calendar.YEAR) + "년 "
						+ (selectedDateCalendar.get(Calendar.MONTH) + 1) + "월 "
						+ selectedDateCalendar.get(Calendar.DATE) + "일";
				Log.d(TAG, modifiedDate);
				_mOnDateConfirmedListener.onClickDateConfirmButton(modifiedDate, _callerFlag);
				dismiss();
			}

		});

		Button cancelButton = (Button) calendarDialogView.findViewById(R.id.btn_cancel);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		return calendarDialogView;
	}

	private void _setDayOfWeek(TextView dayOfWeekTextView, int dayOfWeek) {
		switch (dayOfWeek) {
			case 1:
				dayOfWeekTextView.setText("SUNDAY");
				break;
			case 2:
				dayOfWeekTextView.setText("MONDAY");
				break;
			case 3:
				dayOfWeekTextView.setText("TUESDAY");
				break;
			case 4:
				dayOfWeekTextView.setText("WEDNESDAY");
				break;
			case 5:
				dayOfWeekTextView.setText("THURSDAY");
				break;
			case 6:
				dayOfWeekTextView.setText("FRIDAY");
				break;
			case 7:
				dayOfWeekTextView.setText("SATURDAY");
				break;
		}
	}

	private void _setMonth(TextView monthTextView, int month) {
		switch (month) {
			case 1:
				monthTextView.setText("JAN");
				break;
			case 2:
				monthTextView.setText("FEB");
				break;
			case 3:
				monthTextView.setText("MAR");
				break;
			case 4:
				monthTextView.setText("APR");
				break;
			case 5:
				monthTextView.setText("MAY");
				break;
			case 6:
				monthTextView.setText("JUN");
				break;
			case 7:
				monthTextView.setText("JUL");
				break;
			case 8:
				monthTextView.setText("AUG");
				break;
			case 9:
				monthTextView.setText("SEP");
				break;
			case 10:
				monthTextView.setText("OCT");
				break;
			case 11:
				monthTextView.setText("NOV");
				break;
			case 12:
				monthTextView.setText("DEC");
				break;
		}
	}

	public interface OnDateConfirmedListener {
		public void onClickDateConfirmButton(String dateSelected, int _callerFlag);
	}
}
