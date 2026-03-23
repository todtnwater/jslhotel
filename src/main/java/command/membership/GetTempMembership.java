package command.membership;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import common.CommonExecute;

/**
 * 임시 멤버십 조회 Command
 * - 결제 페이지에서 멤버십 정보를 조회
 */
public class GetTempMembership implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        
        try {
            String orderId = request.getParameter("orderId");
            HttpSession session = request.getSession();
            
            System.out.println("========== 임시 멤버십 정보 조회 ==========");
            System.out.println("orderId: " + orderId);
            
            String membershipGrade = (String) session.getAttribute("tempMembership_" + orderId);
            String membershipName = (String) session.getAttribute("membershipName_" + orderId);
            Integer membershipAmount = (Integer) session.getAttribute("membershipAmount_" + orderId);
            String membershipPeriod = (String) session.getAttribute("membershipPeriod_" + orderId);
            String memberId = (String) session.getAttribute("membershipMemberId_" + orderId);
            
            if (membershipGrade == null || membershipAmount == null) {
                throw new Exception("임시 멤버십 정보를 찾을 수 없습니다.");
            }
            
            System.out.println("세션에서 조회한 멤버십 정보:");
            System.out.println("  membershipGrade: " + membershipGrade);
            System.out.println("  membershipName: " + membershipName);
            System.out.println("  membershipAmount: " + membershipAmount);
            System.out.println("  membershipPeriod: " + membershipPeriod);
            System.out.println("  memberId: " + memberId);
            
            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("message", "멤버십 정보 조회 성공");
            
            JsonObject data = new JsonObject();
            data.addProperty("membershipGrade", membershipGrade);
            data.addProperty("membershipName", membershipName);
            data.addProperty("totalAmount", membershipAmount);
            data.addProperty("validPeriod", membershipPeriod);
            data.addProperty("memberId", memberId);
            
            response.add("data", data);
            
            request.setAttribute("jsonResult", response.toString());
            
            System.out.println("✅ 임시 멤버십 정보 조회 완료");
            
        } catch (Exception e) {
            e.printStackTrace();
            
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("success", false);
            errorResponse.addProperty("message", "멤버십 정보 조회 실패: " + e.getMessage());
            
            request.setAttribute("jsonResult", errorResponse.toString());
        }
    }
}
