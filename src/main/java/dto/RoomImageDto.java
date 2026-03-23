package dto;

public class RoomImageDto {
	
	private String ri_img_no;
	private String ri_room_no;
	private String ri_img_path;
	private String ri_img_name;
	private String ri_img_type;
	private int ri_img_size;
	private String ri_is_main;
	private int ri_display_order;
	private String ri_upload_date;
	private String ri_status;
	
	public RoomImageDto() {}
	
	public RoomImageDto(String ri_img_no, String ri_room_no, String ri_img_path, 
						String ri_img_name, String ri_is_main, int ri_display_order) {
		this.ri_img_no = ri_img_no;
		this.ri_room_no = ri_room_no;
		this.ri_img_path = ri_img_path;
		this.ri_img_name = ri_img_name;
		this.ri_is_main = ri_is_main;
		this.ri_display_order = ri_display_order;
	}
	
	public String getRi_img_no() {
		return ri_img_no;
	}
	
	public void setRi_img_no(String ri_img_no) {
		this.ri_img_no = ri_img_no;
	}
	
	public String getRi_room_no() {
		return ri_room_no;
	}
	
	public void setRi_room_no(String ri_room_no) {
		this.ri_room_no = ri_room_no;
	}
	
	public String getRi_img_path() {
		return ri_img_path;
	}
	
	public void setRi_img_path(String ri_img_path) {
		this.ri_img_path = ri_img_path;
	}
	
	public String getRi_img_name() {
		return ri_img_name;
	}
	
	public void setRi_img_name(String ri_img_name) {
		this.ri_img_name = ri_img_name;
	}
	
	public String getRi_img_type() {
		return ri_img_type;
	}
	
	public void setRi_img_type(String ri_img_type) {
		this.ri_img_type = ri_img_type;
	}
	
	public int getRi_img_size() {
		return ri_img_size;
	}
	
	public void setRi_img_size(int ri_img_size) {
		this.ri_img_size = ri_img_size;
	}
	
	public String getRi_is_main() {
		return ri_is_main;
	}
	
	public void setRi_is_main(String ri_is_main) {
		this.ri_is_main = ri_is_main;
	}
	
	public int getRi_display_order() {
		return ri_display_order;
	}
	
	public void setRi_display_order(int ri_display_order) {
		this.ri_display_order = ri_display_order;
	}
	
	public String getRi_upload_date() {
		return ri_upload_date;
	}
	
	public void setRi_upload_date(String ri_upload_date) {
		this.ri_upload_date = ri_upload_date;
	}
	
	public String getRi_status() {
		return ri_status;
	}
	
	public void setRi_status(String ri_status) {
		this.ri_status = ri_status;
	}
}
