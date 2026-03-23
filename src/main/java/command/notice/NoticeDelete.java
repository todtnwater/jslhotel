package command.notice;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import common.CommonUtil;
import dao.NoticeDao;

/**
 * 공지사항 삭제 Command 클래스
 */
public class NoticeDelete implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {
		NoticeDao dao = new NoticeDao();
		
		String no = request.getParameter("t_no");
		String delAttach = request.getParameter("t_del_attach");
		
		int result = dao.noticeDelete(no);
		
		if(result == 1) {
			// DB 삭제 성공 시 SFTP 서버에서도 첨부파일 삭제
			if(delAttach != null && !delAttach.equals("")) {
				boolean deleteSuccess = CommonUtil.deleteNoticeFromSFTP(delAttach);
				if(!deleteSuccess) {
					System.out.println("공지사항 게시글 삭제 시 SFTP 첨부파일 삭제 오류!");
				}
			}
		}
		
		String msg = result == 1 ? "삭제되었습니다." : "삭제 실패!";
		request.setAttribute("t_msg", msg);
		request.setAttribute("t_url", "Notice");
	}
}