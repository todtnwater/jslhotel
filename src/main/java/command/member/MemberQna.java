package command.member;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import common.CommonUtil;
import dao.ManagerDao;
import dao.MemberDao;
import dao.QnaDao;
import dto.QnaDto;

public class MemberQna implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {

		MemberDao dao = new MemberDao();
		
		String select = request.getParameter("t_select");
		String search = request.getParameter("t_search");
		String id = CommonUtil.getSessionInfo(request, "id");
		
		if(select == null) {
			select = "title";
			search = "";
		} 
		
		/* 페이징 */
		int totalCount = dao.getQnaCount(select, search, id);
		
		String t_listCount = request.getParameter("t_listCount");
		if(t_listCount == null || t_listCount.equals("")) {
			t_listCount = "5";
		}

		String t_nowPage = request.getParameter("t_nowPage");
		if(t_nowPage == null || t_nowPage.equals("")) {
			t_nowPage = "1";
		}
		
		int listCount = Integer.parseInt(t_listCount); // 한 페이지당 출력 목록 수
		int nowPage = Integer.parseInt(t_nowPage);
		int pageCount = 3; // 화면에 출력할 페이지 개수
		
		int totalPage = totalCount / listCount;
		if(totalCount % listCount > 0) totalPage++;
		
		int start = (nowPage - 1) * listCount + 1; 
		int end = nowPage * listCount;
		
		List<QnaDto> dtos = dao.getQnaList(select, search, id, start, end);
		String displayPage = CommonUtil.getPageDisplay(nowPage, totalPage, pageCount);
		
		request.setAttribute("dtos", dtos);
		request.setAttribute("displayPage", displayPage);
		request.setAttribute("t_listCount", t_listCount);
		request.setAttribute("t_select", select);
		request.setAttribute("t_search", search);
		
	}

}


