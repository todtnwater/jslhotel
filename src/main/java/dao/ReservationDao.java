package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import common.DBConnection;
import dto.ReservationDto;
import dto.RoomDto;

public class ReservationDao {
    
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    private ReservationDto mapToDto(ResultSet rs) throws Exception {
        ReservationDto dto = new ReservationDto();
        
        dto.setRv_no(rs.getString("RV_NO"));
        dto.setRv_room_no(rs.getString("RV_ROOM_NO"));
        dto.setRv_member_id(rs.getString("RV_MEMBER_ID"));
        dto.setRv_check_in_date(rs.getString("RV_CHECK_IN_DATE"));
        dto.setRv_check_out_date(rs.getString("RV_CHECK_OUT_DATE"));
        dto.setRv_guest_count(rs.getInt("RV_GUEST_COUNT"));
        dto.setRv_total_price(rs.getInt("RV_TOTAL_PRICE"));
        dto.setRv_status(rs.getString("RV_STATUS"));
        dto.setRv_request_message(rs.getString("RV_REQUEST_MESSAGE"));
        
        dto.setRv_customer_first_name(rs.getString("RV_CUSTOMER_FIRST_NAME"));
        dto.setRv_customer_last_name(rs.getString("RV_CUSTOMER_LAST_NAME"));
        dto.setRv_customer_email_1(rs.getString("RV_CUSTOMER_EMAIL_1"));
        dto.setRv_customer_email_2(rs.getString("RV_CUSTOMER_EMAIL_2"));
        dto.setRv_customer_mobile_1(rs.getString("RV_CUSTOMER_MOBILE_1"));
        dto.setRv_customer_mobile_2(rs.getString("RV_CUSTOMER_MOBILE_2"));
        dto.setRv_customer_mobile_3(rs.getString("RV_CUSTOMER_MOBILE_3"));
        
        dto.setRv_original_price(rs.getInt("RV_ORIGINAL_PRICE"));
        dto.setRv_discount_amount(rs.getInt("RV_DISCOUNT_AMOUNT"));
        dto.setRv_membership_level(rs.getString("RV_MEMBERSHIP_LEVEL"));
        dto.setRv_country(rs.getString("RV_COUNTRY"));
        
        dto.setRv_register_date(rs.getString("RV_REGISTER_DATE"));
        dto.setRv_cancel_date(rs.getString("RV_CANCEL_DATE"));
        dto.setRv_last_update(rs.getString("RV_LAST_UPDATE"));
        
        dto.setRv_final_price(rs.getInt("RV_TOTAL_PRICE"));
        
        try {
            dto.setOrder_id(rs.getString("ORDER_ID"));
        } catch (Exception ignore) {
        }
        
        try {
            dto.setRv_room_type(rs.getString("R_TYPE"));
        } catch (Exception ignore) {
        }
        
        return dto;
    }
    
    /**
     * ✅ ORDER_ID 컬럼 없이 예약 정보 저장
     * ORDER_ID는 PAYMENT 테이블에만 저장됨
     */
    public int insertReservation(ReservationDto dto) {
        int result = 0;

        String sql = "INSERT INTO JSL_HOTEL_RESERVATION " +
                    "(RV_NO, RV_ROOM_NO, RV_MEMBER_ID, RV_CHECK_IN_DATE, RV_CHECK_OUT_DATE, " +
                    " RV_GUEST_COUNT, RV_TOTAL_PRICE, RV_STATUS, RV_REQUEST_MESSAGE, " +
                    " RV_CUSTOMER_FIRST_NAME, RV_CUSTOMER_LAST_NAME, RV_CUSTOMER_EMAIL_1, " +
                    " RV_CUSTOMER_EMAIL_2, RV_CUSTOMER_MOBILE_1, RV_CUSTOMER_MOBILE_2, " +
                    " RV_CUSTOMER_MOBILE_3, RV_ORIGINAL_PRICE, RV_DISCOUNT_AMOUNT, " +
                    " RV_MEMBERSHIP_LEVEL, RV_COUNTRY, RV_REGISTER_DATE) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            
            int i = 1;
            ps.setString(i++, dto.getRv_no());
            ps.setString(i++, dto.getRv_room_no());
            ps.setString(i++, dto.getRv_member_id());
            ps.setString(i++, dto.getRv_check_in_date());
            ps.setString(i++, dto.getRv_check_out_date());
            ps.setInt(i++, dto.getRv_guest_count());
            ps.setInt(i++, dto.getRv_total_price());
            ps.setString(i++, dto.getRv_status());
            ps.setString(i++, dto.getRv_request_message());
            
            ps.setString(i++, dto.getRv_customer_first_name());
            ps.setString(i++, dto.getRv_customer_last_name());
            ps.setString(i++, dto.getRv_customer_email_1());
            ps.setString(i++, dto.getRv_customer_email_2());
            ps.setString(i++, dto.getRv_customer_mobile_1());
            ps.setString(i++, dto.getRv_customer_mobile_2());
            ps.setString(i++, dto.getRv_customer_mobile_3());
            
            ps.setInt(i++, dto.getRv_original_price());
            ps.setInt(i++, dto.getRv_discount_amount());
            ps.setString(i++, dto.getRv_membership_level());
            ps.setString(i++, dto.getRv_country());
            
            result = ps.executeUpdate();
            
            System.out.println("✅ 예약 INSERT 완료 (ORDER_ID는 PAYMENT 테이블에만 저장)");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("insertReservation() 오류: " + e.getMessage());
        } finally {
            DBConnection.closeDB(con, ps, rs);
            System.out.println("booking 결제완료 202511111834");
        }
        return result;
    }
    
    /**
     * ✅ 예약 번호로 예약 정보 조회
     * PAYMENT 테이블과 JOIN하여 ORDER_ID도 함께 가져옴
     */
    public ReservationDto selectReservationByNo(String rvNo) {
        ReservationDto dto = null;
        
        String sql = "SELECT R.*, T.R_TYPE, P.ORDER_ID " +
                     "FROM JSL_HOTEL_RESERVATION R " +
                     "JOIN JSL_HOTEL_ROOM T ON R.RV_ROOM_NO = T.R_ROOM_NO " +
                     "LEFT JOIN JSL_HOTEL_PAYMENT P ON R.RV_NO = P.P_RESERVATION_NO " +
                     "WHERE R.RV_NO = ?";
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, rvNo);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                dto = mapToDto(rs);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("selectReservationByNo() 오류: " + e.getMessage());
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return dto;
    }
    
    /**
     * ✅ 예약 번호로 예약 정보 조회 (이메일 조건 제거됨)
     * PAYMENT 테이블과 JOIN하여 ORDER_ID도 함께 가져옴
     */
    public ReservationDto selectReservationByNumberAndEmail(String rvNo) {
        ReservationDto dto = null;
        
        String sql = "SELECT R.*, T.R_TYPE, P.ORDER_ID " +
                     "FROM JSL_HOTEL_RESERVATION R " +
                     "JOIN JSL_HOTEL_ROOM T ON R.RV_ROOM_NO = T.R_ROOM_NO " +
                     "LEFT JOIN JSL_HOTEL_PAYMENT P ON R.RV_NO = P.P_RESERVATION_NO " +
                     "WHERE R.RV_NO = ?";
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, rvNo);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                dto = mapToDto(rs);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("selectReservationByNumberAndEmail() 오류: " + e.getMessage());
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return dto;
    }
    
    /**
     * 검색 조건에 맞는 예약 목록을 조회하는 메소드 (관리자 검색 기능)
     */
    public List<ReservationDto> selectReservationsBySearch(String searchType, String searchValue) {
        List<ReservationDto> list = new ArrayList<>();
        String sql = "SELECT * FROM JSL_HOTEL_RESERVATION ";
        
        // 검색 조건 추가
        if (searchType != null && !searchType.trim().isEmpty() && 
            searchValue != null && !searchValue.trim().isEmpty()) {
            
            sql += " WHERE ";
            searchValue = "%" + searchValue.toUpperCase() + "%"; // 검색어 대문자 및 % 처리
            
            switch (searchType) {
                case "rv_no":
                    sql += " UPPER(RV_NO) LIKE ? ";
                    break;
                case "rv_customer_name":
                    // 이름은 FIRST_NAME과 LAST_NAME을 합치거나 각각 검색
                    sql += " (UPPER(RV_CUSTOMER_FIRST_NAME) LIKE ? OR UPPER(RV_CUSTOMER_LAST_NAME) LIKE ? OR UPPER(RV_CUSTOMER_FIRST_NAME || ' ' || RV_CUSTOMER_LAST_NAME) LIKE ?) ";
                    break;
                case "rv_status":
                    // 예약 상태는 정확히 일치하는 경우를 가정 (일부 DB에서는 = 대신 LIKE를 선호할 수 있음)
                    sql += " UPPER(RV_STATUS) = ? ";
                    searchValue = searchValue.replace("%", ""); // = 이므로 % 제거
                    break;
                default:
                    // 기본값: RV_NO로 검색
                    sql += " UPPER(RV_NO) LIKE ? ";
                    break;
            }
        }
        
        sql += " ORDER BY RV_REGISTER_DATE DESC, RV_NO DESC";

        try {
            con = DBConnection.getConnection(); 
            ps = con.prepareStatement(sql);
            
            // PreparedStatement에 값 설정
            if (searchType != null && !searchType.trim().isEmpty() && 
                searchValue != null && !searchValue.trim().isEmpty()) {
                
                if ("rv_customer_name".equals(searchType)) {
                    ps.setString(1, searchValue); // FIRST_NAME LIKE %검색어%
                    ps.setString(2, searchValue); // LAST_NAME LIKE %검색어%
                    ps.setString(3, searchValue); // FULL_NAME LIKE %검색어%
                } else if ("rv_status".equals(searchType)) {
                    ps.setString(1, searchValue); // RV_STATUS = '검색어'
                } else {
                    ps.setString(1, searchValue); // RV_NO LIKE %검색어%
                }
            }
            
            rs = ps.executeQuery();

            while (rs.next()) {
                ReservationDto dto = mapToDto(rs); 
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("selectReservationsBySearch() 오류: " + e.getMessage());
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return list;
    }

    /**
     * 특정 회원 ID의 예약 목록을 조회하는 메소드 (회원 본인 조회 기능)
     * - 고객님께서 제공해주신 코드입니다. 
     * - Room Type (R.R_TYPE)과 Payment Order ID (P.ORDER_ID) 조인을 포함합니다.
     */
    public List<ReservationDto> selectReservationsByMemberId(String memberId) {
        List<ReservationDto> list = new ArrayList<>();
        
        // T.R_TYPE을 DTO에서 사용하는 RV_ROOM_TYPE으로 AS 처리하여 mapToDto에서 오류 없이 사용하도록 합니다.
        // DTO 필드명이 'rv_room_type'이므로, ResultSet 컬럼 이름을 'RV_ROOM_TYPE'로 맞춥니다.
        String sql = "SELECT R.*, T.R_TYPE AS RV_ROOM_TYPE, P.ORDER_ID " +
                     "FROM JSL_HOTEL_RESERVATION R " +
                     "JOIN JSL_HOTEL_ROOM T ON R.RV_ROOM_NO = T.R_ROOM_NO " +
                     "LEFT JOIN JSL_HOTEL_PAYMENT P ON R.RV_NO = P.P_RESERVATION_NO " +
                     "WHERE R.RV_MEMBER_ID = ? " +
                     "ORDER BY R.RV_REGISTER_DATE DESC";
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, memberId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapToDto(rs));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("selectReservationsByMemberId() 오류: " + e.getMessage());
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return list;
    }
    
    /**
     * ✅ 예약 가능한 객실 검색
     */
    public List<RoomDto> searchAvailableRooms(String checkIn, String checkOut, int adultCount, int childCount) {
        List<RoomDto> list = new ArrayList<>();
        
        String sql = "SELECT R_ROOM_NO, R_TYPE, R_FLOOR, R_SCALE, " +
                    "       R_PEOPLE_STANDARD, R_PEOPLE_MAX, R_EXTRA_PERSON_FEE, " +
                    "       R_BED_TYPE, R_PRICE, R_STATUS " +
                    "FROM JSL_HOTEL_ROOM " +
                    "WHERE R_STATUS = 'AVAILABLE' " +
                    "AND R_ROOM_NO NOT IN ( " +
                    "    SELECT RV_ROOM_NO FROM JSL_HOTEL_RESERVATION " +
                    "    WHERE RV_STATUS IN ('CONFIRMED', 'CHECKED_IN') " +
                    "    AND (TO_DATE(?, 'YYYY-MM-DD') < TO_DATE(RV_CHECK_OUT_DATE, 'YYYY-MM-DD') " +
                    "    AND TO_DATE(?, 'YYYY-MM-DD') > TO_DATE(RV_CHECK_IN_DATE, 'YYYY-MM-DD')) " +
                    ") " +
                    "ORDER BY R_FLOOR, R_ROOM_NO";
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, checkIn);
            ps.setString(2, checkOut);
            rs = ps.executeQuery();
            
            while(rs.next()) {
                RoomDto dto = new RoomDto();

                dto.setR_room_no(rs.getString("R_ROOM_NO"));
                dto.setR_type(rs.getString("R_TYPE"));
                dto.setR_floor(rs.getInt("R_FLOOR"));
                dto.setR_scale(rs.getString("R_SCALE"));
                dto.setR_people_standard(rs.getInt("R_PEOPLE_STANDARD"));
                dto.setR_people_max(rs.getInt("R_PEOPLE_MAX"));
                dto.setR_bed_type(rs.getString("R_BED_TYPE"));
                dto.setR_price(rs.getInt("R_PRICE"));
                dto.setR_status(rs.getString("R_STATUS"));
                
                dto.setR_extra_person_fee(rs.getInt("R_EXTRA_PERSON_FEE"));
                
                list.add(dto);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("searchAvailableRooms() 오류");
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        
        return list;
    }
    
    /**
     * ✅ 총 가격 계산
     */
    public int calculateTotalPrice(RoomDto room, String checkIn, String checkOut, int adultCount, int childCount) {
        try {
            LocalDate startDate = LocalDate.parse(checkIn);
            LocalDate endDate = LocalDate.parse(checkOut);
            long nights = ChronoUnit.DAYS.between(startDate, endDate);
            
            if(nights <= 0) {
                return 0;
            }
            
            int basePrice = room.getR_price();
            int totalGuests = adultCount + childCount;
            int standardGuests = room.getR_people_standard();
            
            int extraGuests = Math.max(0, totalGuests - standardGuests);
            
            int extraPersonFee = room.getR_extra_person_fee();
            
            int totalPrice = (int)((basePrice + (extraGuests * extraPersonFee)) * nights);
            
            return totalPrice;
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("calculateTotalPrice() 오류");
            return 0;
        }
    }
    
    public List<ReservationDto> selectAllReservations() {
        List<ReservationDto> list = new ArrayList<>();

       
        String sql = "SELECT * FROM JSL_HOTEL_RESERVATION ORDER BY RV_REGISTER_DATE DESC, RV_NO DESC"; 

        try {
            // DB 연결
            con = DBConnection.getConnection(); 
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery(); 
            while (rs.next()) {
                ReservationDto dto = mapToDto(rs); 
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("selectAllReservations() 오류");
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return list;
    }
}