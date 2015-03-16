package com.owo.mtplease;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedList;

/**
 * Created by In-Ho on 2015-01-16.
 */
public class RoomInfoModelController implements Parcelable {

	public static final Parcelable.Creator CREATOR = new Creator<RoomInfoModelController>() {
		@Override
		public RoomInfoModelController createFromParcel(Parcel in) {
			return new RoomInfoModelController(in);
		}

		@Override
		public RoomInfoModelController[] newArray(int size) {
			return new RoomInfoModelController[size];
		}
	};

	private static final String TAG = "RoomInfoModelController";

	private LinkedList<String> _roomRealImageURL;
	private LinkedList<String> _roomUnrealImageURL;
	private String _roomRealThumbnailImageURL;
	private String _roomUnrealThumbnailImageURL;
	private boolean _roomRealImageExists;
	private boolean _roomRealThumbnailImageExists;

	private int _pen_id;
	private String _room_name;
	private String _pen_period_division;
	private String _period_start;
	private String _period_end;
	private int _weekdays;
	private int _friday;
	private int _weekends;
	private int _room_id;
	private int _room_std_people;
	private int _room_max_people;
	private String _room_pyeong;
	private int _num_rooms;
	private int _num_toilets;
	private int _room_aircon;
	private String _room_equipment;
	private String _room_description;
	private int _room_picture_flag;
	private int _num_images;
	private String _pen_region;
	private String _pen_name;
	private String _pen_homepage;
	private String _pen_lot_adr;
	private String _pen_road_adr;
	private String _pen_ceo;
	private String _pen_phone1;
	private String _pen_phone2;
	private String _pen_ceo_account;
	private String _pen_checkin;
	private String _pen_checkout;
	private int _pen_pickup;
	private String _pen_pickup_cost;
	private String _pen_pickup_location;
	private String _pen_pickup_description;
	private int _pen_barbecue;
	private String _pen_barbecue_cost;
	private String _pen_barbecue_component;
	private String _pen_barbecue_location;
	private String _pen_barbecue_description;
	private int _pen_ground;
	private String _pen_ground_type;
	private String _pen_ground_description;
	private int _pen_valley;
	private String _pen_valley_distance;
	private String _pen_valley_depth;
	private String _pen_valley_description;
	private double _pen_latitude;
	private double _pen_longitude;
	private String _pen_walk_station;
	private String _pen_walk_terminal;
	private JSONArray _cost_table;
	private JSONArray _period_table;
	private int _room_cost;
	private JSONArray _facility;
	private JSONArray _service;
	private JSONArray _usage_caution;
	private JSONArray _reserve_caution;
	private JSONArray _refund_caution;
	private String _pen_description;

	public int getPen_id() {
		return _pen_id;
	}

	public void setPen_id(int _pen_id) {
		this._pen_id = _pen_id;
	}

	public String getRoom_name() {
		return _room_name;
	}

	public void setRoom_name(String _room_name) {
		this._room_name = _room_name;
	}

	public String getPen_period_division() {
		return _pen_period_division;
	}

	public void setPen_period_division(String _pen_period_division) {
		this._pen_period_division = _pen_period_division;
	}

	public String getPeriod_start() {
		return _period_start;
	}

	public void setPeriod_start(String _period_start) {
		this._period_start = _period_start;
	}

	public String getPeriod_end() {
		return _period_end;
	}

	public void setPeriod_end(String _period_end) {
		this._period_end = _period_end;
	}

	public int getWeekdays() {
		return _weekdays;
	}

	public void setWeekdays(int _weekdays) {
		this._weekdays = _weekdays;
	}

	public int getFriday() {
		return _friday;
	}

	public void setFriday(int _friday) {
		this._friday = _friday;
	}

	public void setRoom_id(int _room_id) {
		this._room_id = _room_id;
	}

	public int getRoom_id() {
		return _room_id;
	}

	public int getWeekends() {
		return _weekends;
	}

	public void setWeekends(int _weekends) {
		this._weekends = _weekends;
	}

	public int getRoom_std_people() {
		return _room_std_people;
	}

	public void setRoom_std_people(int _room_std_people) {
		this._room_std_people = _room_std_people;
	}

	public int getRoom_max_people() {
		return _room_max_people;
	}

	public void setRoom_max_people(int _room_max_people) {
		this._room_max_people = _room_max_people;
	}

	public String getRoom_pyeong() {
		return _room_pyeong;
	}

	public void setRoom_pyeong(String _room_pyeong) {
		this._room_pyeong = _room_pyeong;
	}

	public int getNum_rooms() {
		return _num_rooms;
	}

	public void setNum_rooms(int _num_rooms) {
		this._num_rooms = _num_rooms;
	}

	public int getNum_toilets() {
		return _num_toilets;
	}

	public void setNum_toilets(int _num_toilets) {
		this._num_toilets = _num_toilets;
	}

	public int getRoom_aircon() {
		return _room_aircon;
	}

	public void setRoom_aircon(int _room_aircon) {
		this._room_aircon = _room_aircon;
	}

	public String getRoom_equipment() {
		return _room_equipment;
	}

	public void setRoom_equipment(String _room_equipment) {
		this._room_equipment = _room_equipment;
	}

	public String getRoom_description() {
		return _room_description;
	}

	public void setRoom_description(String _room_description) {
		this._room_description = _room_description;
	}

	public int getRoom_picture_flag() {
		return _room_picture_flag;
	}

	public void setRoom_picture_flag(int _room_picture_flag) {
		this._room_picture_flag = _room_picture_flag;
	}

	public int getNum_images() {
		return _num_images;
	}

	public void setNum_images(int _num_images) {
		this._num_images = _num_images;
	}

	public String getPen_region() {
		return _pen_region;
	}

	public void setPen_region(String _pen_region) {
		this._pen_region = _pen_region;
	}

	public String getPen_name() {
		return _pen_name;
	}

	public void setPen_name(String _pen_name) {
		this._pen_name = _pen_name;
	}

	public String getPen_homepage() {
		return _pen_homepage;
	}

	public void setPen_homepage(String _pen_homepage) {
		this._pen_homepage = _pen_homepage;
	}

	public String getPen_lot_adr() {
		return _pen_lot_adr;
	}

	public void setPen_lot_adr(String _pen_lot_adr) {
		this._pen_lot_adr = _pen_lot_adr;
	}

	public String getPen_road_adr() {
		return _pen_road_adr;
	}

	public void setPen_road_adr(String _pen_road_adr) {
		this._pen_road_adr = _pen_road_adr;
	}

	public String getPen_ceo() {
		return _pen_ceo;
	}

	public void setPen_ceo(String _pen_ceo) {
		this._pen_ceo = _pen_ceo;
	}

	public String getPen_phone1() {
		return _pen_phone1;
	}

	public void setPen_phone1(String _pen_phone1) {
		this._pen_phone1 = _pen_phone1;
	}

	public String getPen_phone2() {
		return _pen_phone2;
	}

	public void setPen_phone2(String _pen_phone2) {
		this._pen_phone2 = _pen_phone2;
	}

	public String getPen_ceo_account() {
		return _pen_ceo_account;
	}

	public void setPen_ceo_account(String _pen_ceo_account) {
		this._pen_ceo_account = _pen_ceo_account;
	}

	public String getPen_checkin() {
		return _pen_checkin;
	}

	public void setPen_checkin(String _pen_checkin) {
		this._pen_checkin = _pen_checkin;
	}

	public String getPen_checkout() {
		return _pen_checkout;
	}

	public void setPen_checkout(String _pen_checkout) {
		this._pen_checkout = _pen_checkout;
	}

	public int getPen_pickup() {
		return _pen_pickup;
	}

	public void setPen_pickup(int _pen_pickup) {
		this._pen_pickup = _pen_pickup;
	}

	public String getPen_pickup_cost() {
		return _pen_pickup_cost;
	}

	public void setPen_pickup_cost(String _pen_pickup_cost) {
		this._pen_pickup_cost = _pen_pickup_cost;
	}

	public String getPen_pickup_location() {
		return _pen_pickup_location;
	}

	public void setPen_pickup_location(String _pen_pickup_location) {
		this._pen_pickup_location = _pen_pickup_location;
	}

	public String getPen_pickup_description() {
		return _pen_pickup_description;
	}

	public void setPen_pickup_description(String _pen_pickup_description) {
		this._pen_pickup_description = _pen_pickup_description;
	}

	public int getPen_barbecue() {
		return _pen_barbecue;
	}

	public void setPen_barbecue(int _pen_barbecue) {
		this._pen_barbecue = _pen_barbecue;
	}

	public String getPen_barbecue_cost() {
		return _pen_barbecue_cost;
	}

	public void setPen_barbecue_cost(String _pen_barbecue_cost) {
		this._pen_barbecue_cost = _pen_barbecue_cost;
	}

	public String getPen_barbecue_component() {
		return _pen_barbecue_component;
	}

	public void setPen_barbecue_component(String _pen_barbecue_component) {
		this._pen_barbecue_component = _pen_barbecue_component;
	}

	public String getPen_barbecue_location() {
		return _pen_barbecue_location;
	}

	public void setPen_barbecue_location(String _pen_barbecue_location) {
		this._pen_barbecue_location = _pen_barbecue_location;
	}

	public String getPen_barbecue_description() {
		return _pen_barbecue_description;
	}

	public void setPen_barbecue_description(String _pen_barbecue_description) {
		this._pen_barbecue_description = _pen_barbecue_description;
	}

	public int getPen_ground() {
		return _pen_ground;
	}

	public void setPen_ground(int _pen_ground) {
		this._pen_ground = _pen_ground;
	}

	public String getPen_ground_type() {
		return _pen_ground_type;
	}

	public void setPen_ground_type(String _pen_ground_type) {
		this._pen_ground_type = _pen_ground_type;
	}

	public String getPen_ground_description() {
		return _pen_ground_description;
	}

	public void setPen_ground_description(String _pen_ground_description) {
		this._pen_ground_description = _pen_ground_description;
	}

	public int getPen_valley() {
		return _pen_valley;
	}

	public void setPen_valley(int _pen_valley) {
		this._pen_valley = _pen_valley;
	}

	public String getPen_valley_distance() {
		return _pen_valley_distance;
	}

	public void setPen_valley_distance(String _pen_valley_distance) {
		this._pen_valley_distance = _pen_valley_distance;
	}

	public String getPen_valley_depth() {
		return _pen_valley_depth;
	}

	public void setPen_valley_depth(String _pen_valley_depth) {
		this._pen_valley_depth = _pen_valley_depth;
	}

	public String getPen_valley_description() {
		return _pen_valley_description;
	}

	public void setPen_valley_description(String _pen_valley_description) {
		this._pen_valley_description = _pen_valley_description;
	}

	public double getPen_latitude() {
		return _pen_latitude;
	}

	public void setPen_latitude(double _pen_latitude) {
		this._pen_latitude = _pen_latitude;
	}

	public double getPen_longitude() {
		return _pen_longitude;
	}

	public void setPen_longitude(double _pen_longitude) {
		this._pen_longitude = _pen_longitude;
	}

	public String getPen_walk_station() {
		return _pen_walk_station;
	}

	public void setPen_walk_station(String _pen_walk_station) {
		this._pen_walk_station = _pen_walk_station;
	}

	public String getPen_walk_terminal() {
		return _pen_walk_terminal;
	}

	public void setPen_walk_terminal(String _pen_walk_terminal) {
		this._pen_walk_terminal = _pen_walk_terminal;
	}

	public JSONArray getCost_table() {
		return _cost_table;
	}

	public void setCost_table(JSONArray _cost_table) {
		this._cost_table = _cost_table;
	}

	public JSONArray getPeriod_table() {
		return _period_table;
	}

	public void setPeriod_table(JSONArray _period_table) {
		Log.d(TAG, _period_table.toString());
		this._period_table = _period_table;
	}

	public int getRoom_cost() {
		return _room_cost;
	}

	public void setRoom_cost(int _room_cost) {
		this._room_cost = _room_cost;
	}

	public JSONArray getFacility() {
		return _facility;
	}

	public void setFacility(JSONArray _facility) {
		this._facility = _facility;
	}

	public JSONArray getService() {
		return _service;
	}

	public void setService(JSONArray _service) {
		this._service = _service;
	}

	public JSONArray getUsage_caution() {
		return _usage_caution;
	}

	public void setUsage_caution(JSONArray _usage_caution) {
		this._usage_caution = _usage_caution;
	}

	public JSONArray getReserve_caution() {
		return _reserve_caution;
	}

	public void setReserve_caution(JSONArray _reserve_caution) {
		this._reserve_caution = _reserve_caution;
	}

	public JSONArray getRefund_caution() {
		return _refund_caution;
	}

	public void setRefund_caution(JSONArray _refund_caution) {
		this._refund_caution = _refund_caution;
	}

	public String getPen_description() {
		return _pen_description;
	}

	public void setPen_description(String _pen_description) {
		this._pen_description = _pen_description;
	}

	public RoomInfoModelController() {
	}

	public RoomInfoModelController(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(_pen_id);
		dest.writeString(_room_name);
		dest.writeString(_pen_period_division);
		dest.writeString(_period_start);
		dest.writeString(_period_end);
		dest.writeInt(_weekdays);
		dest.writeInt(_friday);
		dest.writeInt(_weekends);
		dest.writeInt(_room_id);
		dest.writeInt(_room_std_people);
		dest.writeInt(_room_max_people);
		dest.writeString(_room_pyeong);
		dest.writeInt(_num_rooms);
		dest.writeInt(_num_toilets);
		dest.writeInt(_room_aircon);
		dest.writeString(_room_equipment);
		dest.writeString(_room_description);
		dest.writeInt(_room_picture_flag);
		dest.writeInt(_num_images);
		dest.writeString(_pen_region);
		dest.writeString(_pen_name);
		dest.writeString(_pen_homepage);
		dest.writeString(_pen_lot_adr);
		dest.writeString(_pen_road_adr);
		dest.writeString(_pen_ceo);
		dest.writeString(_pen_phone1);
		dest.writeString(_pen_phone2);
		dest.writeString(_pen_ceo_account);
		dest.writeString(_pen_checkin);
		dest.writeString(_pen_checkout);
		dest.writeInt(_pen_pickup);
		dest.writeString(_pen_pickup_cost);
		dest.writeString(_pen_pickup_location);
		dest.writeString(_pen_pickup_description);
		dest.writeInt(_pen_barbecue);
		dest.writeString(_pen_barbecue_cost);
		dest.writeString(_pen_barbecue_component);
		dest.writeString(_pen_barbecue_location);
		dest.writeString(_pen_barbecue_description);
		dest.writeInt(_pen_ground);
		dest.writeString(_pen_ground_type);
		dest.writeString(_pen_ground_description);
		dest.writeInt(_pen_valley);
		dest.writeString(_pen_valley_distance);
		dest.writeString(_pen_valley_depth);
		dest.writeString(_pen_valley_description);
		dest.writeDouble(_pen_latitude);
		dest.writeDouble(_pen_longitude);
		dest.writeString(_pen_walk_station);
		dest.writeString(_pen_walk_terminal);
		if(_cost_table != null)
			dest.writeString(_cost_table.toString());
		else
			dest.writeString("");

		Log.d(TAG, _period_table.toString());
		if(_period_table != null)
			dest.writeString(_period_table.toString());
		else
			dest.writeString("");

		dest.writeInt(_room_cost);

		if(_facility != null)
			dest.writeString(_facility.toString());
		else
			dest.writeString("");

		if(_service != null)
			dest.writeString(_service.toString());
		else
			dest.writeString("");

		if(_usage_caution != null)
			dest.writeString(_usage_caution.toString());
		else
			dest.writeString("");

		if(_reserve_caution != null)
			dest.writeString(_reserve_caution.toString());
		else
			dest.writeString("");

		if(_refund_caution != null)
			dest.writeString(_refund_caution.toString());
		else
			dest.writeString("");

		dest.writeString(_pen_description);
	}
	public void readFromParcel(Parcel in) {
		try {
			_pen_id = in.readInt();
			_room_name = in.readString();
			_pen_period_division  = in.readString();
			_period_start = in.readString();
			_period_end = in.readString();
			_weekdays = in.readInt();
			_room_id = in.readInt();
			_friday = in.readInt();
			_weekends = in.readInt();
			_room_std_people = in.readInt();
			_room_max_people = in.readInt();
			_room_pyeong = in.readString();
			_num_rooms = in.readInt();
			_num_toilets = in.readInt();
			_room_aircon = in.readInt();
			_room_equipment = in.readString();
			_room_description = in.readString();
			_room_picture_flag = in.readInt();
			_num_images = in.readInt();
			_pen_region = in.readString();
			_pen_name = in.readString();
			_pen_homepage = in.readString();
			_pen_lot_adr = in.readString();
			_pen_road_adr = in.readString();
			_pen_ceo = in.readString();
			_pen_phone1 = in.readString();
			_pen_phone2 = in.readString();
			_pen_ceo_account = in.readString();
			_pen_checkin = in.readString();
			_pen_checkout = in.readString();
			_pen_pickup = in.readInt();
			_pen_pickup_cost = in.readString();
			_pen_pickup_location = in.readString();
			_pen_pickup_description = in.readString();
			_pen_barbecue = in.readInt();
			_pen_barbecue_cost = in.readString();
			_pen_barbecue_component = in.readString();
			_pen_barbecue_location = in.readString();
			_pen_barbecue_description = in.readString();
			_pen_ground = in.readInt();
			_pen_ground_type = in.readString();
			_pen_ground_description = in.readString();
			_pen_valley = in.readInt();
			_pen_valley_distance = in.readString();
			_pen_valley_depth = in.readString();
			_pen_valley_description = in.readString();
			_pen_latitude = in.readDouble();
			_pen_longitude = in.readDouble();
			_pen_walk_station = in.readString();
			_pen_walk_terminal = in.readString();
			_cost_table = new JSONArray(in.readString());
			_period_table = new JSONArray(in.readString());
			Log.d(TAG, _period_table.toString());
			_room_cost = in.readInt();
			_facility = new JSONArray(in.readString());
			_service = new JSONArray(in.readString());
			_usage_caution = new JSONArray(in.toString());
			_reserve_caution = new JSONArray(in.toString());
			_refund_caution = new JSONArray(in.toString());
			_pen_description = in.readString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public boolean isRoomRealThumbnailImageExists() {
		return _roomRealThumbnailImageExists;
	}

	public void setRoomRealThumbnailImageExists(boolean _roomRealThumbnailImageExists) {
		this._roomRealThumbnailImageExists = _roomRealThumbnailImageExists;
	}

	public boolean isRoomRealImageExists() {
		return _roomRealImageExists;
	}

	public void setRoomRealImageExists(boolean _roomRealImageExists) {
		this._roomRealImageExists = _roomRealImageExists;
	}



	public String getRoomThumbnailImageURL(String mtPleaseUrl) {
		String imageURL;

		/*imageURL = mtPleaseUrl + "img/pensions/" + this._pen_id + "/"
				+ URLEncoder.encode(this._room_name, "utf-8").replaceAll("\\+", "%20")
				+ "/real/thumbnail.png";*/

		imageURL =  mtPleaseUrl + "pensions/" + this._pen_id
				+ "/" + this._room_id + "/real/thumbnail.png";

		return imageURL;
			/*if(_roomRealThumbnailImageExists) {
				imageURL = mtPleaseUrl + "img/pensions/" + this._pen_id + "/"
						+ URLEncoder.encode(this._room_name, "utf-8").replaceAll("\\+", "%20")
						+ "/real/thumbnail.png";
			}
			else {
				imageURL = mtPleaseUrl + "img/pensions/" + this._pen_id + "/"
						+ URLEncoder.encode(this._room_name, "utf-8").replaceAll("\\+", "%20")
						+ "/unreal/thumbnail.png";
			}*/
	}
}
