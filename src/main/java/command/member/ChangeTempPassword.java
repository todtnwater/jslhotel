package command.member;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import common.CommonExecute;
import common.CommonUtil;
import dao.MemberDao;

public class ChangeTempPassword implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {

		String msg;
		
		try {
            ChangePasswordEmail emailService = new ChangePasswordEmail();
            boolean emailSent = emailService.sendChangePasswordEmail(request);
            
            if (emailSent) {
                System.out.println("✅ 임시 비밀번호 발송 완료");
                msg = "임시 비밀번호를 발송하였습니다.";
                
                MemberDao dao = new MemberDao();
        		
                HttpSession session = request.getSession();
                
        		String id = request.getParameter("t_id");
        		String pw = (String)session.getAttribute("t_tempPassword");
        		session.removeAttribute("t_tempPassword");
        		
        		System.out.println(id + pw + emailSent);
        		
        			try {
        				pw = dao.encryptSHA256(pw);
        			} catch (NoSuchAlgorithmException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        		
        		String update_date = CommonUtil.getTodayTime();
        		
        		int result = dao.changePassword(id, pw, update_date);
        		
        		if(result != 1) System.out.println("임시 비밀번호로 변경 오류!");
        		
            } else {
                System.out.println("⚠️ 임시 비밀번호 발송 실패");
                msg = "임시 비밀번호 발송 실패, 관리자에게 문의바랍니다.";
            }
        } catch (Exception e) {
            System.out.println("⚠️ 임시 비밀번호 발송 중 오류 : " + e.getMessage());
            msg = "임시 비밀번호 발송 실패, 관리자에게 문의바랍니다.";
            e.printStackTrace();
        }
		

		request.setAttribute("t_msg", msg);
		request.setAttribute("t_url", "Member");
		request.setAttribute("t_gubun", "loginForm");

	}

}
