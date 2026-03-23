package command.member;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.tribes.membership.Membership;

import common.CommonExecute;
import common.CommonUtil;
import dao.MemberDao;
import dao.MembershipDao;
import dto.MemberDto;

public class MemberSave implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {

		MemberDao dao = new MemberDao();
		MembershipDao dao2 = new MembershipDao();
		
		String id = request.getParameter("t_id");
		String password = request.getParameter("t_password");
		try {
			password = dao.encryptSHA256(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.out.println("비밀번호 암호화 오류");
		}
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
		String reg_date = CommonUtil.getTodayTime();
		
		MemberDto dto = new MemberDto(id, password, first_name, last_name, 
											area, address, email_1, email_2, 
											mobile_1, mobile_2, mobile_3, 
											gender, sns, reg_date);
		
		int result = dao.memberSave(dto);
		
		String msg = result == 1 ? "회원가입 완료하였습니다.":"회원가입에 실패하였습니다.";
		String url = "Member";
		String gubun = result == 1 ? "loginForm":"join";
		
		if(result == 1) {
			String no = dao2.getMembersnipNo();
			int result2 = dao2.membershipSave(no, id, reg_date);
			if(result2 != 1) System.out.println("멤버십 등록 오류!");
		}
		
		request.setAttribute("t_msg", msg);
		request.setAttribute("t_url", url);
		request.setAttribute("t_gubun", gubun);
		
	}

}
