package controller;

import java.io.IOException;

import javax.print.attribute.standard.OrientationRequested;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import command.manager.QnaListAdmin;
import command.member.ChangePasswordEmail;
import command.member.ChangeTempPassword;
import command.member.GetMembershipInfo;
import command.member.MemberExit;
import command.member.MemberLogin;
import command.member.MemberLogout;
import command.member.MemberQna;
import command.member.MemberSave;
import command.member.MemberUpdate;
import command.member.MemberView;
import command.member.changePassword;
import command.qna.QnaView;
import common.CommonExecute;
import common.CommonUtil;
import common.ReservationEmailService;

/**
 * Servlet implementation class Member
 */
@WebServlet("/Member")
public class Member extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Member() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		String gubun = request.getParameter("t_gubun");
		
		String id = CommonUtil.getSessionInfo(request, "id");
		if(!id.equals("") && gubun == null) {
			gubun = "myInfo";
		} else if(gubun == null) {
			gubun = "loginForm";
		}
		String viewPage = "";
		
		
		// 로그인 화면
		if(gubun.equals("loginForm")) {
			viewPage = "member/member_login.jsp";
		
		// 로그인
		} else if(gubun.equals("login")) {
			CommonExecute mem = new MemberLogin();
			mem.execute(request);
			viewPage = "common_alert.jsp";
		
		// 로그아웃
		} else if(gubun.equals("logout")) {
			CommonExecute mem = new MemberLogout();
			mem.execute(request);
			viewPage = "common_alert.jsp";
		
		// 마이페이지
		} else if(gubun.equals("myInfo")) {
		    System.out.println("========== myInfo ==========");
		    
		    // 회원 기본 정보 조회
		    CommonExecute mem = new MemberView();
		    mem.execute(request);
		    
		    // 멤버십 정보 조회 
		    CommonExecute membership = new GetMembershipInfo();
		    membership.execute(request);
		    
		    viewPage = "member/member_myInfo.jsp";

		// 회원가입폼
		} else if(gubun.equals("join")) {
			viewPage = "member/member_join.jsp";
		
		// 멤버 등록
		} else if(gubun.equals("save")) {
			CommonExecute mem = new MemberSave();
			mem.execute(request);
			viewPage = "common_alert_view.jsp";
		
		// 회원 수정
		} else if(gubun.equals("update")) {
			CommonExecute mem = new MemberUpdate();
			mem.execute(request);
			viewPage = "common_alert_view.jsp";
		
		// 회원 탈퇴
		} else if(gubun.equals("exit")) {
			CommonExecute mem = new MemberExit();
			mem.execute(request);
			viewPage = "common_alert_view.jsp";
		
		// 비밀번호 변경폼
		} else if(gubun.equals("password")) {
			CommonExecute mem = new MemberView();
			mem.execute(request);
			viewPage = "member/member_changePassword.jsp";
		
		// 비밀번호 변경
		} else if(gubun.equals("changePassword")) {
			CommonExecute mem = new changePassword();
			mem.execute(request);
			viewPage = "common_alert_view.jsp";
		
		// ID 찾기
		} else if(gubun.equals("searchIdForm")) {
			viewPage = "member/member_search_id.jsp";
		
		// PW 찾기
		} else if(gubun.equals("searchPwForm")) {
			viewPage = "member/member_search_pw.jsp";

		// 임시 비밀번호로 변경
		} else if(gubun.equals("tempPassword")){
			CommonExecute mem = new ChangeTempPassword();
			mem.execute(request);
			viewPage = "common_alert_view.jsp";
		
		// Q&A 목록
		}  else if(gubun.equals("qnaList")) {
			CommonExecute mana = new MemberQna();
			mana.execute(request);
			viewPage = "member/member_qna_list.jsp";
		
		// Q&A 상세보기
		}  else if(gubun.equals("qnaView")) {
			CommonExecute mana = new QnaView();
			mana.execute(request);
			viewPage = "member/member_qna_view.jsp";
			
		// 예약확인
		} else if(gubun.equals("checkPage")) {
            viewPage = "check/reservation_check.jsp";
            
        }
		
		
		request.setAttribute("menuOn", gubun);
		
		RequestDispatcher rd = 
				request.getRequestDispatcher(viewPage);
		rd.forward(request, response);
				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
