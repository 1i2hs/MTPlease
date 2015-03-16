package com.owo.mtplease;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by In-Ho on 2015-01-26.
 */
public class QueryDataModelController {

	private static final int CONDITION_SEARCH_MODE = 1;
	private static final int KEYWORD_SEARCH_MODE = 2;

	private int _region;
	private String _date;
	private String _dateWrittenLang;
	private int _people;

	private String _keyword;

	private int _flag;
	private Context _mContext;

	private int lastRoomCardPosition;

	public QueryDataModelController(Context context) {
		this._region = -1;
		this._date = null;
		this._people = -1;
		this._keyword = "";
		this._flag = -1;
		this._mContext = context;
	}

	public int getFlag() {
		return _flag;
	}

	public void setFlag(int _flag) {
		this._flag = _flag;
	}

	public int getRegion() {
		return _region;
	}

	public void setRegion(int _region) {
		this._region = _region;
	}

	public String getDate() {
		return _date;
	}

	public void setDate(String _date) {
		this._date = _date;
	}

	public String getDateWrittenLang() {
		return _dateWrittenLang;
	}

	public void setDateWrittenLang(String _dateWrittenLang) {
		this._dateWrittenLang = _dateWrittenLang;
	}

	public int getPeople() {
		return _people;
	}

	public void setPeople(int _people) {
		this._people = _people;
	}

	public String getKeyword() {
		return _keyword;
	}

	public void setKeyword(String _keyword) {
		this._keyword = _keyword;
	}

	public int getLastRoomCardPosition() {
		return lastRoomCardPosition;
	}

	public void setLastRoomCardPosition(int lastRoomCardPosition) {
		this.lastRoomCardPosition = lastRoomCardPosition;
	}

	public boolean isVariableSet() {
		if (this._flag == CONDITION_SEARCH_MODE && this._region != -1 && this._date != null
				&& this._people != -1 && this._flag != -1) {
			return true;
		} else if(this._flag == KEYWORD_SEARCH_MODE && !this._keyword.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public String makeHttpGetURL() {
		if(this._flag == CONDITION_SEARCH_MODE && isVariableSet()) {
			return _mContext.getResources().getString(R.string.mtplease_url) + "pensions" + "?region=" + _region + "&date="
					+ _date + "&people=" + _people + "&flag=" + CONDITION_SEARCH_MODE;
		} else if(this._flag == KEYWORD_SEARCH_MODE && isVariableSet()) {
			try {
				return _mContext.getResources().getString(R.string.mtplease_url) + "pensions"
						+ "?keyword=" + URLEncoder.encode(_keyword, "utf-8") + "&flag=" + KEYWORD_SEARCH_MODE;
			} catch(UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
		} else {
			return null;
		}
	}

	public String getUserQueryString() {
		if(this._flag == CONDITION_SEARCH_MODE) {
			return "Date: " + _date + " / Region: "
					+ _region + " / Number of People: " + _people;
		} else {
			return "Keyword: " + _keyword;
		}
	}
}
