package com.owo.mtplease.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.owo.mtplease.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VersionCheckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VersionCheckFragment extends Fragment {

	private static final String MTPLEASE_FACEBOOK_URL = "http://facebook.com/mtowo";
	private TextView _appVersionTextView;
	private ImageButton _sendOpinionImageButton;
	private ImageButton _goFacebookImageButton;
	private ImageButton _goKakaoYellowIdButton;
	private OnVersionCheckFragmentListener _mOnVersionCheckFragmentListener;

	public static VersionCheckFragment newInstance() {
		VersionCheckFragment fragment = new VersionCheckFragment();
		return fragment;
	}

	public VersionCheckFragment() {
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
		View versionCheckFragemntView = inflater.inflate(R.layout.fragment_version_check, container, false);

		_appVersionTextView = (TextView) versionCheckFragemntView.findViewById(R.id.textView_version_application);
		_appVersionTextView.setText(R.string.application_version);

		_sendOpinionImageButton = (ImageButton) versionCheckFragemntView.findViewById(R.id.imageButton_send_decision);
		_sendOpinionImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sendEmailIntent = new Intent(Intent.ACTION_SEND);
				sendEmailIntent.setType("message/rfc822");
				sendEmailIntent.putExtra(Intent.EXTRA_EMAIL,
						new String[]{v.getContext().getResources().getString(R.string.mtplease_email_address)});
				sendEmailIntent.putExtra(Intent.EXTRA_SUBJECT, v.getContext().getResources().getString(R.string.subject_mail));
				v.getContext().startActivity(sendEmailIntent);
			}
		});

		_goFacebookImageButton = (ImageButton) versionCheckFragemntView.findViewById(R.id.imageButton_go_facebook);
		_goFacebookImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri webLink = Uri.parse(MTPLEASE_FACEBOOK_URL);
				Intent webBrowseIntent = new Intent(Intent.ACTION_VIEW, webLink);
				v.getContext().startActivity(webBrowseIntent);
			}
		});

		_goKakaoYellowIdButton = (ImageButton) versionCheckFragemntView.findViewById(R.id.imageButton_kakaotalk_yellow_id);
		_goKakaoYellowIdButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri webLink = Uri.parse("http://goto.kakao.com/@%EC%97%A0%ED%8B%B0%EB%A5%BC%EB%B6%80%ED%83%81%ED%95%B4");
				Intent webBrowseIntent = new Intent(Intent.ACTION_VIEW, webLink);
				startActivity(webBrowseIntent);
			}
		});

		if(_mOnVersionCheckFragmentListener != null)
			_mOnVersionCheckFragmentListener.onCreateVersionCheckFragmentView();

		versionCheckFragemntView.setClickable(true);

		return versionCheckFragemntView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			_mOnVersionCheckFragmentListener = (OnVersionCheckFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnVersionCheckFragmentListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		_mOnVersionCheckFragmentListener = null;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		_mOnVersionCheckFragmentListener.onDestroyVersionCheckFragmentView();
	}

	public interface OnVersionCheckFragmentListener {
		public void onCreateVersionCheckFragmentView();
		public void onDestroyVersionCheckFragmentView();
	}
}
