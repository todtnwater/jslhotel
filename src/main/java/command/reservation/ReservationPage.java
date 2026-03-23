package command.reservation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import dao.RoomDao;
import dao.RoomImageDao;
import dto.RoomDto;

public class ReservationPage implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        
        String checkIn = request.getParameter("t_check_in_date");
        String checkOut = request.getParameter("t_check_out_date");
        String adults = request.getParameter("t_adult_count");
        // ⭐ children 제거
        
        RoomDao dao = new RoomDao();
        RoomImageDao imageDao = new RoomImageDao();
        
        // 층별 객실 목록 조회
        Map<Integer, List<RoomDto>> allRoomsByFloor = new HashMap<>();
        for(int floor = 2; floor <= 5; floor++) {
            List<RoomDto> roomList = dao.getRoomListByFloor(floor);
            allRoomsByFloor.put(floor, roomList);
        }
        
        // 예약 가능한 객실 조회
        List<RoomDto> availableRooms = null;
        if(checkIn != null && !checkIn.isEmpty() && checkOut != null && !checkOut.isEmpty()) {
            availableRooms = dao.getAvailableRooms(checkIn, checkOut);
            System.out.println(">>> 예약 가능한 객실 수: " + (availableRooms != null ? availableRooms.size() : 0));
        }
        
        // ⭐ 모든 객실의 메인 이미지를 한 번에 조회 (성능 최적화!)
        Map<String, String> mainImages = imageDao.getAllMainImages();
        
        System.out.println(">>> 메인 이미지 조회 완료: " + mainImages.size() + "개");
        
        request.setAttribute("allRoomsByFloor", allRoomsByFloor);
        request.setAttribute("availableRooms", availableRooms);
        request.setAttribute("mainImages", mainImages);
        request.setAttribute("checkInDate", checkIn);
        request.setAttribute("checkOutDate", checkOut);
        request.setAttribute("adultCount", adults);
        // ⭐ children 제거
    }
}