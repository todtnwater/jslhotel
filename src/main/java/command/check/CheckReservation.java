package command.check;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import common.CommonExecute;
import dao.ReservationDao;
import dto.ReservationDto;

import java.util.List;
import java.util.Collections;
// import java.util.Enumeration; // 디버깅 코드 사용하지 않을 경우 불필요

/**
 * 예약 조회 (예약번호 또는 회원 ID)
 */
public class CheckReservation implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        
        try {
            String searchType = request.getParameter("searchType");
            HttpSession session = request.getSession();
            
            ReservationDao dao = new ReservationDao();
            List<ReservationDto> reservationList = Collections.emptyList();
            
            System.out.println("========== CheckReservation 실행 ==========");
            System.out.println("searchType: " + searchType);
            
            if ("number".equals(searchType)) {
                String rvNo = request.getParameter("rvNo");
                
                if (rvNo == null || rvNo.trim().isEmpty()) { // trim() 추가하여 공백 문자열도 처리
                    request.setAttribute("error", "예약번호를 입력해야 합니다.");
                    request.setAttribute("viewPage", "check/reservation_check.jsp");
                    return;
                }
                
                System.out.println("========== 예약번호로 조회 ==========");
                System.out.println("예약번호: " + rvNo);
                
                // Assumption: A DAO method for reservation number check
                // NOTE: Using dao.selectReservationByNumberAndEmail(rvNo) requires additional email parameter
                // Assuming simpler single number check for now.
                ReservationDto reservation = dao.selectReservationByNo(rvNo); // selectReservationByNo로 가정
                
                if (reservation != null) {
                    reservationList = Collections.singletonList(reservation); 
                    System.out.println("✅ 예약 조회 성공: " + rvNo);
                } else {
                    request.setAttribute("error", "입력 정보와 일치하는 예약 정보를 찾을 수 없습니다.");
                    request.setAttribute("viewPage", "check/reservation_check.jsp");
                    System.out.println("❌ 예약을 찾을 수 없습니다.");
                    return;
                }
                
            } else if ("sessionId".equals(searchType) || "member".equals(searchType)) {
                System.out.println("========== 회원 ID로 조회 ==========");
                
                // ⭐ 디버깅 코드 제거 (운영 환경 정리)
                /*
                System.out.println(">>> 세션에 저장된 모든 키:");
                Enumeration<String> attributeNames = session.getAttributeNames();
                while (attributeNames.hasMoreElements()) {
                    String key = attributeNames.nextElement();
                    Object value = session.getAttribute(key);
                    System.out.println("  - " + key + " = " + value);
                }
                */
                
                String sessionId = (String) session.getAttribute("sessionId");
                
                System.out.println(">>> 조회된 세션 ID (sessionId): " + sessionId);
                
                if (sessionId == null || sessionId.trim().isEmpty()) {
                    System.out.println("❌ 로그인되지 않음 - sessionId가 null 또는 빈 문자열");
                    request.setAttribute("error", "회원 예약 조회는 로그인이 필요합니다.");
                    request.setAttribute("viewPage", "check/reservation_check.jsp");
                    return;
                }
                
                System.out.println("✅ 회원 ID 확인: " + sessionId);
                
                reservationList = dao.selectReservationsByMemberId(sessionId);
                
                System.out.println("✅ 조회된 예약 수: " + (reservationList != null ? reservationList.size() : 0));
                
                if (reservationList == null || reservationList.isEmpty()) {
                    request.setAttribute("error", "회원님의 예약 내역이 존재하지 않습니다.");
                    request.setAttribute("viewPage", "check/reservation_list.jsp");
                    return;
                }
                
            } else {
                request.setAttribute("error", "잘못된 조회 방식입니다.");
                request.setAttribute("viewPage", "check/reservation_check.jsp");
                return;
            }
            
            request.setAttribute("reservationList", reservationList);
            request.setAttribute("viewPage", "check/reservation_list.jsp");
            
            System.out.println("✅ CheckReservation 완료 - viewPage: check/reservation_list.jsp");
            System.out.println("====================================");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ 예약 조회 중 오류 발생: " + e.getMessage());
            request.setAttribute("error", "예약 조회 중 오류가 발생했습니다.");
            request.setAttribute("viewPage", "check/reservation_check.jsp");
        }
    }
}