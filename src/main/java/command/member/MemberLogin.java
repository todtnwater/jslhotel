package command.member;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import common.CommonExecute;
import common.CommonUtil;
import dao.MemberDao;
import dto.MemberDto;

public class MemberLogin implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {

		MemberDao dao = new MemberDao();
		
		String id = request.getParameter("t_id");
		String password = request.getParameter("t_password");
		String login_date = CommonUtil.getTodayTime();
			
		try {
			password = dao.encryptSHA256(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		MemberDto dto = dao.memberLogin(id, password);
		
		String msg, url;
		
		System.out.println("========== 로그인 처리 시작 ==========");
		System.out.println("입력된 ID: " + id);
		System.out.println("DTO 조회 결과: " + (dto != null ? "성공" : "실패"));
		
		// 이름 존재 시 세션에 등록
		if(dto != null) {
			String name = dto.getFirst_name() + " " + dto.getLast_name();
			
			System.out.println(">>> 로그인 성공 - 회원명: " + name);
			
			// ⭐ 세션 생성 (기존 세션이 있으면 무효화하고 새로 생성)
			HttpSession session = request.getSession(false);
			if (session != null) {
				System.out.println(">>> 기존 세션 무효화");
				session.invalidate();
			}
			session = request.getSession(true);  // 새 세션 생성
			
			System.out.println(">>> 새 세션 생성 완료 - 세션 ID: " + session.getId());
			
			// ⭐ 필수 세션 정보 저장
			session.setAttribute("sessionId", id);
			session.setAttribute("sessionName", name);
			
			System.out.println(">>> sessionId 저장: " + id);
			System.out.println(">>> sessionName 저장: " + name);
			
			int result = dao.memberLoginDate(id, login_date);
			
			if(result == 1) {
				// 관리자 등급일 시 세션레벨 부여
				String rank = dto.getRank();
				String position = dao.getPosition(id);
				String membership = dao.getMembership(id);
				int totalPoints = dao.getMemberPoint(id);

				System.out.println(">>> 회원 정보:");
				System.out.println("  - rank: " + rank);
				System.out.println("  - position: " + position);
				System.out.println("  - membership: " + membership);
				System.out.println("  - totalPoints: " + totalPoints);
				
				// ⭐ level 설정 (일반 회원도 "normal"로 설정)
				if(rank != null && rank.equals("super")) {
					session.setAttribute("sessionLevel", "top");
					System.out.println(">>> sessionLevel 저장: top");
				} else {
					session.setAttribute("sessionLevel", "normal");
					System.out.println(">>> se1ssionLevel 저장: normal");
				}
				
				// ⭐ position 설정 (null이면 빈 문자열)
				if(position == null || position.trim().isEmpty()) {
					position = "";
				}
				session.setAttribute("sessionPosition", position);
				System.out.println(">>> sessionPosition 저장: " + position);
				
				// ⭐ membership 설정 (null이면 "bronze")
				if(membership == null || membership.trim().isEmpty()) {
					membership = "bronze";
				}
				session.setAttribute("sessionMembership", membership);
				System.out.println(">>> sessionMembership 저장: " + membership);

				// ⭐ 포인트 저장
				session.setAttribute("totalPoints", totalPoints);
				System.out.println(">>> totalPoints 저장: " + totalPoints);

				// ⭐ 세션 타임아웃 설정
				session.setMaxInactiveInterval(60 * 60 * 1); // 1시간

				System.out.println("========== 세션 저장 완료 확인 ==========");
				System.out.println("확인 - sessionId: " + session.getAttribute("sessionId"));
				System.out.println("확인 - sessionName: " + session.getAttribute("sessionName"));
				System.out.println("확인 - sessionLevel: " + session.getAttribute("sessionLevel"));
				System.out.println("확인 - sessionPosition: " + session.getAttribute("sessionPosition"));
				System.out.println("확인 - sessionMembership: " + session.getAttribute("sessionMembership"));
				System.out.println("확인 - totalPoints: " + session.getAttribute("totalPoints"));
				System.out.println("========================================");
				
				msg = name + " 님 환영합니다!";
				url = "Index";
			} else {
				System.out.println("로그인 날짜 업데이트 실패");
				msg = "로그인 오류! 관리자에게 문의 바랍니다.";
				url = "Member";
			}
		} else {
			System.out.println("로그인 실패 - ID 또는 비밀번호 불일치");
			msg = "ID나 비밀번호가 올바르지 않습니다.";
			url = "Member";
		}
		
		System.out.println("========== 로그인 처리 종료 ==========");
			
		request.setAttribute("t_msg", msg);
		request.setAttribute("t_url", url);
		
	}

}