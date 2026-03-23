package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import command.check.CheckReservation;
import command.check.ViewReservationDetail;
import common.CommonExecute;
import common.CommonUtil;
import dao.ReservationDao;
import dto.ReservationDto;

@WebServlet("/Check")
public class Check extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public Check() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        
        // ⭐⭐⭐ 세션 정보 설정 (JSP에서 사용 가능 - 필요에 따라 유지/삭제) ⭐⭐⭐
        // 참고: JSP에서 session.getAttribute("sessionId") 등으로 직접 접근 가능합니다.
        HttpSession session = request.getSession();
        request.setAttribute("sessionId", session.getAttribute("sessionId"));
        request.setAttribute("sessionName", session.getAttribute("sessionName"));
        request.setAttribute("sessionLevel", session.getAttribute("sessionLevel"));
        request.setAttribute("sessionPosition", session.getAttribute("sessionPosition"));
        request.setAttribute("sessionMembership", session.getAttribute("sessionMembership"));
        
        String gubun = request.getParameter("t_gubun");
        if(gubun == null || gubun.equals("")) gubun = "checkPage";
        String viewPage = "";
        
        System.out.println("========== Check Servlet ==========");
        System.out.println("gubun: " + gubun);
        System.out.println("sessionId: " + session.getAttribute("sessionId"));
        System.out.println("sessionName: " + session.getAttribute("sessionName"));
        System.out.println("===================================");
        
        // =======================================================
        // 요청 처리 로직 (t_gubun에 따른 분기)
        // 기존 코드의 managerList 로직을 이 체인으로 이동하고, 
        // 불필요한 중괄호(})를 제거했습니다.
        // =======================================================
        if(gubun.equals("checkPage")) {
            viewPage = "check/reservation_check.jsp";
            
        } else if(gubun.equals("checkReservation")) {
            CommonExecute check = new CheckReservation();
            check.execute(request);
            viewPage = (String) request.getAttribute("viewPage");
            
        } else if(gubun.equals("viewDetail")) {
            CommonExecute check = new ViewReservationDetail();
            check.execute(request);
            viewPage = (String) request.getAttribute("viewPage");
            
        }

        request.setAttribute("menuOn", gubun);
		request.setAttribute("headerOn", "check"); //
		
		RequestDispatcher rd = 
				request.getRequestDispatcher(viewPage);
		rd.forward(request, response);	
	}
            
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}