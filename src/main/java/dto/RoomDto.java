package dto;

public class RoomDto {
	
	private String r_room_no, r_type, r_scale, r_bed_type, 
				   r_wifi, r_spa, r_smoking, r_view, r_balcony, 
				   r_kitchen, r_status, r_clean_status, 
				   r_register_date, r_last_update, r_description;
	
	private int r_floor, r_people_standard, r_people_max, 
				r_extra_person_fee, r_bed, r_bathroom, r_price;
	
	public RoomDto() {}
	
	public RoomDto(String r_room_no, String r_type, int r_floor, String r_scale, 
				   int r_people_standard, int r_people_max, String r_bed_type, 
				   int r_price, String r_status) {
		this.r_room_no = r_room_no;
		this.r_type = r_type;
		this.r_floor = r_floor;
		this.r_scale = r_scale;
		this.r_people_standard = r_people_standard;
		this.r_people_max = r_people_max;
		this.r_bed_type = r_bed_type;
		this.r_price = r_price;
		this.r_status = r_status;
	}
	
	public RoomDto(String r_room_no, String r_type, int r_floor, String r_scale, 
				   int r_people_standard, int r_people_max, int r_extra_person_fee,
				   int r_bed, String r_bed_type, String r_wifi, String r_spa, 
				   String r_smoking, String r_view, String r_balcony, int r_bathroom,
				   String r_kitchen, int r_price, String r_status, String r_clean_status,
				   String r_register_date, String r_last_update, String r_description) {
		this.r_room_no = r_room_no;
		this.r_type = r_type;
		this.r_floor = r_floor;
		this.r_scale = r_scale;
		this.r_people_standard = r_people_standard;
		this.r_people_max = r_people_max;
		this.r_extra_person_fee = r_extra_person_fee;
		this.r_bed = r_bed;
		this.r_bed_type = r_bed_type;
		this.r_wifi = r_wifi;
		this.r_spa = r_spa;
		this.r_smoking = r_smoking;
		this.r_view = r_view;
		this.r_balcony = r_balcony;
		this.r_bathroom = r_bathroom;
		this.r_kitchen = r_kitchen;
		this.r_price = r_price;
		this.r_status = r_status;
		this.r_clean_status = r_clean_status;
		this.r_register_date = r_register_date;
		this.r_last_update = r_last_update;
		this.r_description = r_description;
	}
	
	public String getR_room_no() {
		return r_room_no;
	}
	
	public String getR_type() {
		return r_type;
	}
	
	public int getR_floor() {
		return r_floor;
	}
	
	public String getR_scale() {
		return r_scale;
	}
	
	public int getR_people_standard() {
		return r_people_standard;
	}
	
	public int getR_people_max() {
		return r_people_max;
	}
	
	public int getR_extra_person_fee() {
		return r_extra_person_fee;
	}
	
	public int getR_bed() {
		return r_bed;
	}
	
	public String getR_bed_type() {
		return r_bed_type;
	}
	
	public String getR_wifi() {
		return r_wifi;
	}
	
	public String getR_spa() {
		return r_spa;
	}
	
	public String getR_smoking() {
		return r_smoking;
	}
	
	public String getR_view() {
		return r_view;
	}
	
	public String getR_balcony() {
		return r_balcony;
	}
	
	public int getR_bathroom() {
		return r_bathroom;
	}
	
	public String getR_kitchen() {
		return r_kitchen;
	}
	
	public int getR_price() {
		return r_price;
	}
	
	public String getR_status() {
		return r_status;
	}
	
	public String getR_clean_status() {
		return r_clean_status;
	}
	
	public String getR_register_date() {
		return r_register_date;
	}
	
	public String getR_last_update() {
		return r_last_update;
	}
	
	public String getR_description() {
		return r_description;
	}
	
	public void setR_room_no(String r_room_no) {
		this.r_room_no = r_room_no;
	}
	
	public void setR_type(String r_type) {
		this.r_type = r_type;
	}
	
	public void setR_floor(int r_floor) {
		this.r_floor = r_floor;
	}
	
	public void setR_scale(String r_scale) {
		this.r_scale = r_scale;
	}
	
	public void setR_people_standard(int r_people_standard) {
		this.r_people_standard = r_people_standard;
	}
	
	public void setR_people_max(int r_people_max) {
		this.r_people_max = r_people_max;
	}
	
	public void setR_extra_person_fee(int r_extra_person_fee) {
		this.r_extra_person_fee = r_extra_person_fee;
	}
	
	public void setR_bed(int r_bed) {
		this.r_bed = r_bed;
	}
	
	public void setR_bed_type(String r_bed_type) {
		this.r_bed_type = r_bed_type;
	}
	
	public void setR_wifi(String r_wifi) {
		this.r_wifi = r_wifi;
	}
	
	public void setR_spa(String r_spa) {
		this.r_spa = r_spa;
	}
	
	public void setR_smoking(String r_smoking) {
		this.r_smoking = r_smoking;
	}
	
	public void setR_view(String r_view) {
		this.r_view = r_view;
	}
	
	public void setR_balcony(String r_balcony) {
		this.r_balcony = r_balcony;
	}
	
	public void setR_bathroom(int r_bathroom) {
		this.r_bathroom = r_bathroom;
	}
	
	public void setR_kitchen(String r_kitchen) {
		this.r_kitchen = r_kitchen;
	}
	
	public void setR_price(int r_price) {
		this.r_price = r_price;
	}
	
	public void setR_status(String r_status) {
		this.r_status = r_status;
	}
	
	public void setR_clean_status(String r_clean_status) {
		this.r_clean_status = r_clean_status;
	}
	
	public void setR_register_date(String r_register_date) {
		this.r_register_date = r_register_date;
	}
	
	public void setR_last_update(String r_last_update) {
		this.r_last_update = r_last_update;
	}
	
	public void setR_description(String r_description) {
		this.r_description = r_description;
	}
}
