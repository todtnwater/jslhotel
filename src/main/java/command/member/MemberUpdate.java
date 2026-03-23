package command.member;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import common.CommonUtil;
import dao.MemberDao;
import dto.MemberDto;

public class MemberUpdate implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {

		MemberDao dao = new MemberDao();
		
		String id = request.getParameter("t_id");
		String first_name = request.getParameter("t_first_name");
			first_name = CommonUtil.toUpper(first_name);
		String last_name = request.getParameter("t_last_name");
			last_name = CommonUtil.toUpper(last_name);
		String area = request.getParameter("t_area");
		String address = request.getParameter("t_address");
		String email_1 = request.getParameter("t_email_1");
		String email_2 = request.getParameter("t_email_2");
		String mobile_1 = request.getParameter("t_mobile_1");
		String mobile_2 = request.getParameter("t_mobile_2");
		String mobile_3 = request.getParameter("t_mobile_3");
		String gender = request.getParameter("t_gender");
		String sns = request.getParameter("t_sns");
		String update_date = CommonUtil.getTodayTime();
		
		MemberDto dto = new MemberDto(id, "", first_name, last_name, area, address, 
							email_1, email_2, mobile_1, mobile_2, mobile_3, 
							gender, "", sns, "", update_date, "");
		
		int result = dao.memberUpdate(dto);
		
		String msg = result == 1 ? "회원정보 수정 성공":"회원정보 수정 실패!";
		String url = "Member";
		String gubun = "myInfo";

		request.setAttribute("t_msg", msg);
		request.setAttribute("t_url", url);
		request.setAttribute("t_gubun", gubun);
		request.setAttribute("t_id", id);
	}

}
