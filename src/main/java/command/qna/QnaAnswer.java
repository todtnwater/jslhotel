package command.qna;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import common.CommonUtil;
import dao.QnaDao;
import dto.QnaDto;

public class QnaAnswer implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {
		
		QnaDao dao = new QnaDao();
		String q_no = request.getParameter("t_no");
		String a_content = request.getParameter("t_answer");
			a_content = a_content.replaceAll("'", "&#39;");
		String a_id = CommonUtil.getSessionInfo(request, "id");	
		String a_position = CommonUtil.getSessionInfo(request, "position");	
		String a_update_date = CommonUtil.getTodayTime();		
	
		
	
		int result = dao.qnaAnswer(q_no, a_content, a_id, a_position, a_update_date);
		String msg = result == 1 ? "문의사항 답변 성공":"문의사항 답변 실패";
		String url = "Qna";
		String gubun = "qnaView";

		// 관리자페이지에서 답변 저장했을 경우
		String page = request.getParameter("t_page");
		if(page == null) page = "qna";
		if(page.equals("manager")) {
			url = "Manager";
			gubun = "qnaView";
		}		
		
		request.setAttribute("t_msg", msg);
		request.setAttribute("t_url", url);
		request.setAttribute("t_gubun", gubun);
		request.setAttribute("t_no", q_no);

	}

}
