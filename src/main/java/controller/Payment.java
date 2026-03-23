package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import command.membership.FinalizeMembership;
import command.membership.GetTempMembership;
import command.payment.ConfirmPayment;
import command.payment.FinalizeOrder;
import command.payment.GetTempReservation;
import common.CommonExecute;
import common.JsonHelper;
import dto.ReservationDto;

@WebServlet("/Payment")
public class Payment extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public Payment() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        
        setSessionAttributes(request);
        
        String gubun = request.getParameter("t_gubun");
        if(gubun == null || gubun.equals("")) gubun = "payment";
        String viewPage = "";
        
        System.out.println("========== Payment Servlet GET ==========");
        System.out.println("gubun: " + gubun);
        
        if (JsonHelper.isJsonRequest(request)) {
            handleJsonRequest(request, response);
            return;
        }
        
        // 호텔 예약 임시 정보 조회
        if(gubun.equals("getTempReservation")) {
            CommonExecute cmd = new GetTempReservation();
            cmd.execute(request);
            JsonHelper.sendJsonResponse(response, (String) request.getAttribute("jsonResult"));
            return;
        }
        
        // 멤버십 임시 정보 조회
        if(gubun.equals("getTempMembership")) {
            CommonExecute cmd = new GetTempMembership();
            cmd.execute(request);
            JsonHelper.sendJsonResponse(response, (String) request.getAttribute("jsonResult"));
            return;
        }
        
        // 호텔 예약 결제 페이지
        if(gubun.equals("payment")) {
            HttpSession session = request.getSession();
            String orderId = request.getParameter("orderId");
            
            System.out.println("========== payment 페이지 로딩 ==========");
            System.out.println("orderId: " + orderId);
            
            if(orderId == null || orderId.equals("")) {
                System.out.println("❌ orderId가 없습니다!");
                request.setAttribute("t_msg", "주문 정보를 찾을 수 없습니다.");
                request.setAttribute("t_url", "Reservation?t_gubun=reservation");
                viewPage = "common/t_alert.jsp";
            } else {
                ReservationDto tempReservation = (ReservationDto) session.getAttribute("tempReservation_" + orderId);
                
                System.out.println(">>> 세션에서 임시 예약 조회:");
                System.out.println("  tempReservation: " + (tempReservation != null ? "존재" : "null"));
                
                if(tempReservation == null) {
                    System.out.println("❌ 세션에 임시 예약 정보가 없습니다!");
                    request.setAttribute("t_msg", "예약 정보를 찾을 수 없습니다. (세션 만료)");
                    request.setAttribute("t_url", "Reservation?t_gubun=reservation");
                    viewPage = "common/t_alert.jsp";
                } else {
                    System.out.println("✅ 임시 예약 정보 확인 완료");
                    System.out.println("  ROOM_NO: " + tempReservation.getRv_room_no());
                    System.out.println("  CHECK_IN: " + tempReservation.getRv_check_in_date());
                    System.out.println("  CHECK_OUT: " + tempReservation.getRv_check_out_date());
                    System.out.println("  TOTAL_PRICE: " + tempReservation.getRv_total_price());
                    
                    request.setAttribute("orderId", orderId);
                    viewPage = "payment/payment.jsp";
                }
            }
        
        // 멤버십 결제 페이지
        } else if(gubun.equals("membershipPayment")) {
            HttpSession session = request.getSession();
            String orderId = request.getParameter("orderId");
            
            System.out.println("========== membershipPayment 페이지 로딩 ==========");
            System.out.println("orderId: " + orderId);
            
            if(orderId == null || orderId.equals("")) {
                System.out.println("❌ orderId가 없습니다!");
                request.setAttribute("t_msg", "주문 정보를 찾을 수 없습니다.");
                request.setAttribute("t_url", "Membership?t_gubun=membershipList");
                viewPage = "common/t_alert.jsp";
            } else {
                String membershipGrade = (String) session.getAttribute("tempMembership_" + orderId);
                
                System.out.println(">>> 세션에서 임시 멤버십 조회:");
                System.out.println("  membershipGrade: " + membershipGrade);
                
                if(membershipGrade == null) {
                    System.out.println("❌ 세션에 임시 멤버십 정보가 없습니다!");
                    request.setAttribute("t_msg", "멤버십 정보를 찾을 수 없습니다. (세션 만료)");
                    request.setAttribute("t_url", "Membership?t_gubun=membershipList");
                    viewPage = "common/t_alert.jsp";
                } else {
                    System.out.println("✅ 임시 멤버십 정보 확인 완료");
                    
                    request.setAttribute("orderId", orderId);
                    request.setAttribute("paymentType", "membership");
                    viewPage = "payment/membership_payment.jsp";
                }
            }
        
        } else if(gubun.equals("paymentSuccess")) {
            viewPage = "payment/payment_success.jsp";
        
        } else if(gubun.equals("paymentFail")) {
            viewPage = "payment/payment_fail.jsp";
        
        } else if(gubun.equals("membershipPaymentSuccess")) {
            viewPage = "payment/membership_payment_success.jsp";
        
        } else if(gubun.equals("membershipPaymentFail")) {
            viewPage = "payment/payment_fail.jsp";
        
        } else {
            viewPage = "payment/payment.jsp";
        }
        
        request.setAttribute("menuOn", gubun);
        RequestDispatcher rd = request.getRequestDispatcher(viewPage);
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
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
    
    private void handleJsonRequest(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        try {
            setSessionAttributes(request);
            
            request.setAttribute("jsonObject", JsonHelper.readJsonBody(request));
            
            CommonExecute cmd;
            
            if (isConfirmPaymentRequest(request)) {
                cmd = new ConfirmPayment();
            } else if (isMembershipRequest(request)) {
                cmd = new FinalizeMembership();
            } else {
                cmd = new FinalizeOrder();
            }
            
            cmd.execute(request);
            JsonHelper.sendJsonResponse(response, (String) request.getAttribute("jsonResult"));
            
        } catch (Exception e) {
            JsonHelper.sendErrorJson(response, "JSON 처리 중 오류: " + e.getMessage());
        }
    }
    
    private boolean isConfirmPaymentRequest(HttpServletRequest request) {
        try {
            com.google.gson.JsonObject jsonObj = (com.google.gson.JsonObject) request.getAttribute("jsonObject");
            return !jsonObj.has("paymentMethod");
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean isMembershipRequest(HttpServletRequest request) {
        try {
            com.google.gson.JsonObject jsonObj = (com.google.gson.JsonObject) request.getAttribute("jsonObject");
            String orderId = jsonObj.get("orderId").getAsString();
            return orderId.startsWith("MEMBERSHIP_");
        } catch (Exception e) {
            return false;
        }
    }
}
