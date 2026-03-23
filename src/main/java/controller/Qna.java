package controller;

import java.io.IOException;

import javax.print.attribute.standard.OrientationRequested;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import command.qna.QnaAnswer;
import command.qna.QnaDelete;
import command.qna.QnaList;
import command.qna.QnaSave;
import command.qna.QnaUpdate;
import command.qna.QnaView;
import common.CommonExecute;
import common.CommonUtil;

/**
 * Servlet implementation class Qna
 */
@WebServlet("/Qna")
public class Qna extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Qna() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String gubun = request.getParameter("t_gubun");
		if(gubun == null) gubun = "qnaList";
		String viewPage = "";
		
		// QnA 등록폼
		if(gubun.equals("qnaWriteForm")){
			request.setAttribute("toDay", CommonUtil.getToday());
			viewPage = "qna/qna_write.jsp";
		
		// QnA 등록
		} else if(gubun.equals("save")) {
			CommonExecute qna = new QnaSave();
			qna.execute(request);
			viewPage = "common_alert_view.jsp";
		
		// QnA 리스트
		} else if(gubun.equals("qnaList")) {
			CommonExecute qna = new QnaList();
			qna.execute(request);
			viewPage = "qna/qna_list.jsp";
		
		// QnA 상세보기
		} else if(gubun.equals("qnaView")) {
			CommonExecute qna = new QnaView();
			qna.execute(request);
			viewPage = "qna/qna_view.jsp";
		
		// QnA 삭제
		} else if(gubun.equals("delete")) {
			CommonExecute qna = new QnaDelete();
			qna.execute(request);
			viewPage = "common_alert_view.jsp";
		
		// QnA 수정 [작성자]
		} else if(gubun.equals("updateQuestion")) {
			CommonExecute qna = new QnaUpdate();
			qna.execute(request);
			viewPage = "common_alert_view.jsp";
		
		// 답변 저장 및 수정 [관리자]
		} else if(gubun.equals("updateAnswer")) {
			CommonExecute qna = new QnaAnswer();
			qna.execute(request);
			viewPage = "common_alert_view.jsp";
		} 
		
		
		request.setAttribute("menuOn", gubun);
		request.setAttribute("headerOn", "qna");
		
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
