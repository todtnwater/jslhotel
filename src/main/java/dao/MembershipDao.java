package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;

import common.DBConnection;
import dto.MembershipDto;

public class MembershipDao {

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    /**
     * 멤버십 번호 자동 생성
     * @return 새로운 멤버십 번호 (M00001 형식)
     */
    public String getMembershipNo() {
        String no = "";
        String sql = "SELECT NVL(MAX(MB_NO), 'M00000') AS MB_NO " +
                    "FROM JSL_HOTEL_MEMBERSHIP";
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()) {
                no = rs.getString("MB_NO");
                no = no.substring(1);
                int membershipNo = Integer.parseInt(no) + 1;
                DecimalFormat df = new DecimalFormat("M00000");
                no = df.format(membershipNo);
            }
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("getMembershipNo() 오류: " + sql);
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return no;
    }
    
    /**
     * 멤버십 초기 세팅 (기존 메서드 - 호환성 유지)
     * @param no 멤버십 번호
     * @param id 회원 ID
     * @param reg_date 등록일
     * @return 성공 시 1, 실패 시 0
     */
    public int membershipSave(String no, String id, String reg_date) {
        int result = 0;
        String sql = "INSERT INTO JSL_HOTEL_MEMBERSHIP " +
                    "(MB_NO, MB_ID, MB_REG_DATE, MB_START_DATE) " +
                    "VALUES " +
                    "(?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'))";
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, no);
            ps.setString(2, id);
            ps.setString(3, reg_date);
            ps.setString(4, reg_date);
            result = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("membershipSave() 오류: " + sql);
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        
        return result;
    }
    
    /**
     * 멤버십 구독 정보 저장 (결제 연동)
     * @param dto 멤버십 정보 DTO
     * @return 성공 시 1, 실패 시 0
     */
    public int insertMembership(MembershipDto dto) {
        int result = 0;
        
        String sql = "INSERT INTO JSL_HOTEL_MEMBERSHIP " +
                    "(MB_NO, MB_ID, MB_MEMBERSHIP, MB_REG_DATE, MB_START_DATE, MB_END_DATE, " +
                    " MB_PAYMENT_NO, MB_ORDER_ID, MB_AMOUNT, MB_PAYMENT_STATUS) " +
                    "VALUES " +
                    "(?, ?, ?, SYSDATE, TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD'), " +
                    " ?, ?, ?, ?)";
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            
            ps.setString(1, dto.getMb_no());
            ps.setString(2, dto.getMb_id());
            ps.setString(3, dto.getMb_membership());
            ps.setString(4, dto.getMb_start_date());
            ps.setString(5, dto.getMb_end_date());
            ps.setString(6, dto.getMb_payment_no());
            ps.setString(7, dto.getMb_order_id());
            ps.setInt(8, dto.getMb_amount());
            ps.setString(9, dto.getMb_payment_status());
            
            result = ps.executeUpdate();
            
            System.out.println("멤버십 INSERT 완료: " + dto.getMb_no());
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("insertMembership() 오류");
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        
        return result;
    }
    
    /**
     * 멤버십 번호로 멤버십 정보 조회
     * @param mbNo 멤버십 번호
     * @return 멤버십 정보 DTO, 없으면 null
     */
    public MembershipDto getMembershipByNo(String mbNo) {
        MembershipDto dto = null;
        
        String sql = "SELECT MB_NO, MB_ID, MB_MEMBERSHIP, " +
                    "       TO_CHAR(MB_REG_DATE, 'YYYY-MM-DD') AS MB_REG_DATE, " +
                    "       TO_CHAR(MB_START_DATE, 'YYYY-MM-DD') AS MB_START_DATE, " +
                    "       TO_CHAR(MB_END_DATE, 'YYYY-MM-DD') AS MB_END_DATE, " +
                    "       MB_PAYMENT_NO, MB_ORDER_ID, MB_AMOUNT, MB_PAYMENT_STATUS " +
                    "FROM JSL_HOTEL_MEMBERSHIP " +
                    "WHERE MB_NO = ?";
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, mbNo);
            rs = ps.executeQuery();
            
            if(rs.next()) {
                dto = new MembershipDto(
                    rs.getString("MB_NO"),
                    rs.getString("MB_ID"),
                    rs.getString("MB_MEMBERSHIP"),
                    rs.getString("MB_REG_DATE"),
                    rs.getString("MB_START_DATE"),
                    rs.getString("MB_END_DATE"),
                    null,  // MB_UPDATE_DATE
                    null,  // MB_EXIT_DATE
                    rs.getString("MB_PAYMENT_NO"),
                    rs.getString("MB_ORDER_ID"),
                    rs.getInt("MB_AMOUNT"),
                    rs.getString("MB_PAYMENT_STATUS")
                );
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getMembershipByNo() 오류");
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        
        return dto;
    }
    
    /**
     * 회원 ID로 멤버십 정보 조회
     * @param memberId 회원 ID
     * @return 멤버십 정보 DTO, 없으면 null
     */
    public MembershipDto getMembershipByMemberId(String memberId) {
        MembershipDto dto = null;
        
        String sql = "SELECT MB_NO, MB_ID, MB_MEMBERSHIP, " +
                    "       TO_CHAR(MB_REG_DATE, 'YYYY-MM-DD') AS MB_REG_DATE, " +
                    "       TO_CHAR(MB_START_DATE, 'YYYY-MM-DD') AS MB_START_DATE, " +
                    "       TO_CHAR(MB_END_DATE, 'YYYY-MM-DD') AS MB_END_DATE, " +
                    "       MB_PAYMENT_NO, MB_ORDER_ID, MB_AMOUNT, MB_PAYMENT_STATUS " +
                    "FROM JSL_HOTEL_MEMBERSHIP " +
                    "WHERE MB_ID = ? " +
                    "ORDER BY MB_REG_DATE DESC";
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, memberId);
            rs = ps.executeQuery();
            
            if(rs.next()) {
                dto = new MembershipDto(
                    rs.getString("MB_NO"),
                    rs.getString("MB_ID"),
                    rs.getString("MB_MEMBERSHIP"),
                    rs.getString("MB_REG_DATE"),
                    rs.getString("MB_START_DATE"),
                    rs.getString("MB_END_DATE"),
                    null,  // MB_UPDATE_DATE - 테이블에 없음
                    null,  // MB_EXIT_DATE - 테이블에 없음
                    rs.getString("MB_PAYMENT_NO"),
                    rs.getString("MB_ORDER_ID"),
                    rs.getInt("MB_AMOUNT"),
                    rs.getString("MB_PAYMENT_STATUS")
                );
                
                System.out.println("✅ 멤버십 정보 조회 성공: " + memberId);
                System.out.println("   - 멤버십: " + dto.getMb_membership());
                System.out.println("   - 회원번호: " + dto.getMb_no());
            } else {
                System.out.println("⚠️ 멤버십 정보 없음: " + memberId);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getMembershipByMemberId() 오류");
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        
        return dto;
    }
    
    /**
     * 주문 ID로 멤버십 정보 조회
     * @param orderId 주문 ID
     * @return 멤버십 정보 DTO, 없으면 null
     */
    public MembershipDto getMembershipByOrderId(String orderId) {
        MembershipDto dto = null;
        
        String sql = "SELECT MB_NO, MB_ID, MB_MEMBERSHIP, " +
                    "       TO_CHAR(MB_REG_DATE, 'YYYY-MM-DD') AS MB_REG_DATE, " +
                    "       TO_CHAR(MB_START_DATE, 'YYYY-MM-DD') AS MB_START_DATE, " +
                    "       TO_CHAR(MB_END_DATE, 'YYYY-MM-DD') AS MB_END_DATE, " +
                    "       MB_PAYMENT_NO, MB_ORDER_ID, MB_AMOUNT, MB_PAYMENT_STATUS " +
                    "FROM JSL_HOTEL_MEMBERSHIP " +
                    "WHERE MB_ORDER_ID = ?";
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, orderId);
            rs = ps.executeQuery();
            
            if(rs.next()) {
                dto = new MembershipDto(
                    rs.getString("MB_NO"),
                    rs.getString("MB_ID"),
                    rs.getString("MB_MEMBERSHIP"),
                    rs.getString("MB_REG_DATE"),
                    rs.getString("MB_START_DATE"),
                    rs.getString("MB_END_DATE"),
                    null,  // MB_UPDATE_DATE
                    null,  // MB_EXIT_DATE
                    rs.getString("MB_PAYMENT_NO"),
                    rs.getString("MB_ORDER_ID"),
                    rs.getInt("MB_AMOUNT"),
                    rs.getString("MB_PAYMENT_STATUS")
                );
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getMembershipByOrderId() 오류");
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        
        return dto;
    }
    
    /**
     * 멤버십 결제 상태 업데이트
     * @param mbNo 멤버십 번호
     * @param status 결제 상태
     * @return 성공 시 1, 실패 시 0
     */
    public int updateMembershipStatus(String mbNo, String status) {
        int result = 0;
        
        String sql = "UPDATE JSL_HOTEL_MEMBERSHIP " +
                    "SET MB_PAYMENT_STATUS = ? " +
                    "WHERE MB_NO = ?";
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, mbNo);
            
            result = ps.executeUpdate();
            
            System.out.println("멤버십 상태 업데이트 완료: " + mbNo + " -> " + status);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("updateMembershipStatus() 오류");
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        
        return result;
    }

    public String getMembersnipNo() {
        String no = "";
        String sql = "select nvl(max(mb_no), 'M00000') as mb_no " +
                    "from jsl_hotel_membership ";
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()) {
                no = rs.getString("mb_no");
                no = no.substring(1);
                int membershipNo = Integer.parseInt(no) + 1;
                DecimalFormat df = new DecimalFormat("M00000");
                no = df.format(membershipNo);
            }
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("getMembershipNo() 오류 : "+sql);
        }finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return no;
    }
}