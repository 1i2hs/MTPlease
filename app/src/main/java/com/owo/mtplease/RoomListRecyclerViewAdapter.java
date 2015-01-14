package com.owo.mtplease;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private FragmentManager mFragmentManager;
    private JSONArray roomArray;
    private String dateMT;

    public RoomListRecyclerViewAdapter(Context context, FragmentManager fragmentManager, JSONArray jsonArray, String date) {
        Log.d(TAG, "RoomListRecyclerViewAdapter");
        this.mContext = context;
        this.mFragmentManager = fragmentManager;
        this.roomArray = jsonArray;
        this.dateMT = date;
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
            View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_header_placeholder, parent, false);

            return new VHHeader(headerView);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + "make sure your using types correctly");
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
        else if(holder instanceof VHHeader) {

        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount");
        try {
            return roomArray.length() + 1;
        } catch(NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType");
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public static class VHHeader extends RecyclerView.ViewHolder {
        public VHHeader(View cardView) {
            super(cardView);
        }
    }

    public class RoomCard extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            loadingProgressBar = (ProgressBar) cardView.findViewById(R.id.progressBar_image_room);
            roomName = (TextView) cardView.findViewById(R.id.text_room_name);
            pensionName = (TextView) cardView.findViewById(R.id.text_pension_name);
            rangeNumberOfPeople = (TextView) cardView.findViewById(R.id.text_number_range_of_people);
            roomPrice = (TextView) cardView.findViewById(R.id.text_price);
        }

        public void setRoomData(JSONObject roomData) throws UnsupportedEncodingException, JSONException {

            pen_id = roomData.getInt("pen_id");

            String imageURL;
            if(Integer.parseInt(roomData.getString("pen_picture_flag")) == REAL_PICTURE_EXISTS)
                imageURL = MTPLEASE_URL + "img/pensions/" + roomData.getString("pen_id") + "/" + URLEncoder.encode(roomData.getString("room_name"), "utf-8").replaceAll("\\+","%20") + "/real/thumbnail.png";
            else
                imageURL = MTPLEASE_URL + "img/pensions/" + roomData.getString("pen_id") + "/" + URLEncoder.encode(roomData.getString("room_name"), "utf-8").replaceAll("\\+","%20") + "/unreal/thumbnail.png";

            Log.i(TAG, imageURL);
            new ImageLoadingTask(roomImage, loadingProgressBar).execute(imageURL);

            this.room_name = roomData.getString("room_name");
            roomName.setText(this.room_name);

            this.pen_name = roomData.getString("pen_name");
            pensionName.setText(this.pen_name);

            rangeNumberOfPeople.setText(roomData.getString("room_std_people") + " / " + roomData.getString("room_max_people"));
            int roomPriceInt = Integer.parseInt(roomData.getString("room_cost"));
            if (roomPriceInt == 0) {
                roomPrice.setText("전화 문의");
            } else {
                roomPrice.setText(roomPriceInt + "");
            }
            roomCard.setPreventCornerOverlap(false);
        }

        public LinearLayout getLayout() {
            return roomLayout;
        }


        @Override
        public void onClick(View v) {
            SpecificInfoFragment mSpecificInfoFragment = SpecificInfoFragment.newInstance(this.pen_id, dateMT, this.room_name, this.pen_name);

            // commit the SpecificInfoFragment to the current view
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.background_loading, mSpecificInfoFragment);
            mFragmentTransaction.addToBackStack(null);
            mFragmentTransaction.commit();
            // end of commission
        }
    }

    /**
     * @author In-Ho
     * AsyncTask for receiving data from the server.
     * Used AsyncTask to perform the task in the background
     */
    private class ImageLoadingTask extends AsyncTask<String, Integer, Bitmap> {

        private static final int IMAGE_LOADING_SUCCEEDED = 1;
        private static final int IMAGE_LOADING_FAILED = -1;
        private final WeakReference imageViewReference;
        private ProgressBar loadingProgressBar;
        private int isImageLoaded;

        public ImageLoadingTask(ImageView roomImageView, ProgressBar loadingProgressBar) {
            imageViewReference = new WeakReference(roomImageView);
            this.loadingProgressBar = loadingProgressBar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // configure progress bar
            loadingProgressBar.setVisibility(View.VISIBLE);
            loadingProgressBar.setProgress(0);
            // end of the configuration of the progress bar
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                isImageLoaded = IMAGE_LOADING_FAILED;
                HttpClient mHttpClient = new DefaultHttpClient();
                HttpGet mHttpGet = new HttpGet(urls[0]);
                HttpConnectionParams.setConnectionTimeout(mHttpClient.getParams(), 5000);
                HttpResponse mHttpResponseGet = mHttpClient.execute(mHttpGet);
                HttpEntity resEntityGet = mHttpResponseGet.getEntity();

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
            } catch(Exception e) {
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
            loadingProgressBar.setVisibility(View.GONE);
            loadingProgressBar.setProgress(100);
            // end of the configuration of the progress bar
        }
    }
}
