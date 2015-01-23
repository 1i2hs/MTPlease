package com.owo.mtplease;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

public class CalendarDialogFragment extends DialogFragment {

    private OnDateConfirmedListener mOnDateConfirmedListener;

    // string for the selected date
    String modifiedDate = null;

	// flag that defines which of two callers call this fragment
	// two callers are distinguished with two int type variable:
	// CALL_FROM_CONDITIONAL_QUERY = 1; CALL_FROM_PLAN = 2;
	private int callerFlag;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View calendarDialogView = inflater.inflate(R.layout.dialogfragment_calendar, container, false);
        getDialog().setTitle(R.string.select_date);

        CalendarView cal = (CalendarView) calendarDialogView
                .findViewById(R.id.calendarView);
        cal.setShowWeekNumber(false);

        // 달력 날짜 출력 범위 설정
        long mindate = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
        long maxdate = System.currentTimeMillis() + 31536000000L;
        cal.setMinDate(mindate);
        cal.setMaxDate(maxdate);

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                // TODO Auto-generated method stub
                modifiedDate = year + "년 " + (month + 1) + "월 " + dayOfMonth
                        + "일";
            }
        });

        Button dateConfirmButton = (Button) calendarDialogView
                .findViewById(R.id.button_date_confirm);
        dateConfirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mOnDateConfirmedListener.onDateConfirmButtonClicked(modifiedDate, callerFlag);
                dismiss();
            }

        });

        return calendarDialogView;
    }

	public void setCaller(int callerFlag) {
		this.callerFlag = callerFlag;
	}

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDateConfirmedListener {
        // TODO: Update argument type and name
        public void onDateConfirmButtonClicked(String dateSelected, int callerFlag);
    }
}
