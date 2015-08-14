package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import source.SourceHolder;
import util.JSONMessage;
import util.MyUtil;
import util.Static;
import controller.MemberController;
import controller.SearchController;
import dto.Member;
import dto.Permission;

/**
 * Servlet implementation class UpdateServlet
 */
@WebServlet("/UpdateServlet")
public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		response.setContentType("text/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		if (session.isNew()) {
			session.invalidate();
			out.print(Static.REDIRECT_JSON);
		} else {

			Integer id = (Integer) session.getAttribute("id");
			String club = (String) session.getAttribute("club");

			Integer newId = Integer.parseInt(request.getParameter("id"));
			String newClub = request.getParameter("club");

			// System.out.println(id);
			// System.out.println(club);
			// System.out.println(newId);
			// System.out.println(newClub);

			if (newId == id && newClub.equals(club)) {

				Member member = new Member(id);

				member.setName(request.getParameter("name"));
				member.setPassword(request.getParameter("password"));
				member.setGender(request.getParameter("gender"));
				long birthday = Long
						.parseLong(request.getParameter("birthday"));
				member.setBirthday(new Date(birthday));
				member.setContact(request.getParameter("contact"));
				member.setAddress(request.getParameter("address"));
				member.setAddition(request.getParameter("addition"));
				int readable = Integer.parseInt(request
						.getParameter("readable"));

				// System.out.println(readable);

				member.setReadable(new Permission(readable));
				SourceHolder holder = SourceHolder.makeSourceHolder();
				MemberController controller = new MemberController(holder,
						club, id);
				JSONMessage msg = controller.update(member);

				// out.println(name + ":" + password);
				out.print(MyUtil.genResult(msg.toJSON()));
			} else {
				session.invalidate();
				out.print(Static.REDIRECT_JSON);
			}
		}
	}

}
