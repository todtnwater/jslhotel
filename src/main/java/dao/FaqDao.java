package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import common.DBConnection;
import dto.FaqDto;
import dto.QnaDto;

public class FaqDao {

	Connection		  con = null;
	PreparedStatement ps  = null;
	ResultSet 		  rs  = null;
	
	// Faq 번호
	public String getFaqNo() {
		String no = "";
		String sql = "select nvl(max(f_no), 'F000') as f_no\r\n"
				   + "from jsl_hotel_faq ";
		try {
			con = DBConnection.getConnection();
			ps	= con.prepareStatement(sql);
			rs  = ps.executeQuery();
			if(rs.next()) {
				no = rs.getString("f_no"); // N001
				no = no.substring(1); // 001
				int faqNo = Integer.parseInt(no) + 1; // 001 + 1
				DecimalFormat df = new DecimalFormat("F000");
				no = df.format(faqNo); // N002
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("getFaqNo() 오류 : "+sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}	
		return no;
	}
	
	// Faq 목록
	public List<FaqDto> getFaqList() {
		List<FaqDto> dtos = new ArrayList<FaqDto>();
		String sql = "select * from jsl_hotel_faq\r\n"
					+ "order by f_no desc";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			rs  = ps.executeQuery();
			
			while(rs.next()) {
				String f_no = rs.getString("f_no");
				String f_type = rs.getString("f_type");
				String f_question = rs.getString("f_question");
				String f_answer = rs.getString("f_answer");
				String f_reg_id = rs.getString("f_reg_id");
				String f_position = rs.getString("f_position");
				String f_reg_date = rs.getString("f_reg_date");
				String f_update_date = rs.getString("f_update_date");
			
				FaqDto dto = new FaqDto(f_no, f_type, f_question, f_answer, f_reg_id, f_position, f_reg_date, f_update_date);
				dtos.add(dto);
			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getFaqList() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}
		
		return dtos;
	}

	// FAQ 상세보기
	public FaqDto getFaqView(String no) {
		FaqDto dto = null;
		String sql = "select f_no, f_type, f_question, f_answer, f_reg_id, f_position, \r\n"
					+ " to_char(f_reg_date, 'yyyy-MM-dd') as f_reg_date, \r\n"
					+ " to_char(f_update_date, 'yyyy-MM-dd') as f_update_date \r\n"
					+ " from jsl_hotel_faq\r\n"
					+ "where f_no = ?";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, no);
			rs  = ps.executeQuery();

			while(rs.next()) {
				String f_no = rs.getString("f_no");
				String f_type = rs.getString("f_type");
				String f_question = rs.getString("f_question");
				String f_answer = rs.getString("f_answer");
				String f_reg_id = rs.getString("f_reg_id");
				String f_position = rs.getString("f_position");
				String f_reg_date = rs.getString("f_reg_date");
				String f_update_date = rs.getString("f_update_date");

				dto = new FaqDto(f_no, f_type, f_question, f_answer, f_reg_id, f_position, f_reg_date, f_update_date);

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getFaqView() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return dto;
	}

	// FAQ 삭제
	public int deleteFaq(String no) {
		int result = 0;
		String sql = "delete from jsl_hotel_faq \r\n"
				+ "where f_no = ?";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, no);
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("deleteFaq() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return result;
	}

	// FAQ 수정
	public int updateFaq(FaqDto dto) {
		int result = 0;
		String sql = "update jsl_hotel_faq\r\n"
				+ "set\r\n"
				+ "    f_type = ?, \r\n"
				+ "    f_question = ?, \r\n"
				+ "    f_answer = ?, \r\n"
				+ "    f_reg_id = ?, \r\n"
				+ "    f_position = ?,\r\n"
				+ "    f_update_date = to_date(?, 'yyyy-MM-dd hh24:mi:ss')\r\n"
				+ "where f_no = ?";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, dto.getF_type());
			ps.setString(2, dto.getF_question());
			ps.setString(3, dto.getF_answer());
			ps.setString(4, dto.getF_reg_id());
			ps.setString(5, dto.getF_position());
			ps.setString(6, dto.getF_update_date());
			ps.setString(7, dto.getF_no());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("updateFaq() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return result;
	}

	// FAQ 등록
	public int saveFaq(FaqDto dto) {
		int result = 0;
		String sql = "insert into jsl_hotel_faq\r\n"
				+ "(f_no, f_type, f_question, f_answer, f_reg_id, f_position, f_reg_date)\r\n"
				+ "values\r\n"
				+ "(?, ?, ?, ?, ?, ?, to_date(?, 'yyyy-MM-dd hh24:mi:ss'))";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, dto.getF_no());
			ps.setString(2, dto.getF_type());
			ps.setString(3, dto.getF_question());
			ps.setString(4, dto.getF_answer());
			ps.setString(5, dto.getF_reg_id());
			ps.setString(6, dto.getF_position());
			ps.setString(7, dto.getF_reg_date());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("saveFaq() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return result;
	}
	
}
