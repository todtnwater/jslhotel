package command.payment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import common.CommonExecute;
import dao.MemberDao;

/**
 * 결제 완료 후 포인트 적립
 * FinalizeOrder에서 DB 저장 후 호출됨
 */
public class PaymentComplete implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        
        System.out.println("========== 포인트 적립 시작 ==========");
        
        try {
            // 1. 파라미터에서 결제 금액 가져오기
            String amountStr = request.getParameter("amount");
            
            if (amountStr == null || amountStr.isEmpty()) {
                System.out.println("❌ 결제 금액 정보 없음");
                return;
            }
            
            int amount = Integer.parseInt(amountStr);
            
            // 2. 세션에서 회원 정보 가져오기
            HttpSession session = request.getSession();
            String memberId = (String) session.getAttribute("sessionId");
            String membership = (String) session.getAttribute("sessionMembership");
            
            System.out.println("결제금액: " + amount + "원");
            System.out.println("회원ID: " + memberId);
            System.out.println("멤버십: " + membership);
            
            // 3. ⭐ 포인트 적립 (회원만)
            if (memberId != null && !memberId.isEmpty() && !memberId.equals("###GUEST###")) {
                
                MemberDao memberDao = new MemberDao();
                
                // 멤버십 기본값 설정
                if (membership == null || membership.isEmpty()) {
                    membership = "silver";
                }
                
                // 적립 포인트 계산
                int earnPoints = memberDao.calculateEarnPoints(membership, amount);
                
                if (earnPoints > 0) {
                    // 포인트 적립 실행
                    int result = memberDao.addMemberPoint(memberId, earnPoints);
                    
                    if (result > 0) {
                        System.out.println("========== 포인트 적립 완료 ==========");
                        System.out.println("회원: " + memberId);
                        System.out.println("멤버십: " + membership);
                        System.out.println("결제금액: " + amount + "원");
                        System.out.println("적립포인트: " + earnPoints + "P");
                        System.out.println("적립률: " + getEarnRateText(membership));
                        System.out.println("====================================");
                        
                        // 적립 포인트를 request에 저장
                        request.setAttribute("earnedPoints", earnPoints);
                        
                        // 세션의 포인트 정보 갱신
                        int currentPoints = memberDao.getMemberPoint(memberId);
                        session.setAttribute("totalPoints", currentPoints);
                        
                    } else {
                        System.out.println("❌ 포인트 적립 실패");
                    }
                } else {
                    System.out.println("ℹ️ 적립 포인트 없음 (bronze 멤버십)");
                }
                
            } else {
                System.out.println("ℹ️ 비회원 결제 - 포인트 적립 없음");
            }
            
            System.out.println("========== 포인트 적립 처리 종료 ==========");
            
        } catch (Exception e) {
            System.out.println("❌ 포인트 적립 중 오류 발생");
            e.printStackTrace();
        }
    }
    
    /**
     * 멤버십별 적립률 텍스트 반환
     */
    private String getEarnRateText(String membership) {
        if (membership == null) return "0%";
        
        switch(membership.toLowerCase()) {
            case "premium": return "20%";
            case "gold": return "15%";
            case "silver": return "10%";
            case "bronze": return "0%";
            default: return "0%";
        }
    }
}