package command.faq;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import dao.FaqDao;

public class FaqDelete implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {

		FaqDao dao = new FaqDao();
		String no = request.getParameter("t_no");
		
		int result = dao.deleteFaq(no);
		
		String msg = result == 1 ? "FAQ 삭제 성공":"FAQ 삭제 실패";
		String url = "Manager";
		String gubun = result == 1 ? "faqList":"faqView";
		
		request.setAttribute("t_msg", msg);
		request.setAttribute("t_url", url);
		request.setAttribute("t_gubun", gubun);
		request.setAttribute("t_no", no);
		
	}

}
