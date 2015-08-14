package web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import source.SourceHolder;
import util.AuthCode;
import util.JSONMessage;
import controller.ClubsController;
import controller.SearchController;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
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
		AuthCode authCode = new AuthCode();
		String code = authCode.getCode();
		request.getSession().setAttribute("AuthCode", code);

		response.setContentType("image/jpeg");

		response.setDateHeader("expires", -1);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		ImageIO.write(authCode.getBuffer(), "jpg", response.getOutputStream());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String club = request.getParameter("club");
		int id = Integer.parseInt(request.getParameter("id"));
		String password = request.getParameter("password");
		String authCode = request.getParameter("authCode").toLowerCase();

		response.setContentType("text/plain;charset=utf-8");
		PrintWriter out = response.getWriter();

		if (((String) request.getSession().getAttribute("AuthCode"))
				.toLowerCase().equals(authCode)) {
			SourceHolder holder = SourceHolder.makeSourceHolder();
			SearchController controller = new SearchController(holder, id);
			JSONMessage msg = controller.loginCheck(club, password);
			if (msg.isSuccess()) {
				HttpSession session = request.getSession();
				session.setAttribute("club", club);
				session.setAttribute("id", id);
			}

			// out.println(name + ":" + password);
			out.print(msg.toJSON());
		} else {
			JSONMessage msg = JSONMessage
					.genErrorJsonMsg("Login: Wrong auth code");
			out.println(msg.toJSON());
		}
	}

}
