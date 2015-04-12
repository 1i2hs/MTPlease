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
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by In-Ho on 2015-01-07.
 */
public class HomePostListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final String TAG = "TimelinePostListRecyclerViewAdapter";

	private static final int TYPE_HEADER = 0;
	private static final int TYPE_TITLE = 1;
	private static final int TYPE_ITEM = 2;

	private Context mContext;
	private JSONArray postArray;

	private static String imageUrl;

	public HomePostListRecyclerViewAdapter(Context context, JSONArray jsonArray) {
		mContext = context;
		postArray = jsonArray;

		if (jsonArray != null) {
			preLoadImage();
		}
	}

	private void preLoadImage() {
		for (int i = 0; i < postArray.length(); i++) {
			try {
				JSONObject timelinePostData = postArray.getJSONObject(i);

				imageUrl = mContext.getResources().getString(R.string.mtplease_url) + "img/timeline/" + timelinePostData.optInt("timeline_id") + ".jpg";

				Log.d(TAG, imageUrl);

				if (!ServerCommunicationManager.getInstance(mContext).containsImage(imageUrl)) {
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
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == TYPE_ITEM) {
			View itemView;

			if (postArray != null) {
				itemView = LayoutInflater.from(parent.getContext())
						.inflate(R.layout.card_home, parent, false);
				return new PostCard(itemView, mContext);
			} else {
				itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_swipe_to_refresh, parent, false);
				return new SwipeToRefreshCard(itemView);
			}
		} else if (viewType == TYPE_TITLE) {
			View titleView = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_home_title, parent, false);
			return new BlankHeader(titleView);
		} else if (viewType == TYPE_HEADER) {
			View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_header_placeholder_large, parent, false);
			return new BlankHeader(headerView);
		}

		throw new RuntimeException("there is no type that matches the type " + viewType + "make sure your using types correctly");
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof PostCard) {
			try {
				if (postArray != null) {
					JSONObject postData = postArray.getJSONObject(position - 2);
					((PostCard) holder).setItem(postData);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getItemCount() {
		if (postArray != null) {
			return postArray.length() + 2;
		} else {
			return 2;
		}
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return TYPE_HEADER;
		} else if (position == 1) {
			return TYPE_TITLE;
		} else {
			return TYPE_ITEM;
		}
	}

	private static class PostCard extends RecyclerView.ViewHolder {
		private CardView postCard;
		private final WeakReference<ImageView> imageViewReference;
		private TextView postTitle;
		private TextView postDate;
		private String linkURL = null;
		private Context mContext;

		public PostCard(View cardView, Context context) {
			super(cardView);
			postCard = (CardView) cardView;
			imageViewReference = new WeakReference<ImageView>((ImageView) cardView.findViewById(R.id.imageView_post));
			postTitle = (TextView) cardView.findViewById(R.id.textView_post_title);
			//postTitle.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			postDate = (TextView) cardView.findViewById(R.id.textView_post_date);
			//postDate.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			mContext = context;
		}

		public void setItem(JSONObject postData) throws JSONException {
			imageUrl = mContext.getResources().getString(R.string.mtplease_url) + "img/timeline/" + postData.optInt("timeline_id") + ".jpg";

			Log.d(TAG, imageUrl);

			final ImageView postImage = imageViewReference.get();

			ServerCommunicationManager.getInstance(mContext).getImage(imageUrl, new ImageLoader.ImageListener() {
				@Override
				public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
					if (response.getBitmap() != null) {
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
			postCard.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Uri webLink = Uri.parse(linkURL);
					Intent webBrowseIntent = new Intent(Intent.ACTION_VIEW, webLink);
					v.getContext().startActivity(webBrowseIntent);
				}
			});
			postCard.setPreventCornerOverlap(false);
		}
	}

	private static class BlankHeader extends RecyclerView.ViewHolder {
		public BlankHeader(View cardView) {
			super(cardView);
		}
	}

	private static class SwipeToRefreshCard extends RecyclerView.ViewHolder {
		public SwipeToRefreshCard(View cardView) {
			super(cardView);
		}
	}
}

