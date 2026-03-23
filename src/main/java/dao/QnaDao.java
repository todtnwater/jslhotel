package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import common.DBConnection;
import dto.QnaDto;

public class QnaDao {
	
	Connection		  con = null;
	PreparedStatement ps  = null;
	ResultSet 		  rs  = null;
	
	
	// Qna 번호
	public String getQnaNo() {
		String no = "";
		String sql = "select nvl(max(q_no), 'Q00000') as q_no\r\n"
				   + "from jsl_hotel_qna ";
		try {
			con = DBConnection.getConnection();
			ps	= con.prepareStatement(sql);
			rs  = ps.executeQuery();
			if(rs.next()) {
				no = rs.getString("q_no"); // N001
				no = no.substring(1); // 001
				int qnaNo = Integer.parseInt(no) + 1; // 001 + 1
				DecimalFormat df = new DecimalFormat("Q00000");
				no = df.format(qnaNo); // N002
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("getQnaNo() 오류 : "+sql);
		}finally {
			DBConnection.closeDB(con, ps, rs);
		}	
		return no;
	}

	// Qna 등록
	public int qnaSave(QnaDto dto) {
		int result = 0;
		String sql = "insert into jsl_hotel_qna\r\n"
				+ "(q_no, q_title, q_content, q_type, q_member, q_reg_writer, q_password, q_reg_date)\r\n"
				+ "values\r\n"
				+ "(?, ?, ?, ?, ?, ?, ?, to_date(?, 'yyyy-MM-dd hh24:mi:ss'))";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, dto.getQ_no());
			ps.setString(2, dto.getQ_title());
			ps.setString(3, dto.getQ_content());
			ps.setString(4, dto.getQ_type());
			ps.setString(5, dto.getQ_member());
			ps.setString(6, dto.getQ_reg_writer());
			ps.setString(7, dto.getQ_password());
			ps.setString(8, dto.getQ_reg_date());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("qnaSave() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return result;
	}

	// Qna 목록
	public List<QnaDto> getQnaList(String select, String type, String search, int start, int end) {
		List<QnaDto> dtos = new ArrayList<QnaDto>();

		// ⚠️ Column Name Injection 방지: 허용된 컬럼만 사용
		String allowedColumn = "title"; // 기본값
		if ("title".equals(select)) {
			allowedColumn = "title";
		} else if ("content".equals(select)) {
			allowedColumn = "content";
		} else if ("type".equals(select)) {
			allowedColumn = "type";
		} else if ("member".equals(select)) {
			allowedColumn = "member";
		}

		String sql = "select * "
				+ "from( "
				+ "		select rownum as rnum, tbl.* "
				+ "		from (select q_no, q_title, q_content, q_type, q_member,  \r\n"
				+ "        		q_reg_writer, q_password, a_content,  \r\n"
				+ "       		to_char(q_reg_date, 'yyyy-MM-dd') as q_reg_date \r\n"
				+ "			from jsl_hotel_qna \r\n "
				+ "			where q_" + allowedColumn + " like ? \r\n";

		if(!type.equals("all")) {
	    	sql += "and q_type = ? \r\n";
	    }

		sql += "order by q_no desc \r\n) tbl) "
	    		+ "where rnum >= ? and rnum <= ?";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);

			int paramIndex = 1;
			ps.setString(paramIndex++, "%" + search + "%");
			if(!type.equals("all")) {
				ps.setString(paramIndex++, type);
			}
			ps.setInt(paramIndex++, start);
			ps.setInt(paramIndex++, end);

			rs  = ps.executeQuery();

			while(rs.next()) {
				String q_no = rs.getString("q_no");
				String q_title = rs.getString("q_title");
				String q_content = rs.getString("q_content");
				String q_type = rs.getString("q_type");
				String q_member = rs.getString("q_member");
				String q_reg_writer = rs.getString("q_reg_writer");
				String q_password = rs.getString("q_password");
				String q_reg_date = rs.getString("q_reg_date");
				String a_content = rs.getString("a_content");

				QnaDto dto = new QnaDto(q_no, q_title, q_content, q_type, q_member, q_reg_writer, q_password, q_reg_date, a_content);
				dtos.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getQnaList() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return dtos;
	}
	
	// Qna 상세보기
	public QnaDto getQnaView(String q_no) {
		QnaDto dto = null;
		String sql = "select q_no, q_title, q_content, q_type, q_member, "
					+ "q_reg_writer, q_password, "
					+ "to_char(q_reg_date, 'yyyy-MM-dd') as q_reg_date, \r\n"
					+ "to_char(q_update_date, 'yyyy-MM-dd') as q_update_date, \r\n"
					+ "a_content, a_id, a_position, "
					+ "to_char(a_reg_date, 'yyyy-MM-dd') as a_reg_date, \r\n"
					+ "to_char(a_update_date, 'yyyy-MM-dd') as a_update_date \r\n"
					+ "from jsl_hotel_qna "
					+ "where q_no = ?";


		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, q_no);
			rs  = ps.executeQuery();

			while(rs.next()) {
				String q_title = rs.getString("q_title");
				String q_content = rs.getString("q_content");
				String q_type = rs.getString("q_type");
				String q_member = rs.getString("q_member");
				String q_reg_writer = rs.getString("q_reg_writer");
				String q_password = rs.getString("q_password");
				String q_reg_date = rs.getString("q_reg_date");
				String q_update_date = rs.getString("q_update_date");
				String a_content = rs.getString("a_content");
				String a_id = rs.getString("a_id");
				String a_position = rs.getString("a_position");
				String a_reg_date = rs.getString("a_reg_date");
				String a_update_date = rs.getString("a_update_date");

				dto = new QnaDto(q_no, q_title, q_content, q_type, q_member,
							q_reg_writer, q_password, q_reg_date, q_update_date,
							a_content, a_id, a_position, a_reg_date, a_update_date);

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getQnaView() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return dto;
	}

	
	// QnA 삭제 [회원 : 답변 없을 경우만 / 관리자는 삭제 가능]
	public int qnaDelete(String q_no) {
		int result = 0;
		String sql = "delete from jsl_hotel_qna "
				+ "where q_no = ?";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, q_no);
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("qnaDelete() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return result;
	}

	// QnA 내용 수정
	public int qnaUpdate(String q_no, String q_title, String q_content, String q_update_date) {
		int result = 0;
		String sql = "update jsl_hotel_qna "
				+ "set "
				+ "	q_title = ?, \r\n"
				+ " q_content = ?, \r\n"
				+ " q_update_date = to_date(?, 'yyyy-MM-dd hh24:mi:ss') "
				+ "where q_no = ?";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, q_title);
			ps.setString(2, q_content);
			ps.setString(3, q_update_date);
			ps.setString(4, q_no);
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("qnaUpdate() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return result;
	}

	// QnA 답변 등록 및 수정
	public int qnaAnswer(String q_no, String a_content, String a_id, String a_position, String a_update_date) {
		int result = 0;
		String sql = "update jsl_hotel_qna "
				+ "set "
				+ "	a_id = ?, \r\n"
				+ " a_content = ?, \r\n"
				+ " a_position = ?, \r\n"
				+ " a_update_date = to_date(?, 'yyyy-MM-dd hh24:mi:ss') "
				+ "where q_no = ?";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, a_id);
			ps.setString(2, a_content);
			ps.setString(3, a_position);
			ps.setString(4, a_update_date);
			ps.setString(5, q_no);
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("qnaAnswer() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return result;
	}

	// 총 Qna 수
	public int getQnaCount(String select, String type, String search) {
		int count = 0;

		// ⚠️ Column Name Injection 방지: 허용된 컬럼만 사용
		String allowedColumn = "title"; // 기본값
		if ("title".equals(select)) {
			allowedColumn = "title";
		} else if ("content".equals(select)) {
			allowedColumn = "content";
		} else if ("type".equals(select)) {
			allowedColumn = "type";
		} else if ("member".equals(select)) {
			allowedColumn = "member";
		}

		String sql = "select count(*) as count\r\n"
				+ "	from jsl_hotel_qna\r\n "
				+ " where q_" + allowedColumn + " like ? \r\n";

	    if(!type.equals("all")) {
	    	sql += "and q_type like ?\r\n";
	    }


		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);

			int paramIndex = 1;
			ps.setString(paramIndex++, "%" + search + "%");
			if(!type.equals("all")) {
				ps.setString(paramIndex++, "%" + type + "%");
			}

			rs  = ps.executeQuery();

			if(rs.next()) {
				count = rs.getInt("count");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getQnaCount() 오류 : \r\n"+sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return count;
	}
	
	
	
	
}
