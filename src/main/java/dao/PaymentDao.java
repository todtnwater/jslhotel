package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import common.DBConnection;
import dto.PaymentDto;

/**
 * 결제 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * - 결제 정보 저장/조회/수정
 * - 토스페이먼츠와 연동된 결제 데이터 관리
 */
public class PaymentDao {
    
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    /**
     * 결제 정보를 DB에 저장
     * 토스페이먼츠 결제 승인 후 호출됨
     * 
     * @param dto 결제 정보 DTO
     * @return 성공 시 1, 실패 시 0
     */
    public int insertPayment(PaymentDto dto) {
        int result = 0;
        
        String sql = "INSERT INTO JSL_HOTEL_PAYMENT " +
                    "(P_NO, P_RESERVATION_NO, P_AMOUNT, P_METHOD, P_STATUS, " +
                    " PAYMENT_KEY, ORDER_ID, P_REGISTER_DATE) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, SYSDATE)";
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            
            ps.setString(1, dto.getP_no());                    // 결제 번호 (PK)
            ps.setString(2, dto.getP_reservation_no());        // 예약 번호 (FK)
            ps.setInt(3, dto.getP_amount());                   // 결제 금액
            ps.setString(4, dto.getP_method());                // 결제 수단 (CARD/TRANSFER/VIRTUAL_ACCOUNT)
            ps.setString(5, dto.getP_status());                // 결제 상태 (PENDING/COMPLETED/FAILED/CANCELED)
            ps.setString(6, dto.getPayment_key());             // 토스페이먼츠 결제 키 (환불/취소 시 필요)
            ps.setString(7, dto.getOrder_id());                // 주문 ID (중복 방지용)
            
            result = ps.executeUpdate();
            
            System.out.println("결제 INSERT 완료: " + dto.getP_no());
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("insertPayment() 오류");
        } finally {
            DBConnection.closeDB(con, ps, rs);
            System.out.println("sql 결제완료 ");
        }
        
        return result;
    }
    
    /**
     * 결제 상태 업데이트
     * 결제 완료/취소 시 호출
     * 
     * @param pNo 결제 번호
     * @param status 변경할 상태 (COMPLETED, CANCELED 등)
     * @param completeDate 완료 날짜 (YYYY-MM-DD HH24:MI:SS 형식)
     * @return 성공 시 1, 실패 시 0
     */
    public int updatePaymentStatus(String pNo, String status, String completeDate) {
        int result = 0;
        
        String sql = "UPDATE JSL_HOTEL_PAYMENT " +
                    "SET P_STATUS = ?, P_COMPLETE_DATE = TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), " +
                    "    P_LAST_UPDATE = SYSDATE " +
                    "WHERE P_NO = ?";
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, completeDate);
            ps.setString(3, pNo);
            
            result = ps.executeUpdate();
            
            System.out.println("결제 상태 업데이트 완료: " + pNo + " -> " + status);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("updatePaymentStatus() 오류");
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        
        return result;
    }
    
    /**
     * 결제 번호로 결제 정보 조회
     * 결제 내역 조회, 환불 처리 등에 사용
     * 
     * @param pNo 결제 번호
     * @return 결제 정보 DTO, 없으면 null
     */
    public PaymentDto getPaymentByNo(String pNo) {
        PaymentDto dto = null;
        
        String sql = "SELECT * FROM JSL_HOTEL_PAYMENT WHERE P_NO = ?";
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, pNo);
            rs = ps.executeQuery();
            
            if(rs.next()) {
                dto = new PaymentDto(
                    rs.getString("P_NO"),                    // 결제 번호
                    rs.getString("P_RESERVATION_NO"),        // 예약 번호
                    rs.getInt("P_AMOUNT"),                   // 결제 금액
                    rs.getString("P_METHOD"),                // 결제 수단
                    rs.getString("P_STATUS"),                // 결제 상태
                    rs.getString("PAYMENT_KEY"),             // 토스 결제 키
                    rs.getString("ORDER_ID"),                // 주문 ID
                    rs.getString("P_REGISTER_DATE"),         // 등록일
                    rs.getString("P_COMPLETE_DATE"),         // 완료일
                    rs.getString("P_CANCEL_DATE"),           // 취소일
                    rs.getString("P_LAST_UPDATE")            // 마지막 업데이트
                );
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getPaymentByNo() 오류");
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        
        return dto;
    }
    
    /**
     * 주문 ID로 결제 정보 조회
     * 중복 결제 방지, 결제 검증 등에 사용
     * 
     * 주의: ORDER_ID는 UNIQUE 제약조건이 있어야 함
     * 
     * @param orderId 주문 ID (ORDER_xxxxx 형식)
     * @return 결제 정보 DTO, 없으면 null
     */
    public PaymentDto getPaymentByOrderId(String orderId) {
        PaymentDto dto = null;
        
        String sql = "SELECT * FROM JSL_HOTEL_PAYMENT WHERE ORDER_ID = ?";
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, orderId);
            rs = ps.executeQuery();
            
            if(rs.next()) {
                dto = new PaymentDto(
                    rs.getString("P_NO"),
                    rs.getString("P_RESERVATION_NO"),
                    rs.getInt("P_AMOUNT"),
                    rs.getString("P_METHOD"),
                    rs.getString("P_STATUS"),
                    rs.getString("PAYMENT_KEY"),
                    rs.getString("ORDER_ID"),
                    rs.getString("P_REGISTER_DATE"),
                    rs.getString("P_COMPLETE_DATE"),
                    rs.getString("P_CANCEL_DATE"),
                    rs.getString("P_LAST_UPDATE")
                );
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getPaymentByOrderId() 오류");
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        
        return dto;
    }
}