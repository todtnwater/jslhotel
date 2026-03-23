package command.reservation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import common.CommonExecute;
import dao.ReservationDao;
import dao.RoomDao;
import dto.RoomDto;

public class SearchAvailableRooms implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        
        try {
            ReservationDao reservationDao = new ReservationDao();
            RoomDao roomDao = new RoomDao();
            
            String checkIn = request.getParameter("t_check_in_date");
            String checkOut = request.getParameter("t_check_out_date");
            String adultCountStr = request.getParameter("t_adult_count");
            String smoking = request.getParameter("t_smoking");
            
            if(checkIn == null || checkOut == null || adultCountStr == null) {
                throw new Exception("필수 파라미터가 누락되었습니다.");
            }
            
            int adultCount = Integer.parseInt(adultCountStr);
            int totalGuests = adultCount;  // ⭐ children 제거, adults만 사용
            
            System.out.println("========== 객실 검색 ==========");
            System.out.println("체크인: " + checkIn);
            System.out.println("체크아웃: " + checkOut);
            System.out.println("성인: " + adultCount + "명");
            System.out.println("총 인원: " + totalGuests + "명");
            System.out.println("흡연: " + (smoking == null || smoking.isEmpty() ? "상관없음" : smoking));
            System.out.println("============================");
            
            List<RoomDto> rooms;
            
            if (smoking != null && !smoking.isEmpty()) {
                rooms = roomDao.getAvailableRoomsWithSmoking(checkIn, checkOut, smoking);
                System.out.println(">>> 흡연 필터 적용됨: " + smoking);
            } else {
                rooms = reservationDao.searchAvailableRooms(checkIn, checkOut, adultCount, 0);  // ⭐ children = 0
                System.out.println(">>> 흡연 필터 없음 (전체 검색)");
            }
            
            List<RoomDto> filteredRooms = new ArrayList<>();
            
            if(rooms != null && rooms.size() > 0) {
                for(RoomDto room : rooms) {
                    if (room.getR_people_max() >= totalGuests) {
                        int totalPrice = reservationDao.calculateTotalPrice(room, checkIn, checkOut, adultCount, 0);  // ⭐ children = 0
                        room.setR_price(totalPrice);
                        filteredRooms.add(room);
                        
                        System.out.println("✅ " + room.getR_room_no() + " - 최대 " + room.getR_people_max() + "명 (OK)");
                    } else {
                        System.out.println("❌ " + room.getR_room_no() + " - 최대 " + room.getR_people_max() + "명 (인원 초과, 필터링)");
                    }
                }
            }
            
            System.out.println("============================");
            System.out.println("검색된 객실: " + (rooms != null ? rooms.size() : 0) + "개");
            System.out.println("필터링 후: " + filteredRooms.size() + "개");
            System.out.println("============================");
            
            Gson gson = new Gson();
            String jsonResult = gson.toJson(filteredRooms);
            
            request.setAttribute("jsonResult", jsonResult);
            
        } catch (Exception e) {
            e.printStackTrace();
            
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("success", false);
            errorResponse.addProperty("message", "객실 검색 실패: " + e.getMessage());
            
            Gson gson = new Gson();
            request.setAttribute("jsonResult", gson.toJson(errorResponse));
        }
    }
}