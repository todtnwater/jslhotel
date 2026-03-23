package command.room;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import dao.RoomImageDao;
import dto.RoomImageDto;

public class RoomImageList implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {
		
		RoomImageDao dao = new RoomImageDao();
		String room_no = request.getParameter("t_room_no");
		
		if(room_no == null || room_no.equals("")) {
			request.setAttribute("imageList", null);
			return;
		}
		
		List<RoomImageDto> imageList = dao.getRoomImages(room_no);
		request.setAttribute("imageList", imageList);
	}
}
