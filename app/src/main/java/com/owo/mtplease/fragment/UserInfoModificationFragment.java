package com.owo.mtplease.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.owo.mtplease.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserInfoModificationFragment.OnUserInfoModificationFragmentListener} interface
 * to handle interaction events.
 * Use the {@link UserInfoModificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserInfoModificationFragment extends Fragment {

	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	private String _mParam1;
	private String _mParam2;
	
	private TextView _userIdTextView;
	private EditText _currentPasswordEditText;
	private EditText _newPasswordEditText;
	private EditText _newPasswordConfirmEditText;
	private EditText _nicknameModificationEditText;
	private FrameLayout _modifyUserInfoButton;

	private OnUserInfoModificationFragmentListener _mOnUserInfoModificationFragmentListener;

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.

	 * @return A new instance of fragment UserInfoModificationFragment.
	 */
	public static UserInfoModificationFragment newInstance(/*String param1, String param2*/) {
		UserInfoModificationFragment fragment = new UserInfoModificationFragment();
		/*Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);*/
		return fragment;
	}

	public UserInfoModificationFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*if (getArguments() != null) {
			_mParam1 = getArguments().getString(ARG_PARAM1);
			_mParam2 = getArguments().getString(ARG_PARAM2);
		}*/
		if(_mOnUserInfoModificationFragmentListener != null)
			_mOnUserInfoModificationFragmentListener.onCreateUserInfoModificationFragmentView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View userInfoModificationFragmentView = inflater.inflate(
				R.layout.fragment_user_info_modification, container, false);
		
		_userIdTextView = (TextView) userInfoModificationFragmentView.findViewById(R.id.textView_id_user);
		
		_currentPasswordEditText = (EditText) userInfoModificationFragmentView.findViewById(R.id.editText_password_current);
		
		_newPasswordEditText = (EditText) userInfoModificationFragmentView.findViewById(R.id.editText_password_new);
		
		_newPasswordConfirmEditText = (EditText) userInfoModificationFragmentView.findViewById(R.id.editText_password_new_confirm);
		
		_nicknameModificationEditText = (EditText) userInfoModificationFragmentView.findViewById(R.id.editText_modification_nickname);

		_modifyUserInfoButton = (FrameLayout) userInfoModificationFragmentView.findViewById(R.id.frameLayout_modify_info_user);
		_modifyUserInfoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// send modified user info to the server
				//_mOnUserInfoModificationFragmentListener.onClickModifyUserInfoButton();
			}
		});
		
		return userInfoModificationFragmentView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			_mOnUserInfoModificationFragmentListener = (OnUserInfoModificationFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnUserInfoModificationFragmentListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		_mOnUserInfoModificationFragmentListener = null;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	public interface OnUserInfoModificationFragmentListener {
		public void onClickModifyUserInfoButton(String[] schoolNSociety, String password, String nickname);
		public void onCreateUserInfoModificationFragmentView();
		public void onDestroyUserInfoModificationFragmentView();
	}

}
