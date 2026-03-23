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

public class NoticeSave implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        NoticeDao dao = new NoticeDao();
     
        // 1. 첨부파일 임시 저장 경로 및 크기 설정
        String tempDir = CommonUtil.getNoticeTempDir(request);
        int maxSize = 1024 * 1024 * 10; // 10MB
        
        MultipartRequest mpr = null;
        try {
            // 2. 파일 업로드 및 MultipartRequest 객체 생성 (임시 디렉토리에)
            mpr = new MultipartRequest(request, 
                                       tempDir, 
                                       maxSize,
                                       "utf-8", 
                                       new DefaultFileRenamePolicy());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("공지사항 첨부파일 업로드 오류!");
            request.setAttribute("t_msg", "파일 업로드 실패! 관리자에게 문의하세요.");
            request.setAttribute("t_url", "Notice");
            return; 
        }

        // 3. mpr 객체에서 모든 폼 데이터 추출
        String title = mpr.getParameter("t_title");
        title = CommonUtil.setQuote(title); 
        
        String content = mpr.getParameter("t_content");
        content = CommonUtil.setQuote(content);
        
        String position = mpr.getParameter("t_position");
        String type = mpr.getParameter("t_type");
        String reg_id = CommonUtil.getSessionInfo(request, "id");
        String reg_date = CommonUtil.getTodayTime();
        String popupStartDate = mpr.getParameter("popupStartDate");
        String popupEndDate = mpr.getParameter("popupEndDate");
        
        // 4. 첨부 파일명 획득
        String attach = mpr.getFilesystemName("t_attach");
        if(attach == null) attach = ""; 
        
        // 5. 첨부파일이 있으면 SFTP 서버로 업로드
        if(!attach.equals("")) {
            File tempFile = new File(tempDir, attach);
            boolean uploadSuccess = CommonUtil.uploadNoticeToSFTP(tempFile, attach);
            
            if(!uploadSuccess) {
                System.out.println("공지사항 첨부파일 SFTP 업로드 실패!");
                request.setAttribute("t_msg", "파일 서버 업로드 실패! 관리자에게 문의하세요.");
                request.setAttribute("t_url", "Notice");
                tempFile.delete(); // 임시 파일 삭제
                return;
            }
            
            // SFTP 업로드 성공 후 임시 파일 삭제
            boolean deleted = tempFile.delete();
            if(!deleted) {
                System.out.println("임시 파일 삭제 실패: " + attach);
            }
        }
        
        // 6. 다음 게시글 번호 획득
        String no = dao.getNo();
        
        // 7. DTO 생성 및 DB 저장
        NoticeDto dto = new NoticeDto(no, title, content, attach, reg_id, position, reg_date, type, popupStartDate, popupEndDate);	
       
        // 팝업 공지 등록 시 기존 팝업 일반 공지로 전환
        if(type.equals("popup")) {
        	int result = dao.noticeChange();
        	if (result < 0) System.out.println("기존 팝업 공지 전환 오류!");
        }
        
        int result = dao.noticeSave(dto); 
        
        // DB 저장 실패 시 SFTP에서도 삭제
        if(result != 1 && !attach.equals("")) {
            CommonUtil.deleteNoticeFromSFTP(attach);
        }
        
        String msg = result == 1 ? "공지사항 등록 되었습니다.":"등록 실패! DB 처리 오류.";
        request.setAttribute("t_msg", msg);
        request.setAttribute("t_url", "Notice");
    }
}