package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import source.SourceHolder;
import util.JSONMessage;
import util.MyUtil;
import dao.ClubsDao;
import dao.SearchDao;
import dto.Member;

public class SearchController {
	private SearchDao searchDao = null;
	private SourceHolder holder = null;
	private int userId;

	public SearchController(SourceHolder holder, int userId) {
		this.userId = userId;
		this.holder = holder;
	}

	public JSONMessage getResults(String clubName, String query,
			LinkedHashMap<String, Boolean> orders) {
		try {
			searchDao = new SearchDao(holder, clubName, query, orders);
			List<Member> members = new ArrayList<Member>();
			List<Member> nullMembers = new ArrayList<Member>();
			Member member = null;
			while ((member = searchDao.next()) != null) {
				Member m = Member.remakeMember(member, userId);
				// System.out.println(m);
				if (Member.filterMember(m, orders))
					members.add(m);
				else
					nullMembers.add(m);
				// members.add(Member.remakeMember(member, userId));
				// members.add(member);
			}
			// System.out.println(nullMembers);
			members.addAll(nullMembers);
			JSONMessage message = JSONMessage
					.genSuccessJsonMsg("Search: Success!");
			message.setData(members);
			return message;
		} catch (IOException e) {
			return JSONMessage.genErrorJsonMsg("Search: " + e.getMessage());
			// TODO: handle exception
		} finally {
			try {
				holder.closeConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public JSONMessage getResult(String clubName) {
		try {
			searchDao = new SearchDao(holder, clubName, "id=" + userId, null);
			Member member = searchDao.next();
			JSONMessage message = JSONMessage
					.genSuccessJsonMsg("Search: Success!");
			message.setData(member);
			return message;
		} catch (IOException e) {
			return JSONMessage.genErrorJsonMsg("Search: " + e.getMessage());
			// TODO: handle exception
		} finally {
			try {
				holder.closeConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public JSONMessage loginCheck(String clubName, String password) {
		ClubsDao clubsDao = new ClubsDao(holder);
		try {
			if (!clubsDao.check(clubName))
				return JSONMessage
						.genErrorJsonMsg("Login: Does't have this club.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return JSONMessage.genErrorJsonMsg("Login: Login failed.");
		}

		JSONMessage result = getResult(clubName);
		if (!result.isSuccess())
			return JSONMessage.genErrorJsonMsg("Login: Login failed.");
		if (result.getData() == null)
			return JSONMessage.genErrorJsonMsg("Login: Doesn't have this id.");
		Member member = (Member) result.getData();
		if (member.getPassword().equals(password))
			return JSONMessage.genSuccessJsonMsg();
		else
			return JSONMessage.genErrorJsonMsg("Login: Wrong password.");
	}

	public static void main(String[] args) {
		SourceHolder holder = SourceHolder.makeSourceHolder();
		SearchController controller = new SearchController(holder, 4);
		LinkedHashMap<String, Boolean> map = new LinkedHashMap<String, Boolean>();
		map.put("id", false);
		JSONMessage msg = controller.getResults("basketball", "", map);
		System.out.println(msg.toJSON());
		// List<Member> list = (List<Member>) msg.getData();
		// for (Member member : list) {
		// System.out.println(member);
		// }
	}
}
