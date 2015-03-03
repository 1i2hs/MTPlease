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
	private TextView appVersionTextView;
	private ImageButton sendOpinionImageButton;
	private ImageButton goFacebookImageButton;
	private OnVersionCheckFragmentListener mOnVersionCheckFragmentListener;

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

		appVersionTextView = (TextView) versionCheckFragemntView.findViewById(R.id.textView_version_application);
		appVersionTextView.setText(R.string.application_version);

		sendOpinionImageButton = (ImageButton) versionCheckFragemntView.findViewById(R.id.imageButton_send_decision);
		sendOpinionImageButton.setOnClickListener(new View.OnClickListener() {
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

		goFacebookImageButton = (ImageButton) versionCheckFragemntView.findViewById(R.id.imageButton_go_facebook);
		goFacebookImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri webLink = Uri.parse(MTPLEASE_FACEBOOK_URL);
				Intent webBrowseIntent = new Intent(Intent.ACTION_VIEW, webLink);
				v.getContext().startActivity(webBrowseIntent);
			}
		});

		if(mOnVersionCheckFragmentListener != null)
			mOnVersionCheckFragmentListener.onCreateVersionCheckFragmentView();

		versionCheckFragemntView.setClickable(true);

		return versionCheckFragemntView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOnVersionCheckFragmentListener = (OnVersionCheckFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnVersionCheckFragmentListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mOnVersionCheckFragmentListener = null;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mOnVersionCheckFragmentListener.onDestroyVersionCheckFragmentView();
	}

	public interface OnVersionCheckFragmentListener {
		public void onCreateVersionCheckFragmentView();
		public void onDestroyVersionCheckFragmentView();
	}
}
