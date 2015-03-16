package com.owo.mtplease.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.owo.mtplease.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LookForPasswordFragment.OnLookForPasswordFragmentListener} interface
 * to handle interaction events.
 * Use the {@link LookForPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LookForPasswordFragment extends Fragment {

	private EditText _emailAddressEditText;
	private FrameLayout _sendTempPasswordButton;

	private OnLookForPasswordFragmentListener _mOnLookForPasswordFragmentListener;

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment LookForPasswordFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static LookForPasswordFragment newInstance() {
		LookForPasswordFragment fragment = new LookForPasswordFragment();
		return fragment;
	}

	public LookForPasswordFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View lookForPasswordFragmentView = inflater.inflate(R.layout.fragment_look_for_password, container, false);

		_emailAddressEditText = (EditText) lookForPasswordFragmentView.findViewById(R.id.editText_input_email_for_looking_for_password);

		_sendTempPasswordButton = (FrameLayout) lookForPasswordFragmentView.findViewById(R.id.frameLayout_btn_send_password_temporary);
		_sendTempPasswordButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isValidEmail(_emailAddressEditText.getText().toString()))
					_mOnLookForPasswordFragmentListener.onEndSendingTemporaryPassword();
				else {
					Toast.makeText(getActivity(), R.string.temporary_password_sent, Toast.LENGTH_SHORT).show();
				}
			}
		});

		return lookForPasswordFragmentView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			_mOnLookForPasswordFragmentListener = (OnLookForPasswordFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnLookForPasswordFragmentListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		_mOnLookForPasswordFragmentListener = null;
	}

	public static boolean isValidEmail(String email) {
		boolean err = false;
		String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(email);
		if(m.matches()) {
			err = true;
		}
		return err;
	}

	public interface OnLookForPasswordFragmentListener {
		public void onEndSendingTemporaryPassword();
	}

}
