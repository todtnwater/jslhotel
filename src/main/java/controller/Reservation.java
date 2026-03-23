package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import command.reservation.CreateTempReservation;
import command.reservation.GetTempReservation;
import command.reservation.GoRoomView;
import command.reservation.ReservationPage;
import command.reservation.SearchAvailableRooms;
import common.CommonExecute;
import common.JsonHelper;

@WebServlet("/Reservation")
public class Reservation extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public Reservation() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        
        // 세션 정보 설정 (JSP에서 사용 가능)
        HttpSession session = request.getSession();
        request.setAttribute("sessionId", session.getAttribute("sessionId")); 
        request.setAttribute("sessionName", session.getAttribute("sessionName"));
        request.setAttribute("sessionLevel", session.getAttribute("sessionLevel"));
        request.setAttribute("sessionPosition", session.getAttribute("sessionPosition"));
        request.setAttribute("sessionMembership", session.getAttribute("sessionMembership"));
        
        String gubun = request.getParameter("t_gubun");
        if(gubun == null || gubun.equals("")) gubun = "reservation";
        String viewPage = "";
        
        String ajaxHeader = request.getHeader("X-Requested-With");
        boolean isAjaxRequest = "XMLHttpRequest".equals(ajaxHeader);
        
        System.out.println("========== Reservation Servlet ==========");
        System.out.println("gubun: " + gubun);
        System.out.println("isAjaxRequest: " + isAjaxRequest);
        
        if(gubun.equals("reservation")) {
            CommonExecute cmd = new ReservationPage();
            cmd.execute(request);
            viewPage = "reservation/reservation.jsp";
            
        } else if(gubun.equals("searchAvailableRooms")) {
            CommonExecute cmd = new SearchAvailableRooms();
            cmd.execute(request);
            
            if(isAjaxRequest) {
                JsonHelper.sendJsonResponse(response, (String) request.getAttribute("jsonResult"));
                return;
            } else {
                viewPage = "reservation/reservation.jsp";
            }
            
        } else if(gubun.equals("goRoomView")) {
            CommonExecute cmd = new GoRoomView();
            cmd.execute(request);
            viewPage = "reservation/room_view_integrated.jsp";
        
        } else if(gubun.equals("getTempReservation")) {
            CommonExecute cmd = new GetTempReservation();
            cmd.execute(request);
            JsonHelper.sendJsonResponse(response, (String) request.getAttribute("jsonResult"));
            return;
        
        } else if(gubun.equals("payment")) {
            String orderId = request.getParameter("orderId");
            
            System.out.println("========== payment 리다이렉트 ==========");
            System.out.println("orderId: " + orderId);
            
            if(orderId == null || orderId.equals("")) {
                System.out.println("❌ orderId가 없습니다!");
                response.sendRedirect("Reservation?t_gubun=reservation");
                return;
            }
            
            System.out.println("✅ Payment로 리다이렉트: Payment?t_gubun=payment&orderId=" + orderId);
            response.sendRedirect("Payment?t_gubun=payment&orderId=" + orderId);
            return;
        }
        
        request.setAttribute("menuOn", gubun);
        request.setAttribute("headerOn", "room");
        RequestDispatcher rd = request.getRequestDispatcher(viewPage);
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        
        System.out.println("========== Reservation POST ==========");
        System.out.println("Content-Type: " + request.getContentType());
        
        if (JsonHelper.isJsonRequest(request)) {
            try {
                request.setAttribute("jsonObject", JsonHelper.readJsonBody(request));
                
                System.out.println("✅ JSON 요청 확인 - CreateTempReservation 실행");
                
                CommonExecute cmd = new CreateTempReservation();
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
}