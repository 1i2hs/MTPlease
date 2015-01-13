package com.owo.mtplease;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import notboringactionbar.AlphaForegroundColorSpan;


public class MainActivity extends ActionBarActivity implements ScrollTabHolder,TimelineFragment.OnFragmentInteractionListener, ResultFragment.OnFragmentInteractionListener, CalendarDialogFragment.OnDateConfirmedListener {

    // Fragments
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    TimelineFragment mTimelineFragment;
    ResultFragment mResultFragment;
    // End of Fragments
    // Strings
    private static final String TAG = "MainActivity";
    private static final int CONDITION_SEARCH_MODE = 1;
    private static final int KEYWORD_SEARCH_MODE = 2;
    private static final int NETWORK_CONNECTION_FAILED = -1;
    private static final int TIMELINE = 1;
    private static final int RESULT = 2;
    private static final String MTPLEASE_URL = "http://mtplease.herokuapp.com/";
    // End of Strings

    // Graphical transitions
    private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
    private SpannableString mSpannableString;
    private ColorDrawable actionbarBackgroundColor;
    private int mMinHeaderHeightForTimelineFragment;
    private int mMinHeaderHeightForResultFragment;
    private int mHeaderHeight;
    private int mMinHeaderTranslation;
    // End of the Graphical transitions

    // User Interface Views
    private ActionBar mActionbar;
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
    // End of User Interface Views

    // Others
    private Calendar calendar = Calendar.getInstance();
    private ConditionDataForRequest mConditionDataForRequest;
    private ScrollTabHolder mScrollTabHolder = this;
    private float clampValue;

    public static float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // configure the Actionbar
        mActionbar = getSupportActionBar();

        actionbarBackgroundColor = new ColorDrawable(Color.BLACK);
        // end of the configuration

        mFragmentManager = getSupportFragmentManager();

        // configure the SlidingMenu
        SlidingMenu menu = new SlidingMenu(this);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu.setMode(SlidingMenu.LEFT_RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setSecondaryShadowDrawable(R.drawable.shadowright);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.70f);
        menu.setMenu(R.layout.menu_side);
        menu.setSecondaryMenu(R.layout.resume_side);
        // end of configuration of the SlidingMenu

        // configure the NotBoringActionbar
        mMinHeaderHeightForTimelineFragment = getResources().getDimensionPixelSize(R.dimen.min_header_height);
        mMinHeaderHeightForResultFragment = getResources().getDimensionPixelOffset(R.dimen.header_height);
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mHeader = findViewById(R.id.header);
        mSpannableString = new SpannableString(getString(R.string.actionbar_title));
        mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(0xffffffff);
        // end of configuration of NotBoringActionbar

        // configure the side menu buttons
        settingButton = (TextView) findViewById(R.id.btn_menu_setting);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(v.getContext(), SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });
        // end of configuration of the side menu buttons

        // configure the conditional query UIs
        dateSelectButton = (Button) findViewById(R.id.btn_select_date);
        modifiedDate = calendar.get(Calendar.YEAR) + "년 " + (calendar.get(Calendar.MONTH) + 1) + "월 " + calendar.get(Calendar.DATE) + "일";
        dateSelectButton.setText(modifiedDate);
        dateSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDialogFragment calendarDialogFragment = new CalendarDialogFragment();
                calendarDialogFragment.show(mFragmentManager,
                        "calendar_dialog_popped");
            }
        });

        regionSelectSpinner = (Spinner) findViewById(R.id.spinner_select_region);
        ArrayAdapter<CharSequence> regionSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_region, R.layout.spinner_region);
        regionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionSelectSpinner.setAdapter(regionSpinnerAdapter);

        numberOfPeopleSelectEditText = (EditText) findViewById(R.id.editText_select_number_people);

        roomSearchButton = (ImageButton) findViewById(R.id.btn_search_room);
        roomSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Log.i(TAG, mConditionDataForRequest.makeHttpGetURL());
                    new ServerConnectionTask(mScrollTabHolder, RESULT).execute(getUserInputData().makeHttpGetURL());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                   Toast.makeText(MainActivity.this, R.string.notify_number_of_people_input, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        roomCountText = (TextView) findViewById(R.id.text_room_count);

        dateQuerySubTabText = (TextView) findViewById(R.id.text_query_date);

        regionQuerySubTabText = (TextView) findViewById(R.id.text_query_region);

        numberOfPeopleQuerySubTabText = (TextView) findViewById(R.id.text_query_number_people);

        loadingLayout = (FrameLayout) findViewById(R.id.background_loading);
        loadingBackground = loadingLayout.getBackground();
        loadingBackground.setAlpha(0);

        loadingProgressBar = (ProgressBar) findViewById(R.id.progressBar_condition_search);
        loadingProgressBar.setVisibility(View.GONE);
        // end of the configuration of the conditional query UIs

        // get timeline data from the server and create the mTimelineFragment
        new ServerConnectionTask(mScrollTabHolder, TIMELINE).execute(MTPLEASE_URL);
        // end of getting the data from the server and the creation of the fragment
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
    public void onResultFragmentInteraction(Uri uri) {

    }

    @Override
    public void onTimelineFragmentInteraction(Uri uri) {

    }

    @Override
    public void adjustScroll(int scrollHeight) {

    }

    @Override
    public void onScroll(RecyclerView recyclerView, int firstVisibleItem, int pagePosition) {
        int scrollY = getScrollY(recyclerView, firstVisibleItem);
        if(mTimelineFragment.isVisible()) {
            mMinHeaderTranslation = -mMinHeaderHeightForTimelineFragment + getSupportActionBar().getHeight();
            mHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));

        } else if(mResultFragment.isVisible()) {
            mMinHeaderTranslation = -mMinHeaderHeightForResultFragment + getSupportActionBar().getHeight();
            mHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
        }
        float ratio = clamp(mHeader.getTranslationY() / mMinHeaderTranslation, 0.0f, 1.0f);
        clampValue = clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F);
        setTitleAlpha(clampValue);

        actionbarBackgroundColor.setAlpha((int)(clampValue * 255));
        getSupportActionBar().setBackgroundDrawable(actionbarBackgroundColor);
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

    private void setTitleAlpha(float alpha) {
        mAlphaForegroundColorSpan.setAlpha(alpha);
        mSpannableString.setSpan(mAlphaForegroundColorSpan, 0, mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(mSpannableString);
    }

    @Override
    public void onDateConfirmButtonClicked(String dateSelected) {
        if(dateSelected != null) {
            modifiedDate = dateSelected;
            dateSelectButton.setText(modifiedDate);
        }
    }

    public ConditionDataForRequest getUserInputData() {
        ConditionDataForRequest conditionDataForRequest = new ConditionDataForRequest();

        String region = regionSelectSpinner.getSelectedItem()
                .toString();
        if (region.equals("대성리로")) {
            conditionDataForRequest.region = 1;
        } else if (region.equals("청평으로")) {
            conditionDataForRequest.region = 2;
        } else {
            conditionDataForRequest.region = 3;
        }

        conditionDataForRequest.people = Integer.parseInt(numberOfPeopleSelectEditText.getText().toString());
        Log.i(TAG, conditionDataForRequest.people + "");

        String[] tmp = (modifiedDate).split(" ");
        conditionDataForRequest.date = tmp[0].substring(0, 4) + "-"
                + tmp[1].split("월")[0] + "-" + tmp[2].split("일")[0];
        Log.i(TAG, conditionDataForRequest.date);

        conditionDataForRequest.flag = CONDITION_SEARCH_MODE;

        return conditionDataForRequest;
    }

    private class ConditionDataForRequest {
        public int region;
        public String date;
        public int people;
        public int flag;

        public ConditionDataForRequest() {
            this.region = -1;
            this.date = null;
            this.people = -1;
            this.flag = -1;
        }

        public boolean isVariableSet() {
            // ************************************************************string compare reminded!!
            if(this.region != -1 && this.date != null && this.people != -1 && this.flag != -1)
                return true;
            else
                return false;
        }

        public String makeHttpGetURL() {
            if(isVariableSet()) {
                String httpGetURL = "http://mtplease.herokuapp.com/" + "pensions"+ "?region=" + region + "&date='"
                        + date + "'&people=" + people +"&flag=1";

                return httpGetURL;
            }
            return null;
        }
    }

    /**
     * @author In-Ho
     * AsyncTask for receiving data from the server.
     * Used AsyncTask to perform the task in the background
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
                HttpClient mHttpClient = new DefaultHttpClient();
                HttpGet mHttpGet = new HttpGet(urls[0]);
                HttpConnectionParams.setConnectionTimeout(mHttpClient.getParams(), 5000);
                HttpResponse mHttpResponseGet = mHttpClient.execute(mHttpGet);
                HttpEntity resEntityGet = mHttpResponseGet.getEntity();

                if(resEntityGet != null) {
                    Log.i(TAG,"HttpResponseGet Completed!!");
                    return EntityUtils.toString(resEntityGet);
                }
            } catch(Exception e) {
                Log.i(TAG,"HttpResponseGet Failed....");
                requestLabel = NETWORK_CONNECTION_FAILED;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);
            switch(requestLabel) {
                case TIMELINE:
                    // create the mTimelineFragment with the Interface ScrollTabHolder
                    try {
                        dateQuerySubTabText.setVisibility(View.INVISIBLE);
                        regionQuerySubTabText.setVisibility(View.INVISIBLE);
                        numberOfPeopleQuerySubTabText.setVisibility(View.INVISIBLE);

                        JSONObject mJSONObject = new JSONObject(jsonString);
                        // redundant to code. needs to be refactored
                        mJSONObject = mJSONObject.getJSONObject("main");

                        roomCountText.setText(new JSONArray(mJSONObject.getString("room_cnt")).getJSONObject(0).getString("room_cnt") + "개의 방");

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
                    dateQuerySubTabText.setVisibility(View.VISIBLE);
                    regionQuerySubTabText.setVisibility(View.VISIBLE);
                    numberOfPeopleQuerySubTabText.setVisibility(View.VISIBLE);

                    // create the mTimelineFragment with the Interface ScrollTabHolder
                    mResultFragment = ResultFragment.newInstance(jsonString);
                    mResultFragment.setScrollTabHolder(mScrollTabHolder);
                    // end of creation of the mTimelineFragment

                    // commit the mTimelineFragment to the current view
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.replace(R.id.body, mResultFragment);
                    mFragmentTransaction.addToBackStack(null);
                    mFragmentTransaction.commit();
                    // end of commission

                    mConditionDataForRequest = getUserInputData();
                    dateQuerySubTabText.setText(mConditionDataForRequest.date);
                    int regionCode = mConditionDataForRequest.region;
                    switch(regionCode) {
                        case 1:
                            regionQuerySubTabText.setText("대성리");
                            break;
                        case 2:
                            regionQuerySubTabText.setText("청평");
                            break;
                        case 3:
                            regionQuerySubTabText.setText("가평");
                            break;
                    }
                    numberOfPeopleQuerySubTabText.setText(getUserInputData().people + "명");

                    break;
                case NETWORK_CONNECTION_FAILED:
                    Toast.makeText(MainActivity.this, R.string.notify_network_error,Toast.LENGTH_SHORT);
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
