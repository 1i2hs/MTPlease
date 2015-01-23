package com.owo.mtplease;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	private static final int CARD_NOTICE = 11;
	// End of series of cards

	// Model: Data variables for User Interface Views
	private RoomInfoModel mRoomInfoModel;
	// End of the Model

	// others
	private Context mContext;

	// Naver Map API Key
	private static final String NAVER_MAP_API_KEY = "d95aa436e270994d04d2fcc5bffdb1cb";
	// Naver related instances
	private NMapView mMapView;
	private NMapController mMapController;
		/*NMapViewerResourceProvider mMapViewerResourceProvider = null;
		NMapOverlayManager mOverlayManager;*/

	public SpecificInfoRoomRecyclerViewAdapter(Context context, RoomInfoModel mRoomInfoModel) {
		Log.d(TAG, TAG);
		this.mContext = context;
		this.mRoomInfoModel = mRoomInfoModel;
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
			case CARD_NOTICE:
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
				((PhoneAndWebsiteButtonsCard) cardViewHolder).setComponents();
				break;
			case CARD_PRICE_AND_RATE:
				((PriceAndRateCard) cardViewHolder).setComponents();
				break;
			case CARD_BASIC_INFO:
				((BasicInfoCard) cardViewHolder).setComponents();
				break;
			case CARD_OPTIONS:
				((OptionsCard) cardViewHolder).setComponents();
				break;
			case CARD_VISITED_SCHOOLS:
				((VisitedSchoolsCard) cardViewHolder).setComponents();
				break;
			case CARD_REVIEWS:
				((ReviewsCard) cardViewHolder).setComponents();
				break;
			case CARD_ADDRESS_AND_ROUTE_AND_MAP:
				//((AddressAndRouteAndMapCard) cardViewHolder).setComponents();
				break;
			case CARD_PRICE_AND_DATE_SELECTION:
				((PriceAndDateSelectionCard) cardViewHolder).setComponents();
				break;
			case CARD_OWNER_INFO:
				((OwnerInfoCard) cardViewHolder).setComponents();
				break;
			case CARD_OTHERS:
				((OthersCard) cardViewHolder).setComponents();
				break;
			case CARD_NOTICE:
				//((NoticeCard) cardViewHolder).setMtpleaseNotice();
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
				return CARD_NOTICE;
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
			int room_cost = mRoomInfoModel.getRoom_cost();
			if(room_cost == 0)
				roomPrice.setText(R.string.telephone_inquiry);
			else
				roomPrice.setText(R.string.currency_unit + room_cost);
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
					+ "명 / 최대 인원: " + mRoomInfoModel.getRoom_max_people() + "명");
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
			playground.setText(mRoomInfoModel.getPen_ground() + "");
			pickup.setText(mRoomInfoModel.getPen_pickup() + "");
			barbecue.setText(mRoomInfoModel.getPen_barbecue() + "");
			valley.setText(mRoomInfoModel.getPen_valley() + "");
			aircon.setText(mRoomInfoModel.getRoom_aircon() + "");
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

	private class AddressAndRouteAndMapCard extends RecyclerView.ViewHolder implements NMapView.OnMapStateChangeListener, NMapView.OnMapViewTouchEventListener,
			NMapOverlayManager.OnCalloutOverlayListener, NMapPOIdataOverlay.OnStateChangeListener {

		private TextView walkFromStationTime;
		private TextView walkFromTerminalTime;


		public void setComponents() {
			walkFromStationTime.setText(mRoomInfoModel.getPen_walk_station());
			walkFromTerminalTime.setText(mRoomInfoModel.getPen_walk_terminal());

			// set a registered API key for Open MapViewer Library
			mMapView.setApiKey(NAVER_MAP_API_KEY);

			mMapView.setClickable(true);
			// register listener for map state changes

			mMapView.setOnMapStateChangeListener(new OnMapStateChangeListener());
			mMapView.setOnMapViewTouchEventListener(this);

			// use map controller to zoom in/out, pan and set map center, zoom
			// level etc.
			mMapController = mMapView.getMapController();

			// use built in zoom controls
			mMapView.setBuiltInZoomControls(true, null);
		}

		public AddressAndRouteAndMapCard(View cardView) {
			super(cardView);
			//mMapView = (NMapView) cardView.findViewById(R.id.nmapview_pension);
			walkFromStationTime = (TextView) cardView.findViewById(R.id.text_walk_station);
			walkFromTerminalTime = (TextView) cardView.findViewById(R.id.text_walk_terminal);
		}

		@Override
		public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay nMapOverlay, NMapOverlayItem nMapOverlayItem, Rect rect) {
			return null;
		}

		@Override
		public void onLongPress(NMapView nMapView, MotionEvent motionEvent) {

		}

		@Override
		public void onLongPressCanceled(NMapView nMapView) {

		}

		@Override
		public void onTouchDown(NMapView nMapView, MotionEvent motionEvent) {

		}

		@Override
		public void onTouchUp(NMapView nMapView, MotionEvent motionEvent) {

		}

		@Override
		public void onScroll(NMapView nMapView, MotionEvent motionEvent, MotionEvent motionEvent2) {

		}

		@Override
		public void onSingleTapUp(NMapView nMapView, MotionEvent motionEvent) {

		}

		@Override
		public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {

		}

		@Override
		public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {

		}

		@Override
		public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {

		}

		@Override
		public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {

		}

		@Override
		public void onMapCenterChangeFine(NMapView nMapView) {

		}

		@Override
		public void onZoomLevelChange(NMapView nMapView, int i) {

		}

		@Override
		public void onAnimationStateChange(NMapView nMapView, int i, int i2) {

		}
	}

	private class PriceAndDateSelectionCard extends RecyclerView.ViewHolder {

		private TableLayout periodTable;

		private TextView priceCell22;
		private TextView priceCell23;
		private TextView priceCell24;
		private TextView priceCell32;
		private TextView priceCell33;
		private TextView priceCell34;
		private TextView priceCell42;
		private TextView priceCell43;
		private TextView priceCell44;
		private TextView priceCell52;
		private TextView priceCell53;
		private TextView priceCell54;

		private boolean isPeriodTableVisible = false;

		public void setComponents() {
			try {
				for (int i = 0; i < mRoomInfoModel.getCost_table().length(); i++) {
					JSONObject costObject = mRoomInfoModel.getCost_table().getJSONObject(i);
					String pen_period_division = costObject.optString("pen_period_division");
					Log.d(TAG, pen_period_division);
					if (pen_period_division.equals(mContext.getResources().getString(R.string.off_season))) {
						Log.d(TAG, "비수기");
						priceCell22.setText(castRoomPrice(costObject.optInt("weekdays")));
						priceCell23.setText(castRoomPrice(costObject.optInt("friday")));
						priceCell24.setText(castRoomPrice(costObject.optInt("weekends")));
					} else if (pen_period_division.equals(mContext.getResources().getString(R.string.semi_on_season))) {
						priceCell32.setText(castRoomPrice(costObject.optInt("weekdays")));
						priceCell33.setText(castRoomPrice(costObject.optInt("friday")));
						priceCell34.setText(castRoomPrice(costObject.optInt("weekends")));
					} else if (pen_period_division.equals(mContext.getResources().getString(R.string.on_season))) {
						Log.d(TAG, "성수기");
						priceCell42.setText(castRoomPrice(costObject.optInt("weekdays")));
						priceCell43.setText(castRoomPrice(costObject.optInt("friday")));
						priceCell44.setText(castRoomPrice(costObject.optInt("weekends")));
					} else {
						Log.d(TAG, "극성수기");
						priceCell52.setText(castRoomPrice(costObject.optInt("weekdays")));
						priceCell53.setText(castRoomPrice(costObject.optInt("friday")));
						priceCell54.setText(castRoomPrice(costObject.optInt("weekends")));
					}
				}

				if(!isPeriodTableVisible) {
					for (int i = 0; i < mRoomInfoModel.getPeriod_table().length(); i++) {
						int rowNum = i + 1;

						TableRow periodTableRow = new TableRow(mContext);
						periodTableRow.setId(rowNum); // id is set by the level of the row
						periodTableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));


						JSONObject periodObject = mRoomInfoModel.getPeriod_table().getJSONObject(i);
						String pen_period_division = periodObject.optString("pen_period_division");
						String period = periodObject.optString("period_start") + " ~ " + periodObject.optString("period_end");

						for (int j = 0; j < 2; j++) {
							int colNum = j + 1;
							int cellNum = rowNum * 10 + colNum;
							TextView periodCell = new TextView(mContext);
							periodCell.setId(cellNum);
							periodCell.setGravity(Gravity.CENTER);
							periodCell.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

							if (pen_period_division.equals(mContext.getResources().getString(R.string.off_season))) {
								Log.d(TAG, "비수기");
								setPeriodCell(periodCell, R.string.off_season, period);
							} else if (pen_period_division.equals(mContext.getResources().getString(R.string.semi_on_season))) {
								Log.d(TAG, "준성수기");
								setPeriodCell(periodCell, R.string.semi_on_season, period);
							} else if (pen_period_division.equals(mContext.getResources().getString(R.string.on_season))) {
								Log.d(TAG, "성수기");
								setPeriodCell(periodCell, R.string.on_season, period);
							} else {
								Log.d(TAG, "극성수기");
								setPeriodCell(periodCell, R.string.extreme_on_season, period);
							}

							periodTableRow.addView(periodCell);
						}
						periodTable.addView(periodTableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
					}
					isPeriodTableVisible = true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		private void setPeriodCell(TextView periodCell, int periodResId, String period) {
			if(periodCell.getId() % 10 == 1) {
				periodCell.setText(periodResId);
				periodCell.setBackgroundResource(R.drawable.cell_shape_right_line);
			} else
				periodCell.setText(period);
		}


		private String castRoomPrice(int roomPrice) {
			if(roomPrice == 0) {
				return mContext.getResources().getString(R.string.telephone_inquiry);
			} else {
				return mContext.getResources().getString(R.string.currency_unit) + roomPrice;
			}
		}

		public PriceAndDateSelectionCard(View cardView) {
			super(cardView);

			priceCell22 = (TextView) cardView.findViewById(R.id.cell22_price);
			priceCell23 = (TextView) cardView.findViewById(R.id.cell23_price);
			priceCell24 = (TextView) cardView.findViewById(R.id.cell24_price);
			priceCell32 = (TextView) cardView.findViewById(R.id.cell32_price);
			priceCell33 = (TextView) cardView.findViewById(R.id.cell33_price);
			priceCell34 = (TextView) cardView.findViewById(R.id.cell34_price);
			priceCell42 = (TextView) cardView.findViewById(R.id.cell42_price);
			priceCell43 = (TextView) cardView.findViewById(R.id.cell43_price);
			priceCell44 = (TextView) cardView.findViewById(R.id.cell44_price);
			priceCell52 = (TextView) cardView.findViewById(R.id.cell52_price);
			priceCell53 = (TextView) cardView.findViewById(R.id.cell53_price);
			priceCell54 = (TextView) cardView.findViewById(R.id.cell54_price);
			periodTable = (TableLayout) cardView.findViewById(R.id.table_period_room);
		}
	}

	private class OwnerInfoCard extends RecyclerView.ViewHolder {
		private TextView ceoName;
		private TextView ceoAccount;

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
		private TextView otherFacilities;
		private TextView checkInOutTime;
		private TextView checkInOutNotice;
		private TextView pensionNotice;

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
		private TextView mtpleaseNotice;

		public void setMtpleaseNotice(TextView mtpleaseNotice) {
			this.mtpleaseNotice = mtpleaseNotice;
		}

		public NoticeCard(View cardView) {
			super(cardView);
			mtpleaseNotice = (TextView) cardView.findViewById(R.id.text_notice_mtplease);
		}
	}

	private class OnMapStateChangeListener implements NMapView.OnMapStateChangeListener {

		@Override
		public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {
			// if initialization of the map succeeds
			if (nMapError == null) {
				mMapController.setMapCenter(new NGeoPoint(mRoomInfoModel.getPen_longitude(),
						mRoomInfoModel.getPen_latitude()), 11);
			} else {
				Log.e("ERROR",
						"onMapInitHandler: error=" + nMapError.toString());
			}
		}

		@Override
		public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {

		}

		@Override
		public void onMapCenterChangeFine(NMapView nMapView) {

		}

		@Override
		public void onZoomLevelChange(NMapView nMapView, int i) {

		}

		@Override
		public void onAnimationStateChange(NMapView nMapView, int i, int i2) {

		}
	}
}
