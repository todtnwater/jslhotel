package command.faq;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import common.CommonUtil;
import dao.FaqDao;
import dto.FaqDto;

public class FaqSave implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {

		FaqDao dao = new FaqDao();
		
		String no = dao.getFaqNo();
		String type = request.getParameter("t_type");
		String question = request.getParameter("t_question");
			question = CommonUtil.setSquot(question);
		String answer = request.getParameter("t_answer");
			answer = CommonUtil.setSquot(answer);
		String reg_id = CommonUtil.getSessionInfo(request, "id");
		String position = CommonUtil.getSessionInfo(request, "position");
		String reg_date = CommonUtil.getTodayTime();
		
		
		
		FaqDto dto = new FaqDto(no, type, question, answer, reg_id, position, reg_date, "");	
		
		int result = dao.saveFaq(dto);
		
		String msg = result == 1 ? "FAQ 등록 성공":"FAQ 등록 실패";
		String url = "Manager";
		String gubun = result == 1 ? "faqList":"faqWriteForm";
		
		request.setAttribute("t_msg", msg);
		request.setAttribute("t_url", url);
		request.setAttribute("t_gubun", gubun);
		request.setAttribute("t_no", no);
		
	}

}
