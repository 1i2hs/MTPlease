package com.owo.mtplease.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kakao.APIErrorResult;
import com.kakao.MeResponseCallback;
import com.kakao.UserManagement;
import com.kakao.UserProfile;
import com.owo.mtplease.R;
import com.owo.mtplease.SignUpModelController;
import com.owo.mtplease.fragment.SignUpFragment;
import com.owo.mtplease.view.SwippingDisabledViewPager;


public class SignUpActivity extends ActionBarActivity implements SignUpFragment.OnSignUpFragmentListener,
		SignUpModelController.SignUpModelControllerListener {

	private static final String TAG = "SignUpActivity";

	private static final int NUMBER_OF_SIGN_UP_PAGES = 3;

	private FragmentManager mFragmentManager;
	private SwippingDisabledViewPager mSwippingDisabledViewPager;
	private SignUpFragmentPagerAdapter mSignUpFragmentPagerAdapter;
	private ActionBar actionBar;
	private SignUpModelController mSignUpModelController;

	private FrameLayout loadingLayout;
	private Drawable loadingBackground;
	private ProgressBar loadingProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		if (savedInstanceState == null) {

			//requestMe();

			// hide actionbar

			actionBar = getSupportActionBar();
			actionBar.setBackgroundDrawable(new ColorDrawable(0xFF38E4FF));

			mFragmentManager = getSupportFragmentManager();
			int fragmentManagerBackStackSize = mFragmentManager.getBackStackEntryCount();
			for (int i = 0; i < fragmentManagerBackStackSize; i++) {
				mFragmentManager.popBackStack();
			}

			mSignUpFragmentPagerAdapter = new SignUpFragmentPagerAdapter(mFragmentManager);

			mSwippingDisabledViewPager = (SwippingDisabledViewPager) findViewById(R.id.swippingDisabledViewPager);
			mSwippingDisabledViewPager.setAdapter(mSignUpFragmentPagerAdapter);
			mSwippingDisabledViewPager.setOffscreenPageLimit(2);

			loadingLayout = (FrameLayout) findViewById(R.id.background_loading);
			loadingBackground = loadingLayout.getBackground();
			loadingBackground.setAlpha(0);

			loadingProgressBar = (ProgressBar) findViewById(R.id.progressBar_login);
			loadingProgressBar.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		mSignUpModelController = new SignUpModelController(this);
	}

	private void showSignUp() {

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	private void requestMe() {
		UserManagement.requestMe(new MeResponseCallback() {

			@Override
			protected void onSuccess(final UserProfile userProfile) {
				//Logger.getInstance().d("UserProfile : " + userProfile);
				userProfile.saveUserToCache();

				// start MainActivity
			}

			@Override
			protected void onNotSignedUp() {
				showSignUp();
			}

			@Override
			protected void onSessionClosedFailure(final APIErrorResult errorResult) {
				// failure due to the closure of the session
				// start LoginActivity

			}

			@Override
			protected void onFailure(final APIErrorResult errorResult) {
				// failure excluding the failure of the signing up or that of the closure of the session
				String message = "failed to get user info. msg=" + errorResult;
				//Logger.getInstance().d(message);

				// start LoginActivity

			}
		});
	}

	@Override
	public void onStartSendingAuthenticationCode(View view) {
		hideKeyboard(view);
		startLoadingProgress();
	}

	@Override
	public void onEndSendingAuthenticationCode() {
		endLoadingProgress();
	}

	@Override
	public void onFinishSignUp(int statusCode) {

		Toast toast = new Toast(this);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);

		if(statusCode == SignUpModelController.SIGN_UP_SUCCESS) {
			endLoadingProgress();

			toast.setText(R.string.sign_up_success);
			toast.show();

			// go to sign up completion page
			Intent mainIntent = new Intent(SignUpActivity.this, MainActivity.class);
			mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(mainIntent);
			finish();
		} else if(statusCode == SignUpModelController.SIGN_UP_FAILED) {
			toast.setText(R.string.sign_up_failed);
			toast.show();
		} else {
			toast.setText(R.string.server_error);
			toast.show();
		}
	}

	public static class SignUpFragmentPagerAdapter extends FragmentPagerAdapter {
		public SignUpFragmentPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return NUMBER_OF_SIGN_UP_PAGES;
		}

		@Override
		public Fragment getItem(int position) {
			Log.d(TAG, position + "");
			if(position == 0)
				return SignUpFragment.newInstance(SignUpFragment.FIRST_SIGN_UP_PAGE);
			else if(position == 1)
				return SignUpFragment.newInstance(SignUpFragment.SECOND_SIGN_UP_PAGE);
			else
				return SignUpFragment.newInstance(SignUpFragment.THIRD_SIGN_UP_PAGE);
		}
	}

	@Override
	public void onClickGoToNextPageButton() {
		mSwippingDisabledViewPager.setCurrentItem(SignUpFragment.SECOND_SIGN_UP_PAGE - 1);
	}

	@Override
	public void onClickGoToNextPageButton(String emailAddress, String password) {
		mSignUpModelController.setEmailAddress(emailAddress);
		mSignUpModelController.setPassword(password);

		mSwippingDisabledViewPager.setCurrentItem(SignUpFragment.THIRD_SIGN_UP_PAGE - 1);
	}

	@Override
	public void onClickSignUpButton(String userName, String schoolName, String nickname) {
		mSignUpModelController.setUserName(userName);
		mSignUpModelController.setSchoolName(schoolName);
		mSignUpModelController.setNickname(nickname);

		new AlertDialog.Builder(this)
				.setMessage(R.string.do_you_want_to_sign_up)
				.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								startLoadingProgress();
								mSignUpModelController.signUp();
								dialog.dismiss();
							}
						})
				.setNegativeButton(getString(R.string.no),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}).show();

	}

	@Override
	public void onClickGoToPreviousPageButton(int pageNumber) {
		switch(pageNumber) {
			case SignUpFragment.FIRST_SIGN_UP_PAGE:
				break;
			case SignUpFragment.SECOND_SIGN_UP_PAGE:
				mSwippingDisabledViewPager.setCurrentItem(SignUpFragment.FIRST_SIGN_UP_PAGE - 1);
				break;
			case SignUpFragment.THIRD_SIGN_UP_PAGE:
				mSwippingDisabledViewPager.setCurrentItem(SignUpFragment.SECOND_SIGN_UP_PAGE - 1);
				break;
		}
	}

	public void startLoadingProgress() {
		// configure progress bar
		loadingProgressBar.setVisibility(View.VISIBLE);
		loadingProgressBar.setProgress(0);
		loadingBackground.setAlpha(100);
		// end of the configuration of the progress bar
	}

	public void endLoadingProgress() {
		// configure progress bar
		loadingProgressBar.setVisibility(View.GONE);
		loadingProgressBar.setProgress(100);
		loadingBackground.setAlpha(0);
		// end of the configuration of the progress bar
	}

	public void hideKeyboard(View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
