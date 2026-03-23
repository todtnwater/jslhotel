package command.room;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import dao.RoomDao;
import dto.RoomDto;

public class RoomStatusUpdate implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {
		
		RoomDao dao = new RoomDao();
		String room_no = request.getParameter("t_room_no");
		
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
		
		int result = dao.updateRoomStatus(room_no, status, clean_status);
		
		String msg = result == 1 ? "객실 상태 변경 성공" : "객실 상태 변경 실패!";
		
		request.setAttribute("t_msg", msg);
		request.setAttribute("t_url", "RoomManager");
		request.setAttribute("t_gubun", "roomView");
		request.setAttribute("t_room_no", room_no);
	}
}