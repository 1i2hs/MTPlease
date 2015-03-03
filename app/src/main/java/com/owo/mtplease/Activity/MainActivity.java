package com.owo.mtplease.Activity;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.owo.mtplease.Analytics;
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
import com.owo.mtplease.fragment.SettingsFragment;
import com.owo.mtplease.fragment.ShoppingItemListFragment;
import com.owo.mtplease.fragment.SpecificInfoFragment;
import com.owo.mtplease.fragment.TimelineFragment;
import com.owo.mtplease.fragment.UserInfoModificationFragment;
import com.owo.mtplease.fragment.VersionCheckFragment;
import com.owo.mtplease.view.TypefaceLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
		GuideFragment.OnGuideFragmentListener,
		SettingsFragment.OnSettingsFragmentListener {

	// String for application version
	private static final int APPLICATION_VERSION = 16;

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
	public static final int SETTINGS_PAGE_STATE = 7;

	private static final int CONDITION_SEARCH_MODE = 1;
	private static final int KEYWORD_SEARCH_MODE = 2;
	private static final int NETWORK_CONNECTION_FAILED = -1;
	private static final int TIMELINE = 1;
	private static final int RESULT = 2;
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
	private FragmentManager _fragmentManager;
	private TimelineFragment _timelineFragment = null;
	private ResultFragment _resultFragment = null;
	private VersionCheckFragment _versionCheckFragment = null;
	private GuideFragment _guideFragment = null;
	private SpecificInfoFragment _specificInfoFragment = null;
	private ShoppingItemListFragment _shoppingItemListFragment = null;
	private SettingsFragment _settingsFragment = null;
	// End of the fragments

	// Graphical transitions
	private AlphaForegroundColorSpan _alphaForegroundColorSpan;
	private SpannableString _spannableStringAppTitle;
	private SpannableString _spannableStringVariable;
	private ColorDrawable _actionBarBackgroundColor;
	private int _minHeaderHeightForTimelineFragment;
	private int _minHeaderHeightForResultFragment;
	private int _headerHeight;
	private int _minHeaderTranslation;
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
	private PlanModelController _planModelController = null;
	// End of Model

	// Controller
	private UndoToastController _undoToastController;
	private TextWatcher _editTextWatcherForNumberOfPeopleSelectPlan = new TextWatcher() {

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
	private boolean _isBackButtonPressed = false;
	private Calendar _calendar = Calendar.getInstance();
	private String _modifiedDate;
	private ConditionDataForRequest _conditionDataForRequest = null;
	private float _clampValue;
	private int _searchMode;
	private LinkedList<ConditionDataForRequest> _conditionDataStack = null;
	private static int _currentPage = -1;
	// End of others


	public static float clamp(float value, float max, float min) {
		return Math.max(Math.min(value, min), max);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {

			_fragmentManager = getSupportFragmentManager();
			int fragmentManagerBackStackSize = _fragmentManager.getBackStackEntryCount();
			for (int i = 0; i < fragmentManagerBackStackSize; i++) {
				_fragmentManager.popBackStack();
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

			// configure the reconnect server button
			reconnectServerButton = (Button) findViewById(R.id.btn_reconnect_server);
			reconnectServerButton.setTypeface(mTypeface);
			reconnectServerButton.setVisibility(View.GONE);
			reconnectServerButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(_currentPage == TIMELINE_PAGE_STATE) {
						Log.d(TAG, "load timeline");
						loadTimelineFragment();
					}/*
					else if(_currentPage == RESULT_PAGE_STATE) {
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
			_searchMode = CONDITION_SEARCH_MODE;
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

		if(_conditionDataStack == null)
			_conditionDataStack = new LinkedList();
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

		mActionBar.setCustomView(actionBarView);

		changeActionBarStyle(getResources().getColor(R.color.mtplease_actionbar_color), getResources().getString(R.string.actionbar_title), null);

		ImageView drawerImageButton = (ImageView) actionBarView.findViewById(R.id.imageView_icon_drawer);
		drawerImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doubleSideSlidingMenu.showMenu(true);
			}
		});

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
		doubleSideSlidingMenu.setMode(SlidingMenu.LEFT);
		doubleSideSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		doubleSideSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
//		doubleSideSlidingMenu.setSecondaryShadowDrawable(R.drawable.shadow_right);
		doubleSideSlidingMenu.setShadowDrawable(R.drawable.shadow);
		doubleSideSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		doubleSideSlidingMenu.setFadeDegree(0.70f);
		doubleSideSlidingMenu.setMenu(R.layout.menu_side);
//		doubleSideSlidingMenu.setSecondaryMenu(R.layout.plan_side);
	}

	private void setPlan() {

		// instantiate mPlanModel to store plan data inside it.
		_planModelController = new PlanModelController();
		// end of instantiation

		// configure a custom toast controller
		mUndoToastView = (LinearLayout) findViewById(R.id.layout_toast_plan);
		_undoToastController = new UndoToastController(mUndoToastView, this);
		// end of configuration

		dateSelectPlanButton = (Button) findViewById(R.id.btn_select_date_plan);
		dateSelectPlanButton.setText(_modifiedDate);
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
		numberOfPeopleSelectPlanEditText.addTextChangedListener(_editTextWatcherForNumberOfPeopleSelectPlan);
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
				if(_planModelController.getRoomDataCount() > 0) {
					for(int roomIndex = 0; roomIndex < _planModelController.getRoomDataCount(); roomIndex++)
						planItemFadeOut(roomPlanLinearLayout.getChildAt(roomIndex));

					for(int directInputRoomIndex = 0;
						directInputRoomIndex < _planModelController.getDirectInputRoomDataCount(); directInputRoomIndex++)
						planItemFadeOut(directInputRoomPlanLinearLayout.getChildAt(directInputRoomIndex));

					/*_undoToastController.showUndoBar(getResources().getString(R.string.added_rooms_cleared),
							UndoToastController.CLEAR_ADDED_ROOMS_CASE);*/
					_undoToastController.showUndoBar(getResources().getString(R.string.added_rooms_cleared),
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
				if(_planModelController.getItemDataCount(ShoppingItemListFragment.MEAT_ITEM) > 0) {
					for(int meatIndex = 0;
						meatIndex < _planModelController.getItemDataCount(ShoppingItemListFragment.MEAT_ITEM);
						meatIndex++)
						planItemFadeOut(meatPlanLinearLayout.getChildAt(meatIndex));

					_undoToastController.showUndoBar(getResources().getString(R.string.added_meats_cleared),
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
				if(_planModelController.getItemDataCount(ShoppingItemListFragment.ALCOHOL_ITEM) > 0) {
					for(int alcoholIndex = 0;
						alcoholIndex < _planModelController.getItemDataCount(ShoppingItemListFragment.ALCOHOL_ITEM);
						alcoholIndex++)
						planItemFadeOut(alcoholPlanLinearLayout.getChildAt(alcoholIndex));

					_undoToastController.showUndoBar(getResources().getString(R.string.added_alcohols_cleared),
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
				if(_planModelController.getItemDataCount(ShoppingItemListFragment.OTHERS_ITEM) > 0) {
					for(int othersIndex = 0;
						othersIndex < _planModelController.getItemDataCount(ShoppingItemListFragment.OTHERS_ITEM);
						othersIndex++)
						planItemFadeOut(othersPlanLinearLayout.getChildAt(othersIndex));

					_undoToastController.showUndoBar(getResources().getString(R.string.added_others_cleared),
							UndoToastController.CLEAR_ADDED_OTHERS_CASE, -1);
				}
			}
		});

		totalOthersPrice = (TextView) findViewById(R.id.textView_price_total_others_plan);
		totalOthersPrice.setText(getResources().getString(R.string.currency_unit) + "0");
	}

	private void showShoppingItemList(int stringResId, int shoppingItemType) {
		Toast.makeText(this, stringResId, Toast.LENGTH_SHORT).show();
		if(_shoppingItemListFragment == null) {
			ShoppingItemListFragment shoppingItemListFragment = ShoppingItemListFragment.newInstance(shoppingItemType);
			// commit the ShoppingItemListFragment to the current view
			beginFragmentTransaction(shoppingItemListFragment, R.id.body_background);
			// end of the comission
		} else {
			_shoppingItemListFragment.switchRecyclerViewAdpater(shoppingItemType);
		}
		doubleSideSlidingMenu.toggle();
	}

	/*private void setAutoCompleteBasicInfoPlan() {
		_conditionDataForRequest = getUserInputData();
		dateSelectPlanButton.setText(_conditionDataForRequest.getDateWrittenLang());
		regionSelectPlanButton.setSelection(_conditionDataForRequest.getRegion() - 1);
		numberOfPeopleSelectPlanEditText.setText(String.valueOf(_conditionDataForRequest.getPeople()));
	}

	private void setAutoCompleteBasicInfoPlan(String mtDate, int regionOfMT, int numberOfPeople, int sexRatioProgress) {
		dateSelectPlanButton.setText(mtDate);
		regionSelectPlanButton.setSelection(regionOfMT);
		numberOfPeopleSelectPlanEditText.setText(String.valueOf(numberOfPeople));
		sexRatioPlanSeekBar.setProgress(sexRatioProgress);
	}*/

	private void setQueryHeader() {
		_minHeaderHeightForTimelineFragment = getResources().getDimensionPixelSize(R.dimen.min_header_height);
		_minHeaderHeightForResultFragment = getResources().getDimensionPixelOffset(R.dimen.header_height);
		_headerHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
		mHeader = findViewById(R.id.header);
		_spannableStringAppTitle = new SpannableString(getString(R.string.actionbar_title));
		_alphaForegroundColorSpan = new AlphaForegroundColorSpan(0xffffffff);
	}

	private void setSideMenuButtons() {
		homeButton = (Button) findViewById(R.id.btn_menu_home);
		homeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadTimelineFragment();
				doubleSideSlidingMenu.toggle();
			}
		});

		//compareButton = (TextView) findViewById(R.id.btn_menu_compare);

		mypageButton = (Button) findViewById(R.id.btn_menu_mypage);
		mypageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*MyPageFragment myPageFragment = MyPageFragment.newInstance();
				beginFragmentTransaction(myPageFragment, R.id.body_background);

				doubleSideSlidingMenu.toggle();*/
			}
		});

		settingButton = (Button) findViewById(R.id.btn_menu_setting);
		settingButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingsFragment settingsFragment = SettingsFragment.newInstance();
				beginFragmentTransaction(settingsFragment, R.id.body_background);

				doubleSideSlidingMenu.toggle();
			}
		});

		helpButton = (Button)findViewById(R.id.btn_menu_help);
		helpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GuideFragment guideFragment = GuideFragment.newInstance();
				beginFragmentTransaction(guideFragment, R.id.body_background);

				doubleSideSlidingMenu.toggle();
			}
		});

		/*logoutButton = (Button) findViewById(R.id.btn_menu_logout);
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
		});*/
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
		_modifiedDate = _calendar.get(Calendar.YEAR) + "년 " + (_calendar.get(Calendar.MONTH) + 1)
				+ "월 " + _calendar.get(Calendar.DATE) + "일";
		dateSelectButton.setText(_modifiedDate);
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
		// get timeline data from the server and create the _timelineFragment
		/*MTPleaseJsonObjectRequest getRequest = new MTPleaseJsonObjectRequest(Request.Method.GET, MTPLEASE_URL, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				splashScreenLayout.setVisibility(View.GONE);
				mActionBar.show();

				endLoadingProgress();
				// create the _timelineFragment with the Interface ScrollTabHolder
				try {
					Log.i(TAG, response.toString());
					// redundant to code. needs to be refactored
					JSONObject mJSONObject = response.getJSONObject("main");

					roomCountText.setText(mJSONObject.getString("roomCount") + "개의 방");

					TimelineFragment timelineFragment = TimelineFragment.newInstance(mJSONObject.toString());
					// end of creation of the _timelineFragment

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

		// clearing the backstack entries
		int fragmentManagerBackStackSize = _fragmentManager.getBackStackEntryCount();
		for (int i = 0; i < fragmentManagerBackStackSize; i++) {
			_fragmentManager.popBackStack();
		}

		JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, getResources().getString(R.string.mtplease_url), null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				splashScreenLayout.setVisibility(View.GONE);
				mActionBar.show();
				reconnectServerButton.setVisibility(View.GONE);
				endLoadingProgress();

				_currentPage = TIMELINE_PAGE_STATE;
				// create the _timelineFragment with the Interface ScrollTabHolder
				try {
					Log.i(TAG, response.toString());
					// redundant to code. needs to be refactored
					JSONObject mJSONObject = response.getJSONObject("main");

					roomCountText.setText(mJSONObject.getString("roomCount") + "개의 방이 함께하고 있습니다.");

					TimelineFragment timelineFragment = TimelineFragment.newInstance(mJSONObject.toString());
					// end of creation of the _timelineFragment

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

				_currentPage = TIMELINE_PAGE_STATE;
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

			_conditionDataForRequest = getUserInputData();

			numberOfPeopleSelectEditText.clearFocus();
					/*if (_conditionDataForRequest.getFlag() == CONDITION_SEARCH_MODE) {
						setAutoCompleteBasicInfoPlan();
					}*/

			//new DataRequestTask(RESULT).execute(_conditionDataForRequest.makeHttpGetURL());

					/*MTPleaseJsonObjectRequest getRequest = new MTPleaseJsonObjectRequest(Request.Method.GET, _conditionDataForRequest.makeHttpGetURL(), null, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							endLoadingProgress();
							Log.i(TAG, response.toString());
							_conditionDataForRequest = getUserInputData();
							// create the resultFragment with the Interface ScrollTabHolder
							ResultFragment resultFragment = ResultFragment.newInstance(response.toString(), _conditionDataForRequest.getDate());
							// end of creation of the _timelineFragment

							// commit the resultFragment to the current view
							beginFragmentTransaction(resultFragment, R.id.body_background);
							// end of commission

							_conditionDataStack.push(_conditionDataForRequest);
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

			Tracker t = ((Analytics) getApplication()).getTracker();
			t.send(new HitBuilders.EventBuilder()
					.setCategory("User Interaction")
					.setAction("Search Button Clicked")
					.setLabel(_conditionDataForRequest.getUserQueryString())
					.build());

			JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, _conditionDataForRequest.makeHttpGetURL(), null, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					reconnectServerButton.setVisibility(View.GONE);
					Log.i(TAG, response.toString());
					endLoadingProgress();

					_currentPage = RESULT_PAGE_STATE;

					// create the resultFragment with the Interface ScrollTabHolder
					ResultFragment resultFragment = ResultFragment.newInstance(response.toString(), _conditionDataForRequest.getDate());
					// end of creation of the _timelineFragment

					// commit the resultFragment to the current view
					beginFragmentTransaction(resultFragment, R.id.body_background);
					// end of commission

					_conditionDataStack.push(_conditionDataForRequest);
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					reconnectServerButton.setVisibility(View.VISIBLE);
					Log.d(TAG, error.toString());
					endLoadingProgress();

					_currentPage = RESULT_PAGE_STATE;
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
		CalendarDialogFragment _calendarDialogFragment = CalendarDialogFragment.newInstance(callerFlag);
		_calendarDialogFragment.show(_fragmentManager, "_calendar_dialog_popped");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.menu_main, menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch(id) {
			case R.id.action_version_check:
				/*VersionCheckFragment versionCheckFragment = VersionCheckFragment.newInstance();
				beginFragmentTransaction(versionCheckFragment, R.id.body_background);*/
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

		_currentPage = RESULT_PAGE_STATE;

		mHeader.setVisibility(View.VISIBLE);

		subTabLinearLayout.setVisibility(View.VISIBLE);

		/*if(!noResults && !_isBackButtonPressed) {
			mHeader.setTranslationY(-_minHeaderHeightForResultFragment);
			subTabLinearLayout.setAlpha(1);
			subTabLinearLayout.setBackgroundColor(getResources().getColor(R.color.mtplease_subtab_background_color));
		} else {
			mHeader.setTranslationY(0);
			subTabLinearLayout.setAlpha(0);
		}*/
		mHeader.setTranslationY(-_minHeaderHeightForResultFragment);
		subTabLinearLayout.setAlpha(1);
		subTabLinearLayout.setBackgroundColor(getResources().getColor(R.color.mtplease_subtab_background_color));

		// configure actionbar for result page
		if (noResults) {
			changeActionBarStyle(getResources().getColor(R.color.mtplease_actionbar_color), getResources().getString(R.string.results), getResources().getString(R.string.no_results));
		} else {
			changeActionBarStyle(getResources().getColor(R.color.mtplease_actionbar_color), getResources().getString(R.string.results), String.valueOf(numRoom) + "개의 방");
		}
		// end of the configuration

		// configure subtab for result page
		if(_isBackButtonPressed) {
			Log.d(TAG, "back button clicked");
			_conditionDataForRequest = _conditionDataStack.peekLast();
			Log.d(TAG, _conditionDataForRequest.getPeople() + "people");
		} else {
			Log.d(TAG, "back button not clicked");
			_conditionDataForRequest = getUserInputData();
		}

		String queryString = getResources().getString(R.string.we) + " ";
		queryString += _conditionDataForRequest.getDateWrittenLang() + getResources().getString(R.string.postposition_1);
		dateSelectButton.setText(_conditionDataForRequest.getDateWrittenLang());
		int regionCode = _conditionDataForRequest.getRegion();
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
		queryString += " " + _conditionDataForRequest.getPeople() + getResources().getString(R.string.postposition_2);
		numberOfPeopleSelectEditText.setText(String.valueOf(_conditionDataForRequest.getPeople()));

		queryString += " " + getResources().getString(R.string.go_MT);
		querySubTabText.setText(queryString);
		// end of the configuration

		Log.d(TAG, _isBackButtonPressed + "");
	}

	@Override
	public void onDestroyResultFragmentView() {
		if(_isBackButtonPressed)
			_conditionDataStack.pop();
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
		Log.d(TAG, "_isBackButtonPressed: " + _isBackButtonPressed);
		if(_isBackButtonPressed) {
			Log.d(TAG, "firstVisibleItemPosition: "+ firstVisibleViewPosition);
			if(firstVisibleViewPosition == 0) {
				mHeader.setTranslationY(0);
				subTabLinearLayout.setAlpha(0);
			}
		} else {
			layoutManager.scrollToPosition(defaultPosition);
		}
		_isBackButtonPressed = false;
	}

	@Override
	public void onCreateTimelineFragmentView() {

		_currentPage = TIMELINE_PAGE_STATE;

		_conditionDataStack.clear();

		mHeader.setVisibility(View.VISIBLE);

		subTabLinearLayout.setVisibility(View.INVISIBLE);
		roomCountText.setVisibility(View.VISIBLE);

		changeActionBarStyle(getResources().getColor(R.color.mtplease_actionbar_color), getResources().getString(R.string.actionbar_title), null);

		mHeader.setTranslationY(0);

		_isBackButtonPressed = false;
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
				_clampValue = changeHeaderTranslation(scrollY, _minHeaderHeightForTimelineFragment);
				return;
			case RESULT_FRAGMENT_VISIBLE:
				_clampValue = changeHeaderTranslation(scrollY, _minHeaderHeightForResultFragment);
				subTabLinearLayout.setAlpha(_clampValue);
				//subTabBackgroundColor.setAlpha((int) (_clampValue * 255));
				break;
			/*case SPECIFIC_INFO_FRAGMENT_VISIBLE:
				ratio = (float) getSupportActionBar().getHeight() / (float) (scrollY + 1);
				_clampValue = clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F);
				break;*/
			case SPECIFIC_INFO_FRAGMENT_VISIBLE:
			case SHOPPINGITEMLIST_FRAGMENT_VISIBLE:
			default:
				return;
		}

		//setTitleAlpha(_clampValue, visibleFragment);
	}

	private float changeHeaderTranslation(int scrollY, int minHeaderHeight) {
		_minHeaderTranslation = -minHeaderHeight/* + getSupportActionBar().getHeight()*/;

		//Log.i(TAG, -scrollY + "dp / " + _minHeaderTranslation + "dp");

		mHeader.setTranslationY(Math.max(-scrollY, _minHeaderTranslation));

		float ratio = clamp(mHeader.getTranslationY() / _minHeaderTranslation, 0.0F, 1.0F);

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
			headerHeight = _headerHeight;
		}

		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}

	private void setTitleAlpha(float alpha, int visibleFragment) {
		_alphaForegroundColorSpan.setAlpha(alpha);

		switch (visibleFragment) {
			case TIMELINE_FRAGMENT_VISIBLE:
				_spannableStringAppTitle.setSpan(_alphaForegroundColorSpan, 0, _spannableStringAppTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				//mActionBar.setTitle(_spannableStringAppTitle);
				break;
			case RESULT_FRAGMENT_VISIBLE:
				break;
			case SPECIFIC_INFO_FRAGMENT_VISIBLE:
				/*_spannableStringVariable = new SpannableString(getSupportActionBar().getTitle());
				_spannableStringVariable.setSpan(_alphaForegroundColorSpan, 0, _spannableStringVariable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				mActionBar.setTitle(_spannableStringVariable);
				_spannableStringVariable = null;
				_spannableStringVariable = new SpannableString(getSupportActionBar().getSubtitle());
				_spannableStringVariable.setSpan(_alphaForegroundColorSpan, 0, _spannableStringVariable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				mActionBar.setSubtitle(_spannableStringVariable);*/
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
			_modifiedDate = dateSelected;
			switch (callerFlag) {
				case CALL_FROM_CONDITIONAL_QUERY:
					dateSelectButton.setText(_modifiedDate);
					break;
				case CALL_FROM_PLAN:
					dateSelectPlanButton.setText(_modifiedDate);
					break;
				/*case CALL_FROM_ADDTOPLANDIALOG:
					AddToPlanDialogFragment mAddToPlanDialogFragment =
							(AddToPlanDialogFragment) getSupportFragmentManager()
									.findFragmentByTag("addtoplan_dialog_popped");
					if (mAddToPlanDialogFragment != null) {
						mAddToPlanDialogFragment.updateDate(_modifiedDate);
					}
					break;*/
			}
		}
	}

	public ConditionDataForRequest getUserInputData() throws NumberFormatException{
		ConditionDataForRequest conditionDataForRequest = new ConditionDataForRequest(this);

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


		conditionDataForRequest.setDateWrittenLang(_modifiedDate);

		String[] tmp = _modifiedDate.split(" ");
		conditionDataForRequest.setDate(tmp[0].substring(0, 4) + "-"
				+ tmp[1].split("월")[0] + "-" + tmp[2].split("일")[0]);
		Log.i(TAG, conditionDataForRequest.getDate());

		if (_searchMode == CONDITION_SEARCH_MODE) {
			conditionDataForRequest.setFlag(CONDITION_SEARCH_MODE);
		} else {
			conditionDataForRequest.setFlag(KEYWORD_SEARCH_MODE);
		}

		return conditionDataForRequest;
	}

	@Override
	public void onCreateSpecificInfoFragmentView(String roomName, String pensionName) {
		mHeader.setVisibility(View.GONE);

		_currentPage = SPECIFIC_INFO_PAGE_STATE;

		// configure actionbar for specific info page
		changeActionBarStyle(getResources().getColor(R.color.mtplease_actionbar_color), roomName, pensionName);
		// end of the configuration
		// **********************actionbar button issue...........

		_isBackButtonPressed = false;
	}

	@Override
	public void onDestroySpecificInfoFragmentView() {
		_specificInfoFragment = null;
	}

	@Override
	public void onClickAddRoomToPlanButton(final RoomInfoModelController roomInfoModelController) {

		// check rather room has been added already in the plan or not
		if(_planModelController.isRoomAddedAlready(roomInfoModelController.getPen_id(),
				roomInfoModelController.getRoom_name(), roomInfoModelController.getRoom_cost())) {
			Toast.makeText(this, R.string.room_already_added_before, Toast.LENGTH_LONG).show();
			return;
		}

		int roomDataCount = _planModelController.getRoomDataCount();
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

				_undoToastController.showUndoBar(roomInfoModelController.getRoom_name() + " " + getResources().getString(R.string.deleted),
						UndoToastController.DELETE_ADDED_ROOM_CASE, roomPlanViewIndex);
//				_undoToastController.setRoomViewAndData(roomPlanView, roomInfoModel.getPen_id(), roomInfoModel.getRoom_name(), roomInfoModel.getRoom_cost());
			}
		});
		// end of the configuration

		// get a thumbnail image of the room from the server and set it as an image
		// of the added room view
		ImageView roomThumbnailImageView = (ImageView) roomPlanView.findViewById(R.id.imageView_room_thumbnail);
		ProgressBar roomThumbnailImageLoadingProgressBar = (ProgressBar) roomPlanView.
				findViewById(R.id.progressBar_thumbnailImage_room_plan);

		new ImageLoadingTask(roomThumbnailImageView, roomThumbnailImageLoadingProgressBar).
				execute(roomInfoModelController.getRoomThumbnailImageURL(getResources().getString(R.string.mtplease_url)));
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
		_planModelController.addRoomData(roomInfoModelController.getPen_id(),
				roomInfoModelController.getRoom_name(), roomInfoModelController.getRoom_cost());

		// calculate the total cost for the added rooms
		totalRoomPrice.setText(castItemPriceToString(_planModelController.getTotalRoomCost()));

		// calculate the total cost for the whole plan
		totalPlanCost.setText(castItemPriceToString(_planModelController.getPlanTotalCost()));

		// notify room is added to the plan to the user
		Toast.makeText(this, R.string.added_to_plan, Toast.LENGTH_LONG).show();

		// show plan
		doubleSideSlidingMenu.showSecondaryMenu(true);

		/*_conditionDataForRequest = getUserInputData();

		if (_conditionDataForRequest.getFlag() == CONDITION_SEARCH_MODE) {
			AddToPlanDialogFragment addToPlanDialogFragment =
					AddToPlanDialogFragment.newInstance(_conditionDataForRequest.getDateWrittenLang(),
							_conditionDataForRequest.getRegion() - 1,
							_conditionDataForRequest.getPeople(), CONDITION_SEARCH_MODE);
			addToPlanDialogFragment.show(getSupportFragmentManager(), "addtoplan_dialog_popped");
		}*/
	}

	private void addViewAtPlan(ViewGroup parentView, View viewToBeAdded, int index) {
		parentView.addView(viewToBeAdded, index);
	}

	private void addDirectInputRoomPriceToPlan(final int directInputRoomPrice) {

		final int directInputRoomDataCount = _planModelController.getRoomDataCount();
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

				_undoToastController.showUndoBar(getResources().getString(R.string.directly_added_room)
								+ getResources().getString(R.string.deleted), UndoToastController.DELETE_DIRECTLY_INPUT_ROOM_CASE,
						directInputRoomPlanViewIndex);
//				_undoToastController.setDirectInputRoomViewAndData(directInputRoomPlanView, directInputRoomPrice);
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
		_planModelController.addDirectInputRoomData(directInputRoomPrice);

		// calculate the total cost for the added rooms
		totalRoomPrice.setText(castItemPriceToString(_planModelController.getTotalRoomCost()));

		// calculate the total cost for the whole plan
		totalPlanCost.setText(castItemPriceToString(_planModelController.getPlanTotalCost()));

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

			if(_shoppingItemListFragment != null && _shoppingItemListFragment.isVisible()) {
				Log.d(TAG,"Visible");
				Log.d(TAG,_fragmentManager.getBackStackEntryCount() + "ea");
				doubleSideSlidingMenu.showSecondaryMenu(true);
			}*/

			if(_currentPage == TIMELINE_PAGE_STATE) {
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

			_isBackButtonPressed = true;
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
		_actionBarBackgroundColor = new ColorDrawable(color);
		mActionBar.setBackgroundDrawable(_actionBarBackgroundColor);

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

		FragmentTransaction fragmentTransaction = _fragmentManager.beginTransaction();

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
			_currentPage = TIMELINE_PAGE_STATE;
			_timelineFragment = (TimelineFragment) targetFragment;
		} else if(targetFragment instanceof VersionCheckFragment) {
			_currentPage = VERSION_PAGE_STATE;
			_versionCheckFragment = (VersionCheckFragment) targetFragment;
		} else if(targetFragment instanceof  GuideFragment) {
			_currentPage = GUIDE_PAGE_STATE;
			_guideFragment = (GuideFragment) targetFragment;
		} else if(targetFragment instanceof SettingsFragment) {
			_currentPage = SETTINGS_PAGE_STATE;
			_settingsFragment = (SettingsFragment) targetFragment;
		} else if(targetFragment instanceof ResultFragment) {
			_currentPage = RESULT_PAGE_STATE;
			_resultFragment = (ResultFragment) targetFragment;
		} else if(targetFragment instanceof SpecificInfoFragment) {
			_currentPage = SPECIFIC_INFO_PAGE_STATE;
			_specificInfoFragment = (SpecificInfoFragment) targetFragment;
		} else if(targetFragment instanceof ShoppingItemListFragment) {
			_currentPage = SHOPPINGITEMLIST_PAGE_STATE;
			_shoppingItemListFragment = (ShoppingItemListFragment) targetFragment;
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

		_isBackButtonPressed = false;
	}

	@Override
	public void onDestroyShoppingItemListFragmentView() {
		_shoppingItemListFragment = null;
	}

	@Override
	public void onClickItem(int itemType, String itemName, String itemUnit, String itemUnitCount, int itemPrice) {
		AddItemToPlanDialogFragment addItemToPlanDialogFragment =
				AddItemToPlanDialogFragment.
						newInstance(itemType, itemName, itemUnit, itemUnitCount, itemPrice, SHOPPING_ITEM_INPUT_MODE);

		addItemToPlanDialogFragment.show(_fragmentManager, "addItemToPlanDialogFragment");
	}

	@Override
	public void onClickItem(int itemType) {
		AddItemToPlanDialogFragment addItemToPlanDialogFragment =
				AddItemToPlanDialogFragment.newInstance(itemType, CUSTOM_SHOPPING_ITEM_INPUT_MODE);

		addItemToPlanDialogFragment.show(_fragmentManager, "addItemToPlanDialogFragment");
	}

	@Override
	public void onClickAddItemToPlanButton(final int itemType, final String itemName, final int itemUnitPrice,
										   final int itemCount, final String itemUnit, final String itemCountUnit) {

		// check rather room has been added already in the plan or not
		if(_planModelController.isItemAddedAlready(itemType, itemName, itemUnitPrice)) {
			Toast.makeText(this, R.string.item_already_added_before, Toast.LENGTH_LONG).show();
			return;
		}

		int itemDataCount = _planModelController.getItemDataCount(itemType);
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

						_undoToastController.showUndoBar(itemName + getResources().getString(R.string.deleted),
								UndoToastController.DELETE_ADDED_MEAT_CASE, itemPlanViewIndex);
//						_undoToastController.setItemViewAndData(itemPlanView, itemType, itemName, itemUnitPrice);
					}
				});
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				addViewAtPlan(alcoholPlanLinearLayout, itemPlanView, itemPlanViewIndex);
				deleteItemButtonPlanImageView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						planItemFadeOut(itemPlanView);

						_undoToastController.showUndoBar(itemName + getResources().getString(R.string.deleted),
								UndoToastController.DELETE_ADDED_ALCOHOL_CASE, itemPlanViewIndex);
//						_undoToastController.setItemViewAndData(itemPlanView, itemType, itemName, itemUnitPrice);
					}
				});
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				addViewAtPlan(othersPlanLinearLayout, itemPlanView, itemPlanViewIndex);
				deleteItemButtonPlanImageView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						planItemFadeOut(itemPlanView);

						_undoToastController.showUndoBar(itemName + getResources().getString(R.string.deleted),
								UndoToastController.DELETE_ADDED_OTHERS_CASE, itemPlanViewIndex);
//						_undoToastController.setItemViewAndData(itemPlanView, itemType, itemName, itemUnitPrice);
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
										_planModelController.getSingleItemCount(itemType, itemName, itemUnitPrice), itemCountUnit);
				changeItemNumberInPlanDialogFragment.show(_fragmentManager, "changeItemNumberInPlanDialogFragment");
			}
		});

		// store item data in the mPlanModel
		_planModelController.addItemData(itemType, itemName, itemUnitPrice, itemCount);

		// calculate the total cost for the added rooms
		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				totalMeatPrice.setText(castItemPriceToString(_planModelController.getTotalItemCost(itemType)));
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				totalAlcoholPrice.setText(castItemPriceToString(_planModelController.getTotalItemCost(itemType)));
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				totalOthersPrice.setText(castItemPriceToString(_planModelController.getTotalItemCost(itemType)));
				break;
		}

		// calculate the total cost for the whole plan
		totalPlanCost.setText(castItemPriceToString(_planModelController.getPlanTotalCost()));

		// notify room is added to the plan to the user
		Toast.makeText(this, R.string.added_to_plan, Toast.LENGTH_SHORT).show();

		// show plan
		doubleSideSlidingMenu.showSecondaryMenu(true);

	}

	@Override
	public void onClickChangeButton(int itemType, String itemName, int itemUnitPrice, int newItemCount) {
		tempItemTotalPricePlanTextView.setText(castItemPriceToString(itemUnitPrice * newItemCount));
		tempNumberPickerCallButton.setText(String.valueOf(newItemCount));

		_planModelController.changeItemCount(itemType, itemName, itemUnitPrice, newItemCount);

		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				totalMeatPrice.setText(castItemPriceToString(_planModelController.getTotalItemCost(itemType)));
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				totalAlcoholPrice.setText(castItemPriceToString(_planModelController.getTotalItemCost(itemType)));
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				totalOthersPrice.setText(castItemPriceToString(_planModelController.getTotalItemCost(itemType)));
				break;
		}

		// calculate the total cost for the whole plan
		totalPlanCost.setText(castItemPriceToString(_planModelController.getPlanTotalCost()));
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
				 singleItemIndex < FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN + _planModelController.getItemDataCount(itemType);
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
					_planModelController.changeItemCount(itemType, _planModelController.getItemName(itemType, singleItemIndex - FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN),
							_planModelController.getItemUnitPrice(itemType, singleItemIndex - FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN), numPeople);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		totalMeatPrice.setText(castItemPriceToString(_planModelController.getTotalItemCost(ShoppingItemListFragment.MEAT_ITEM)));
		totalAlcoholPrice.setText(castItemPriceToString(_planModelController.getTotalItemCost(ShoppingItemListFragment.ALCOHOL_ITEM)));
		totalOthersPrice.setText(castItemPriceToString(_planModelController.getTotalItemCost(ShoppingItemListFragment.OTHERS_ITEM)));
		totalPlanCost.setText(castItemPriceToString(_planModelController.getPlanTotalCost()));
	}

	private void deleteAddedRoomInPlan(int viewIndex, LinearLayout roomPlanLinearLayout) {
		roomPlanLinearLayout.removeViewAt(viewIndex);

		if(roomPlanLinearLayout == this.roomPlanLinearLayout)
			_planModelController.removeRoomData(viewIndex);
		else
			_planModelController.removeDirectInputRoomData(viewIndex);
	}

	private void deleteAddedItemInPlan(int viewIndex, int itemType, LinearLayout itemPlanLinearLayout, TextView totalItemPrice) {

		itemPlanLinearLayout.removeViewAt(viewIndex);

		_planModelController.removeItemData(itemType, viewIndex);

		// calculate the total cost for the added rooms after deletion of one room
		totalItemPrice.setText(castItemPriceToString(_planModelController.getTotalItemCost(itemType)));

		// calculate the total cost for the whole plan
		totalPlanCost.setText(castItemPriceToString(_planModelController.getPlanTotalCost()));
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
				for(int roomIndex = 0; roomIndex < _planModelController.getRoomDataCount(); roomIndex++) {
					planItemFadeIn(roomPlanLinearLayout.getChildAt(roomIndex));
				}

				for(int directInputRoomIndex = 0;
					directInputRoomIndex < _planModelController.getDirectInputRoomDataCount(); directInputRoomIndex++) {
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
				for(int roomIndex = 0; roomIndex < _planModelController.getRoomDataCount(); roomIndex++)
					roomPlanLinearLayout.removeViewAt(0);

				for(int directInputRoomIndex = 0;
					directInputRoomIndex < _planModelController.getDirectInputRoomDataCount(); directInputRoomIndex++)
					directInputRoomPlanLinearLayout.removeViewAt(0);

				_planModelController.clearRoomData();

				totalRoomPrice.setText(castItemPriceToString(_planModelController.getTotalRoomCost()));

				// calculate the total cost for the whole plan
				totalPlanCost.setText(castItemPriceToString(_planModelController.getPlanTotalCost()));
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
			itemIndex < _planModelController.getItemDataCount(itemType);
			itemIndex++) {
			planItemFadeIn(itemPlanLinearLayout.getChildAt(itemIndex));
		}
	}

	private void clearAddedItemsInPlan(int itemType, LinearLayout itemPlanLinearLayout, TextView totalItemPrice) {
		for(int itemIndex = 0;
			itemIndex < _planModelController.getItemDataCount(itemType);
			itemIndex++)
			itemPlanLinearLayout.removeViewAt(itemIndex);

		_planModelController.clearItemData(itemType);

		totalItemPrice.setText(castItemPriceToString(_planModelController.getTotalItemCost(itemType)));

		// calculate the total cost for the whole plan
		totalPlanCost.setText(castItemPriceToString(_planModelController.getPlanTotalCost()));
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

		_isBackButtonPressed = false;
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

		_isBackButtonPressed = false;
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
		_currentPage = VERSION_PAGE_STATE;
	}

	@Override
	public void onDestroyVersionCheckFragmentView() {
		mActionBar.show();
		_versionCheckFragment = null;
		_isBackButtonPressed = false;
	}

	@Override
	public void onCreateGuideFragmentView() {
		mActionBar.hide();
		mHeader.setVisibility(View.GONE);
		_currentPage = GUIDE_PAGE_STATE;
	}

	@Override
	public void popGuideFragment() {
		_fragmentManager.popBackStackImmediate();
	}

	@Override
	public void onDestroyGuideFragmentView() {
		mActionBar.show();
		if(_specificInfoFragment != null && _specificInfoFragment.isVisible())
			mHeader.setVisibility(View.GONE);
		else if(_settingsFragment != null && _settingsFragment.isVisible())
			mHeader.setVisibility(View.GONE);
		else
			mHeader.setVisibility(View.VISIBLE);
		_guideFragment = null;
		_isBackButtonPressed = false;
	}

	@Override
	public void onCreateSettingsFragmentView() {
		setActionBarTitle(getResources().getString(R.string.action_settings), null);
		mHeader.setVisibility(View.GONE);
		_currentPage = SETTINGS_PAGE_STATE;
	}

	@Override
	public void onLoadVersionCheckFragmentView() {
		VersionCheckFragment versionCheckFragment = VersionCheckFragment.newInstance();
		beginFragmentTransaction(versionCheckFragment, R.id.body_background);
	}

	@Override
	public void onDestroySettingsFragmentView() {
		if(_specificInfoFragment != null && _specificInfoFragment.isVisible())
			mHeader.setVisibility(View.GONE);
		else
			mHeader.setVisibility(View.VISIBLE);
		_settingsFragment = null;
		_isBackButtonPressed = false;
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
