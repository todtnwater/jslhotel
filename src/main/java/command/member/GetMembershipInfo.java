package command.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import common.CommonExecute;
import dao.MembershipDao;
import dto.MembershipDto;

public class GetMembershipInfo implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        
        HttpSession session = request.getSession();
        String memberId = (String) session.getAttribute("sessionId");
        
        if (memberId == null || memberId.isEmpty()) {
            System.out.println("멤버십 정보 조회 실패: 로그인 필요");
            return;
        }
        
        try {
            MembershipDao dao = new MembershipDao();
            MembershipDto dto = dao.getMembershipByMemberId(memberId);
            
            if (dto != null) {
                request.setAttribute("membershipInfo", dto);
                
                System.out.println("========== 멤버십 정보 조회 성공 ==========");
                System.out.println("회원번호: " + dto.getMb_no());
                System.out.println("주문번호: " + dto.getMb_order_id());
                System.out.println("시작일: " + dto.getMb_start_date());
                System.out.println("종료일: " + dto.getMb_end_date());
                System.out.println("========================================");
            } else {
                System.out.println("멤버십 정보 없음: " + memberId);
            }
            
        } catch (Exception e) {
            System.out.println("멤버십 정보 조회 오류:");
            e.printStackTrace();
        }
    }
}