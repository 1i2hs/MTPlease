package com.owo.mtplease;

import android.util.Log;

import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by In-Ho on 2015-01-27.
 */
public class PlanModel {
	
	private static final String TAG = "PlanModel";

	// Basic Info written in the plan
	private String date;
	private int region;
	private int numPeople;
	private int numMale;
	private int numFemale;
	// End of the basic info

	// Room info
	private LinkedList<String> roomDataLinkedList;
	// End of the room info

	// Meat info
	private LinkedList<String> meatDataLinkedList;
	// End of the meat info

	// Alcohol info
	private LinkedList<String> alcoholDataLinkedList;
	// End of the alcohol info

	// Others Info
	private LinkedList<String> othersDataLinkedList;
	// End of the alcohol info

	public PlanModel() {
		initPlanModel();
	}

	public void initPlanModel() {
		date = "";
		region = 0;
		numPeople = 0;
		numMale = 0;
		numFemale = 0;

		roomDataLinkedList = new LinkedList();
		meatDataLinkedList = new LinkedList();
		alcoholDataLinkedList = new LinkedList();
		othersDataLinkedList = new LinkedList();
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public void setNumPeople(int numPeople) {
		this.numPeople = numPeople;
	}

	public void setNumMale(int numMale) {
		this.numMale = numMale;
	}

	public void setNumFemale(int numFemale) {
		this.numFemale = numFemale;
	}

	public void addRoomData(int penId, String roomName, int roomPrice) {
		roomDataLinkedList.add(penId + "!" + roomName + "!" + roomPrice);
		Log.d(TAG, roomDataLinkedList.toString());
	}

	public void removeRoomData(int penId, String roomName, int roomPrice) {
		if(roomDataLinkedList.size() > 0) {
			roomDataLinkedList.remove(penId + "!" + roomName + "!" + roomPrice);
			Log.d(TAG, roomDataLinkedList.toString());
		}
	}

	public void clearRoomData() {
		if(roomDataLinkedList.size() > 0)
			roomDataLinkedList.clear();
	}

	public int getRoomDataCount() {
		return roomDataLinkedList.size();
	}

	public boolean isRoomAddedAlready(int penId, String roomName, int roomPrice) {
		return roomDataLinkedList.contains(penId + "!" + roomName + "!" + roomPrice);
	}

	public int getTotalRoomCost() {
		int totalRoomCost = 0;
		for(int i = 0; i < getRoomDataCount(); i++) {
			String roomData = roomDataLinkedList.get(i);
			String temp[] = roomData.split("!");
			totalRoomCost += Integer.parseInt(temp[2]);
		}

		return totalRoomCost;
	}

	public void addItemData(int itemType, String itemName, int itemUnitPrice, int itemCount) {
		String itemDataString = itemName + "!" + itemUnitPrice + "!" + itemCount;

		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				meatDataLinkedList.add(itemDataString);
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				alcoholDataLinkedList.add(itemDataString);
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				othersDataLinkedList.add(itemDataString);
				break;
		}
	}
	
	public void removeItemData(int itemType, String itemName, int itemUnitPrice, int itemCount) {
		String itemDataString = itemName + "!" + itemUnitPrice + "!" + itemCount;

		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				if(meatDataLinkedList.size() > 0)
					meatDataLinkedList.remove(itemDataString);
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				if(alcoholDataLinkedList.size() > 0)
					alcoholDataLinkedList.remove(itemDataString);
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				if(othersDataLinkedList.size() > 0)
					othersDataLinkedList.remove(itemDataString);
				break;
		}
	}

	public void clearItemData(int itemType) {
		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				if(meatDataLinkedList.size() > 0)
					meatDataLinkedList.clear();
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				if(alcoholDataLinkedList.size() > 0)
					alcoholDataLinkedList.clear();
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				if(othersDataLinkedList.size() > 0)
					othersDataLinkedList.clear();
				break;
		}
	}
	
	public int getItemDataCount(int itemType) {
		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				return meatDataLinkedList.size();
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				return alcoholDataLinkedList.size();
			case ShoppingItemListFragment.OTHERS_ITEM:
				return othersDataLinkedList.size();
			default:
				return -1;
		}
	}

	/**
	 * checks rather an item to be added is inside the plan already or not
	 *
	 * @param itemType      type of the item to add(ShoppingItemListFragment.MEAT_ITEM, ShoppingItemListFragment.ALCOHOL_ITEM, ShoppingItemListFragment.OTHRES_ITEM)
	 * @param itemName      name of the item to add
	 * @param itemUnitPrice unit price of the item to add
	 * @param itemCount    number of the item to add
	 * @return	true if the item is already inside the plan, false if the item is not inside the plan
	 */
	public boolean isItemAddedAlready(int itemType, String itemName, int itemUnitPrice, int itemCount) {
		String itemDataString = itemName + "!" + itemUnitPrice + "!" + itemCount;

		switch (itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				return meatDataLinkedList.contains(itemDataString);
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				return alcoholDataLinkedList.contains(itemDataString);
			case ShoppingItemListFragment.OTHERS_ITEM:
				return othersDataLinkedList.contains(itemDataString);
			default:
				return false;
		}
	}

	public JSONObject getPlanJSONObjectForRequest() {
		JSONObject planJSONObject = new JSONObject();

		return planJSONObject;
	}
}
