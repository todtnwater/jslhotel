package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import command.faq.FaqDelete;
import command.faq.FaqList;
import command.faq.FaqSave;
import command.faq.FaqUpdate;
import command.faq.FaqView;
import command.manager.MemberChangeStatus;
import command.manager.MemberList;
import command.manager.MemberUpdateAdmin;
import command.manager.MemberViewAdmin;
import command.manager.QnaListAdmin;
import command.member.MemberView;
import command.qna.QnaList;
import command.qna.QnaView;
import common.CommonExecute;
import common.CommonUtil;
import dao.ReservationDao;
import dto.ReservationDto;

@WebServlet("/Manager")
public class Manager extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Manager() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		
		String level = CommonUtil.getSessionInfo(request, "level");
		
		String gubun = request.getParameter("t_gubun");
		if(gubun == null || gubun.equals("")) gubun = "memberList";
		String viewPage = "";
		
		
		if(level.equals("")) {
			request.setAttribute("t_msg", "잘못된 접근입니다.");
			request.setAttribute("t_url", "Index");
			viewPage = "common_alert.jsp";
		} else { // Level is not empty (user is logged in)
			
			// ========== 회원 관리 ==========
			// 회원 목록 [관리자]
			if(gubun.equals("memberList")) {
				CommonExecute mana = new MemberList();
				mana.execute(request);
				viewPage = "mana/member_list.jsp";
			
			// 회원 상세보기
			} else if(gubun.equals("memberView")) {
				CommonExecute mana = new MemberViewAdmin();
				mana.execute(request);
				viewPage = "mana/member_view.jsp";	
			
			// 마이페이지 [관리자]
			} else if(gubun.equals("myInfo")) {
				CommonExecute mem = new MemberViewAdmin();
				mem.execute(request);
				viewPage = "mana/member_view.jsp";
			
			// 비밀번호 변경 [관리자]
			} else if(gubun.equals("password")) {
				CommonExecute mem = new MemberView();
				mem.execute(request);
				viewPage = "member/member_changePassword.jsp";
			
			// 회원정보 수정 [관리자]
			} else if(gubun.equals("memberUpdate")) {
				CommonExecute mana = new MemberUpdateAdmin();
				mana.execute(request);
				viewPage = "common_alert_view.jsp";
			
			// 탈퇴 정보 수정 
			} else if(gubun.equals("memberStatus")) {
				CommonExecute mana = new MemberChangeStatus();
				mana.execute(request);
				viewPage = "common_alert_view.jsp";
			
			// Q&A 목록
			}  else if(gubun.equals("qnaList")) {
				CommonExecute mana = new QnaListAdmin();
				mana.execute(request);
				viewPage = "mana/m_qna_list.jsp";
			
			// Q&A 상세보기
			}  else if(gubun.equals("qnaView")) {
				CommonExecute mana = new QnaView();
				mana.execute(request);
				viewPage = "mana/m_qna_view.jsp";
			
			// FAQ 목록	
			} else if(gubun.equals("faqList")) {
				CommonExecute mana = new FaqList();
				mana.execute(request);
				viewPage = "mana/m_faq_list.jsp";
			
			// FAQ 상세보기	
			} else if(gubun.equals("faqView")) {
				CommonExecute mana = new FaqView();
				mana.execute(request);
				viewPage = "mana/m_faq_view.jsp";
			
			// FAQ 수정
			} else if(gubun.equals("faqUpdate")) {
				CommonExecute mana = new FaqUpdate();
				mana.execute(request);
				viewPage = "common_alert_view.jsp";
			
			// FAQ 삭제
			} else if(gubun.equals("faqDelete")) {
				CommonExecute mana = new FaqDelete();
				mana.execute(request);
				viewPage = "common_alert_view.jsp";
			
			// FAQ 등록폼
			} else if(gubun.equals("faqWriteForm")) {
				request.setAttribute("toDay", CommonUtil.getToday());
				viewPage = "mana/m_faq_write.jsp";
			
			// FAQ 등록
			} else if(gubun.equals("faqSave")) {
				CommonExecute mana = new FaqSave();
				mana.execute(request);
				viewPage = "common_alert_view.jsp";
			
			// 예약 목록 / 예약 검색 [관리자]
			} else if(gubun.equals("bookList") || gubun.equals("bookSearch")) { // ✅ gubun 통합 및 구조 수정
			    
			    String managerLevel = CommonUtil.getSessionInfo(request, "level");

			    if ("top".equals(managerLevel)) { // ✅ 관리자 권한 확인
	                try {
	                    ReservationDao dao = new ReservationDao(); 
	                    List<ReservationDto> reservationList = null; 
	                    
	                    // 검색 조건 확인
	                    String searchType = request.getParameter("searchType");
	                    String searchValue = request.getParameter("searchValue");
	                    
	                    if (gubun.equals("bookSearch") && 
	                        searchType != null && !searchType.trim().isEmpty() &&
	                        searchValue != null && !searchValue.trim().isEmpty()) {
	                        
	                        // ⭐ 검색 실행 (ReservationDao에 selectReservationsBySearch 메서드가 있다고 가정)
	                        reservationList = dao.selectReservationsBySearch(searchType, searchValue);
	                        request.setAttribute("searchType", searchType); // 검색 유지
	                        request.setAttribute("searchValue", searchValue); // 검색 유지
	                        System.out.println("✅ 관리자 예약 검색 실행: " + searchType + " / " + searchValue);
	                    } else {
	                        // ⭐ 전체 목록 조회 실행 (bookList 또는 검색 조건이 없는 경우)
	                        reservationList = dao.selectAllReservations(); 
	                        System.out.println("✅ 관리자 전체 예약 목록 조회 실행");
	                    }
	                    
	                    request.setAttribute("reservationList", reservationList);
	                    viewPage = "checkmanager/m_check_list.jsp"; 
	                    
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    request.setAttribute("t_msg", "예약 목록 조회 중 오류가 발생했습니다.");
	                    request.setAttribute("t_url", "Index"); // 메인으로
	                    viewPage = "common_alert.jsp"; 
	                }
	            } else { // 관리자 권한 없는 경우
	                request.setAttribute("t_msg", "관리자 권한이 필요합니다.");
	                request.setAttribute("t_url", "Index");
	                viewPage = "common_alert.jsp"; 
	            }
	        } 
			
		} 
		
		request.setAttribute("menuOn", gubun);
		
		RequestDispatcher rd = request.getRequestDispatcher(viewPage);
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}