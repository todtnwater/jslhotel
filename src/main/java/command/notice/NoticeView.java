package command.notice;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import dao.NoticeDao;
import dto.NoticeDto;

public class NoticeView implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {
		NoticeDao dao = new NoticeDao();
		String no = request.getParameter("t_no");
		
		int result = dao.setHitCount(no);
		if(result != 1) System.out.println("공지사항 조회수 증가 오류!");
		
		NoticeDto dto = dao.getNoticeView(no);		
		request.setAttribute("dto", dto);
	}
}