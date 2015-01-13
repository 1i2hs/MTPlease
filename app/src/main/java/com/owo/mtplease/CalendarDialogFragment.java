package com.owo.mtplease;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarDialogFragment.OnDateConfirmedListener} interface
 * to handle interaction events.
 * Use the {@link CalendarDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnDateConfirmedListener mOnDateConfirmedListener;

    // string for the selected date
    String modifiedDate = null;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarDialogFragment newInstance(String param1, String param2) {
        CalendarDialogFragment fragment = new CalendarDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CalendarDialogFragment() {
        // Required empty public constructor
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
                mOnDateConfirmedListener.onDateConfirmButtonClicked(modifiedDate);
                dismiss();
            }

        });

        return calendarDialogView;
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
        public void onDateConfirmButtonClicked(String dateSelected);
    }
}
