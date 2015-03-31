package com.owo.mtplease.Activity;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.owo.mtplease.ImageLoadingTask;
import com.owo.mtplease.PlanModelController;
import com.owo.mtplease.QueryDataModelController;
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
	private static final int APPLICATION_VERSION = 20;

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
	public static final int USER_INFO_MODIFICATION_PAGE_STATE = 8;

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
	private ShoppingItemListFragment _shoppingItemListFragment = null;
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
	private RelativeLayout _splashScreenLayout;
	private Toolbar _toolbar;
	private ActionBar _actionBar;
	private TextView _actionBarTitleTextView;
	private TextView _actionBarSubtitleTextView;
	private LinearLayout _actionBarQueryHeaderLinearLayout;
	private ImageButton _actionBarMenuButton;
	private SlidingMenu _doubleSideSlidingMenu;

	private RadioGroup _searchModeSwitchRadioGroup;
	private RadioButton _searchModeConditionalSearchRadioButton;
	private RadioButton _searchModeKeywordSearchRadioButton;

	private Button _dateSelectButton;
	private Spinner _regionSelectSpinner;
	private EditText _numberOfPeopleSelectEditText;
	private ImageButton _roomSearchButton;
	private LinearLayout _subTabLinearLayout;
	private TextView _querySubtabText;
	private FrameLayout _headerToggleImageViewFrameLayout;
	private ImageView _headerToggleImageView;

	private EditText _keywordInputEditText;
	private ImageButton _keywordSearchButton;

	private FrameLayout _loadingLayout;
	private Drawable _loadingBackground;
	private ProgressBar _loadingProgressBar;
	private View _mHeader;
	private LinearLayout _homeButton;
	private FrameLayout _homeButtonIndicator;
	private LinearLayout _compareButton;
	private FrameLayout _compareButtonIndicator;
	private LinearLayout _mypageButton;
	private FrameLayout _mypageButtonIndicator;
	private LinearLayout _settingButton;
	private FrameLayout _settingButtonIndicator;
	private LinearLayout _helpButton;
	private FrameLayout _helpButtonIndicator;
	private LinearLayout _logoutButton;
	private LinearLayout _mUndoToastView;
	private Button _dateSelectPlanButton;
	private Spinner _regionSelectPlanButton;
	private EditText _numberOfPeopleSelectPlanEditText;
	private TextView _numberOfMaleTextView;
	private TextView _numberOfFemaleTextView;
	private SeekBar _sexRatioPlanSeekBar;
	private EditText _directInputRoomPriceEditText;
	private Button _addDirectInputRoomPriceButton;
	private Button _recommendItemsPlanButton;
	private LinearLayout _roomPlanLinearLayout;
	private LinearLayout _directInputRoomPlanLinearLayout;
	private RelativeLayout _notSelectedRoomPlanRelativeLayout;
	private TextView _clearRoomPlanButton;
	private LinearLayout _meatPlanLinearLayout;
	private RelativeLayout _notSelectedMeatPlanRelativeLayout;
	private TextView _clearMeatPlanButton;
	private LinearLayout _alcoholPlanLinearLayout;
	private RelativeLayout _notSelectedAlcoholPlanRelativeLayout;
	private TextView _clearAlcoholPlanButton;
	private LinearLayout _othersPlanLinearLayout;
	private RelativeLayout _notSelectedOthersPlanRelativeLayout;
	private TextView _clearOthersPlanButton;
	private TextView _totalRoomPrice;
	private TextView _totalMeatPrice;
	private TextView _totalAlcoholPrice;
	private TextView _totalOthersPrice;
	private TextView _totalPlanCost;
	private TextView _tempItemTotalPricePlanTextView;    // temporary TextView instance
	private Button _tempNumberPickerCallButton;
	private Typeface _mTypeface;
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
				_sexRatioPlanSeekBar.setMax(newNumPeople);
				if (!_numberOfPeopleSelectPlanEditText.getText().toString().equals("")) {
					_recommendItemsPlanButton.setEnabled(true);
				} else {
					_recommendItemsPlanButton.setEnabled(false);
				}
			} catch (NumberFormatException e) {
				_sexRatioPlanSeekBar.setMax(0);
				e.printStackTrace();
			}
		}
	};
	// End of Controllers


	// Others
	private boolean _isApplicationOnCreateState = true;
	private boolean _isBackButtonPressed = false;
	private boolean _isHeaderOpened = false;
	private Calendar _calendar = Calendar.getInstance();
	private String _modifiedDate;
	private float _clampValue;
	private int _searchMode;
	private LinkedList<Integer> _pageStateStack = null;
	private LinkedList<QueryDataModelController> _conditionDataStack = null;
	private static int _currentPage = TIMELINE_PAGE_STATE;
	private static int _firstVisibleItemPosition = 0;

	private boolean _searchTurtorialPopped = false;
	private boolean _resultPageTutorialPopped = false;
	private boolean _specificInfoPageTutorialPopped = false;
	// End of others


	public static float clamp(float value, float max, float min) {
		return Math.max(Math.min(value, min), max);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		_toolbar = (Toolbar) findViewById(R.id.toolbar_main);
		setSupportActionBar(_toolbar);

		if (savedInstanceState == null) {

			_fragmentManager = getSupportFragmentManager();
			int fragmentManagerBackStackSize = _fragmentManager.getBackStackEntryCount();
			for (int i = 0; i < fragmentManagerBackStackSize; i++) {
				_fragmentManager.popBackStack();
			}

			// configure font type of the activity
			_mTypeface = TypefaceLoader.getInstance(getApplicationContext()).getTypeface();
			// end of the configuration

			// configure splash screen(will be deleted later)
			_setSplashScreen();
			// end of the configuration

			// configure actionbar
			_setActionBar();
			// end of the configuration of the actionbar

			// configure the SlidingMenu
			_setSlidingMenu();
			// end of the configuration of the SlidingMenu

			// configure the QueryHeader
			_setQueryHeader();
			// end of the configuration of QueryHeader

			// configure the side menu buttons
			_setSideMenuButtons();
			// end of the configuration of the side menu buttons

			// configure the conditional query UIs
			_setConditionalQueryInput();
			// end of the configuration of the conditional query UIs

			// configure the subtab of the query header
			_setSubtab();
			// end of the configuration of the subtab

			// configure the Plan UIs
			//_setPlan();
			// end of the configuration of the Plan UIs

			// configure the loading animation
			_setLoadingAnimation();
			// end of the configuration of the loading animation

			// configure mode of the search
			_searchMode = CONDITION_SEARCH_MODE;
			// end of the configuration of the mode of the search

			// set ServerCommunicationManager
			ServerCommunicationManager.getInstance(this).initImageLoader("cache", 1024 * 1024 * 20, Bitmap.CompressFormat.JPEG, 100);
			// end of the setting

			// check version of the application
			_checkApplicationVersion();
			// end of checking

			_loadTimelineFragment(null);
			_toggleMenuButton(R.id.layout_btn_home);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (_mTypeface == null) {
			_mTypeface = TypefaceLoader.getInstance(getApplicationContext()).getTypeface();
		}

		if (_conditionDataStack == null) {
			_conditionDataStack = new LinkedList();
		}

		if(_pageStateStack == null) {
			_pageStateStack = new LinkedList();
		}

		_loadingBackground.setAlpha(0);
	}

	private void _setSplashScreen() {
		_splashScreenLayout = (RelativeLayout) findViewById(R.id.relativelayout_splash);
	}

	private void _setActionBar() {

		_actionBar = getSupportActionBar();

		_actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

		View actionBarView = getLayoutInflater().inflate(R.layout.actionbar, null);

		_actionBarTitleTextView = (TextView) actionBarView.findViewById(R.id.textView_title_actionBar);
		_actionBarTitleTextView.setTypeface(_mTypeface);
		_actionBarTitleTextView.setText(getResources().getString(R.string.actionbar_title));

		_actionBarSubtitleTextView = (TextView) actionBarView.findViewById(R.id.textView_subtitle_actionBar);
		_actionBarSubtitleTextView.setTypeface(_mTypeface);

		_actionBarQueryHeaderLinearLayout = (LinearLayout) actionBarView.findViewById(R.id.linearLayout_query);

		_actionBar.setCustomView(actionBarView);

		_changeActionBarStyle(getResources().getColor(R.color.mtplease_color_primary), getResources().getString(R.string.actionbar_title), null);

		ImageView openDrawerButton = (ImageView) actionBarView.findViewById(R.id.imageView_icon_drawer);
		openDrawerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_doubleSideSlidingMenu.showMenu(true);
			}
		});

		_actionBar.hide();
	}

	/*private void _switchSearchMode() {
		((RelativeLayout) _mHeader).removeViewAt(0);
		if (_searchMode == CONDITION_SEARCH_MODE) {
			_setKeywordQueryInput();
			_searchMode = KEYWORD_SEARCH_MODE;
		} else {
			_setConditionalQueryInput();
			_searchMode = CONDITION_SEARCH_MODE;
		}
		_popTutorial();
	}*/

	/*private void _switchSearchMode(final int targetMode) {
		((RelativeLayout) _mHeader).removeViewAt(0);
		if (targetMode == CONDITION_SEARCH_MODE) {
			_setConditionalQueryInput();
		} else {
			_setKeywordQueryInput();
		}
		_searchMode = targetMode;

		_popTutorial();
	}*/

	private void _setActionBarTitle(String title, String subtitle) {
		_actionBarTitleTextView.setText(title);

		LinearLayout.LayoutParams params;
		if (subtitle != null) {
			params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
		} else {
			params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
		}

		_actionBarSubtitleTextView.setLayoutParams(params);
		_actionBarSubtitleTextView.setText(subtitle);
	}

	private void _setSlidingMenu() {
		_doubleSideSlidingMenu = new SlidingMenu(this);
		_doubleSideSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		_doubleSideSlidingMenu.setMode(SlidingMenu.LEFT);
		_doubleSideSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		_doubleSideSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
//		_doubleSideSlidingMenu.setSecondaryShadowDrawable(R.drawable.shadow_right);
		_doubleSideSlidingMenu.setShadowDrawable(R.drawable.shadow);
		_doubleSideSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		_doubleSideSlidingMenu.setFadeDegree(0.70f);
		_doubleSideSlidingMenu.setMenu(R.layout.menu_side);
//		_doubleSideSlidingMenu.setSecondaryMenu(R.layout.plan_side);
	}

	private void _setPlan() {

		// instantiate mPlanModel to store plan data inside it.
		_planModelController = new PlanModelController();
		// end of instantiation

		// configure a custom toast controller
		_mUndoToastView = (LinearLayout) findViewById(R.id.layout_toast_plan);
		_undoToastController = new UndoToastController(_mUndoToastView, this);
		// end of configuration

		_dateSelectPlanButton = (Button) findViewById(R.id.btn_select_date_plan);
		_dateSelectPlanButton.setText(_modifiedDate);
		_dateSelectPlanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_numberOfPeopleSelectPlanEditText.clearFocus();
				_callCalendarDialogFragment(CALL_FROM_PLAN);
			}
		});

		_regionSelectPlanButton = (Spinner) findViewById(R.id.spinner_select_region_plan);
		ArrayAdapter<CharSequence> regionSpinnerPlanAdapter =
				ArrayAdapter.createFromResource(this, R.array.array_region, R.layout.spinner_region);
		regionSpinnerPlanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_regionSelectPlanButton.setAdapter(regionSpinnerPlanAdapter);

		_numberOfPeopleSelectPlanEditText = (EditText) findViewById(R.id.editText_number_people_plan);
		_numberOfPeopleSelectPlanEditText.addTextChangedListener(_editTextWatcherForNumberOfPeopleSelectPlan);
		_numberOfPeopleSelectPlanEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Log.d(TAG, "onFocusChange");
				if (!hasFocus) {
					hideKeyboard(v);
				}
			}
		});

		_numberOfMaleTextView = (TextView) findViewById(R.id.textView_number_male_plan);
		_numberOfMaleTextView.setText("0" + getResources().getString(R.string.people_unit));
		_numberOfFemaleTextView = (TextView) findViewById(R.id.textView_number_female_plan);
		_numberOfFemaleTextView.setText("0" + getResources().getString(R.string.people_unit));

		_sexRatioPlanSeekBar = (SeekBar) findViewById(R.id.seekBar_ratio_sex_plan);
		_sexRatioPlanSeekBar.setMax(0);

		_sexRatioPlanSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// set number of male to the TextView
				_numberOfMaleTextView.setText(progress + getResources().getString(R.string.people_unit));
				// set number of female to the TextView
				_numberOfFemaleTextView.setText((seekBar.getMax() - progress) + getResources().getString(R.string.people_unit));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				_numberOfPeopleSelectPlanEditText.clearFocus();
				_directInputRoomPriceEditText.clearFocus();
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		_directInputRoomPriceEditText = (EditText) findViewById(R.id.editText_input_direct_price_room_plan);
		_directInputRoomPriceEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Log.d(TAG, "onFocusChange");
				if (!hasFocus) {
					hideKeyboard(v);
				}
			}
		});

		_addDirectInputRoomPriceButton = (Button) findViewById(R.id.btn_add_direct_price_room_plan);
		_addDirectInputRoomPriceButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (!_addDirectInputRoomPriceButton.getText().toString().equals("")) {
						_addDirectInputRoomPriceToPlan(Integer.parseInt(_directInputRoomPriceEditText.getText().toString()));
					}
				} catch (NumberFormatException e) {
					Toast.makeText(v.getContext(), R.string.please_type_only_number_for_price, Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		});

		_recommendItemsPlanButton = (Button) findViewById(R.id.btn_recommend_items_plan);
		_recommendItemsPlanButton.setEnabled(false);

		_totalPlanCost = (TextView) findViewById(R.id.textView_cost_total_plan);
		_totalPlanCost.setText(_castItemPriceToString(0));

		_roomPlanLinearLayout = (LinearLayout) findViewById(R.id.layout_room);

		_directInputRoomPlanLinearLayout = (LinearLayout) findViewById(R.id.layout_room_direct_input);

		_notSelectedRoomPlanRelativeLayout = (RelativeLayout) findViewById(R.id.layout_add_room);
		_notSelectedRoomPlanRelativeLayout.setOnClickListener(new View.OnClickListener() {
			// evokes when black block(the view "frame_add_room_plan") is clicked
			@Override
			public void onClick(View v) {
				Toast.makeText(v.getContext(), R.string.please_search_for_room, Toast.LENGTH_SHORT).show();
				_doubleSideSlidingMenu.toggle();
			}
		});

		_clearRoomPlanButton = (TextView) findViewById(R.id.btn_clear_room);
		_clearRoomPlanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (_planModelController.getRoomDataCount() > 0) {
					for (int roomIndex = 0; roomIndex < _planModelController.getRoomDataCount(); roomIndex++)
						_planItemFadeOut(_roomPlanLinearLayout.getChildAt(roomIndex));

					for (int directInputRoomIndex = 0;
						 directInputRoomIndex < _planModelController.getDirectInputRoomDataCount(); directInputRoomIndex++)
						_planItemFadeOut(_directInputRoomPlanLinearLayout.getChildAt(directInputRoomIndex));

					/*_undoToastController.showUndoBar(getResources().getString(R.string.added_rooms_cleared),
							UndoToastController.CLEAR_ADDED_ROOMS_CASE);*/
					_undoToastController.showUndoBar(getResources().getString(R.string.added_rooms_cleared),
							UndoToastController.CLEAR_ADDED_ROOMS_CASE, -1);
				}
			}
		});

		_totalRoomPrice = (TextView) findViewById(R.id.textView_price_total_room_plan);
		_totalRoomPrice.setText(getResources().getString(R.string.currency_unit) + "0");

		_meatPlanLinearLayout = (LinearLayout) findViewById(R.id.layout_meat);

		_notSelectedMeatPlanRelativeLayout = (RelativeLayout) findViewById(R.id.layout_add_meat);
		_notSelectedMeatPlanRelativeLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_showShoppingItemList(R.string.please_select_meat, ShoppingItemListFragment.MEAT_ITEM);
			}
		});

		_clearMeatPlanButton = (TextView) findViewById(R.id.btn_clear_meat);
		_clearMeatPlanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (_planModelController.getItemDataCount(ShoppingItemListFragment.MEAT_ITEM) > 0) {
					for (int meatIndex = 0;
						 meatIndex < _planModelController.getItemDataCount(ShoppingItemListFragment.MEAT_ITEM);
						 meatIndex++)
						_planItemFadeOut(_meatPlanLinearLayout.getChildAt(meatIndex));

					_undoToastController.showUndoBar(getResources().getString(R.string.added_meats_cleared),
							UndoToastController.CLEAR_ADDED_MEATS_CASE, -1);
				}
			}
		});

		_totalMeatPrice = (TextView) findViewById(R.id.textView_price_total_meat_plan);
		_totalMeatPrice.setText(getResources().getString(R.string.currency_unit) + "0");

		_alcoholPlanLinearLayout = (LinearLayout) findViewById(R.id.layout_alcohol);

		_notSelectedAlcoholPlanRelativeLayout = (RelativeLayout) findViewById(R.id.layout_add_alcohol);
		_notSelectedAlcoholPlanRelativeLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_showShoppingItemList(R.string.please_select_alcohol, ShoppingItemListFragment.ALCOHOL_ITEM);
			}
		});

		_clearAlcoholPlanButton = (TextView) findViewById(R.id.btn_clear_alcohol);
		_clearAlcoholPlanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (_planModelController.getItemDataCount(ShoppingItemListFragment.ALCOHOL_ITEM) > 0) {
					for (int alcoholIndex = 0;
						 alcoholIndex < _planModelController.getItemDataCount(ShoppingItemListFragment.ALCOHOL_ITEM);
						 alcoholIndex++)
						_planItemFadeOut(_alcoholPlanLinearLayout.getChildAt(alcoholIndex));

					_undoToastController.showUndoBar(getResources().getString(R.string.added_alcohols_cleared),
							UndoToastController.CLEAR_ADDED_ALCOHOLS_CASE, -1);
				}
			}
		});

		_totalAlcoholPrice = (TextView) findViewById(R.id.textView_price_total_alcohol_plan);
		_totalAlcoholPrice.setText(getResources().getString(R.string.currency_unit) + "0");

		_othersPlanLinearLayout = (LinearLayout) findViewById(R.id.layout_others);

		_notSelectedOthersPlanRelativeLayout = (RelativeLayout) findViewById(R.id.layout_add_others);
		_notSelectedOthersPlanRelativeLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_showShoppingItemList(R.string.please_select_others, ShoppingItemListFragment.OTHERS_ITEM);
			}
		});

		_clearOthersPlanButton = (TextView) findViewById(R.id.btn_clear_others);
		_clearOthersPlanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (_planModelController.getItemDataCount(ShoppingItemListFragment.OTHERS_ITEM) > 0) {
					for (int othersIndex = 0;
						 othersIndex < _planModelController.getItemDataCount(ShoppingItemListFragment.OTHERS_ITEM);
						 othersIndex++)
						_planItemFadeOut(_othersPlanLinearLayout.getChildAt(othersIndex));

					_undoToastController.showUndoBar(getResources().getString(R.string.added_others_cleared),
							UndoToastController.CLEAR_ADDED_OTHERS_CASE, -1);
				}
			}
		});

		_totalOthersPrice = (TextView) findViewById(R.id.textView_price_total_others_plan);
		_totalOthersPrice.setText(getResources().getString(R.string.currency_unit) + "0");
	}

	private void _showShoppingItemList(int stringResId, int shoppingItemType) {
		Toast.makeText(this, stringResId, Toast.LENGTH_SHORT).show();
		if (_shoppingItemListFragment == null) {
			ShoppingItemListFragment shoppingItemListFragment = ShoppingItemListFragment.newInstance(shoppingItemType);
			// commit the ShoppingItemListFragment to the current view
			_beginFragmentTransaction(shoppingItemListFragment, R.id.body_background);
			// end of the comission
		} else {
			_shoppingItemListFragment.switchRecyclerViewAdpater(shoppingItemType);
		}
		_doubleSideSlidingMenu.toggle();
	}

	/*private void setAutoCompleteBasicInfoPlan() {
		_conditionDataForRequest = _getUserInputData();
		_dateSelectPlanButton.setText(_conditionDataForRequest.getDateWrittenLang());
		_regionSelectPlanButton.setSelection(_conditionDataForRequest.getRegion() - 1);
		_numberOfPeopleSelectPlanEditText.setText(String.valueOf(_conditionDataForRequest.getPeople()));
	}

	private void setAutoCompleteBasicInfoPlan(String mtDate, int regionOfMT, int numberOfPeople, int sexRatioProgress) {
		_dateSelectPlanButton.setText(mtDate);
		_regionSelectPlanButton.setSelection(regionOfMT);
		_numberOfPeopleSelectPlanEditText.setText(String.valueOf(numberOfPeople));
		_sexRatioPlanSeekBar.setProgress(sexRatioProgress);
	}*/

	private void _setQueryHeader() {
		_minHeaderHeightForTimelineFragment = getResources().getDimensionPixelSize(R.dimen.max_header_height);
		_minHeaderHeightForResultFragment = getResources().getDimensionPixelOffset(R.dimen.header_height);
		_headerHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
		_mHeader = findViewById(R.id.header);
		_spannableStringAppTitle = new SpannableString(getString(R.string.actionbar_title));
		_alphaForegroundColorSpan = new AlphaForegroundColorSpan(0xffffffff);
		/*_searchModeSwitchRadioGroup = (RadioGroup) findViewById(R.id.radioGroup_switch_search_mode);
		_searchModeConditionalSearchRadioButton = (RadioButton) findViewById(R.id.radioButton_search_mode_conditional);
		_searchModeKeywordSearchRadioButton = (RadioButton) findViewById(R.id.radioButton_search_mode_keyword);
		_searchModeSwitchRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId) {
					case R.id.radioButton_search_mode_conditional:
						_searchModeConditionalSearchRadioButton.setTextColor(Color.WHITE);
						_searchModeKeywordSearchRadioButton.setTextColor(getResources().getColor(R.color.mtplease_main_text_color));
						break;
					case R.id.radioButton_search_mode_keyword:
						_searchModeKeywordSearchRadioButton.setTextColor(Color.WHITE);
						_searchModeConditionalSearchRadioButton.setTextColor(getResources().getColor(R.color.mtplease_main_text_color));
						break;
				}
				_switchSearchMode();
				hideKeyboard(group);
			}
		});*/

	}

	private void _setSideMenuButtons() {

		_setKeywordQueryInput();

		_homeButton = (LinearLayout) findViewById(R.id.layout_btn_home);
		_homeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_loadTimelineFragment(null);
				_doubleSideSlidingMenu.toggle(true);
				_toggleMenuButton(R.id.layout_btn_home);
			}
		});

		_homeButtonIndicator = (FrameLayout) findViewById(R.id.framelayout_indicator_home);

		//_compareButton = (TextView) findViewById(R.id.btn_menu_compare);

		_mypageButton = (LinearLayout) findViewById(R.id.layout_btn_mypage);
		_mypageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*MyPageFragment myPageFragment = MyPageFragment.newInstance();
				_beginFragmentTransaction(myPageFragment, R.id.body_background);

				_doubleSideSlidingMenu.toggle();
				_toggleMenuButton(R.id.layout_btn_mypage);
*/
			}
		});

		_mypageButtonIndicator = (FrameLayout) findViewById(R.id.framelayout_indicator_mypage);

		_settingButton = (LinearLayout) findViewById(R.id.layout_btn_setting);
		_settingButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingsFragment settingsFragment = SettingsFragment.newInstance();
				_beginFragmentTransaction(settingsFragment, R.id.body_background);
				_doubleSideSlidingMenu.toggle();
				_toggleMenuButton(R.id.layout_btn_setting);
			}
		});

		_settingButtonIndicator = (FrameLayout) findViewById(R.id.framelayout_indicator_setting);

		_helpButton = (LinearLayout) findViewById(R.id.layout_btn_help);
		_helpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GuideFragment guideFragment = GuideFragment.newInstance();
				_beginFragmentTransaction(guideFragment, R.id.body_background);
				_doubleSideSlidingMenu.toggle();
				_toggleMenuButton(R.id.layout_btn_help);
			}
		});

		_helpButtonIndicator = (FrameLayout) findViewById(R.id.framelayout_indicator_help);

		/*_logoutButton = (Button) findViewById(R.id.btn_menu_logout);
		_logoutButton.setOnClickListener(new View.OnClickListener() {
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

	private void _toggleMenuButton(int buttonResId) {

		_homeButtonIndicator.setVisibility(View.INVISIBLE);
		_mypageButtonIndicator.setVisibility(View.INVISIBLE);
		_settingButtonIndicator.setVisibility(View.INVISIBLE);
		_helpButtonIndicator.setVisibility(View.INVISIBLE);

		_homeButton.setBackgroundColor(Color.TRANSPARENT);
		_mypageButton.setBackgroundColor(Color.TRANSPARENT);
		_settingButton.setBackgroundColor(Color.TRANSPARENT);
		_helpButton.setBackgroundColor(Color.TRANSPARENT);

		switch(buttonResId) {
			case R.id.layout_btn_home:
				_homeButton.setBackgroundColor(Color.GRAY);
				_homeButtonIndicator.setVisibility(View.VISIBLE);
				break;
			case R.id.layout_btn_mypage:
				_mypageButton.setBackgroundColor(Color.GRAY);
				_mypageButtonIndicator.setVisibility(View.VISIBLE);
				break;
			case R.id.layout_btn_setting:
				_settingButton.setBackgroundColor(Color.GRAY);
				_settingButtonIndicator.setVisibility(View.VISIBLE);
				break;
			case R.id.layout_btn_help:
				_helpButton.setBackgroundColor(Color.GRAY);
				_helpButtonIndicator.setVisibility(View.VISIBLE);
				break;
		}
	}

	private void _setConditionalQueryInput() {

		View conditionalSearchHeaderView = getLayoutInflater().inflate(R.layout.frame_conditional_search, (RelativeLayout) _mHeader, false);

		TextView weTextView = (TextView) conditionalSearchHeaderView.findViewById(R.id.textView_we);
		weTextView.setTypeface(_mTypeface);

		TextView postpositionTextView = (TextView) conditionalSearchHeaderView.findViewById(R.id.textView_postposition);
		postpositionTextView.setTypeface(_mTypeface);

		TextView withTextView = (TextView) conditionalSearchHeaderView.findViewById(R.id.textView_with);
		withTextView.setTypeface(_mTypeface);

		TextView goMTTextView = (TextView) conditionalSearchHeaderView.findViewById(R.id.textView_go_MT);
		goMTTextView.setTypeface(_mTypeface);

		_dateSelectButton = (Button) conditionalSearchHeaderView.findViewById(R.id.btn_select_date);
		_modifiedDate = _calendar.get(Calendar.YEAR) + "년 " + (_calendar.get(Calendar.MONTH) + 1)
				+ "월 " + _calendar.get(Calendar.DATE) + "일";
		_dateSelectButton.setText(_modifiedDate);
		_dateSelectButton.setTypeface(_mTypeface);
		_dateSelectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_callCalendarDialogFragment(CALL_FROM_CONDITIONAL_QUERY);
			}
		});

		_regionSelectSpinner = (Spinner) conditionalSearchHeaderView.findViewById(R.id.spinner_select_region);
		/*ArrayAdapter<CharSequence> regionSpinnerAdapter =
				ArrayAdapter.createFromResource(this, R.array.array_region, R.layout.spinner_region);
		regionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
		ArrayList<String> regionNameList = new ArrayList<String>();
		regionNameList.add(getResources().getString(R.string.daesungri));
		SpinnerArrayAdapter spinnerArrayAdapter = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, regionNameList);
		_regionSelectSpinner.setAdapter(spinnerArrayAdapter);

		_numberOfPeopleSelectEditText = (EditText) conditionalSearchHeaderView.findViewById(R.id.editText_input_number_people);
		_numberOfPeopleSelectEditText.setTypeface(_mTypeface);
		_numberOfPeopleSelectEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Log.d(TAG, "onFocusChange");
				if (!hasFocus) {
					hideKeyboard(v);
				}
			}
		});

		_roomSearchButton = (ImageButton) conditionalSearchHeaderView.findViewById(R.id.btn_search_room);
		_roomSearchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_searchMode = CONDITION_SEARCH_MODE;
				_loadResultFragment();
			}
		});

		((RelativeLayout) _mHeader).addView(conditionalSearchHeaderView, 0);

//		_searchModeConditionalSearchRadioButton.setChecked(true);
	}


	private void _setKeywordQueryInput() {
//		View keywordSearchHeaderView = getLayoutInflater().inflate(R.layout.frame_keyword_search, (RelativeLayout) _mHeader, false);

		_keywordInputEditText = (EditText) findViewById(R.id.editText_search_keyword);
		_keywordInputEditText.setTypeface(_mTypeface);
		_keywordInputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					_searchMode = KEYWORD_SEARCH_MODE;
					if (!_keywordInputEditText.getText().toString().equals("")) {
						_loadResultFragment();
						_doubleSideSlidingMenu.toggle(true);
						_toggleMenuButton(R.id.layout_btn_home);
					} else {
						Toast.makeText(v.getContext(), R.string.please_type_keyword, Toast.LENGTH_SHORT).show();
					}

					return false;
				}
				return false;
			}
		});

		_keywordInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					hideKeyboard(v);
				}
			}
		});

		_keywordSearchButton = (ImageButton) findViewById(R.id.imageButton_search_keyword);
		_keywordSearchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_searchMode = KEYWORD_SEARCH_MODE;
				if (!_keywordInputEditText.getText().toString().equals("")) {
					_loadResultFragment();
					_doubleSideSlidingMenu.toggle(true);
					_toggleMenuButton(R.id.layout_btn_home);
				} else {
					Toast.makeText(v.getContext(), R.string.please_type_keyword, Toast.LENGTH_SHORT).show();
				}
			}
		});

//		((RelativeLayout) _mHeader).addView(keywordSearchHeaderView, 0);

//		_searchModeKeywordSearchRadioButton.setChecked(true);
	}

	private void _setSubtab() {

		// configure the number of the room that our service have inside our database
		/*_roomCountText = (TextView) findViewById(R.id.text_room_count);
		_roomCountText.setTypeface(_mTypeface);*/
		// end of the configuration

		_subTabLinearLayout = (LinearLayout) findViewById(R.id.layout_subtab);
		_querySubtabText = (TextView) findViewById(R.id.textView_query);

		_headerToggleImageView = (ImageView) findViewById(R.id.imageView_icn_toggle_header);

		_headerToggleImageViewFrameLayout = (FrameLayout) findViewById(R.id.layout_btn_toggle_header);
		_headerToggleImageViewFrameLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (_currentPage == RESULT_PAGE_STATE) {
					if (!_isHeaderOpened) {
						_mHeader.animate().translationYBy(_minHeaderHeightForResultFragment);
						_subTabLinearLayout.setAlpha(0.0F);
						_headerToggleImageView.animate().rotationBy(180F);
						v.animate().translationYBy(-getResources().getDimension(R.dimen.query_status_bar_height));
						_isHeaderOpened = true;
					} else {
						_mHeader.animate().translationYBy(-_minHeaderHeightForResultFragment);
						_subTabLinearLayout.setAlpha(1.0F);
						_headerToggleImageView.animate().rotationBy(180F);
						v.animate().translationYBy(getResources().getDimension(R.dimen.query_status_bar_height));
						_isHeaderOpened = false;
					}
				}
			}
		});


	}

	private void _setLoadingAnimation() {
		_loadingLayout = (FrameLayout) findViewById(R.id.framelayout_loading);
		_loadingBackground = _loadingLayout.getBackground();
		_loadingBackground.setAlpha(0);

		_loadingProgressBar = (ProgressBar) findViewById(R.id.progressBar_condition_search);
		_loadingProgressBar.setVisibility(View.GONE);
	}

	private void _loadTimelineFragment(final SwipeRefreshLayout swipeRefreshLayout) {
		// get timeline data from the server and create the _timelineFragment
		/*MTPleaseJsonObjectRequest getRequest = new MTPleaseJsonObjectRequest(Request.Method.GET, MTPLEASE_URL, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				_splashScreenLayout.setVisibility(View.GONE);
				_actionBar.show();

				endLoadingProgress();
				// create the _timelineFragment with the Interface ScrollTabHolder
				try {
					Log.i(TAG, response.toString());
					// redundant to code. needs to be refactored
					JSONObject mJSONObject = response.getJSONObject("main");

					_roomCountText.setText(mJSONObject.getString("roomCount") + "개의 방");

					TimelineFragment timelineFragment = TimelineFragment.newInstance(mJSONObject.toString());
					// end of creation of the _timelineFragment

					// commit the timelineFragment to the current view
					_beginFragmentTransaction(timelineFragment, R.id.body_background);
					// end of commission
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				_splashScreenLayout.setVisibility(View.GONE);
				_actionBar.show();

				_subTabLinearLayout.setVisibility(View.INVISIBLE);
				_roomCountText.setVisibility(View.VISIBLE);

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

		JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, getResources().getString(R.string.mtplease_url), (String) null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				if(_isApplicationOnCreateState) {
					_splashScreenLayout.setVisibility(View.GONE);
					_actionBar.show();
				}

				if(swipeRefreshLayout != null)
					swipeRefreshLayout.setRefreshing(false);
				else
					endLoadingProgress();

				// create the _timelineFragment with the Interface ScrollTabHolder
				try {
					Log.i(TAG, response.toString());
					// redundant to code. needs to be refactored
					JSONObject mJSONObject = response.getJSONObject("main");

//					_roomCountText.setText(mJSONObject.getString("roomCount") + "개의 방이 함께하고 있습니다.");

					TimelineFragment timelineFragment = TimelineFragment.newInstance(mJSONObject.toString());
					// end of creation of the _timelineFragment

					// commit the timelineFragment to the current view
					_beginFragmentTransaction(timelineFragment, R.id.body_background);
					// end of commission

					// pop tutorial if the use of the application is first time or right after any application update
					_popTutorial();

					if(_isApplicationOnCreateState) {

						// left menu opening animation to show users that there is a menu on left side of the application
						_doubleSideSlidingMenu.toggle(true);
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								_doubleSideSlidingMenu.toggle(true);
							}
						}, 1000);

						_isApplicationOnCreateState = false;
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if(_isApplicationOnCreateState) {
					_splashScreenLayout.setVisibility(View.GONE);
					_actionBar.show();
					_isApplicationOnCreateState = false;
				}
				_subTabLinearLayout.setVisibility(View.INVISIBLE);
//				_roomCountText.setVisibility(View.VISIBLE);

				if(swipeRefreshLayout != null)
					swipeRefreshLayout.setRefreshing(false);
				else
					endLoadingProgress();

				TimelineFragment timelineFragment = TimelineFragment.newInstance(null);
				// end of creation of the _timelineFragment

				// commit the timelineFragment to the current view
				_beginFragmentTransaction(timelineFragment, R.id.body_background);
				// end of commission

				Log.d(TAG, error.toString());
				Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
			}
		});

		if(swipeRefreshLayout == null)
			startLoadingProgress();

		ServerCommunicationManager.getInstance(this).addToRequestQueue(getRequest);
		// end of getting the data from the server and the creation of the fragment
	}

	private void _loadResultFragment() {

		try {
			QueryDataModelController queryDataModelController = _getUserInputData();

			Tracker t = ((Analytics) getApplication()).getTracker();
			t.send(new HitBuilders.EventBuilder()
					.setCategory("User Interaction")
					.setAction("Search Button Clicked")
					.setLabel(queryDataModelController.getUserQueryString())
					.build());

			if (_searchMode == CONDITION_SEARCH_MODE) {
				_numberOfPeopleSelectEditText.clearFocus();
					/*if (_conditionDataForRequest.getFlag() == CONDITION_SEARCH_MODE) {
						setAutoCompleteBasicInfoPlan();
					}*/

				//new DataRequestTask(RESULT).execute(_conditionDataForRequest.makeHttpGetURL());

					/*MTPleaseJsonObjectRequest getRequest = new MTPleaseJsonObjectRequest(Request.Method.GET, _conditionDataForRequest.makeHttpGetURL(), null, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							endLoadingProgress();
							Log.i(TAG, response.toString());
							_conditionDataForRequest = _getUserInputData();
							// create the resultFragment with the Interface ScrollTabHolder
							ResultFragment resultFragment = ResultFragment.newInstance(response.toString(), _conditionDataForRequest.getDate());
							// end of creation of the _timelineFragment

							// commit the resultFragment to the current view
							_beginFragmentTransaction(resultFragment, R.id.body_background);
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
			} else {
				_keywordInputEditText.clearFocus();
			}
			// if the search mode is set on keyword, then queryDataModelController.getDate() will return null
			// else if the search mode is set on conditional query, the queryDataModelController.getDate() will return input date
			_requestRoomResultFromServer(queryDataModelController, ResultFragment.LIST_OF_ROOMS_AFTER_SEARCH);

		} catch (NumberFormatException e) {
			if (_numberOfPeopleSelectEditText.getText().toString().equals("")) {
				Toast.makeText(MainActivity.this,
						R.string.please_number_of_people_input, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MainActivity.this,
						R.string.please_type_only_number_for_number_of_people, Toast.LENGTH_SHORT).show();
			}
			e.printStackTrace();
		}
	}

	private void _requestRoomResultFromServer(final QueryDataModelController queryDataModelController, final int resultListType) {
		JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, queryDataModelController.makeHttpGetURL(),
				(String) null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Log.i(TAG, response.toString());
				endLoadingProgress();

				// create the resultFragment with the Interface ScrollTabHolder
				ResultFragment resultFragment = ResultFragment.newInstance(response.toString(),
						queryDataModelController.getDate(), resultListType);
				// end of creation of the _timelineFragment

				// commit the resultFragment to the current view
				_beginFragmentTransaction(resultFragment, R.id.body_background);
				// end of commission

				_conditionDataStack.push(queryDataModelController);

				// pop tutorial if the use of the application is first time or right after any application update
				_popTutorial();

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d(TAG, error.toString());
				endLoadingProgress();
				Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
			}
		});

		startLoadingProgress();
		ServerCommunicationManager.getInstance(this).addToRequestQueue(getRequest);
	}

	private void _checkApplicationVersion() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		int appVersion = sharedPreferences.getInt(getResources().getString(R.string.pref_app_ver), 0);

		Log.d(TAG, "Application Version Check: " + appVersion);
		if (appVersion < APPLICATION_VERSION) {
			editor.putInt(getResources().getString(R.string.pref_app_ver), APPLICATION_VERSION);
			editor.putBoolean(getResources().getString(R.string.pref_first_use), true);
			editor.commit();

			_searchTurtorialPopped = false;
			_resultPageTutorialPopped = false;
			_specificInfoPageTutorialPopped = false;
			//GuideFragment guideFragment = GuideFragment.newInstance();
			//_beginFragmentTransaction(guideFragment, R.id.body_background);
		} else {
			editor.putBoolean(getResources().getString(R.string.pref_first_use), false);
			editor.commit();
			return;
		}
	}

	private void _callCalendarDialogFragment(int callerFlag) {
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

		switch (id) {
			case R.id.action_version_check:
				/*VersionCheckFragment versionCheckFragment = VersionCheckFragment.newInstance();
				_beginFragmentTransaction(versionCheckFragment, R.id.body_background);*/
				break;
			case R.id.action_help:
				GuideFragment guideFragment = GuideFragment.newInstance();
				_beginFragmentTransaction(guideFragment, R.id.body_background);
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
	public void onCreateResultFragmentView(int numRoom, int listType) {
		Log.d("ResultFragemnt Life Cycle: ", "onCreateResultFragmentView");

		_currentPage = _pageStateStack.peekFirst();

		if(listType == ResultFragment.LIST_OF_ROOMS_AFTER_SEARCH) {
			_popTutorial();

			_mHeader.setVisibility(View.VISIBLE);
			_subTabLinearLayout.setVisibility(View.VISIBLE);
			_headerToggleImageViewFrameLayout.setVisibility(View.VISIBLE);

			// configure actionbar and query header for the result page
			_changeActionBarStyle(getResources().getColor(R.color.mtplease_color_primary), getResources().getString(R.string.results), String.valueOf(numRoom) + "개의 방");
			_subTabLinearLayout.setBackgroundColor(getResources().getColor(R.color.mtplease_subtab_background_color));

			if (numRoom < 2) {
				_mHeader.setTranslationY(0);
				_subTabLinearLayout.setAlpha(0.0F);
				_headerToggleImageViewFrameLayout.setAlpha(0.0F);
			} else {
				_mHeader.setTranslationY(-_minHeaderHeightForResultFragment);
				_subTabLinearLayout.setAlpha(1.0F);
				_headerToggleImageViewFrameLayout.setAlpha(1.0F);
			}
			// end of the configuration

			// if header was opened before searching, translate toggle button vertically with the height of the subtab bar
			if (_isHeaderOpened) {
				_isHeaderOpened = false;
				_headerToggleImageViewFrameLayout.animate().translationYBy(getResources().getDimension(R.dimen.query_status_bar_height));
				_headerToggleImageView.animate().rotationBy(180F);
			}

			/*if (_isBackButtonPressed) {
				Log.d(TAG, "back button clicked");
				queryDataModelController = _conditionDataStack.peekFirst();
			} else {
				Log.d(TAG, "back button not clicked");
				queryDataModelController = _getUserInputData();
			}*/
			QueryDataModelController queryDataModelController = _conditionDataStack.peekFirst();

			String queryString = "";
			// configure subtab for result page
			if (queryDataModelController.getFlag() == CONDITION_SEARCH_MODE) {
//			_switchSearchMode(queryDataModelController.getFlag());
				queryString += getResources().getString(R.string.we) + " ";
				queryString += queryDataModelController.getDateWrittenLang() + getResources().getString(R.string.postposition_1);
				_dateSelectButton.setText(queryDataModelController.getDateWrittenLang());
				int regionCode = queryDataModelController.getRegion();
				switch (regionCode) {
					case 1:
						queryString += " " + getResources().getString(R.string.daesungri);
						_regionSelectSpinner.setSelection(0);
						break;
					case 2:
						queryString += " " + getResources().getString(R.string.cheongpyung);
						_regionSelectSpinner.setSelection(1);
						break;
					case 3:
						queryString += " " + getResources().getString(R.string.gapyung);
						_regionSelectSpinner.setSelection(2);
						break;
				}
				queryString += " " + queryDataModelController.getPeople() + getResources().getString(R.string.postposition_2);
				_numberOfPeopleSelectEditText.setText(String.valueOf(queryDataModelController.getPeople()));

				queryString += " " + getResources().getString(R.string.go_MT);
			} else {
//			_switchSearchMode(queryDataModelController.getFlag());
				_keywordInputEditText.setText(queryDataModelController.getKeyword());
				queryString += "\"" + queryDataModelController.getKeyword() + "\"" + getResources().getString(R.string.results_for);
			}

			_querySubtabText.setText(queryString);
			// end of the configuration
		} else {
			QueryDataModelController queryDataModelController = _conditionDataStack.peekFirst();

			_mHeader.setVisibility(View.GONE);
			_subTabLinearLayout.setVisibility(View.GONE);
			_headerToggleImageViewFrameLayout.setVisibility(View.GONE);

			// configure actionbar and query header for the result page
			_changeActionBarStyle(getResources().getColor(R.color.mtplease_color_primary), queryDataModelController.getKeyword(), String.valueOf(numRoom) + "개의 방");
		}
	}

	@Override
	public void onResumeResultFragmentView(LinearLayoutManager layoutManager, int defaultPosition) {
		if (_isBackButtonPressed) {
			Log.d(TAG, "firstVisibleItemPosition: " + _firstVisibleItemPosition);
			if (_firstVisibleItemPosition == 0 || _firstVisibleItemPosition == -1) {
				_mHeader.setTranslationY(0);
				_subTabLinearLayout.setAlpha(0.0F);
				_headerToggleImageViewFrameLayout.setAlpha(0.0F);
			}
			layoutManager.scrollToPosition(_firstVisibleItemPosition);
		} else {
			layoutManager.scrollToPosition(defaultPosition);
		}

		_isBackButtonPressed = false;
	}

	@Override
	public void onDestroyResultFragmentView() {
		Log.d("ResultFragemnt Life Cycle: ", "onDestroyResultFragmentView");
		if (_isBackButtonPressed) {
			_firstVisibleItemPosition = _conditionDataStack.pop().getLastRoomCardPosition();
			_pageStateStack.pop();
			_restorePreviousPageState();
		}
		_mHeader.setVisibility(View.GONE);
		_headerToggleImageViewFrameLayout.setVisibility(View.GONE);
	}

	@Override
	public void onPreLoadSpecificInfoFragment() {
		startLoadingProgress();
	}

	@Override
	public void onLoadSpecificInfoFragment(RoomInfoModelController roomInfoModelController, JSONArray roomArray) {
		SpecificInfoFragment specificInfoFragment = SpecificInfoFragment.newInstance(roomInfoModelController, roomArray.length());

		// commit the SpecificInfoFragment to the current view
		_beginFragmentTransaction(specificInfoFragment, R.id.body_background);
		// end of commission
	}

	@Override
	public void onPostLoadSpecificInfoFragment() {
		endLoadingProgress();
	}

	@Override
	public void onCreateTimelineFragmentView(String roomCount) {

		_currentPage = _pageStateStack.peekFirst();

		_conditionDataStack.clear();

		_mHeader.setVisibility(View.VISIBLE);

		_subTabLinearLayout.setVisibility(View.INVISIBLE);
//		_roomCountText.setVisibility(View.VISIBLE);

		_changeActionBarStyle(getResources().getColor(R.color.mtplease_color_primary),
				getResources().getString(R.string.actionbar_title), roomCount + getResources().getString(R.string.with_number_of_rooms));

		_mHeader.setTranslationY(0);

		_isBackButtonPressed = false;
	}

	@Override
	public void onDestroyTimelineFragmentView() {
//		_roomCountText.setVisibility(View.GONE);
		if(_isBackButtonPressed) {
			_pageStateStack.pop();
		}
		_mHeader.setVisibility(View.GONE);
	}

	@Override
	public void onRefreshTimelineFragment(SwipeRefreshLayout swipeRefreshLayout) {
		_loadTimelineFragment(swipeRefreshLayout);
	}

	@Override
	public void adjustScroll(int scrollHeight) {
	}

	@Override
	public void onScroll(RecyclerView recyclerView, int firstVisibleItem, int pagePosition, int visibleFragment) {
		_firstVisibleItemPosition = firstVisibleItem;
		int scrollY = getScrollY(recyclerView, firstVisibleItem);
		float ratio;
		switch (visibleFragment) {
			case TIMELINE_FRAGMENT_VISIBLE:
				_clampValue = _changeHeaderTranslation(scrollY, _minHeaderHeightForTimelineFragment);
				return;
			case RESULT_FRAGMENT_VISIBLE:
				if (!_isHeaderOpened) {
					_clampValue = _changeHeaderTranslation(scrollY, _minHeaderHeightForResultFragment);
					_subTabLinearLayout.setAlpha(_clampValue);
					_headerToggleImageViewFrameLayout.setAlpha(_clampValue);
					//subTabBackgroundColor.setAlpha((int) (_clampValue * 255));
				} else {
					if (scrollY == 0) {
						_headerToggleImageViewFrameLayout.setAlpha(0.0F);
						_headerToggleImageView.animate().rotationBy(180F);
						_headerToggleImageViewFrameLayout.animate().translationYBy(getResources().getDimension(R.dimen.query_status_bar_height));
						_isHeaderOpened = false;
					}
				}
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

		//_setTitleAlpha(_clampValue, visibleFragment);
	}

	private float _changeHeaderTranslation(int scrollY, int minHeaderHeight) {
		_minHeaderTranslation = -minHeaderHeight;

		//Log.i(TAG, -scrollY + "dp / " + _minHeaderTranslation + "dp");

		_mHeader.setTranslationY(Math.max(-scrollY, _minHeaderTranslation));

		_actionBarQueryHeaderLinearLayout.setTranslationY(Math.max(-scrollY, _minHeaderTranslation));

		float ratio = clamp(_mHeader.getTranslationY() / _minHeaderTranslation, 0.0F, 1.0F);

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

	private void _setTitleAlpha(float alpha, int visibleFragment) {
		_alphaForegroundColorSpan.setAlpha(alpha);

		switch (visibleFragment) {
			case TIMELINE_FRAGMENT_VISIBLE:
				_spannableStringAppTitle.setSpan(_alphaForegroundColorSpan, 0, _spannableStringAppTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				//_actionBar.setTitle(_spannableStringAppTitle);
				break;
			case RESULT_FRAGMENT_VISIBLE:
				break;
			case SPECIFIC_INFO_FRAGMENT_VISIBLE:
				/*_spannableStringVariable = new SpannableString(getSupportActionBar().getTitle());
				_spannableStringVariable.setSpan(_alphaForegroundColorSpan, 0, _spannableStringVariable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				_actionBar.setTitle(_spannableStringVariable);
				_spannableStringVariable = null;
				_spannableStringVariable = new SpannableString(getSupportActionBar().getSubtitle());
				_spannableStringVariable.setSpan(_alphaForegroundColorSpan, 0, _spannableStringVariable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				_actionBar.setSubtitle(_spannableStringVariable);*/
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
					_dateSelectButton.setText(_modifiedDate);
					break;
				case CALL_FROM_PLAN:
					_dateSelectPlanButton.setText(_modifiedDate);
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

	private QueryDataModelController _getUserInputData() throws NumberFormatException {

		QueryDataModelController queryDataModelController = new QueryDataModelController(this);

		queryDataModelController.setLastRoomCardPosition(_firstVisibleItemPosition);

		if (_searchMode == CONDITION_SEARCH_MODE) {
			String regionName = (String) _regionSelectSpinner.getSelectedItem();

			if (regionName.equals(getResources().getString(R.string.daesungri))) {
				queryDataModelController.setRegion(INDEX_OF_DAESUNGRI + 1);
			} else if (regionName.equals(getResources().getString(R.string.cheongpyung))) {
				queryDataModelController.setRegion(INDEX_OF_CHEONGPYUNG + 1);
			} else {
				queryDataModelController.setRegion(INDEX_OF_GAPYUNG + 1);
			}


			queryDataModelController
					.setPeople(Integer.parseInt(_numberOfPeopleSelectEditText.getText().toString()));
			Log.i(TAG, queryDataModelController.getPeople() + "");


			queryDataModelController.setDateWrittenLang(_modifiedDate);

			String[] tmp = _modifiedDate.split(" ");
			queryDataModelController.setDate(tmp[0].substring(0, 4) + "-"
					+ tmp[1].split("월")[0] + "-" + tmp[2].split("일")[0]);
			Log.i(TAG, queryDataModelController.getDate());

			queryDataModelController.setFlag(CONDITION_SEARCH_MODE);
		} else {
			queryDataModelController.setDate(_calendar.get(Calendar.YEAR) + "-" + (_calendar.get(Calendar.MONTH) + 1)
					+ "-" + _calendar.get(Calendar.DATE));
			queryDataModelController.setKeyword(_keywordInputEditText.getText().toString());
			queryDataModelController.setFlag(KEYWORD_SEARCH_MODE);
		}

		return queryDataModelController;
	}

	@Override
	public void onCreateSpecificInfoFragmentView(String roomName, String pensionName) {
		_mHeader.setVisibility(View.GONE);

		_currentPage = _pageStateStack.peekFirst();

		_popTutorial();

		// configure actionbar for specific info page
		_changeActionBarStyle(getResources().getColor(R.color.mtplease_color_primary), roomName, pensionName);
		// end of the configuration
		// **********************actionbar button issue...........

		_isBackButtonPressed = false;
	}

	@Override
	public void onDestroySpecificInfoFragmentView() {
		if(_isBackButtonPressed)
			_pageStateStack.pop();
	}

	@Override
	public void onClickAddRoomToPlanButton(final RoomInfoModelController roomInfoModelController) {

		// check rather room has been added already in the plan or not
		if (_planModelController.isRoomAddedAlready(roomInfoModelController.getPen_id(),
				roomInfoModelController.getRoom_name(), roomInfoModelController.getRoom_cost())) {
			Toast.makeText(this, R.string.room_already_added_before, Toast.LENGTH_LONG).show();
			return;
		}

		int roomDataCount = _planModelController.getRoomDataCount();
		final int roomPlanViewIndex = roomDataCount;
		// "roomDataCount + FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN" -> used this notation, because
		// there is a gap, between first index of the roomDataLinkedList(inside PlanModel class)
		// and first index of the _roomPlanLinearLayout(starts the index from 2), which is 2.
		final View roomPlanView = getLayoutInflater().inflate(R.layout.frame_room_selected_plan,
				(LinearLayout) findViewById(R.id.layout_room), false);
		_addViewAtPlan(_roomPlanLinearLayout, roomPlanView, roomPlanViewIndex);

		// configure the room delete button
		ImageView deleteRoomButtonImageView =
				(ImageView) roomPlanView.findViewById(R.id.imageView_btn_delete_room_plan);
		deleteRoomButtonImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_planItemFadeOut(roomPlanView);

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
			roomPricePlanTextView.setText(_castItemPriceToString(roomPrice));
		}

		// store room data in the mPlanModel
		_planModelController.addRoomData(roomInfoModelController.getPen_id(),
				roomInfoModelController.getRoom_name(), roomInfoModelController.getRoom_cost());

		// calculate the total cost for the added rooms
		_totalRoomPrice.setText(_castItemPriceToString(_planModelController.getTotalRoomCost()));

		// calculate the total cost for the whole plan
		_totalPlanCost.setText(_castItemPriceToString(_planModelController.getPlanTotalCost()));

		// notify room is added to the plan to the user
		Toast.makeText(this, R.string.added_to_plan, Toast.LENGTH_LONG).show();

		// show plan
		_doubleSideSlidingMenu.showSecondaryMenu(true);

		/*_conditionDataForRequest = _getUserInputData();

		if (_conditionDataForRequest.getFlag() == CONDITION_SEARCH_MODE) {
			AddToPlanDialogFragment addToPlanDialogFragment =
					AddToPlanDialogFragment.newInstance(_conditionDataForRequest.getDateWrittenLang(),
							_conditionDataForRequest.getRegion() - 1,
							_conditionDataForRequest.getPeople(), CONDITION_SEARCH_MODE);
			addToPlanDialogFragment.show(getSupportFragmentManager(), "addtoplan_dialog_popped");
		}*/
	}

	@Override
	public void onClickSeeOtherRoomsButton(String pensionName) {
		QueryDataModelController queryDataModelController = new QueryDataModelController(this);

		queryDataModelController.setLastRoomCardPosition(_firstVisibleItemPosition);
		queryDataModelController.setKeyword(pensionName);
		queryDataModelController.setDate(_calendar.get(Calendar.YEAR) + "-" + (_calendar.get(Calendar.MONTH) + 1)
				+ "-" + _calendar.get(Calendar.DATE));
		queryDataModelController.setFlag(KEYWORD_SEARCH_MODE);

		_requestRoomResultFromServer(queryDataModelController, ResultFragment.LIST_OF_ROOMS_OF_PENSION);
	}

	private void _addViewAtPlan(ViewGroup parentView, View viewToBeAdded, int index) {
		parentView.addView(viewToBeAdded, index);
	}

	private void _addDirectInputRoomPriceToPlan(final int directInputRoomPrice) {

		final int directInputRoomDataCount = _planModelController.getRoomDataCount();
		final int directInputRoomPlanViewIndex = directInputRoomDataCount;
		// "directInputRoomDataCount + FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN" -> used this notation, because
		// there is a gap, between first index of the roomDataLinkedList(inside PlanModel class)
		// and first index of the _roomPlanLinearLayout(starts the index from 2), which is 2.
		final View directInputRoomPlanView = getLayoutInflater().inflate(R.layout.frame_room_direct_input_plan,
				(LinearLayout) findViewById(R.id.layout_room_direct_input), false);
		_addViewAtPlan(_directInputRoomPlanLinearLayout, directInputRoomPlanView, directInputRoomPlanViewIndex);

		// configure the room delete button
		ImageView deleteDirectInputRoomPlanButtonImageView =
				(ImageView) directInputRoomPlanView.findViewById(R.id.imageView_btn_delete_room_direct_input_plan);
		deleteDirectInputRoomPlanButtonImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_planItemFadeOut(directInputRoomPlanView);

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
		directInputRoomPricePlanTextView.setText(_castItemPriceToString(directInputRoomPrice));
		// end of the setting

		// store room data in the mPlanModel
		_planModelController.addDirectInputRoomData(directInputRoomPrice);

		// calculate the total cost for the added rooms
		_totalRoomPrice.setText(_castItemPriceToString(_planModelController.getTotalRoomCost()));

		// calculate the total cost for the whole plan
		_totalPlanCost.setText(_castItemPriceToString(_planModelController.getPlanTotalCost()));

		// notify room is added to the plan to the user
		Toast.makeText(this, R.string.added_to_plan, Toast.LENGTH_LONG).show();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			/*if(_doubleSideSlidingMenu.isSecondaryMenuShowing()) {
				_doubleSideSlidingMenu.toggle();
				return false;
			}

			if(_doubleSideSlidingMenu.isMenuShowing()) {
				_doubleSideSlidingMenu.toggle();
				return false;
			}

			if(_shoppingItemListFragment != null && _shoppingItemListFragment.isVisible()) {
				Log.d(TAG,"Visible");
				Log.d(TAG,_fragmentManager.getBackStackEntryCount() + "ea");
				_doubleSideSlidingMenu.showSecondaryMenu(true);
			}*/

			if (_currentPage == TIMELINE_PAGE_STATE) {
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

	private String _castItemPriceToString(int price) {
		String totalRoomCostString = String.valueOf(price);
		String totalRoomCostStringChanged = "";

		if (totalRoomCostString.length() > 0) {
			int charCounter = 0;
			for (int i = totalRoomCostString.length() - 1; i >= 0; i--) {
				if (charCounter != 0 && charCounter % 3 == 0) {
					totalRoomCostStringChanged += "," + totalRoomCostString.charAt(i);
				} else {
					totalRoomCostStringChanged += totalRoomCostString.charAt(i);
				}
				charCounter++;
			}
		}
		return getResources().getString(R.string.currency_unit) +
				new StringBuffer(totalRoomCostStringChanged).reverse().toString();
	}

	private void _changeActionBarStyle(int color, String titleText, String subtitleText) {
		/*_actionBarBackgroundColor = new ColorDrawable(color);
		_actionBar.setBackgroundDrawable(_actionBarBackgroundColor);*/

		//_actionBar.setTitle(titleText);
		//_actionBar.setSubtitle(subtitleText);
		_setActionBarTitle(titleText, subtitleText);
	}

	/*@Override
	public void onAddToPlanDialogFragmentViewDetached(String mtDate, int regionOfMT, int numberOfPeople, int sexRatioProgress) {
		*//*setAutoCompleteBasicInfoPlan(mtDate, regionOfMT, numberOfPeople, sexRatioProgress);*//*
		Toast.makeText(this, R.string.added_to_plan, Toast.LENGTH_LONG).show();
		_doubleSideSlidingMenu.showSecondaryMenu(true);
	}*/

	public void startLoadingProgress() {
		// configure progress bar
		_loadingProgressBar.setVisibility(View.VISIBLE);
		_loadingProgressBar.setProgress(0);
		_loadingBackground.setAlpha(100);
		// end of the configuration of the progress bar
	}

	public void endLoadingProgress() {
		// configure progress bar
		_loadingProgressBar.setVisibility(View.GONE);
		_loadingProgressBar.setProgress(100);
		_loadingBackground.setAlpha(0);
		// end of the configuration of the progress bar
	}

	private void _beginFragmentTransaction(Fragment targetFragment, int containerViewId) {

		FragmentTransaction fragmentTransaction = _fragmentManager.beginTransaction();

		if (targetFragment instanceof TimelineFragment) {
			fragmentTransaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_fade_in, R.anim.abc_fade_out);
			fragmentTransaction.replace(containerViewId, targetFragment);
		}/* else if (targetFragment instanceof VersionCheckFragment || targetFragment instanceof GuideFragment) {
			fragmentTransaction.add(containerViewId, targetFragment);
			fragmentTransaction.addToBackStack(null);
		}*/else if(targetFragment instanceof SpecificInfoFragment) {
			fragmentTransaction.setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out, R.anim.abc_fade_in, R.anim.abc_slide_out_bottom);
			fragmentTransaction.replace(containerViewId, targetFragment);
			fragmentTransaction.addToBackStack(null);
		} else {
			fragmentTransaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_fade_in, R.anim.abc_fade_out);
			fragmentTransaction.replace(containerViewId, targetFragment);
			fragmentTransaction.addToBackStack(null);
		}

		if (targetFragment instanceof TimelineFragment) {
			_pageStateStack.clear();
			_pageStateStack.push(TIMELINE_PAGE_STATE);
		} else if (targetFragment instanceof VersionCheckFragment) {
			_pageStateStack.push(VERSION_PAGE_STATE);
		} else if (targetFragment instanceof GuideFragment) {
			_pageStateStack.push(GUIDE_PAGE_STATE);
		} else if (targetFragment instanceof SettingsFragment) {
			_pageStateStack.push(SETTINGS_PAGE_STATE);
		} else if (targetFragment instanceof ResultFragment) {
			_pageStateStack.push(RESULT_PAGE_STATE);
		} else if (targetFragment instanceof SpecificInfoFragment) {
			_pageStateStack.push(SPECIFIC_INFO_PAGE_STATE);
		} else if (targetFragment instanceof ShoppingItemListFragment) {
			_pageStateStack.push(SHOPPINGITEMLIST_PAGE_STATE);
		}

		Log.i(TAG, "page stack state: " + _pageStateStack.toString());

		fragmentTransaction.commit();
	}

	@Override
	public void onCreateShoppingItemListFragmentView(int itemType, int numRoom) {

		_currentPage = _pageStateStack.peekFirst();

		_mHeader.setVisibility(View.GONE);
		// configure actionbar for specific info page
		String itemTypeString = null;

		switch (itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				itemTypeString = getResources().getString(R.string.meat);
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				itemTypeString = getResources().getString(R.string.alcohol);
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				itemTypeString = getResources().getString(R.string.others);
		}

		_changeActionBarStyle(getResources().getColor(R.color.mtplease_color_primary), itemTypeString, numRoom + getResources().getString(R.string.n_results));
		// end of the configuration

		_isBackButtonPressed = false;
	}

	@Override
	public void onDestroyShoppingItemListFragmentView() {
		if(_isBackButtonPressed)
			_pageStateStack.pop();
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
		if (_planModelController.isItemAddedAlready(itemType, itemName, itemUnitPrice)) {
			Toast.makeText(this, R.string.item_already_added_before, Toast.LENGTH_LONG).show();
			return;
		}

		int itemDataCount = _planModelController.getItemDataCount(itemType);
		final int itemPlanViewIndex = itemDataCount + FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN;
		// "itemDataCount + FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN" -> used this notation, because
		// there is a gap, between first index of the [itemData]LinkedList(inside PlanModel class)
		// and first index of the _roomPlanLinearLayout(starts the index from 2), which is 2.
		final View itemPlanView = getLayoutInflater().inflate(R.layout.frame_shopping_item_selected_plan,
				(LinearLayout) findViewById(R.id.layout_room), false);

		ImageView deleteItemButtonPlanImageView =
				(ImageView) itemPlanView.findViewById(R.id.imageView_btn_delete_item_plan);

		switch (itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				_addViewAtPlan(_meatPlanLinearLayout, itemPlanView, itemPlanViewIndex);
				deleteItemButtonPlanImageView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						_planItemFadeOut(itemPlanView);

						_undoToastController.showUndoBar(itemName + getResources().getString(R.string.deleted),
								UndoToastController.DELETE_ADDED_MEAT_CASE, itemPlanViewIndex);
//						_undoToastController.setItemViewAndData(itemPlanView, itemType, itemName, itemUnitPrice);
					}
				});
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				_addViewAtPlan(_alcoholPlanLinearLayout, itemPlanView, itemPlanViewIndex);
				deleteItemButtonPlanImageView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						_planItemFadeOut(itemPlanView);

						_undoToastController.showUndoBar(itemName + getResources().getString(R.string.deleted),
								UndoToastController.DELETE_ADDED_ALCOHOL_CASE, itemPlanViewIndex);
//						_undoToastController.setItemViewAndData(itemPlanView, itemType, itemName, itemUnitPrice);
					}
				});
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				_addViewAtPlan(_othersPlanLinearLayout, itemPlanView, itemPlanViewIndex);
				deleteItemButtonPlanImageView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						_planItemFadeOut(itemPlanView);

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
		itemUnitPricePlanTextView.setText(_castItemPriceToString(itemUnitPrice));

		TextView itemCountUnitPlanTextView =
				(TextView) itemPlanView.findViewById(R.id.textView_count_unit_item_plan);
		itemCountUnitPlanTextView.setText(itemCountUnit);

		final TextView itemTotalPricePlanTextView =
				(TextView) itemPlanView.findViewById(R.id.textView_price_total_item_plan);
		itemTotalPricePlanTextView.setText(_castItemPriceToString(itemUnitPrice * itemCount));

		final Button numberPickerCallButton =
				(Button) itemPlanView.findViewById(R.id.btn_number_picker_item_plan);
		numberPickerCallButton.setText(String.valueOf(itemCount));
		numberPickerCallButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_tempItemTotalPricePlanTextView = itemTotalPricePlanTextView;
				_tempNumberPickerCallButton = numberPickerCallButton;
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
		switch (itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				_totalMeatPrice.setText(_castItemPriceToString(_planModelController.getTotalItemCost(itemType)));
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				_totalAlcoholPrice.setText(_castItemPriceToString(_planModelController.getTotalItemCost(itemType)));
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				_totalOthersPrice.setText(_castItemPriceToString(_planModelController.getTotalItemCost(itemType)));
				break;
		}

		// calculate the total cost for the whole plan
		_totalPlanCost.setText(_castItemPriceToString(_planModelController.getPlanTotalCost()));

		// notify room is added to the plan to the user
		Toast.makeText(this, R.string.added_to_plan, Toast.LENGTH_SHORT).show();

		// show plan
		_doubleSideSlidingMenu.showSecondaryMenu(true);

	}

	@Override
	public void onClickChangeButton(int itemType, String itemName, int itemUnitPrice, int newItemCount) {
		_tempItemTotalPricePlanTextView.setText(_castItemPriceToString(itemUnitPrice * newItemCount));
		_tempNumberPickerCallButton.setText(String.valueOf(newItemCount));

		_planModelController.changeItemCount(itemType, itemName, itemUnitPrice, newItemCount);

		switch (itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				_totalMeatPrice.setText(_castItemPriceToString(_planModelController.getTotalItemCost(itemType)));
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				_totalAlcoholPrice.setText(_castItemPriceToString(_planModelController.getTotalItemCost(itemType)));
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				_totalOthersPrice.setText(_castItemPriceToString(_planModelController.getTotalItemCost(itemType)));
				break;
		}

		// calculate the total cost for the whole plan
		_totalPlanCost.setText(_castItemPriceToString(_planModelController.getPlanTotalCost()));
	}

	/**
	 * apply rate calculated by division of newly input number of people with previous number input
	 * to the number of shopping items already selected in the plan.
	 *
	 * @param oldNumPeople number of people input before
	 * @param newNumPeople number of people just input
	 */
	private void _applyNumberOfPeopleChangeToItems(int oldNumPeople, int newNumPeople) {
		float rate = newNumPeople / oldNumPeople;

		Log.d(TAG, "rate:" + rate);

		for (int itemType = 1; itemType < 4; itemType++) {
			for (int singleItemIndex = FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN;
				 singleItemIndex < FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN + _planModelController.getItemDataCount(itemType);
				 singleItemIndex++) {
				View singleShoppingItemLayout = null;
				Button numberPickerButton;
				int numPeople;
				try {
					switch (itemType) {
						case ShoppingItemListFragment.MEAT_ITEM:
							singleShoppingItemLayout = _meatPlanLinearLayout.getChildAt(singleItemIndex);
							break;
						case ShoppingItemListFragment.ALCOHOL_ITEM:
							singleShoppingItemLayout = _alcoholPlanLinearLayout.getChildAt(singleItemIndex);
							break;
						case ShoppingItemListFragment.OTHERS_ITEM:
							singleShoppingItemLayout = _othersPlanLinearLayout.getChildAt(singleItemIndex);
							break;
					}
					numberPickerButton = (Button) singleShoppingItemLayout.findViewById(R.id.btn_number_picker_item_plan);
					numPeople = Integer.parseInt(numberPickerButton.getText().toString());
					numPeople *= rate;
					Log.d(TAG, "result:" + numPeople);
					numberPickerButton.setText(String.valueOf(numPeople));
					_planModelController.changeItemCount(itemType, _planModelController.getItemName(itemType, singleItemIndex - FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN),
							_planModelController.getItemUnitPrice(itemType, singleItemIndex - FIRST_INDEX_OF_EACH_SHOPPING_ITEM_IN_PLAN), numPeople);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		_totalMeatPrice.setText(_castItemPriceToString(_planModelController.getTotalItemCost(ShoppingItemListFragment.MEAT_ITEM)));
		_totalAlcoholPrice.setText(_castItemPriceToString(_planModelController.getTotalItemCost(ShoppingItemListFragment.ALCOHOL_ITEM)));
		_totalOthersPrice.setText(_castItemPriceToString(_planModelController.getTotalItemCost(ShoppingItemListFragment.OTHERS_ITEM)));
		_totalPlanCost.setText(_castItemPriceToString(_planModelController.getPlanTotalCost()));
	}

	private void _deleteAddedRoomInPlan(int viewIndex, LinearLayout _roomPlanLinearLayout) {
		_roomPlanLinearLayout.removeViewAt(viewIndex);

		if (_roomPlanLinearLayout == this._roomPlanLinearLayout) {
			_planModelController.removeRoomData(viewIndex);
		} else {
			_planModelController.removeDirectInputRoomData(viewIndex);
		}
	}

	private void _deleteAddedItemInPlan(int viewIndex, int itemType, LinearLayout itemPlanLinearLayout, TextView totalItemPrice) {

		itemPlanLinearLayout.removeViewAt(viewIndex);

		_planModelController.removeItemData(itemType, viewIndex);

		// calculate the total cost for the added rooms after deletion of one room
		totalItemPrice.setText(_castItemPriceToString(_planModelController.getTotalItemCost(itemType)));

		// calculate the total cost for the whole plan
		_totalPlanCost.setText(_castItemPriceToString(_planModelController.getPlanTotalCost()));
	}

	@Override
	public void onClickUndoButton(int toastCase, final int viewIndex) {
		switch (toastCase) {
			case UndoToastController.DELETE_ADDED_ROOM_CASE:
				_planItemFadeIn(_roomPlanLinearLayout.getChildAt(viewIndex));
				break;
			case UndoToastController.DELETE_DIRECTLY_INPUT_ROOM_CASE:
				_planItemFadeIn(_directInputRoomPlanLinearLayout.getChildAt(viewIndex));
				break;
			case UndoToastController.DELETE_ADDED_MEAT_CASE:
				_planItemFadeIn(_meatPlanLinearLayout.getChildAt(viewIndex));
				break;
			case UndoToastController.DELETE_ADDED_ALCOHOL_CASE:
				_planItemFadeIn(_alcoholPlanLinearLayout.getChildAt(viewIndex));
				break;
			case UndoToastController.DELETE_ADDED_OTHERS_CASE:
				_planItemFadeIn(_othersPlanLinearLayout.getChildAt(viewIndex));
				break;
			case UndoToastController.CLEAR_ADDED_ROOMS_CASE:
				for (int roomIndex = 0; roomIndex < _planModelController.getRoomDataCount(); roomIndex++) {
					_planItemFadeIn(_roomPlanLinearLayout.getChildAt(roomIndex));
				}

				for (int directInputRoomIndex = 0;
					 directInputRoomIndex < _planModelController.getDirectInputRoomDataCount(); directInputRoomIndex++) {
					_planItemFadeIn(_directInputRoomPlanLinearLayout.getChildAt(directInputRoomIndex));
				}
				break;
			case UndoToastController.CLEAR_ADDED_MEATS_CASE:
				_makeClearedItemsInPlanVisible(ShoppingItemListFragment.MEAT_ITEM, _meatPlanLinearLayout);
				break;
			case UndoToastController.CLEAR_ADDED_ALCOHOLS_CASE:
				_makeClearedItemsInPlanVisible(ShoppingItemListFragment.ALCOHOL_ITEM, _alcoholPlanLinearLayout);
				break;
			case UndoToastController.CLEAR_ADDED_OTHERS_CASE:
				_makeClearedItemsInPlanVisible(ShoppingItemListFragment.OTHERS_ITEM, _othersPlanLinearLayout);
				break;
		}

	}

	@Override
	public void onTimePassed(int toastCase, int viewIndex) {
		switch (toastCase) {
			case UndoToastController.DELETE_ADDED_ROOM_CASE:
				_deleteAddedRoomInPlan(viewIndex, _roomPlanLinearLayout);
				break;
			case UndoToastController.DELETE_DIRECTLY_INPUT_ROOM_CASE:
				_deleteAddedRoomInPlan(viewIndex, _directInputRoomPlanLinearLayout);
				break;
			case UndoToastController.DELETE_ADDED_MEAT_CASE:
				_deleteAddedItemInPlan(viewIndex, ShoppingItemListFragment.MEAT_ITEM, _meatPlanLinearLayout, _totalMeatPrice);
				break;
			case UndoToastController.DELETE_ADDED_ALCOHOL_CASE:
				_deleteAddedItemInPlan(viewIndex, ShoppingItemListFragment.ALCOHOL_ITEM, _alcoholPlanLinearLayout, _totalAlcoholPrice);
				break;
			case UndoToastController.DELETE_ADDED_OTHERS_CASE:
				_deleteAddedItemInPlan(viewIndex, ShoppingItemListFragment.OTHERS_ITEM, _othersPlanLinearLayout, _totalOthersPrice);
				break;
			case UndoToastController.CLEAR_ADDED_ROOMS_CASE:
				for (int roomIndex = 0; roomIndex < _planModelController.getRoomDataCount(); roomIndex++)
					_roomPlanLinearLayout.removeViewAt(0);

				for (int directInputRoomIndex = 0;
					 directInputRoomIndex < _planModelController.getDirectInputRoomDataCount(); directInputRoomIndex++)
					_directInputRoomPlanLinearLayout.removeViewAt(0);

				_planModelController.clearRoomData();

				_totalRoomPrice.setText(_castItemPriceToString(_planModelController.getTotalRoomCost()));

				// calculate the total cost for the whole plan
				_totalPlanCost.setText(_castItemPriceToString(_planModelController.getPlanTotalCost()));
				break;
			case UndoToastController.CLEAR_ADDED_MEATS_CASE:
				_clearAddedItemsInPlan(ShoppingItemListFragment.MEAT_ITEM, _meatPlanLinearLayout, _totalMeatPrice);
				break;
			case UndoToastController.CLEAR_ADDED_ALCOHOLS_CASE:
				_clearAddedItemsInPlan(ShoppingItemListFragment.ALCOHOL_ITEM, _alcoholPlanLinearLayout, _totalAlcoholPrice);
				break;
			case UndoToastController.CLEAR_ADDED_OTHERS_CASE:
				_clearAddedItemsInPlan(ShoppingItemListFragment.OTHERS_ITEM, _othersPlanLinearLayout, _totalOthersPrice);
				break;
		}
	}

	private void _makeClearedItemsInPlanVisible(int itemType, LinearLayout itemPlanLinearLayout) {
		for (int itemIndex = 0;
			 itemIndex < _planModelController.getItemDataCount(itemType);
			 itemIndex++) {
			_planItemFadeIn(itemPlanLinearLayout.getChildAt(itemIndex));
		}
	}

	private void _clearAddedItemsInPlan(int itemType, LinearLayout itemPlanLinearLayout, TextView totalItemPrice) {
		for (int itemIndex = 0;
			 itemIndex < _planModelController.getItemDataCount(itemType);
			 itemIndex++)
			itemPlanLinearLayout.removeViewAt(itemIndex);

		_planModelController.clearItemData(itemType);

		totalItemPrice.setText(_castItemPriceToString(_planModelController.getTotalItemCost(itemType)));

		// calculate the total cost for the whole plan
		_totalPlanCost.setText(_castItemPriceToString(_planModelController.getPlanTotalCost()));
	}

	private void _planItemFadeOut(final View targetView) {
		targetView.animate().alpha(0).setListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				targetView.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}
		});
	}

	private void _planItemFadeIn(final View targetView) {
		targetView.setVisibility(View.VISIBLE);
		targetView.animate().alpha(1).setListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				targetView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}
		});
	}

	public void hideKeyboard(View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	@Override
	public void onClickModifyUserInfoIcon() {
		UserInfoModificationFragment userInfoModificationFragment = UserInfoModificationFragment.newInstance();

		_beginFragmentTransaction(userInfoModificationFragment, R.id.body_background);
	}

	@Override
	public void onCreateMyPageFragmentView() {
		_mHeader.setVisibility(View.GONE);
		// configure actionbar for my page
		_changeActionBarStyle(getResources().getColor(R.color.mtplease_color_primary), getResources().getString(R.string.my_page), null);
		// end of the configuration
		// **********************actionbar button issue...........

		_isBackButtonPressed = false;
	}

	@Override
	public void onDestroyMyPageFragmentView() {

	}

	@Override
	public void onCreateUserInfoModificationFragmentView() {
		_currentPage = _pageStateStack.peekFirst();

		_mHeader.setVisibility(View.GONE);
		// configure actionbar for user info modification page
		_changeActionBarStyle(getResources().getColor(R.color.mtplease_color_primary), getResources().getString(R.string.user_info_modification), null);
		// end of the configuration
		// **********************actionbar button issue...........

		_isBackButtonPressed = false;
	}

	@Override
	public void onDestroyUserInfoModificationFragmentView() {
		if(_isBackButtonPressed)
			_pageStateStack.pop();
	}

	@Override
	public void onClickModifyUserInfoButton(String[] schoolNSociety, String password, String nickname) {
		// send modified user info to the server
	}

	@Override
	public void onCreateVersionCheckFragmentView() {
		_actionBar.hide();
		_mHeader.setVisibility(View.GONE);
		_currentPage = _pageStateStack.peekFirst();
	}

	@Override
	public void onDestroyVersionCheckFragmentView() {
		if(_isBackButtonPressed) {
			_pageStateStack.pop();
			_restorePreviousPageState();
		}
		_actionBar.show();
		_mHeader.setVisibility(View.VISIBLE);
		_isBackButtonPressed = false;
	}

	@Override
	public void onCreateGuideFragmentView() {
		_actionBar.hide();
		_mHeader.setVisibility(View.GONE);
		_currentPage = _pageStateStack.peekFirst();
	}

	@Override
	public void popGuideFragment() {
		_isBackButtonPressed = true;
		_fragmentManager.popBackStackImmediate();
	}

	@Override
	public void onDestroyGuideFragmentView() {
		if(_isBackButtonPressed) {
			_pageStateStack.pop();
			_restorePreviousPageState();
		}
		_actionBar.show();
		_mHeader.setVisibility(View.VISIBLE);
		_isBackButtonPressed = false;
	}

	@Override
	public void onCreateSettingsFragmentView() {
		_setActionBarTitle(getResources().getString(R.string.action_settings), null);
		_mHeader.setVisibility(View.GONE);
		_currentPage = _pageStateStack.peekFirst();
	}

	@Override
	public void onDestroySettingsFragmentView() {
		if(_isBackButtonPressed) {
			_pageStateStack.pop();
			_restorePreviousPageState();
		}
		_mHeader.setVisibility(View.VISIBLE);
		_isBackButtonPressed = false;
	}

	@Override
	public void onLoadVersionCheckFragmentView() {
		VersionCheckFragment versionCheckFragment = VersionCheckFragment.newInstance();
		_beginFragmentTransaction(versionCheckFragment, R.id.body_background);
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

			regionName.setTypeface(_mTypeface);
			regionName.setText(regionNameList.get(position));

			return spinnerItemView;
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View spinnerItemView = getLayoutInflater().inflate(R.layout.spinner_region, parent, false);

			TextView regionName = (TextView) spinnerItemView.findViewById(R.id.spinner_item);

			regionName.setTypeface(_mTypeface);
			regionName.setText(regionNameList.get(position));

			return spinnerItemView;
		}
	}

	private void _restorePreviousPageState() {
		Log.i(TAG, "page stack state: " + _pageStateStack.toString());
		Log.d(TAG,  "_restorePreviousPageState(): " + "previous page: " +_pageStateStack.peekFirst());
		switch(_pageStateStack.peekFirst()) {
			case TIMELINE_PAGE_STATE:
			case RESULT_PAGE_STATE:
				_toggleMenuButton(R.id.layout_btn_home);
				break;
			case SPECIFIC_INFO_PAGE_STATE:
				_toggleMenuButton(R.id.layout_btn_home);
				break;
			case GUIDE_PAGE_STATE:
				_toggleMenuButton(R.id.layout_btn_help);
				break;
			case SETTINGS_PAGE_STATE:
				_toggleMenuButton(R.id.layout_btn_setting);
				break;
		}
	}

	private void _popTutorial() {
		if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getResources().getString(R.string.pref_first_use), false)) {

			Log.d(TAG, "_currentPage: " + _currentPage + " / _search: " + _searchTurtorialPopped
					+ " / _resultPage: " + _resultPageTutorialPopped + " / _specificInfoPage: " + _specificInfoPageTutorialPopped);

			switch (_currentPage) {
				case TIMELINE_PAGE_STATE:
					if(!_searchTurtorialPopped) {
						_actionBar.hide();
						_splashScreenLayout.setBackgroundResource(R.drawable.scrn_tutorial_2);
						_splashScreenLayout.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								v.setBackgroundResource(R.drawable.scrn_tutorial_3);
								v.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										v.setBackgroundResource(R.drawable.scrn_tutorial_4);
										v.setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												v.setVisibility(View.GONE);
												v.setOnClickListener(null);
												_actionBar.show();
												_doubleSideSlidingMenu.toggle(true);
											}
										});
									}
								});
							}
						});
						_searchTurtorialPopped = true;
					} else return;
					break;
				case RESULT_PAGE_STATE:
					if(!_resultPageTutorialPopped) {
						_setTutorial(R.drawable.scrn_tutorial_5);
						_resultPageTutorialPopped = true;
					} else return;
					break;
				case SPECIFIC_INFO_PAGE_STATE:
					if(!_specificInfoPageTutorialPopped) {
						_setTutorial(R.drawable.scrn_tutorial_6);
						_specificInfoPageTutorialPopped = true;
					} else return;
					break;
				default:
					return;
			}

			_splashScreenLayout.setVisibility(View.VISIBLE);
			for(int i = 0; i < _splashScreenLayout.getChildCount(); i++)
				_splashScreenLayout.getChildAt(i).setVisibility(View.GONE);
		}
	}

	private void _setTutorial(int resid) {
		_actionBar.hide();
		_splashScreenLayout.setBackgroundResource(resid);
		_splashScreenLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.GONE);
				v.setOnClickListener(null);
				_actionBar.show();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// clear cached files
		//ServerCommunicationManager.getInstance(this).clearCache();
	}
}
