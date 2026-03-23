package dto;

/**
 * 포인트 내역 DTO
 * - 포인트 적립/사용 내역 관리
 */
public class PointDto {
    
    private String pt_no;              // 포인트 내역 번호 (PK)
    private String pt_member_id;       // 회원 ID
    private String pt_type;            // 적립/사용 (EARN/USE/CANCEL)
    private int pt_amount;             // 포인트 금액
    private int pt_balance;            // 거래 후 잔액
    private String pt_source;          // 출처 (RESERVATION/MEMBERSHIP/REFUND/ADMIN)
    private String pt_source_no;       // 출처 번호 (예약번호, 멤버십번호 등)
    private String pt_description;     // 설명
    private String pt_reg_date;        // 등록일
    private String pt_expire_date;     // 만료일
    private String pt_status;          // 상태 (ACTIVE/EXPIRED/CANCELED)
    
    // 기본 생성자
    public PointDto() {
    }
    
    // 전체 필드 생성자
    public PointDto(String pt_no, String pt_member_id, String pt_type, 
                   int pt_amount, int pt_balance, String pt_source, 
                   String pt_source_no, String pt_description, 
                   String pt_reg_date, String pt_expire_date, String pt_status) {
        this.pt_no = pt_no;
        this.pt_member_id = pt_member_id;
        this.pt_type = pt_type;
        this.pt_amount = pt_amount;
        this.pt_balance = pt_balance;
        this.pt_source = pt_source;
        this.pt_source_no = pt_source_no;
        this.pt_description = pt_description;
        this.pt_reg_date = pt_reg_date;
        this.pt_expire_date = pt_expire_date;
        this.pt_status = pt_status;
    }
    
    // 간단 생성자 (적립용)
    public PointDto(String pt_member_id, String pt_type, int pt_amount, 
                   String pt_source, String pt_source_no, String pt_description) {
        this.pt_member_id = pt_member_id;
        this.pt_type = pt_type;
        this.pt_amount = pt_amount;
        this.pt_source = pt_source;
        this.pt_source_no = pt_source_no;
        this.pt_description = pt_description;
    }

    // Getters
    public String getPt_no() {
        return pt_no;
    }

    public String getPt_member_id() {
        return pt_member_id;
    }

    public String getPt_type() {
        return pt_type;
    }

    public int getPt_amount() {
        return pt_amount;
    }

    public int getPt_balance() {
        return pt_balance;
    }

    public String getPt_source() {
        return pt_source;
    }

    public String getPt_source_no() {
        return pt_source_no;
    }

    public String getPt_description() {
        return pt_description;
    }

    public String getPt_reg_date() {
        return pt_reg_date;
    }

    public String getPt_expire_date() {
        return pt_expire_date;
    }

    public String getPt_status() {
        return pt_status;
    }

    // Setters
    public void setPt_no(String pt_no) {
        this.pt_no = pt_no;
    }

    public void setPt_member_id(String pt_member_id) {
        this.pt_member_id = pt_member_id;
    }

    public void setPt_type(String pt_type) {
        this.pt_type = pt_type;
    }

    public void setPt_amount(int pt_amount) {
        this.pt_amount = pt_amount;
    }

    public void setPt_balance(int pt_balance) {
        this.pt_balance = pt_balance;
    }

    public void setPt_source(String pt_source) {
        this.pt_source = pt_source;
    }

    public void setPt_source_no(String pt_source_no) {
        this.pt_source_no = pt_source_no;
    }

    public void setPt_description(String pt_description) {
        this.pt_description = pt_description;
    }

    public void setPt_reg_date(String pt_reg_date) {
        this.pt_reg_date = pt_reg_date;
    }

    public void setPt_expire_date(String pt_expire_date) {
        this.pt_expire_date = pt_expire_date;
    }

    public void setPt_status(String pt_status) {
        this.pt_status = pt_status;
    }

    @Override
    public String toString() {
        return "PointDto{" +
                "pt_no='" + pt_no + '\'' +
                ", pt_member_id='" + pt_member_id + '\'' +
                ", pt_type='" + pt_type + '\'' +
                ", pt_amount=" + pt_amount +
                ", pt_balance=" + pt_balance +
                ", pt_source='" + pt_source + '\'' +
                ", pt_source_no='" + pt_source_no + '\'' +
                ", pt_description='" + pt_description + '\'' +
                ", pt_reg_date='" + pt_reg_date + '\'' +
                ", pt_expire_date='" + pt_expire_date + '\'' +
                ", pt_status='" + pt_status + '\'' +
                '}';
    }
}
