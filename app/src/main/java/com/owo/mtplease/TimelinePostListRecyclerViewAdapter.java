package com.owo.mtplease;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by In-Ho on 2015-01-07.
 */
public class TimelinePostListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final String TAG = "TimelinePostListRecyclerViewAdapter";

	private static final int TYPE_HEADER = 0;
	private static final int TYPE_ITEM = 1;

	private Context mContext;
	private JSONArray postArray;

	public TimelinePostListRecyclerViewAdapter(Context context, JSONArray jsonArray) {
		this.mContext = context;
		this.postArray = jsonArray;
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

			return new VHHeader(headerView);
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
		else if(holder instanceof VHHeader) {
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

	public static class PostCard extends RecyclerView.ViewHolder implements View.OnClickListener {
		private CardView postCard;
		private LinearLayout postLayout;
		private ImageView postImage;
		private TextView postTitle;
		private TextView postDate;
		private String linkURL = null;
		private Context mContext;

		public PostCard(View cardView, Context mContext) {
			super(cardView);
			postCard = (CardView) cardView;
			postLayout = (LinearLayout) cardView.findViewById(R.id.LinearLayout_card_timeline);
			postImage = (ImageView) cardView.findViewById(R.id.image_post);
			postTitle = (TextView) cardView.findViewById(R.id.text_post_title);
			postDate = (TextView) cardView.findViewById(R.id.text_post_date);
			this.mContext = mContext;
		}

		public void setItem(JSONObject postData) throws JSONException {
			postImage.setImageResource(R.drawable.img_sample);
			postImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
			mContext.startActivity(webBrowseIntent);
		}
	}

	public static class VHHeader extends RecyclerView.ViewHolder {
		public VHHeader(View cardView) {
			super(cardView);
		}
	}
}

