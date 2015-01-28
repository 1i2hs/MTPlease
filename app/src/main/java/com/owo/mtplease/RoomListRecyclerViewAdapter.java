package com.owo.mtplease;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
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
	private ResultFragment.OnResultFragmentInteractionListener mOnResultFragmentInteractionListener;
	// End of the listeners

	// others
	private String mtDate;
	// End of the others

	public RoomListRecyclerViewAdapter(Context context, JSONArray jsonArray, String date,
			ResultFragment.OnResultFragmentInteractionListener onResultFragmentInteractionListener) {
		Log.d(TAG, "RoomListRecyclerViewAdapter");
		mContext = context;
		roomArray = jsonArray;
		mtDate = date;
		mOnResultFragmentInteractionListener = onResultFragmentInteractionListener;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Log.d(TAG, "onCreateViewHolder");

		if(viewType == TYPE_ITEM) {
			//inflate your layout and pass it to view holder
			View itemView = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.card_room, parent, false);

			return new RoomCard(itemView);
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
				((RoomCard) holder).setRoomData(roomData);
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

	private class RoomCard extends RecyclerView.ViewHolder implements View.OnClickListener {
		private CardView roomCard;
		private LinearLayout roomLayout;
		private ImageView roomImage;
		private ProgressBar loadingProgressBar;
		private TextView roomName;
		private TextView pensionName;
		private TextView rangeNumberOfPeople;
		private TextView roomPrice;

		private int pen_id;
		private String room_name;
		private String pen_name;

		// each data item is just a string in this case
		public RoomCard(View cardView) {
			super(cardView);
			roomCard = (CardView) cardView;
			roomLayout = (LinearLayout) cardView.findViewById(R.id.LinearLayout_card_room);
			roomImage = (ImageView) cardView.findViewById(R.id.image_room);
			loadingProgressBar = (ProgressBar) cardView.findViewById(R.id.progressBar_thumbnailImage_room);
			roomName = (TextView) cardView.findViewById(R.id.textView_name_room_card);
			pensionName = (TextView) cardView.findViewById(R.id.textView_name_pension_card);
			rangeNumberOfPeople = (TextView) cardView.findViewById(R.id.textView_number_range_of_people_card);
			roomPrice = (TextView) cardView.findViewById(R.id.textView_price_room_card);
		}

		public void setRoomData(JSONObject roomData) throws UnsupportedEncodingException, JSONException {

			pen_id = roomData.optInt("pen_id");

			String imageURL;
			if(Integer.parseInt(roomData.optString("pen_picture_flag")) == REAL_PICTURE_EXISTS)
				imageURL = MTPLEASE_URL + "img/pensions/" + roomData.getString("pen_id") + "/" + URLEncoder.encode(roomData.optString("room_name"), "utf-8").replaceAll("\\+","%20") + "/real/thumbnail.png";
			else
				imageURL = MTPLEASE_URL + "img/pensions/" + roomData.getString("pen_id") + "/" + URLEncoder.encode(roomData.optString("room_name"), "utf-8").replaceAll("\\+","%20") + "/unreal/thumbnail.png";

			Log.i(TAG, imageURL);
			new ImageLoadingTask(roomImage, loadingProgressBar).execute(imageURL);
			/*Picasso imageLoader = Picasso.with(mContext);
			imageLoader.setIndicatorsEnabled(true);
			if(roomImage == null) {
				roomImage = (ImageView) roomCard.findViewById(R.id.image_room);
			}
			imageLoader.load(imageURL).placeholder(R.drawable.img_sample).into(roomImage, new Callback.EmptyCallback() {
				@Override public void onSuccess() {
					loadingProgressBar.setVisibility(View.GONE);
				}
				@Override
				public void onError() {
					loadingProgressBar.setVisibility(View.GONE);
				}
			});*/


			this.room_name = roomData.optString("room_name");
			roomName.setText(this.room_name);

			this.pen_name = roomData.optString("pen_name");
			pensionName.setText(this.pen_name);

			rangeNumberOfPeople.setText(roomData.optString("room_std_people") + " / " + roomData.optString("room_max_people"));
			int roomPriceInt = roomData.optInt("room_cost");
			if (roomPriceInt == 0) {
				roomPrice.setText(R.string.telephone_inquiry);
			} else {
				roomPrice.setText(castRoomPriceToString(roomPriceInt));
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
			Log.d(TAG, this.room_name);
			// ***************receive the specific info data of the room from the server;
			try {
				new RoomInfoDataDownloadingTask(this.pen_id).execute(MTPLEASE_URL +
						"pension?pen_id="+ this.pen_id + "&date=" + mtDate + "&room_name=" + URLEncoder.encode(this.room_name, "utf-8").replaceAll("\\+", "%20"));
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, "Wrong URL sent!");
				e.printStackTrace();
			}
		}
	}

	/**
	 * @author In-Ho
	 * AsyncTask for receiving data from the server.
	 * Used AsyncTask to perform the task in the background
	 */
	/*private class RoomThumbnailImageLoadingTask extends AsyncTask<String, Integer, Bitmap> {

		private static final int IMAGE_LOADING_SUCCEEDED = 1;
		private static final int IMAGE_LOADING_FAILED = -1;
		private final WeakReference imageViewReference;
		private ProgressBar imageLoadingProgressBar;
		private int isImageLoaded;

		public RoomThumbnailImageLoadingTask(ImageView roomImageView, ProgressBar loadingProgressBar) {
			imageViewReference = new WeakReference(roomImageView);
			this.imageLoadingProgressBar = loadingProgressBar;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// configure progress bar
			imageLoadingProgressBar.setVisibility(View.VISIBLE);
			imageLoadingProgressBar.setProgress(0);
			// end of the configuration of the progress bar
		}

		@Override
		protected Bitmap doInBackground(String... urls) {
			try {
				isImageLoaded = IMAGE_LOADING_FAILED;

				HttpEntity resEntityGet = getHttpEntityFromServer(urls[0]);

				if(resEntityGet != null) {
					Log.i(TAG,"Receiving room image from the server succeeded!");
					InputStream inputStream = null;
					try {
						inputStream = resEntityGet.getContent();
						final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
						isImageLoaded = IMAGE_LOADING_SUCCEEDED;
						return bitmap;
					} finally {
						if (inputStream != null) {
							inputStream.close();
						}
						resEntityGet.consumeContent();
					}
				}
			} catch(IOException e) {
				Log.i(TAG,"Receiving room image from the server failed....");
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap roomImage) {
			super.onPostExecute(roomImage);
			if(isImageLoaded == IMAGE_LOADING_FAILED) {
				roomImage = null;
			}

			if(imageViewReference != null) {
				ImageView roomImageView = (ImageView) imageViewReference.get();

				if(roomImageView != null) {
					if(roomImage != null) {
						roomImageView.setImageBitmap(roomImage);
						roomImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
					}
					else {
						// image for replacement when there is no image for the room or when the loading of the image
						// fails.
						roomImageView.setImageBitmap(null);
					}
				}
			}

			// configure progress bar
			imageLoadingProgressBar.setVisibility(View.GONE);
			imageLoadingProgressBar.setProgress(100);
			// end of the configuration of the progress bar
		}
	}*/

	/**
	 * @author In-Ho
	 * AsyncTask for receiving specific room data from the server
	 * Used AsyncTask to perform the task in the background.
	 */
	private class RoomInfoDataDownloadingTask extends AsyncTask<String, Integer, String> {

		private int pen_id;
		private RoomInfoModel mRoomInfoModel;

		private RoomInfoDataDownloadingTask(int pen_id) {
			this.pen_id = pen_id;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mOnResultFragmentInteractionListener.onPreLoadSpecificInfoFragment();
		}

		@Override
		protected String doInBackground(String... urls) {

			try {
				Log.d(TAG, urls[0]);
				HttpEntity resEntityGet = getHttpEntityFromServer(urls[0]);

				if(resEntityGet != null) {
					Log.i(TAG,"HttpResponseGet Completed!!");
					return EntityUtils.toString(resEntityGet);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String jsonString) {
			super.onPostExecute(jsonString);

			try {
				JSONObject roomInfoList = new JSONObject(jsonString);
				Log.d(TAG, roomInfoList.toString());
				roomInfoList = new JSONObject(roomInfoList.getString("roomInfo"));
				// set all data of the room into the model instance
				mRoomInfoModel = new RoomInfoModel();
				mRoomInfoModel.setPen_id(roomInfoList.optInt("pen_id"));
				mRoomInfoModel.setRoom_name(roomInfoList.optString("room_name"));
				mRoomInfoModel.setPen_period_division(roomInfoList.optString("pen_period_division"));
				mRoomInfoModel.setPeriod_start(roomInfoList.optString("period_start"));
				mRoomInfoModel.setPeriod_end(roomInfoList.optString("period_end"));
				mRoomInfoModel.setWeekdays(roomInfoList.optInt("weekdays"));
				mRoomInfoModel.setFriday(roomInfoList.optInt("friday"));
				mRoomInfoModel.setWeekends(roomInfoList.optInt("weekends"));
				mRoomInfoModel.setRoom_std_people(roomInfoList.optInt("room_std_people"));
				mRoomInfoModel.setRoom_max_people(roomInfoList.optInt("room_max_people"));
				mRoomInfoModel.setRoom_pyeong(roomInfoList.optInt("room_pyeong"));
				mRoomInfoModel.setNum_rooms(roomInfoList.optInt("num_rooms"));
				mRoomInfoModel.setNum_toilets(roomInfoList.optInt("num_toilets"));
				mRoomInfoModel.setRoom_aircon(roomInfoList.optInt("room_aircon"));
				mRoomInfoModel.setRoom_equipment(roomInfoList.optString("room_equipment"));
				mRoomInfoModel.setRoom_description(roomInfoList.optString("room_description"));
				mRoomInfoModel.setPen_region(roomInfoList.optString("pen_region"));
				mRoomInfoModel.setPen_name(roomInfoList.optString("pen_name"));
				mRoomInfoModel.setPen_homepage(roomInfoList.optString("pen_homepage"));
				mRoomInfoModel.setPen_lot_adr(roomInfoList.optString("pen_lot_adr"));
				mRoomInfoModel.setPen_road_adr(roomInfoList.optString("pen_road_adr"));
				mRoomInfoModel.setPen_latitude(roomInfoList.optDouble("pen_latitude"));
				mRoomInfoModel.setPen_longitude(roomInfoList.optDouble("pen_longitude"));
				mRoomInfoModel.setPen_ceo(roomInfoList.optString("pen_ceo"));
				mRoomInfoModel.setPen_phone1(roomInfoList.optString("pen_phone1"));
				mRoomInfoModel.setPen_phone2(roomInfoList.optString("pen_phone2"));
				mRoomInfoModel.setPen_checkin(roomInfoList.optString("pen_checkin"));
				mRoomInfoModel.setPen_checkout(roomInfoList.optString("pen_checkout"));
				mRoomInfoModel.setPen_check_caution(roomInfoList.optString("pen_check_caution"));
				mRoomInfoModel.setPen_pickup(roomInfoList.optInt("pen_pickup"));
				mRoomInfoModel.setPen_pickup_description(roomInfoList.optString("pen_pickup_description"));
				mRoomInfoModel.setPen_barbecue(roomInfoList.optInt("pen_barbecue"));
				mRoomInfoModel.setPen_barbecue_description(roomInfoList.optString("pen_barbecue_description"));
				mRoomInfoModel.setPen_ground(roomInfoList.optInt("pen_ground"));
				mRoomInfoModel.setPen_ground_description(roomInfoList.optString("pen_ground_description"));
				mRoomInfoModel.setPen_valley(roomInfoList.optInt("pen_valley"));
				mRoomInfoModel.setPen_valley_description(roomInfoList.optString("pen_valley_description"));
				mRoomInfoModel.setPen_etc_facility(roomInfoList.optString("pen_etc_facility"));
				mRoomInfoModel.setPen_caution(roomInfoList.optString("pen_caution"));
				mRoomInfoModel.setPen_cost_caution(roomInfoList.optString("pen_cost_caution"));
				mRoomInfoModel.setPen_walk_station(roomInfoList.optString("pen_walk_station"));
				mRoomInfoModel.setPen_walk_terminal(roomInfoList.optString("pen_walk_terminal"));
				mRoomInfoModel.setPen_picture_flag(roomInfoList.optInt("pen_picture_flag"));
				mRoomInfoModel.setCost_table(roomInfoList.optJSONArray("cost_table"));
				mRoomInfoModel.setPeriod_table(roomInfoList.optJSONArray("period_table"));
				mRoomInfoModel.setRoom_cost(roomInfoList.optInt("room_cost"));
				// end of the setting

				mOnResultFragmentInteractionListener.onLoadSpecificInfoFragment(mRoomInfoModel, roomArray);

				mOnResultFragmentInteractionListener.onPostLoadSpecificInfoFragment();

			} catch (JSONException e) {
				Log.e(TAG, "Error receiving room data");
				e.printStackTrace();
			}
		}
	}

	private HttpEntity getHttpEntityFromServer(String url) throws IOException {
		try {
			HttpClient mHttpClient = new DefaultHttpClient();
			HttpGet mHttpGet = new HttpGet(url);
			HttpConnectionParams.setConnectionTimeout(mHttpClient.getParams(), 5000);
			HttpResponse mHttpResponseGet = mHttpClient.execute(mHttpGet);
			HttpEntity resEntityGet = mHttpResponseGet.getEntity();
			return resEntityGet;
		} catch(ClientProtocolException e) {
			Log.e(TAG, "ClientProtocolException");
			e.printStackTrace();
		}
		return null;
	}
}
