package web;

import java.io.IOException;
import java.io.PrintWriter;

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
import controller.ClubsController;
import controller.MemberController;
import dto.Member;

/**
 * Servlet implementation class DismissServlet
 */
@WebServlet("/DismissServlet")
public class DismissServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DismissServlet() {
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

			if (newId == id && newClub.equals(club)) {

				SourceHolder holder = SourceHolder.makeSourceHolder();
				ClubsController controller = new ClubsController(holder);
				JSONMessage msg = controller.delete(club, id);

				// response.setContentType("text/plain;charset=utf-8");
				// out.println(name + ":" + password);
				out.print(MyUtil.genResult(msg.toJSON()));
			} else {
				session.invalidate();
				out.print(Static.REDIRECT_JSON);
			}
		}
	}

}
