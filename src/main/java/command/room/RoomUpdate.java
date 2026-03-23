package command.room;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import dao.RoomDao;
import dto.RoomDto;

public class RoomUpdate implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {
		
		RoomDao dao = new RoomDao();
		
		String room_no = request.getParameter("t_room_no");
		String extra_person_fee = request.getParameter("t_extra_person_fee");
		String price = request.getParameter("t_price");
		String bed_type = request.getParameter("t_bed_type");
		String bed = request.getParameter("t_bed");
		String smoking = request.getParameter("t_smoking");
		String bathroom = request.getParameter("t_bathroom");
		String kitchen = request.getParameter("t_kitchen");
		String wifi = request.getParameter("t_wifi");
		String spa = request.getParameter("t_spa");
		String view = request.getParameter("t_view");
		String balcony = request.getParameter("t_balcony");
		String description = request.getParameter("t_description");
		
		// 통합 상태를 분리
		String room_status = request.getParameter("t_room_status");
		String status = "";
		String clean_status = "";
		
		if(room_status != null && room_status.contains("_")) {
			String[] parts = room_status.split("_", 2);
			status = parts[0];           // AVAILABLE, OCCUPIED, MAINTENANCE
			clean_status = parts[1];     // CLEAN, DIRTY, CLEANING
		} else {
			status = "AVAILABLE";
			clean_status = "CLEAN";
		}
		
		// ⚠️ 안전장치: OCCUPIED 상태로 변경 시도 차단
		if("OCCUPIED".equals(status)) {
			request.setAttribute("t_msg", "투숙중(OCCUPIED) 상태는 체크인 시스템에서만 변경할 수 있습니다!");
			request.setAttribute("t_url", "RoomManager");
			request.setAttribute("t_gubun", "roomView");
			request.setAttribute("t_room_no", room_no);
			return;
		}
		
		// ⚠️ 안전장치: 현재 OCCUPIED 상태인 객실을 다른 상태로 변경 시도 차단
		RoomDto currentRoom = dao.getRoomView(room_no);
		if(currentRoom != null && "OCCUPIED".equals(currentRoom.getR_status())) {
			request.setAttribute("t_msg", "투숙중인 객실은 체크아웃 전까지 상태를 변경할 수 없습니다!");
			request.setAttribute("t_url", "RoomManager");
			request.setAttribute("t_gubun", "roomView");
			request.setAttribute("t_room_no", room_no);
			return;
		}
		
		int result = dao.updateRoomInfo(room_no, extra_person_fee, price, bed_type, bed, 
										smoking, bathroom, kitchen, wifi, spa, view, balcony, 
										status, clean_status, description);
		
		String msg = result == 1 ? "객실 정보가 수정되었습니다." : "객실 정보 수정 실패!";
		
		request.setAttribute("t_msg", msg);
		request.setAttribute("t_url", "RoomManager");
		request.setAttribute("t_gubun", "roomView");
		request.setAttribute("t_room_no", room_no);
	}
}