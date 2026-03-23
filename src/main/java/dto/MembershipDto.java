package dto;

/**
 * 멤버십 DTO
 * - 멤버십 구독 정보와 결제 정보를 포함
 */
public class MembershipDto {

    private String mb_no;              // 멤버십 번호 (PK) - M00001
    private String mb_id;              // 회원 ID
    private String mb_membership;      // 멤버십 등급 (silver/gold/premium)
    private String mb_reg_date;        // 등록일
    private String mb_start_date;      // 시작일
    private String mb_end_date;        // 종료일
    private String mb_update_date;     // 수정일
    private String mb_exit_date;       // 탈퇴일
    
    // 결제 관련 필드 (추가)
    private String mb_payment_no;      // 결제 번호 (P_NO와 연결)
    private String mb_order_id;        // 주문 ID (MEMBERSHIP_xxxxx)
    private int mb_amount;             // 결제 금액
    private String mb_payment_status;  // 결제 상태 (PENDING/COMPLETED/CANCELED)

    // 기본 생성자
    public MembershipDto() {
    }
    
    // 기존 생성자 (기존 코드와 호환성 유지)
    public MembershipDto(String mb_id, String mb_membership, String mb_reg_date, 
                        String mb_update_date, String mb_exit_date) {
        this.mb_id = mb_id;
        this.mb_membership = mb_membership;
        this.mb_reg_date = mb_reg_date;
        this.mb_update_date = mb_update_date;
        this.mb_exit_date = mb_exit_date;
    }
    
    // 전체 필드 생성자
    public MembershipDto(String mb_no, String mb_id, String mb_membership, 
                        String mb_reg_date, String mb_start_date, String mb_end_date,
                        String mb_update_date, String mb_exit_date,
                        String mb_payment_no, String mb_order_id, 
                        int mb_amount, String mb_payment_status) {
        this.mb_no = mb_no;
        this.mb_id = mb_id;
        this.mb_membership = mb_membership;
        this.mb_reg_date = mb_reg_date;
        this.mb_start_date = mb_start_date;
        this.mb_end_date = mb_end_date;
        this.mb_update_date = mb_update_date;
        this.mb_exit_date = mb_exit_date;
        this.mb_payment_no = mb_payment_no;
        this.mb_order_id = mb_order_id;
        this.mb_amount = mb_amount;
        this.mb_payment_status = mb_payment_status;
    }

    // Getters
    public String getMb_no() {
        return mb_no;
    }

    public String getMb_id() {
        return mb_id;
    }
    
    // 기존 코드 호환성을 위한 getId()
    public String getId() {
        return mb_id;
    }

    public String getMb_membership() {
        return mb_membership;
    }
    
    // 기존 코드 호환성을 위한 getMembership()
    public String getMembership() {
        return mb_membership;
    }

    public String getMb_reg_date() {
        return mb_reg_date;
    }

    public String getMb_start_date() {
        return mb_start_date;
    }

    public String getMb_end_date() {
        return mb_end_date;
    }

    public String getMb_update_date() {
        return mb_update_date;
    }

    public String getMb_exit_date() {
        return mb_exit_date;
    }

    public String getMb_payment_no() {
        return mb_payment_no;
    }

    public String getMb_order_id() {
        return mb_order_id;
    }

    public int getMb_amount() {
        return mb_amount;
    }

    public String getMb_payment_status() {
        return mb_payment_status;
    }

    // Setters
    public void setMb_no(String mb_no) {
        this.mb_no = mb_no;
    }

    public void setMb_id(String mb_id) {
        this.mb_id = mb_id;
    }

    public void setMb_membership(String mb_membership) {
        this.mb_membership = mb_membership;
    }

    public void setMb_reg_date(String mb_reg_date) {
        this.mb_reg_date = mb_reg_date;
    }

    public void setMb_start_date(String mb_start_date) {
        this.mb_start_date = mb_start_date;
    }

    public void setMb_end_date(String mb_end_date) {
        this.mb_end_date = mb_end_date;
    }

    public void setMb_update_date(String mb_update_date) {
        this.mb_update_date = mb_update_date;
    }

    public void setMb_exit_date(String mb_exit_date) {
        this.mb_exit_date = mb_exit_date;
    }

    public void setMb_payment_no(String mb_payment_no) {
        this.mb_payment_no = mb_payment_no;
    }

    public void setMb_order_id(String mb_order_id) {
        this.mb_order_id = mb_order_id;
    }

    public void setMb_amount(int mb_amount) {
        this.mb_amount = mb_amount;
    }

    public void setMb_payment_status(String mb_payment_status) {
        this.mb_payment_status = mb_payment_status;
    }
}
