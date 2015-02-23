package com.owo.mtplease;

import android.util.Log;

import com.owo.mtplease.fragment.ShoppingItemListFragment;

import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by In-Ho on 2015-01-27.
 */
public class PlanModelController {
	
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
	private LinkedList<Integer> directInputRoomDataLinkedList;
	// End of the room info

	// Meat info
	private LinkedList<String> meatDataLinkedList;
	private LinkedList<Integer> meatCountLinkedList;
	// End of the meat info

	// Alcohol info
	private LinkedList<String> alcoholDataLinkedList;
	private LinkedList<Integer> alcoholCountLinkedList;
	// End of the alcohol info

	// Others Info
	private LinkedList<String> othersDataLinkedList;
	private LinkedList<Integer> othersCountLinkedList;
	// End of the alcohol info

	public PlanModelController() {
		initPlanModel();
	}

	public void initPlanModel() {
		date = "";
		region = 0;
		numPeople = 0;
		numMale = 0;
		numFemale = 0;

		roomDataLinkedList = new LinkedList();
		directInputRoomDataLinkedList = new LinkedList();
		meatDataLinkedList = new LinkedList();
		meatCountLinkedList = new LinkedList();
		alcoholDataLinkedList = new LinkedList();
		alcoholCountLinkedList = new LinkedList();
		othersDataLinkedList = new LinkedList();
		othersCountLinkedList = new LinkedList();
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

	public void addDirectInputRoomData(int roomPrice) {
		directInputRoomDataLinkedList.add((Integer)roomPrice);
		Log.d(TAG, directInputRoomDataLinkedList.toString());
	}

	public void removeRoomData(int penId, String roomName, int roomPrice) {
		if(roomDataLinkedList.size() > 0) {
			roomDataLinkedList.remove(penId + "!" + roomName + "!" + roomPrice);
			Log.d(TAG, roomDataLinkedList.toString());
		}
	}

	public void removeRoomData(int roomIndex) {
		if(roomDataLinkedList.size() > 0) {
			roomDataLinkedList.remove(roomIndex);
			Log.d(TAG, roomDataLinkedList.toString());
		}
	}

/*	public void removeDirectInputRoomData(int roomPrice) {
		if(directInputRoomDataLinkedList.size() > 0) {
			directInputRoomDataLinkedList.remove((Integer)roomPrice);
			Log.d(TAG, directInputRoomDataLinkedList.toString());
		}
	}*/

	public void removeDirectInputRoomData(int roomIndex) {
		if(directInputRoomDataLinkedList.size() > 0) {
			directInputRoomDataLinkedList.remove(roomIndex);
			Log.d(TAG, directInputRoomDataLinkedList.toString());
		}
	}

	public void clearRoomData() {
		if(roomDataLinkedList.size() > 0)
			roomDataLinkedList.clear();

		if(directInputRoomDataLinkedList.size() > 0)
			directInputRoomDataLinkedList.clear();
	}

	public int getRoomDataCount() {
		return roomDataLinkedList.size();
	}

	public int getDirectInputRoomDataCount() {
		return directInputRoomDataLinkedList.size();
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

		for(int i = 0; i< getDirectInputRoomDataCount(); i++) {
			totalRoomCost += directInputRoomDataLinkedList.get(i);
		}

		return totalRoomCost;
	}

	public void addItemData(int itemType, String itemName, int itemUnitPrice, int itemCount) {
		String itemDataString = itemName + "!" + itemUnitPrice;

		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				meatDataLinkedList.add(itemDataString);
				meatCountLinkedList.add(itemCount);
				Log.d(TAG, meatDataLinkedList.toString());
				Log.d(TAG, meatCountLinkedList.toString());
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				alcoholDataLinkedList.add(itemDataString);
				alcoholCountLinkedList.add(itemCount);
				Log.d(TAG, alcoholDataLinkedList.toString());
				Log.d(TAG, alcoholCountLinkedList.toString());
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				othersDataLinkedList.add(itemDataString);
				othersCountLinkedList.add(itemCount);
				Log.d(TAG, othersDataLinkedList.toString());
				Log.d(TAG, othersCountLinkedList.toString());
				break;
		}
	}
	
	public void removeItemData(int itemType, String itemName, int itemUnitPrice) {
		String itemDataString = itemName + "!" + itemUnitPrice;
		int itemIndex;

		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				if(meatDataLinkedList.size() > 0) {
					itemIndex = meatDataLinkedList.indexOf(itemDataString);
					meatDataLinkedList.remove(itemIndex);
					meatCountLinkedList.remove(itemIndex);

					Log.d(TAG, meatDataLinkedList.toString());
					Log.d(TAG, meatCountLinkedList.toString());
				}
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				if(alcoholDataLinkedList.size() > 0) {
					itemIndex = alcoholDataLinkedList.indexOf(itemDataString);
					alcoholDataLinkedList.remove(itemIndex);
					alcoholCountLinkedList.remove(itemIndex);

					Log.d(TAG, alcoholDataLinkedList.toString());
					Log.d(TAG, alcoholCountLinkedList.toString());
				}
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				if(othersDataLinkedList.size() > 0) {
					itemIndex = othersDataLinkedList.indexOf(itemDataString);
					othersDataLinkedList.remove(itemIndex);
					othersCountLinkedList.remove(itemIndex);

					Log.d(TAG, othersDataLinkedList.toString());
					Log.d(TAG, othersCountLinkedList.toString());
				}
				break;
		}
	}

	public void removeItemData(int itemType, int itemIndex) {
		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				if(meatDataLinkedList.size() > 0) {
					meatDataLinkedList.remove(itemIndex);
					meatCountLinkedList.remove(itemIndex);

					Log.d(TAG, meatDataLinkedList.toString());
					Log.d(TAG, meatCountLinkedList.toString());
				}
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				if(alcoholDataLinkedList.size() > 0) {
					alcoholDataLinkedList.remove(itemIndex);
					alcoholCountLinkedList.remove(itemIndex);

					Log.d(TAG, alcoholDataLinkedList.toString());
					Log.d(TAG, alcoholCountLinkedList.toString());
				}
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				if(othersDataLinkedList.size() > 0) {
					othersDataLinkedList.remove(itemIndex);
					othersCountLinkedList.remove(itemIndex);

					Log.d(TAG, othersDataLinkedList.toString());
					Log.d(TAG, othersCountLinkedList.toString());
				}
				break;
		}
	}

	public void clearItemData(int itemType) {
		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				if(meatDataLinkedList.size() > 0) {
					meatDataLinkedList.clear();
					meatCountLinkedList.clear();
				}
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				if(alcoholDataLinkedList.size() > 0) {
					alcoholDataLinkedList.clear();
					alcoholCountLinkedList.clear();
				}
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				if(othersDataLinkedList.size() > 0) {
					othersDataLinkedList.clear();
					othersCountLinkedList.clear();
				}
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

	public String getItemName(int itemType, int itemIndex) {
		String itemData[];
		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				itemData = meatDataLinkedList.get(itemIndex).split("!");
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				itemData = alcoholDataLinkedList.get(itemIndex).split("!");
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				itemData = othersDataLinkedList.get(itemIndex).split("!");
				break;
			default:
				return null;
		}
		return itemData[0];
	}

	public int getItemUnitPrice(int itemType, int itemIndex) {
		String itemData[];
		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				itemData = meatDataLinkedList.get(itemIndex).split("!");
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				itemData = alcoholDataLinkedList.get(itemIndex).split("!");
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				itemData = othersDataLinkedList.get(itemIndex).split("!");
				break;
			default:
				return 0;
		}
		return Integer.parseInt(itemData[1]);
	}

	/**
	 * checks rather an item to be added is inside the plan already or not
	 *
	 * @param itemType      type of the item to add(ShoppingItemListFragment.MEAT_ITEM, ShoppingItemListFragment.ALCOHOL_ITEM, ShoppingItemListFragment.OTHRES_ITEM)
	 * @param itemName      name of the item to add
	 * @param itemUnitPrice unit price of the item to add
	 * @return	true if the item is already inside the plan, false if the item is not inside the plan
	 */
	public boolean isItemAddedAlready(int itemType, String itemName, int itemUnitPrice) {
		String itemDataString = itemName + "!" + itemUnitPrice;

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

	public void changeItemCount(int itemType, String itemName, int itemUnitPrice, int newItemCount) {
		String itemDataString = itemName + "!" + itemUnitPrice;
		int itemIndex = 0;

		switch (itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				itemIndex = meatDataLinkedList.indexOf(itemDataString);
				meatCountLinkedList.set(itemIndex, newItemCount);
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				itemIndex = alcoholDataLinkedList.indexOf(itemDataString);
				alcoholCountLinkedList.set(itemIndex, newItemCount);
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				itemIndex = othersDataLinkedList.indexOf(itemDataString);
				othersCountLinkedList.set(itemIndex, newItemCount);
				break;
		}
	}

	public int getSingleItemCount(int itemType, String itemName, int itemUnitPrice) {
		String itemDataString = itemName + "!" + itemUnitPrice;
		int itemIndex = 0;

		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				itemIndex = meatDataLinkedList.indexOf(itemDataString);
				return meatCountLinkedList.get(itemIndex);
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				itemIndex = alcoholDataLinkedList.indexOf(itemDataString);
				return alcoholCountLinkedList.get(itemIndex);
			case ShoppingItemListFragment.OTHERS_ITEM:
				itemIndex = othersDataLinkedList.indexOf(itemDataString);
				return othersCountLinkedList.get(itemIndex);
		}

		return 0;
	}

	public int getTotalItemCost(int itemType) {
		int totalItemCost = 0;
		for(int i = 0; i < getItemDataCount(itemType); i++) {
			String itemData;
			int itemCount;
			switch(itemType) {
				case ShoppingItemListFragment.MEAT_ITEM:
					itemData = meatDataLinkedList.get(i);
					itemCount = meatCountLinkedList.get(i);
					break;
				case ShoppingItemListFragment.ALCOHOL_ITEM:
					itemData = alcoholDataLinkedList.get(i);
					itemCount = alcoholCountLinkedList.get(i);
					break;
				case ShoppingItemListFragment.OTHERS_ITEM:
					itemData = othersDataLinkedList.get(i);
					itemCount = othersCountLinkedList.get(i);
					break;
				default:
					return 0;
			}
			String temp[] = itemData.split("!");
			totalItemCost += (Integer.parseInt(temp[1]) * itemCount);
			Log.d(TAG, totalItemCost + "");
		}

		return totalItemCost;
	}

	public int getPlanTotalCost() {
		int totalCost = getTotalRoomCost();
		totalCost += getTotalItemCost(ShoppingItemListFragment.MEAT_ITEM);
		totalCost += getTotalItemCost(ShoppingItemListFragment.ALCOHOL_ITEM);
		totalCost += getTotalItemCost(ShoppingItemListFragment.OTHERS_ITEM);

		return totalCost;
	}

	public JSONObject getPlanJSONObjectForRequest() {
		JSONObject planJSONObject = new JSONObject();

		return planJSONObject;
	}
}
