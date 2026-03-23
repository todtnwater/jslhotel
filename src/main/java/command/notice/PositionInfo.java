package command.notice;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import common.CommonUtil;
import dao.NoticeDao;

public class PositionInfo implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {
		NoticeDao dao = new NoticeDao();
		
		String id = CommonUtil.getSessionInfo(request, "id");		
		String position = dao.getPosition(id);
		
		request.setAttribute("t_position", position);
	}

}