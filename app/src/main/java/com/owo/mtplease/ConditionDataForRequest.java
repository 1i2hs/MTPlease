package com.owo.mtplease;

/**
 * Created by In-Ho on 2015-01-26.
 */
public class ConditionDataForRequest {

	private int region;
	private String date;
	private String dateWrittenLang;
	private int people;
	private int flag;

	public ConditionDataForRequest() {
		this.region = -1;
		this.date = null;
		this.people = -1;
		this.flag = -1;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getRegion() {
		return region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDateWrittenLang() {
		return dateWrittenLang;
	}

	public void setDateWrittenLang(String dateWrittenLang) {
		this.dateWrittenLang = dateWrittenLang;
	}

	public int getPeople() {
		return people;
	}

	public void setPeople(int people) {
		this.people = people;
	}

	public boolean isVariableSet() {
		if (this.region != -1 && this.date != null && this.people != -1 && this.flag != -1) {
			return true;
		} else {
			return false;
		}
	}

	public String makeHttpGetURL() {
		if (isVariableSet()) {
			String httpGetURL = "http://mtplease.herokuapp.com/" + "pensions" + "?region=" + region + "&date="
					+ date + "&people=" + people + "&flag=1";

			return httpGetURL;
		}
		return null;
	}

	public String getUserQueryString() {
		String queryString = "Date: " + date + " / Region: "
				+ region + " / Number of People: " + people;
		return queryString;
	}
}
