package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.DBConnection;
import dto.RoomImageDto;

public class RoomImageDao {
	
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	// 다음 이미지 번호 생성 (1부터 시작)
	public String getNextImageNo() {
		String imgNo = "RI0001"; // 기본값
		String sql = "SELECT COUNT(*) + 1 AS NEXT_NO " +
					 "FROM JSL_HOTEL_ROOM_IMAGE";
		
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				int nextNo = rs.getInt("NEXT_NO");
				imgNo = String.format("RI%04d", nextNo);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getNextImageNo() 오류: " + e.getMessage());
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}
		
		return imgNo;
	}
	
	// 1. 특정 객실의 이미지 목록 조회
	public List<RoomImageDto> getRoomImages(String roomNo) {
		List<RoomImageDto> list = new ArrayList<>();
		
		String sql = "SELECT RI_IMG_NO, RI_ROOM_NO, RI_IMG_PATH, RI_IMG_NAME, " +
					 "RI_IS_MAIN, RI_DISPLAY_ORDER " +
					 "FROM JSL_HOTEL_ROOM_IMAGE " +
					 "WHERE RI_ROOM_NO = ? AND RI_STATUS = 'ACTIVE' " +
					 "ORDER BY RI_DISPLAY_ORDER";
		
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, roomNo);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				String ri_img_no = rs.getString("RI_IMG_NO");
				String ri_room_no = rs.getString("RI_ROOM_NO");
				String ri_img_path = rs.getString("RI_IMG_PATH");
				String ri_img_name = rs.getString("RI_IMG_NAME");
				String ri_is_main = rs.getString("RI_IS_MAIN");
				int ri_display_order = rs.getInt("RI_DISPLAY_ORDER");
				
				RoomImageDto dto = new RoomImageDto(ri_img_no, ri_room_no, ri_img_path, 
													ri_img_name, ri_is_main, ri_display_order);
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}
		
		return list;
	}
	
	// ⭐ NEW: 모든 객실의 메인 이미지 한 번에 조회 (성능 최적화)
	public Map<String, String> getAllMainImages() {
		Map<String, String> mainImages = new HashMap<>();
		
		String sql = "SELECT RI_ROOM_NO, RI_IMG_PATH " +
					 "FROM JSL_HOTEL_ROOM_IMAGE " +
					 "WHERE RI_IS_MAIN = 'Y' AND RI_STATUS = 'ACTIVE' " +
					 "ORDER BY RI_ROOM_NO";
		
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				String roomNo = rs.getString("RI_ROOM_NO");
				String imgPath = rs.getString("RI_IMG_PATH");
				mainImages.put(roomNo, imgPath);
			}
			
			System.out.println(">>> 전체 메인 이미지 조회 완료: " + mainImages.size() + "개");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}
		
		return mainImages;
	}
	
	// 2. 이미지 업로드 (삽입)
	public int insertRoomImage(RoomImageDto dto) {
		int result = 0;
		
		String sql = "INSERT INTO JSL_HOTEL_ROOM_IMAGE " +
					 "(RI_IMG_NO, RI_ROOM_NO, RI_IMG_PATH, RI_IMG_NAME, " +
					 "RI_IMG_TYPE, RI_IMG_SIZE, RI_IS_MAIN, RI_DISPLAY_ORDER) " +
					 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, dto.getRi_img_no());
			ps.setString(2, dto.getRi_room_no());
			ps.setString(3, dto.getRi_img_path());
			ps.setString(4, dto.getRi_img_name());
			ps.setString(5, dto.getRi_img_type());
			ps.setInt(6, dto.getRi_img_size());
			ps.setString(7, dto.getRi_is_main());
			ps.setInt(8, dto.getRi_display_order());
			result = ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}
		
		return result;
	}
	
	// 3. 이미지 삭제 (논리적 삭제)
	public int deleteRoomImage(String imgNo) {
		int result = 0;
		
		String sql = "UPDATE JSL_HOTEL_ROOM_IMAGE " +
					 "SET RI_STATUS = 'DELETED' " +
					 "WHERE RI_IMG_NO = ?";
		
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, imgNo);
			result = ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}
		
		return result;
	}
	
	// 4. 메인 이미지 설정
	public int setMainImage(String roomNo, String imgNo) {
		int result = 0;
		
		try {
			con = DBConnection.getConnection();
			con.setAutoCommit(false);
			
			// 1. 기존 메인 이미지 해제
			String sql1 = "UPDATE JSL_HOTEL_ROOM_IMAGE " +
						  "SET RI_IS_MAIN = 'N' " +
						  "WHERE RI_ROOM_NO = ?";
			ps = con.prepareStatement(sql1);
			ps.setString(1, roomNo);
			ps.executeUpdate();
			ps.close();
			
			// 2. 새 메인 이미지 설정
			String sql2 = "UPDATE JSL_HOTEL_ROOM_IMAGE " +
						  "SET RI_IS_MAIN = 'Y' " +
						  "WHERE RI_IMG_NO = ?";
			ps = con.prepareStatement(sql2);
			ps.setString(1, imgNo);
			result = ps.executeUpdate();
			
			con.commit();
			
		} catch (Exception e) {
			try {
				if(con != null) con.rollback();
			} catch (SQLException se) {
				se.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if(con != null) con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DBConnection.closeDB(con, ps, rs);
		}
		
		return result;
	}
	
	// 5. 다음 표시 순서 번호 가져오기 (ACTIVE만 카운트)
	public int getNextDisplayOrder(String roomNo) {
		int order = 1;
		
		String sql = "SELECT NVL(MAX(RI_DISPLAY_ORDER), 0) + 1 AS NEXT_ORDER " +
					 "FROM JSL_HOTEL_ROOM_IMAGE " +
					 "WHERE RI_ROOM_NO = ? AND RI_STATUS = 'ACTIVE'";
		
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, roomNo);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				order = rs.getInt("NEXT_ORDER");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}
		
		return order;
	}
	
	// 6. 메인 이미지 조회
	public RoomImageDto getMainImage(String roomNo) {
		RoomImageDto dto = null;
		
		String sql = "SELECT RI_IMG_NO, RI_ROOM_NO, RI_IMG_PATH, RI_IMG_NAME, " +
					 "RI_IS_MAIN, RI_DISPLAY_ORDER " +
					 "FROM JSL_HOTEL_ROOM_IMAGE " +
					 "WHERE RI_ROOM_NO = ? AND RI_IS_MAIN = 'Y' AND RI_STATUS = 'ACTIVE'";
		
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, roomNo);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				String ri_img_no = rs.getString("RI_IMG_NO");
				String ri_room_no = rs.getString("RI_ROOM_NO");
				String ri_img_path = rs.getString("RI_IMG_PATH");
				String ri_img_name = rs.getString("RI_IMG_NAME");
				String ri_is_main = rs.getString("RI_IS_MAIN");
				int ri_display_order = rs.getInt("RI_DISPLAY_ORDER");
				
				dto = new RoomImageDto(ri_img_no, ri_room_no, ri_img_path, 
									   ri_img_name, ri_is_main, ri_display_order);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}
		
		return dto;
	}
}