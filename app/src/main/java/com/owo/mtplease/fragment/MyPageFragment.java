package com.owo.mtplease.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.owo.mtplease.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyPageFragment.OnMyPageFragmentListener} interface
 * to handle interaction events.
 * Use the {@link MyPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPageFragment extends Fragment {

	private static final String TAG = "MyPageFragment";

	private ImageView _modifyUserInfoIcon;
	private ImageView _userProfileImageView;
	private TextView _userNickname;
	private TextView _userEmailAddress;

	private OnMyPageFragmentListener _mOnMyPageFragmentListener;

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment MyPageFragment.
	 */
	public static MyPageFragment newInstance(/*String param1, String param2*/) {
		MyPageFragment fragment = new MyPageFragment();
/*		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);*/
		return fragment;
	}

	public MyPageFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
/*		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}*/
		if(_mOnMyPageFragmentListener != null)
			_mOnMyPageFragmentListener.onCreateMyPageFragmentView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View myPageFragmentView = inflater.inflate(R.layout.fragment_my_page, container, false);

		_modifyUserInfoIcon = (ImageView) myPageFragmentView.findViewById(R.id.imageView_profile);
		_modifyUserInfoIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_mOnMyPageFragmentListener.onClickModifyUserInfoIcon();
			}
		});

		_userProfileImageView = (ImageView) myPageFragmentView.findViewById(R.id.imageView_modify_info_user);

		_userNickname = (TextView) myPageFragmentView.findViewById(R.id.textView_nickname);

		_userEmailAddress = (TextView) myPageFragmentView.findViewById(R.id.textView_email_address);

		return myPageFragmentView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			_mOnMyPageFragmentListener = (OnMyPageFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnMyPageFragmentListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		_mOnMyPageFragmentListener = null;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d(TAG, "MyPageFragmentView destroyed");
		_mOnMyPageFragmentListener.onDestroyMyPageFragmentView();
	}

	public interface OnMyPageFragmentListener {
		public void onClickModifyUserInfoIcon();
		public void onCreateMyPageFragmentView();
		public void onDestroyMyPageFragmentView();
	}

}
