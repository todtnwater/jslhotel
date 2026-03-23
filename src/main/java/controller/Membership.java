package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import command.membership.CreateTempMembership;
import command.membership.GetTempMembership;
import common.CommonExecute;
import common.CommonUtil;
import common.JsonHelper;

/**
 * Servlet implementation class Membership
 */
@WebServlet("/Membership")
public class Membership extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Membership() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		// 세션 정보 설정 (JSP에서 사용 가능)
		setSessionAttributes(request);
		
		String gubun = request.getParameter("t_gubun");
		if(gubun == null || gubun.equals("")) gubun = "membership";
		String viewPage = "";
		
		System.out.println("========== Membership Servlet GET ==========");
		System.out.println("gubun: " + gubun);
		
		if(gubun.equals("membershipList")) {
			viewPage = "membership/membership_card.jsp";
		
		} else if(gubun.equals("membership")) {
			viewPage = "membership/membership_main.jsp";
		
		} else if(gubun.equals("point")) {
			viewPage = "membership/membership_point.jsp";
		
		} else if(gubun.equals("gold")) {
			String id = CommonUtil.getSessionInfo(request, "id");
			gubun = "membershipList";
			request.setAttribute("t_id", id);
			viewPage = "membership/membership_gold.jsp";
		
		} else if(gubun.equals("premium")) {
			String id = CommonUtil.getSessionInfo(request, "id");
			gubun = "membershipList";
			request.setAttribute("t_id", id);
			viewPage = "membership/membership_premium.jsp";
		
		// 임시 멤버십 조회 (AJAX)
		} else if(gubun.equals("getTempMembership")) {
			CommonExecute cmd = new GetTempMembership();
			cmd.execute(request);
			JsonHelper.sendJsonResponse(response, (String) request.getAttribute("jsonResult"));
			return;
		
		// Payment로 리다이렉트
		} else if(gubun.equals("payment")) {
			String orderId = request.getParameter("orderId");
			
			System.out.println("========== payment 리다이렉트 ==========");
			System.out.println("orderId: " + orderId);
			
			if(orderId == null || orderId.equals("")) {
				System.out.println("❌ orderId가 없습니다!");
				response.sendRedirect("Membership?t_gubun=membershipList");
				return;
			}
			
			System.out.println("✅ Payment로 리다이렉트: Payment?t_gubun=membershipPayment&orderId=" + orderId);
			response.sendRedirect("Payment?t_gubun=membershipPayment&orderId=" + orderId);
			return;
		}
		
		request.setAttribute("menuOn", gubun);
		request.setAttribute("headerOn", "membership");
		
		RequestDispatcher rd = request.getRequestDispatcher(viewPage);
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		System.out.println("========== Membership POST ==========");
		System.out.println("Content-Type: " + request.getContentType());
		
		if (JsonHelper.isJsonRequest(request)) {
			try {
				setSessionAttributes(request);
				request.setAttribute("jsonObject", JsonHelper.readJsonBody(request));
				
				System.out.println("✅ JSON 요청 확인 - CreateTempMembership 실행");
				
				CommonExecute cmd = new CreateTempMembership();
				cmd.execute(request);
				
				String jsonResult = (String) request.getAttribute("jsonResult");
				System.out.println("✅ JSON 결과: " + jsonResult);
				
				JsonHelper.sendJsonResponse(response, jsonResult);
				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("❌ JSON 처리 중 오류: " + e.getMessage());
				JsonHelper.sendErrorJson(response, "JSON 처리 중 오류: " + e.getMessage());
			}
		} else {
			doGet(request, response);
		}
	}
	
	/**
	 * 세션 정보를 request에 설정
	 */
	private void setSessionAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession();
		request.setAttribute("sessionId", session.getAttribute("sessionId"));
		request.setAttribute("sessionName", session.getAttribute("sessionName"));
		request.setAttribute("sessionLevel", session.getAttribute("sessionLevel"));
		request.setAttribute("sessionPosition", session.getAttribute("sessionPosition"));
		request.setAttribute("sessionMembership", session.getAttribute("sessionMembership"));
	}
}
