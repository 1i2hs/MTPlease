package com.owo.mtplease;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.Vector;

/**
 * Created by In-Ho on 2015-01-14.
 */
public class SpecificInfoRoomRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final String TAG = "SpecificInfoRoomRecyclerViewAdapter";
	private static final String MTPLEASE_URL = "http://mtplease.herokuapp.com/";

	// Series of cards
	private static final int CARD_IMAGE_VIEW_PAGER = 0;
	private static final int CARD_PHONE_AND_WEBSITE_BUTTONS = 1;
	private static final int CARD_PRICE_AND_RATE = 2;
	private static final int CARD_BASIC_INFO = 3;
	private static final int CARD_OPTIONS = 4;
	private static final int CARD_VISITED_SCHOOLS = 5;
	private static final int CARD_REVIEWS = 6;
	private static final int CARD_ADDRESS_AND_ROUTE_AND_MAP = 7;
	private static final int CARD_PRICE_AND_DATE_SELECTION = 8;
	private static final int CARD_OWNER_INFO = 9;
	private static final int CARD_OTHERS = 10;
	private static final int CARD_WARNINGS = 11;
	// End of series of cards

	// Model: Data variables for User Interface Views
	private JSONArray roomInfoArray;
	private RoomInfoModel mRoomInfoModel;
	// End of the Model

	// variables required for server request for specific information of selected room
	private int pen_id;
	private String dateMT;
	private String room_name;

	// others
	private Context mContext;


	public SpecificInfoRoomRecyclerViewAdapter(Context context, int pen_id, String dateMT, String room_name) {
		Log.d(TAG, TAG);
		this.mContext = context;
		this.pen_id = pen_id;
		this.dateMT = dateMT;
		this.room_name = room_name;
		this.mRoomInfoModel = new RoomInfoModel();

		// ***************receive the specific info data of the room from the server;
		/*try {
			new RoomInfoDataDownloadingTask().execute(MTPLEASE_URL +
					"pensions?pen_id="+ pen_id + "&date=" + dateMT + "&room_name=" + URLEncoder.encode(room_name, "utf-8").replaceAll("\\+", "%20"));
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "Wrong URL sent!");
			e.printStackTrace();
		}*/
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int cardViewType) {
		Log.d(TAG, "onCreateViewHolder");

		View cardView;

		switch(cardViewType) {
			case CARD_IMAGE_VIEW_PAGER:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_pager_room_images, parent, false);
				return new RoomImageViewPagerCard(cardView);
			case CARD_PHONE_AND_WEBSITE_BUTTONS:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_phone_and_website_buttons, parent, false);
				return new PhoneAndWebsiteButtonsCard(cardView);
			case CARD_PRICE_AND_RATE:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_price_and_rate, parent, false);
				return new PriceAndRateCard(cardView);
			case CARD_BASIC_INFO:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_basic_info, parent, false);
				return new BasicInfoCard(cardView);
			case CARD_OPTIONS:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_options, parent, false);
				return new OptionsCard(cardView);
			case CARD_VISITED_SCHOOLS:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_visited_schools, parent, false);
				return new VisitedSchoolsCard(cardView);
			case CARD_REVIEWS:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_reviews, parent, false);
				return new ReviewsCard(cardView);
			case CARD_ADDRESS_AND_ROUTE_AND_MAP:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_address_and_route_and_map, parent, false);
				return new AddressAndRouteAndMapCard(cardView);
			case CARD_PRICE_AND_DATE_SELECTION:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_price_and_date_selection, parent, false);
				return new PriceAndDateSelectionCard(cardView);
			case CARD_OWNER_INFO:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_owner_info, parent, false);
				return new OwnerInfoCard(cardView);
			case CARD_OTHERS:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_others, parent, false);
				return new OthersCard(cardView);
			case CARD_WARNINGS:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notice, parent, false);
				return new NoticeCard(cardView);
		}

		throw new RuntimeException("there is no type that matches the type " + cardViewType + "make sure your using types correctly");
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder cardViewHolder, int position) {
		Log.d(TAG, "onBindViewHolder");
		switch(position) {
			case CARD_IMAGE_VIEW_PAGER:
				break;
			case CARD_PHONE_AND_WEBSITE_BUTTONS:
				break;
			case CARD_PRICE_AND_RATE:
				break;
			case CARD_BASIC_INFO:
				break;
			case CARD_OPTIONS:
				break;
			case CARD_VISITED_SCHOOLS:
				break;
			case CARD_REVIEWS:
				break;
			case CARD_ADDRESS_AND_ROUTE_AND_MAP:
				break;
			case CARD_PRICE_AND_DATE_SELECTION:
				break;
			case CARD_OWNER_INFO:
				break;
			case CARD_OTHERS:
				break;
			case CARD_WARNINGS:
				break;
		}
	}

	@Override
	public int getItemCount() {
		return 12;
	}

	@Override
	public int getItemViewType(int position) {
		switch(position) {
			case 0:
				return CARD_IMAGE_VIEW_PAGER;
			case 1:
				return CARD_PHONE_AND_WEBSITE_BUTTONS;
			case 2:
				return CARD_PRICE_AND_RATE;
			case 3:
				return CARD_BASIC_INFO;
			case 4:
				return CARD_OPTIONS;
			case 5:
				return CARD_VISITED_SCHOOLS;
			case 6:
				return CARD_REVIEWS;
			case 7:
				return CARD_ADDRESS_AND_ROUTE_AND_MAP;
			case 8:
				return CARD_PRICE_AND_DATE_SELECTION;
			case 9:
				return CARD_OWNER_INFO;
			case 10:
				return CARD_OTHERS;
			case 11:
				return CARD_WARNINGS;
			default:
				return position;
		}
	}

	private class RoomImageViewPagerCard extends RecyclerView.ViewHolder {
		private ViewPager roomImageViewPager;
		private ImageCarouselAdapter roomImageCarouselAdapter;

		public RoomImageViewPagerCard(View cardView) {
			super(cardView);
			roomImageViewPager = (ViewPager) cardView.findViewById(R.id.view_pager_room_images);

			roomImageCarouselAdapter = new ImageCarouselAdapter(mContext, 3);
			roomImageViewPager.setAdapter(roomImageCarouselAdapter);
		}

		public void setRoomImages(int pen_id, String room_name) {

			// ******************get images from the server here with the parameters

            /*roomImageCarouselAdapter = new ImageCarouselAdapter(mContext, 3);
            roomImageViewPager.setAdapter(roomImageCarouselAdapter);*/
		}
	}

	private class ImageCarouselAdapter extends PagerAdapter {
		private Context mContext;
		private Vector<Bitmap> roomImageVector;
		private int imageCount;

		private ImageCarouselAdapter(Context mContext, int imageCount) {
			this.mContext = mContext;
			this.imageCount = imageCount;
			roomImageVector = new Vector<Bitmap>(imageCount);

			BitmapDrawable drawable = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.img_sample);
			Bitmap bitmap = drawable.getBitmap();
			roomImageVector.add(bitmap);
			roomImageVector.add(bitmap);
			roomImageVector.add(bitmap);
		}

		@Override
		public int getCount() {
			return this.imageCount;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(mContext);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setImageBitmap(roomImageVector.get(position));
			container.addView(imageView, 0);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((ImageView) object);
		}
	}

	private class PhoneAndWebsiteButtonsCard extends RecyclerView.ViewHolder {
		private Button phoneCallButton;
		private Button websiteButton;

		public void setComponents() {
			phoneCallButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Uri phoneNumber = Uri.parse("tel:"+ mRoomInfoModel.getPen_phone1());
					Intent phoneCallIntent = new Intent(Intent.ACTION_DIAL, phoneNumber);
					mContext.startActivity(phoneCallIntent);
				}
			});
			websiteButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Uri webLink = Uri.parse(mRoomInfoModel.getPen_homepage());
					Intent webBrowseIntent = new Intent(Intent.ACTION_VIEW, webLink);
					mContext.startActivity(webBrowseIntent);
				}
			});
		}

		public PhoneAndWebsiteButtonsCard(View cardView) {
			super(cardView);
			phoneCallButton = (Button) cardView.findViewById(R.id.button_call);
			websiteButton = (Button) cardView.findViewById(R.id.button_website);
		}
	}

	private class PriceAndRateCard extends RecyclerView.ViewHolder {
		private TextView roomPrice;

		public void setComponents() {
			roomPrice.setText(mRoomInfoModel.getRoom_cost());
		}

		public PriceAndRateCard(View cardView) {
			super(cardView);
			roomPrice = (TextView) cardView.findViewById(R.id.text_price_room);
		}
	}

	private class BasicInfoCard extends RecyclerView.ViewHolder {
		private TextView numberPeopleRoom;
		private TextView roomSize;
		private TextView numberRooms;
		private TextView numberToilets;

		public void setComponents() {
			numberPeopleRoom.setText("기준 인원: " + mRoomInfoModel.getRoom_std_people()
					+ "명 / 최대 인원" + mRoomInfoModel.getRoom_max_people() + "명");
			roomSize.setText(mRoomInfoModel.getRoom_pyeong() + "평");
			numberRooms.setText(mRoomInfoModel.getNum_rooms() + "개");
			numberToilets.setText(mRoomInfoModel.getNum_toilets() + "개");
		}

		public BasicInfoCard(View cardView) {
			super(cardView);
			numberPeopleRoom = (TextView) cardView.findViewById(R.id.text_people_number_room);
			roomSize = (TextView) cardView.findViewById(R.id.text_size_room);
			numberRooms = (TextView) cardView.findViewById(R.id.text_num_rooms);
			numberToilets = (TextView) cardView.findViewById(R.id.text_num_toilets);
		}
	}

	private class OptionsCard extends RecyclerView.ViewHolder {
		private TextView playground;
		private TextView pickup;
		private TextView barbecue;
		private TextView valley;
		private TextView aircon;

		public void setComponents() {
			playground.setText(mRoomInfoModel.getPen_ground());
			pickup.setText(mRoomInfoModel.getPen_pickup());
			barbecue.setText(mRoomInfoModel.getPen_barbecue());
			valley.setText(mRoomInfoModel.getPen_valley());
			aircon.setText(mRoomInfoModel.getRoom_aircon());
		}


		public OptionsCard(View cardView) {
			super(cardView);
			playground = (TextView) cardView.findViewById(R.id.text_playground);
			pickup = (TextView) cardView.findViewById(R.id.text_pickup);
			barbecue = (TextView) cardView.findViewById(R.id.text_barbecue);
			valley = (TextView) cardView.findViewById(R.id.text_valley);
			aircon = (TextView) cardView.findViewById(R.id.text_aircon);
		}
	}

	private class VisitedSchoolsCard extends RecyclerView.ViewHolder {

		public void setComponents() {

		}

		public VisitedSchoolsCard(View cardView) {
			super(cardView);
		}
	}

	private class ReviewsCard extends RecyclerView.ViewHolder {

		public void setComponents() {

		}

		public ReviewsCard(View cardView) {
			super(cardView);
		}
	}

	private class AddressAndRouteAndMapCard extends RecyclerView.ViewHolder {
		TextView walkFromStationTime;
		TextView walkFromTerminalTime;
		// NMap instance;

		public void setComponents() {
			walkFromStationTime.setText(mRoomInfoModel.getPen_walk_station());
			walkFromTerminalTime.setText(mRoomInfoModel.getPen_walk_terminal());
		}

		public AddressAndRouteAndMapCard(View cardView) {
			super(cardView);
			walkFromStationTime = (TextView) cardView.findViewById(R.id.text_walk_station);
			walkFromTerminalTime = (TextView) cardView.findViewById(R.id.text_walk_terminal);
		}
	}

	private class PriceAndDateSelectionCard extends RecyclerView.ViewHolder {

		public void setComponents() {

		}

		public PriceAndDateSelectionCard(View cardView) {
			super(cardView);
		}
	}

	private class OwnerInfoCard extends RecyclerView.ViewHolder {
		TextView ceoName;
		TextView ceoAccount;

		public void setComponents() {
			ceoName.setText(mRoomInfoModel.getPen_ceo());
			//ceoAccount.setText(mRoomInfoModel);
		}

		public OwnerInfoCard(View cardView) {
			super(cardView);
			ceoName = (TextView) cardView.findViewById(R.id.text_name_ceo);
			ceoAccount = (TextView) cardView.findViewById(R.id.text_account_ceo);
		}
	}

	private class OthersCard extends RecyclerView.ViewHolder {
		TextView otherFacilities;
		TextView checkInOutTime;
		TextView checkInOutNotice;
		TextView pensionNotice;

		public void setComponents() {
			otherFacilities.setText(mRoomInfoModel.getPen_etc_facility());
			checkInOutTime.setText("입실 시간: " + mRoomInfoModel.getPen_checkin()
					+ " / 퇴실 시간: " + mRoomInfoModel.getPen_checkout());
			checkInOutNotice.setText(mRoomInfoModel.getPen_check_caution());
			pensionNotice.setText(mRoomInfoModel.getPen_caution());
		}

		public OthersCard(View cardView) {
			super(cardView);
			otherFacilities = (TextView) cardView.findViewById(R.id.text_facilities_other);
			checkInOutTime = (TextView) cardView.findViewById(R.id.text_time_checkin_checkout);
			checkInOutNotice = (TextView) cardView.findViewById(R.id.text_notice_checkinout);
			pensionNotice = (TextView) cardView.findViewById(R.id.text_notice_pension);
		}
	}

	private class NoticeCard extends RecyclerView.ViewHolder {
		TextView mtpleaseNotice;

		public void setMtpleaseNotice(TextView mtpleaseNotice) {
			this.mtpleaseNotice = mtpleaseNotice;
		}

		public NoticeCard(View cardView) {
			super(cardView);
			mtpleaseNotice = (TextView) cardView.findViewById(R.id.text_notice_mtplease);
		}
	}

	/**
	 * @author In-Ho
	 * AsyncTask for receiving specific room data from the server
	 * Used AsyncTask to perform the task in the background.
	 */
	private class RoomInfoDataDownloadingTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
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
			} catch(ClientProtocolException e) {
				Log.e(TAG, "ClientProtocolException");
				e.printStackTrace();
			} catch(IOException e) {
				Log.e(TAG, "IOException");
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String jsonString) {
			super.onPostExecute(jsonString);

			try {
				JSONObject roomInfoObject = new JSONObject(jsonString);
				roomInfoArray = new JSONArray(roomInfoObject.getString("results"));

				JSONObject roomInfoList = roomInfoArray.getJSONObject(0);
				// set all data of the room into the model instance
				mRoomInfoModel.setPen_id(roomInfoList.getInt("pen_id"));
				mRoomInfoModel.setRoom_name(roomInfoList.getString("room_name"));
				mRoomInfoModel.setPen_period_division(roomInfoList.getString("pen_period_division"));
				mRoomInfoModel.setPeriod_start(roomInfoList.getString("period_start"));
				mRoomInfoModel.setPeriod_end(roomInfoList.getString("period_end"));
				mRoomInfoModel.setWeekdays(roomInfoList.getInt("weekdays"));
				mRoomInfoModel.setFriday(roomInfoList.getInt("friday"));
				mRoomInfoModel.setWeekends(roomInfoList.getInt("weekends"));
				mRoomInfoModel.setRoom_std_people(roomInfoList.getInt("room_std_people"));
				mRoomInfoModel.setRoom_max_people(roomInfoList.getInt("room_max_people"));
				mRoomInfoModel.setRoom_pyeong(roomInfoList.getInt("room_pyeong"));
				mRoomInfoModel.setNum_rooms(roomInfoList.getInt("num_rooms"));
				mRoomInfoModel.setNum_toilets(roomInfoList.getInt("num_toilets"));
				mRoomInfoModel.setRoom_aircon(roomInfoList.getInt("room_aircon"));
				mRoomInfoModel.setRoom_equipment(roomInfoList.getString("room_equipment"));
				mRoomInfoModel.setRoom_description(roomInfoList.getString("room_description"));
				mRoomInfoModel.setPen_region(roomInfoList.getString("pen_region"));
				mRoomInfoModel.setPen_name(roomInfoList.getString("pen_name"));
				mRoomInfoModel.setPen_homepage(roomInfoList.getString("pen_homepage"));
				mRoomInfoModel.setPen_lot_adr(roomInfoList.getString("pen_lot_adr"));
				mRoomInfoModel.setPen_road_adr(roomInfoList.getString("pen_road_adr"));
				mRoomInfoModel.setPen_latitude(roomInfoList.getDouble("pen_latitude"));
				mRoomInfoModel.setPen_longitude(roomInfoList.getDouble("pen_longitude"));
				mRoomInfoModel.setPen_ceo(roomInfoList.getString("pen_ceo"));
				mRoomInfoModel.setPen_phone1(roomInfoList.getString("pen_phone1"));
				mRoomInfoModel.setPen_phone2(roomInfoList.getString("pen_phone2"));
				mRoomInfoModel.setPen_checkin(roomInfoList.getString("pen_checkin"));
				mRoomInfoModel.setPen_checkout(roomInfoList.getString("pen_checkout"));
				mRoomInfoModel.setPen_check_caution(roomInfoList.getString("pen_check_caution"));
				mRoomInfoModel.setPen_pickup(roomInfoList.getInt("pen_pickup"));
				mRoomInfoModel.setPen_pickup_description(roomInfoList.getString("pen_pickup_description"));
				mRoomInfoModel.setPen_barbecue(roomInfoList.getInt("pen_barbecue"));
				mRoomInfoModel.setPen_barbecue_description(roomInfoList.getString("pen_barbecue_description"));
				mRoomInfoModel.setPen_ground(roomInfoList.getInt("pen_ground"));
				mRoomInfoModel.setPen_ground_description(roomInfoList.getString("pen_ground_description"));
				mRoomInfoModel.setPen_valley(roomInfoList.getInt("pen_valley"));
				mRoomInfoModel.setPen_valley_description(roomInfoList.getString("pen_valley_description"));
				mRoomInfoModel.setPen_etc_facility(roomInfoList.getString("pen_etc_facility"));
				mRoomInfoModel.setPen_caution(roomInfoList.getString("pen_caution"));
				mRoomInfoModel.setPen_cost_caution(roomInfoList.getString("pen_cost_caution"));
				mRoomInfoModel.setPen_walk_station(roomInfoList.getString("pen_walk_station"));
				mRoomInfoModel.setPen_walk_terminal(roomInfoList.getString("pen_walk_terminal"));
				mRoomInfoModel.setPen_picture_flag(roomInfoList.getInt("pen_picture_flag"));
				mRoomInfoModel.setCost_table(roomInfoList.getJSONArray("cost_table"));
				mRoomInfoModel.setPeriod_table(roomInfoList.getJSONArray("period_table"));
				mRoomInfoModel.setRoom_cost(roomInfoList.getInt("room_cost"));
				// end of the setting

			} catch (JSONException e) {
				Log.e(TAG, "Error receiving room data");
				e.printStackTrace();
			}


		}

	}
}
