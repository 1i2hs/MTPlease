package com.owo.mtplease.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.owo.mtplease.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnSettingsFragmentListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

	private static final String TAG = "SettingsFragment";

	private OnSettingsFragmentListener _onSettingsFragmentListener;

	public static SettingsFragment newInstance() {
		SettingsFragment fragment = new SettingsFragment();
		return fragment;
	}

	public SettingsFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View settingsFragmentView = inflater.inflate(R.layout.fragment_settings, container, false);

		_setAppVersionView(settingsFragmentView);

		if(_onSettingsFragmentListener != null)
			_onSettingsFragmentListener.onCreateSettingsFragmentView();

		settingsFragmentView.setClickable(true);

		return settingsFragmentView;
	}

	private void _setAppVersionView(View parentView) {
		RelativeLayout appVersionButton = (RelativeLayout) parentView.findViewById(R.id.layout_ver_app);
		appVersionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "App version view clicked");
				_onSettingsFragmentListener.onLoadVersionCheckFragmentView();
			}
		});
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			_onSettingsFragmentListener = (OnSettingsFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		_onSettingsFragmentListener = null;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		_onSettingsFragmentListener.onDestroySettingsFragmentView();
	}

	public interface OnSettingsFragmentListener {
		public void onCreateSettingsFragmentView();
		public void onLoadVersionCheckFragmentView();
		public void onDestroySettingsFragmentView();
	}

}
