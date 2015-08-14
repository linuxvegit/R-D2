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
 * Servlet implementation class InsertServlet
 */
@WebServlet("/InsertServlet")
public class InsertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InsertServlet() {
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
		Integer newId = Integer.parseInt(request.getParameter("id"));
		String newClub = request.getParameter("club");
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		Date birthday = MyUtil.genDate(request.getParameter("birthday"));
		String contact = request.getParameter("contact");
		String address = request.getParameter("address");
		String addition = request.getParameter("addition");
		int writable = Integer.parseInt(request.getParameter("writable"));

		response.setContentType("text/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		if (session.isNew()) {
			session.invalidate();
			out.print(Static.REDIRECT_JSON);
		} else {
			Integer id = (Integer) session.getAttribute("id");
			String club = (String) session.getAttribute("club");
			if (id == newId && club.equals(newClub)) {
				SourceHolder holder = SourceHolder.makeSourceHolder();
				MemberController controller = new MemberController(holder,
						club, id);
				// generate random password
				String password = MyUtil.genPassword();

				Member newMember = new Member(id, name, password);
				newMember.setWritable(new Permission(writable));
				newMember.setGender(gender);
				newMember.setBirthday(birthday);
				newMember.setContact(contact);
				newMember.setAddress(address);
				newMember.setAddition(addition);
				// Only store the hashcode of password
				newMember.setPassword(MyUtil.passwordHash(password));

				JSONMessage msg = controller.insert(newMember);

				((Member) msg.getData()).setPassword(password);

				out.print(MyUtil.genResult(msg.toJSON()));
			} else {
				session.invalidate();
				out.print(Static.REDIRECT_JSON);
			}
		}
	}
}
