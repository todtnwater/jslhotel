package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import command.notice.NoticePopup;  // 추가

@WebServlet("/Index")
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Index() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		HttpSession session = request.getSession();
		request.setAttribute("sessionId", session.getAttribute("sessionId"));
		request.setAttribute("sessionName", session.getAttribute("sessionName"));
		request.setAttribute("sessionLevel", session.getAttribute("sessionLevel"));
		request.setAttribute("sessionPosition", session.getAttribute("sessionPosition"));
		request.setAttribute("sessionMembership", session.getAttribute("sessionMembership"));
		
		String gubun = request.getParameter("t_gubun");
		if (gubun == null) gubun = "index";
		String viewPage = "";
		
		if(gubun.equals("index")){
			// ⭐⭐⭐ 팝업 공지사항 조회 추가 ⭐⭐⭐
			NoticePopup popupCommand = new NoticePopup();
			popupCommand.execute(request);
			
			// 디버깅용 (나중에 삭제)
			dto.NoticeDto popup = (dto.NoticeDto)request.getAttribute("popupNotice");
			if(popup != null) {
				System.out.println("=== 팝업 공지 조회 성공 ===");
				System.out.println("번호: " + popup.getN_no());
				System.out.println("제목: " + popup.getN_title());
				System.out.println("기간: " + popup.getPopupStartDate() + " ~ " + popup.getPopupEndDate());
			} else {
				System.out.println("=== 팝업 공지 없음 ===");
			}
			
			viewPage = "index.jsp";
		}
		
		RequestDispatcher rd = request.getRequestDispatcher(viewPage);
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}