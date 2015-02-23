package com.owo.mtplease;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.owo.mtplease.view.TypefaceLoader;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by In-Ho on 2015-01-07.
 */
public class TimelinePostListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final String TAG = "TimelinePostListRecyclerViewAdapter";
	private static final String MTPLEASE_URL = "http://mtplease.herokuapp.com/";

	private static final int TYPE_HEADER = 0;
	private static final int TYPE_ITEM = 1;

	private Context mContext;
	private JSONArray postArray;

	private static String imageUrl;

	public TimelinePostListRecyclerViewAdapter(Context context, JSONArray jsonArray) {
		mContext = context;
		postArray = jsonArray;

		preLoadImage();
	}

	private void preLoadImage() {
		for(int i = 0; i < postArray.length(); i++) {
			try {
				JSONObject timelinePostData = postArray.getJSONObject(i);

				imageUrl = MTPLEASE_URL + "img/timeline/" + timelinePostData.optInt("timeline_id") + ".jpg";

				Log.d(TAG, imageUrl);

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
			} catch(JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if(viewType == TYPE_ITEM) {
			//inflate your layout and pass it to view holder
			View itemView = LayoutInflater.from(parent.getContext())
						.inflate(R.layout.card_timeline, parent, false);

			return new PostCard(itemView, mContext);
		}
		else if(viewType == TYPE_HEADER) {
			View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_header_placeholder, parent, false);

			return new BlankHeader(headerView);
		}

		throw new RuntimeException("there is no type that matches the type " + viewType + "make sure your using types correctly");
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if(holder instanceof PostCard) {
			try {
				JSONObject postData = postArray.getJSONObject(position - 1);
				((PostCard) holder).setItem(postData);
				((PostCard) holder).getLayout().setOnClickListener((PostCard) holder);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch(NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getItemCount() {
		return postArray.length() + 1;
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

	private static class PostCard extends RecyclerView.ViewHolder implements View.OnClickListener {
		private CardView postCard;
		private LinearLayout postLayout;
		private ImageView postImage;
		private TextView postTitle;
		private TextView postDate;
		private String linkURL = null;
		private Context mContext;

		public PostCard(View cardView, Context context) {
			super(cardView);
			postCard = (CardView) cardView;
			postLayout = (LinearLayout) cardView.findViewById(R.id.LinearLayout_card_timeline);
			postImage = (ImageView) cardView.findViewById(R.id.image_post);
			postImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
			postTitle = (TextView) cardView.findViewById(R.id.text_post_title);
			postTitle.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			postDate = (TextView) cardView.findViewById(R.id.text_post_date);
			postDate.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			mContext = context;
		}

		public void setItem(JSONObject postData) throws JSONException {
			imageUrl = MTPLEASE_URL + "img/timeline/" + postData.optInt("timeline_id") + ".jpg";

			Log.d(TAG, imageUrl);

			ServerCommunicationManager.getInstance(mContext).getImage(imageUrl, new ImageLoader.ImageListener() {
						@Override
						public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
							if(response.getBitmap() != null) {
								postImage.setAlpha(0.0F);
								postImage.setImageBitmap(response.getBitmap());
								postImage.animate().alpha(1.0F);
							} else {
								postImage.setImageResource(R.drawable.scrn_room_img_place_holder);
							}
						}

						@Override
						public void onErrorResponse(VolleyError error) {
							postImage.setImageResource(R.drawable.scrn_room_img_error);
						}
					});

			postTitle.setText(postData.optString("timeline_title"));
			postDate.setText(postData.optString("timeline_datetime"));
			linkURL = postData.optString("timeline_link");
			postCard.setPreventCornerOverlap(false);
		}

		public LinearLayout getLayout() {
			return postLayout;
		}

		@Override
		public void onClick(View v) {
			Uri webLink = Uri.parse(linkURL);
			Intent webBrowseIntent = new Intent(Intent.ACTION_VIEW, webLink);
			//mContext.startActivity(webBrowseIntent);
			v.getContext().startActivity(webBrowseIntent);
		}
	}

	private static class BlankHeader extends RecyclerView.ViewHolder {
		public BlankHeader(View cardView) {
			super(cardView);
		}
	}
}

