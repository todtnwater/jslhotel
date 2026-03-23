package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import common.DBConnection;
import dto.RoomDto;

public class RoomDao {
	
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	// 1. 층별 객실 목록 조회
	public List<RoomDto> getRoomListByFloor(int floor) {
		List<RoomDto> list = new ArrayList<>();
		
		String sql = "SELECT R_ROOM_NO, R_TYPE, R_FLOOR, R_SCALE, " +
					 "R_PEOPLE_STANDARD, R_PEOPLE_MAX, R_BED_TYPE, " +
					 "R_PRICE, R_STATUS " +
					 "FROM JSL_HOTEL_ROOM " +
					 "WHERE R_FLOOR = ? " +
					 "ORDER BY R_ROOM_NO";
		
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, floor);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				String r_room_no = rs.getString("R_ROOM_NO");
				String r_type = rs.getString("R_TYPE");
				int r_floor = rs.getInt("R_FLOOR");
				String r_scale = rs.getString("R_SCALE");
				int r_people_standard = rs.getInt("R_PEOPLE_STANDARD");
				int r_people_max = rs.getInt("R_PEOPLE_MAX");
				String r_bed_type = rs.getString("R_BED_TYPE");
				int r_price = rs.getInt("R_PRICE");
				String r_status = rs.getString("R_STATUS");
				
				RoomDto dto = new RoomDto(r_room_no, r_type, r_floor, r_scale,
										  r_people_standard, r_people_max, r_bed_type,
										  r_price, r_status);
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}
		
		return list;
	}
	
	// 2. 전체 객실 목록 조회
	public List<RoomDto> getAllRoomList() {
		List<RoomDto> list = new ArrayList<>();
		
		String sql = "SELECT R_ROOM_NO, R_TYPE, R_FLOOR, R_SCALE, " +
					 "R_PEOPLE_STANDARD, R_PEOPLE_MAX, R_BED_TYPE, " +
					 "R_PRICE, R_STATUS " +
					 "FROM JSL_HOTEL_ROOM " +
					 "ORDER BY R_FLOOR, R_ROOM_NO";
		
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				String r_room_no = rs.getString("R_ROOM_NO");
				String r_type = rs.getString("R_TYPE");
				int r_floor = rs.getInt("R_FLOOR");
				String r_scale = rs.getString("R_SCALE");
				int r_people_standard = rs.getInt("R_PEOPLE_STANDARD");
				int r_people_max = rs.getInt("R_PEOPLE_MAX");
				String r_bed_type = rs.getString("R_BED_TYPE");
				int r_price = rs.getInt("R_PRICE");
				String r_status = rs.getString("R_STATUS");
				
				RoomDto dto = new RoomDto(r_room_no, r_type, r_floor, r_scale,
										  r_people_standard, r_people_max, r_bed_type,
										  r_price, r_status);
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}
		
		return list;
	}
	
	// 3. 객실 상세 정보 조회
	public RoomDto getRoomView(String room_no) {
		RoomDto dto = null;
		
		String sql = "SELECT * FROM JSL_HOTEL_ROOM WHERE R_ROOM_NO = ?";
		
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, room_no);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				String r_room_no = rs.getString("R_ROOM_NO");
				String r_type = rs.getString("R_TYPE");
				int r_floor = rs.getInt("R_FLOOR");
				String r_scale = rs.getString("R_SCALE");
				int r_people_standard = rs.getInt("R_PEOPLE_STANDARD");
				int r_people_max = rs.getInt("R_PEOPLE_MAX");
				int r_extra_person_fee = rs.getInt("R_EXTRA_PERSON_FEE");
				int r_bed = rs.getInt("R_BED");
				String r_bed_type = rs.getString("R_BED_TYPE");
				String r_wifi = rs.getString("R_WIFI");
				String r_spa = rs.getString("R_SPA");
				String r_smoking = rs.getString("R_SMOKING");
				String r_view = rs.getString("R_VIEW");
				String r_balcony = rs.getString("R_BALCONY");
				int r_bathroom = rs.getInt("R_BATHROOM");
				String r_kitchen = rs.getString("R_KITCHEN");
				int r_price = rs.getInt("R_PRICE");
				String r_status = rs.getString("R_STATUS");
				String r_clean_status = rs.getString("R_CLEAN_STATUS");
				String r_register_date = rs.getString("R_REGISTER_DATE");
				String r_last_update = rs.getString("R_LAST_UPDATE");
				String r_description = rs.getString("R_DESCRIPTION");
				
				dto = new RoomDto(r_room_no, r_type, r_floor, r_scale,
								  r_people_standard, r_people_max, r_extra_person_fee,
								  r_bed, r_bed_type, r_wifi, r_spa, r_smoking,
								  r_view, r_balcony, r_bathroom, r_kitchen,
								  r_price, r_status, r_clean_status,
								  r_register_date, r_last_update, r_description);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}
		
		return dto;
	}
	
	// 4. 객실 상태 변경
	public int updateRoomStatus(String room_no, String status, String clean_status) {
		int result = 0;
		
		String sql = "UPDATE JSL_HOTEL_ROOM " +
					 "SET R_STATUS = ?, R_CLEAN_STATUS = ?, R_LAST_UPDATE = SYSDATE " +
					 "WHERE R_ROOM_NO = ?";
		
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, status);
			ps.setString(2, clean_status);
			ps.setString(3, room_no);
			result = ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}
		
		return result;
	}
	
	// 5. 객실 타입별 개수 조회
	public int getRoomCountByType(String type) {
		int count = 0;
		
		String sql = "SELECT COUNT(*) FROM JSL_HOTEL_ROOM WHERE R_TYPE = ?";
		
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, type);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				count = rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}
		
		return count;
	}
	
	// 6. 예약 가능한 객실 조회 (날짜 기반)
	public List<RoomDto> getAvailableRooms(String checkIn, String checkOut) {
		List<RoomDto> list = new ArrayList<>();
		
		String sql = "SELECT R_ROOM_NO, R_TYPE, R_FLOOR, R_SCALE, " +
					 "R_PEOPLE_STANDARD, R_PEOPLE_MAX, R_BED_TYPE, " +
					 "R_PRICE, R_STATUS " +
					 "FROM JSL_HOTEL_ROOM " +
					 "WHERE R_STATUS = 'AVAILABLE' " +
					 "AND R_ROOM_NO NOT IN ( " +
					 "    SELECT RV_ROOM_NO FROM JSL_HOTEL_RESERVATION " +
					 "    WHERE RV_STATUS IN ('CONFIRMED', 'CHECKED_IN') " +
					 "    AND (TO_DATE(?, 'YYYY-MM-DD') < RV_CHECK_OUT_DATE " +
					 "    AND TO_DATE(?, 'YYYY-MM-DD') > RV_CHECK_IN_DATE) " +
					 ") " +
					 "ORDER BY R_FLOOR, R_ROOM_NO";
		
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, checkIn);
			ps.setString(2, checkOut);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				String r_room_no = rs.getString("R_ROOM_NO");
				String r_type = rs.getString("R_TYPE");
				int r_floor = rs.getInt("R_FLOOR");
				String r_scale = rs.getString("R_SCALE");
				int r_people_standard = rs.getInt("R_PEOPLE_STANDARD");
				int r_people_max = rs.getInt("R_PEOPLE_MAX");
				String r_bed_type = rs.getString("R_BED_TYPE");
				int r_price = rs.getInt("R_PRICE");
				String r_status = rs.getString("R_STATUS");
				
				RoomDto dto = new RoomDto(r_room_no, r_type, r_floor, r_scale,
										  r_people_standard, r_people_max, r_bed_type,
										  r_price, r_status);
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}
		
		return list;
	}
	
	/**
	 * ✅ 예약 가능한 객실 조회 (흡연 필터 추가)
	 * 
	 * @param checkIn 체크인 날짜 (YYYY-MM-DD)
	 * @param checkOut 체크아웃 날짜 (YYYY-MM-DD)
	 * @param smoking 흡연 가능 여부 (Y/N/null-상관없음)
	 * @return 예약 가능한 객실 목록
	 */
	public List<RoomDto> getAvailableRoomsWithSmoking(String checkIn, String checkOut, String smoking) {
		List<RoomDto> list = new ArrayList<>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT R_ROOM_NO, R_TYPE, R_FLOOR, R_SCALE, ");
		sql.append("       R_PEOPLE_STANDARD, R_PEOPLE_MAX, R_EXTRA_PERSON_FEE, ");
		sql.append("       R_BED_TYPE, R_PRICE, R_STATUS, R_SMOKING ");
		sql.append("FROM JSL_HOTEL_ROOM ");
		sql.append("WHERE R_STATUS = 'AVAILABLE' ");
		sql.append("AND R_ROOM_NO NOT IN ( ");
		sql.append("    SELECT RV_ROOM_NO FROM JSL_HOTEL_RESERVATION ");
		sql.append("    WHERE RV_STATUS IN ('CONFIRMED', 'CHECKED_IN') ");
		sql.append("    AND (TO_DATE(?, 'YYYY-MM-DD') < RV_CHECK_OUT_DATE ");
		sql.append("    AND TO_DATE(?, 'YYYY-MM-DD') > RV_CHECK_IN_DATE) ");
		sql.append(") ");
		
		if (smoking != null && !smoking.isEmpty()) {
			sql.append("AND R_SMOKING = ? ");
		}
		
		sql.append("ORDER BY R_FLOOR, R_ROOM_NO");
		
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql.toString());
			
			ps.setString(1, checkIn);
			ps.setString(2, checkOut);
			
			if (smoking != null && !smoking.isEmpty()) {
				ps.setString(3, smoking);
			}
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				String r_room_no = rs.getString("R_ROOM_NO");
				String r_type = rs.getString("R_TYPE");
				int r_floor = rs.getInt("R_FLOOR");
				String r_scale = rs.getString("R_SCALE");
				int r_people_standard = rs.getInt("R_PEOPLE_STANDARD");
				int r_people_max = rs.getInt("R_PEOPLE_MAX");
				String r_bed_type = rs.getString("R_BED_TYPE");
				int r_price = rs.getInt("R_PRICE");
				String r_status = rs.getString("R_STATUS");
				
				RoomDto dto = new RoomDto(r_room_no, r_type, r_floor, r_scale,
										  r_people_standard, r_people_max, r_bed_type,
										  r_price, r_status);
				
				dto.setR_extra_person_fee(rs.getInt("R_EXTRA_PERSON_FEE"));
				dto.setR_smoking(rs.getString("R_SMOKING"));
				
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}
		
		return list;
	}
	
	// 7. 객실 정보 수정 (운영 정보만)
	public int updateRoomInfo(String room_no, String extra_person_fee, String price, 
							  String bed_type, String bed, String smoking, String bathroom,
							  String kitchen, String wifi, String spa, String view, 
							  String balcony, String status, String clean_status, String description) {
		int result = 0;
		
		String sql = "UPDATE JSL_HOTEL_ROOM SET " +
					 "R_EXTRA_PERSON_FEE = ?, " +
					 "R_PRICE = ?, " +
					 "R_BED_TYPE = ?, " +
					 "R_BED = ?, " +
					 "R_SMOKING = ?, " +
					 "R_BATHROOM = ?, " +
					 "R_KITCHEN = ?, " +
					 "R_WIFI = ?, " +
					 "R_SPA = ?, " +
					 "R_VIEW = ?, " +
					 "R_BALCONY = ?, " +
					 "R_STATUS = ?, " +
					 "R_CLEAN_STATUS = ?, " +
					 "R_DESCRIPTION = ?, " +
					 "R_LAST_UPDATE = SYSDATE " +
					 "WHERE R_ROOM_NO = ?";
		
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(extra_person_fee));
			ps.setInt(2, Integer.parseInt(price));
			ps.setString(3, bed_type);
			ps.setInt(4, Integer.parseInt(bed));
			ps.setString(5, smoking);
			ps.setInt(6, Integer.parseInt(bathroom));
			ps.setString(7, kitchen);
			ps.setString(8, wifi);
			ps.setString(9, spa);
			ps.setString(10, view);
			ps.setString(11, balcony);
			ps.setString(12, status);
			ps.setString(13, clean_status);
			ps.setString(14, description);
			ps.setString(15, room_no);
			
			result = ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}
		
		return result;
	}
	
	/**
	 * 객실 번호로 객실 정보 조회
	 */
	public RoomDto getRoomByNo(String roomNo) {
		RoomDto dto = null;
		
		String sql = "SELECT R_ROOM_NO, R_TYPE, R_FLOOR, R_SCALE, " +
					"       R_PEOPLE_STANDARD, R_PEOPLE_MAX, R_EXTRA_PERSON_FEE, " +
					"       R_BED, R_BED_TYPE, R_WIFI, R_SPA, R_SMOKING, " +
					"       R_VIEW, R_BALCONY, R_BATHROOM, R_KITCHEN, " +
					"       R_PRICE, R_STATUS, R_CLEAN_STATUS, " +
					"       R_REGISTER_DATE, R_LAST_UPDATE, R_DESCRIPTION " +
					"FROM JSL_HOTEL_ROOM " +
					"WHERE R_ROOM_NO = ?";
		
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, roomNo);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				dto = new RoomDto(
					rs.getString("R_ROOM_NO"),
					rs.getString("R_TYPE"),
					rs.getInt("R_FLOOR"),
					rs.getString("R_SCALE"),
					rs.getInt("R_PEOPLE_STANDARD"),
					rs.getInt("R_PEOPLE_MAX"),
					rs.getInt("R_EXTRA_PERSON_FEE"),
					rs.getInt("R_BED"),
					rs.getString("R_BED_TYPE"),
					rs.getString("R_WIFI"),
					rs.getString("R_SPA"),
					rs.getString("R_SMOKING"),
					rs.getString("R_VIEW"),
					rs.getString("R_BALCONY"),
					rs.getInt("R_BATHROOM"),
					rs.getString("R_KITCHEN"),
					rs.getInt("R_PRICE"),
					rs.getString("R_STATUS"),
					rs.getString("R_CLEAN_STATUS"),
					rs.getString("R_REGISTER_DATE"),
					rs.getString("R_LAST_UPDATE"),
					rs.getString("R_DESCRIPTION")
				);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getRoomByNo() 오류");
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}
		
		return dto;
	}
}