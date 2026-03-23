package command.qna;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import common.CommonUtil;
import dao.QnaDao;
import dto.QnaDto;

public class QnaSave implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {

		QnaDao dao = new QnaDao();
		String q_no = dao.getQnaNo();
		String q_title = request.getParameter("t_title");
			q_title = q_title.replaceAll("'", "&#39;");
		String q_content = request.getParameter("t_content");
			q_content = q_content.replaceAll("'", "&#39;");
		String q_type = request.getParameter("t_type");
		String q_member = "n";
		String q_reg_writer = request.getParameter("t_reg_writer");
		String q_password = request.getParameter("t_password");
		String q_reg_date = CommonUtil.getTodayTime();		
	
		if(!CommonUtil.getSessionInfo(request, "id").equals("")) {
			q_member = "y"; 
			q_password = "";
		}
	
		QnaDto dto = new QnaDto(q_no, q_title, q_content, q_type, q_member, q_reg_writer, q_password, q_reg_date);
		int result = dao.qnaSave(dto);
		String msg = result == 1 ? "문의 등록 성공":"문의 등록 실패";
		String url = "Qna";
		String gubun = result == 1 ? "qnaList":"writeForm";
		
		request.setAttribute("t_msg", msg);
		request.setAttribute("t_url", url);
		request.setAttribute("t_gubun", gubun);
			
	
	}

}
