package command.reservation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import common.CommonExecute;
import common.CommonUtil;
import dao.MemberDao;
import dao.RoomDao;
import dao.RoomImageDao;
import dto.MemberDto;
import dto.RoomDto;
import dto.RoomImageDto;

public class GoRoomView implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        
        try {
            // 다양한 파라미터 이름 처리
            String roomNo = request.getParameter("roomNo");
            if (roomNo == null || roomNo.equals("")) {
                roomNo = request.getParameter("t_room_no");
            }
            if (roomNo == null || roomNo.equals("")) {
                roomNo = request.getParameter("r_room_no");
            }
            
            String checkIn = request.getParameter("checkIn");
            if (checkIn == null || checkIn.equals("")) {
                checkIn = request.getParameter("t_check_in");
            }
            if (checkIn == null || checkIn.equals("")) {
                checkIn = request.getParameter("t_check_in_date");
            }
            
            String checkOut = request.getParameter("checkOut");
            if (checkOut == null || checkOut.equals("")) {
                checkOut = request.getParameter("t_check_out");
            }
            if (checkOut == null || checkOut.equals("")) {
                checkOut = request.getParameter("t_check_out_date");
            }
            
            String adultsStr = request.getParameter("adults");
            if (adultsStr == null || adultsStr.equals("")) {
                adultsStr = request.getParameter("t_adult_count");
            }
            
            System.out.println("========== GoRoomView 실행 ==========");
            System.out.println("roomNo: " + roomNo);
            System.out.println("checkIn: " + checkIn);
            System.out.println("checkOut: " + checkOut);
            System.out.println("adults: " + adultsStr);
            
            if (roomNo == null || roomNo.equals("")) {
                throw new Exception("객실 번호가 없습니다.");
            }
            
            if (checkIn == null || checkOut == null) {
                throw new Exception("체크인/체크아웃 날짜가 없습니다.");
            }
            
            int adults = (adultsStr != null && !adultsStr.equals("")) ? Integer.parseInt(adultsStr) : 1;
            
            // 객실 정보 조회
            RoomDao roomDao = new RoomDao();
            RoomDto dto = roomDao.getRoomByNo(roomNo);
            
            if (dto == null) {
                throw new Exception("객실 정보를 찾을 수 없습니다. 객실번호: " + roomNo);
            }
            
            // 객실 이미지 조회
            RoomImageDao imageDao = new RoomImageDao();
            
            // 메인 이미지 조회
            RoomImageDto mainImage = imageDao.getMainImage(roomNo);
            String mainImagePath = null;
            if(mainImage != null) {
                mainImagePath = mainImage.getRi_img_path();
            }
            
            // 모든 이미지 목록 조회
            List<RoomImageDto> roomImages = imageDao.getRoomImages(roomNo);
            
            System.out.println(">>> 메인 이미지: " + mainImagePath);
            System.out.println(">>> 전체 이미지 수: " + (roomImages != null ? roomImages.size() : 0));
            
            // ⭐⭐⭐ 세션 디버깅 강화 ⭐⭐⭐
            HttpSession session = request.getSession(false);
            
            System.out.println("========== 세션 디버깅 시작 ==========");
            
            if (session == null) {
                System.out.println("❌ 세션이 존재하지 않습니다!");
                System.out.println(">>> 비회원 사용자로 처리됩니다.");
            } else {
                System.out.println("✅ 세션 존재 확인");
                System.out.println("세션 ID: " + session.getId());
                System.out.println("세션 생성 시간: " + new java.util.Date(session.getCreationTime()));
                System.out.println("세션 최종 접근 시간: " + new java.util.Date(session.getLastAccessedTime()));
                System.out.println("세션 타임아웃: " + session.getMaxInactiveInterval() + "초");
                
                // 모든 세션 속성 출력
                System.out.println(">>> 세션에 저장된 모든 속성:");
                java.util.Enumeration<String> attributeNames = session.getAttributeNames();
                boolean hasAttributes = false;
                while (attributeNames.hasMoreElements()) {
                    hasAttributes = true;
                    String attributeName = attributeNames.nextElement();
                    Object attributeValue = session.getAttribute(attributeName);
                    System.out.println("  - " + attributeName + " = " + attributeValue);
                }
                
                if (!hasAttributes) {
                    System.out.println("  ⚠️ 세션에 저장된 속성이 없습니다!");
                }
                
                // 개별 속성 확인
                System.out.println(">>> 개별 속성 확인:");
                System.out.println("  sessionId: " + session.getAttribute("sessionId"));
                System.out.println("  sessionName: " + session.getAttribute("sessionName"));
                System.out.println("  sessionLevel: " + session.getAttribute("sessionLevel"));
                System.out.println("  sessionPosition: " + session.getAttribute("sessionPosition"));
                System.out.println("  sessionMembership: " + session.getAttribute("sessionMembership"));
                System.out.println("  totalPoints: " + session.getAttribute("totalPoints"));
            }
            
            System.out.println("====================================");
            
            // ✅ 로그인한 회원이면 회원 정보 조회
            String memberId = CommonUtil.getSessionInfo(request, "id");
            System.out.println(">>> CommonUtil.getSessionInfo('id'): " + memberId);
            
            if (memberId != null && !memberId.isEmpty()) {
                System.out.println("✅ 회원 ID 확인됨: " + memberId);
                
                MemberDao memberDao = new MemberDao();
                MemberDto memberDto = memberDao.getMemberById(memberId);
                
                if (memberDto != null) {
                    request.setAttribute("memberDto", memberDto);
                    System.out.println("✅ 회원 정보 조회 성공: " + memberDto.getFirst_name() + " " + memberDto.getLast_name());
                    System.out.println("   이메일: " + memberDto.getEmail_1() + "@" + memberDto.getEmail_2());
                    System.out.println("   연락처: " + memberDto.getMobile_1() + "-" + memberDto.getMobile_2() + "-" + memberDto.getMobile_3());
                 
                } else {
                    System.out.println("⚠️ DB에서 회원 정보를 찾을 수 없습니다. (memberId: " + memberId + ")");
                }
            } else {
                System.out.println(">>> 비회원 사용자 (memberId가 null 또는 빈 문자열)");
            }
            
            request.setAttribute("dto", dto);
            request.setAttribute("checkIn", checkIn);
            request.setAttribute("checkOut", checkOut);
            request.setAttribute("adults", adults);
            request.setAttribute("mainImagePath", mainImagePath);
            request.setAttribute("roomImages", roomImages);
            
          
            System.out.println("====================================");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("GoRoomView 오류: " + e.getMessage());
            
            request.setAttribute("t_msg", "객실 정보 조회 중 오류가 발생했습니다: " + e.getMessage());
            request.setAttribute("t_url", "Reservation?t_gubun=reservation");
        }
    }
}