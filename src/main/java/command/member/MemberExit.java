package command.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import common.CommonExecute;
import common.CommonUtil;
import dao.MemberDao;

public class MemberExit implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {

		MemberDao dao = new MemberDao();
		
		String id = request.getParameter("t_id");
		String exit_date = CommonUtil.getTodayTime();
		
		int result = dao.memberExit(id, exit_date);
		
		String msg, url, gubun;
		
		if(result == 1) {
			msg = "회원탈퇴 신청 완료하였습니다.";
			url = "Index";
			gubun = "index";
			
			HttpSession session = request.getSession();
			session.invalidate();
			
		} else {
			msg = "회원탈퇴 신청 오류! 관리자에게 문의 바랍니다.";
			url = "Member";
			gubun = "myInfo";
		}
		
		request.setAttribute("t_msg", msg);
		request.setAttribute("t_url", url);
		request.setAttribute("t_gubun", gubun);
		request.setAttribute("t_id", id);
		
	}

}
