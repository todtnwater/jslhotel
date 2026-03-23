package dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import common.DBConnection;
import dto.MemberDto;
import dto.QnaDto;

public class MemberDao {

	Connection 			con = null;
	PreparedStatement 	ps = null;
	ResultSet 			rs = null;
	
	
	
	public MemberDto getMemberById(String memberId) {
        MemberDto dto = null;
        
        //회원 ID로 회원 정보 조회
        String sql = "SELECT M_ID, M_PASSWORD, M_FIRST_NAME, M_LAST_NAME, " +
                    "       M_AREA, M_ADDRESS, " +
                    "       M_EMAIL_1, M_EMAIL_2, " +
                    "       M_MOBILE_1, M_MOBILE_2, M_MOBILE_3, " +
                    "       M_GENDER, M_RANK, M_SNS, " +
                    "       TO_CHAR(M_REG_DATE, 'YYYY-MM-DD') AS M_REG_DATE, " +
                    "       TO_CHAR(M_UPDATE_DATE, 'YYYY-MM-DD') AS M_UPDATE_DATE, " +
                    "       TO_CHAR(M_EXIT_DATE, 'YYYY-MM-DD') AS M_EXIT_DATE " +
                    "FROM JSL_HOTEL_MEMBER " +
                    "WHERE M_ID = ?";
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, memberId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                dto = new MemberDto(
                    rs.getString("M_ID"),
                    rs.getString("M_PASSWORD"),
                    rs.getString("M_FIRST_NAME"),
                    rs.getString("M_LAST_NAME"),
                    rs.getString("M_AREA"),
                    rs.getString("M_ADDRESS"),
                    rs.getString("M_EMAIL_1"),
                    rs.getString("M_EMAIL_2"),
                    rs.getString("M_MOBILE_1"),
                    rs.getString("M_MOBILE_2"),
                    rs.getString("M_MOBILE_3"),
                    rs.getString("M_GENDER"),
                    rs.getString("M_RANK"),
                    rs.getString("M_SNS"),
                    rs.getString("M_REG_DATE"),
                    rs.getString("M_UPDATE_DATE"),
                    rs.getString("M_EXIT_DATE")
                );
                
                System.out.println("✅ 회원 정보 조회 성공: " + memberId);
            } else {
                System.out.println("⚠️ 회원 정보 없음: " + memberId);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getMemberById() 오류");
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        
        return dto;
    }
	
	
	// 로그인 정보 확인
	public MemberDto memberLogin(String id, String password) {
		MemberDto dto = null;
		String sql = "select m_first_name, m_last_name, m_rank \r\n"
				+ "from jsl_hotel_member\r\n"
				+ "where m_id = ? \r\n"
				+ "and m_password = ? "
				+ "and m_exit_date is null";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, password);
			rs  = ps.executeQuery();

			if(rs.next()) {
				String first_name = rs.getString("m_first_name");
				String last_name = rs.getString("m_last_name");
				String rank = rs.getString("m_rank");

				dto = new MemberDto(first_name, last_name, rank);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("memberLogin() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return dto;
	}
	
	// 등급 확인 [MemberLogin 클래스]
	public String getMemberRank(String id, String password) {
		String rank = "";
		String sql = "select m_rank \r\n"
				+ "from jsl_hotel_member\r\n"
				+ "where m_id = ? \r\n"
				+ "and m_password = ?";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, password);
			rs  = ps.executeQuery();

			if(rs.next()) {
				rank = rs.getString("m_rank");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("memberRank() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return rank;
	}

	// ID 중복 체크
	public int memberCheckId(String id) {
		int count = 0;
		String sql = "select count(*) as count "
				+ "from jsl_hotel_member "
				+ "where m_id = ?";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, id);
			rs  = ps.executeQuery();

			if(rs.next()) {
				count = rs.getInt("count");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("memberCheckId() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return count;
	}
	
	// 비밀번호 암호화
	public String encryptSHA256(String pw) throws NoSuchAlgorithmException{
		
		String encryptData = "";
		MessageDigest sha = MessageDigest.getInstance("SHA-256");
		sha.update(pw.getBytes());
	
		byte[] digest = sha.digest();
		for (int i=0; i<digest.length; i++) {
			encryptData += Integer.toHexString(digest[i] &0xFF).toUpperCase();
		}
		
		return encryptData;
	}

	// 회원가입
	public int memberSave(MemberDto dto) {
		int result = 0;
		String sql = "insert into jsl_hotel_member\n"
				+ "(m_id, m_password, m_first_name, m_last_name, "
				+ "m_area, m_address, m_email_1, m_email_2, "
				+ "m_mobile_1, m_mobile_2, m_mobile_3, "
				+ "m_gender, m_sns, m_reg_date)\n"
				+ "values\n"
				+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, to_date(?, 'yyyy-MM-dd hh24:mi:ss'))";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, dto.getId());
			ps.setString(2, dto.getPassword());
			ps.setString(3, dto.getFirst_name());
			ps.setString(4, dto.getLast_name());
			ps.setString(5, dto.getArea());
			ps.setString(6, dto.getAddress());
			ps.setString(7, dto.getEmail_1());
			ps.setString(8, dto.getEmail_2());
			ps.setString(9, dto.getMobile_1());
			ps.setString(10, dto.getMobile_2());
			ps.setString(11, dto.getMobile_3());
			ps.setString(12, dto.getGender());
			ps.setString(13, dto.getSns());
			ps.setString(14, dto.getReg_date());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("memberSave() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return result;
	}

	

	// 마이페이지 [멤버 테이블]
	public MemberDto getMemberInfo(String id) {
		MemberDto dto = null;
		String sql = "select m_first_name, m_last_name, m_area, m_address, \n"
				+ "m_email_1, m_email_2, m_mobile_1, m_mobile_2, m_mobile_3, "
				+ "m_gender, m_rank, m_sns, \n"
				+ "to_char(m_reg_date, 'yyyy-MM-dd hh24:mi:ss') as m_reg_date, "
				+ "to_char(m_update_date, 'yyyy-MM-dd hh24:mi:ss') as m_update_date \n"
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
				String reg_date = rs.getString("m_reg_date");
				String update_date = rs.getString("m_update_date");

				dto = new MemberDto(id, first_name, last_name, area, address,
								email_1, email_2, mobile_1, mobile_2, mobile_3,
								gender, rank, sns, reg_date, update_date);


			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getMemberInfo() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return dto;
	}

	// 회원탈퇴
	public int memberExit(String id, String exit_date) {
		int result = 0;
		String sql = "update jsl_hotel_member "
				+ "set"
				+ " m_exit_date = to_date(?, 'yyyy-MM-dd hh24:mi:ss') "
				+ "where m_id = ?";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, exit_date);
			ps.setString(2, id);
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("memberExit() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return result;
	}

	// 비밀번호 확인 [마이페이지 수정, 탈퇴]
	public int memberCheckPw(String id, String pw) {
		int count = 0;
		String sql = "select count(*) as count "
				+ "from jsl_hotel_member "
				+ "where m_id = ? "
				+ "and m_password = ?";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, pw);
			rs = ps.executeQuery();

			if(rs.next()) {
				count = rs.getInt("count");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("memberCheckPw() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return count;
	}

	// 회원정보 수정
	public int memberUpdate(MemberDto dto) {
		int result = 0;
		String sql = "update jsl_hotel_member \n"
				+ "set \n"
				+ "    m_first_name = ?, \n"
				+ "    m_last_name = ?, \n"
				+ "    m_area = ?, \n"
				+ "    m_address = ?, \n"
				+ "    m_email_1 = ?,\n"
				+ "    m_email_2 = ?,\n"
				+ "    m_mobile_1 = ?, \n"
				+ "    m_mobile_2 = ?, \n"
				+ "    m_mobile_3 = ?, \n"
				+ "    m_gender = ?, \n"
				+ "    m_sns = ?, \n"
				+ "    m_update_date = to_date(?, 'yyyy-MM-dd hh24:mi:ss') \n"
				+ "where m_id = ?";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, dto.getFirst_name());
			ps.setString(2, dto.getLast_name());
			ps.setString(3, dto.getArea());
			ps.setString(4, dto.getAddress());
			ps.setString(5, dto.getEmail_1());
			ps.setString(6, dto.getEmail_2());
			ps.setString(7, dto.getMobile_1());
			ps.setString(8, dto.getMobile_2());
			ps.setString(9, dto.getMobile_3());
			ps.setString(10, dto.getGender());
			ps.setString(11, dto.getSns());
			ps.setString(12, dto.getUpdate_date());
			ps.setString(13, dto.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("memberUpdate() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return result;
	}

	// 비밀번호 수정
	public int changePassword(String id, String pw, String update_date) {
		int result = 0;
		String sql = "update jsl_hotel_member "
				+ "set "
				+ "	m_password = ?, "
				+ " m_update_date = to_date(?, 'yyyy-MM-dd hh24:mi:ss') "
				+ "where m_id = ?";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, pw);
			ps.setString(2, update_date);
			ps.setString(3, id);
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("changePassword() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return result;
	}

	// 아이디 찾기
	public String memberFindId(String first_name, String last_name, String email_1, String email_2) {
		String id = "";
		String sql = "select m_id "
				+ "from jsl_hotel_member "
				+ "where m_first_name = ? "
				+ "and m_last_name = ? "
				+ "and m_email_1 = ? "
				+ "and m_email_2 = ?";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, first_name);
			ps.setString(2, last_name);
			ps.setString(3, email_1);
			ps.setString(4, email_2);
			rs  = ps.executeQuery();

			if(rs.next()) {
				id = rs.getString("m_id");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("memberFindId() 오류 : \r\n"+sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return id;
	}

	// 로그인 일시 기록 [휴면 계정 전환 대비]
	public int memberLoginDate(String id, String login_date) {
		int result = 0;
		String sql = "update jsl_hotel_member "
				+ "set "
				+ "	m_login_date = to_date(?, 'yyyy-MM-dd hh24:mi:ss') "
				+ "where m_id = ?";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, login_date);
			ps.setString(2, id);
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("memberLoginDate() 오류 : \r\n"+sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return result;
	}

	// 직책
	public String getPosition(String id) {
		String position = "";
		String sql = "select m_position from jsl_hotel_member "
				+ "where m_id = ?";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();

			if(rs.next()) {
				position = rs.getString("m_position");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getPosition() 오류 : \r\n"+sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return position;
	}

	
	// QnA 목록 [마이페이지]
	public List<QnaDto> getQnaList(String select, String search, String id, int start, int end) {
		List<QnaDto> dtos = new ArrayList<QnaDto>();

		// ⚠️ Column Name Injection 방지: 허용된 컬럼만 사용
		String allowedColumn = "title"; // 기본값
		if ("title".equals(select)) {
			allowedColumn = "title";
		} else if ("content".equals(select)) {
			allowedColumn = "content";
		} else if ("type".equals(select)) {
			allowedColumn = "type";
		}

		String sql = "select * \r\n"
				+ "from( 		\r\n"
				+ "        select rownum as rnum, tbl.* 		\r\n"
				+ "            from (select q_no, q_title, q_content, q_type, q_member,  \r\n"
				+ "                    q_reg_writer, q_password, a_content,  \r\n"
				+ "                    to_char(q_reg_date, 'yyyy-MM-dd') as q_reg_date \r\n"
				+ "                    from jsl_hotel_qna q\r\n"
				+ " 			where q.q_reg_writer = ? \r\n"
				+ "            and q_" + allowedColumn + " like ? 			\r\n"
				+ "            order by q_no desc) tbl )\r\n"
				+ "where rnum >= ? and rnum <= ?";


		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, "%" + search + "%");
			ps.setInt(3, start);
			ps.setInt(4, end);
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
			System.out.println("[member] getQnaList() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return dtos;
	}

	// QnA 개수 [회원]
	public int getQnaCount(String select, String search, String id) {
		int count = 0;
		String sql = "select count(*) as count "
					+ "from jsl_hotel_qna "
					+ "where q_reg_writer = ? ";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, id);
			rs  = ps.executeQuery();

			if(rs.next()) {
				count = rs.getInt("count");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[member] getQnaList() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}


		return count;
	}

	
	// 멤버십 정보 가져오기
	public String getMembership(String id) {
		String membership = "";
		String sql = "select m_membership from jsl_hotel_member "
				+ "where m_id = ?";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();

			if(rs.next()) {
				membership = rs.getString("m_membership");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getMembership() 오류 : \r\n"+sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return membership;
	}

	// 회원정보 확인 [임시 비밀번호 생성 시]
	public int memberCheckIdEmail(String id, String email_1, String email_2) {
		int count = 0;
		String sql = "select count(*) as count "
				+ "from jsl_hotel_member "
				+ "where m_id = ? "
				+ "and m_email_1 = ? "
				+ "and m_email_2 = ?";

		try {
			con = DBConnection.getConnection();
			ps  = con.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, email_1);
			ps.setString(3, email_2);
			rs  = ps.executeQuery();

			if(rs.next()) {
				count = rs.getInt("count");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("memberCheckIdEmail() 오류 : \r\n" + sql);
		} finally {
			DBConnection.closeDB(con, ps, rs);
		}

		return count;
	}
	
	// 회원의 현재 포인트 조회 (마이페이지용)
		public int getMemberPoint(String id) {
			int point = 0;
			String sql = "SELECT M_POINT FROM JSL_HOTEL_MEMBER WHERE M_ID = ?";
			
			try {
				con = DBConnection.getConnection();
				ps = con.prepareStatement(sql);
				ps.setString(1, id);
				rs = ps.executeQuery();
				
				if(rs.next()) {
					point = rs.getInt("M_POINT");
				}
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println("getMemberPoint() 오류");
			} finally {
				DBConnection.closeDB(con, ps, rs);
			}
			
			return point;
		}
		
		
		// 회원의 누적 포인트 조회
		public int getMemberTotalPoint(String id) {
			int totalPoint = 0;
			String sql = "SELECT M_TOTAL_POINT FROM JSL_HOTEL_MEMBER WHERE M_ID = ?";
			
			try {
				con = DBConnection.getConnection();
				ps = con.prepareStatement(sql);
				ps.setString(1, id);
				rs = ps.executeQuery();
				
				if(rs.next()) {
					totalPoint = rs.getInt("M_TOTAL_POINT");
				}
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println("getMemberTotalPoint() 오류");
			} finally {
				DBConnection.closeDB(con, ps, rs);
			}
			
			return totalPoint;
		}
		
		// ========================================
		// MemberDao.java 끝에 추가할 완전한 코드
		// ========================================

		/**
		 * 포인트 사용 (결제 시 차감)
		 * @param memberId 회원 ID
		 * @param points 사용할 포인트
		 * @return 성공 시 1, 실패 시 0
		 */
		public int useMemberPoint(String memberId, int points) {
		    int result = 0;
		    
		    String sql = "UPDATE JSL_HOTEL_MEMBER " +
		                "SET M_POINT = M_POINT - ? " +
		                "WHERE M_ID = ? AND M_POINT >= ?";
		    
		    try {
		        con = DBConnection.getConnection();
		        ps = con.prepareStatement(sql);
		        ps.setInt(1, points);
		        ps.setString(2, memberId);
		        ps.setInt(3, points);
		        
		        result = ps.executeUpdate();
		        
		        if (result > 0) {
		            System.out.println("✅ 포인트 사용 성공: " + memberId + " → -" + points + "P");
		        } else {
		            System.out.println("❌ 포인트 부족: " + memberId + " (사용 시도: " + points + "P)");
		        }
		        
		    } catch (Exception e) {
		        e.printStackTrace();
		        System.out.println("useMemberPoint() 오류");
		    } finally {
		        DBConnection.closeDB(con, ps, rs);
		    }
		    
		    return result;
		}

		/**
		 * 포인트 적립 (결제 완료 시)
		 * @param memberId 회원 ID
		 * @param points 적립할 포인트
		 * @return 성공 시 1, 실패 시 0
		 */
		public int addMemberPoint(String memberId, int points) {
		    int result = 0;
		    
		    String sql = "UPDATE JSL_HOTEL_MEMBER " +
		                "SET M_POINT = M_POINT + ?, " +
		                "    M_TOTAL_POINT = M_TOTAL_POINT + ? " +
		                "WHERE M_ID = ?";
		    
		    try {
		        con = DBConnection.getConnection();
		        ps = con.prepareStatement(sql);
		        ps.setInt(1, points);
		        ps.setInt(2, points);
		        ps.setString(3, memberId);
		        
		        result = ps.executeUpdate();
		        
		        System.out.println("✅ 포인트 적립 성공: " + memberId + " → +" + points + "P");
		        
		    } catch (Exception e) {
		        e.printStackTrace();
		        System.out.println("addMemberPoint() 오류");
		    } finally {
		        DBConnection.closeDB(con, ps, rs);
		    }
		    
		    return result;
		}

		/**
		 * 멤버십별 포인트 적립률 계산
		 * @param membership 멤버십 등급 (bronze/silver/gold/premium)
		 * @param amount 결제 금액
		 * @return 적립될 포인트
		 */
		public int calculateEarnPoints(String membership, int amount) {
		    double rate = 0.0;
		    
		    // null 체크
		    if (membership == null || membership.isEmpty()) {
		        membership = "silver";
		    }
		    
		    // 멤버십별 적립률
		    switch(membership.toLowerCase()) {
		        case "bronze":
		            rate = 0.0;     // 0%
		            break;
		        case "silver":
		            rate = 0.10;    // 10%
		            break;
		        case "gold":
		            rate = 0.15;    // 15%
		            break;
		        case "premium":
		            rate = 0.20;    // 20%
		            break;
		        default:
		            rate = 0.0;    // 기본 0%
		    }
		    
		    int earnPoints = (int)(amount * rate);
		    
		    System.out.println("========== 적립 포인트 계산 ==========");
		    System.out.println("멤버십: " + membership);
		    System.out.println("적립률: " + (rate * 100) + "%");
		    System.out.println("결제금액: " + amount + "원");
		    System.out.println("적립포인트: " + earnPoints + "P");
		    System.out.println("====================================");
		    
		    return earnPoints;
		}
		

		/**
		 * 회원 멤버십 등급 업데이트 (멤버십 구매 시)
		 * @param memberId 회원 ID
		 * @param membershipGrade 멤버십 등급 (gold/premium)
		 * @return 성공 시 1, 실패 시 0
		 */
		public int updateMembershipGrade(String memberId, String membershipGrade) {
		    int result = 0;
		    
		    String sql = "UPDATE JSL_HOTEL_MEMBER " +
		                "SET M_MEMBERSHIP = ? " +
		                "WHERE M_ID = ?";
		    
		    try {
		        con = DBConnection.getConnection();
		        ps = con.prepareStatement(sql);
		        ps.setString(1, membershipGrade);
		        ps.setString(2, memberId);
		        
		        result = ps.executeUpdate();
		        
		        if (result > 0) {
		            System.out.println("========== 멤버십 등급 업데이트 완료 ==========");
		            System.out.println("회원 ID: " + memberId);
		            System.out.println("변경 등급: " + membershipGrade.toUpperCase());
		            System.out.println("==========================================");
		        } else {
		            System.out.println("❌ 멤버십 등급 업데이트 실패: 회원을 찾을 수 없음");
		        }
		        
		    } catch (Exception e) {
		        e.printStackTrace();
		        System.out.println("updateMembershipGrade() 오류");
		    } finally {
		        DBConnection.closeDB(con, ps, rs);
		    }
		    
		    return result;
		}

	
}
