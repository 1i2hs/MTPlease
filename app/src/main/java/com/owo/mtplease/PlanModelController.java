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
	private String _date;
	private int _region;
	private int _numPeople;
	private int _numMale;
	private int _numFemale;
	// End of the basic info

	// Room info
	private LinkedList<String> _roomDataLinkedList;
	private LinkedList<Integer> _directInputRoomDataLinkedList;
	// End of the room info

	// Meat info
	private LinkedList<String> _meatDataLinkedList;
	private LinkedList<Integer> _meatCountLinkedList;
	// End of the meat info

	// Alcohol info
	private LinkedList<String> _alcoholDataLinkedList;
	private LinkedList<Integer> _alcoholCountLinkedList;
	// End of the alcohol info

	// Others Info
	private LinkedList<String> _othersDataLinkedList;
	private LinkedList<Integer> _othersCountLinkedList;
	// End of the alcohol info

	public PlanModelController() {
		initPlanModel();
	}

	public void initPlanModel() {
		_date = "";
		_region = 0;
		_numPeople = 0;
		_numMale = 0;
		_numFemale = 0;

		_roomDataLinkedList = new LinkedList();
		_directInputRoomDataLinkedList = new LinkedList();
		_meatDataLinkedList = new LinkedList();
		_meatCountLinkedList = new LinkedList();
		_alcoholDataLinkedList = new LinkedList();
		_alcoholCountLinkedList = new LinkedList();
		_othersDataLinkedList = new LinkedList();
		_othersCountLinkedList = new LinkedList();
	}

	public void setDate(String _date) {
		this._date = _date;
	}

	public void setRegion(int _region) {
		this._region = _region;
	}

	public void setNumPeople(int _numPeople) {
		this._numPeople = _numPeople;
	}

	public void setNumMale(int _numMale) {
		this._numMale = _numMale;
	}

	public void setNumFemale(int _numFemale) {
		this._numFemale = _numFemale;
	}

	public void addRoomData(int penId, String roomName, int roomPrice) {
		_roomDataLinkedList.add(penId + "!" + roomName + "!" + roomPrice);
		Log.d(TAG, _roomDataLinkedList.toString());
	}

	public void addDirectInputRoomData(int roomPrice) {
		_directInputRoomDataLinkedList.add((Integer)roomPrice);
		Log.d(TAG, _directInputRoomDataLinkedList.toString());
	}

	public void removeRoomData(int penId, String roomName, int roomPrice) {
		if(_roomDataLinkedList.size() > 0) {
			_roomDataLinkedList.remove(penId + "!" + roomName + "!" + roomPrice);
			Log.d(TAG, _roomDataLinkedList.toString());
		}
	}

	public void removeRoomData(int roomIndex) {
		if(_roomDataLinkedList.size() > 0) {
			_roomDataLinkedList.remove(roomIndex);
			Log.d(TAG, _roomDataLinkedList.toString());
		}
	}

/*	public void removeDirectInputRoomData(int roomPrice) {
		if(_directInputRoomDataLinkedList.size() > 0) {
			_directInputRoomDataLinkedList.remove((Integer)roomPrice);
			Log.d(TAG, _directInputRoomDataLinkedList.toString());
		}
	}*/

	public void removeDirectInputRoomData(int roomIndex) {
		if(_directInputRoomDataLinkedList.size() > 0) {
			_directInputRoomDataLinkedList.remove(roomIndex);
			Log.d(TAG, _directInputRoomDataLinkedList.toString());
		}
	}

	public void clearRoomData() {
		if(_roomDataLinkedList.size() > 0)
			_roomDataLinkedList.clear();

		if(_directInputRoomDataLinkedList.size() > 0)
			_directInputRoomDataLinkedList.clear();
	}

	public int getRoomDataCount() {
		return _roomDataLinkedList.size();
	}

	public int getDirectInputRoomDataCount() {
		return _directInputRoomDataLinkedList.size();
	}

	public boolean isRoomAddedAlready(int penId, String roomName, int roomPrice) {
		return _roomDataLinkedList.contains(penId + "!" + roomName + "!" + roomPrice);
	}

	public int getTotalRoomCost() {
		int totalRoomCost = 0;
		for(int i = 0; i < getRoomDataCount(); i++) {
			String roomData = _roomDataLinkedList.get(i);
			String temp[] = roomData.split("!");
			totalRoomCost += Integer.parseInt(temp[2]);
		}

		for(int i = 0; i< getDirectInputRoomDataCount(); i++) {
			totalRoomCost += _directInputRoomDataLinkedList.get(i);
		}

		return totalRoomCost;
	}

	public void addItemData(int itemType, String itemName, int itemUnitPrice, int itemCount) {
		String itemDataString = itemName + "!" + itemUnitPrice;

		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				_meatDataLinkedList.add(itemDataString);
				_meatCountLinkedList.add(itemCount);
				Log.d(TAG, _meatDataLinkedList.toString());
				Log.d(TAG, _meatCountLinkedList.toString());
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				_alcoholDataLinkedList.add(itemDataString);
				_alcoholCountLinkedList.add(itemCount);
				Log.d(TAG, _alcoholDataLinkedList.toString());
				Log.d(TAG, _alcoholCountLinkedList.toString());
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				_othersDataLinkedList.add(itemDataString);
				_othersCountLinkedList.add(itemCount);
				Log.d(TAG, _othersDataLinkedList.toString());
				Log.d(TAG, _othersCountLinkedList.toString());
				break;
		}
	}
	
	public void removeItemData(int itemType, String itemName, int itemUnitPrice) {
		String itemDataString = itemName + "!" + itemUnitPrice;
		int itemIndex;

		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				if(_meatDataLinkedList.size() > 0) {
					itemIndex = _meatDataLinkedList.indexOf(itemDataString);
					_meatDataLinkedList.remove(itemIndex);
					_meatCountLinkedList.remove(itemIndex);

					Log.d(TAG, _meatDataLinkedList.toString());
					Log.d(TAG, _meatCountLinkedList.toString());
				}
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				if(_alcoholDataLinkedList.size() > 0) {
					itemIndex = _alcoholDataLinkedList.indexOf(itemDataString);
					_alcoholDataLinkedList.remove(itemIndex);
					_alcoholCountLinkedList.remove(itemIndex);

					Log.d(TAG, _alcoholDataLinkedList.toString());
					Log.d(TAG, _alcoholCountLinkedList.toString());
				}
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				if(_othersDataLinkedList.size() > 0) {
					itemIndex = _othersDataLinkedList.indexOf(itemDataString);
					_othersDataLinkedList.remove(itemIndex);
					_othersCountLinkedList.remove(itemIndex);

					Log.d(TAG, _othersDataLinkedList.toString());
					Log.d(TAG, _othersCountLinkedList.toString());
				}
				break;
		}
	}

	public void removeItemData(int itemType, int itemIndex) {
		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				if(_meatDataLinkedList.size() > 0) {
					_meatDataLinkedList.remove(itemIndex);
					_meatCountLinkedList.remove(itemIndex);

					Log.d(TAG, _meatDataLinkedList.toString());
					Log.d(TAG, _meatCountLinkedList.toString());
				}
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				if(_alcoholDataLinkedList.size() > 0) {
					_alcoholDataLinkedList.remove(itemIndex);
					_alcoholCountLinkedList.remove(itemIndex);

					Log.d(TAG, _alcoholDataLinkedList.toString());
					Log.d(TAG, _alcoholCountLinkedList.toString());
				}
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				if(_othersDataLinkedList.size() > 0) {
					_othersDataLinkedList.remove(itemIndex);
					_othersCountLinkedList.remove(itemIndex);

					Log.d(TAG, _othersDataLinkedList.toString());
					Log.d(TAG, _othersCountLinkedList.toString());
				}
				break;
		}
	}

	public void clearItemData(int itemType) {
		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				if(_meatDataLinkedList.size() > 0) {
					_meatDataLinkedList.clear();
					_meatCountLinkedList.clear();
				}
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				if(_alcoholDataLinkedList.size() > 0) {
					_alcoholDataLinkedList.clear();
					_alcoholCountLinkedList.clear();
				}
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				if(_othersDataLinkedList.size() > 0) {
					_othersDataLinkedList.clear();
					_othersCountLinkedList.clear();
				}
				break;
		}
	}
	
	public int getItemDataCount(int itemType) {
		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				return _meatDataLinkedList.size();
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				return _alcoholDataLinkedList.size();
			case ShoppingItemListFragment.OTHERS_ITEM:
				return _othersDataLinkedList.size();
			default:
				return -1;
		}
	}

	public String getItemName(int itemType, int itemIndex) {
		String itemData[];
		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				itemData = _meatDataLinkedList.get(itemIndex).split("!");
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				itemData = _alcoholDataLinkedList.get(itemIndex).split("!");
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				itemData = _othersDataLinkedList.get(itemIndex).split("!");
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
				itemData = _meatDataLinkedList.get(itemIndex).split("!");
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				itemData = _alcoholDataLinkedList.get(itemIndex).split("!");
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				itemData = _othersDataLinkedList.get(itemIndex).split("!");
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
				return _meatDataLinkedList.contains(itemDataString);
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				return _alcoholDataLinkedList.contains(itemDataString);
			case ShoppingItemListFragment.OTHERS_ITEM:
				return _othersDataLinkedList.contains(itemDataString);
			default:
				return false;
		}
	}

	public void changeItemCount(int itemType, String itemName, int itemUnitPrice, int newItemCount) {
		String itemDataString = itemName + "!" + itemUnitPrice;
		int itemIndex = 0;

		switch (itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				itemIndex = _meatDataLinkedList.indexOf(itemDataString);
				_meatCountLinkedList.set(itemIndex, newItemCount);
				break;
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				itemIndex = _alcoholDataLinkedList.indexOf(itemDataString);
				_alcoholCountLinkedList.set(itemIndex, newItemCount);
				break;
			case ShoppingItemListFragment.OTHERS_ITEM:
				itemIndex = _othersDataLinkedList.indexOf(itemDataString);
				_othersCountLinkedList.set(itemIndex, newItemCount);
				break;
		}
	}

	public int getSingleItemCount(int itemType, String itemName, int itemUnitPrice) {
		String itemDataString = itemName + "!" + itemUnitPrice;
		int itemIndex = 0;

		switch(itemType) {
			case ShoppingItemListFragment.MEAT_ITEM:
				itemIndex = _meatDataLinkedList.indexOf(itemDataString);
				return _meatCountLinkedList.get(itemIndex);
			case ShoppingItemListFragment.ALCOHOL_ITEM:
				itemIndex = _alcoholDataLinkedList.indexOf(itemDataString);
				return _alcoholCountLinkedList.get(itemIndex);
			case ShoppingItemListFragment.OTHERS_ITEM:
				itemIndex = _othersDataLinkedList.indexOf(itemDataString);
				return _othersCountLinkedList.get(itemIndex);
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
					itemData = _meatDataLinkedList.get(i);
					itemCount = _meatCountLinkedList.get(i);
					break;
				case ShoppingItemListFragment.ALCOHOL_ITEM:
					itemData = _alcoholDataLinkedList.get(i);
					itemCount = _alcoholCountLinkedList.get(i);
					break;
				case ShoppingItemListFragment.OTHERS_ITEM:
					itemData = _othersDataLinkedList.get(i);
					itemCount = _othersCountLinkedList.get(i);
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
