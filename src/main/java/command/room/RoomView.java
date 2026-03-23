package command.room;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import dao.RoomDao;
import dto.RoomDto;

public class RoomView implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {
		
		RoomDao dao = new RoomDao();
		String room_no = request.getParameter("t_room_no");
		
		if(room_no == null || room_no.equals("")) {
			request.setAttribute("t_msg", "객실 번호가 없습니다.");
			request.setAttribute("t_url", "Manager");
			request.setAttribute("t_gubun", "roomList");
			return;
		}
		
		RoomDto dto = dao.getRoomView(room_no);
		
		if(dto == null) {
			request.setAttribute("t_msg", "해당 객실을 찾을 수 없습니다.");
			request.setAttribute("t_url", "Manager");
			request.setAttribute("t_gubun", "roomList");
			return;
		}
		
		request.setAttribute("dto", dto);
	}
}