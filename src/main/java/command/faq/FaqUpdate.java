package command.faq;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import common.CommonUtil;
import dao.FaqDao;
import dto.FaqDto;

public class FaqUpdate implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {

		FaqDao dao = new FaqDao();
		
		String no = request.getParameter("t_no");
		String type = request.getParameter("t_type");
		String question = request.getParameter("t_question");
			question = CommonUtil.setSquot(question);
		String answer = request.getParameter("t_answer");
			answer = CommonUtil.setSquot(answer);
		String reg_id = CommonUtil.getSessionInfo(request, "id");
		String position = CommonUtil.getSessionInfo(request, "position");
		String update_date = CommonUtil.getTodayTime();
		
		
		
		FaqDto dto = new FaqDto(no, type, question, answer, reg_id, position, "", update_date);	
		
		int result = dao.updateFaq(dto);
		
		String msg = result == 1 ? "FAQ 수정 성공":"FAQ 수정 실패";
		String url = "Manager";
		String gubun = "faqView";
		
		request.setAttribute("t_msg", msg);
		request.setAttribute("t_url", url);
		request.setAttribute("t_gubun", gubun);
		request.setAttribute("t_no", no);
		
	}

}
