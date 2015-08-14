package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import controller.SearchController;

import source.SourceHolder;
import util.JSONMessage;
import util.MyUtil;
import util.Static;

/**
 * Servlet implementation class QueryServlet
 */
@WebServlet("/QueryServlet")
public class QueryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QueryServlet() {
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
		HttpSession session = request.getSession();
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		if (session.isNew()) {
			session.invalidate();
			out.println(Static.REDIRECT_JSON);
		} else {
			String club = (String) session.getAttribute("club");
			Integer id = (Integer) session.getAttribute("id");
			String query = request.getParameter("query");
			LinkedHashMap<String, Boolean> orders = MyUtil.parseOrders(request
					.getParameter("orders"));
			// System.out.println(orders);
			SourceHolder holder = SourceHolder.makeSourceHolder();
			SearchController controller = new SearchController(holder, id);
			JSONMessage msg = controller.getResults(club, query, orders);

			// out.println(name + ":" + password);
			out.print(MyUtil.genResult(msg.toJSON()));
		}
	}

}
