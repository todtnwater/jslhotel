package command.reservation;

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
            
            if (orderId == null || orderId.equals("")) {
                throw new Exception("주문번호가 없습니다.");
            }
            
            System.out.println("========== 임시 예약 조회 ==========");
            System.out.println("orderId: " + orderId);
            
            HttpSession session = request.getSession();
            ReservationDto tempReservation = (ReservationDto) session.getAttribute("tempReservation_" + orderId);
            
            if (tempReservation == null) {
                throw new Exception("예약 정보를 찾을 수 없습니다. (세션 만료)");
            }
            
            String customerName = (String) session.getAttribute("customerName_" + orderId);
            String customerEmail = (String) session.getAttribute("customerEmail_" + orderId);
            String customerMobilePhone = (String) session.getAttribute("customerMobilePhone_" + orderId);
            String breakfastOption = (String) session.getAttribute("breakfastOption_" + orderId);
            
            if (customerName == null) customerName = "";
            if (customerEmail == null) customerEmail = "";
            if (customerMobilePhone == null) customerMobilePhone = "";
            if (breakfastOption == null) breakfastOption = "none";
            
            System.out.println("예약 정보 조회 성공");
            System.out.println("roomNo: " + tempReservation.getRv_room_no());
            System.out.println("customerName: " + customerName);
            System.out.println("customerEmail: " + customerEmail);
            System.out.println("===================================");
            
            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            
            JsonObject data = new JsonObject();
            data.addProperty("orderId", orderId);
            data.addProperty("roomNo", tempReservation.getRv_room_no());
            data.addProperty("roomType", "Standard");
            data.addProperty("checkIn", tempReservation.getRv_check_in_date());
            data.addProperty("checkOut", tempReservation.getRv_check_out_date());
            data.addProperty("guestCount", tempReservation.getRv_guest_count());
            data.addProperty("requestMessage", tempReservation.getRv_request_message());
            data.addProperty("totalAmount", tempReservation.getRv_total_price());
            
            data.addProperty("customerName", customerName);
            data.addProperty("customerEmail", customerEmail);
            data.addProperty("customerMobilePhone", customerMobilePhone);
            data.addProperty("breakfastOption", breakfastOption);
            
            response.add("data", data);
            
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