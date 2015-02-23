package com.owo.mtplease;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.owo.mtplease.fragment.ResultFragment;
import com.owo.mtplease.view.TypefaceLoader;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by In-Ho on 2014-12-23.
 */
public class RoomListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final String TAG = "RoomListRecyclerViewAdapter";
	private static final String MTPLEASE_URL = "http://mtplease.herokuapp.com/";

	private static final int TYPE_HEADER = 0;
	private static final int TYPE_ITEM = 1;

	private static final int REAL_PICTURE_DOESNT_EXISTS = 0;
	private static final int REAL_PICTURE_EXISTS = 1;

	private Context mContext;

	// Model: Data variables for User Interface Views
	private JSONArray roomArray;
	// End of the Model

	// Listeners
	private ResultFragment.OnResultFragmentListener mOnResultFragmentListener;
	// End of the listeners

	// others
	private String mtDate;
	// End of the others

	private static String imageUrl;

	public RoomListRecyclerViewAdapter(Context context, JSONArray jsonArray, String date,
									   ResultFragment.OnResultFragmentListener onResultFragmentListener) {
		mContext = context;
		roomArray = jsonArray;
		mtDate = date;
		mOnResultFragmentListener = onResultFragmentListener;

		preLoadImages();
	}

	private void preLoadImages() {
		for(int i = 0; i < roomArray.length(); i ++) {
			try {
				JSONObject roomBasicData = roomArray.getJSONObject(i);

				imageUrl =  MTPLEASE_URL + "img/pensions/" + roomBasicData.optInt("pen_id")
						+ "/" + URLEncoder.encode(roomBasicData.optString("room_name"), "utf-8").replaceAll("\\+","%20");

				if(Integer.parseInt(roomBasicData.optString("room_picture_flag")) == REAL_PICTURE_EXISTS)
					imageUrl += "/real/1.JPG";
				else
					imageUrl += "/unreal/1.JPG";

				//Log.i(TAG, imageUrl);

				if(!ServerCommunicationManager.getInstance(mContext).containsImage(imageUrl)) {
					ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
						@Override
						public void onResponse(Bitmap response) {
							ServerCommunicationManager.getInstance(mContext).putBitmap(imageUrl, response);
						}
					}, 0, 0, null, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							error.printStackTrace();
						}
					});

					ServerCommunicationManager.getInstance(mContext).addToRequestQueue(imageRequest);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Log.d(TAG, "onCreateViewHolder");

		if(viewType == TYPE_ITEM) {
			//inflate your layout and pass it to view holder
			View itemView = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.card_room, parent, false);

			return new RoomCard(itemView, mContext, mtDate, mOnResultFragmentListener, roomArray);
		}
		else if(viewType == TYPE_HEADER) {
			View headerView = LayoutInflater.from(parent.getContext()).
					inflate(R.layout.view_header_placeholder, parent, false);

			return new BlankHeader(headerView);
		}

		throw new RuntimeException("there is no type that matches the type "
				+ viewType + "make sure your using types correctly");
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		Log.d(TAG, "onBindViewHolder");
		if(holder instanceof RoomCard) {
			try {
				JSONObject roomData = roomArray.getJSONObject(position - 1);
				((RoomCard) holder).setComponents(roomData);
				((RoomCard) holder).getLayout().setOnClickListener((RoomCard) holder);
			} catch(UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch(JSONException e) {
				e.printStackTrace();
			} catch(NullPointerException e) {
				e.printStackTrace();
			}
		}
		else if(holder instanceof BlankHeader) {

		}
	}

	@Override
	public int getItemCount() {
		try {
			return roomArray.length() + 1;
		} catch(NullPointerException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int getItemViewType(int position) {
		if (isPositionHeader(position))
			return TYPE_HEADER;

		return TYPE_ITEM;
	}

	private boolean isPositionHeader(int position) {
		return position == 0;
	}

	private static class BlankHeader extends RecyclerView.ViewHolder {
		public BlankHeader(View cardView) {
			super(cardView);
		}
	}

	private static class RoomCard extends RecyclerView.ViewHolder implements View.OnClickListener {
		private CardView roomCard;
		private LinearLayout roomLayout;
		private ImageView roomThumbnailImage;
		//private NetworkImageView roomThumbnailImage;
		private ImageView realPictureSticker;
		private TextView roomName;
		private TextView pensionName;
		private TextView rangeNumberOfPeople;
		private ImageView airConditionerIcon;
		private ImageView pickUpIcon;
		private ImageView playgroundIcon;
		private ImageView barbecueIcon;
		private ImageView valleyIcon;
		private TextView roomPrice;
		private int pen_id;
		private String room_name;
		private String pen_name;
		private Context mContext;
		private String mtDate;
		private ResultFragment.OnResultFragmentListener mOnResultFragmentListener;
		private JSONArray roomArray;

		// each data item is just a string in this case
		public RoomCard(View cardView, Context context, String mtDate,
						ResultFragment.OnResultFragmentListener onResultFragmentListener, JSONArray roomArray) {
			super(cardView);

			roomCard = (CardView) cardView;
			mContext = context;
			this.mtDate = mtDate;
			mOnResultFragmentListener = onResultFragmentListener;
			this.roomArray = roomArray;

			roomLayout = (LinearLayout) cardView.findViewById(R.id.LinearLayout_card_room);
			roomThumbnailImage = (ImageView) cardView.findViewById(R.id.imageView_thumbnail_room);
			realPictureSticker = (ImageView) cardView.findViewById(R.id.imageView_sticker_real);

			roomName = (TextView) cardView.findViewById(R.id.textView_name_room_card);
			roomName.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			pensionName = (TextView) cardView.findViewById(R.id.textView_name_pension_card);
			pensionName.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			rangeNumberOfPeople = (TextView) cardView.findViewById(R.id.textView_number_range_of_people_card);
			rangeNumberOfPeople.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			airConditionerIcon = (ImageView) cardView.findViewById(R.id.imageView_option_aircon_card);
			pickUpIcon = (ImageView) cardView.findViewById(R.id.imageView_option_pickup_card);
			playgroundIcon = (ImageView) cardView.findViewById(R.id.imageView_option_playground_card);
			barbecueIcon = (ImageView) cardView.findViewById(R.id.imageView_option_barbecue_card);
			valleyIcon = (ImageView) cardView.findViewById(R.id.imageView_option_valley_card);
			roomPrice = (TextView) cardView.findViewById(R.id.textView_price_room_card);
			roomPrice.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
		}

		public void setComponents(JSONObject roomData) throws UnsupportedEncodingException, JSONException {

			pen_id = roomData.optInt("pen_id");

			imageUrl =  MTPLEASE_URL + "img/pensions/" + pen_id + "/" + URLEncoder.encode(roomData.optString("room_name"), "utf-8").replaceAll("\\+","%20");
			if(Integer.parseInt(roomData.optString("room_picture_flag")) == REAL_PICTURE_EXISTS)
				imageUrl += "/real/1.JPG";
			else
				imageUrl += "/unreal/1.JPG";

			Log.i(TAG, imageUrl);

			ServerCommunicationManager.getInstance(mContext).getImage(imageUrl,
					ImageLoader.getImageListener(roomThumbnailImage, R.drawable.scrn_room_img_place_holder, R.drawable.scrn_room_img_error));

			this.room_name = roomData.optString("room_name");
			roomName.setText(this.room_name);
			roomName.setSelected(true);

			this.pen_name = roomData.optString("pen_name");
			pensionName.setText(this.pen_name);

			rangeNumberOfPeople.setText(roomData.optString("room_std_people") + " / " + roomData.optString("room_max_people"));

			if(roomData.optInt("room_aircon") != 1)
				airConditionerIcon.setImageResource(R.drawable.ic_no_air_conditioner);

			if(roomData.optInt("pen_pickup") != 1)
				pickUpIcon.setImageResource(R.drawable.ic_no_pick_up);

			if(roomData.optInt("pen_ground") != 1)
				playgroundIcon.setImageResource(R.drawable.ic_no_playground);

			if(roomData.optInt("pen_barbecue") != 1)
				barbecueIcon.setImageResource(R.drawable.ic_no_barbecue);

			if(roomData.optInt("pen_valley") != 1)
				valleyIcon.setImageResource(R.drawable.ic_no_valley);

			int roomPriceInt = roomData.optInt("room_cost");

			if (roomPriceInt == 0) {
				roomPrice.setText(R.string.telephone_inquiry);
			} else {
				roomPrice.setText(castRoomPriceToString(roomPriceInt));
			}

			if(roomData.optInt("room_picture_flag") == 1) {
				realPictureSticker.setImageResource(R.drawable.ic_real_picture_sticker);
			}

			roomCard.setPreventCornerOverlap(false);
		}

		public LinearLayout getLayout() {
			return roomLayout;
		}

		private String castRoomPriceToString(int price) {
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
			return mContext.getResources().getString(R.string.currency_unit) +
					new StringBuffer(totalRoomCostStringChanged).reverse().toString();
		}

		@Override
		public void onClick(View v) {
			try {
				String specificRoomURL = MTPLEASE_URL +
						"pension?pen_id="+ this.pen_id + "&date=" + mtDate + "&room_name="
						+ URLEncoder.encode(this.room_name, "utf-8").replaceAll("\\+", "%20");

				Log.d(TAG, specificRoomURL);
				/*MTPleaseJsonObjectRequest getRequest = new MTPleaseJsonObjectRequest(
						Request.Method.GET, specificRoomURL, null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						try {
							Log.d(TAG, response.toString());
							JSONObject roomInfoList = new JSONObject(response.getString("roomInfo"));

							// set all data of the room into the model instance
							RoomInfoModelController roomInfoModelController = new RoomInfoModelController();
							roomInfoModelController.setPen_id(roomInfoList.optInt("pen_id"));
							roomInfoModelController.setRoom_name(roomInfoList.optString("room_name"));
							roomInfoModelController.setPen_period_division(roomInfoList.optString("pen_period_division"));
							roomInfoModelController.setPeriod_start(roomInfoList.optString("period_start"));
							roomInfoModelController.setPeriod_end(roomInfoList.optString("period_end"));
							roomInfoModelController.setWeekdays(roomInfoList.optInt("weekdays"));
							roomInfoModelController.setFriday(roomInfoList.optInt("friday"));
							roomInfoModelController.setWeekends(roomInfoList.optInt("weekends"));
							roomInfoModelController.setRoom_std_people(roomInfoList.optInt("room_std_people"));
							roomInfoModelController.setRoom_max_people(roomInfoList.optInt("room_max_people"));
							roomInfoModelController.setRoom_pyeong(roomInfoList.optInt("room_pyeong"));
							roomInfoModelController.setNum_rooms(roomInfoList.optInt("num_rooms"));
							roomInfoModelController.setNum_toilets(roomInfoList.optInt("num_toilets"));
							roomInfoModelController.setRoom_aircon(roomInfoList.optInt("room_aircon"));
							roomInfoModelController.setRoom_equipment(roomInfoList.optString("room_equipment"));
							roomInfoModelController.setRoom_description(roomInfoList.optString("room_description"));
							roomInfoModelController.setRoom_picture_flag(roomInfoList.optInt("room_picture_flag"));
							roomInfoModelController.setNum_images(roomInfoList.optInt("num_images"));
							roomInfoModelController.setPen_region(roomInfoList.optString("pen_region"));
							roomInfoModelController.setPen_name(roomInfoList.optString("pen_name"));
							roomInfoModelController.setPen_homepage(roomInfoList.optString("pen_homepage"));
							roomInfoModelController.setPen_lot_adr(roomInfoList.optString("pen_lot_adr"));
							roomInfoModelController.setPen_road_adr(roomInfoList.optString("pen_road_adr"));
							roomInfoModelController.setPen_ceo(roomInfoList.optString("pen_ceo"));
							roomInfoModelController.setPen_phone1(roomInfoList.optString("pen_phone1"));
							roomInfoModelController.setPen_phone2(roomInfoList.optString("pen_phone2"));
							roomInfoModelController.setPen_ceo_account(roomInfoList.optString("pen_ceo_account"));
							roomInfoModelController.setPen_checkin(roomInfoList.optString("pen_checkin"));
							roomInfoModelController.setPen_checkout(roomInfoList.optString("pen_checkout"));
							roomInfoModelController.setPen_pickup(roomInfoList.optInt("pen_pickup"));
							roomInfoModelController.setPen_pickup_cost(roomInfoList.optString("pen_pickup_cost"));
							roomInfoModelController.setPen_pickup_location(roomInfoList.optString("pen_pickup_location"));
							roomInfoModelController.setPen_pickup_description(roomInfoList.optString("pen_pickup_description"));
							roomInfoModelController.setPen_barbecue(roomInfoList.optInt("pen_barbecue"));
							roomInfoModelController.setPen_barbecue_cost(roomInfoList.optString("pen_barbecue_cost"));
							roomInfoModelController.setPen_barbecue_component(roomInfoList.optString("pen_barbecue_component"));
							roomInfoModelController.setPen_barbecue_location(roomInfoList.optString("pen_barbecue_location"));
							roomInfoModelController.setPen_barbecue_description(roomInfoList.optString("pen_barbecue_description"));
							roomInfoModelController.setPen_ground(roomInfoList.optInt("pen_ground"));
							roomInfoModelController.setPen_ground_type(roomInfoList.optString("pen_ground_type"));
							roomInfoModelController.setPen_ground_description(roomInfoList.optString("pen_ground_description"));
							roomInfoModelController.setPen_valley(roomInfoList.optInt("pen_valley"));
							roomInfoModelController.setPen_valley_distance(roomInfoList.optString("pen_valley_distance"));
							roomInfoModelController.setPen_valley_depth(roomInfoList.optString("pen_valley_depth"));
							roomInfoModelController.setPen_valley_description(roomInfoList.optString("pen_valley_description"));
							roomInfoModelController.setPen_latitude(roomInfoList.optDouble("pen_latitude"));
							roomInfoModelController.setPen_longitude(roomInfoList.optDouble("pen_longitude"));
							roomInfoModelController.setPen_walk_station(roomInfoList.optString("pen_walk_station"));
							roomInfoModelController.setPen_walk_terminal(roomInfoList.optString("pen_walk_terminal"));
							roomInfoModelController.setCost_table(roomInfoList.optJSONArray("cost_table"));
							roomInfoModelController.setPeriod_table(roomInfoList.optJSONArray("period_table"));
							roomInfoModelController.setRoom_cost(roomInfoList.optInt("room_cost"));
							roomInfoModelController.setFacility(roomInfoList.optJSONArray("facility"));
							roomInfoModelController.setService(roomInfoList.optJSONArray("service"));
							roomInfoModelController.setUsage_caution(roomInfoList.optJSONArray("usage_caution"));
							roomInfoModelController.setReserve_caution(roomInfoList.optJSONArray("reserve_caution"));
							roomInfoModelController.setRefund_caution(roomInfoList.optJSONArray("refund_table"));
							// end of the setting

							mOnResultFragmentListener.onLoadSpecificInfoFragment(roomInfoModelController, roomArray);

							mOnResultFragmentListener.onPostLoadSpecificInfoFragment();
						} catch(JSONException e) {
							e.printStackTrace();
							Toast.makeText(mContext, R.string.fail_loading_room_info, Toast.LENGTH_SHORT).show();
							mOnResultFragmentListener.onPostLoadSpecificInfoFragment();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d(TAG, error.toString());
						Toast.makeText(mContext, R.string.server_error, Toast.LENGTH_SHORT).show();
						mOnResultFragmentListener.onPostLoadSpecificInfoFragment();
					}
				});

				getRequest.setContext(mContext);*/

				JsonObjectRequest getRequest = new JsonObjectRequest(
						Request.Method.GET, specificRoomURL, null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						try {
							Log.d(TAG, response.toString());
							JSONObject roomInfoList = new JSONObject(response.getString("roomInfo"));

							// set all data of the room into the model instance
							RoomInfoModelController roomInfoModelController = new RoomInfoModelController();
							roomInfoModelController.setPen_id(roomInfoList.optInt("pen_id"));
							roomInfoModelController.setRoom_name(roomInfoList.optString("room_name"));
							roomInfoModelController.setPen_period_division(roomInfoList.optString("pen_period_division"));
							roomInfoModelController.setPeriod_start(roomInfoList.optString("period_start"));
							roomInfoModelController.setPeriod_end(roomInfoList.optString("period_end"));
							roomInfoModelController.setWeekdays(roomInfoList.optInt("weekdays"));
							roomInfoModelController.setFriday(roomInfoList.optInt("friday"));
							roomInfoModelController.setWeekends(roomInfoList.optInt("weekends"));
							roomInfoModelController.setRoom_std_people(roomInfoList.optInt("room_std_people"));
							roomInfoModelController.setRoom_max_people(roomInfoList.optInt("room_max_people"));
							roomInfoModelController.setRoom_pyeong(roomInfoList.optString("room_pyeong"));
							roomInfoModelController.setNum_rooms(roomInfoList.optInt("num_rooms"));
							roomInfoModelController.setNum_toilets(roomInfoList.optInt("num_toilets"));
							roomInfoModelController.setRoom_aircon(roomInfoList.optInt("room_aircon"));
							roomInfoModelController.setRoom_equipment(roomInfoList.optString("room_equipment"));
							roomInfoModelController.setRoom_description(roomInfoList.optString("room_description"));
							roomInfoModelController.setRoom_picture_flag(roomInfoList.optInt("room_picture_flag"));
							roomInfoModelController.setNum_images(roomInfoList.optInt("num_images"));
							roomInfoModelController.setPen_region(roomInfoList.optString("pen_region"));
							roomInfoModelController.setPen_name(roomInfoList.optString("pen_name"));
							roomInfoModelController.setPen_homepage(roomInfoList.optString("pen_homepage"));
							roomInfoModelController.setPen_lot_adr(roomInfoList.optString("pen_lot_adr"));
							roomInfoModelController.setPen_road_adr(roomInfoList.optString("pen_road_adr"));
							roomInfoModelController.setPen_ceo(roomInfoList.optString("pen_ceo"));
							roomInfoModelController.setPen_phone1(roomInfoList.optString("pen_phone1"));
							roomInfoModelController.setPen_phone2(roomInfoList.optString("pen_phone2"));
							roomInfoModelController.setPen_ceo_account(roomInfoList.optString("pen_ceo_account"));
							roomInfoModelController.setPen_checkin(roomInfoList.optString("pen_checkin"));
							roomInfoModelController.setPen_checkout(roomInfoList.optString("pen_checkout"));
							roomInfoModelController.setPen_pickup(roomInfoList.optInt("pen_pickup"));
							roomInfoModelController.setPen_pickup_cost(roomInfoList.optString("pen_pickup_cost"));
							roomInfoModelController.setPen_pickup_location(roomInfoList.optString("pen_pickup_location"));
							roomInfoModelController.setPen_pickup_description(roomInfoList.optString("pen_pickup_description"));
							roomInfoModelController.setPen_barbecue(roomInfoList.optInt("pen_barbecue"));
							roomInfoModelController.setPen_barbecue_cost(roomInfoList.optString("pen_barbecue_cost"));
							roomInfoModelController.setPen_barbecue_component(roomInfoList.optString("pen_barbecue_component"));
							roomInfoModelController.setPen_barbecue_location(roomInfoList.optString("pen_barbecue_location"));
							roomInfoModelController.setPen_barbecue_description(roomInfoList.optString("pen_barbecue_description"));
							roomInfoModelController.setPen_ground(roomInfoList.optInt("pen_ground"));
							roomInfoModelController.setPen_ground_type(roomInfoList.optString("pen_ground_type"));
							roomInfoModelController.setPen_ground_description(roomInfoList.optString("pen_ground_description"));
							roomInfoModelController.setPen_valley(roomInfoList.optInt("pen_valley"));
							roomInfoModelController.setPen_valley_distance(roomInfoList.optString("pen_valley_distance"));
							roomInfoModelController.setPen_valley_depth(roomInfoList.optString("pen_valley_depth"));
							roomInfoModelController.setPen_valley_description(roomInfoList.optString("pen_valley_description"));
							roomInfoModelController.setPen_latitude(roomInfoList.optDouble("pen_latitude"));
							roomInfoModelController.setPen_longitude(roomInfoList.optDouble("pen_longitude"));
							roomInfoModelController.setPen_walk_station(roomInfoList.optString("pen_walk_station"));
							roomInfoModelController.setPen_walk_terminal(roomInfoList.optString("pen_walk_terminal"));
							roomInfoModelController.setCost_table(roomInfoList.optJSONArray("cost_table"));
							roomInfoModelController.setPeriod_table(roomInfoList.optJSONArray("period_table"));
							roomInfoModelController.setRoom_cost(roomInfoList.optInt("room_cost"));
							roomInfoModelController.setFacility(roomInfoList.optJSONArray("facility"));
							roomInfoModelController.setService(roomInfoList.optJSONArray("service"));
							roomInfoModelController.setUsage_caution(roomInfoList.optJSONArray("usage_caution"));
							roomInfoModelController.setReserve_caution(roomInfoList.optJSONArray("reserve_caution"));
							roomInfoModelController.setRefund_caution(roomInfoList.optJSONArray("refund_table"));
							roomInfoModelController.setPen_description(roomInfoList.optString("pen_description"));
							// end of the setting

							mOnResultFragmentListener.onLoadSpecificInfoFragment(roomInfoModelController, roomArray);

							mOnResultFragmentListener.onPostLoadSpecificInfoFragment();
						} catch(JSONException e) {
							e.printStackTrace();
							Toast.makeText(mContext, R.string.fail_loading_room_info, Toast.LENGTH_SHORT).show();
							mOnResultFragmentListener.onPostLoadSpecificInfoFragment();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d(TAG, error.toString());
						Toast.makeText(mContext, R.string.server_error, Toast.LENGTH_SHORT).show();
						mOnResultFragmentListener.onPostLoadSpecificInfoFragment();
					}
				});

				mOnResultFragmentListener.onPreLoadSpecificInfoFragment();
				ServerCommunicationManager.getInstance(mContext).addToRequestQueue(getRequest);
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, "Wrong URL sent!");
				e.printStackTrace();
			}
		}
	}
}
