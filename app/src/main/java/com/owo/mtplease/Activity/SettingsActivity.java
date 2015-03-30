package com.owo.mtplease.Activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.owo.mtplease.R;
import com.owo.mtplease.fragment.VersionCheckFragment;


public class SettingsActivity extends ActionBarActivity implements VersionCheckFragment.OnVersionCheckFragmentListener {

	private static final String TAG = "SettingsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		if (savedInstanceState == null) {

			getSupportActionBar().setDisplayHomeAsUpEnabled(false);

			getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mtplease_color_primary)));

			getFragmentManager().beginTransaction()
					.replace(R.id.body_background, new SettingsFragment())
					.commit();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.menu_settings, menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public static class SettingsFragment extends PreferenceFragment {

		private Preference _versionCheckPreference;
		private Preference _idWithdrawalPreference;
		private CheckBoxPreference _autoLoginPreference;

		public SettingsFragment() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			addPreferencesFromResource(R.xml.pref_general);


//			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

			/*autoLoginPreference = (CheckBoxPreference) findPreference(getResources().getString(R.string.pref_auto_login_key));
			autoLoginPreference.setChecked(sharedPreferences.getBoolean(getResources().getString(R.string.pref_auto_login_key), false));
*/

			/*idWithdrawalPreference = (Preference) findPreference(getResources().getString(R.string.pref_withdrawal_id));
			idWithdrawalPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(final Preference preference) {
					new AlertDialog.Builder(preference.getContext())
							.setMessage(R.string.withdrawal_id_warning_message)
							.setPositiveButton(R.string.yes,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											UserManagement.requestUnlink(new UnlinkResponseCallback() {
												@Override
												protected void onSuccess(final long userId) {
													Intent loginIntent = new Intent(preference.getContext(), LoginActivity.class);
													loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
													startActivity(loginIntent);
													getActivity().finish();
												}

												@Override
												protected void onSessionClosedFailure(final APIErrorResult errorResult) {
												}

												@Override
												protected void onFailure(final APIErrorResult errorResult) {
												}
											});
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
					return true;
				}
			});*/
		}
	}

	@Override
	public void onCreateVersionCheckFragmentView() {
		getSupportActionBar().hide();
	}

	@Override
	public void onDestroyVersionCheckFragmentView() {
		getSupportActionBar().show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "onKeyDonwn");
		if(keyCode == KeyEvent.KEYCODE_BACK) {
				Log.d(TAG, "onBackKeyPressed");
				getSupportFragmentManager().popBackStackImmediate();
				return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
