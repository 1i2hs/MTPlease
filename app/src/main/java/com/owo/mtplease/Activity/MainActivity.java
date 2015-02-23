package com.owo.mtplease.Activity;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.kakao.APIErrorResult;
import com.kakao.LogoutResponseCallback;
import com.kakao.UserManagement;
import com.owo.mtplease.ConditionDataForRequest;
import com.owo.mtplease.ImageLoadingTask;
import com.owo.mtplease.PlanModelController;
import com.owo.mtplease.R;
import com.owo.mtplease.RoomInfoModelController;
import com.owo.mtplease.ScrollTabHolder;
import com.owo.mtplease.ServerCommunicationManager;
import com.owo.mtplease.UndoToastController;
import com.owo.mtplease.fragment.AddItemToPlanDialogFragment;
import com.owo.mtplease.fragment.CalendarDialogFragment;
import com.owo.mtplease.fragment.ChangeItemNumberInPlanDialogFragment;
import com.owo.mtplease.fragment.GuideFragment;
import com.owo.mtplease.fragment.MyPageFragment;
import com.owo.mtplease.fragment.ResultFragment;
import com.owo.mtplease.fragment.ShoppingItemListFragment;
import com.owo.mtplease.fragment.SpecificInfoFragment;
import com.owo.mtplease.fragment.TimelineFragment;
import com.owo.mtplease.fragment.UserInfoModificationFragment;
import com.owo.mtplease.fragment.VersionCheckFragment;
import com.owo.mtplease.view.TypefaceLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

import notboringactionbar.AlphaForegroundColorSpan;


public class MainActivity extends ActionBarActivity implements ScrollTabHolder,
		TimelineFragment.OnTimelineFragmentListener, ResultFragment.OnResultFragmentListener,
		CalendarDialogFragment.OnDateConfirmedListener, SpecificInfoFragment.OnSpecificInfoFragmentListener,
		AddItemToPlanDialogFragment.OnAddItemToPlanDialogFragmentListener,
		ShoppingItemListFragment.OnShoppingItemListFragmentListener,
		ChangeItemNumberInPlanDialogFragment.OnChangeItemNumberInPlanDialogFragmentListener,
		UndoToastController.UndoToastListener,
		MyPageFragment.OnMyPageFragmentListener,
		UserInfoModificationFragment.OnUserInfoModificationFragmentListener,
		VersionCheckFragment.OnVersionCheckFragmentListener,
		GuideFragment.OnGuideFragmentListener {

	// String for application version
	private static final int APPLICATION_VERSION = 12;

	// Flags and Strings
	public static final int TIMELINE_FRAGMENT_VISIBLE = 1;
	public static final int RESULT_FRAGMENT_VISIBLE = 2;
	public static final int SPECIFIC_INFO_FRAGMENT_VISIBLE = 3;
	public static final int GUIDE_FRAGMENT_VISIBLE = 4;
	public static final int VERSION_FRAGMENT_VISIBLE = 5;
	public static final int SHOPPINGITEMLIST_FRAGMENT_VISIBLE = 4;
	private static final String TAG = "MainActivity";

	public static final int TIMELINE_PAGE_STATE = 1;
	public static final int RESULT_PAGE_STATE = 2;
	public static final int SPECIFIC_INFO_PAGE_STATE = 3;
	public static final int GUIDE_PAGE_STATE = 4;
	public static final int VERSION_PAGE_STATE = 5;
	public static final int SHOPPINGITEMLIST_PAGE_STATE = 6;

	//public static final int ACTIONBAR_BACKGROUND_COLOR = 0xFF38E4FF;
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
	private static final int INDEX_OF_CHEONGPYUNG = 1;
	private static final int INDEX_OF_GAPYUNG = 2;
	private static final int FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN = 0;
	private static final int CUSTOM_SHOPPING_ITEM_INPUT_MODE = 1;
	private static final int SHOPPING_ITEM_INPUT_MODE = 2;
	// End of the flags and strings

	// Fragments
	private FragmentManager mFragmentManager;
	private TimelineFragment mTimelineFragment = null;
	private ResultFragment mResultFragment = null;
	private VersionCheckFragment mVersionCheckFragment = null;
	private GuideFragment mGuideFragment = null;
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
	private FrameLayout splashScreenLayout;
	private ActionBar mActionBar;
	private TextView actionBarTitleTextView;
	private TextView actionBarSubtitleTextView;
	private ImageButton actionBarMenuButton;
	private SlidingMenu doubleSideSlidingMenu;
	private Button dateSelectButton;
	private Spinner regionSelectSpinner;
	private EditText numberOfPeopleSelectEditText;
	private ImageButton roomSearchButton;
	private TextView roomCountText;
	private LinearLayout subTabLinearLayout;
	private TextView querySubTabText;
	private Button reconnectServerButton;
	private FrameLayout loadingLayout;
	private Drawable loadingBackground;
	private ProgressBar loadingProgressBar;
	private View mHeader;
	private Button homeButton;
	private Button compareButton;
	private Button mypageButton;
	private Button settingButton;
	private Button helpButton;
	private Button logoutButton;
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
	private Typeface mTypeface;
	// End of User Interface Views

	// Model
	private PlanModelController mPlanModelController = null;
	// End of Model

	// Controller
	private UndoToastController mUndoToastController;
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
	// End of Controllers


	// Others
	private boolean isBackButtonPressed = false;
	private Calendar calendar = Calendar.getInstance();
	private String modifiedDate;
	private ConditionDataForRequest mConditionDataForRequest = null;
	private float clampValue;
	private int searchMode;
	private LinkedList<ConditionDataForRequest> conditionDataStack = null;
	private static int currentPage = -1;
	// End of others


	public static float clamp(float value, float max, float min) {
		return Math.max(Math.min(value, min), max);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {

			mFragmentManager = getSupportFragmentManager();
			int fragmentManagerBackStackSize = mFragmentManager.getBackStackEntryCount();
			for (int i = 0; i < fragmentManagerBackStackSize; i++) {
				mFragmentManager.popBackStack();
			}

			// configure font type of the activity
			mTypeface = TypefaceLoader.getInstance(getApplicationContext()).getTypeface();
			// end of the configuration

			// configure splash screen(will be deleted later)
			splashScreenLayout = (FrameLayout) findViewById(R.id.screen_splash);
			// end of the configuration

			// configure actionbar
			setActionBar();
			// end of the configuration of the actionbar

			// configure the SlidingMenu
			//setSlidingMenu();
			// end of the configuration of the SlidingMenu

			// configure the QueryHeader
			setQueryHeader();
			// end of the configuration of QueryHeader

			// configure the side menu buttons
			//setSideMenuButtons();
			// end of the configuration of the side menu buttons

			// configure the conditional query UIs
			setConditionalQueryInput();
			// end of the configuration of the conditional query UIs

			// configure the reconnect server button
			reconnectServerButton = (Button) findViewById(R.id.btn_reconnect_server);
			reconnectServerButton.setTypeface(mTypeface);
			reconnectServerButton.setVisibility(View.GONE);
			reconnectServerButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(currentPage == TIMELINE_PAGE_STATE) {
						Log.d(TAG, "load timeline");
						loadTimelineFragment();
					}/*
					else if(currentPage == RESULT_PAGE_STATE) {
						Log.d(TAG, "load result page");
						loadResultFragment();
					}*/
				}
			});
			// end of the configuration

			// configure the subtab(tab under the query header that displays conditional query which users input)
			setSubTab();
			// end of the configuration of the subtab

			// configure the Plan UIs
			//setPlan();
			// end of the configuration of the Plan UIs

			// configure the loading animation
			setLoadingAnimation();
			// end of the configuration of the loading animation

			// configure mode of the search
			searchMode = CONDITION_SEARCH_MODE;
			// end of the configuration of the mode of the search

			// set ServerCommunicationManager
			ServerCommunicationManager.getInstance(this).initImageLoader("cache", 1024 * 1024 * 20, Bitmap.CompressFormat.JPEG, 85);
			// end of the setting

			loadTimelineFragment();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(mTypeface == null)
			mTypeface = TypefaceLoader.getInstance(getApplicationContext()).getTypeface();

		if(conditionDataStack == null)
			conditionDataStack = new LinkedList();
	}

	private void setActionBar() {
		mActionBar = getSupportActionBar();

		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

		View actionBarView = getLayoutInflater().inflate(R.layout.actionbar, null);

		actionBarTitleTextView = (TextView) actionBarView.findViewById(R.id.textView_title_actionBar);
		actionBarTitleTextView.setTypeface(mTypeface);
		actionBarTitleTextView.setText(getResources().getString(R.string.actionbar_title));

		actionBarSubtitleTextView = (TextView) actionBarView.findViewById(R.id.textView_subtitle_actionBar);
		actionBarSubtitleTextView.setTypeface(mTypeface);

		mActionBar.setCustomView(actionBarView/*, params*/);


		changeActionBarStyle(getResources().getColor(R.color.mtplease_actionbar_color), getResources().getString(R.string.actionbar_title), null);

		mActionBar.hide();
	}

	private void setActionBarTitle(String title, String subtitle) {
		actionBarTitleTextView.setText(title);

		LinearLayout.LayoutParams params;
		if(subtitle != null) {
			params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
		} else {
			params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
		}

		actionBarSubtitleTextView.setLayoutParams(params);
		actionBarSubtitleTextView.setText(subtitle);
	}

	private void setSlidingMenu() {
		doubleSideSlidingMenu = new SlidingMenu(this);
		doubleSideSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		doubleSideSlidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		doubleSideSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		doubleSideSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		doubleSideSlidingMenu.setSecondaryShadowDrawable(R.drawable.shadow_right);
		doubleSideSlidingMenu.setShadowDrawable(R.drawable.shadow);
		doubleSideSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		doubleSideSlidingMenu.setFadeDegree(0.70f);
		doubleSideSlidingMenu.setMenu(R.layout.menu_side);
		doubleSideSlidingMenu.setSecondaryMenu(R.layout.plan_side);
	}

	private void setPlan() {

		// instantiate mPlanModel to store plan data inside it.
		mPlanModelController = new PlanModelController();
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
				if(mPlanModelController.getRoomDataCount() > 0) {
					for(int roomIndex = 0; roomIndex < mPlanModelController.getRoomDataCount(); roomIndex++)
						planItemFadeOut(roomPlanLinearLayout.getChildAt(roomIndex));

					for(int directInputRoomIndex = 0;
						directInputRoomIndex < mPlanModelController.getDirectInputRoomDataCount(); directInputRoomIndex++)
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
				if(mPlanModelController.getItemDataCount(ShoppingItemListFragment.MEAT_ITEM) > 0) {
					for(int meatIndex = 0;
						meatIndex < mPlanModelController.getItemDataCount(ShoppingItemListFragment.MEAT_ITEM);
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
				if(mPlanModelController.getItemDataCount(ShoppingItemListFragment.ALCOHOL_ITEM) > 0) {
					for(int alcoholIndex = 0;
						alcoholIndex < mPlanModelController.getItemDataCount(ShoppingItemListFragment.ALCOHOL_ITEM);
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
				if(mPlanModelController.getItemDataCount(ShoppingItemListFragment.OTHERS_ITEM) > 0) {
					for(int othersIndex = 0;
						othersIndex < mPlanModelController.getItemDataCount(ShoppingItemListFragment.OTHERS_ITEM);
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
		if(mShoppingItemListFragment == null) {
			ShoppingItemListFragment shoppingItemListFragment = ShoppingItemListFragment.newInstance(shoppingItemType);
			// commit the ShoppingItemListFragment to the current view
			beginFragmentTransaction(shoppingItemListFragment, R.id.body_background);
			// end of the comission
		} else {
			mShoppingItemListFragment.switchRecyclerViewAdpater(shoppingItemType);
		}
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
		homeButton = (Button) findViewById(R.id.btn_menu_home);
		homeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		//compareButton = (TextView) findViewById(R.id.btn_menu_compare);

		mypageButton = (Button) findViewById(R.id.btn_menu_mypage);
		mypageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyPageFragment myPageFragment = MyPageFragment.newInstance();
				beginFragmentTransaction(myPageFragment, R.id.body_background);

				doubleSideSlidingMenu.toggle();
			}
		});

		settingButton = (Button) findViewById(R.id.btn_menu_setting);
		settingButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent settingsIntent = new Intent(v.getContext(), SettingsActivity.class);
				startActivity(settingsIntent);
			}
		});

		helpButton = (Button)findViewById(R.id.btn_menu_help);
		helpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		logoutButton = (Button) findViewById(R.id.btn_menu_logout);
		logoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UserManagement.requestLogout(new LogoutResponseCallback() {
					@Override
					protected void onSuccess(final long userId) {
						Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
						startActivity(loginIntent);
						finish();
					}

					@Override
					protected void onFailure(final APIErrorResult apiErrorResult) {
						Log.d(TAG, apiErrorResult.getErrorMessage());
						Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
						startActivity(loginIntent);
						finish();
					}
				});
			}
		});
	}

	private void setConditionalQueryInput() {

		TextView weTextView = (TextView) findViewById(R.id.textView_we);
		weTextView.setTypeface(mTypeface);

		TextView postpositionTextView = (TextView) findViewById(R.id.textView_postposition);
		postpositionTextView.setTypeface(mTypeface);

		TextView withTextView = (TextView) findViewById(R.id.textView_with);
		withTextView.setTypeface(mTypeface);

		TextView goMTTextView = (TextView) findViewById(R.id.textView_go_MT);
		goMTTextView.setTypeface(mTypeface);

		dateSelectButton = (Button) findViewById(R.id.btn_select_date);
		modifiedDate = calendar.get(Calendar.YEAR) + "년 " + (calendar.get(Calendar.MONTH) + 1)
				+ "월 " + calendar.get(Calendar.DATE) + "일";
		dateSelectButton.setText(modifiedDate);
		dateSelectButton.setTypeface(mTypeface);
		dateSelectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callCalendarDialogFragment(CALL_FROM_CONDITIONAL_QUERY);
			}
		});

		regionSelectSpinner = (Spinner) findViewById(R.id.spinner_select_region);
		/*ArrayAdapter<CharSequence> regionSpinnerAdapter =
				ArrayAdapter.createFromResource(this, R.array.array_region, R.layout.spinner_region);
		regionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
		ArrayList<String> regionNameList = new ArrayList<String>();
		regionNameList.add(getResources().getString(R.string.daesungri));
		SpinnerArrayAdapter spinnerArrayAdapter = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, regionNameList);
		regionSelectSpinner.setAdapter(spinnerArrayAdapter);

		numberOfPeopleSelectEditText = (EditText) findViewById(R.id.editText_input_number_people);
		numberOfPeopleSelectEditText.setTypeface(mTypeface);
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
				loadResultFragment();
			}
		});

		// configure the number of the room that our service have inside our database
		roomCountText = (TextView) findViewById(R.id.text_room_count);
		roomCountText.setTypeface(mTypeface);
		// end of the configuration
	}

	private void setSubTab() {
		subTabLinearLayout = (LinearLayout) findViewById(R.id.layout_subtab);
		querySubTabText = (TextView) findViewById(R.id.textView_query);
		querySubTabText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHeader.setTranslationY(0);
				subTabLinearLayout.setAlpha(0);
			}
		});
	}

	private void setLoadingAnimation() {
		loadingLayout = (FrameLayout) findViewById(R.id.background_loading);
		loadingBackground = loadingLayout.getBackground();
		loadingBackground.setAlpha(0);

		loadingProgressBar = (ProgressBar) findViewById(R.id.progressBar_condition_search);
		loadingProgressBar.setVisibility(View.GONE);
	}

	private void loadTimelineFragment() {
		// get timeline data from the server and create the mTimelineFragment
		/*MTPleaseJsonObjectRequest getRequest = new MTPleaseJsonObjectRequest(Request.Method.GET, MTPLEASE_URL, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				splashScreenLayout.setVisibility(View.GONE);
				mActionBar.show();

				endLoadingProgress();
				// create the mTimelineFragment with the Interface ScrollTabHolder
				try {
					Log.i(TAG, response.toString());
					// redundant to code. needs to be refactored
					JSONObject mJSONObject = response.getJSONObject("main");

					roomCountText.setText(mJSONObject.getString("roomCount") + "개의 방");

					TimelineFragment timelineFragment = TimelineFragment.newInstance(mJSONObject.toString());
					// end of creation of the mTimelineFragment

					// commit the timelineFragment to the current view
					beginFragmentTransaction(timelineFragment, R.id.body_background);
					// end of commission
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				splashScreenLayout.setVisibility(View.GONE);
				mActionBar.show();

				subTabLinearLayout.setVisibility(View.INVISIBLE);
				roomCountText.setVisibility(View.VISIBLE);

				endLoadingProgress();
				Log.d(TAG, error.toString());
				Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
			}
		});

		getRequest.setContext(this);*/

		JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, MTPLEASE_URL, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				splashScreenLayout.setVisibility(View.GONE);
				mActionBar.show();
				reconnectServerButton.setVisibility(View.GONE);
				endLoadingProgress();

				currentPage = TIMELINE_PAGE_STATE;
				// create the mTimelineFragment with the Interface ScrollTabHolder
				try {
					Log.i(TAG, response.toString());
					// redundant to code. needs to be refactored
					JSONObject mJSONObject = response.getJSONObject("main");

					roomCountText.setText(mJSONObject.getString("roomCount") + "개의 방이 함께하고 있습니다.");

					TimelineFragment timelineFragment = TimelineFragment.newInstance(mJSONObject.toString());
					// end of creation of the mTimelineFragment

					// commit the timelineFragment to the current view
					beginFragmentTransaction(timelineFragment, R.id.body_background);
					// end of commission

					// check version of the application
					checkApplicationVersion();
					// end of checking
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				splashScreenLayout.setVisibility(View.GONE);
				mActionBar.show();
				reconnectServerButton.setVisibility(View.VISIBLE);
				subTabLinearLayout.setVisibility(View.INVISIBLE);
				roomCountText.setVisibility(View.VISIBLE);

				endLoadingProgress();

				currentPage = TIMELINE_PAGE_STATE;
				Log.d(TAG, error.toString());
				Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
			}
		});

		startLoadingProgress();
		ServerCommunicationManager.getInstance(this).addToRequestQueue(getRequest);
		// end of getting the data from the server and the creation of the fragment
	}

	private void loadResultFragment() {
		try {
			mConditionDataForRequest = getUserInputData();

			numberOfPeopleSelectEditText.clearFocus();
					/*if (mConditionDataForRequest.getFlag() == CONDITION_SEARCH_MODE) {
						setAutoCompleteBasicInfoPlan();
					}*/

			//new DataRequestTask(RESULT).execute(mConditionDataForRequest.makeHttpGetURL());

					/*MTPleaseJsonObjectRequest getRequest = new MTPleaseJsonObjectRequest(Request.Method.GET, mConditionDataForRequest.makeHttpGetURL(), null, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							endLoadingProgress();
							Log.i(TAG, response.toString());
							mConditionDataForRequest = getUserInputData();
							// create the resultFragment with the Interface ScrollTabHolder
							ResultFragment resultFragment = ResultFragment.newInstance(response.toString(), mConditionDataForRequest.getDate());
							// end of creation of the mTimelineFragment

							// commit the resultFragment to the current view
							beginFragmentTransaction(resultFragment, R.id.body_background);
							// end of commission

							conditionDataStack.push(mConditionDataForRequest);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Log.d(TAG, error.toString());
							endLoadingProgress();
							Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
						}
					});
					getRequest.setContext(v.getContext());
					*/

			JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, mConditionDataForRequest.makeHttpGetURL(), null, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					reconnectServerButton.setVisibility(View.GONE);
					Log.i(TAG, response.toString());
					endLoadingProgress();

					currentPage = RESULT_PAGE_STATE;

					mConditionDataForRequest = getUserInputData();
					// create the resultFragment with the Interface ScrollTabHolder
					ResultFragment resultFragment = ResultFragment.newInstance(response.toString(), mConditionDataForRequest.getDate());
					// end of creation of the mTimelineFragment

					// commit the resultFragment to the current view
					beginFragmentTransaction(resultFragment, R.id.body_background);
					// end of commission

					conditionDataStack.push(mConditionDataForRequest);
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					reconnectServerButton.setVisibility(View.VISIBLE);
					Log.d(TAG, error.toString());
					endLoadingProgress();

					currentPage = RESULT_PAGE_STATE;
					Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
				}
			});

			startLoadingProgress();
			ServerCommunicationManager.getInstance(this).addToRequestQueue(getRequest);

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

	private void checkApplicationVersion() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		int appVersion = sharedPreferences.getInt(getResources().getString(R.string.pref_app_ver), 0);

		Log.d(TAG, "Application Version Check: " + appVersion);
		if(appVersion < APPLICATION_VERSION) {
			editor.putInt(getResources().getString(R.string.pref_app_ver), APPLICATION_VERSION);
			editor.commit();
			GuideFragment guideFragment = GuideFragment.newInstance();
			beginFragmentTransaction(guideFragment, R.id.body_background);
		} else {
			return;
		}
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

		switch(id) {
			case R.id.action_version_check:
				VersionCheckFragment versionCheckFragment = VersionCheckFragment.newInstance();
				beginFragmentTransaction(versionCheckFragment, R.id.body_background);
				break;
			case R.id.action_help:
				GuideFragment guideFragment = GuideFragment.newInstance();
				beginFragmentTransaction(guideFragment, R.id.body_background);
				break;
			case R.id.action_terminate:
				finish();
				break;
		}
		/*if (id == R.id.action_settings) {
			Log.i("MainActivity", "action_settings");
			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingsIntent);
			return false;
		}*/

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateResultFragmentView(boolean noResults, int numRoom) {

		currentPage = RESULT_PAGE_STATE;

		mHeader.setVisibility(View.VISIBLE);

		subTabLinearLayout.setVisibility(View.VISIBLE);

		/*if(!noResults && !isBackButtonPressed) {
			mHeader.setTranslationY(-mMinHeaderHeightForResultFragment);
			subTabLinearLayout.setAlpha(1);
			subTabLinearLayout.setBackgroundColor(getResources().getColor(R.color.mtplease_subtab_background_color));
		} else {
			mHeader.setTranslationY(0);
			subTabLinearLayout.setAlpha(0);
		}*/
		mHeader.setTranslationY(-mMinHeaderHeightForResultFragment);
		subTabLinearLayout.setAlpha(1);
		subTabLinearLayout.setBackgroundColor(getResources().getColor(R.color.mtplease_subtab_background_color));

		// configure actionbar for result page
		if (noResults) {
			changeActionBarStyle(getResources().getColor(R.color.mtplease_actionbar_color), getResources().getString(R.string.results), getResources().getString(R.string.no_results));
		} else {
			changeActionBarStyle(getResources().getColor(R.color.mtplease_actionbar_color), getResources().getString(R.string.results), String.valueOf(numRoom));
		}
		// end of the configuration

		// configure subtab for result page
		if(isBackButtonPressed) {
			Log.d(TAG, "back button clicked");
			mConditionDataForRequest = conditionDataStack.peekLast();
			Log.d(TAG, mConditionDataForRequest.getPeople() + "people");
		} else {
			Log.d(TAG, "back button not clicked");
			mConditionDataForRequest = getUserInputData();
		}

		String queryString = getResources().getString(R.string.we) + " ";
		queryString += mConditionDataForRequest.getDateWrittenLang() + getResources().getString(R.string.postposition_1);
		dateSelectButton.setText(mConditionDataForRequest.getDateWrittenLang());
		int regionCode = mConditionDataForRequest.getRegion();
		switch (regionCode) {
			case 1:
				queryString += " " + getResources().getString(R.string.daesungri);
				regionSelectSpinner.setSelection(0);
				break;
			case 2:
				queryString += " " + getResources().getString(R.string.cheongpyung);
				regionSelectSpinner.setSelection(1);
				break;
			case 3:
				queryString += " " + getResources().getString(R.string.gapyung);
				regionSelectSpinner.setSelection(2);
				break;
		}
		queryString += " " + mConditionDataForRequest.getPeople() + getResources().getString(R.string.postposition_2);
		numberOfPeopleSelectEditText.setText(String.valueOf(mConditionDataForRequest.getPeople()));

		queryString += " " + getResources().getString(R.string.go_MT);
		querySubTabText.setText(queryString);
		// end of the configuration

		Log.d(TAG, isBackButtonPressed + "");
	}

	@Override
	public void onDestroyResultFragmentView() {
		if(isBackButtonPressed)
			conditionDataStack.pop();
		mHeader.setVisibility(View.GONE);
	}

	@Override
	public void onPreLoadSpecificInfoFragment() {
		startLoadingProgress();
	}

	@Override
	public void onLoadSpecificInfoFragment(RoomInfoModelController roomInfoModelController, JSONArray roomArray) {
		SpecificInfoFragment specificInfoFragment = SpecificInfoFragment.newInstance(roomInfoModelController, roomArray.length());

		// commit the SpecificInfoFragment to the current view
		beginFragmentTransaction(specificInfoFragment, R.id.body_background);
		// end of commission
	}

	@Override
	public void onPostLoadSpecificInfoFragment() {
		endLoadingProgress();
	}

	@Override
	public void onResumeResultFragmentView(LinearLayoutManager layoutManager, int firstVisibleViewPosition, int defaultPosition) {
		Log.d(TAG, "isBackButtonPressed: " + isBackButtonPressed);
		if(isBackButtonPressed) {
			Log.d(TAG, "firstVisibleItemPosition: "+ firstVisibleViewPosition);
			if(firstVisibleViewPosition == 0) {
				mHeader.setTranslationY(0);
				subTabLinearLayout.setAlpha(0);
			}
		} else {
			layoutManager.scrollToPosition(defaultPosition);
		}
		isBackButtonPressed = false;
	}

	@Override
	public void onCreateTimelineFragmentView() {

		currentPage = TIMELINE_PAGE_STATE;

		conditionDataStack.clear();

		mHeader.setVisibility(View.VISIBLE);

		subTabLinearLayout.setVisibility(View.INVISIBLE);
		roomCountText.setVisibility(View.VISIBLE);

		changeActionBarStyle(getResources().getColor(R.color.mtplease_actionbar_color), getResources().getString(R.string.actionbar_title), null);

		mHeader.setTranslationY(0);

		isBackButtonPressed = false;
	}

	@Override
	public void onDestroyTimelineFragmentView() {
		roomCountText.setVisibility(View.GONE);
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
				return;
			case RESULT_FRAGMENT_VISIBLE:
				clampValue = changeHeaderTranslation(scrollY, mMinHeaderHeightForResultFragment);
				subTabLinearLayout.setAlpha(clampValue);
				//subTabBackgroundColor.setAlpha((int) (clampValue * 255));
				break;
			/*case SPECIFIC_INFO_FRAGMENT_VISIBLE:
				ratio = (float) getSupportActionBar().getHeight() / (float) (scrollY + 1);
				clampValue = clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F);
				break;*/
			case SPECIFIC_INFO_FRAGMENT_VISIBLE:
			case SHOPPINGITEMLIST_FRAGMENT_VISIBLE:
			default:
				return;
		}

		//setTitleAlpha(clampValue, visibleFragment);
	}

	private float changeHeaderTranslation(int scrollY, int minHeaderHeight) {
		mMinHeaderTranslation = -minHeaderHeight/* + getSupportActionBar().getHeight()*/;

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
				//mActionBar.setTitle(mSpannableStringAppTitle);
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

		String regionName = (String) regionSelectSpinner.getSelectedItem();

		if(regionName.equals(getResources().getString(R.string.daesungri)))
			conditionDataForRequest.setRegion(INDEX_OF_DAESUNGRI + 1);
		else if(regionName.equals(getResources().getString(R.string.cheongpyung)))
			conditionDataForRequest.setRegion(INDEX_OF_CHEONGPYUNG + 1);
		else
			conditionDataForRequest.setRegion(INDEX_OF_GAPYUNG + 1);


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
		mHeader.setVisibility(View.GONE);

		currentPage = SPECIFIC_INFO_PAGE_STATE;

		// configure actionbar for specific info page
		changeActionBarStyle(getResources().getColor(R.color.mtplease_actionbar_color), roomName, pensionName);
		// end of the configuration
		// **********************actionbar button issue...........

		isBackButtonPressed = false;
	}

	@Override
	public void onDestroySpecificInfoFragmentView() {
		mSpecificInfoFragment = null;
	}

	@Override
	public void onClickAddRoomToPlanButton(final RoomInfoModelController roomInfoModelController) {

		// check rather room has been added already in the plan or not
		if(mPlanModelController.isRoomAddedAlready(roomInfoModelController.getPen_id(),
				roomInfoModelController.getRoom_name(), roomInfoModelController.getRoom_cost())) {
			Toast.makeText(this, R.string.room_already_added_before, Toast.LENGTH_LONG).show();
			return;
		}

		int roomDataCount = mPlanModelController.getRoomDataCount();
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

				mUndoToastController.showUndoBar(roomInfoModelController.getRoom_name() + " " + getResources().getString(R.string.deleted),
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
				execute(roomInfoModelController.getRoomThumbnailImageURL());
		// end of getting and setting of the thumbnail image

		// set a room name of the added room view
		TextView roomNamePlanTextView = (TextView) roomPlanView.findViewById(R.id.textView_name_room_plan);
		roomNamePlanTextView.setText(roomInfoModelController.getRoom_name());
		// end of the setting

		// set a pension name of the added room view
		TextView pensionNamePlanTextView =
				(TextView) roomPlanView.findViewById(R.id.textView_name_pension_and_region_plan);
		pensionNamePlanTextView.setText(roomInfoModelController.getPen_name() + " / " + roomInfoModelController.getPen_region());
		// end of the setting

		// set standard and maximum number of people allowed of the added room view
		TextView numberPeopleStdMaxPlanTextView =
				(TextView) roomPlanView.findViewById(R.id.textView_number_people_std_max_plan);
		numberPeopleStdMaxPlanTextView.setText(getResources().getString(R.string.standard)
				+ roomInfoModelController.getRoom_std_people() + " / " + getResources().getString(R.string.max)
				+ roomInfoModelController.getRoom_max_people());
		// end of the setting

		// Options goes here!!!!!!!!!

		// set a room price of the added room view
		TextView roomPricePlanTextView = (TextView) roomPlanView.findViewById(R.id.textView_price_room_plan);
		int roomPrice = roomInfoModelController.getRoom_cost();
		if (roomPrice == 0) {
			roomPricePlanTextView.setText(getResources().getString(R.string.telephone_inquiry));
		} else {
			roomPricePlanTextView.setText(castItemPriceToString(roomPrice));
		}

		// store room data in the mPlanModel
		mPlanModelController.addRoomData(roomInfoModelController.getPen_id(),
				roomInfoModelController.getRoom_name(), roomInfoModelController.getRoom_cost());

		// calculate the total cost for the added rooms
		totalRoomPrice.setText(castItemPriceToString(mPlanModelController.getTotalRoomCost()));

		// calculate the total cost for the whole plan
		totalPlanCost.setText(castItemPriceToString(mPlanModelController.getPlanTotalCost()));

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

		final int directInputRoomDataCount = mPlanModelController.getRoomDataCount();
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
		mPlanModelController.addDirectInputRoomData(directInputRoomPrice);

		// calculate the total cost for the added rooms
		totalRoomPrice.setText(castItemPriceToString(mPlanModelController.getTotalRoomCost()));

		// calculate the total cost for the whole plan
		totalPlanCost.setText(castItemPriceToString(mPlanModelController.getPlanTotalCost()));

		// notify room is added to the plan to the user
		Toast.makeText(this, R.string.added_to_plan, Toast.LENGTH_LONG).show();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			/*if(doubleSideSlidingMenu.isSecondaryMenuShowing()) {
				doubleSideSlidingMenu.toggle();
				return false;
			}

			if(doubleSideSlidingMenu.isMenuShowing()) {
				doubleSideSlidingMenu.toggle();
				return false;
			}

			if(mShoppingItemListFragment != null && mShoppingItemListFragment.isVisible()) {
				Log.d(TAG,"Visible");
				Log.d(TAG,mFragmentManager.getBackStackEntryCount() + "ea");
				doubleSideSlidingMenu.showSecondaryMenu(true);
			}*/

			if(currentPage == TIMELINE_PAGE_STATE) {
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

		//mActionBar.setTitle(titleText);
		//mActionBar.setSubtitle(subtitleText);
		setActionBarTitle(titleText, subtitleText);
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

		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

		if(targetFragment instanceof TimelineFragment) {
			fragmentTransaction.replace(containerViewId, targetFragment);
		} else if(targetFragment instanceof VersionCheckFragment || targetFragment instanceof GuideFragment) {
			fragmentTransaction.add(containerViewId, targetFragment);
			fragmentTransaction.addToBackStack(null);
		} else {
			fragmentTransaction.replace(containerViewId, targetFragment);
			fragmentTransaction.addToBackStack(null);
		}

		if(targetFragment instanceof TimelineFragment) {
			currentPage = TIMELINE_PAGE_STATE;
			mTimelineFragment = (TimelineFragment) targetFragment;
		}
		else if(targetFragment instanceof VersionCheckFragment) {
			currentPage = VERSION_PAGE_STATE;
			mVersionCheckFragment = (VersionCheckFragment) targetFragment;
		}
		else if(targetFragment instanceof  GuideFragment) {
			currentPage = GUIDE_PAGE_STATE;
			mGuideFragment = (GuideFragment) targetFragment;
		}
		else if(targetFragment instanceof ResultFragment) {
			currentPage = RESULT_PAGE_STATE;
			mResultFragment = (ResultFragment) targetFragment;
		}
		else if(targetFragment instanceof SpecificInfoFragment) {
			currentPage = SPECIFIC_INFO_PAGE_STATE;
			mSpecificInfoFragment = (SpecificInfoFragment) targetFragment;
		}
		else if(targetFragment instanceof ShoppingItemListFragment) {
			currentPage = SHOPPINGITEMLIST_PAGE_STATE;
			mShoppingItemListFragment = (ShoppingItemListFragment) targetFragment;
		}

		fragmentTransaction.commit();
	}

	@Override
	public void onCreateShoppingItemListFragmentView(int itemType, int numRoom) {
		mHeader.setVisibility(View.GONE);
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

		changeActionBarStyle(getResources().getColor(R.color.mtplease_actionbar_color), itemTypeString, numRoom + getResources().getString(R.string.n_results));
		// end of the configuration

		isBackButtonPressed = false;
	}

	@Override
	public void onDestroyShoppingItemListFragmentView() {
		mShoppingItemListFragment = null;
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
		if(mPlanModelController.isItemAddedAlready(itemType, itemName, itemUnitPrice)) {
			Toast.makeText(this, R.string.item_already_added_before, Toast.LENGTH_LONG).show();
			return;
		}

		int itemDataCount = mPlanModelController.getItemDataCount(itemType);
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
										mPlanModelController.getSingleItemCount(itemType, itemName, itemUnitPrice), itemCountUnit);
				changeItemNumberInPlanDialogFragment.show(mFragmentManager, "changeItemNumberInPlanDialogFragment");
			}
		});

		// store item data in the mPlanModel
		mPlanModelController.addItemData(itemType, itemName, itemUnitPrice, itemCount);

		// calculate the total cost for the added rooms
		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				totalMeatPrice.setText(castItemPriceToString(mPlanModelController.getTotalItemCost(itemType)));
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				totalAlcoholPrice.setText(castItemPriceToString(mPlanModelController.getTotalItemCost(itemType)));
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				totalOthersPrice.setText(castItemPriceToString(mPlanModelController.getTotalItemCost(itemType)));
				break;
		}

		// calculate the total cost for the whole plan
		totalPlanCost.setText(castItemPriceToString(mPlanModelController.getPlanTotalCost()));

		// notify room is added to the plan to the user
		Toast.makeText(this, R.string.added_to_plan, Toast.LENGTH_SHORT).show();

		// show plan
		doubleSideSlidingMenu.showSecondaryMenu(true);

	}

	@Override
	public void onClickChangeButton(int itemType, String itemName, int itemUnitPrice, int newItemCount) {
		tempItemTotalPricePlanTextView.setText(castItemPriceToString(itemUnitPrice * newItemCount));
		tempNumberPickerCallButton.setText(String.valueOf(newItemCount));

		mPlanModelController.changeItemCount(itemType, itemName, itemUnitPrice, newItemCount);

		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				totalMeatPrice.setText(castItemPriceToString(mPlanModelController.getTotalItemCost(itemType)));
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				totalAlcoholPrice.setText(castItemPriceToString(mPlanModelController.getTotalItemCost(itemType)));
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				totalOthersPrice.setText(castItemPriceToString(mPlanModelController.getTotalItemCost(itemType)));
				break;
		}

		// calculate the total cost for the whole plan
		totalPlanCost.setText(castItemPriceToString(mPlanModelController.getPlanTotalCost()));
	}

	/**
	 * apply rate calculated by division of newly input number of people with previous number input
	 * to the number of shopping items already selected in the plan.
	 *
	 * @param oldNumPeople   number of people input before
	 * @param newNumPeople   number of people just input
	 */
	private void applyNumberOfPeopleChangeToItems(int oldNumPeople, int newNumPeople) {
		float rate = newNumPeople / oldNumPeople;

		Log.d(TAG, "rate:" + rate);

		for(int itemType = 1; itemType < 4; itemType++) {
			for (int singleItemIndex = FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN;
				 singleItemIndex < FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN + mPlanModelController.getItemDataCount(itemType);
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
					mPlanModelController.changeItemCount(itemType, mPlanModelController.getItemName(itemType, singleItemIndex - FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN),
							mPlanModelController.getItemUnitPrice(itemType, singleItemIndex - FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN), numPeople);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		totalMeatPrice.setText(castItemPriceToString(mPlanModelController.getTotalItemCost(ShoppingItemListFragment.MEAT_ITEM)));
		totalAlcoholPrice.setText(castItemPriceToString(mPlanModelController.getTotalItemCost(ShoppingItemListFragment.ALCOHOL_ITEM)));
		totalOthersPrice.setText(castItemPriceToString(mPlanModelController.getTotalItemCost(ShoppingItemListFragment.OTHERS_ITEM)));
		totalPlanCost.setText(castItemPriceToString(mPlanModelController.getPlanTotalCost()));
	}

	private void deleteAddedRoomInPlan(int viewIndex, LinearLayout roomPlanLinearLayout) {
		roomPlanLinearLayout.removeViewAt(viewIndex);

		if(roomPlanLinearLayout == this.roomPlanLinearLayout)
			mPlanModelController.removeRoomData(viewIndex);
		else
			mPlanModelController.removeDirectInputRoomData(viewIndex);
	}

	private void deleteAddedItemInPlan(int viewIndex, int itemType, LinearLayout itemPlanLinearLayout, TextView totalItemPrice) {

		itemPlanLinearLayout.removeViewAt(viewIndex);

		mPlanModelController.removeItemData(itemType, viewIndex);

		// calculate the total cost for the added rooms after deletion of one room
		totalItemPrice.setText(castItemPriceToString(mPlanModelController.getTotalItemCost(itemType)));

		// calculate the total cost for the whole plan
		totalPlanCost.setText(castItemPriceToString(mPlanModelController.getPlanTotalCost()));
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
				for(int roomIndex = 0; roomIndex < mPlanModelController.getRoomDataCount(); roomIndex++) {
					planItemFadeIn(roomPlanLinearLayout.getChildAt(roomIndex));
				}

				for(int directInputRoomIndex = 0;
					directInputRoomIndex < mPlanModelController.getDirectInputRoomDataCount(); directInputRoomIndex++) {
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
				for(int roomIndex = 0; roomIndex < mPlanModelController.getRoomDataCount(); roomIndex++)
					roomPlanLinearLayout.removeViewAt(0);

				for(int directInputRoomIndex = 0;
					directInputRoomIndex < mPlanModelController.getDirectInputRoomDataCount(); directInputRoomIndex++)
					directInputRoomPlanLinearLayout.removeViewAt(0);

				mPlanModelController.clearRoomData();

				totalRoomPrice.setText(castItemPriceToString(mPlanModelController.getTotalRoomCost()));

				// calculate the total cost for the whole plan
				totalPlanCost.setText(castItemPriceToString(mPlanModelController.getPlanTotalCost()));
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
			itemIndex < mPlanModelController.getItemDataCount(itemType);
			itemIndex++) {
			planItemFadeIn(itemPlanLinearLayout.getChildAt(itemIndex));
		}
	}

	private void clearAddedItemsInPlan(int itemType, LinearLayout itemPlanLinearLayout, TextView totalItemPrice) {
		for(int itemIndex = 0;
			itemIndex < mPlanModelController.getItemDataCount(itemType);
			itemIndex++)
			itemPlanLinearLayout.removeViewAt(itemIndex);

		mPlanModelController.clearItemData(itemType);

		totalItemPrice.setText(castItemPriceToString(mPlanModelController.getTotalItemCost(itemType)));

		// calculate the total cost for the whole plan
		totalPlanCost.setText(castItemPriceToString(mPlanModelController.getPlanTotalCost()));
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

	public void hideKeyboard(View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	@Override
	public void onClickModifyUserInfoIcon() {
		UserInfoModificationFragment userInfoModificationFragment = UserInfoModificationFragment.newInstance();

		beginFragmentTransaction(userInfoModificationFragment, R.id.body_background);
	}

	@Override
	public void onCreateMyPageFragmentView() {
		mHeader.setVisibility(View.GONE);
		// configure actionbar for my page
		changeActionBarStyle(getResources().getColor(R.color.mtplease_actionbar_color), getResources().getString(R.string.my_page), null);
		// end of the configuration
		// **********************actionbar button issue...........

		isBackButtonPressed = false;
	}

	@Override
	public void onDestroyMyPageFragmentView() {

	}

	@Override
	public void onCreateUserInfoModificationFragmentView() {
		mHeader.setVisibility(View.GONE);
		// configure actionbar for user info modification page
		changeActionBarStyle(getResources().getColor(R.color.mtplease_actionbar_color), getResources().getString(R.string.user_info_modification), null);
		// end of the configuration
		// **********************actionbar button issue...........

		isBackButtonPressed = false;
	}

	@Override
	public void onDestroyUserInfoModificationFragmentView() {

	}

	@Override
	public void onClickModifyUserInfoButton(String[] schoolNSociety, String password, String nickname) {
		// send modified user info to the server
	}

	@Override
	public void onCreateVersionCheckFragmentView() {
		mActionBar.hide();
		mHeader.setVisibility(View.GONE);
		currentPage = VERSION_PAGE_STATE;
	}

	@Override
	public void onDestroyVersionCheckFragmentView() {
		mActionBar.show();
		if(mSpecificInfoFragment != null && mSpecificInfoFragment.isVisible())
			mHeader.setVisibility(View.GONE);
		else
			mHeader.setVisibility(View.VISIBLE);
		mVersionCheckFragment = null;
		isBackButtonPressed = false;
	}

	@Override
	public void onCreateGuideFragmentView() {
		mActionBar.hide();
		mHeader.setVisibility(View.GONE);
		currentPage = GUIDE_PAGE_STATE;
	}

	@Override
	public void popGuideFragment() {
		mFragmentManager.popBackStackImmediate();
	}

	@Override
	public void onDestroyGuideFragmentView() {
		mActionBar.show();
		if(mSpecificInfoFragment != null && mSpecificInfoFragment.isVisible())
			mHeader.setVisibility(View.GONE);
		else
			mHeader.setVisibility(View.VISIBLE);
		mGuideFragment = null;
		isBackButtonPressed = false;
	}

	private class SpinnerArrayAdapter extends ArrayAdapter<String> {
		private Activity context;
		ArrayList<String> regionNameList;

		public SpinnerArrayAdapter(Activity context, int resource, ArrayList<String> regionNameList) {

			super(context, resource, regionNameList);
			this.context = context;
			this.regionNameList = regionNameList;

		}

		@Override
		public View getDropDownView(int position, View convertView,
									ViewGroup parent) {

			View spinnerItemView = getLayoutInflater().inflate(R.layout.spinner_region, parent, false);

			TextView regionName = (TextView) spinnerItemView.findViewById(R.id.spinner_item);

			regionName.setTypeface(mTypeface);
			regionName.setText(regionNameList.get(position));

			return spinnerItemView;
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View spinnerItemView = getLayoutInflater().inflate(R.layout.spinner_region, parent, false);

			TextView regionName = (TextView) spinnerItemView.findViewById(R.id.spinner_item);

			regionName.setTypeface(mTypeface);
			regionName.setText(regionNameList.get(position));

			return spinnerItemView;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// clear cached files
		//ServerCommunicationManager.getInstance(this).clearCache();
	}
}
