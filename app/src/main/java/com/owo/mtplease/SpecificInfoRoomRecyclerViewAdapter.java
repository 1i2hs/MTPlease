package com.owo.mtplease;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

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

    private int pen_id;
    private String dateMT;
    private String room_name;

    public SpecificInfoRoomRecyclerViewAdapter(int pen_id, String dateMT, String room_name) {
        this.pen_id = pen_id;
        this.dateMT = dateMT;
        this.room_name = room_name;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int cardViewType) {
        Log.d(TAG, "onCreateViewHolder");

        int cardLayoutId = -1;

        switch(cardViewType) {
            case CARD_IMAGE_VIEW_PAGER:
                cardLayoutId = R.id.view_pager_room_images;
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
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType");
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
}
