package command.member;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.CommonUtil;
import dao.MemberDao;

/**
 * Servlet implementation class FindId
 */
@WebServlet("/MemberFindId")
public class MemberFindId extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberFindId() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
	
		MemberDao dao = new MemberDao();
		String first_name = request.getParameter("t_first_name");
			first_name = CommonUtil.toUpper(first_name);
		String last_name = request.getParameter("t_last_name");
			last_name = CommonUtil.toUpper(last_name);
		String email_1 = request.getParameter("t_email_1");
		String email_2 = request.getParameter("t_email_2");
		
		String id = dao.memberFindId(first_name, last_name, email_1, email_2);
		
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		
		if(!id.equals("")) out.print("해당 정보의 ID는 ["+id+"] 입니다.");
		else out.print("해당 정보의 ID는 존재하지 않습니다.");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
