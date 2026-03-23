package command.faq;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import dao.FaqDao;
import dto.FaqDto;

public class FaqList implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {

		FaqDao dao = new FaqDao();
		
		List<FaqDto> dtos = dao.getFaqList();
		
		request.setAttribute("dtos", dtos);
		
	}

}
