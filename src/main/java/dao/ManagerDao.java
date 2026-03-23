package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import common.DBConnection;
import dto.ManagerDto;
import dto.MemberDto;
import dto.QnaDto;

public class ManagerDao {

	Connection 			con = null;
	PreparedStatement 	ps = null;
	ResultSet 			rs = null;

	// 멤버 리스트 [관리자페이지]
		public List<ManagerDto> getMemberList(String select, String mb, String search, int start, int end) {
			List<ManagerDto> dtos = new ArrayList<ManagerDto>();

			// ⚠️ Column Name Injection 방지: 허용된 컬럼만 사용
			String allowedColumn = "m_id"; // 기본값
			if ("id".equals(select)) {
				allowedColumn = "m_id";
			} else if ("email_1".equals(select)) {
				allowedColumn = "m_email_1";
			} else if ("name".equals(select)) {
				allowedColumn = "m_first_name"; // name인 경우 OR 조건으로 처리
			}

			String sql = "select * "
					+ "from( "
					+ "		select rownum as rnum, tbl.* "
					+ "		from (select m_id, m_first_name, m_last_name, m_email_1, m_email_2, \r\n"
					+ "        		to_char(m_reg_date, 'yyyy-MM-dd') as m_reg_date, to_char(m_exit_date, 'yyyy-MM-dd') as m_exit_date, \r\n"
					+ "       		mb_membership\r\n"
					+ "			from jsl_hotel_member m, jsl_hotel_membership b\r\n"
					+ "		where m.m_id = b.mb_id\r\n";

		    if(select.equals("name")) {
		    	sql += "and (m_first_name like ? \r\n";
		    	sql += "or m_last_name like ?) \r\n";
		    } else {
		    	sql += "and " + allowedColumn + " like ? \r\n";
		    }

		    if(!mb.equals("all")) {
		    	sql += "and mb_membership like ? \r\n";
		    }

		    sql += ") tbl) "
		    		+ "where rnum >= ? and rnum <= ?";

			try {
				con = DBConnection.getConnection();
				ps  = con.prepareStatement(sql);

				int paramIndex = 1;
				if(select.equals("name")) {
					ps.setString(paramIndex++, "%" + search + "%");
					ps.setString(paramIndex++, "%" + search + "%");
				} else {
					ps.setString(paramIndex++, "%" + search + "%");
				}

				if(!mb.equals("all")) {
					ps.setString(paramIndex++, "%" + mb + "%");
				}

				ps.setInt(paramIndex++, start);
				ps.setInt(paramIndex++, end);

				rs  = ps.executeQuery();

				while(rs.next()) {
					String id = rs.getString("m_id");
					String first_name = rs.getString("m_first_name");
					String last_name = rs.getString("m_last_name");
					String email_1 = rs.getString("m_email_1");
					String email_2 = rs.getString("m_email_2");
					String reg_date = rs.getString("m_reg_date");
					String exit_date = rs.getString("m_exit_date");
					String membership = rs.getString("mb_membership");

					ManagerDto dto = new ManagerDto(id, first_name, last_name, email_1, email_2, reg_date, exit_date, membership);
					dtos.add(dto);
				}


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("getMemberList() 오류 : \r\n"+sql);
			} finally {
				DBConnection.closeDB(con, ps, rs);
			}

			return dtos;
		}

		// 회원정보 [관리자용]
		public ManagerDto getMemberView(String id) {
			ManagerDto dto = null;
			String sql = "select m_first_name, m_last_name, m_area, m_address, \n"
					+ "m_email_1, m_email_2, m_mobile_1, m_mobile_2, m_mobile_3, "
					+ "m_gender, m_rank, m_sns, m_position, \n"
					+ "to_char(m_reg_date, 'yyyy-MM-dd hh24:mi:ss') as m_reg_date, "
					+ "to_char(m_update_date, 'yyyy-MM-dd hh24:mi:ss') as m_update_date, \n"
					+ "to_char(m_exit_date, 'yyyy-MM-dd hh24:mi:ss') as m_exit_date \n"
					+ "from jsl_hotel_member\n"
					+ "where m_id = ?";

			try {
				con = DBConnection.getConnection();
				ps  = con.prepareStatement(sql);
				ps.setString(1, id);
				rs = ps.executeQuery();

				if(rs.next()) {
					String first_name = rs.getString("m_first_name");
					String last_name = rs.getString("m_last_name");
					String area = rs.getString("m_area");
					String address = rs.getString("m_address");
					String email_1 = rs.getString("m_email_1");
					String email_2 = rs.getString("m_email_2");
					String mobile_1 = rs.getString("m_mobile_1");
					String mobile_2 = rs.getString("m_mobile_2");
					String mobile_3 = rs.getString("m_mobile_3");
					String gender = rs.getString("m_gender");
					String rank = rs.getString("m_rank");
					String sns = rs.getString("m_sns");
					String position = rs.getString("m_position");
					String reg_date = rs.getString("m_reg_date");
					String update_date = rs.getString("m_update_date");
					String exit_date = rs.getString("m_exit_date");

					dto = new ManagerDto(id, "", first_name, last_name, area,
							address, email_1, email_2, mobile_1, mobile_2, mobile_3,
							gender, rank, sns, position, reg_date, update_date, exit_date);
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("getMemberView() 오류 : \r\n" + sql);
			} finally {
				DBConnection.closeDB(con, ps, rs);
			}

			return dto;
		}

		// 회원정보 수정 [관리자]
		public int memberUpdate(ManagerDto dto) {
			int result = 0;
			String sql = "update jsl_hotel_member \n"
					+ "set \n";

			int paramIndex = 1;
			boolean hasPassword = !dto.getPassword().equals("");

			if(hasPassword) {
				sql += "	m_password = ?, \n";
			}

			sql +=  "    m_first_name = ?, \n"
					+ "    m_last_name = ?, \n"
					+ "    m_area = ?, \n"
					+ "    m_address = ?, \n"
					+ "    m_email_1 = ?,\n"
					+ "    m_email_2 = ?,\n"
					+ "    m_mobile_1 = ?, \n"
					+ "    m_mobile_2 = ?, \n"
					+ "    m_mobile_3 = ?, \n"
					+ "    m_gender = ?, \n"
					+ "    m_rank = ?, \n"
					+ "    m_position = ?, \n"
					+ "    m_sns = ?, \n"
					+ "    m_update_date = to_date(?, 'yyyy-MM-dd hh24:mi:ss') \n"
					+ "where m_id = ?";

			try {
				con = DBConnection.getConnection();
				ps  = con.prepareStatement(sql);

				if(hasPassword) {
					ps.setString(paramIndex++, dto.getPassword());
				}

				ps.setString(paramIndex++, dto.getFirst_name());
				ps.setString(paramIndex++, dto.getLast_name());
				ps.setString(paramIndex++, dto.getArea());
				ps.setString(paramIndex++, dto.getAddress());
				ps.setString(paramIndex++, dto.getEmail_1());
				ps.setString(paramIndex++, dto.getEmail_2());
				ps.setString(paramIndex++, dto.getMobile_1());
				ps.setString(paramIndex++, dto.getMobile_2());
				ps.setString(paramIndex++, dto.getMobile_3());
				ps.setString(paramIndex++, dto.getGender());
				ps.setString(paramIndex++, dto.getRank());
				ps.setString(paramIndex++, dto.getPosition());
				ps.setString(paramIndex++, dto.getSns());
				ps.setString(paramIndex++, dto.getUpdate_date());
				ps.setString(paramIndex++, dto.getId());

				result = ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("memberUpdate() 오류 : \r\n" + sql);
			} finally {
				DBConnection.closeDB(con, ps, rs);
			}

			return result;
		}

		// 멤버 상태 변경 [탈퇴 여부]
		public int changeStatus(String id, String status, String date) {
			int result = 0;
			String sql = "update jsl_hotel_member "
					+ "set ";
			if(status.equals("exit")) {
				sql += "	m_exit_date = to_date(? , 'yyyy-MM-dd hh24:mi:ss') ";
			} else if(status.equals("recovery")) {
				sql += "	m_update_date = to_date(? , 'yyyy-MM-dd hh24:mi:ss'), "
						+ "	m_exit_date = '' ";
			}

			sql += "where m_id = ?";

			try {
				con = DBConnection.getConnection();
				ps  = con.prepareStatement(sql);
				ps.setString(1, date);
				ps.setString(2, id);
				result = ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("changeStatus() 오류 : \r\n" + sql);
			} finally {
				DBConnection.closeDB(con, ps, rs);
			}

			return result;
		}

		// 총 회원 수 [페이징]
		public int getMemberCount(String select, String mb, String search) {
			int count = 0;

			// ⚠️ Column Name Injection 방지: 허용된 컬럼만 사용
			String allowedColumn = "m_id"; // 기본값
			if ("id".equals(select)) {
				allowedColumn = "m_id";
			} else if ("email_1".equals(select)) {
				allowedColumn = "m_email_1";
			} else if ("name".equals(select)) {
				allowedColumn = "m_first_name"; // name인 경우 OR 조건으로 처리
			}

			String sql = "select count(*) as count\r\n"
					+ "	from jsl_hotel_member m, jsl_hotel_membership b\r\n"
					+ "	where m.m_id = b.mb_id\r\n";
		    if(select.equals("name")) {
		    	sql += "and (m_first_name like ? \r\n";
		    	sql += "or m_last_name like ?) \r\n";
		    } else {
		    	sql += "and " + allowedColumn + " like ? \r\n";
		    }

		    if(!mb.equals("all")) {
		    	sql += "and mb_membership like ?\r\n";
		    }


			try {
				con = DBConnection.getConnection();
				ps  = con.prepareStatement(sql);

				int paramIndex = 1;
				if(select.equals("name")) {
					ps.setString(paramIndex++, "%" + search + "%");
					ps.setString(paramIndex++, "%" + search + "%");
				} else {
					ps.setString(paramIndex++, "%" + search + "%");
				}

				if(!mb.equals("all")) {
					ps.setString(paramIndex++, "%" + mb + "%");
				}

				rs  = ps.executeQuery();

				if(rs.next()) {
					count = rs.getInt("count");
				}


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("getMemberCount() 오류 : \r\n"+sql);
			} finally {
				DBConnection.closeDB(con, ps, rs);
			}

			return count;
		}


		// QnA 목록 [관리자]
		public List<QnaDto> getQnaList(String select, String order, String type, String search, int start, int end) {
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

			// desc => 질문 등록 날짜 내림차순 / asc => 답변 없는 질문 오름차순으로
			if(order.equals("desc")) {
				sql += "order by q_no desc ";
			} else if(order.equals("asc")) {
				sql += "order by case when a_content is null then 0 else 1 end asc, q_reg_date asc";
			}

			sql += ") tbl) "
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


}
