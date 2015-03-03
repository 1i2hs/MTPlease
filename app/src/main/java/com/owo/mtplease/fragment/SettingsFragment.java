package com.owo.mtplease.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
		View settingsFragementView = inflater.inflate(R.layout.fragment_settings, container, false);

		setAppVersionView(settingsFragementView);

		if(_onSettingsFragmentListener != null)
			_onSettingsFragmentListener.onCreateSettingsFragmentView();

		settingsFragementView.setClickable(true);

		return settingsFragementView;
	}

	private void setAppVersionView(View parentView) {
		RelativeLayout appVersionButton = (RelativeLayout) parentView.findViewById(R.id.layout_ver_app);
		appVersionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
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
