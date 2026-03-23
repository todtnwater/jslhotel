package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import command.notice.NoticeDelete;
import command.notice.NoticeList;
import command.notice.NoticeSave;
import command.notice.NoticeUpdate;
import command.notice.NoticeView;
import command.notice.PositionInfo;
import common.CommonExecute;
import common.CommonUtil;

/**
 * Servlet implementation class Notice
 */
@WebServlet("/Notice")
public class Notice extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Notice() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String gubun = request.getParameter("t_gubun");
		if(gubun == null) gubun = "list";
		String viewPage = "";
		
		// 공지 목록
		if(gubun.equals("list")) {
			CommonExecute noti = new NoticeList();
			noti.execute(request);
			viewPage = "notice/notice_list.jsp";
		
		// 공지 등록 폼
		} else if(gubun.equals("writeForm")) {
			CommonExecute noti = new PositionInfo();
			noti.execute(request);
			request.setAttribute("toDay", CommonUtil.getToday());
			viewPage ="notice/notice_write.jsp";
		
		// 공지 등록
		}	else if(gubun.equals("save")) {
			CommonExecute noti = new NoticeSave();
			noti.execute(request);
			viewPage ="common_alert.jsp";
		
		// 공지 상세 보기	
		} 	else if(gubun.equals("view")) {	
			CommonExecute noti = new NoticeView();
			noti.execute(request);
			viewPage = "notice/notice_view.jsp";
			
		// 공지 수정 폼	
		} else if(gubun.equals("updateForm")) {
			CommonExecute noti = new NoticeView();
			noti.execute(request);
			viewPage = "notice/notice_update.jsp";	
			
		// 공지 수정 저장
		} else if(gubun.equals("update")) {
			CommonExecute noti = new NoticeUpdate();
			noti.execute(request);
			viewPage = "common_alert_view.jsp";	
			
		// 공지 삭제
		} else if(gubun.equals("delete")) {
			CommonExecute noti = new NoticeDelete();
			noti.execute(request);
			viewPage = "common_alert.jsp";
		}
		
		request.setAttribute("menuOn", gubun);
		request.setAttribute("headerOn", "notice");
		
		RequestDispatcher rd = request.getRequestDispatcher(viewPage);
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