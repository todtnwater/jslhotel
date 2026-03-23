package command.reservation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import common.CommonExecute;

public class ClearTempReservation implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        
        try {
            String orderId = request.getParameter("orderId");
            
            System.out.println("========== 임시 예약 세션 정리 ==========");
            System.out.println("orderId: " + orderId);
            
            if (orderId == null || orderId.equals("")) {
                throw new Exception("주문번호가 없습니다.");
            }
            
            HttpSession session = request.getSession(false);
            
            if (session != null) {
                // 해당 orderId 관련 세션 모두 삭제
                session.removeAttribute("tempReservation_" + orderId);
                session.removeAttribute("customerName_" + orderId);
                session.removeAttribute("customerEmail_" + orderId);
                session.removeAttribute("customerMobilePhone_" + orderId);
                session.removeAttribute("breakfastOption_" + orderId);
                session.removeAttribute("usePoints_" + orderId);
                session.removeAttribute("memberRank_" + orderId);
                session.removeAttribute("isLoggedIn_" + orderId);
                session.removeAttribute("memberId_" + orderId);
                session.removeAttribute("orderId");
                
                System.out.println("✅ 세션 정리 완료 - orderId: " + orderId);
            } else {
                System.out.println("⚠️ 세션이 존재하지 않음");
            }
            
            System.out.println("===================================");
            
            // 성공 응답
            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("message", "세션 정리 완료");
            
            request.setAttribute("jsonResult", response.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
            
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("success", false);
            errorResponse.addProperty("message", e.getMessage());
            
            request.setAttribute("jsonResult", errorResponse.toString());
        }
    }
}