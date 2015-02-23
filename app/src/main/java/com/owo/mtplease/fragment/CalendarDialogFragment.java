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
import android.widget.CalendarView;
import android.widget.TextView;

import com.owo.mtplease.R;
import com.owo.mtplease.view.TypefaceLoader;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

public class CalendarDialogFragment extends DialogFragment {

	private static final String TAG = "CalendarDialogFragment";
	private static final String DIALOG_CALLER_FLAG = "param1";

	private OnDateConfirmedListener mOnDateConfirmedListener;

	// flag that defines which of two callers call this fragment
	// two callers are distinguished with two int type variable:
	// CALL_FROM_CONDITIONAL_QUERY = 1; CALL_FROM_PLAN = 2;
	private int callerFlag;

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
		mOnDateConfirmedListener = null;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOnDateConfirmedListener = (OnDateConfirmedListener) activity;
		} catch(ClassCastException e) {
			throw new ClassCastException(activity.toString() + "must implement OnDateConfirmedListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null)
			callerFlag = getArguments().getInt(DIALOG_CALLER_FLAG);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View calendarDialogView = inflater.inflate(R.layout.dialogfragment_calendar, container, false);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		/*getDialog().setTitle(R.string.select_date);

		CalendarView cal = (CalendarView) calendarDialogView
				.findViewById(R.id.calendarView);
		cal.setShowWeekNumber(false);

		// 달력 날짜 출력 범위 설정
//		long mindate = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
		long maxdate = System.currentTimeMillis() + 31536000000L;
		cal.setMinDate(System.currentTimeMillis());
		cal.setMaxDate(maxdate);

		cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

			@Override
			public void onSelectedDayChange(CalendarView view, int year,
											int month, int dayOfMonth) {
				modifiedDate = year + "년 " + (month + 1) + "월 " + dayOfMonth
						+ "일";
			}
		});



		return calendarDialogView;*/

		TextView dialogTitle = (TextView) calendarDialogView.findViewById(R.id.textView_title_dialog_calendar);
		dialogTitle.setTypeface(TypefaceLoader.getInstance(getActivity()).getTypeface());

		Calendar nextYear = Calendar.getInstance();
		nextYear.add(Calendar.YEAR, 1);

		final CalendarPickerView calendar = (CalendarPickerView) calendarDialogView.findViewById(R.id.calendarView);
		Date today = new Date();
		calendar.init(today, nextYear.getTime()).withSelectedDate(today);

		Button dateConfirmButton = (Button) calendarDialogView
				.findViewById(R.id.button_date_confirm);
		dateConfirmButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar selectedDateCalendar = Calendar.getInstance();
				selectedDateCalendar.setTime(calendar.getSelectedDate());
				String modifiedDate = selectedDateCalendar.get(Calendar.YEAR) + "년 "
						+ (selectedDateCalendar.get(Calendar.MONTH) + 1) + "월 "
						+ selectedDateCalendar.get(Calendar.DATE) + "일";
				Log.d(TAG, modifiedDate);
				mOnDateConfirmedListener.onDateConfirmButtonClicked(modifiedDate, callerFlag);
				dismiss();
			}

		});

		return calendarDialogView;
	}

	public interface OnDateConfirmedListener {
		public void onDateConfirmButtonClicked(String dateSelected, int callerFlag);
	}
}
