package command.manager;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import common.CommonUtil;
import dao.ManagerDao;
import dto.ManagerDto;

public class MemberViewAdmin implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {

		ManagerDao dao = new ManagerDao();
		String id = request.getParameter("t_id");
		if(id == null || id.equals("")) {
			id = CommonUtil.getSessionInfo(request, "id");
		}
				
		ManagerDto dto = dao.getMemberView(id);
		
		request.setAttribute("dto", dto);
		
		
	}

}
