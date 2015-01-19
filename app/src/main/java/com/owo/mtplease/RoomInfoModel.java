package com.owo.mtplease;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by In-Ho on 2015-01-16.
 */
public class RoomInfoModel implements Parcelable {

	public static final Parcelable.Creator CREATOR = new Creator<RoomInfoModel>() {
		@Override
		public RoomInfoModel createFromParcel(Parcel in) {
			return new RoomInfoModel(in);
		}

		@Override
		public RoomInfoModel[] newArray(int size) {
			return new RoomInfoModel[size];
		}
	};

	private int pen_id;
	private String room_name;
	private String pen_period_division;
	private String period_start;
	private String period_end;
	private int weekdays;
	private int friday;
	private int weekends;
	private int room_std_people;
	private int room_max_people;
	private int room_pyeong; // type check required
	private int num_rooms;
	private int num_toilets;
	private int room_aircon; // type check required
	private String room_equipment;
	private String room_description;
	private String pen_region;
	private String pen_name;
	private String pen_homepage;
	private String pen_lot_adr;
	private String pen_road_adr;
	private double pen_latitude;
	private double pen_longitude;
	private String pen_ceo;
	private String pen_phone1;
	private String pen_phone2;
	private String pen_checkin;
	private String pen_checkout;
	private String pen_check_caution;
	private int pen_pickup;
	private String pen_pickup_description;
	private int pen_barbecue;
	private String pen_barbecue_description;
	private int pen_ground;
	private String pen_ground_description;
	private int pen_valley;
	private String pen_valley_description;
	private String pen_etc_facility;
	private String pen_caution; // need to be considered how to show later
	private String pen_cost_caution;
	private String pen_walk_station;
	private String pen_walk_terminal;
	private int pen_picture_flag; // type check required
	private JSONArray cost_table;
	private JSONArray period_table;
	private int room_cost;

	public RoomInfoModel() {
	}

	public RoomInfoModel(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(pen_id);
		dest.writeString(room_name);
		dest.writeString(pen_period_division);
		dest.writeString(period_start);
		dest.writeString(period_end);
		dest.writeInt(weekdays);
		dest.writeInt(friday);
		dest.writeInt(weekends);
		dest.writeInt(room_std_people);
		dest.writeInt(room_max_people);
		dest.writeInt(room_pyeong); // type check required
		dest.writeInt(num_rooms);
		dest.writeInt(num_toilets);
		dest.writeInt(room_aircon); // type check required
		dest.writeString(room_equipment);
		dest.writeString(room_description);
		dest.writeString(pen_region);
		dest.writeString(pen_name);
		dest.writeString(pen_homepage);
		dest.writeString(pen_lot_adr);
		dest.writeString(pen_road_adr);
		dest.writeDouble(pen_latitude);
		dest.writeDouble(pen_longitude);
		dest.writeString(pen_ceo);
		dest.writeString(pen_phone1);
		dest.writeString(pen_phone2);
		dest.writeString(pen_checkin);
		dest.writeString(pen_checkout);
		dest.writeString(pen_check_caution);
		dest.writeInt(pen_pickup);
		dest.writeString(pen_pickup_description);
		dest.writeInt(pen_barbecue);
		dest.writeString(pen_barbecue_description);
		dest.writeInt(pen_ground);
		dest.writeString(pen_ground_description);
		dest.writeInt(pen_valley);
		dest.writeString(pen_valley_description);
		dest.writeString(pen_etc_facility);
		dest.writeString(pen_caution); // need to be considered how to show later
		dest.writeString(pen_cost_caution);
		dest.writeString(pen_walk_station);
		dest.writeString(pen_walk_terminal);
		dest.writeInt(pen_picture_flag); // type check required
		dest.writeString(cost_table.toString());
		dest.writeString(period_table.toString());
		dest.writeInt(room_cost);
	}

	public void readFromParcel(Parcel in) {
		try {
			pen_id = in.readInt();
			room_name = in.readString();
			pen_period_division = in.readString();
			period_start = in.readString();
			period_end = in.readString();
			weekdays = in.readInt();
			friday = in.readInt();
			weekends = in.readInt();
			room_std_people = in.readInt();
			room_max_people = in.readInt();
			room_pyeong = in.readInt(); // type check required
			num_rooms = in.readInt();
			num_toilets = in.readInt();
			room_aircon = in.readInt(); // type check required
			room_equipment = in.readString();
			room_description = in.readString();
			pen_region = in.readString();
			pen_name = in.readString();
			pen_homepage = in.readString();
			pen_lot_adr = in.readString();
			pen_road_adr = in.readString();
			pen_latitude = in.readDouble();
			pen_longitude = in.readDouble();
			pen_ceo = in.readString();
			pen_phone1 = in.readString();
			pen_phone2 = in.readString();
			pen_checkin = in.readString();
			pen_checkout = in.readString();
			pen_check_caution = in.readString();
			pen_pickup = in.readInt();
			pen_pickup_description = in.readString();
			pen_barbecue = in.readInt();
			pen_barbecue_description= in.readString();
			pen_ground = in.readInt();
			pen_ground_description = in.readString();
			pen_valley = in.readInt();
			pen_valley_description = in.readString();
			pen_etc_facility = in.readString();
			pen_caution = in.readString(); // need to be considered how to show later
			pen_cost_caution = in.readString();
			pen_walk_station = in.readString();
			pen_walk_terminal = in.readString();
			pen_picture_flag = in.readInt(); // type check required
			cost_table = null;
			cost_table = new JSONArray(in.readString());
			period_table = null;
			period_table = new JSONArray(in.readString());
			room_cost = in.readInt();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public int getPen_id() {
		return pen_id;
	}

	public void setPen_id(int pen_id) {
		this.pen_id = pen_id;
	}

	public String getRoom_name() {
		return room_name;
	}

	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}

	public String getPen_period_division() {
		return pen_period_division;
	}

	public void setPen_period_division(String pen_period_division) {
		this.pen_period_division = pen_period_division;
	}

	public String getPeriod_start() {
		return period_start;
	}

	public void setPeriod_start(String period_start) {
		this.period_start = period_start;
	}

	public String getPeriod_end() {
		return period_end;
	}

	public void setPeriod_end(String period_end) {
		this.period_end = period_end;
	}

	public int getWeekdays() {
		return weekdays;
	}

	public void setWeekdays(int weekdays) {
		this.weekdays = weekdays;
	}

	public int getFriday() {
		return friday;
	}

	public void setFriday(int friday) {
		this.friday = friday;
	}

	public int getWeekends() {
		return weekends;
	}

	public void setWeekends(int weekends) {
		this.weekends = weekends;
	}

	public int getRoom_std_people() {
		return room_std_people;
	}

	public void setRoom_std_people(int room_std_people) {
		this.room_std_people = room_std_people;
	}

	public int getRoom_max_people() {
		return room_max_people;
	}

	public void setRoom_max_people(int room_max_people) {
		this.room_max_people = room_max_people;
	}

	public int getRoom_pyeong() {
		return room_pyeong;
	}

	public void setRoom_pyeong(int room_pyeong) {
		this.room_pyeong = room_pyeong;
	}

	public int getNum_rooms() {
		return num_rooms;
	}

	public void setNum_rooms(int num_rooms) {
		this.num_rooms = num_rooms;
	}

	public int getNum_toilets() {
		return num_toilets;
	}

	public void setNum_toilets(int num_toilets) {
		this.num_toilets = num_toilets;
	}

	public int getRoom_aircon() {
		return room_aircon;
	}

	public void setRoom_aircon(int room_aircon) {
		this.room_aircon = room_aircon;
	}

	public String getRoom_equipment() {
		return room_equipment;
	}

	public void setRoom_equipment(String room_equipment) {
		this.room_equipment = room_equipment;
	}

	public String getRoom_description() {
		return room_description;
	}

	public void setRoom_description(String room_description) {
		this.room_description = room_description;
	}

	public String getPen_region() {
		return pen_region;
	}

	public void setPen_region(String pen_region) {
		this.pen_region = pen_region;
	}

	public String getPen_name() {
		return pen_name;
	}

	public void setPen_name(String pen_name) {
		this.pen_name = pen_name;
	}

	public String getPen_homepage() {
		return pen_homepage;
	}

	public void setPen_homepage(String pen_homepage) {
		this.pen_homepage = pen_homepage;
	}

	public String getPen_lot_adr() {
		return pen_lot_adr;
	}

	public void setPen_lot_adr(String pen_lot_adr) {
		this.pen_lot_adr = pen_lot_adr;
	}

	public String getPen_road_adr() {
		return pen_road_adr;
	}

	public void setPen_road_adr(String pen_road_adr) {
		this.pen_road_adr = pen_road_adr;
	}

	public double getPen_latitude() {
		return pen_latitude;
	}

	public void setPen_latitude(double pen_latitude) {
		this.pen_latitude = pen_latitude;
	}

	public double getPen_longitude() {
		return pen_longitude;
	}

	public void setPen_longitude(double pen_longitude) {
		this.pen_longitude = pen_longitude;
	}

	public String getPen_ceo() {
		return pen_ceo;
	}

	public void setPen_ceo(String pen_ceo) {
		this.pen_ceo = pen_ceo;
	}

	public String getPen_phone1() {
		return pen_phone1;
	}

	public void setPen_phone1(String pen_phone1) {
		this.pen_phone1 = pen_phone1;
	}

	public String getPen_phone2() {
		return pen_phone2;
	}

	public void setPen_phone2(String pen_phone2) {
		this.pen_phone2 = pen_phone2;
	}

	public String getPen_checkin() {
		return pen_checkin;
	}

	public void setPen_checkin(String pen_checkin) {
		this.pen_checkin = pen_checkin;
	}

	public String getPen_checkout() {
		return pen_checkout;
	}

	public void setPen_checkout(String pen_checkout) {
		this.pen_checkout = pen_checkout;
	}

	public String getPen_check_caution() {
		return pen_check_caution;
	}

	public void setPen_check_caution(String pen_check_caution) {
		this.pen_check_caution = pen_check_caution;
	}

	public int getPen_pickup() {
		return pen_pickup;
	}

	public void setPen_pickup(int pen_pickup) {
		this.pen_pickup = pen_pickup;
	}

	public String getPen_pickup_description() {
		return pen_pickup_description;
	}

	public void setPen_pickup_description(String pen_pickup_description) {
		this.pen_pickup_description = pen_pickup_description;
	}

	public int getPen_barbecue() {
		return pen_barbecue;
	}

	public void setPen_barbecue(int pen_barbecue) {
		this.pen_barbecue = pen_barbecue;
	}

	public String getPen_barbecue_description() {
		return pen_barbecue_description;
	}

	public void setPen_barbecue_description(String pen_barbecue_description) {
		this.pen_barbecue_description = pen_barbecue_description;
	}

	public int getPen_ground() {
		return pen_ground;
	}

	public void setPen_ground(int pen_ground) {
		this.pen_ground = pen_ground;
	}

	public String getPen_ground_description() {
		return pen_ground_description;
	}

	public void setPen_ground_description(String pen_ground_description) {
		this.pen_ground_description = pen_ground_description;
	}

	public int getPen_valley() {
		return pen_valley;
	}

	public void setPen_valley(int pen_valley) {
		this.pen_valley = pen_valley;
	}

	public String getPen_valley_description() {
		return pen_valley_description;
	}

	public void setPen_valley_description(String pen_valley_description) {
		this.pen_valley_description = pen_valley_description;
	}

	public String getPen_etc_facility() {
		return pen_etc_facility;
	}

	public void setPen_etc_facility(String pen_etc_facility) {
		this.pen_etc_facility = pen_etc_facility;
	}

	public String getPen_caution() {
		return pen_caution;
	}

	public void setPen_caution(String pen_caution) {
		this.pen_caution = pen_caution;
	}

	public String getPen_cost_caution() {
		return pen_cost_caution;
	}

	public void setPen_cost_caution(String pen_cost_caution) {
		this.pen_cost_caution = pen_cost_caution;
	}

	public String getPen_walk_station() {
		return pen_walk_station;
	}

	public void setPen_walk_station(String pen_walk_station) {
		this.pen_walk_station = pen_walk_station;
	}

	public String getPen_walk_terminal() {
		return pen_walk_terminal;
	}

	public void setPen_walk_terminal(String pen_walk_terminal) {
		this.pen_walk_terminal = pen_walk_terminal;
	}

	public int getPen_picture_flag() {
		return pen_picture_flag;
	}

	public void setPen_picture_flag(int pen_picture_flag) {
		this.pen_picture_flag = pen_picture_flag;
	}

	public JSONArray getCost_table() {
		return cost_table;
	}

	public void setCost_table(JSONArray cost_table) {
		this.cost_table = cost_table;
	}

	public JSONArray getPeriod_table() {
		return period_table;
	}

	public void setPeriod_table(JSONArray period_table) {
		this.period_table = period_table;
	}

	public int getRoom_cost() {
		return room_cost;
	}

	public void setRoom_cost(int room_cost) {
		this.room_cost = room_cost;
	}
}
