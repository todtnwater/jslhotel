package dto;

public class PaymentDto {
    
    private String p_no;
    private String p_reservation_no;
    private int p_amount;
    private String p_method;
    private String p_status;
    private String p_register_date;
    private String p_complete_date;
    private String p_cancel_date;
    private String p_last_update;
    private String payment_key;
    private String order_id;
    
    public PaymentDto() {
    }
    
    public PaymentDto(String p_no, String p_reservation_no, int p_amount, String p_method, 
                     String p_status, String payment_key, String order_id, String p_register_date,
                     String p_complete_date, String p_cancel_date, String p_last_update) {
        this.p_no = p_no;
        this.p_reservation_no = p_reservation_no;
        this.p_amount = p_amount;
        this.p_method = p_method;
        this.p_status = p_status;
        this.payment_key = payment_key;
        this.order_id = order_id;
        this.p_register_date = p_register_date;
        this.p_complete_date = p_complete_date;
        this.p_cancel_date = p_cancel_date;
        this.p_last_update = p_last_update;
    }

    public String getP_no() {
        return p_no;
    }

    public void setP_no(String p_no) {
        this.p_no = p_no;
    }

    public String getP_reservation_no() {
        return p_reservation_no;
    }

    public void setP_reservation_no(String p_reservation_no) {
        this.p_reservation_no = p_reservation_no;
    }

    public int getP_amount() {
        return p_amount;
    }

    public void setP_amount(int p_amount) {
        this.p_amount = p_amount;
    }

    public String getP_method() {
        return p_method;
    }

    public void setP_method(String p_method) {
        this.p_method = p_method;
    }

    public String getP_status() {
        return p_status;
    }

    public void setP_status(String p_status) {
        this.p_status = p_status;
    }

    public String getP_register_date() {
        return p_register_date;
    }

    public void setP_register_date(String p_register_date) {
        this.p_register_date = p_register_date;
    }

    public String getP_complete_date() {
        return p_complete_date;
    }

    public void setP_complete_date(String p_complete_date) {
        this.p_complete_date = p_complete_date;
    }

    public String getP_cancel_date() {
        return p_cancel_date;
    }

    public void setP_cancel_date(String p_cancel_date) {
        this.p_cancel_date = p_cancel_date;
    }

    public String getP_last_update() {
        return p_last_update;
    }

    public void setP_last_update(String p_last_update) {
        this.p_last_update = p_last_update;
    }

    public String getPayment_key() {
        return payment_key;
    }

    public void setPayment_key(String payment_key) {
        this.payment_key = payment_key;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}