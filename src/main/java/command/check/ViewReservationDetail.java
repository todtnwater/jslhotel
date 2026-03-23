package command.check;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import common.CommonExecute;
import dao.ReservationDao;
import dto.ReservationDto;

public class ViewReservationDetail implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        
        try {
            String rvNo = request.getParameter("rvNo");
            // HttpSession session = request.getSession(); // 세션 정보를 사용하지 않아 주석 처리
            
            System.out.println("========== 예약 상세 조회 ==========");
            System.out.println("rvNo: " + rvNo);
            
            if (rvNo == null || rvNo.trim().isEmpty()) {
                throw new Exception("예약번호가 없습니다.");
            }
            
            ReservationDao dao = new ReservationDao();
            ReservationDto reservation = dao.selectReservationByNo(rvNo);
            
            if (reservation == null) {
                throw new Exception("예약 정보를 찾을 수 없습니다.");
            }
            
            System.out.println("✅ 예약 상세 조회 성공");
            System.out.println("  예약번호: " + reservation.getRv_no());
            System.out.println("  객실타입: " + reservation.getRv_room_type());
            System.out.println("  예약자: " + reservation.getRv_customer_full_name());
            
            // ⭐⭐⭐ JSP에서 session 객체를 직접 사용하므로 아래 request.setAttribute 코드는 제거합니다. ⭐⭐⭐
            /*
            String sessionId = (String) session.getAttribute("sessionId");
            String sessionName = (String) session.getAttribute("sessionName");
            String sessionLevel = (String) session.getAttribute("sessionLevel");
            String sessionPosition = (String) session.getAttribute("sessionPosition");
            String sessionMembership = (String) session.getAttribute("sessionMembership");

            System.out.println("========== 세션 정보 확인 ==========");
            // ... (세션 정보 출력 로직 제거)

            request.setAttribute("sessionId", sessionId != null ? sessionId : "");
            request.setAttribute("sessionName", sessionName != null ? sessionName : "");
            // ... (나머지 request.setAttribute 제거)
            */
            
            request.setAttribute("reservation", reservation);
            request.setAttribute("viewPage", "check/reservation_receipt.jsp");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ 예약 상세 조회 실패: " + e.getMessage());
            
            request.setAttribute("error", e.getMessage());
            request.setAttribute("viewPage", "check/reservation_check.jsp");
        }
    }
}