package command.member;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import common.CommonUtil;
import dao.MemberDao;
import dto.MemberDto;

public class MemberView implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        
        MemberDao dao = new MemberDao();
        String id = CommonUtil.getSessionInfo(request, "id");
        
        // 회원 정보 조회
        MemberDto dto = dao.getMemberInfo(id);
        
        // 포인트 정보 조회 (추가)
        int currentPoint = dao.getMemberPoint(id);
        int totalPoint = dao.getMemberTotalPoint(id);
        
        // 포인트 포맷팅 (1000 → 1,000)
        String formattedPoint = String.format("%,d", currentPoint);
        String formattedTotalPoint = String.format("%,d", totalPoint);

        
        // request에 담기
        request.setAttribute("dto", dto);
        request.setAttribute("currentPoint", currentPoint);          // 숫자 (계산용)
        request.setAttribute("totalPoint", totalPoint);              // 숫자 (계산용)
        request.setAttribute("formattedPoint", formattedPoint);      // 문자열 (표시용)
        request.setAttribute("formattedTotalPoint", formattedTotalPoint);  // 문자열 (표시용)
    }
}