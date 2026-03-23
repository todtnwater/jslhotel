package command.reservation;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import common.CommonExecute;
import common.CommonUtil;
import dto.ReservationDto;

public class CreateTempReservation implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        
        try {
            JsonObject jsonObj = (JsonObject) request.getAttribute("jsonObject");
            
            if (jsonObj == null) {
                throw new Exception("JSON 데이터를 찾을 수 없습니다.");
            }
            
            System.out.println("========== 임시 예약 생성 시작 ==========");
            System.out.println("받은 JSON: " + jsonObj.toString());
            
            // =========================================================
            // 1. JSON 데이터 파싱
            // =========================================================
            
            // 기본 예약 정보
            String roomNo = jsonObj.get("roomNo").getAsString();
            String checkIn = jsonObj.get("checkIn").getAsString();
            String checkOut = jsonObj.get("checkOut").getAsString();
            int guestCount = jsonObj.get("guestCount").getAsInt();
            String specialRequest = jsonObj.get("specialRequest").getAsString();
            int totalAmount = jsonObj.get("totalAmount").getAsInt();
            
            // 고객 정보
            String customerFullName = jsonObj.has("customerName") ? jsonObj.get("customerName").getAsString() : "";
            String customerEmail = jsonObj.has("customerEmail") ? jsonObj.get("customerEmail").getAsString() : "";
            String customerMobilePhone = jsonObj.has("customerMobilePhone") ? jsonObj.get("customerMobilePhone").getAsString() : "";
            
            // 조식 옵션
            String breakfastOption = jsonObj.has("breakfastOption") ? jsonObj.get("breakfastOption").getAsString() : "none";
            
            // 포인트 사용 정보
            int usePoints = jsonObj.has("usePoints") ? jsonObj.get("usePoints").getAsInt() : 0;
            
            // 회원 정보
            boolean isLoggedIn = jsonObj.has("isLoggedIn") ? jsonObj.get("isLoggedIn").getAsBoolean() : false;
            
            System.out.println(">>> 파싱된 데이터:");
            System.out.println("  roomNo: " + roomNo);
            System.out.println("  checkIn: " + checkIn);
            System.out.println("  checkOut: " + checkOut);
            System.out.println("  guestCount: " + guestCount);
            System.out.println("  customerFullName: " + customerFullName);
            System.out.println("  customerEmail: " + customerEmail);
            System.out.println("  customerMobilePhone: " + customerMobilePhone);
            System.out.println("  totalAmount: " + totalAmount);
            System.out.println("  usePoints: " + usePoints);
            System.out.println("  isLoggedIn: " + isLoggedIn);
            
            // ⭐ 세션에서 회원 ID 가져오기 (오타 수정!)
            HttpSession session = request.getSession();
            
            // ❌ 기존 코드 (오타)
            // String memberId = (String) session.getAttribute("sesssionId");
            // String memberRank = (String) session.getAttribute("sesssionMembership");
            
            // ✅ 수정된 코드 (올바른 속성 이름)
            String memberId = (String) session.getAttribute("sessionId");
            String memberRank = (String) session.getAttribute("sessionMembership");
            
            System.out.println(">>> 세션 정보 (수정됨):");
            System.out.println("  memberId (from sessionId): " + memberId);
            System.out.println("  memberRank (from sessionMembership): " + memberRank);
            
            // ⭐ 추가 검증: CommonUtil로도 확인
            String memberIdFromUtil = CommonUtil.getSessionInfo(request, "id");
            String membershipFromUtil = CommonUtil.getSessionInfo(request, "membership");
            
            System.out.println(">>> CommonUtil로 확인한 세션 정보:");
            System.out.println("  memberId (CommonUtil): " + memberIdFromUtil);
            System.out.println("  membership (CommonUtil): " + membershipFromUtil);
            
            // ⭐ 둘 중 하나라도 값이 있으면 사용
            if (memberId == null || memberId.trim().isEmpty()) {
                memberId = memberIdFromUtil;
            }
            if (memberRank == null || memberRank.trim().isEmpty()) {
                memberRank = membershipFromUtil;
            }
            
            System.out.println(">>> 최종 결정된 회원 정보:");
            System.out.println("  memberId: " + (memberId != null ? memberId : "null"));
            System.out.println("  memberRank: " + (memberRank != null ? memberRank : "null"));
            
            // =========================================================
            // 2. 포인트 사용 검증
            // =========================================================
            
            if (usePoints > 0) {
                if (!isLoggedIn && (memberId == null || memberId.trim().isEmpty())) {
                    throw new Exception("비회원은 포인트를 사용할 수 없습니다.");
                }
                
                if (usePoints < 100) {
                    throw new Exception("포인트는 100P 이상부터 사용 가능합니다.");
                }
                
                // 보유 포인트 확인 (세션에서)
                Integer totalPoints = (Integer) session.getAttribute("totalPoints");
                if (totalPoints == null || totalPoints < usePoints) {
                    throw new Exception("보유 포인트가 부족합니다.");
                }
            }
            
            // =========================================================
            // 3. DTO 생성 및 데이터 설정
            // =========================================================
            
            // 주문 ID 생성 (결제 Key로 사용될 수 있음)
            String orderId = "ORDER_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
            
            System.out.println(">>> 생성된 orderId: " + orderId);
            
            ReservationDto dto = new ReservationDto();
            
            // 기본 예약 정보 설정
            dto.setRv_room_no(roomNo);
            dto.setRv_check_in_date(checkIn);
            dto.setRv_check_out_date(checkOut);
            dto.setRv_guest_count(guestCount);
            dto.setRv_request_message(specialRequest);
            
            // rv_total_price는 DB에 저장될 최종 금액(총 금액 - 사용 포인트)
            dto.setRv_total_price(totalAmount - usePoints); 
            // 원 금액 (할인, 포인트 미적용 금액) 설정
            dto.setRv_original_price(totalAmount); 
            // 할인 금액 (포인트 사용 금액) 설정
            dto.setRv_discount_amount(usePoints);
            
            // 예약 상태 임시 설정
            dto.setRv_status("TEMP");
            
            // 고객 정보 설정 (Full Name을 first/last name으로 분리)
            String[] names = customerFullName.split(" ", 2);
            dto.setRv_customer_first_name(names[0].trim());
            dto.setRv_customer_last_name(names.length > 1 ? names[1].trim() : "");

            // 이메일 분리
            String[] emails = customerEmail.split("@", 2);
            dto.setRv_customer_email_1(emails[0].trim());
            dto.setRv_customer_email_2(emails.length > 1 ? emails[1].trim() : "");
            
            // 모바일 번호 분리 (DTO 필드가 3개이므로)
            String[] mobiles = customerMobilePhone.split("-", 3);
            dto.setRv_customer_mobile_1(mobiles.length > 0 ? mobiles[0].trim() : "");
            dto.setRv_customer_mobile_2(mobiles.length > 1 ? mobiles[1].trim() : "");
            dto.setRv_customer_mobile_3(mobiles.length > 2 ? mobiles[2].trim() : "");
            
            // ⭐ 회원 및 임시 결제 정보 설정 (수정됨)
            // CommonUtil을 통해 최종 확인
            String member_id = CommonUtil.getSessionInfo(request, "id");
            String membership_level = CommonUtil.getSessionInfo(request, "membership");
            
            // ⭐ 값이 없으면 GUEST로 설정
            if (member_id == null || member_id.trim().isEmpty()) {
                member_id = "###GUEST###";
            }
            if (membership_level == null || membership_level.trim().isEmpty()) {
                membership_level = "BRONZE";
            }
            
            dto.setRv_member_id(member_id);
            dto.setRv_membership_level(membership_level);
            
            // order_id 설정
            dto.setOrder_id(orderId); 
            
            System.out.println(">>> DTO 설정 완료:");
            System.out.println("  RV_ROOM_NO: " + dto.getRv_room_no());
            System.out.println("  RV_MEMBER_ID: " + dto.getRv_member_id());
            System.out.println("  RV_MEMBERSHIP_LEVEL: " + dto.getRv_membership_level());
            System.out.println("  RV_TOTAL_PRICE: " + dto.getRv_total_price());
            System.out.println("  RV_ORIGINAL_PRICE: " + dto.getRv_original_price());
            System.out.println("  RV_DISCOUNT_AMOUNT: " + dto.getRv_discount_amount());
            System.out.println("  ORDER_ID: " + dto.getOrder_id());
            
            // =========================================================
            // 4. 세션에 임시 예약 정보 저장
            // =========================================================
            
            // ReservationDto 자체를 저장하여 결제 시에 DB에 INSERT할 데이터로 사용
            session.setAttribute("tempReservation_" + orderId, dto);
            
            // 고객 정보를 별도로 세션에 저장 (결제 페이지에서 사용)
            session.setAttribute("customerName_" + orderId, customerFullName);
            session.setAttribute("customerEmail_" + orderId, customerEmail);
            session.setAttribute("customerMobilePhone_" + orderId, customerMobilePhone);
            session.setAttribute("breakfastOption_" + orderId, breakfastOption);
            session.setAttribute("usePoints_" + orderId, usePoints);
            session.setAttribute("memberRank_" + orderId, memberRank);
            session.setAttribute("isLoggedIn_" + orderId, isLoggedIn);
            session.setAttribute("memberId_" + orderId, memberId);
            session.setAttribute("country_" + orderId, "korea"); // 기본값
            
            // orderId를 세션에 저장하여 결제 페이지로 이동할 준비를 합니다.
            session.setAttribute("orderId", orderId);
            session.setMaxInactiveInterval(900); // 15분
            
            System.out.println("✅ 세션에 임시 예약 정보 저장 완료");
            System.out.println("=========================================");
            
            // =========================================================
            // 5. JSON 응답 생성
            // =========================================================
            
            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("message", "임시 예약이 생성되었습니다.");
            
            JsonObject data = new JsonObject();
            data.addProperty("orderId", orderId);
            data.addProperty("roomNo", roomNo);
            data.addProperty("checkIn", checkIn);
            data.addProperty("checkOut", checkOut);
            data.addProperty("guestCount", guestCount);
            data.addProperty("totalAmount", totalAmount);
            data.addProperty("usePoints", usePoints);
            data.addProperty("finalAmount", totalAmount - usePoints);
            
            response.add("data", data);
            
            request.setAttribute("jsonResult", response.toString());
            
            System.out.println("✅ JSON 응답 생성 완료");
            System.out.println("응답: " + response.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ 임시 예약 생성 실패: " + e.getMessage());
            
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("success", false);
            errorResponse.addProperty("message", "임시 예약 생성 실패: " + e.getMessage());
            
            request.setAttribute("jsonResult", errorResponse.toString());
        }
    }
}