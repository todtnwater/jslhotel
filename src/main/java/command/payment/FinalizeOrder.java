package command.payment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import common.CommonExecute;
import common.CommonUtil;
import common.ReservationEmailService;
import dao.MemberDao;
import dao.PaymentDao;
import dao.PointDao;
import dao.ReservationDao;
import dto.PaymentDto;
import dto.PointDto;
import dto.ReservationDto;

public class FinalizeOrder implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        
        try {
            JsonObject jsonObj = (JsonObject) request.getAttribute("jsonObject");
            HttpSession session = request.getSession();
            
            String orderId = jsonObj.get("orderId").getAsString();
            String paymentKey = jsonObj.get("paymentKey").getAsString();
            String paymentMethod = jsonObj.get("paymentMethod").getAsString();
            String paymentStatus = jsonObj.get("paymentStatus").getAsString();
            int amount = jsonObj.get("amount").getAsInt();
            
            String customerName = jsonObj.has("customerName") ? jsonObj.get("customerName").getAsString() : "";
            String customerEmail = jsonObj.has("customerEmail") ? jsonObj.get("customerEmail").getAsString() : "";
            String customerPhone = jsonObj.has("customerPhone") ? jsonObj.get("customerPhone").getAsString() : "";
            
            System.out.println("========== 예약 확정 시작 ==========");
            System.out.println("orderId: " + orderId);
            System.out.println("customerName: " + customerName);
            System.out.println("customerEmail: " + customerEmail);
            System.out.println("customerPhone: " + customerPhone);
            
            ReservationDto tempReservation = (ReservationDto) session.getAttribute("tempReservation_" + orderId);
            
            if (tempReservation == null) {
                throw new Exception("주문 정보를 찾을 수 없습니다. (세션 만료)");
            }
            /*
            String memberId = (String) session.getAttribute("memberId_" + orderId);
            if (memberId == null || memberId.trim().isEmpty()) {
                memberId = "";
            }
            */
            
            String memberId = CommonUtil.getSessionInfo(request, "id");
            if(memberId == null || memberId.equals("")) memberId = "###GUEST###";
            
            String country = (String) session.getAttribute("country_" + orderId);
            if (country == null || country.trim().isEmpty()) {
                country = "korea";
            }
            
            if (customerName == null || customerName.trim().isEmpty()) {
                customerName = "Guest User";
            }
            if (customerEmail == null || customerEmail.trim().isEmpty()) {
                customerEmail = "guest@jslhotel.com";
            }
            if (customerPhone == null || customerPhone.trim().isEmpty()) {
                customerPhone = "010-0000-0000";
            }
            
            System.out.println("========== 예약자 정보 ==========");
            System.out.println("memberId: " + memberId);
            System.out.println("customerName: " + customerName);
            System.out.println("customerEmail: " + customerEmail);
            System.out.println("customerPhone: " + customerPhone);
            System.out.println("country: " + country);
            
            String[] nameParts = customerName.split(" ", 2);
            String firstName = nameParts.length > 0 ? nameParts[0].trim() : "Guest";
            String lastName = nameParts.length > 1 ? nameParts[1].trim() : "User";
            
            String[] emailParts = customerEmail.split("@", 2);
            String email1 = emailParts.length > 0 ? emailParts[0].trim() : "guest";
            String email2 = emailParts.length > 1 ? emailParts[1].trim() : "jslhotel.com";
            
            String[] phoneParts = customerPhone.split("-", 3);
            String mobile1 = phoneParts.length > 0 ? phoneParts[0].trim() : "010";
            String mobile2 = phoneParts.length > 1 ? phoneParts[1].trim() : "0000";
            String mobile3 = phoneParts.length > 2 ? phoneParts[2].trim() : "0000";
            
            System.out.println("========== 예약자 정보 (분리) ==========");
            System.out.println("firstName: " + firstName);
            System.out.println("lastName: " + lastName);
            System.out.println("email1: " + email1);
            System.out.println("email2: " + email2);
            System.out.println("mobile1: " + mobile1);
            System.out.println("mobile2: " + mobile2);
            System.out.println("mobile3: " + mobile3);
            
            String rvNo = "RV" + System.currentTimeMillis();

            // ⭐ 세션에서 사용 포인트 가져오기
            Integer usePoints = (Integer) session.getAttribute("usePoints_" + orderId);
            if (usePoints == null) usePoints = 0;

            int originalAmount = amount;
            int discountAmount = usePoints;
            int finalAmount = amount; // 실제 결제된 금액 (이미 포인트 차감됨)
            String membershipLevel = CommonUtil.getSessionInfo(request, "membership");
            if(membershipLevel == null || membershipLevel.equals("")) membershipLevel = "bronze";

            System.out.println("========== 포인트 사용 정보 ==========");
            System.out.println("원래 금액: " + originalAmount + "원");
            System.out.println("사용 포인트: " + usePoints + "P");
            System.out.println("할인 금액: " + discountAmount + "원");
            System.out.println("최종 결제 금액: " + finalAmount + "원");
            System.out.println("==================================");
            
            ReservationDto reservationDto = new ReservationDto();
            reservationDto.setRv_no(rvNo);
            reservationDto.setRv_room_no(tempReservation.getRv_room_no());
            reservationDto.setRv_member_id(memberId);
            reservationDto.setRv_check_in_date(tempReservation.getRv_check_in_date());
            reservationDto.setRv_check_out_date(tempReservation.getRv_check_out_date());
            reservationDto.setRv_guest_count(tempReservation.getRv_guest_count());
            reservationDto.setRv_total_price(finalAmount);
            reservationDto.setRv_status("CONFIRMED");
            reservationDto.setRv_request_message(tempReservation.getRv_request_message() != null ? 
                                                 tempReservation.getRv_request_message() : "");
            
            reservationDto.setRv_customer_first_name(firstName);
            reservationDto.setRv_customer_last_name(lastName);
            reservationDto.setRv_customer_email_1(email1);
            reservationDto.setRv_customer_email_2(email2);
            reservationDto.setRv_customer_mobile_1(mobile1);
            reservationDto.setRv_customer_mobile_2(mobile2);
            reservationDto.setRv_customer_mobile_3(mobile3);
            
            reservationDto.setRv_original_price(originalAmount);
            reservationDto.setRv_discount_amount(discountAmount);
            reservationDto.setRv_membership_level(membershipLevel);
            reservationDto.setRv_country(country);
            reservationDto.setOrder_id(orderId);
            
            System.out.println("========== DB 저장 시도 ==========");
            System.out.println("RV_NO: " + reservationDto.getRv_no());
            System.out.println("RV_ROOM_NO: " + reservationDto.getRv_room_no());
            System.out.println("RV_MEMBER_ID: " + reservationDto.getRv_member_id());
            System.out.println("RV_CHECK_IN_DATE: " + reservationDto.getRv_check_in_date());
            System.out.println("RV_CHECK_OUT_DATE: " + reservationDto.getRv_check_out_date());
            System.out.println("RV_GUEST_COUNT: " + reservationDto.getRv_guest_count());
            System.out.println("RV_TOTAL_PRICE: " + reservationDto.getRv_total_price());
            System.out.println("RV_STATUS: " + reservationDto.getRv_status());
            System.out.println("RV_CUSTOMER_FIRST_NAME: " + reservationDto.getRv_customer_first_name());
            System.out.println("RV_CUSTOMER_LAST_NAME: " + reservationDto.getRv_customer_last_name());
            System.out.println("ORDER_ID: " + reservationDto.getOrder_id());
            System.out.println("컨트리: " + reservationDto.getRv_country());
            System.out.println("멤버쉽: " + reservationDto.getRv_membership_level());
            System.out.println("==================================");
            
            ReservationDao reservationDao = new ReservationDao();
            int rvResult = reservationDao.insertReservation(reservationDto);
            
            if(rvResult == 0) {
            	throw new Exception("예약 저장 실패");
            }
            
            System.out.println("✅ 예약 저장 완료 - RV_NO: " + rvNo);
            
            String pNo = "P" + System.currentTimeMillis();
            
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setP_no(pNo);
            paymentDto.setP_reservation_no(rvNo);
            paymentDto.setP_amount(finalAmount);
            paymentDto.setP_method(paymentMethod);
            paymentDto.setP_status(paymentStatus);
            paymentDto.setPayment_key(paymentKey);
            paymentDto.setOrder_id(orderId);
            
            PaymentDao paymentDao = new PaymentDao();
            int payResult = paymentDao.insertPayment(paymentDto);
            
            if(payResult == 0) {
                throw new Exception("결제 저장 실패");
            }
            

         // 결제 저장 완료 후
            System.out.println("✅ 결제 저장 완료 - P_NO: " + pNo);

            // ⭐⭐⭐ 포인트 사용 (차감) ⭐⭐⭐
            if (usePoints > 0 && memberId != null && !memberId.equals("###GUEST###")) {
                MemberDao memberDao = new MemberDao();

                // 포인트 잔액 확인
                int currentPoint = memberDao.getMemberPoint(memberId);

                if (currentPoint < usePoints) {
                    throw new Exception("포인트 부족 - 보유: " + currentPoint + "P, 사용: " + usePoints + "P");
                }

                // 포인트 차감
                int useResult = memberDao.useMemberPoint(memberId, usePoints);

                if (useResult == 0) {
                    throw new Exception("포인트 차감 실패");
                }

                System.out.println("✅ 포인트 사용 완료 - " + memberId + ": -" + usePoints + "P");

                // 포인트 사용 내역 기록 (PointDao 사용)
                try {
                    PointDao pointDao = new PointDao();
                    PointDto usePointDto = new PointDto();
                    usePointDto.setPt_member_id(memberId);
                    usePointDto.setPt_amount(usePoints);
                    usePointDto.setPt_source("RESERVATION");
                    usePointDto.setPt_source_no(rvNo);
                    usePointDto.setPt_description("예약 시 포인트 사용 (예약번호: " + rvNo + ")");

                    int pointHistoryResult = pointDao.usePoint(usePointDto);

                    if (pointHistoryResult > 0) {
                        System.out.println("✅ 포인트 사용 내역 저장 완료");
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ 포인트 사용 내역 저장 실패: " + e.getMessage());
                    e.printStackTrace();
                }

                // 세션 포인트 정보 갱신
                session.setAttribute("totalPoints", memberDao.getMemberPoint(memberId));
            }

            // ⭐⭐⭐ 포인트 적립 ⭐⭐⭐
            if (memberId != null && !memberId.equals("###GUEST###")) {
                MemberDao memberDao = new MemberDao();
                int earnPoints = memberDao.calculateEarnPoints(membershipLevel, finalAmount);

                if (earnPoints > 0) {
                    int earnResult = memberDao.addMemberPoint(memberId, earnPoints);

                    if (earnResult > 0) {
                        System.out.println("✅ 포인트 적립 완료 - " + memberId + ": +" + earnPoints + "P");

                        // 포인트 적립 내역 기록 (PointDao 사용)
                        try {
                            PointDao pointDao = new PointDao();
                            PointDto earnPointDto = new PointDto();
                            earnPointDto.setPt_member_id(memberId);
                            earnPointDto.setPt_amount(earnPoints);
                            earnPointDto.setPt_source("RESERVATION");
                            earnPointDto.setPt_source_no(rvNo);
                            earnPointDto.setPt_description("예약 완료 적립 (예약번호: " + rvNo + ")");

                            int pointHistoryResult = pointDao.earnPoint(earnPointDto);

                            if (pointHistoryResult > 0) {
                                System.out.println("✅ 포인트 적립 내역 저장 완료");
                            }
                        } catch (Exception e) {
                            System.out.println("⚠️ 포인트 적립 내역 저장 실패: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    // 세션 포인트 정보 갱신
                    session.setAttribute("totalPoints", memberDao.getMemberPoint(memberId));
                }
            }
            try {
                ReservationEmailService emailService = new ReservationEmailService();
                boolean emailSent = emailService.sendReservationConfirmEmail(reservationDto);
                
                if (emailSent) {
                    System.out.println("✅ 예약 확인 이메일 발송 완료");
                } else {
                    System.out.println("⚠️ 이메일 발송 실패 (예약은 정상 완료됨)");
                }
            } catch (Exception e) {
                System.out.println("⚠️ 이메일 발송 중 오류 (예약은 정상 완료됨): " + e.getMessage());
                e.printStackTrace();
            }
            
            session.removeAttribute("tempReservation_" + orderId);
            session.removeAttribute("customerName_" + orderId);
            session.removeAttribute("customerEmail_" + orderId);
            session.removeAttribute("customerMobilePhone_" + orderId);
            session.removeAttribute("country_" + orderId);
            session.removeAttribute("breakfastOption_" + orderId);
            session.removeAttribute("usePoints_" + orderId);
            session.removeAttribute("memberRank_" + orderId);
            session.removeAttribute("isLoggedIn_" + orderId);
            session.removeAttribute("memberId_" + orderId);
            
            System.out.println("✅ 예약 확정 완료 - RV_NO: " + rvNo);
            
            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("message", "예약이 완료되었습니다.");
            
            JsonObject data = new JsonObject();
            data.addProperty("reservationNo", rvNo);
            data.addProperty("paymentNo", pNo);
            data.addProperty("roomNo", tempReservation.getRv_room_no());
            data.addProperty("checkIn", tempReservation.getRv_check_in_date());
            data.addProperty("checkOut", tempReservation.getRv_check_out_date());
            data.addProperty("totalAmount", finalAmount);
            data.addProperty("originalAmount", originalAmount);
            data.addProperty("customerName", customerName);
            data.addProperty("customerEmail", customerEmail);
            
            response.add("data", data);
            
            request.setAttribute("jsonResult", response.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
            
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("success", false);
            errorResponse.addProperty("message", "예약 확정 실패: " + e.getMessage());
            
            request.setAttribute("jsonResult", errorResponse.toString());
        }
    }
}