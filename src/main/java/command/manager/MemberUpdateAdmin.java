package command.manager;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import common.CommonExecute;
import common.CommonUtil;
import dao.ManagerDao;
import dao.MemberDao;
import dto.ManagerDto;
import dto.MemberDto;

public class MemberUpdateAdmin implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {
		
		ManagerDao dao = new ManagerDao();
		MemberDao dao2 = new MemberDao();
		
		String id = request.getParameter("t_id");
		String password = request.getParameter("t_password");
			if(password == null) password = "";
			if(!password.equals("")) {
				try {
					password = dao2.encryptSHA256(password);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		String first_name = request.getParameter("t_first_name");
			first_name = CommonUtil.toUpper(first_name);
		String last_name = request.getParameter("t_last_name");
			last_name = CommonUtil.toUpper(last_name);
		String area = request.getParameter("t_area");
		String rank = request.getParameter("t_rank");
		String position = request.getParameter("t_position");
		String address = request.getParameter("t_address");
		String email_1 = request.getParameter("t_email_1");
		String email_2 = request.getParameter("t_email_2");
		String mobile_1 = request.getParameter("t_mobile_1");
		String mobile_2 = request.getParameter("t_mobile_2");
		String mobile_3 = request.getParameter("t_mobile_3");
		String gender = request.getParameter("t_gender");
		String sns = request.getParameter("t_sns");
		String update_date = CommonUtil.getTodayTime();
		
		ManagerDto dto = new ManagerDto(id, password, first_name, last_name, area, address, email_1, email_2, mobile_1, mobile_2, mobile_3, gender, rank, sns, position, "", update_date, "");
		
		int result = dao.memberUpdate(dto);
		
		String msg = result == 1 ? "회원정보 수정 성공":"회원정보 수정 실패!";
		String url = "Manager";
		String gubun = "memberView";

		// 세션아이디의 등급을 하향 조정할 경우 로그아웃
		String sessionId = CommonUtil.getSessionInfo(request, "id");
		if(result == 1 && sessionId.equals(id) && rank.equals("member")) {
			HttpSession session = request.getSession();
			session.invalidate();
			msg = "권한 변경 성공! 로그아웃됩니다.";
			url = "Index";
			gubun = "index";
		}
		
		request.setAttribute("t_msg", msg);
		request.setAttribute("t_url", url);
		request.setAttribute("t_gubun", gubun);
		request.setAttribute("t_id", id);

	}

}
