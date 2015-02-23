package com.owo.mtplease.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kakao.AuthType;
import com.kakao.Session;
import com.kakao.SessionCallback;
import com.kakao.exception.KakaoException;
import com.kakao.widget.LoginButton;
import com.owo.mtplease.R;
import com.owo.mtplease.fragment.EmailLoginFragment;
import com.owo.mtplease.fragment.LookForPasswordFragment;


public class LoginActivity extends ActionBarActivity implements EmailLoginFragment.OnEmailLoginFragmentListener,
		LookForPasswordFragment.OnLookForPasswordFragmentListener {

	private static final String TAG = "LoginActivity";
	private static final String LOGIN_TYPE = "login_type";
	private static final int FACEBOOK_LOGIN = 1;
	private static final int KAKAOTALK_LOGIN = 2;
	private static final int EMAIL_LOGIN = 3;

	private FragmentManager mFragmentManager;
	private final SessionCallback mySessionCallback = new MySessionStatusCallback();
	private ActionBar actionBar;
	private RelativeLayout loginMenuRelativeLayout;
	private LoginButton kakaoTalkLoginButton;
	private FrameLayout goEmailLoginButton;
	private TextView signUpTextView;
	private FrameLayout loadingLayout;
	private Drawable loadingBackground;
	private ProgressBar loadingProgressBar;

	private int loginType;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		if (savedInstanceState == null) {

			// hide actionbar
			actionBar = getSupportActionBar();
			actionBar.hide();

			mFragmentManager = getSupportFragmentManager();
			int fragmentManagerBackStackSize = mFragmentManager.getBackStackEntryCount();
			for (int i = 0; i < fragmentManagerBackStackSize; i++) {
				mFragmentManager.popBackStack();
			}

			loginMenuRelativeLayout = (RelativeLayout) findViewById(R.id.layout_menu_login);
			loginMenuRelativeLayout.setVisibility(View.GONE);

			kakaoTalkLoginButton = (LoginButton) findViewById(R.id.com_kakao_login);

			goEmailLoginButton = (FrameLayout) findViewById(R.id.frameLayout_btn_login_email);
			goEmailLoginButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					EmailLoginFragment emailLoginFragment = EmailLoginFragment.newInstance();

					FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
					fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
							android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
					fragmentTransaction.add(R.id.body_background, emailLoginFragment);
					fragmentTransaction.addToBackStack(null);

					fragmentTransaction.commit();
				}
			});

			signUpTextView = (TextView) findViewById(R.id.textView_sign_up);
			signUpTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// go to SignUpActivity
					Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
//					signUpIntent.putExtra(LOGIN_TYPE, );
					startActivity(signUpIntent);
				}
			});
		}

		loadingLayout = (FrameLayout) findViewById(R.id.background_loading);
		loadingBackground = loadingLayout.getBackground();
		loadingBackground.setAlpha(0);

		loadingProgressBar = (ProgressBar) findViewById(R.id.progressBar_login);
		loadingProgressBar.setVisibility(View.GONE);

	}

	@Override
	protected void onResume() {
		super.onResume();
		// 세션을 초기화 한다. 카카오톡으로만 로그인을 유도하고 싶다면
		Session.initializeSession(this, mySessionCallback, AuthType.KAKAO_TALK);

		if (Session.initializeSession(this, mySessionCallback)) {
			// 1. 세션을 갱신 중이면, 프로그레스바를 보이거나 버튼을 숨기는 등의 액션을 취한다
			kakaoTalkLoginButton.setVisibility(View.GONE);
		} else if (Session.getCurrentSession().isOpened()) {
			// 2. 세션이 오픈된된 상태이면, 다음 activity로 이동한다.
			onSessionOpened();
		} else {
			// 3. else 로그인 창이 보인다.
			loginMenuRelativeLayout.setVisibility(View.VISIBLE);
		}
	}

	protected void onSessionOpened() {
		Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
		mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(mainIntent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(mFragmentManager.getBackStackEntryCount() == 0) {
				new AlertDialog.Builder(this)
						.setMessage(R.string.do_you_want_end_the_app)
						.setPositiveButton(R.string.yes,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
										finish();
									}
								})
						.setNegativeButton(getString(R.string.no),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								}).show();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onEmailLoginSuccess() {
		endLoadingProgress();

		final Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
		mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(mainIntent);
		finish();
	}

	@Override
	public void onClickLookForPasswordButton() {
		LookForPasswordFragment lookForPasswordFragment = LookForPasswordFragment.newInstance();

		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
				android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
		fragmentTransaction.replace(R.id.body_background, lookForPasswordFragment);
		fragmentTransaction.addToBackStack(null);

		fragmentTransaction.commit();
	}

	@Override
	public void onEndSendingTemporaryPassword() {

	}

	private class MySessionStatusCallback implements SessionCallback {
		@Override
		public void onSessionOpened() {
			// 프로그레스바를 보이고 있었다면 중지하고 세션 오픈후 보일 페이지로 이동
			LoginActivity.this.onSessionOpened();
		}

		@Override
		public void onSessionClosed(final KakaoException exception) {
			// 프로그레스바를 보이고 있었다면 중지하고 세션 오픈을 못했으니 다시 로그인 버튼 노출.
			kakaoTalkLoginButton.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onStartLogin() {
		startLoadingProgress();
	}

	@Override
	public void onEmailLoginFailed() {
		endLoadingProgress();
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
}
