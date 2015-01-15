package com.owo.mtplease;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

        // ***************receive the specific info data of the room from the server;
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
                cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_warnings, parent, false);
                return new WarningsCard(cardView);
        }

        throw new RuntimeException("there is no type that matches the type " + cardViewType + "make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder cardViewHolder, int position) {
        Log.d(TAG, "onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        return 12;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType" + position);
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
        }

        @Override
        public int getCount() {
            return this.imageCount;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(roomImageVector.get(position));
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }

    private class PhoneAndWebsiteButtonsCard extends RecyclerView.ViewHolder {
        public PhoneAndWebsiteButtonsCard(View cardView) {
            super(cardView);
        }
    }

    private class PriceAndRateCard extends RecyclerView.ViewHolder {
        public PriceAndRateCard(View cardView) {
            super(cardView);
        }
    }

    private class BasicInfoCard extends RecyclerView.ViewHolder {
        public BasicInfoCard(View cardView) {
            super(cardView);
        }
    }

    private class OptionsCard extends RecyclerView.ViewHolder {
        public OptionsCard(View cardView) {
            super(cardView);
        }
    }

    private class VisitedSchoolsCard extends RecyclerView.ViewHolder {
        public VisitedSchoolsCard(View cardView) {
            super(cardView);
        }
    }

    private class ReviewsCard extends RecyclerView.ViewHolder {
        public ReviewsCard(View cardView) {
            super(cardView);
        }
    }

    private class AddressAndRouteAndMapCard extends RecyclerView.ViewHolder {
        public AddressAndRouteAndMapCard(View cardView) {
            super(cardView);
        }
    }

    private class PriceAndDateSelectionCard extends RecyclerView.ViewHolder {
        public PriceAndDateSelectionCard(View cardView) {
            super(cardView);
        }
    }

    private class OwnerInfoCard extends RecyclerView.ViewHolder {
        public OwnerInfoCard(View cardView) {
            super(cardView);
        }
    }

    private class OthersCard extends RecyclerView.ViewHolder {
        public OthersCard(View cardView) {
            super(cardView);
        }
    }

    private class WarningsCard extends RecyclerView.ViewHolder {
        public WarningsCard(View cardView) {
            super(cardView);
        }
    }
}
