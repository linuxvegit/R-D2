package controller;

import java.io.IOException;
import java.sql.SQLException;

import source.SourceHolder;
import util.JSONMessage;
import dao.ClubsDao;
import dao.MemberDao;
import dto.Club;
import dto.Member;

public class ClubsController {
	private ClubsDao clubsDao = null;
	private SourceHolder holder = null;

	public ClubsController(SourceHolder holder) {
		clubsDao = new ClubsDao(holder);
		this.holder = holder;
		if (this.holder == null)
			this.holder = SourceHolder.makeSourceHolder();
	}

	public JSONMessage create(String name, String password) {
		name = name.trim().toLowerCase();
		try {
			if (clubsDao.check(name))
				return JSONMessage
						.genErrorJsonMsg("Create club: The club has existed.");
			clubsDao.insert(name);
			MemberDao memberDao = new MemberDao(holder, name);
			int id = clubsDao.update(name);
			memberDao.insert(new Member(1, "admin", password));
			return JSONMessage.genSuccessJsonMsg("Create club: Success!");
		} catch (IOException e) {
			return JSONMessage
					.genErrorJsonMsg("Create club: " + e.getMessage());
		} finally {
			try {
				if (holder != null)
					holder.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public JSONMessage insert(String name) {
		name = name.trim().toLowerCase();
		try {
			if (clubsDao.check(name))
				return JSONMessage
						.genErrorJsonMsg("Create club: The club has existed.");
			clubsDao.insert(name);
			return JSONMessage.genSuccessJsonMsg("Create club: Success!");
		} catch (IOException e) {
			return JSONMessage
					.genErrorJsonMsg("Create club: " + e.getMessage());
		} finally {
			try {
				if (holder != null)
					holder.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public JSONMessage check(String name) {
		name = name.trim().toLowerCase();
		try {
			boolean data = clubsDao.check(name);
			JSONMessage message = JSONMessage
					.genSuccessJsonMsg("Check club: Success!");
			message.setData(data);
			return message;
		} catch (IOException e) {
			return JSONMessage.genErrorJsonMsg("Check club: " + e.getMessage());
		} finally {
			try {
				if (holder != null)
					holder.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public JSONMessage query(String name) {
		name = name.trim().toLowerCase();
		try {
			Club club = clubsDao.query(name);
			JSONMessage message = JSONMessage
					.genSuccessJsonMsg("Query club: Success!");
			message.setData(club);
			return message;
		} catch (IOException e) {
			return JSONMessage.genErrorJsonMsg("Query club: " + e.getMessage());
		} finally {
			try {
				if (holder != null)
					holder.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public JSONMessage update(String name) {
		name = name.trim().toLowerCase();
		try {
			if (!clubsDao.check(name))
				return JSONMessage
						.genErrorJsonMsg("Update club: The club does not exist.");
			clubsDao.update(name);
			return JSONMessage.genSuccessJsonMsg("Update club: Success!");
		} catch (IOException e) {
			return JSONMessage
					.genErrorJsonMsg("Update club: " + e.getMessage());
		} finally {
			try {
				if (holder != null)
					holder.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public JSONMessage delete(String name, int id) {
		name = name.trim().toLowerCase();
		if (id != 1)
			return JSONMessage
					.genErrorJsonMsg("Delete club: You are not allowed to delete club.");
		try {
			if (!clubsDao.check(name))
				return JSONMessage
						.genSuccessJsonMsg("Delete club: The club has been deleted.");
			clubsDao.delete(name);
			return JSONMessage.genSuccessJsonMsg("Delete club: Success!");
		} catch (IOException e) {
			return JSONMessage
					.genErrorJsonMsg("Delete club: " + e.getMessage());
		} finally {
			try {
				if (holder != null)
					holder.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		SourceHolder holder = SourceHolder.makeSourceHolder();
		ClubsController controller = new ClubsController(holder);
		JSONMessage message = controller.create("game", "123");
		System.out.println(message.getMsg());
	}
}
