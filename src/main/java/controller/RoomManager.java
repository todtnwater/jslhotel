package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import command.room.RoomImageDelete;
import command.room.RoomImageList;
import command.room.RoomImageSetMain;
import command.room.RoomImageUpload;
import command.room.RoomList;
import command.room.RoomStatusUpdate;
import command.room.RoomUpdate;
import command.room.RoomView;
import common.CommonExecute;
import common.CommonUtil;

/**
 * Servlet implementation class RoomManager
 */
@WebServlet("/RoomManager")
public class RoomManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RoomManager() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		
		String level = CommonUtil.getSessionInfo(request, "level");
		String viewPage = "";
		
		// null 체크 추가
		if(level == null || level.equals("")) {
			request.setAttribute("t_msg", "로그인이 필요합니다.");
			request.setAttribute("t_url", "Index");
			viewPage = "common_alert.jsp";
			
			RequestDispatcher rd = request.getRequestDispatcher(viewPage);
			rd.forward(request, response);
			return;
		}
		
		String gubun = request.getParameter("t_gubun");
		if(gubun == null || gubun.equals("")) gubun = "roomList";
		
		//========== 객실 관리 ==========
		//객실 목록 (평면도)
		if(gubun.equals("roomList")) {
			CommonExecute RoomManager = new RoomList();
			RoomManager.execute(request);
			viewPage = "room_manager/room_list.jsp";

		//객실 상세보기
		} else if(gubun.equals("roomView")) {
			CommonExecute RoomManager = new RoomView();
			RoomManager.execute(request);
			
			// 이미지 목록도 함께 조회
			CommonExecute ImageList = new RoomImageList();
			ImageList.execute(request);
			
			viewPage = "room_manager/room_view.jsp";

		//객실 정보 수정
		} else if(gubun.equals("roomUpdate")) {
			CommonExecute RoomManager = new RoomUpdate();
			RoomManager.execute(request);
			viewPage = "common_alert_view.jsp";

		//객실 상태 변경
		} else if(gubun.equals("roomStatusUpdate")) {
			CommonExecute RoomManager = new RoomStatusUpdate();
			RoomManager.execute(request);
			viewPage = "common_alert_view.jsp";
			
		//========== 이미지 관리 ==========
		//이미지 업로드
		} else if(gubun.equals("imageUpload")) {
			RoomImageUpload imageUpload = new RoomImageUpload();
			imageUpload.execute(request, response);
			return;
			
		//이미지 삭제
		} else if(gubun.equals("imageDelete")) {
			RoomImageDelete imageDelete = new RoomImageDelete();
			imageDelete.execute(request, response);
			return;
			
		//메인 이미지 설정
		} else if(gubun.equals("setMainImage")) {
			RoomImageSetMain setMain = new RoomImageSetMain();
			setMain.execute(request, response);
			return;
		}
		
		request.setAttribute("menuOn", gubun);
		
		RequestDispatcher rd = request.getRequestDispatcher(viewPage);
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}