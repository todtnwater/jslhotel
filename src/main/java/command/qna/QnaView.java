package command.qna;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import dao.QnaDao;
import dto.QnaDto;

public class QnaView implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {

		QnaDao dao = new QnaDao();

		String q_no = request.getParameter("t_no");
		
		QnaDto dto = dao.getQnaView(q_no);
		
		request.setAttribute("dto", dto);
	}

}
