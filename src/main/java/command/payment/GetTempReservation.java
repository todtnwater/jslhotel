package command.payment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import common.CommonExecute;
import dto.ReservationDto;

public class GetTempReservation implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        
        try {
            String orderId = request.getParameter("orderId");
            
            System.out.println("========== 임시 예약 정보 조회 (Payment) ==========");
            System.out.println("orderId: " + orderId);
            
            // ⭐ 멤버십 결제는 예약 정보 없음 - 정상 응답
            if (orderId != null && orderId.startsWith("MEMBERSHIP_")) {
                System.out.println("ℹ️ 멤버십 결제 - 예약 정보 불필요");
                
                JsonObject response = new JsonObject();
                response.addProperty("success", true);
                response.addProperty("message", "멤버십 결제");
                
                JsonObject data = new JsonObject();
                data.addProperty("type", "membership");
                data.addProperty("orderId", orderId);
                
                response.add("data", data);
                request.setAttribute("jsonResult", response.toString());
                return;
            }
            
            // 일반 객실 예약만 세션에서 조회
            HttpSession session = request.getSession();
            
            ReservationDto tempReservation = (ReservationDto) session.getAttribute("tempReservation_" + orderId);
            String customerName = (String) session.getAttribute("customerName_" + orderId);
            String customerEmail = (String) session.getAttribute("customerEmail_" + orderId);
            String customerMobilePhone = (String) session.getAttribute("customerMobilePhone_" + orderId);
            
            if (tempReservation == null) {
                throw new Exception("임시 예약 정보를 찾을 수 없습니다.");
            }
            
            System.out.println("세션에서 조회한 고객 정보:");
            System.out.println("  customerName: " + customerName);
            System.out.println("  customerEmail: " + customerEmail);
            System.out.println("  customerMobilePhone: " + customerMobilePhone);
            
            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("message", "예약 정보 조회 성공");
            
            JsonObject data = new JsonObject();
            data.addProperty("roomNo", tempReservation.getRv_room_no());
            data.addProperty("checkIn", tempReservation.getRv_check_in_date());
            data.addProperty("checkOut", tempReservation.getRv_check_out_date());
            data.addProperty("guestCount", tempReservation.getRv_guest_count());
            data.addProperty("customerName", customerName != null ? customerName : "Guest User");
            data.addProperty("customerEmail", customerEmail != null ? customerEmail : "guest@jslhotel.com");
            data.addProperty("customerMobilePhone", customerMobilePhone != null ? customerMobilePhone : "010-0000-0000");
            
            response.add("data", data);
            
            request.setAttribute("jsonResult", response.toString());
            
            System.out.println("✅ 임시 예약 정보 조회 완료");
            
        } catch (Exception e) {
            e.printStackTrace();
            
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("success", false);
            errorResponse.addProperty("message", "예약 정보 조회 실패: " + e.getMessage());
            
            request.setAttribute("jsonResult", errorResponse.toString());
        }
    }
}