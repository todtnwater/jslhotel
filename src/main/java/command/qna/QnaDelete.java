package command.qna;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import dao.QnaDao;

public class QnaDelete implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {

		QnaDao dao = new QnaDao();
		String q_no = request.getParameter("t_no");
		
		int result = dao.qnaDelete(q_no);
		//int result = 0; // 삭제 테스트용
		
		String msg = result == 1 ? "문의사항을 삭제하였습니다.":"문의사항 삭제에 실패하였습니다. 관리자에게 문의 바랍니다.";
		String url = "Qna";
		String gubun = result == 1 ? "qnaList":"qnaView";
		
		// 관리자페이지에서 삭제했을 경우
		String page = request.getParameter("t_page");
		if(page == null) page = "qna";
		if(page.equals("manager")) {
			url = "Manager";
		} else if(page.equals("member")) {
			url = "Member";
		}
		
		request.setAttribute("t_gubun", gubun);
		request.setAttribute("t_no", q_no);
		request.setAttribute("t_msg", msg);
		request.setAttribute("t_url", url);
		
		
	}

}
