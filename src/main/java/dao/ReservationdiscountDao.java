package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;

import common.DBConnection;

/**
 * 예약 할인 정보 DAO
 */
public class ReservationdiscountDao {
    
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    /**
     * 할인 번호 자동 생성 (DB에서 최대값 조회)
     */
    public String getDiscountNo() {
        String no = "";
        String sql = "SELECT NVL(MAX(RD_NO), 'RD00000') AS RD_NO " +
                     "FROM JSL_HOTEL_RESERVATION_DISCOUNT";
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()) {
                no = rs.getString("RD_NO");       // RD00001
                no = no.substring(2);             // 00001
                int discountNo = Integer.parseInt(no) + 1; // 00001 + 1
                DecimalFormat df = new DecimalFormat("RD00000");
                no = df.format(discountNo);       // RD00002
            }
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("❌ getDiscountNo() 오류");
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return no;
    }
    
    /**
     * 할인 정보 저장
     */
    public int insertDiscount(String reservationNo, String discountType, String discountName,
                             double discountRate, int originalAmount, 
                             int discountAmount, int finalAmount) {
        int result = 0;
        
        String sql = "INSERT INTO JSL_HOTEL_RESERVATION_DISCOUNT " +
                    "(RD_NO, RD_RESERVATION_NO, RD_DISCOUNT_TYPE, RD_DISCOUNT_NAME, " +
                    " RD_DISCOUNT_RATE, RD_ORIGINAL_AMOUNT, RD_DISCOUNT_AMOUNT, " +
                    " RD_FINAL_AMOUNT, RD_REGISTER_DATE) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            
            // ✅ DB에서 자동 증가 번호 생성
            String rdNo = getDiscountNo();
            
            ps.setString(1, rdNo);
            ps.setString(2, reservationNo);
            ps.setString(3, discountType);
            ps.setString(4, discountName);
            ps.setDouble(5, discountRate);
            ps.setInt(6, originalAmount);
            ps.setInt(7, discountAmount);
            ps.setInt(8, finalAmount);
            
            result = ps.executeUpdate();
            
            System.out.println("✅ 할인 정보 INSERT 완료: " + rdNo);
            System.out.println("   예약번호: " + reservationNo);
            System.out.println("   할인유형: " + discountType);
            System.out.println("   할인명: " + discountName);
            System.out.println("   할인율: " + discountRate + "%");
            System.out.println("   할인액: " + discountAmount + "원");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ insertDiscount() 오류");
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        
        return result;
    }
    
    /**
     * 예약 번호로 할인 정보 조회
     */
    public int getTotalDiscountByReservation(String reservationNo) {
        int totalDiscount = 0;
        
        String sql = "SELECT SUM(RD_DISCOUNT_AMOUNT) AS TOTAL_DISCOUNT " +
                    "FROM JSL_HOTEL_RESERVATION_DISCOUNT " +
                    "WHERE RD_RESERVATION_NO = ?";
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, reservationNo);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                totalDiscount = rs.getInt("TOTAL_DISCOUNT");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ getTotalDiscountByReservation() 오류");
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        
        return totalDiscount;
    }
    
    /**
     * 할인 정보 삭제 (예약 취소 시)
     */
    public int deleteDiscountByReservation(String reservationNo) {
        int result = 0;
        
        String sql = "DELETE FROM JSL_HOTEL_RESERVATION_DISCOUNT " +
                    "WHERE RD_RESERVATION_NO = ?";
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, reservationNo);
            
            result = ps.executeUpdate();
            
            if (result > 0) {
                System.out.println("✅ 할인 정보 삭제 완료: " + reservationNo + " (" + result + "건)");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ deleteDiscountByReservation() 오류");
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        
        return result;
    }
}