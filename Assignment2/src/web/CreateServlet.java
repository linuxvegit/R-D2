package web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import source.SourceHolder;
import util.AuthCode;
import util.JSONMessage;
import controller.ClubsController;
import controller.MemberController;
import dto.Member;

/**
 * Servlet implementation class CreateServlet
 */
@WebServlet("/CreateServlet")
public class CreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateServlet() {
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
		request.getSession().setAttribute("CAuthCode", code);

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
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String authCode = request.getParameter("authCode").toLowerCase();

		response.setContentType("text/plain;charset=utf-8");
		PrintWriter out = response.getWriter();

		if (((String) request.getSession().getAttribute("CAuthCode"))
				.toLowerCase().equals(authCode)) {
			SourceHolder holder = SourceHolder.makeSourceHolder();
			ClubsController controller = new ClubsController(holder);
			JSONMessage msg = controller.create(name, password);

			// out.println(name + ":" + password);
			out.print(msg.toJSON());
		} else {
			JSONMessage msg = JSONMessage
					.genErrorJsonMsg("Create club: Wrong auth code");
			out.println(msg.toJSON());
		}

	}

}
