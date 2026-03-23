package command.notice;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import common.CommonExecute;
import common.CommonUtil;
import dao.NoticeDao;
import dto.NoticeDto;

public class NoticeUpdate implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {
		NoticeDao dao = new NoticeDao();
		
		// 임시 저장 경로 사용
		String tempDir = CommonUtil.getNoticeTempDir(request);
		int maxSize = 1024 * 1024 * 10; // 10MB
		MultipartRequest mpr = null;
		
		try {
			mpr = new MultipartRequest(request, tempDir, maxSize,"utf-8", new DefaultFileRenamePolicy());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("공지사항 첨부파일 업로드 오류!");
			request.setAttribute("t_msg", "파일 업로드 실패! 관리자에게 문의하세요.");
			request.setAttribute("t_url", "Notice");
			return;
		}
		
		String no 	 = mpr.getParameter("t_no");
		String title = mpr.getParameter("t_title");
		title = CommonUtil.setQuote(title);
		
		String content = mpr.getParameter("t_content");
		content = CommonUtil.setQuote(content);
		
		String attach = mpr.getFilesystemName("t_attach");
		if(attach == null) attach="";
		
		String dbAttachName = "";
		String deleteAttach = mpr.getParameter("t_delete_checkbox");
		String oriAttach	= mpr.getParameter("t_ori_attach");
		if(oriAttach == null) oriAttach="";
		
		// 기존 첨부파일 삭제 체크박스가 선택된 경우
		if(deleteAttach != null) {
			// SFTP 서버에서 삭제
			boolean deleteSuccess = CommonUtil.deleteNoticeFromSFTP(deleteAttach);
			if(!deleteSuccess) {
				System.out.println("SFTP 공지사항 첨부파일 삭제 오류!");
			}
		} else {
			dbAttachName = oriAttach;
		}
		
		String popupStartDate = mpr.getParameter("popupStartDate");
		String popupEndDate   = mpr.getParameter("popupEndDate");
		if(popupStartDate == null) popupStartDate = "";
		if(popupEndDate == null) popupEndDate = "";
		
		String reg_date = CommonUtil.getTodayTime();
		
		// 새로운 첨부파일이 있는 경우
		if(!attach.equals("")) {
			// 임시 디렉토리의 파일을 SFTP로 업로드
			File tempFile = new File(tempDir, attach);
			boolean uploadSuccess = CommonUtil.uploadNoticeToSFTP(tempFile, attach);
			
			if(!uploadSuccess) {
				System.out.println("공지사항 첨부파일 SFTP 업로드 실패!");
				request.setAttribute("t_msg", "파일 서버 업로드 실패! 관리자에게 문의하세요.");
				request.setAttribute("t_url", "Notice");
				tempFile.delete(); // 임시 파일 삭제
				return;
			}
			
			// 기존 첨부파일이 있다면 SFTP에서 삭제
			if(!oriAttach.equals("")) {
				boolean deleteSuccess = CommonUtil.deleteNoticeFromSFTP(oriAttach);
				if(!deleteSuccess) {
					System.out.println("SFTP 기존 공지사항 첨부파일 삭제 오류!!");
				}
			}
			
			// 임시 파일 삭제
			boolean deleted = tempFile.delete();
			if(!deleted) {
				System.out.println("임시 파일 삭제 실패: " + attach);
			}
			
			dbAttachName = attach;
		}
		
		String type = mpr.getParameter("t_type");
		
		NoticeDto dto = new NoticeDto(
			no,                  
			title,               
			content,             
			dbAttachName,        
			type,                
			popupStartDate,      
			popupEndDate,
			reg_date
		);
		
		int result = dao.noticeUpdate(dto);
		
		// DB 수정 실패 시 새로 업로드한 파일 SFTP에서 삭제
		if(result != 1 && !attach.equals("")) {
			CommonUtil.deleteNoticeFromSFTP(attach);
		}
		
		String msg = result == 1 ? "수정되었습니다.":"수정 실패!";
		
		request.setAttribute("t_msg", msg);
		request.setAttribute("t_url", "Notice");
		request.setAttribute("t_gubun", "view");
		request.setAttribute("t_no", no);			
	}
}