package dto;

public class ReservationDto {

    private String rv_no;
    private String rv_room_no;
    private String rv_member_id;
    private String rv_check_in_date;
    private String rv_check_out_date;
    private int rv_guest_count;
    private int rv_total_price;
    private String rv_status;
    private String rv_request_message;
    private String rv_register_date;
    private String rv_cancel_date;
    private String rv_last_update;

    private String rv_customer_first_name;
    private String rv_customer_last_name;
    private String rv_customer_email_1;
    private String rv_customer_email_2;
    private String rv_customer_mobile_1;
    private String rv_customer_mobile_2;
    private String rv_customer_mobile_3;

    private int rv_original_price;
    private int rv_discount_amount;
    private String rv_membership_level;
    private String rv_country;

    private String rv_room_type;
    private int rv_final_price;
    
    private String order_id;

    public ReservationDto() {
    }

    public String getRv_no() {
        return rv_no;
    }

    public void setRv_no(String rv_no) {
        this.rv_no = rv_no;
    }

    public String getRv_room_no() {
        return rv_room_no;
    }

    public void setRv_room_no(String rv_room_no) {
        this.rv_room_no = rv_room_no;
    }

    public String getRv_member_id() {
        return rv_member_id;
    }

    public void setRv_member_id(String rv_member_id) {
        this.rv_member_id = rv_member_id;
    }

    public String getRv_check_in_date() {
        return rv_check_in_date;
    }

    public void setRv_check_in_date(String rv_check_in_date) {
        this.rv_check_in_date = rv_check_in_date;
    }

    public String getRv_check_out_date() {
        return rv_check_out_date;
    }

    public void setRv_check_out_date(String rv_check_out_date) {
        this.rv_check_out_date = rv_check_out_date;
    }

    public int getRv_guest_count() {
        return rv_guest_count;
    }

    public void setRv_guest_count(int rv_guest_count) {
        this.rv_guest_count = rv_guest_count;
    }

    public int getRv_total_price() {
        return rv_total_price;
    }

    public void setRv_total_price(int rv_total_price) {
        this.rv_total_price = rv_total_price;
    }

    public String getRv_status() {
        return rv_status;
    }

    public void setRv_status(String rv_status) {
        this.rv_status = rv_status;
    }

    public String getRv_request_message() {
        return rv_request_message;
    }

    public void setRv_request_message(String rv_request_message) {
        this.rv_request_message = rv_request_message;
    }

    public String getRv_register_date() {
        return rv_register_date;
    }

    public void setRv_register_date(String rv_register_date) {
        this.rv_register_date = rv_register_date;
    }

    public String getRv_cancel_date() {
        return rv_cancel_date;
    }

    public void setRv_cancel_date(String rv_cancel_date) {
        this.rv_cancel_date = rv_cancel_date;
    }

    public String getRv_last_update() {
        return rv_last_update;
    }

    public void setRv_last_update(String rv_last_update) {
        this.rv_last_update = rv_last_update;
    }

    public String getRv_customer_first_name() {
        return rv_customer_first_name;
    }

    public void setRv_customer_first_name(String rv_customer_first_name) {
        this.rv_customer_first_name = rv_customer_first_name;
    }

    public String getRv_customer_last_name() {
        return rv_customer_last_name;
    }

    public void setRv_customer_last_name(String rv_customer_last_name) {
        this.rv_customer_last_name = rv_customer_last_name;
    }

    public String getRv_customer_email_1() {
        return rv_customer_email_1;
    }

    public void setRv_customer_email_1(String rv_customer_email_1) {
        this.rv_customer_email_1 = rv_customer_email_1;
    }

    public String getRv_customer_email_2() {
        return rv_customer_email_2;
    }

    public void setRv_customer_email_2(String rv_customer_email_2) {
        this.rv_customer_email_2 = rv_customer_email_2;
    }

    public String getRv_customer_mobile_1() {
        return rv_customer_mobile_1;
    }

    public void setRv_customer_mobile_1(String rv_customer_mobile_1) {
        this.rv_customer_mobile_1 = rv_customer_mobile_1;
    }

    public String getRv_customer_mobile_2() {
        return rv_customer_mobile_2;
    }

    public void setRv_customer_mobile_2(String rv_customer_mobile_2) {
        this.rv_customer_mobile_2 = rv_customer_mobile_2;
    }

    public String getRv_customer_mobile_3() {
        return rv_customer_mobile_3;
    }

    public void setRv_customer_mobile_3(String rv_customer_mobile_3) {
        this.rv_customer_mobile_3 = rv_customer_mobile_3;
    }

    public int getRv_original_price() {
        return rv_original_price;
    }

    public void setRv_original_price(int rv_original_price) {
        this.rv_original_price = rv_original_price;
    }

    public int getRv_discount_amount() {
        return rv_discount_amount;
    }

    public void setRv_discount_amount(int rv_discount_amount) {
        this.rv_discount_amount = rv_discount_amount;
    }

    public String getRv_membership_level() {
        return rv_membership_level;
    }

    public void setRv_membership_level(String rv_membership_level) {
        this.rv_membership_level = rv_membership_level;
    }

    public String getRv_country() {
        return rv_country;
    }

    public void setRv_country(String rv_country) {
        this.rv_country = rv_country;
    }

    public String getRv_room_type() {
        return rv_room_type;
    }

    public void setRv_room_type(String rv_room_type) {
        this.rv_room_type = rv_room_type;
    }

    public int getRv_final_price() {
        return rv_final_price;
    }

    public void setRv_final_price(int rv_final_price) {
        this.rv_final_price = rv_final_price;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getRv_customer_full_name() {
        return (rv_customer_first_name != null ? rv_customer_first_name : "") + " " +
               (rv_customer_last_name != null ? rv_customer_last_name : "");
    }

    public String getRv_customer_email() {
        return (rv_customer_email_1 != null ? rv_customer_email_1 : "") + "@" +
               (rv_customer_email_2 != null ? rv_customer_email_2 : "");
    }

    public String getRv_customer_mobile_phone() {
        return (rv_customer_mobile_1 != null ? rv_customer_mobile_1 : "") + "-" +
               (rv_customer_mobile_2 != null ? rv_customer_mobile_2 : "") + "-" +
               (rv_customer_mobile_3 != null ? rv_customer_mobile_3 : "");
    }
}