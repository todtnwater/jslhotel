package command.membership;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import common.CommonExecute;
import common.CommonUtil;

/**
 * 멤버십 임시 생성 Command
 * - 결제 전 멤버십 정보를 세션에 저장
 * - orderId 생성 후 Payment로 리다이렉트
 */
public class CreateTempMembership implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        
        try {
            JsonObject jsonObj = (JsonObject) request.getAttribute("jsonObject");
            HttpSession session = request.getSession();
            
            // 파라미터 추출
            String membershipGrade = jsonObj.get("membershipGrade").getAsString(); // "gold" or "premium"
            String memberId = CommonUtil.getSessionInfo(request, "id");
            
            System.out.println("========== 임시 멤버십 생성 시작 ==========");
            System.out.println("membershipGrade: " + membershipGrade);
            System.out.println("memberId: " + memberId);
            
            // 로그인 체크
            if (memberId == null || memberId.equals("")) {
                throw new Exception("로그인이 필요한 서비스입니다.");
            }
            
            // 멤버십 금액 설정
            int amount = 0;
            String membershipName = "";
            String validPeriod = "2026-01-01 ~ 2026-06-30";
            
            if (membershipGrade.equalsIgnoreCase("gold")) {
                amount = 600;
                membershipName = "Gold 회원권";
            } else if (membershipGrade.equalsIgnoreCase("premium")) {
                amount = 1200;
                membershipName = "Premium 회원권";
            } else {
                throw new Exception("잘못된 멤버십 등급입니다.");
            }
            
            // orderId 생성 (중복 방지용 고유 ID)
            String orderId = "MEMBERSHIP_" + System.currentTimeMillis();
            
            System.out.println("========== 멤버십 정보 ==========");
            System.out.println("orderId: " + orderId);
            System.out.println("membershipName: " + membershipName);
            System.out.println("amount: " + amount);
            System.out.println("validPeriod: " + validPeriod);
            System.out.println("================================");
            
            // 세션에 임시 멤버십 정보 저장
            session.setAttribute("tempMembership_" + orderId, membershipGrade);
            session.setAttribute("membershipName_" + orderId, membershipName);
            session.setAttribute("membershipAmount_" + orderId, amount);
            session.setAttribute("membershipPeriod_" + orderId, validPeriod);
            session.setAttribute("membershipMemberId_" + orderId, memberId);
            
            System.out.println("✅ 세션에 임시 멤버십 정보 저장 완료");
            
            // 성공 응답
            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("message", "멤버십 정보가 생성되었습니다.");
            response.addProperty("orderId", orderId);
            
            JsonObject data = new JsonObject();
            data.addProperty("membershipGrade", membershipGrade);
            data.addProperty("membershipName", membershipName);
            data.addProperty("amount", amount);
            data.addProperty("validPeriod", validPeriod);
            
            response.add("data", data);
            
            request.setAttribute("jsonResult", response.toString());
            
            System.out.println("✅ 임시 멤버십 생성 완료");
            
        } catch (Exception e) {
            e.printStackTrace();
            
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("success", false);
            errorResponse.addProperty("message", "멤버십 생성 실패: " + e.getMessage());
            
            request.setAttribute("jsonResult", errorResponse.toString());
        }
    }
}
