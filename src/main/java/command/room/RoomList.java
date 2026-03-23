package command.room;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import dao.RoomDao;
import dao.RoomImageDao;
import dto.RoomDto;

public class RoomList implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {
		
		RoomDao roomDao = new RoomDao();
		RoomImageDao imageDao = new RoomImageDao();
		
		// 층 파라미터 받기
		String t_floor = request.getParameter("t_floor");
		if(t_floor == null || t_floor.equals("")) {
			t_floor = "5"; // 기본값 5층
		}
		
		// 층별로 분류
		Map<Integer, List<RoomDto>> roomsByFloor = new HashMap<>();
		for(int floor = 2; floor <= 5; floor++) {
			List<RoomDto> roomList = roomDao.getRoomListByFloor(floor);
			roomsByFloor.put(floor, roomList);
		}
		
		// ⭐ 모든 객실의 메인 이미지를 한 번에 조회 (성능 최적화!)
		Map<String, String> mainImages = imageDao.getAllMainImages();
		
		System.out.println(">>> [RoomList] 메인 이미지 조회 완료: " + mainImages.size() + "개");
		
		request.setAttribute("roomsByFloor", roomsByFloor);
		request.setAttribute("mainImages", mainImages);
		request.setAttribute("t_floor", t_floor);
	}
}