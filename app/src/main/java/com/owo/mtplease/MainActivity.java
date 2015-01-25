package com.owo.mtplease;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import notboringactionbar.AlphaForegroundColorSpan;


public class MainActivity extends ActionBarActivity implements ScrollTabHolder,
		TimelineFragment.OnTimelineFragmentInteractionListener, ResultFragment.OnResultFragmentInteractionListener,
		CalendarDialogFragment.OnDateConfirmedListener, SpecificInfoFragment.OnSpecificInfoFragmentInteractionListener,
		AddToPlanDialogFragment.OnAddToPlanDialogFragmentInteractionListener {

	// Fragments
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	private TimelineFragment mTimelineFragment;
	private ResultFragment mResultFragment;
	// End of Fragments
	// Strings
	private static final String TAG = "MainActivity";
	private static final int CONDITION_SEARCH_MODE = 1;
	private static final int KEYWORD_SEARCH_MODE = 2;
	private static final int NETWORK_CONNECTION_FAILED = -1;
	private static final int TIMELINE = 1;
	private static final int RESULT = 2;
	private static final String MTPLEASE_URL = "http://mtplease.herokuapp.com/";
	public static final int TIMELINE_FRAGMENT_VISIBLE = 1;
	public static final int RESULT_FRAGMENT_VISIBLE = 2;
	public static final int SPECIFIC_INFO_FRAGMENT_VISIBLE = 3;
	private static final int CALL_FROM_CONDITIONAL_QUERY = 1;
	private static final int CALL_FROM_PLAN = 2;
	private static final int CALL_FROM_ADDTOPLANDIALOG = 3;
	private static final int INDEX_OF_DAESUNGRI = 0;
	private static final int INDEX_OF_CHEONGPYUND = 1;
	private static final int INDEX_OF_GAPYUNG = 2;
	// End of Strings

	// Graphical transitions
	private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
	private SpannableString mSpannableStringAppTitle;
	private SpannableString mSpannableStringVariable;
	private ColorDrawable actionBarBackgroundColor;
	private int mMinHeaderHeightForTimelineFragment;
	private int mMinHeaderHeightForResultFragment;
	private int mHeaderHeight;
	private int mMinHeaderTranslation;
	// End of the Graphical transitions

	// User Interface Views
	private ActionBar mActionBar;
	private SlidingMenu doubleSideSlidingMenu;
	private Button dateSelectButton;
	private Spinner regionSelectSpinner;
	private EditText numberOfPeopleSelectEditText;
	private ImageButton roomSearchButton;
	private TextView roomCountText;
	private TextView dateQuerySubTabText;
	private TextView regionQuerySubTabText;
	private TextView numberOfPeopleQuerySubTabText;
	private FrameLayout loadingLayout;
	private Drawable loadingBackground;
	private ProgressBar loadingProgressBar;
	private View mHeader;
	private TextView homeButton;
	private TextView compareButton;
	private TextView mypageButton;
	private TextView settingButton;
	private String modifiedDate;
	private Button dateSelectPlanButton;
	private Spinner regionSelectPlanButton;
	private EditText numberOfPeopleSelectPlanEditText;
	private TextView numberOfMaleTextView;
	private TextView numberOfFemaleTextView;
	private SeekBar sexRatioPlanSeekBar;
	private LinearLayout roomPlanLinearLayout;
	private RelativeLayout notSelectedRoomPlanRelativeLayout;
	private LinearLayout meatPlanLinearLayout;
	private LinearLayout alcoholPlanLinearLayout;
	private LinearLayout othersPlanLinearLayout;
	private TextView roomNamePlanTextView;
	private TextView pensionNamePlanTextView;
	private TextView numberPeopleStdMaxPlanTextView;
	private TextView roomPricePlanTextView;
	private TextView clearRoomPlanButton;
	// End of User Interface Views

	// Controller
	private ScrollTabHolder mScrollTabHolder = this;
	private TextWatcher editTextWatcherForNumberOfPeopleSelectPlan = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) throws NumberFormatException {
			try {
				sexRatioPlanSeekBar.setMax(Integer.parseInt(s.toString()));
			} catch(NumberFormatException e) {
				sexRatioPlanSeekBar.setMax(0);
				e.printStackTrace();
			}
		}
	};
	// End of Controllers

	// Others
	private Calendar calendar = Calendar.getInstance();
	private ConditionDataForRequest mConditionDataForRequest;
	private float clampValue;
	private int searchMode;

	public static float clamp(float value, float max, float min) {
		return Math.max(Math.min(value, min), max);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// configure the Actionbar
		mActionBar = getSupportActionBar();

		actionBarBackgroundColor = new ColorDrawable(Color.BLACK);
		// end of the configuration

		mFragmentManager = getSupportFragmentManager();

		// configure the SlidingMenu
		setSlidingMenu();
		// end of the configuration of the SlidingMenu

		// configure the QueryHeader
		setQueryHeader();
		// end of the configuration of QueryHeader

		// configure the side menu buttons
		setSideMenuButtons();
		// end of the configuration of the side menu buttons

		// configure the conditional query UIs
		setConditionalQueryInput();
		// end of the configuration of the conditional query UIs

		// configure the number of the room that our service have inside our database
		roomCountText = (TextView) findViewById(R.id.text_room_count);
		// end of the configuration

		// configure the subtab(tab under the query header that displays conditional query which users input)
		setSubTab();
		// end of the configuration of the subtab

		// configure the Plan UIs
		setPlan();
		// end of the configuration of the Plan UIs

		// configure the loading animation
		setLoadingAnimation();
		// end of the configuration of the loading animation

		// configure the mode of the search
		searchMode = CONDITION_SEARCH_MODE;
		// end of the configuration of the mode of the search

		// get timeline data from the server and create the mTimelineFragment
		new ServerConnectionTask(mScrollTabHolder, TIMELINE).execute(MTPLEASE_URL);
		// end of getting the data from the server and the creation of the fragment
	}

	private void setSlidingMenu() {
		doubleSideSlidingMenu = new SlidingMenu(this);
		doubleSideSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		doubleSideSlidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		doubleSideSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		doubleSideSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		doubleSideSlidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
		doubleSideSlidingMenu.setShadowDrawable(R.drawable.shadow);
		doubleSideSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		doubleSideSlidingMenu.setFadeDegree(0.70f);
		doubleSideSlidingMenu.setMenu(R.layout.menu_side);
		doubleSideSlidingMenu.setSecondaryMenu(R.layout.plan_side);
	}

	private void setPlan() {
		dateSelectPlanButton = (Button) findViewById(R.id.btn_select_date_plan);
		dateSelectPlanButton.setText(modifiedDate);
		dateSelectPlanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callCalendarDialogFragment(CALL_FROM_PLAN);
			}
		});

		regionSelectPlanButton = (Spinner) findViewById(R.id.spinner_select_region_plan);
		ArrayAdapter<CharSequence> regionSpinnerPlanAdapter =
				ArrayAdapter.createFromResource(this, R.array.array_region, R.layout.spinner_region);
		regionSpinnerPlanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		regionSelectPlanButton.setAdapter(regionSpinnerPlanAdapter);

		numberOfPeopleSelectPlanEditText = (EditText) findViewById(R.id.editText_number_people_plan);
		numberOfPeopleSelectPlanEditText.addTextChangedListener(editTextWatcherForNumberOfPeopleSelectPlan);

		numberOfMaleTextView = (TextView) findViewById(R.id.textView_number_male_plan);
		numberOfMaleTextView.setText("0" + getResources().getString(R.string.people_unit));
		numberOfFemaleTextView = (TextView) findViewById(R.id.textView_number_female_plan);
		numberOfFemaleTextView.setText("0" + getResources().getString(R.string.people_unit));

		sexRatioPlanSeekBar = (SeekBar) findViewById(R.id.seekBar_ratio_sex_plan);
		sexRatioPlanSeekBar.setMax(0);

		sexRatioPlanSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// set number of male to the TextView
				numberOfMaleTextView.setText(progress + getResources().getString(R.string.people_unit));
				// set number of female to the TextView
				numberOfFemaleTextView.setText(seekBar.getMax() - progress + getResources().getString(R.string.people_unit));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		// dynamically adding view "frame_room_not_selected_plan.xml"(meaning room is not selected in the plan)
		// to the plan
		roomPlanLinearLayout = (LinearLayout) findViewById(R.id.layout_room);
		roomPlanLinearLayout.addView(getLayoutInflater().inflate(R.layout.frame_room_not_selected_plan,
				(LinearLayout) findViewById(R.id.layout_room), false), 2);

		notSelectedRoomPlanRelativeLayout = (RelativeLayout) findViewById(R.id.layout_room_not_selected);
		notSelectedRoomPlanRelativeLayout.setOnClickListener(new View.OnClickListener() {
			// evokes when black block(the view "frame_room_not_selected_plan") is clicked
			@Override
			public void onClick(View v) {
				Toast.makeText(v.getContext(), R.string.please_search_for_room, Toast.LENGTH_SHORT).show();
				doubleSideSlidingMenu.toggle();
				/*roomPlanLinearLayout.removeView(notSelectedRoomPlanRelativeLayout);
				roomPlanLinearLayout.addView(getLayoutInflater().inflate(R.layout.frame_room_plan,
						(LinearLayout) findViewById(R.id.layout_room), false), 2);*/
			}
		});

		meatPlanLinearLayout = (LinearLayout) findViewById(R.id.layout_meat);
		meatPlanLinearLayout.addView(getLayoutInflater().
				inflate(R.layout.space_item_not_selected_plan,
						(LinearLayout) findViewById(R.id.layout_items), false), 2);

		alcoholPlanLinearLayout = (LinearLayout) findViewById(R.id.layout_alcohol);
		alcoholPlanLinearLayout.addView(getLayoutInflater().
				inflate(R.layout.space_item_not_selected_plan,
						(LinearLayout) findViewById(R.id.layout_items), false), 2);

		othersPlanLinearLayout = (LinearLayout) findViewById(R.id.layout_others);
		othersPlanLinearLayout.addView(getLayoutInflater().
				inflate(R.layout.space_item_not_selected_plan,
						(LinearLayout) findViewById(R.id.layout_items), false), 2);
	}

	private void setAutoCompleteBasicInfoPlan() {
		mConditionDataForRequest = getUserInputData();
		dateSelectPlanButton.setText(modifiedDate);
		regionSelectPlanButton.setSelection(mConditionDataForRequest.getRegion() - 1);
		numberOfPeopleSelectPlanEditText.setText(String.valueOf(mConditionDataForRequest.getPeople()));
	}

	private void setAutoCompleteBasicInfoPlan(String dateOfMT, int regionOfMT, int numberOfPeople, int sexRatioProgress) {
		dateSelectPlanButton.setText(dateOfMT);
		regionSelectPlanButton.setSelection(regionOfMT);
		numberOfPeopleSelectPlanEditText.setText(String.valueOf(numberOfPeople));
		sexRatioPlanSeekBar.setProgress(sexRatioProgress);
	}

	private void setQueryHeader() {
		mMinHeaderHeightForTimelineFragment = getResources().getDimensionPixelSize(R.dimen.min_header_height);
		mMinHeaderHeightForResultFragment = getResources().getDimensionPixelOffset(R.dimen.header_height);
		mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
		mHeader = findViewById(R.id.header);
		mSpannableStringAppTitle = new SpannableString(getString(R.string.actionbar_title));
		mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(0xffffffff);
	}

	private void setSideMenuButtons() {
		homeButton = (TextView) findViewById(R.id.btn_menu_home);
		homeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		//compareButton = (TextView) findViewById(R.id.btn_menu_compare);

		mypageButton = (TextView) findViewById(R.id.btn_menu_mypage);
		mypageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		settingButton = (TextView) findViewById(R.id.btn_menu_setting);
		settingButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent settingsIntent = new Intent(v.getContext(), SettingsActivity.class);
				startActivity(settingsIntent);
			}
		});
	}

	private void setConditionalQueryInput() {
		dateSelectButton = (Button) findViewById(R.id.btn_select_date);
		modifiedDate = calendar.get(Calendar.YEAR) + "년 " + (calendar.get(Calendar.MONTH) + 1)
				+ "월 " + calendar.get(Calendar.DATE) + "일";
		dateSelectButton.setText(modifiedDate);
		dateSelectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callCalendarDialogFragment(CALL_FROM_CONDITIONAL_QUERY);
			}
		});

		regionSelectSpinner = (Spinner) findViewById(R.id.spinner_select_region);
		ArrayAdapter<CharSequence> regionSpinnerAdapter =
				ArrayAdapter.createFromResource(this, R.array.array_region, R.layout.spinner_region);
		regionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		regionSelectSpinner.setAdapter(regionSpinnerAdapter);

		numberOfPeopleSelectEditText = (EditText) findViewById(R.id.editText_input_number_people);

		roomSearchButton = (ImageButton) findViewById(R.id.btn_search_room);
		roomSearchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if(searchMode == CONDITION_SEARCH_MODE)
						setAutoCompleteBasicInfoPlan();

					new ServerConnectionTask(mScrollTabHolder,
							RESULT).execute(getUserInputData().makeHttpGetURL());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void setSubTab() {
		dateQuerySubTabText = (TextView) findViewById(R.id.text_query_date);
		regionQuerySubTabText = (TextView) findViewById(R.id.text_query_region);
		numberOfPeopleQuerySubTabText = (TextView) findViewById(R.id.text_query_number_people);
	}

	private void setLoadingAnimation() {
		loadingLayout = (FrameLayout) findViewById(R.id.background_loading);
		loadingBackground = loadingLayout.getBackground();
		loadingBackground.setAlpha(0);

		loadingProgressBar = (ProgressBar) findViewById(R.id.progressBar_condition_search);
		loadingProgressBar.setVisibility(View.GONE);
	}

	private void callCalendarDialogFragment(int callerFlag) {
		CalendarDialogFragment calendarDialogFragment = CalendarDialogFragment.newInstance(callerFlag);
		calendarDialogFragment.show(mFragmentManager, "calendar_dialog_popped");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			Log.i("MainActivity", "action_settings");
			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingsIntent);
			return false;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResultFragmentViewResumed(boolean noResults, int numRoom) {
		Log.d(TAG, "onResultFragmentViewResumed");
		dateQuerySubTabText.setVisibility(View.VISIBLE);
		regionQuerySubTabText.setVisibility(View.VISIBLE);
		numberOfPeopleQuerySubTabText.setVisibility(View.VISIBLE);

		// configure actionbar for result page
		if (noResults) {
			changeActionBarStyle(Color.BLACK, getResources().getString(R.string.results), getResources().getString(R.string.no_results));
		} else {
			changeActionBarStyle(Color.BLACK, getResources().getString(R.string.results), numRoom + "개의 방");
		}
		// end of the configuration

		// configure subtab for result page
		mConditionDataForRequest = getUserInputData();
		dateQuerySubTabText.setText(mConditionDataForRequest.getDate());
		int regionCode = mConditionDataForRequest.getRegion();
		switch (regionCode) {
			case 1:
				regionQuerySubTabText.setText(R.string.daesungri);
				break;
			case 2:
				regionQuerySubTabText.setText(R.string.cheongpyung);
				break;
			case 3:
				regionQuerySubTabText.setText(R.string.gapyung);
				break;
		}
		numberOfPeopleQuerySubTabText.setText(mConditionDataForRequest.getPeople() + "명");
		// end of the configuration

		mHeader.setTranslationY(-mMinHeaderHeightForResultFragment + getSupportActionBar().getHeight());
	}

	@Override
	public void onSpecificInfoFragmentLoad() {
		// configure progress bar
		loadingProgressBar.setVisibility(View.VISIBLE);
		loadingProgressBar.setProgress(0);
		loadingBackground.setAlpha(100);
		// end of the configuration of the progress bar
	}

	@Override
	public void onSpecificInfoFragmentLoadDone() {
		// configure progress bar
		loadingProgressBar.setVisibility(View.GONE);
		loadingProgressBar.setProgress(100);
		loadingBackground.setAlpha(0);
		// end of the configuration of the progress bar
	}

	@Override
	public void onTimelineFragmentViewResumed() {
		dateQuerySubTabText.setVisibility(View.INVISIBLE);
		regionQuerySubTabText.setVisibility(View.INVISIBLE);
		numberOfPeopleQuerySubTabText.setVisibility(View.INVISIBLE);

		changeActionBarStyle(Color.TRANSPARENT, getResources().getString(R.string.app_name), null);
		mHeader.setTranslationY(0);
	}

	@Override
	public void adjustScroll(int scrollHeight) {

	}

	@Override
	public void onScroll(RecyclerView recyclerView, int firstVisibleItem, int pagePosition, int visibleFragment) {
		int scrollY = getScrollY(recyclerView, firstVisibleItem);
		float ratio;
		switch (visibleFragment) {
			case TIMELINE_FRAGMENT_VISIBLE:
				clampValue = changeHeaderTranslation(scrollY, mMinHeaderHeightForTimelineFragment);
				break;
			case RESULT_FRAGMENT_VISIBLE:
				changeHeaderTranslation(scrollY, mMinHeaderHeightForResultFragment);
				return;
			case SPECIFIC_INFO_FRAGMENT_VISIBLE:
				ratio = (float) getSupportActionBar().getHeight() / (float) (scrollY + 1);
				clampValue = clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F);
				break;
			default:
				return;
		}

		setTitleAlpha(clampValue, visibleFragment);

		actionBarBackgroundColor.setAlpha((int) (clampValue * 255));
		mActionBar.setBackgroundDrawable(actionBarBackgroundColor);
	}

	private float changeHeaderTranslation(int scrollY, int minHeaderHeight) {
		mMinHeaderTranslation = -minHeaderHeight + getSupportActionBar().getHeight();

		//Log.i(TAG, -scrollY + "dp / " + mMinHeaderTranslation + "dp");

		mHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));

		float ratio = clamp(mHeader.getTranslationY() / mMinHeaderTranslation, 0.0F, 1.0F);

		return clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F);
	}

	public int getScrollY(RecyclerView view, int firstVisibleItem) {
		View c = view.getChildAt(0);
		if (c == null) {
			return 0;
		}

		int firstVisiblePosition = firstVisibleItem;
		int top = c.getTop();

		int headerHeight = 0;
		if (firstVisiblePosition >= 1) {
			headerHeight = mHeaderHeight;
		}

		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}

	private void setTitleAlpha(float alpha, int visibleFragment) {
		mAlphaForegroundColorSpan.setAlpha(alpha);

		switch (visibleFragment) {
			case TIMELINE_FRAGMENT_VISIBLE:
				mSpannableStringAppTitle.setSpan(mAlphaForegroundColorSpan, 0, mSpannableStringAppTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				mActionBar.setTitle(mSpannableStringAppTitle);
				break;
			case RESULT_FRAGMENT_VISIBLE:
				break;
			case SPECIFIC_INFO_FRAGMENT_VISIBLE:
				mSpannableStringVariable = new SpannableString(getSupportActionBar().getTitle());
				mSpannableStringVariable.setSpan(mAlphaForegroundColorSpan, 0, mSpannableStringVariable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				mActionBar.setTitle(mSpannableStringVariable);
				mSpannableStringVariable = null;
				mSpannableStringVariable = new SpannableString(getSupportActionBar().getSubtitle());
				mSpannableStringVariable.setSpan(mAlphaForegroundColorSpan, 0, mSpannableStringVariable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				mActionBar.setSubtitle(mSpannableStringVariable);
				break;
			default:
				return;
		}
	}

	@Override
	public void onDateConfirmButtonClicked(String dateSelected, int callerFlag) {
		if (dateSelected != null) {
			modifiedDate = dateSelected;
			switch(callerFlag) {
				case CALL_FROM_CONDITIONAL_QUERY:
					dateSelectButton.setText(modifiedDate);
					break;
				case CALL_FROM_PLAN:
					dateSelectPlanButton.setText(modifiedDate);
					break;
				case CALL_FROM_ADDTOPLANDIALOG:
					AddToPlanDialogFragment mAddToPlanDialogFragment =
							(AddToPlanDialogFragment) getSupportFragmentManager()
									.findFragmentByTag("addtoplan_dialog_popped");
					if(mAddToPlanDialogFragment != null) {
						mAddToPlanDialogFragment.updateDate(modifiedDate);
					}
					break;
			}
		}
	}

	public ConditionDataForRequest getUserInputData() {
		ConditionDataForRequest conditionDataForRequest = new ConditionDataForRequest();

		String region = regionSelectSpinner.getSelectedItem()
				.toString();
		if (region.equals("대성리로")) {
			conditionDataForRequest.setRegion(1);
		} else if (region.equals("청평으로")) {
			conditionDataForRequest.setRegion(2);
		} else {
			conditionDataForRequest.setRegion(3);
		}

		try {
			conditionDataForRequest
					.setPeople(Integer.parseInt(numberOfPeopleSelectEditText.getText().toString()));
			Log.i(TAG, conditionDataForRequest.getPeople() + "");
		} catch (NumberFormatException e) {
			if(numberOfPeopleSelectEditText.getText().toString().equals("")) {
				Toast.makeText(MainActivity.this,
						R.string.notify_number_of_people_input, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MainActivity.this,
						R.string.notify_type_of_text_wrong, Toast.LENGTH_SHORT).show();
			}
			e.printStackTrace();
		}
		String[] tmp = modifiedDate.split(" ");
		conditionDataForRequest.setDate(tmp[0].substring(0, 4) + "-"
				+ tmp[1].split("월")[0] + "-" + tmp[2].split("일")[0]);
		Log.i(TAG, conditionDataForRequest.getDate());

		conditionDataForRequest.flag = CONDITION_SEARCH_MODE;

		return conditionDataForRequest;
	}

	@Override
	public void onSpecificInfoFragmentViewAttach(String roomName, String pensionName) {
		// configure actionbar for specific info page
		changeActionBarStyle(Color.BLACK, roomName, pensionName);
		// end of the configuration
		// **********************actionbar button issue...........
	}

	public void onSpecificInfoFragmentViewDetach(int numRoom) {
		// configure actionbar for result page
		changeActionBarStyle(Color.BLACK, getResources().getString(R.string.results), numRoom + "개의 방");
		// end of the configuration
	}

	@Override
	public void onAddToPlanButtonClicked(RoomInfoModel roomInfoModel) {

		if(searchMode == CONDITION_SEARCH_MODE) {
			mConditionDataForRequest = getUserInputData();
			AddToPlanDialogFragment addToPlanDialogFragment =
					AddToPlanDialogFragment.newInstance(modifiedDate, mConditionDataForRequest.getRegion() - 1, mConditionDataForRequest.getPeople(), CONDITION_SEARCH_MODE);
			addToPlanDialogFragment.show(getSupportFragmentManager(), "addtoplan_dialog_popped");
		}

		changeViewAtPlan(roomPlanLinearLayout, roomPlanLinearLayout.getChildAt(2),
				getLayoutInflater().inflate(R.layout.frame_room_plan,
						(LinearLayout) findViewById(R.id.layout_room), false), 2);

		// configure room addition/deletion section
		clearRoomPlanButton = (TextView) findViewById(R.id.btn_clear_room);
		clearRoomPlanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeViewAtPlan(roomPlanLinearLayout, roomPlanLinearLayout.getChildAt(2),
						getLayoutInflater().inflate(R.layout.frame_room_not_selected_plan,
								(LinearLayout) findViewById(R.id.layout_room), false), 2);
			}
		});

		// AsyncTask로 썸네일 이미지 가져오기!!!!!!
		// ThumbnailDownloadTaks().execute(roomInfoModel.getThumbnailURL());

		roomNamePlanTextView = (TextView) findViewById(R.id.textView_name_room_plan);
		roomNamePlanTextView.setText(roomInfoModel.getRoom_name());

		pensionNamePlanTextView = (TextView) findViewById(R.id.textView_name_pension_and_region_plan);
		pensionNamePlanTextView.setText(roomInfoModel.getPen_name() + "/" + roomInfoModel.getPen_region());

		numberPeopleStdMaxPlanTextView = (TextView) findViewById(R.id.textView_number_people_std_max_plan);
		numberPeopleStdMaxPlanTextView.setText(getResources().getString(R.string.standard)
				+ roomInfoModel.getRoom_std_people() + "/"
				+ roomInfoModel.getRoom_max_people() + getResources().getString(R.string.max));

		// Options goes here!!!!!!!!!

		roomPricePlanTextView = (TextView) findViewById(R.id.textView_price_room_plan);
		int roomPrice = roomInfoModel.getRoom_cost();
		if(roomPrice == 0)
			roomPricePlanTextView.setText(getResources().getString(R.string.telephone_inquiry));
		else
			roomPricePlanTextView.setText(getResources().getString(R.string.currency_unit) + roomPrice);

		// end of the configuration of room addition/deletion
	}

	private void changeViewAtPlan(ViewGroup parentView, View viewToBeDeleted, View viewToBeAdded, int index) {
		parentView.removeView(viewToBeDeleted);
		parentView.addView(viewToBeAdded, index);
	}

	private void changeActionBarStyle(int color, String titleText, String subtitleText) {
		actionBarBackgroundColor = new ColorDrawable(color);
		mActionBar.setBackgroundDrawable(actionBarBackgroundColor);

		mActionBar.setTitle(titleText);
		mActionBar.setSubtitle(subtitleText);
	}

	@Override
	public void onAddToPlanDialogFragmentViewDetached(String dateOfMT, int regionOfMT, int numberOfPeople, int sexRatioProgress) {
		setAutoCompleteBasicInfoPlan(dateOfMT, regionOfMT, numberOfPeople, sexRatioProgress);
		Toast.makeText(this, R.string.added_to_plan, Toast.LENGTH_LONG).show();
		doubleSideSlidingMenu.showSecondaryMenu(true);
	}

	private class ConditionDataForRequest {
		private int region;
		private String date;
		private int people;
		private int flag;

		public ConditionDataForRequest() {
			this.region = -1;
			this.date = null;
			this.people = -1;
			this.flag = -1;
		}

		public int getFlag() {
			return flag;
		}

		public void setFlag(int flag) {
			this.flag = flag;
		}

		public int getRegion() {
			return region;
		}

		public void setRegion(int region) {
			this.region = region;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public int getPeople() {
			return people;
		}

		public void setPeople(int people) {
			this.people = people;
		}

		public boolean isVariableSet() {

			// ************************************************************string compare reminded!!
			if (this.region != -1 && this.date != null && this.people != -1 && this.flag != -1) {
				return true;
			} else {
				return false;
			}
		}

		public String makeHttpGetURL() {
			if (isVariableSet()) {
				String httpGetURL = "http://mtplease.herokuapp.com/" + "pensions" + "?region=" + region + "&date="
						+ date + "&people=" + people + "&flag=1";

				return httpGetURL;
			}
			return null;
		}
	}

	/**
	 * @author In-Ho
	 *         AsyncTask for receiving data from the server.
	 *         Used AsyncTask to perform the task in the background
	 */
	private class ServerConnectionTask extends AsyncTask<String, Integer, String> {

		private ScrollTabHolder mScrollTabHolder;
		private int requestLabel;

		public ServerConnectionTask(ScrollTabHolder scrollTabHolder, int requestLabel) {
			this.mScrollTabHolder = scrollTabHolder;
			this.requestLabel = requestLabel;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// configure progress bar
			loadingProgressBar.setVisibility(View.VISIBLE);
			loadingProgressBar.setProgress(0);
			loadingBackground.setAlpha(100);
			// end of the configuration of the progress bar
		}

		@Override
		protected String doInBackground(String... urls) {
			try {
				Log.i(TAG, "URL: " + urls[0]);
				HttpClient mHttpClient = new DefaultHttpClient();
				HttpGet mHttpGet = new HttpGet(urls[0]);
				HttpConnectionParams.setConnectionTimeout(mHttpClient.getParams(), 5000);
				HttpResponse mHttpResponseGet = mHttpClient.execute(mHttpGet);
				HttpEntity resEntityGet = mHttpResponseGet.getEntity();

				if (resEntityGet != null) {
					Log.i(TAG, "HttpResponseGet Completed!!");
					return EntityUtils.toString(resEntityGet);
				}
			} catch (ClientProtocolException e) {
				Log.e(TAG, "ClientProtocolException");
				Log.e(TAG, "HttpResponseGet Failed....");
				requestLabel = NETWORK_CONNECTION_FAILED;
				e.printStackTrace();
			} catch (IOException e) {
				Log.e(TAG, "IOException");
				requestLabel = NETWORK_CONNECTION_FAILED;
				e.printStackTrace();
			} catch (Exception e) {
				requestLabel = NETWORK_CONNECTION_FAILED;
			}
			return null;
		}

		@Override
		protected void onPostExecute(String jsonString) {
			super.onPostExecute(jsonString);
			switch (requestLabel) {
				case TIMELINE:
					// create the mTimelineFragment with the Interface ScrollTabHolder
					try {
						JSONObject mJSONObject = new JSONObject(jsonString);
						Log.i(TAG, jsonString);
						// redundant to code. needs to be refactored
						mJSONObject = mJSONObject.getJSONObject("main");

						roomCountText.setText(mJSONObject.getString("roomCount") + "개의 방");

						mTimelineFragment = TimelineFragment.newInstance(mJSONObject.toString());
						mTimelineFragment.setScrollTabHolder(mScrollTabHolder);
						// end of creation of the mTimelineFragment

						// commit the mTimelineFragment to the current view
						mFragmentTransaction = mFragmentManager.beginTransaction();
						mFragmentTransaction.replace(R.id.body, mTimelineFragment);
						mFragmentTransaction.commit();
						// end of commission
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case RESULT:
					mConditionDataForRequest = getUserInputData();
					// create the mResultFragment with the Interface ScrollTabHolder
					mResultFragment = ResultFragment.newInstance(jsonString, mConditionDataForRequest.getDate());
					mResultFragment.setScrollTabHolder(mScrollTabHolder);
					mResultFragment.setFragmentManager(mFragmentManager);
					// end of creation of the mTimelineFragment

					// commit the mResultFragment to the current view
					mFragmentTransaction = mFragmentManager.beginTransaction();
					mFragmentTransaction.replace(R.id.body, mResultFragment);
					mFragmentTransaction.addToBackStack(null);
					mFragmentTransaction.commit();
					// end of commission
					break;
				case NETWORK_CONNECTION_FAILED:
					Toast.makeText(MainActivity.this, R.string.notify_network_error, Toast.LENGTH_SHORT);
					break;
			}

			// configure progress bar
			loadingProgressBar.setVisibility(View.GONE);
			loadingProgressBar.setProgress(100);
			loadingBackground.setAlpha(0);
			// end of the configuration of the progress bar
		}
	}
}
