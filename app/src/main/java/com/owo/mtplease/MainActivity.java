package com.owo.mtplease;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import notboringactionbar.AlphaForegroundColorSpan;


public class MainActivity extends ActionBarActivity implements ScrollTabHolder,
		TimelineFragment.OnTimelineFragmentInteractionListener, ResultFragment.OnResultFragmentInteractionListener,
		CalendarDialogFragment.OnDateConfirmedListener, SpecificInfoFragment.OnSpecificInfoFragmentInteractionListener,
		AddItemToPlanDialogFragment.OnAddItemToPlanDialogFragmentInteractionListener,
		ShoppingItemListFragment.OnShoppingItemListFragmentInteractionListener,
		ChangeItemNumberInPlanDialogFragment.OnChangeItemNumberInPlanDialogFragmentInteractionListener,
		UndoToastController.UndoToastListener {

	// Flags and Strings
	private static final String TAG = "MainActivity";
	private static final int CONDITION_SEARCH_MODE = 1;
	private static final int KEYWORD_SEARCH_MODE = 2;
	private static final int NETWORK_CONNECTION_FAILED = -1;
	private static final int TIMELINE = 1;
	private static final int RESULT = 2;
	private static final String MTPLEASE_URL = "http://mtplease.herokuapp.com/";
	private static final int CALL_FROM_CONDITIONAL_QUERY = 1;
	private static final int CALL_FROM_PLAN = 2;
	private static final int CALL_FROM_ADD_ITEM_TO_PLAN_DIALOG = 3;
	private static final int INDEX_OF_DAESUNGRI = 0;
	private static final int INDEX_OF_CHEONGPYUND = 1;
	private static final int INDEX_OF_GAPYUNG = 2;
	private static final int FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN = 0;
	private static final int CUSTOM_SHOPPING_ITEM_INPUT_MODE = 1;
	private static final int SHOPPING_ITEM_INPUT_MODE = 2;
	public static final int TIMELINE_FRAGMENT_VISIBLE = 1;
	public static final int RESULT_FRAGMENT_VISIBLE = 2;
	public static final int SPECIFIC_INFO_FRAGMENT_VISIBLE = 3;
	public static final int SHOPPINGITEMLIST_FRAGMENT_VISIBLE = 4;
	// End of the flags and strings

	// Fragments
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	private TimelineFragment mTimelineFragment = null;
	private ResultFragment mResultFragment = null;
	private SpecificInfoFragment mSpecificInfoFragment = null;
	private ShoppingItemListFragment mShoppingItemListFragment = null;
	// End of the fragments

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
	private LinearLayout mUndoToastView;
	private Button dateSelectPlanButton;
	private Spinner regionSelectPlanButton;
	private EditText numberOfPeopleSelectPlanEditText;
	private TextView numberOfMaleTextView;
	private TextView numberOfFemaleTextView;
	private SeekBar sexRatioPlanSeekBar;
	private EditText directInputRoomPriceEditText;
	private Button addDirectInputRoomPriceButton;
	private Button recommendItemsPlanButton;
	private LinearLayout roomPlanLinearLayout;
	private LinearLayout directInputRoomPlanLinearLayout;
	private RelativeLayout notSelectedRoomPlanRelativeLayout;
	private TextView clearRoomPlanButton;
	private LinearLayout meatPlanLinearLayout;
	private RelativeLayout notSelectedMeatPlanRelativeLayout;
	private TextView clearMeatPlanButton;
	private LinearLayout alcoholPlanLinearLayout;
	private RelativeLayout notSelectedAlcoholPlanRelativeLayout;
	private TextView clearAlcoholPlanButton;
	private LinearLayout othersPlanLinearLayout;
	private RelativeLayout notSelectedOthersPlanRelativeLayout;
	private TextView clearOthersPlanButton;
	private TextView totalRoomPrice;
	private TextView totalMeatPrice;
	private TextView totalAlcoholPrice;
	private TextView totalOthersPrice;
	private TextView totalPlanCost;
	private TextView tempItemTotalPricePlanTextView;	// temporary TextView instance
	private Button tempNumberPickerCallButton;
	// End of User Interface Views

	// Controller
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
				int newNumPeople = Integer.parseInt(s.toString());
				sexRatioPlanSeekBar.setMax(newNumPeople);
				if(oldNumPeople != 0)
					applyNumberOfPeopleChangeToItems(oldNumPeople, newNumPeople);
				oldNumPeople = newNumPeople;

				if(!numberOfPeopleSelectPlanEditText.getText().toString().equals("")) {
					recommendItemsPlanButton.setEnabled(true);
				} else {
					recommendItemsPlanButton.setEnabled(false);
				}
			} catch (NumberFormatException e) {
				sexRatioPlanSeekBar.setMax(0);
				e.printStackTrace();
			}
		}
	};
	private UndoToastController mUndoToastController;
	// End of Controllers

	// Others
	private Calendar calendar = Calendar.getInstance();
	private String modifiedDate;
	private ConditionDataForRequest mConditionDataForRequest = null;
	private PlanModel mPlanModel = null;
	private float clampValue;
	private int searchMode;
	private int oldNumPeople = 0;	// temporary variable to store the number of people that user input right before he/she changes the value;
	private boolean isBackButtonPressed = false;
	// End of others

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

		// configure mode of the search
		searchMode = CONDITION_SEARCH_MODE;
		// end of the configuration of the mode of the search

		// get timeline data from the server and create the mTimelineFragment
		new DataRequestTask(TIMELINE).execute(MTPLEASE_URL);
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

		// instantiate mPlanModel to store plan data inside it.
		mPlanModel = new PlanModel();
		// end of instantiation

		// configure a custom toast controller
		mUndoToastView = (LinearLayout) findViewById(R.id.layout_toast_plan);
		mUndoToastController = new UndoToastController(mUndoToastView, this);
		// end of configuration

		dateSelectPlanButton = (Button) findViewById(R.id.btn_select_date_plan);
		dateSelectPlanButton.setText(modifiedDate);
		dateSelectPlanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				numberOfPeopleSelectPlanEditText.clearFocus();
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
		numberOfPeopleSelectPlanEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Log.d(TAG, "onFocusChange");
				if(!hasFocus)
					hideKeyboard(v);
			}
		});

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
				numberOfFemaleTextView.setText((seekBar.getMax() - progress) + getResources().getString(R.string.people_unit));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				numberOfPeopleSelectPlanEditText.clearFocus();
				directInputRoomPriceEditText.clearFocus();
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		directInputRoomPriceEditText = (EditText) findViewById(R.id.editText_input_direct_price_room_plan);
		directInputRoomPriceEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Log.d(TAG, "onFocusChange");
				if(!hasFocus)
					hideKeyboard(v);
			}
		});

		addDirectInputRoomPriceButton = (Button) findViewById(R.id.btn_add_direct_price_room_plan);
		addDirectInputRoomPriceButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (!addDirectInputRoomPriceButton.getText().toString().equals(""))
						addDirectInputRoomPriceToPlan(Integer.parseInt(directInputRoomPriceEditText.getText().toString()));
				} catch(NumberFormatException e) {
					Toast.makeText(v.getContext(), R.string.please_type_only_number_for_price, Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		});

		recommendItemsPlanButton = (Button) findViewById(R.id.btn_recommend_items_plan);
		recommendItemsPlanButton.setEnabled(false);

		totalPlanCost = (TextView) findViewById(R.id.textView_cost_total_plan);
		totalPlanCost.setText(castItemPriceToString(0));

		roomPlanLinearLayout = (LinearLayout) findViewById(R.id.layout_room);

		directInputRoomPlanLinearLayout = (LinearLayout) findViewById(R.id.layout_room_direct_input);

		notSelectedRoomPlanRelativeLayout = (RelativeLayout) findViewById(R.id.layout_add_room);
		notSelectedRoomPlanRelativeLayout.setOnClickListener(new View.OnClickListener() {
			// evokes when black block(the view "frame_add_room_plan") is clicked
			@Override
			public void onClick(View v) {
				Toast.makeText(v.getContext(), R.string.please_search_for_room, Toast.LENGTH_SHORT).show();
				doubleSideSlidingMenu.toggle();
			}
		});

		clearRoomPlanButton = (TextView) findViewById(R.id.btn_clear_room);
		clearRoomPlanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mPlanModel.getRoomDataCount() > 0) {
					for(int roomIndex = 0; roomIndex < mPlanModel.getRoomDataCount(); roomIndex++)
						planItemFadeOut(roomPlanLinearLayout.getChildAt(roomIndex));

					for(int directInputRoomIndex = 0;
						directInputRoomIndex < mPlanModel.getDirectInputRoomDataCount(); directInputRoomIndex++)
						planItemFadeOut(directInputRoomPlanLinearLayout.getChildAt(directInputRoomIndex));

					/*mUndoToastController.showUndoBar(getResources().getString(R.string.added_rooms_cleared),
							UndoToastController.CLEAR_ADDED_ROOMS_CASE);*/
					mUndoToastController.showUndoBar(getResources().getString(R.string.added_rooms_cleared),
							UndoToastController.CLEAR_ADDED_ROOMS_CASE, -1);
				}
			}
		});

		totalRoomPrice = (TextView) findViewById(R.id.textView_price_total_room_plan);
		totalRoomPrice.setText(getResources().getString(R.string.currency_unit) + "0");

		meatPlanLinearLayout = (LinearLayout) findViewById(R.id.layout_meat);

		notSelectedMeatPlanRelativeLayout = (RelativeLayout) findViewById(R.id.layout_add_meat);
		notSelectedMeatPlanRelativeLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showShoppingItemList(R.string.please_select_meat, ShoppingItemListFragment.MEAT_ITEM);
			}
		});

		clearMeatPlanButton = (TextView) findViewById(R.id.btn_clear_meat);
		clearMeatPlanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mPlanModel.getItemDataCount(ShoppingItemListFragment.MEAT_ITEM) > 0) {
					for(int meatIndex = 0;
						meatIndex < mPlanModel.getItemDataCount(ShoppingItemListFragment.MEAT_ITEM);
						meatIndex++)
						planItemFadeOut(meatPlanLinearLayout.getChildAt(meatIndex));

					mUndoToastController.showUndoBar(getResources().getString(R.string.added_meats_cleared),
							UndoToastController.CLEAR_ADDED_MEATS_CASE, -1);
				}
			}
		});

		totalMeatPrice = (TextView) findViewById(R.id.textView_price_total_meat_plan);
		totalMeatPrice.setText(getResources().getString(R.string.currency_unit) + "0");

		alcoholPlanLinearLayout = (LinearLayout) findViewById(R.id.layout_alcohol);

		notSelectedAlcoholPlanRelativeLayout = (RelativeLayout) findViewById(R.id.layout_add_alcohol);
		notSelectedAlcoholPlanRelativeLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showShoppingItemList(R.string.please_select_alcohol, ShoppingItemListFragment.ALCOHOL_ITEM);
			}
		});

		clearAlcoholPlanButton = (TextView) findViewById(R.id.btn_clear_alcohol);
		clearAlcoholPlanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mPlanModel.getItemDataCount(ShoppingItemListFragment.ALCOHOL_ITEM) > 0) {
					for(int alcoholIndex = 0;
						alcoholIndex < mPlanModel.getItemDataCount(ShoppingItemListFragment.ALCOHOL_ITEM);
						alcoholIndex++)
						planItemFadeOut(alcoholPlanLinearLayout.getChildAt(alcoholIndex));

					mUndoToastController.showUndoBar(getResources().getString(R.string.added_alcohols_cleared),
							UndoToastController.CLEAR_ADDED_ALCOHOLS_CASE, -1);
				}
			}
		});

		totalAlcoholPrice = (TextView) findViewById(R.id.textView_price_total_alcohol_plan);
		totalAlcoholPrice.setText(getResources().getString(R.string.currency_unit) + "0");

		othersPlanLinearLayout = (LinearLayout) findViewById(R.id.layout_others);

		notSelectedOthersPlanRelativeLayout = (RelativeLayout) findViewById(R.id.layout_add_others);
		notSelectedOthersPlanRelativeLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showShoppingItemList(R.string.please_select_others, ShoppingItemListFragment.OTHERS_ITEM);
			}
		});

		clearOthersPlanButton = (TextView) findViewById(R.id.btn_clear_others);
		clearOthersPlanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mPlanModel.getItemDataCount(ShoppingItemListFragment.OTHERS_ITEM) > 0) {
					for(int othersIndex = 0;
						othersIndex < mPlanModel.getItemDataCount(ShoppingItemListFragment.OTHERS_ITEM);
						othersIndex++)
						planItemFadeOut(othersPlanLinearLayout.getChildAt(othersIndex));

					mUndoToastController.showUndoBar(getResources().getString(R.string.added_others_cleared),
							UndoToastController.CLEAR_ADDED_OTHERS_CASE, -1);
				}
			}
		});

		totalOthersPrice = (TextView) findViewById(R.id.textView_price_total_others_plan);
		totalOthersPrice.setText(getResources().getString(R.string.currency_unit) + "0");
	}

	private void showShoppingItemList(int stringResId, int shoppingItemType) {
		Toast.makeText(this, stringResId, Toast.LENGTH_SHORT).show();
		mShoppingItemListFragment = ShoppingItemListFragment.newInstance(shoppingItemType);
		// commit the SpecificInfoFragment to the current view
		beginFragmentTransaction(mShoppingItemListFragment, R.id.body_background);
		// end of the comission
		doubleSideSlidingMenu.toggle();
	}

	/*private void setAutoCompleteBasicInfoPlan() {
		mConditionDataForRequest = getUserInputData();
		dateSelectPlanButton.setText(mConditionDataForRequest.getDateWrittenLang());
		regionSelectPlanButton.setSelection(mConditionDataForRequest.getRegion() - 1);
		numberOfPeopleSelectPlanEditText.setText(String.valueOf(mConditionDataForRequest.getPeople()));
	}

	private void setAutoCompleteBasicInfoPlan(String mtDate, int regionOfMT, int numberOfPeople, int sexRatioProgress) {
		dateSelectPlanButton.setText(mtDate);
		regionSelectPlanButton.setSelection(regionOfMT);
		numberOfPeopleSelectPlanEditText.setText(String.valueOf(numberOfPeople));
		sexRatioPlanSeekBar.setProgress(sexRatioProgress);
	}*/

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
		numberOfPeopleSelectEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Log.d(TAG, "onFocusChange");
				if(!hasFocus)
					hideKeyboard(v);
			}
		});

		roomSearchButton = (ImageButton) findViewById(R.id.btn_search_room);
		roomSearchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					mConditionDataForRequest = getUserInputData();

					numberOfPeopleSelectEditText.clearFocus();
					/*if (mConditionDataForRequest.getFlag() == CONDITION_SEARCH_MODE) {
						setAutoCompleteBasicInfoPlan();
					}*/

					new DataRequestTask(RESULT).execute(mConditionDataForRequest.makeHttpGetURL());
				} catch (NumberFormatException e) {
					if (numberOfPeopleSelectEditText.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this,
								R.string.please_number_of_people_input, Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MainActivity.this,
								R.string.please_type_only_number_for_number_of_people, Toast.LENGTH_SHORT).show();
					}
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
	public void onCreateResultFragmentView(boolean noResults, int numRoom) {

		mHeader.setVisibility(View.VISIBLE);

		dateQuerySubTabText.setVisibility(View.VISIBLE);
		regionQuerySubTabText.setVisibility(View.VISIBLE);
		numberOfPeopleQuerySubTabText.setVisibility(View.VISIBLE);

		Log.d(TAG, noResults + "");

		// configure actionbar for result page
		if (noResults) {
			changeActionBarStyle(Color.BLACK, getResources().getString(R.string.results), getResources().getString(R.string.no_results));
		} else {
			changeActionBarStyle(Color.BLACK, getResources().getString(R.string.results), numRoom + "개의 방");
		}
		// end of the configuration

		// configure subtab for result page
		mConditionDataForRequest = getUserInputData();
		dateQuerySubTabText.setText(mConditionDataForRequest.getDateWrittenLang());
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

		if(!noResults && !isBackButtonPressed) {
			Log.d(TAG, "dfsdfasdfasdfasdf");
			mHeader.setTranslationY(-mMinHeaderHeightForResultFragment + getSupportActionBar().getHeight());
		}

		isBackButtonPressed = false;
	}

	@Override
	public void onDestroyResultFragmentView() {
		mHeader.setVisibility(View.GONE);
	}

	@Override
	public void onPreLoadSpecificInfoFragment() {
		startLoadingProgress();
	}

	@Override
	public void onLoadSpecificInfoFragment(RoomInfoModel roomInfoModel, JSONArray roomArray) {
		mSpecificInfoFragment = SpecificInfoFragment.newInstance(roomInfoModel, roomArray.length());

		// commit the SpecificInfoFragment to the current view
		beginFragmentTransaction(mSpecificInfoFragment, R.id.body_background);
		// end of commission
	}

	@Override
	public void onPostLoadSpecificInfoFragment() {
		endLoadingProgress();
	}

	@Override
	public void onCreateTimelineFragmentView() {
		mHeader.setVisibility(View.VISIBLE);

		dateQuerySubTabText.setVisibility(View.INVISIBLE);
		regionQuerySubTabText.setVisibility(View.INVISIBLE);
		numberOfPeopleQuerySubTabText.setVisibility(View.INVISIBLE);

		changeActionBarStyle(Color.TRANSPARENT, getResources().getString(R.string.app_name), null);
		mHeader.setTranslationY(0);

		isBackButtonPressed = false;
	}

	@Override
	public void onDestroyTimelineFragmentView() {
		mHeader.setVisibility(View.GONE);
	}

	@Override
	public void adjustScroll(int scrollHeight) {}

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
			/*case SPECIFIC_INFO_FRAGMENT_VISIBLE:
				ratio = (float) getSupportActionBar().getHeight() / (float) (scrollY + 1);
				clampValue = clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F);
				break;*/
			case SPECIFIC_INFO_FRAGMENT_VISIBLE:
			case SHOPPINGITEMLIST_FRAGMENT_VISIBLE:
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
				/*mSpannableStringVariable = new SpannableString(getSupportActionBar().getTitle());
				mSpannableStringVariable.setSpan(mAlphaForegroundColorSpan, 0, mSpannableStringVariable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				mActionBar.setTitle(mSpannableStringVariable);
				mSpannableStringVariable = null;
				mSpannableStringVariable = new SpannableString(getSupportActionBar().getSubtitle());
				mSpannableStringVariable.setSpan(mAlphaForegroundColorSpan, 0, mSpannableStringVariable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				mActionBar.setSubtitle(mSpannableStringVariable);*/
				break;
			case SHOPPINGITEMLIST_FRAGMENT_VISIBLE:
				break;
			default:
				return;
		}
	}

	@Override
	public void onDateConfirmButtonClicked(String dateSelected, int callerFlag) {
		if (dateSelected != null) {
			modifiedDate = dateSelected;
			switch (callerFlag) {
				case CALL_FROM_CONDITIONAL_QUERY:
					dateSelectButton.setText(modifiedDate);
					break;
				case CALL_FROM_PLAN:
					dateSelectPlanButton.setText(modifiedDate);
					break;
				/*case CALL_FROM_ADDTOPLANDIALOG:
					AddToPlanDialogFragment mAddToPlanDialogFragment =
							(AddToPlanDialogFragment) getSupportFragmentManager()
									.findFragmentByTag("addtoplan_dialog_popped");
					if (mAddToPlanDialogFragment != null) {
						mAddToPlanDialogFragment.updateDate(modifiedDate);
					}
					break;*/
			}
		}
	}

	public ConditionDataForRequest getUserInputData() throws NumberFormatException{
		ConditionDataForRequest conditionDataForRequest = new ConditionDataForRequest();

		String region = regionSelectSpinner.getSelectedItem()
				.toString();
		if (region.equals(getResources().getString(R.string.daesungri))) {
			conditionDataForRequest.setRegion(INDEX_OF_DAESUNGRI + 1);
		} else if (region.equals(R.string.cheongpyung)) {
			conditionDataForRequest.setRegion(INDEX_OF_CHEONGPYUND + 1);
		} else {
			conditionDataForRequest.setRegion(INDEX_OF_GAPYUNG + 1);
		}


		conditionDataForRequest
				.setPeople(Integer.parseInt(numberOfPeopleSelectEditText.getText().toString()));
		Log.i(TAG, conditionDataForRequest.getPeople() + "");


		conditionDataForRequest.setDateWrittenLang(modifiedDate);

		String[] tmp = modifiedDate.split(" ");
		conditionDataForRequest.setDate(tmp[0].substring(0, 4) + "-"
				+ tmp[1].split("월")[0] + "-" + tmp[2].split("일")[0]);
		Log.i(TAG, conditionDataForRequest.getDate());

		if (searchMode == CONDITION_SEARCH_MODE) {
			conditionDataForRequest.setFlag(CONDITION_SEARCH_MODE);
		} else {
			conditionDataForRequest.setFlag(KEYWORD_SEARCH_MODE);
		}

		return conditionDataForRequest;
	}

	@Override
	public void onCreateSpecificInfoFragmentView(String roomName, String pensionName) {
		// configure actionbar for specific info page
		changeActionBarStyle(Color.BLACK, roomName, pensionName);
		// end of the configuration
		// **********************actionbar button issue...........

		isBackButtonPressed = false;
	}

	@Override
	public void onClickAddRoomToPlanButton(final RoomInfoModel roomInfoModel) {

		// check rather room has been added already in the plan or not
		if(mPlanModel.isRoomAddedAlready(roomInfoModel.getPen_id(),
				roomInfoModel.getRoom_name(), roomInfoModel.getRoom_cost())) {
			Toast.makeText(this, R.string.room_already_added_before, Toast.LENGTH_LONG).show();
			return;
		}

		int roomDataCount = mPlanModel.getRoomDataCount();
		final int roomPlanViewIndex = roomDataCount;
		// "roomDataCount + FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN" -> used this notation, because
		// there is a gap, between first index of the roomDataLinkedList(inside PlanModel class)
		// and first index of the roomPlanLinearLayout(starts the index from 2), which is 2.
		final View roomPlanView = getLayoutInflater().inflate(R.layout.frame_room_selected_plan,
				(LinearLayout) findViewById(R.id.layout_room), false);
		addViewAtPlan(roomPlanLinearLayout, roomPlanView, roomPlanViewIndex);

		// configure the room delete button
		ImageView deleteRoomButtonImageView =
				(ImageView) roomPlanView.findViewById(R.id.imageView_btn_delete_room_plan);
		deleteRoomButtonImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				planItemFadeOut(roomPlanView);

				mUndoToastController.showUndoBar(roomInfoModel.getRoom_name() + " " + getResources().getString(R.string.deleted),
						UndoToastController.DELETE_ADDED_ROOM_CASE, roomPlanViewIndex);
//				mUndoToastController.setRoomViewAndData(roomPlanView, roomInfoModel.getPen_id(), roomInfoModel.getRoom_name(), roomInfoModel.getRoom_cost());
			}
		});
		// end of the configuration

		// get a thumbnail image of the room from the server and set it as an image
		// of the added room view
		ImageView roomThumbnailImageView = (ImageView) roomPlanView.findViewById(R.id.imageView_room_thumbnail);
		ProgressBar roomThumbnailImageLoadingProgressBar = (ProgressBar) roomPlanView.
				findViewById(R.id.progressBar_thumbnailImage_room_plan);

		new ImageLoadingTask(roomThumbnailImageView, roomThumbnailImageLoadingProgressBar).
				execute(roomInfoModel.getRoomThumbnailImageURL());
		// end of getting and setting of the thumbnail image

		// set a room name of the added room view
		TextView roomNamePlanTextView = (TextView) roomPlanView.findViewById(R.id.textView_name_room_plan);
		roomNamePlanTextView.setText(roomInfoModel.getRoom_name());
		// end of the setting

		// set a pension name of the added room view
		TextView pensionNamePlanTextView =
				(TextView) roomPlanView.findViewById(R.id.textView_name_pension_and_region_plan);
		pensionNamePlanTextView.setText(roomInfoModel.getPen_name() + " / " + roomInfoModel.getPen_region());
		// end of the setting

		// set standard and maximum number of people allowed of the added room view
		TextView numberPeopleStdMaxPlanTextView =
				(TextView) roomPlanView.findViewById(R.id.textView_number_people_std_max_plan);
		numberPeopleStdMaxPlanTextView.setText(getResources().getString(R.string.standard)
				+ roomInfoModel.getRoom_std_people() + " / " + getResources().getString(R.string.max)
				+ roomInfoModel.getRoom_max_people());
		// end of the setting

		// Options goes here!!!!!!!!!

		// set a room price of the added room view
		TextView roomPricePlanTextView = (TextView) roomPlanView.findViewById(R.id.textView_price_room_plan);
		int roomPrice = roomInfoModel.getRoom_cost();
		if (roomPrice == 0) {
			roomPricePlanTextView.setText(getResources().getString(R.string.telephone_inquiry));
		} else {
			roomPricePlanTextView.setText(castItemPriceToString(roomPrice));
		}

		// store room data in the mPlanModel
		mPlanModel.addRoomData(roomInfoModel.getPen_id(),
				roomInfoModel.getRoom_name(), roomInfoModel.getRoom_cost());

		// calculate the total cost for the added rooms
		totalRoomPrice.setText(castItemPriceToString(mPlanModel.getTotalRoomCost()));

		// calculate the total cost for the whole plan
		totalPlanCost.setText(castItemPriceToString(mPlanModel.getPlanTotalCost()));

		// notify room is added to the plan to the user
		Toast.makeText(this, R.string.added_to_plan, Toast.LENGTH_LONG).show();

		// show plan
		doubleSideSlidingMenu.showSecondaryMenu(true);

		/*mConditionDataForRequest = getUserInputData();

		if (mConditionDataForRequest.getFlag() == CONDITION_SEARCH_MODE) {
			AddToPlanDialogFragment addToPlanDialogFragment =
					AddToPlanDialogFragment.newInstance(mConditionDataForRequest.getDateWrittenLang(),
							mConditionDataForRequest.getRegion() - 1,
							mConditionDataForRequest.getPeople(), CONDITION_SEARCH_MODE);
			addToPlanDialogFragment.show(getSupportFragmentManager(), "addtoplan_dialog_popped");
		}*/
	}

	private void addViewAtPlan(ViewGroup parentView, View viewToBeAdded, int index) {
		parentView.addView(viewToBeAdded, index);
	}

	private void addDirectInputRoomPriceToPlan(final int directInputRoomPrice) {

		final int directInputRoomDataCount = mPlanModel.getRoomDataCount();
		final int directInputRoomPlanViewIndex = directInputRoomDataCount;
		// "directInputRoomDataCount + FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN" -> used this notation, because
		// there is a gap, between first index of the roomDataLinkedList(inside PlanModel class)
		// and first index of the roomPlanLinearLayout(starts the index from 2), which is 2.
		final View directInputRoomPlanView = getLayoutInflater().inflate(R.layout.frame_room_direct_input_plan,
				(LinearLayout) findViewById(R.id.layout_room_direct_input), false);
		addViewAtPlan(directInputRoomPlanLinearLayout, directInputRoomPlanView, directInputRoomPlanViewIndex);

		// configure the room delete button
		ImageView deleteDirectInputRoomPlanButtonImageView =
				(ImageView) directInputRoomPlanView.findViewById(R.id.imageView_btn_delete_room_direct_input_plan);
		deleteDirectInputRoomPlanButtonImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				planItemFadeOut(directInputRoomPlanView);

				mUndoToastController.showUndoBar(getResources().getString(R.string.directly_added_room)
						+ getResources().getString(R.string.deleted), UndoToastController.DELETE_DIRECTLY_INPUT_ROOM_CASE,
						directInputRoomPlanViewIndex);
//				mUndoToastController.setDirectInputRoomViewAndData(directInputRoomPlanView, directInputRoomPrice);
			}
		});
		// end of the configuration

		// set a directly input room name of the added room view
/*		TextView directInputRoomNamePlanTextView = (TextView) directInputRoomPlanView.findViewById(R.id.textView_room_direct_input_plan);
		directInputRoomNamePlanTextView.setText(R.string.direct_input_room);*/
		// end of the setting

		// set a directly input room price of the added room view
		TextView directInputRoomPricePlanTextView = (TextView) directInputRoomPlanView.findViewById(R.id.textView_price_room_direct_input_plan);
		directInputRoomPricePlanTextView.setText(castItemPriceToString(directInputRoomPrice));
		// end of the setting

		// store room data in the mPlanModel
		mPlanModel.addDirectInputRoomData(directInputRoomPrice);

		// calculate the total cost for the added rooms
		totalRoomPrice.setText(castItemPriceToString(mPlanModel.getTotalRoomCost()));

		// calculate the total cost for the whole plan
		totalPlanCost.setText(castItemPriceToString(mPlanModel.getPlanTotalCost()));

		// notify room is added to the plan to the user
		Toast.makeText(this, R.string.added_to_plan, Toast.LENGTH_LONG).show();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(doubleSideSlidingMenu.isSecondaryMenuShowing()) {
				doubleSideSlidingMenu.toggle();
				return false;
			}

			if(doubleSideSlidingMenu.isMenuShowing()) {
				doubleSideSlidingMenu.toggle();
				return false;
			}

			if(mShoppingItemListFragment != null && mShoppingItemListFragment.isVisible()) {
				Log.d(TAG,"Visible");
				doubleSideSlidingMenu.showSecondaryMenu(true);
				mFragmentManager.popBackStack();
				return false;
			}

			isBackButtonPressed = true;
			Log.d(TAG, "onKeyDonwn");
		}
		return super.onKeyDown(keyCode, event);
	}

	private String castItemPriceToString(int price) {
		String totalRoomCostString = String.valueOf(price);
		String totalRoomCostStringChanged = "";

		if(totalRoomCostString.length() > 0) {
			int charCounter = 0;
			for (int i = totalRoomCostString.length() - 1; i >= 0; i--) {
				if(charCounter != 0 && charCounter % 3 == 0)
					totalRoomCostStringChanged += "," + totalRoomCostString.charAt(i);
				else
					totalRoomCostStringChanged += totalRoomCostString.charAt(i) ;
				charCounter++;
			}
		}
		return getResources().getString(R.string.currency_unit) +
				new StringBuffer(totalRoomCostStringChanged).reverse().toString();
	}

	private void changeActionBarStyle(int color, String titleText, String subtitleText) {
		actionBarBackgroundColor = new ColorDrawable(color);
		mActionBar.setBackgroundDrawable(actionBarBackgroundColor);

		mActionBar.setTitle(titleText);
		mActionBar.setSubtitle(subtitleText);
	}

	/*@Override
	public void onAddToPlanDialogFragmentViewDetached(String mtDate, int regionOfMT, int numberOfPeople, int sexRatioProgress) {
		*//*setAutoCompleteBasicInfoPlan(mtDate, regionOfMT, numberOfPeople, sexRatioProgress);*//*
		Toast.makeText(this, R.string.added_to_plan, Toast.LENGTH_LONG).show();
		doubleSideSlidingMenu.showSecondaryMenu(true);
	}*/

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

	private void beginFragmentTransaction(Fragment targetFragment, int containerViewId) {

		mFragmentTransaction = mFragmentManager.beginTransaction();
		mFragmentTransaction.replace(containerViewId, targetFragment);
		mFragmentTransaction.addToBackStack(null);

		mFragmentTransaction.commit();
	}

	@Override
	public void onCreateShoppingItemListFragmentView(int itemType, int numRoom) {
		// configure actionbar for specific info page
		String itemTypeString = null;

		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				itemTypeString = getResources().getString(R.string.meat);
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				itemTypeString = getResources().getString(R.string.alcohol);
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				itemTypeString = getResources().getString(R.string.others);
		}

		changeActionBarStyle(Color.BLACK, itemTypeString, numRoom + getResources().getString(R.string.n_results));
		// end of the configuration

		isBackButtonPressed = false;
	}

	@Override
	public void onDestroyShoppingItemListFragmentView() {

	}

	@Override
	public void onClickItem(int itemType, String itemName, String itemUnit, String itemUnitCount, int itemPrice) {
		AddItemToPlanDialogFragment addItemToPlanDialogFragment =
				AddItemToPlanDialogFragment.
						newInstance(itemType, itemName, itemUnit, itemUnitCount, itemPrice, SHOPPING_ITEM_INPUT_MODE);

		addItemToPlanDialogFragment.show(mFragmentManager, "addItemToPlanDialogFragment");
	}

	@Override
	public void onClickItem(int itemType) {
		AddItemToPlanDialogFragment addItemToPlanDialogFragment =
				AddItemToPlanDialogFragment.newInstance(itemType, CUSTOM_SHOPPING_ITEM_INPUT_MODE);

		addItemToPlanDialogFragment.show(mFragmentManager, "addItemToPlanDialogFragment");
	}

	@Override
	public void onClickAddItemToPlanButton(final int itemType, final String itemName, final int itemUnitPrice,
										   final int itemCount, final String itemUnit, final String itemCountUnit) {

		// check rather room has been added already in the plan or not
		if(mPlanModel.isItemAddedAlready(itemType, itemName, itemUnitPrice)) {
			Toast.makeText(this, R.string.item_already_added_before, Toast.LENGTH_LONG).show();
			return;
		}

		int itemDataCount = mPlanModel.getItemDataCount(itemType);
		final int itemPlanViewIndex = itemDataCount + FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN;
		// "itemDataCount + FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN" -> used this notation, because
		// there is a gap, between first index of the [itemData]LinkedList(inside PlanModel class)
		// and first index of the roomPlanLinearLayout(starts the index from 2), which is 2.
		final View itemPlanView = getLayoutInflater().inflate(R.layout.frame_shopping_item_selected_plan,
				(LinearLayout) findViewById(R.id.layout_room), false);

		ImageView deleteItemButtonPlanImageView =
				(ImageView) itemPlanView.findViewById(R.id.imageView_btn_delete_item_plan);

		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				addViewAtPlan(meatPlanLinearLayout, itemPlanView, itemPlanViewIndex);
				deleteItemButtonPlanImageView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						planItemFadeOut(itemPlanView);

						mUndoToastController.showUndoBar(itemName + getResources().getString(R.string.deleted),
								UndoToastController.DELETE_ADDED_MEAT_CASE, itemPlanViewIndex);
//						mUndoToastController.setItemViewAndData(itemPlanView, itemType, itemName, itemUnitPrice);
					}
				});
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				addViewAtPlan(alcoholPlanLinearLayout, itemPlanView, itemPlanViewIndex);
				deleteItemButtonPlanImageView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						planItemFadeOut(itemPlanView);

						mUndoToastController.showUndoBar(itemName + getResources().getString(R.string.deleted),
								UndoToastController.DELETE_ADDED_ALCOHOL_CASE, itemPlanViewIndex);
//						mUndoToastController.setItemViewAndData(itemPlanView, itemType, itemName, itemUnitPrice);
					}
				});
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				addViewAtPlan(othersPlanLinearLayout, itemPlanView, itemPlanViewIndex);
				deleteItemButtonPlanImageView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						planItemFadeOut(itemPlanView);

						mUndoToastController.showUndoBar(itemName + getResources().getString(R.string.deleted),
								UndoToastController.DELETE_ADDED_OTHERS_CASE, itemPlanViewIndex);
//						mUndoToastController.setItemViewAndData(itemPlanView, itemType, itemName, itemUnitPrice);
					}
				});
				break;
		}

		TextView itemNamePlanTextView =
				(TextView) itemPlanView.findViewById(R.id.textView_name_item_plan);
		itemNamePlanTextView.setText(itemName);

		TextView itemUnitPlanTextView =
				(TextView) itemPlanView.findViewById(R.id.textView_unit_item_plan);
		itemUnitPlanTextView.setText(itemUnit);

		TextView itemUnitPricePlanTextView =
				(TextView) itemPlanView.findViewById(R.id.textView_unit_price_item_plan);
		itemUnitPricePlanTextView.setText(castItemPriceToString(itemUnitPrice));

		TextView itemCountUnitPlanTextView =
				(TextView) itemPlanView.findViewById(R.id.textView_count_unit_item_plan);
		itemCountUnitPlanTextView.setText(itemCountUnit);

		final TextView itemTotalPricePlanTextView =
				(TextView) itemPlanView.findViewById(R.id.textView_price_total_item_plan);
		itemTotalPricePlanTextView.setText(castItemPriceToString(itemUnitPrice * itemCount));

		final Button numberPickerCallButton =
				(Button) itemPlanView.findViewById(R.id.btn_number_picker_item_plan);
		numberPickerCallButton.setText(String.valueOf(itemCount));
		numberPickerCallButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tempItemTotalPricePlanTextView = itemTotalPricePlanTextView;
				tempNumberPickerCallButton = numberPickerCallButton;
				ChangeItemNumberInPlanDialogFragment
						changeItemNumberInPlanDialogFragment =
						ChangeItemNumberInPlanDialogFragment.
								newInstance(itemType, itemName, itemUnit, itemUnitPrice,
										mPlanModel.getSingleItemCount(itemType, itemName, itemUnitPrice), itemCountUnit);
				changeItemNumberInPlanDialogFragment.show(mFragmentManager, "changeItemNumberInPlanDialogFragment");
			}
		});

		// store item data in the mPlanModel
		mPlanModel.addItemData(itemType, itemName, itemUnitPrice, itemCount);

		// calculate the total cost for the added rooms
		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				totalMeatPrice.setText(castItemPriceToString(mPlanModel.getTotalItemCost(itemType)));
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				totalAlcoholPrice.setText(castItemPriceToString(mPlanModel.getTotalItemCost(itemType)));
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				totalOthersPrice.setText(castItemPriceToString(mPlanModel.getTotalItemCost(itemType)));
				break;
		}

		// calculate the total cost for the whole plan
		totalPlanCost.setText(castItemPriceToString(mPlanModel.getPlanTotalCost()));

		// notify room is added to the plan to the user
		Toast.makeText(this, R.string.added_to_plan, Toast.LENGTH_SHORT).show();

		// show plan
		doubleSideSlidingMenu.showSecondaryMenu(true);

	}

	@Override
	public void onClickChangeButton(int itemType, String itemName, int itemUnitPrice, int newItemCount) {
		tempItemTotalPricePlanTextView.setText(castItemPriceToString(itemUnitPrice * newItemCount));
		tempNumberPickerCallButton.setText(String.valueOf(newItemCount));

		mPlanModel.changeItemCount(itemType, itemName, itemUnitPrice, newItemCount);

		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				totalMeatPrice.setText(castItemPriceToString(mPlanModel.getTotalItemCost(itemType)));
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				totalAlcoholPrice.setText(castItemPriceToString(mPlanModel.getTotalItemCost(itemType)));
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				totalOthersPrice.setText(castItemPriceToString(mPlanModel.getTotalItemCost(itemType)));
				break;
		}

		// calculate the total cost for the whole plan
		totalPlanCost.setText(castItemPriceToString(mPlanModel.getPlanTotalCost()));
	}

	private void applyNumberOfPeopleChangeToItems(int oldNumPeople, int newNumPeople) {
		float rate = newNumPeople / oldNumPeople;

		Log.d(TAG, "rate:" + rate);

		for(int itemType = 1; itemType < 4; itemType++) {
			for (int singleItemIndex = FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN;
				 singleItemIndex < FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN + mPlanModel.getItemDataCount(itemType);
				 singleItemIndex++) {
				View singleShoppingItemLayout = null;
				Button numberPickerButton;
				int numPeople;
				try {
					switch (itemType) {
						case ShoppingItemListFragment.MEAT_ITEM:
							singleShoppingItemLayout = meatPlanLinearLayout.getChildAt(singleItemIndex);
							break;
						case ShoppingItemListFragment.ALCOHOL_ITEM:
							singleShoppingItemLayout = alcoholPlanLinearLayout.getChildAt(singleItemIndex);
							break;
						case ShoppingItemListFragment.OTHERS_ITEM:
							singleShoppingItemLayout = othersPlanLinearLayout.getChildAt(singleItemIndex);
							break;
					}
					numberPickerButton = (Button) singleShoppingItemLayout.findViewById(R.id.btn_number_picker_item_plan);
					numPeople = Integer.parseInt(numberPickerButton.getText().toString());
					numPeople *= rate;
					Log.d(TAG, "result:" + numPeople);
					numberPickerButton.setText(String.valueOf(numPeople));
					mPlanModel.changeItemCount(itemType, mPlanModel.getItemName(itemType, singleItemIndex - FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN),
							mPlanModel.getItemUnitPrice(itemType, singleItemIndex - FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN), numPeople);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		totalMeatPrice.setText(castItemPriceToString(mPlanModel.getTotalItemCost(ShoppingItemListFragment.MEAT_ITEM)));
		totalAlcoholPrice.setText(castItemPriceToString(mPlanModel.getTotalItemCost(ShoppingItemListFragment.ALCOHOL_ITEM)));
		totalOthersPrice.setText(castItemPriceToString(mPlanModel.getTotalItemCost(ShoppingItemListFragment.OTHERS_ITEM)));
		totalPlanCost.setText(castItemPriceToString(mPlanModel.getPlanTotalCost()));
	}

	private void deleteAddedRoomInPlan(int viewIndex, LinearLayout roomPlanLinearLayout) {
		roomPlanLinearLayout.removeViewAt(viewIndex);

		if(roomPlanLinearLayout == this.roomPlanLinearLayout)
			mPlanModel.removeRoomData(viewIndex);
		else
			mPlanModel.removeDirectInputRoomData(viewIndex);
	}

	private void deleteAddedItemInPlan(int viewIndex, int itemType, LinearLayout itemPlanLinearLayout, TextView totalItemPrice) {

		itemPlanLinearLayout.removeViewAt(viewIndex);

		mPlanModel.removeItemData(itemType, viewIndex);

		// calculate the total cost for the added rooms after deletion of one room
		totalItemPrice.setText(castItemPriceToString(mPlanModel.getTotalItemCost(itemType)));

		// calculate the total cost for the whole plan
		totalPlanCost.setText(castItemPriceToString(mPlanModel.getPlanTotalCost()));
	}

	@Override
	public void onClickUndoButton(int toastCase, final int viewIndex) {
		switch(toastCase) {
			case UndoToastController.DELETE_ADDED_ROOM_CASE:
				planItemFadeIn(roomPlanLinearLayout.getChildAt(viewIndex));
				break;
			case UndoToastController.DELETE_DIRECTLY_INPUT_ROOM_CASE:
				planItemFadeIn(directInputRoomPlanLinearLayout.getChildAt(viewIndex));
				break;
			case UndoToastController.DELETE_ADDED_MEAT_CASE:
				planItemFadeIn(meatPlanLinearLayout.getChildAt(viewIndex));
				break;
			case UndoToastController.DELETE_ADDED_ALCOHOL_CASE:
				planItemFadeIn(alcoholPlanLinearLayout.getChildAt(viewIndex));
				break;
			case UndoToastController.DELETE_ADDED_OTHERS_CASE:
				planItemFadeIn(othersPlanLinearLayout.getChildAt(viewIndex));
				break;
			case UndoToastController.CLEAR_ADDED_ROOMS_CASE:
				for(int roomIndex = 0; roomIndex < mPlanModel.getRoomDataCount(); roomIndex++) {
					planItemFadeIn(roomPlanLinearLayout.getChildAt(roomIndex));
				}

				for(int directInputRoomIndex = 0;
					directInputRoomIndex < mPlanModel.getDirectInputRoomDataCount(); directInputRoomIndex++) {
					planItemFadeIn(directInputRoomPlanLinearLayout.getChildAt(directInputRoomIndex));
				}
				break;
			case UndoToastController.CLEAR_ADDED_MEATS_CASE:
				makeClearedItemsInPlanVisible(ShoppingItemListFragment.MEAT_ITEM, meatPlanLinearLayout);
				break;
			case UndoToastController.CLEAR_ADDED_ALCOHOLS_CASE:
				makeClearedItemsInPlanVisible(ShoppingItemListFragment.ALCOHOL_ITEM, alcoholPlanLinearLayout);
				break;
			case UndoToastController.CLEAR_ADDED_OTHERS_CASE:
				makeClearedItemsInPlanVisible(ShoppingItemListFragment.OTHERS_ITEM, othersPlanLinearLayout);
				break;
		}

	}

	@Override
	public void onTimePassed(int toastCase, int viewIndex) {
		switch(toastCase) {
			case UndoToastController.DELETE_ADDED_ROOM_CASE:
				deleteAddedRoomInPlan(viewIndex, roomPlanLinearLayout);
				break;
			case UndoToastController.DELETE_DIRECTLY_INPUT_ROOM_CASE:
				deleteAddedRoomInPlan(viewIndex, directInputRoomPlanLinearLayout);
				break;
			case UndoToastController.DELETE_ADDED_MEAT_CASE:
				deleteAddedItemInPlan(viewIndex, ShoppingItemListFragment.MEAT_ITEM, meatPlanLinearLayout, totalMeatPrice);
				break;
			case UndoToastController.DELETE_ADDED_ALCOHOL_CASE:
				deleteAddedItemInPlan(viewIndex, ShoppingItemListFragment.ALCOHOL_ITEM, alcoholPlanLinearLayout, totalAlcoholPrice);
				break;
			case UndoToastController.DELETE_ADDED_OTHERS_CASE:
				deleteAddedItemInPlan(viewIndex, ShoppingItemListFragment.OTHERS_ITEM, othersPlanLinearLayout, totalOthersPrice);
				break;
			case UndoToastController.CLEAR_ADDED_ROOMS_CASE:
				for(int roomIndex = 0; roomIndex < mPlanModel.getRoomDataCount(); roomIndex++)
					roomPlanLinearLayout.removeViewAt(0);

				for(int directInputRoomIndex = 0;
					directInputRoomIndex < mPlanModel.getDirectInputRoomDataCount(); directInputRoomIndex++)
					directInputRoomPlanLinearLayout.removeViewAt(0);

				mPlanModel.clearRoomData();

				totalRoomPrice.setText(castItemPriceToString(mPlanModel.getTotalRoomCost()));

				// calculate the total cost for the whole plan
				totalPlanCost.setText(castItemPriceToString(mPlanModel.getPlanTotalCost()));
				break;
			case UndoToastController.CLEAR_ADDED_MEATS_CASE:
				clearAddedItemsInPlan(ShoppingItemListFragment.MEAT_ITEM, meatPlanLinearLayout, totalMeatPrice);
				break;
			case UndoToastController.CLEAR_ADDED_ALCOHOLS_CASE:
				clearAddedItemsInPlan(ShoppingItemListFragment.ALCOHOL_ITEM, alcoholPlanLinearLayout, totalAlcoholPrice);
				break;
			case UndoToastController.CLEAR_ADDED_OTHERS_CASE:
				clearAddedItemsInPlan(ShoppingItemListFragment.OTHERS_ITEM, othersPlanLinearLayout, totalOthersPrice);
				break;
		}
	}

	private void makeClearedItemsInPlanVisible(int itemType, LinearLayout itemPlanLinearLayout) {
		for(int itemIndex = 0;
			itemIndex < mPlanModel.getItemDataCount(itemType);
			itemIndex++) {
			planItemFadeIn(itemPlanLinearLayout.getChildAt(itemIndex));
		}
	}

	private void clearAddedItemsInPlan(int itemType, LinearLayout itemPlanLinearLayout, TextView totalItemPrice) {
		for(int itemIndex = 0;
			itemIndex < mPlanModel.getItemDataCount(itemType);
			itemIndex++)
			itemPlanLinearLayout.removeViewAt(itemIndex);

		mPlanModel.clearItemData(itemType);

		totalItemPrice.setText(castItemPriceToString(mPlanModel.getTotalItemCost(itemType)));

		// calculate the total cost for the whole plan
		totalPlanCost.setText(castItemPriceToString(mPlanModel.getPlanTotalCost()));
	}

	private void planItemFadeOut(final View targetView) {
		targetView.animate().alpha(0).setListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {}

			@Override
			public void onAnimationEnd(Animator animation) {
				targetView.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {}

			@Override
			public void onAnimationRepeat(Animator animation) {}
		});
	}

	private void planItemFadeIn(final View targetView) {
		targetView.setVisibility(View.VISIBLE);
		targetView.animate().alpha(1).setListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {}

			@Override
			public void onAnimationEnd(Animator animation) {
				targetView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {}

			@Override
			public void onAnimationRepeat(Animator animation) {}
		});
	}

	/**
	 * @author In-Ho
	 *         AsyncTask for receiving data from the server.
	 *         Used AsyncTask to perform the task in the background
	 */
	private class DataRequestTask extends AsyncTask<String, Integer, String> {

		private int requestLabel;

		public DataRequestTask(int requestLabel) {
			this.requestLabel = requestLabel;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			startLoadingProgress();
		}

		@Override
		protected String doInBackground(String... urls) {
			try {
				Log.i(TAG, "URL: " + urls[0]);
				HttpClient mHttpClient = new DefaultHttpClient();
				HttpGet mHttpGet = new HttpGet(urls[0]);
				HttpConnectionParams.setConnectionTimeout(mHttpClient.getParams(), 5000);
				HttpResponse mHttpResponse = mHttpClient.execute(mHttpGet);
				HttpEntity resEntityGet = mHttpResponse.getEntity();

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
						// end of creation of the mTimelineFragment

						// commit the mTimelineFragment to the current view
						beginFragmentTransaction(mTimelineFragment, R.id.body_background);
						// end of commission
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case RESULT:
					mConditionDataForRequest = getUserInputData();
					// create the mResultFragment with the Interface ScrollTabHolder
					mResultFragment = ResultFragment.newInstance(jsonString, mConditionDataForRequest.getDate());
					// end of creation of the mTimelineFragment

					// commit the mResultFragment to the current view
					beginFragmentTransaction(mResultFragment, R.id.body_background);
					// end of commission
					break;
				case NETWORK_CONNECTION_FAILED:
					Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT);
					break;
			}

			endLoadingProgress();
		}
	}

	public void hideKeyboard(View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
