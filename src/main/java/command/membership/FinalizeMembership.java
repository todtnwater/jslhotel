package command.membership;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import common.CommonExecute;
import dao.MemberDao;
import dao.MembershipDao;
import dao.PaymentDao;
import dto.MembershipDto;
import dto.PaymentDto;

/**
 * 멤버십 최종 확정 Command
 * - 토스 결제 승인 후 멤버십을 DB에 저장
 * - 결제 정보도 함께 저장
 */
public class FinalizeMembership implements CommonExecute {

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
            
            System.out.println("========== 멤버십 확정 시작 ==========");
            System.out.println("orderId: " + orderId);
            System.out.println("paymentKey: " + paymentKey);
            System.out.println("amount: " + amount);
            
            // 세션에서 임시 멤버십 정보 조회
            String membershipGrade = (String) session.getAttribute("tempMembership_" + orderId);
            String membershipName = (String) session.getAttribute("membershipName_" + orderId);
            String membershipPeriod = (String) session.getAttribute("membershipPeriod_" + orderId);
            String memberId = (String) session.getAttribute("membershipMemberId_" + orderId);
            
            if (membershipGrade == null) {
                throw new Exception("주문 정보를 찾을 수 없습니다. (세션 만료)");
            }
            
            System.out.println("========== 멤버십 정보 ==========");
            System.out.println("memberId: " + memberId);
            System.out.println("membershipGrade: " + membershipGrade);
            System.out.println("membershipName: " + membershipName);
            System.out.println("membershipPeriod: " + membershipPeriod);
            System.out.println("================================");
            
            // 멤버십 번호 생성
            MembershipDao membershipDao = new MembershipDao();
            String membershipNo = membershipDao.getMembershipNo();
            
            System.out.println("생성된 멤버십 번호: " + membershipNo);
            
            // 결제 번호 생성
            String pNo = "P" + System.currentTimeMillis();
            
            // 멤버십 시작일/종료일 설정
            String startDate = "2026-01-01";
            String endDate = "2026-06-30";
            
            // 멤버십 DTO 생성
            MembershipDto membershipDto = new MembershipDto();
            membershipDto.setMb_no(membershipNo);
            membershipDto.setMb_id(memberId);
            membershipDto.setMb_membership(membershipGrade);
            membershipDto.setMb_start_date(startDate);
            membershipDto.setMb_end_date(endDate);
            membershipDto.setMb_payment_no(pNo);
            membershipDto.setMb_order_id(orderId);
            membershipDto.setMb_amount(amount);
            membershipDto.setMb_payment_status("COMPLETED");
            
            System.out.println("========== DB 저장 시도 ==========");
            System.out.println("MB_NO: " + membershipDto.getMb_no());
            System.out.println("MB_ID: " + membershipDto.getMb_id());
            System.out.println("MB_MEMBERSHIP: " + membershipDto.getMb_membership());
            System.out.println("MB_PAYMENT_NO: " + membershipDto.getMb_payment_no());
            System.out.println("==================================");
            
            // 멤버십 저장
            int mbResult = membershipDao.insertMembership(membershipDto);
            
            if(mbResult == 0) {
                throw new Exception("멤버십 저장 실패");
            }
            
            System.out.println("✅ 멤버십 저장 완료 - MB_NO: " + membershipNo);
            
            // ⭐⭐⭐ 추가: JSL_HOTEL_MEMBER 테이블 업데이트
            MemberDao memberDao = new MemberDao();
            int updateResult = memberDao.updateMembershipGrade(memberId, membershipGrade);
            
            if (updateResult > 0) {
                System.out.println("✅ 회원 멤버십 등급 업데이트 완료: " + memberId + " → " + membershipGrade.toUpperCase());
                
                // 세션 갱신 (중요!)
                session.setAttribute("sessionMembership", membershipGrade);
                System.out.println("✅ 세션 멤버십 정보 갱신 완료");
                
            } else {
                System.out.println("❌ 회원 멤버십 등급 업데이트 실패 (멤버십 데이터는 저장됨)");
            }
            
            // 결제 정보 저장
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setP_no(pNo);
            paymentDto.setP_reservation_no(membershipNo); // 멤버십 번호를 예약번호 필드에 저장
            paymentDto.setP_amount(amount);
            paymentDto.setP_method(paymentMethod);
            paymentDto.setP_status(paymentStatus);
            paymentDto.setPayment_key(paymentKey);
            paymentDto.setOrder_id(orderId);
            
            PaymentDao paymentDao = new PaymentDao();
            int payResult = paymentDao.insertPayment(paymentDto);
            
            if(payResult == 0) {
                throw new Exception("결제 저장 실패");
            }
            
            System.out.println("✅ 결제 저장 완료 - P_NO: " + pNo);
            
            // 세션 정리
            session.removeAttribute("tempMembership_" + orderId);
            session.removeAttribute("membershipName_" + orderId);
            session.removeAttribute("membershipAmount_" + orderId);
            session.removeAttribute("membershipPeriod_" + orderId);
            session.removeAttribute("membershipMemberId_" + orderId);
            
            System.out.println("✅ 멤버십 확정 완료 - MS_NO: " + membershipNo);
            
            // 성공 응답
            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("message", "멤버십이 구독되었습니다.");
            
            JsonObject data = new JsonObject();
            data.addProperty("membershipNo", membershipNo);
            data.addProperty("paymentNo", pNo);
            data.addProperty("membershipGrade", membershipGrade);
            data.addProperty("membershipName", membershipName);
            data.addProperty("totalAmount", amount);
            data.addProperty("validPeriod", membershipPeriod);
            
            response.add("data", data);
            
            request.setAttribute("jsonResult", response.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
            
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("success", false);
            errorResponse.addProperty("message", "멤버십 확정 실패: " + e.getMessage());
            
            request.setAttribute("jsonResult", errorResponse.toString());
        }
    }
}