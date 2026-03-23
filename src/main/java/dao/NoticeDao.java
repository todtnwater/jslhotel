package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;

import common.DBConnection;
import dto.NoticeDto;

public class NoticeDao {
	Connection		  con = null;
	PreparedStatement ps  = null;
	ResultSet 		  rs  = null;
	
	//인덱스 팝업
	public NoticeDto getActivePopupNotice() {
	    NoticeDto dto = null;
	    String sql = "SELECT n_no, n_title, n_content, n_attach, n_position, n_type, " +
	                 "       to_char(n_popupStartDate, 'yyyy-MM-dd') as start_date, " +
	                 "       to_char(n_popupEndDate, 'yyyy-MM-dd') as end_date, " +
	                 "       to_char(n_reg_date, 'yyyy-MM-dd hh24:mi:ss') as reg_date, n_hit " +
	                 "FROM jsl_hotel_notice " +
	                 "WHERE n_type = 'popup' " +
	                 "  AND TRUNC(SYSDATE) >= n_popupStartDate " +
	                 "  AND TRUNC(SYSDATE) <= n_popupEndDate " +
	                 "ORDER BY n_no DESC " +
	                 "FETCH FIRST 1 ROW ONLY";  // 가장 최신 팝업만 가져오기
	    
	    try {
	        con = DBConnection.getConnection();
	        ps = con.prepareStatement(sql);
	        rs = ps.executeQuery();
	        
	        if(rs.next()) {
	            String n_no = rs.getString("n_no");
	            String title = rs.getString("n_title");
	            String attach = rs.getString("n_attach");
	            String content = rs.getString("n_content");
	            String position = rs.getString("n_position");
	            String reg_date = rs.getString("reg_date");
	            String type = rs.getString("n_type");
	            int hit = rs.getInt("n_hit");
	            String popupStartDate = rs.getString("start_date");
	            String popupEndDate = rs.getString("end_date");
	            
	            dto = new NoticeDto(n_no, title, attach, content, position, 
	                              reg_date, type, hit, popupStartDate, popupEndDate);
	        }
	    } catch(Exception e) {
	        e.printStackTrace();
	        System.out.println("getActivePopupNotice() 오류 : " + sql);
	    } finally {
	        DBConnection.closeDB(con, ps, rs);
	    }
	    
	    return dto;
	}

 
	// 공지 목록(페이징)
	public ArrayList<NoticeDto> getNoticeList(String select, String search, int start, int end) {
		ArrayList<NoticeDto> dtos = new ArrayList<NoticeDto>();

		// ⚠️ Column Name Injection 방지: 허용된 컬럼만 사용
		String allowedColumn = "n_title"; // 기본값
		if ("n_title".equals(select) || "title".equals(select)) {
			allowedColumn = "n_title";
		} else if ("n_content".equals(select) || "content".equals(select)) {
			allowedColumn = "n_content";
		} else if ("n_type".equals(select) || "type".equals(select)) {
			allowedColumn = "n_type";
		}

		String sql = "select * from(\r\n"
				   + "              select rownum as rnum,tbl.*\r\n"
				   + "              from(\r\n"
				   + "              select n_no, n_type, n_title, n_content, n_position,\r\n"
				   + "                     to_char(n_reg_date, 'yyyy-MM-dd') as reg_date, n_hit\r\n"
				   + "              from jsl_hotel_notice\r\n"
				   + "              where " + allowedColumn + " like ?\r\n"
				   + "              order by\r\n"
				   + "                case n_type\r\n"
				   + "                    when 'urgent' then 1\r\n"
				   + "                    when 'popup' then 2\r\n"
				   + "                    when 'notice' then 3\r\n"
				   + "                    else 4\r\n"
				   + "                end asc,\r\n"
				   + "                n_no desc\r\n"
				   + "              ) tbl) \r\n"
				   + "              where rnum >= ? and rnum <= ?";
		try {
			con = DBConnection.getConnection();
			ps	= con.prepareStatement(sql);
			ps.setString(1, "%" + search + "%");
			ps.setInt(2, start);
			ps.setInt(3, end);
			rs  = ps.executeQuery();
			while(rs.next()) {
				String no 	    = rs.getString("n_no");
				String type 	= rs.getString("n_type");
				String title    = rs.getString("n_title");
				String content   = rs.getString("n_content");
				String position = rs.getString("n_position");
				String reg_date = rs.getString("reg_date");
				int hit = rs.getInt("n_hit");

				NoticeDto dto = new NoticeDto(no, title, content, position, reg_date, type, hit);
				dtos.add(dto);
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("getNoticeList() 오류 : "+sql);
		}finally {
			DBConnection.closeDB(con, ps, rs);
		}
		return dtos;
	}
	
	// 관리자 직책
	public String getPosition(String id) {
		String position = "";
		String sql = "select m_position from jsl_hotel_member\r\n"
				   + "where m_id = '"+id+"' ";
		try {
			con = DBConnection.getConnection();
			ps	= con.prepareStatement(sql);
			rs  = ps.executeQuery();
			if(rs.next()) {
				position = rs.getString("m_position");
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("getPosition() 오류 : "+sql);
		}finally {
			DBConnection.closeDB(con, ps, rs);
		}		
		return position;
	}
	// 공지 번호
	public String getNo() {
		String no = "";
		String sql = "select nvl(max(n_no), 'N000') as n_no\r\n"
				   + "from jsl_hotel_notice ";
		try {
			con = DBConnection.getConnection();
			ps	= con.prepareStatement(sql);
			rs  = ps.executeQuery();
			if(rs.next()) {
				no = rs.getString("n_no"); // N001
				no = no.substring(1); // 001
				int noticeNo = Integer.parseInt(no) + 1; // 001 + 1
				DecimalFormat df = new DecimalFormat("N000");
				no = df.format(noticeNo); // N002
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("getNo() 오류 : "+sql);
		}finally {
			DBConnection.closeDB(con, ps, rs);
		}	
		return no;
	}
	
	// 공지 등록
	public int noticeSave(NoticeDto dto) {
	    int result = 0;
	    
	    String startDate = dto.getPopupStartDate();
	    String endDate = dto.getPopupEndDate();
	    
	    if (!dto.getN_type().equals("popup") || startDate == null || startDate.isEmpty()) {
	        startDate = "NULL";
	        endDate = "NULL";
	    } else {
	        startDate = "to_date('" + startDate + "', 'yyyy-MM-dd')";
	        endDate = "to_date('" + endDate + "', 'yyyy-MM-dd')";
	    }
	    
	    String sql = "insert into jsl_hotel_notice\r\n"
	               + "(n_no, n_title, n_content, n_attach, n_reg_id, n_position, n_reg_date, n_type, n_popupstartdate, n_popupenddate)\r\n" 
	               + "values\r\n"
	               + "('"+dto.getN_no()+"', '"+dto.getN_title()+"', '"+dto.getN_content()+"', '"+dto.getN_attach()+"','"+dto.getN_reg_id()+"', '"+dto.getN_position()+"', \r\n"
	               + "to_date('"+dto.getN_reg_date()+"', 'yyyy-MM-dd hh24:mi:ss'), '"+dto.getN_type()+"'"
	               + ", " + startDate + ", " 
	               + endDate   + ")"; 
	               
	    try {
	        con     = DBConnection.getConnection();
	        ps      = con.prepareStatement(sql);
	        result  = ps.executeUpdate();
	    }catch(Exception e) {
	        e.printStackTrace();
	        System.out.println("noticeSave() 오류 : "+sql);
	    }finally {
	        DBConnection.closeDB(con, ps, rs);
	    }       
	    return result;
	}
	
	// 공지 목록 건수
	public int getTotalCount(String select, String search){
		int count = 0;
		String sql = "select count(*) as count\r\n"
				   + "from jsl_hotel_notice \r\n"
				   + "where "+select+" like '%"+search+"%'" ;
		try {
			con = DBConnection.getConnection();
			ps	= con.prepareStatement(sql);
			rs  = ps.executeQuery();
			if(rs.next()) {
				count = rs.getInt("count");
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("getTotalCount() 오류 : "+sql);
		}finally {
			DBConnection.closeDB(con, ps, rs);
		}		
		return count;
	}
	
	// 팝업 공지 등록 시 기존 팝업 공지를 일반 공지로 전환
	public int noticeChange() {
		int result = 0;
		String sql = "update jsl_hotel_notice\r\n"
				   + "    set\r\n"
				   + "    n_type = 'notice'\r\n"
				   + "    where n_type = 'popup'";
		try {
			con 	= DBConnection.getConnection();
			ps		= con.prepareStatement(sql);
			result  = ps.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("noticeChange() 오류 : "+sql);
		}finally {
			DBConnection.closeDB(con, ps, rs);
		}		
		return result;
	}
	
	// 공지 조회수
	public int setHitCount(String no) {
		int result = 0;
		String sql = "update jsl_hotel_notice\r\n"
				   + "    set n_hit = n_hit + 1\r\n"
				   + "    where n_no = '"+no+"'";
		try {
			con 	= DBConnection.getConnection();
			ps		= con.prepareStatement(sql);
			result  = ps.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("setHitCount() 오류 : "+sql);
		}finally {
			DBConnection.closeDB(con, ps, rs);
		}		
		return result;
	}
	
	// 공지 상세 보기
	public NoticeDto getNoticeView(String no) {
	    NoticeDto dto = null;
	    String sql = "select n_no, n_title, n_content, n_attach, n_position, n_type,\r\n"
	               + "to_char(n_popupStartDate, 'yyyy-MM-dd') as start_date,\r\n" 
	               + "to_char(n_popupEndDate, 'yyyy-MM-dd') as end_date,\r\n"
	               + "to_char(n_reg_date, 'yyyy-MM-dd hh24:mi:ss') as reg_date, n_hit\r\n"
	               + "from jsl_hotel_notice \r\n"
	               + "where n_no='"+no+"' ";
	    try {
	        con = DBConnection.getConnection();
	        ps	= con.prepareStatement(sql);
	        rs  = ps.executeQuery();
	        if(rs.next()) {
	            String n_no 	= rs.getString("n_no");
	            String title    = rs.getString("n_title");
	            String attach 	= rs.getString("n_attach");
	            String content  = rs.getString("n_content");
	            String position = rs.getString("n_position");
	            String reg_date = rs.getString("reg_date");
	            String type 	= rs.getString("n_type");
	            int hit = rs.getInt("n_hit");
	            String popupStartDate = rs.getString("start_date");
	            String popupEndDate   = rs.getString("end_date");

	            dto = new NoticeDto(n_no, title, attach, content, position, reg_date, type, hit, popupStartDate, popupEndDate);
	        }
	    }catch(Exception e) {
	        e.printStackTrace();
	        System.out.println("getNoticeView() 오류 : "+sql);
	    }finally {
	        DBConnection.closeDB(con, ps, rs);
	    }
	    return dto;
	}

	// 공지 사항 수정 저장
	public int noticeUpdate(NoticeDto dto) {
	    int result = 0;
	    String startDate = dto.getPopupStartDate();
	    String endDate = dto.getPopupEndDate();
	    
	    String popupDateSql;
	    
	    // 팝업일 경우 날짜를 to_date() 형태로
	    if (dto.getN_type().equals("popup") && startDate != null && !startDate.isEmpty()) {
	        popupDateSql = "n_popupstartdate = to_date('" + startDate + "', 'yyyy-MM-dd'), n_popupenddate = to_date('" + endDate + "', 'yyyy-MM-dd')";
	    } else {
	        popupDateSql = "n_popupstartdate = null, n_popupenddate = null";
	    }
	    
	    String sql = "update jsl_hotel_notice set "
	               + "n_title = '" + dto.getN_title() + "', "
	               + "n_content = '" + dto.getN_content() + "', "
	               + "n_attach = '" + dto.getN_attach() + "', " 
	               + "n_type = '" + dto.getN_type() + "', "
	               + popupDateSql + ", " 
	               + "n_reg_date = to_date('" + dto.getN_reg_date() + "', 'yyyy-MM-dd hh24:mi:ss') "
	               + "where n_no = '" + dto.getN_no() + "'";  
	    try {
	        con 	= DBConnection.getConnection();
	        ps		= con.prepareStatement(sql);
	        result  = ps.executeUpdate();
	    }catch(Exception e) {
	        e.printStackTrace();
	        System.out.println("noticeUpdate() 오류: " + sql);
	    }finally {
	        DBConnection.closeDB(con, ps, rs);
	    }		
	    return result;
	}
	
	// 공지 삭제
	public int noticeDelete(String no) {
		int result = 0;
		String sql = "delete from jsl_hotel_notice\r\n"
				   + "where n_no = '"+no+"'";
		
		try {
			con 	= DBConnection.getConnection();
			ps		= con.prepareStatement(sql);
			result  = ps.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("noticeDelete() 오류"+sql);
		}finally {
			DBConnection.closeDB(con, ps, rs);
			System.out.println(sql);
		}		
		return result;
	}	
}