package command.manager;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import common.CommonUtil;
import dao.ManagerDao;

public class MemberChangeStatus implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {

		ManagerDao dao = new ManagerDao();
		String id = request.getParameter("t_id");
		String status = request.getParameter("t_status");
		String date = CommonUtil.getTodayTime();
		
		int result = dao.changeStatus(id, status, date);
		
		String msg = result == 1 ? "탈퇴 정보 수정 성공":"탈퇴 정보 수정 실패!";
		String gubun = "memberView";
		
		request.setAttribute("t_msg", msg);
		request.setAttribute("t_url", "Manager");
		request.setAttribute("t_gubun", gubun);
		request.setAttribute("t_id", id);
		
		
	}

}
