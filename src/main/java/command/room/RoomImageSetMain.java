package command.room;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.RoomImageDao;

public class RoomImageSetMain {
	
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		
		response.setContentType("application/json; charset=utf-8");
		
		try {
			PrintWriter out = response.getWriter();
			
			String roomNo = request.getParameter("roomNo");
			String imgNo = request.getParameter("imgNo");
			
			if(roomNo != null && imgNo != null && !imgNo.equals("")) {
				RoomImageDao dao = new RoomImageDao();
				int result = dao.setMainImage(roomNo, imgNo);
				
				if(result > 0) {
					out.print("{\"success\": true}");
				} else {
					out.print("{\"success\": false, \"message\": \"메인 이미지 설정 실패\"}");
				}
			} else {
				out.print("{\"success\": false, \"message\": \"필수 파라미터 없음\"}");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				PrintWriter out = response.getWriter();
				out.print("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
