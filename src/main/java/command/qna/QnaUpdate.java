package command.qna;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import common.CommonUtil;
import dao.QnaDao;
import dto.QnaDto;

public class QnaUpdate implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {
		
		QnaDao dao = new QnaDao();
		String q_no = request.getParameter("t_no");
		String q_title = request.getParameter("t_title");
			q_title = q_title.replaceAll("'", "&#39;");
		String q_content = request.getParameter("t_content");
			q_content = q_content.replaceAll("'", "&#39;");
		String q_update_date = CommonUtil.getTodayTime();		
	
		
	
		int result = dao.qnaUpdate(q_no, q_title, q_content, q_update_date);
		String msg = result == 1 ? "문의사항 수정 성공":"문의사항 수정 실패";
		String url = "Qna";
		String gubun = "qnaView";
		
		String page = request.getParameter("t_page");
		if(page == null) page = "qna";
		if(page.equals("member")) {
			url = "Member";
			gubun = "qnaView";
		}
		
		request.setAttribute("t_msg", msg);
		request.setAttribute("t_url", url);
		request.setAttribute("t_gubun", gubun);
		request.setAttribute("t_no", q_no);

	}

}
