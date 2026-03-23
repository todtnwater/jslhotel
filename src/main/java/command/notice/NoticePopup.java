package command.notice;

import javax.servlet.http.HttpServletRequest;
import common.CommonExecute;
import dao.NoticeDao;
import dto.NoticeDto;

/**
 * 인덱스 페이지에 팝업 공지사항을 표시하기 위한 Command 클래스
 * 현재 날짜 기준으로 활성화된 팝업 공지를 조회
 */
public class NoticePopup implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        NoticeDao dao = new NoticeDao();
        
        // 현재 활성화된 팝업 공지 조회
        NoticeDto popupNotice = dao.getActivePopupNotice();
        
        // 팝업 공지가 있으면 request에 설정
        if(popupNotice != null) {
            request.setAttribute("popupNotice", popupNotice);
        }
    }
}