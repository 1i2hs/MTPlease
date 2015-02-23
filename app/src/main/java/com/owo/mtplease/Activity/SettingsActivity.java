package com.owo.mtplease.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.kakao.APIErrorResult;
import com.kakao.UnlinkResponseCallback;
import com.kakao.UserManagement;
import com.owo.mtplease.R;


public class SettingsActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		if (savedInstanceState == null) {

			getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mtplease_actionbar_color)));

			getFragmentManager().beginTransaction()
					.replace(R.id.container, new SettingsFragment())
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

		private Preference idWithdrawalPreference;
		private CheckBoxPreference autoLoginPreference;

		public SettingsFragment() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			addPreferencesFromResource(R.xml.pref_general);


			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

			/*autoLoginPreference = (CheckBoxPreference) findPreference(getResources().getString(R.string.pref_auto_login_key));
			autoLoginPreference.setChecked(sharedPreferences.getBoolean(getResources().getString(R.string.pref_auto_login_key), false));
*/

			idWithdrawalPreference = (Preference) findPreference(getResources().getString(R.string.pref_withdrawal_id));
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
			});
		}
	}
}
