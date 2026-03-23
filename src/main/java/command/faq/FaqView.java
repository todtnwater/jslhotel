package command.faq;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import dao.FaqDao;
import dto.FaqDto;

public class FaqView implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {

		FaqDao dao = new FaqDao();
		String no = request.getParameter("t_no");
		
		FaqDto dto = dao.getFaqView(no);
		
		request.setAttribute("dto", dto);
		
	}

}
