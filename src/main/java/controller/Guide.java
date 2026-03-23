package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Guide")
public class Guide extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Guide() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		request.setCharacterEncoding("utf-8");
		
		String gubun = request.getParameter("t_gubun");
		String viewPage = "";
		
		if (gubun == null || gubun.isEmpty()) {
            viewPage = "guide/guide_overview.jsp"; 
            request.setAttribute("menuOn", "overview"); 
            request.setAttribute("headerOn", "guide");
		} else {
			// 호텔 개요
            if(gubun.equals("overview")){
                viewPage = "guide/guide_overview.jsp"; 
            
            // 오시는 길
            } else if (gubun.equals("road")){
                viewPage = "guide/guide_road.jsp";     
            
            // 객실 안내
            } else if (gubun.equals("room")){
                viewPage = "guide/guide_room.jsp";     
            
            // 부속시설 안내
            } else if (gubun.equals("facility")){
                viewPage = "guide/guide_facility.jsp"; 
            
            // 호텔 소개 메인 화면    
            } else {
                 viewPage = "guide/guide_overview.jsp"; 
            }
            request.setAttribute("menuOn", gubun);
            request.setAttribute("headerOn", "guide");
		}
		RequestDispatcher rd = request.getRequestDispatcher(viewPage);
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}