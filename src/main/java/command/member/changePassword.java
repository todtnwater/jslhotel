package command.member;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import common.CommonExecute;
import common.CommonUtil;
import dao.MemberDao;

public class changePassword implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {
		
		MemberDao dao = new MemberDao();
		
		String id = request.getParameter("t_id");
		String pw = request.getParameter("t_new_password");
			try {
				pw = dao.encryptSHA256(pw);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		String update_date = CommonUtil.getTodayTime();
		
		int result = dao.changePassword(id, pw, update_date);
		
		String msg, gubun;
		String url = "Member";

		if(result == 1) {
			msg = "비밀번호 변경을 완료하였습니다.";
			gubun = "loginForm";
			
			HttpSession session = request.getSession();
			session.invalidate();
		} else {
			msg = "비밀번호 변경 실패! 관리자에게 문의 바랍니다.";
			gubun = "password";
		}
		
		request.setAttribute("t_msg", msg);
		request.setAttribute("t_url", url);
		request.setAttribute("t_gubun", gubun);
		request.setAttribute("t_id", id); 
		
	}

}
