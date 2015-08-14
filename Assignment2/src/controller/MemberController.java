package controller;

import java.io.IOException;
import java.sql.SQLException;

import dao.ClubsDao;
import dao.MemberDao;
import dto.Member;
import source.SourceHolder;
import util.JSONMessage;

public class MemberController {
	private MemberDao memberDao = null;
	private ClubsDao clubsDao = null;
	private String clubName = null;
	private int userId;
	private SourceHolder holder = null;

	public MemberController(SourceHolder holder, String clubName, int userId) {
		memberDao = new MemberDao(holder, clubName);
		clubsDao = new ClubsDao(holder);
		this.userId = userId;
		this.clubName = clubName;
		this.holder = holder;
		if (this.holder == null)
			this.holder = SourceHolder.makeSourceHolder();
	}

	public JSONMessage insert(Member member) {
		if (userId != 1)
			return JSONMessage.genErrorJsonMsg("No permission to add member.");
		try {
			if (!clubsDao.check(clubName))
				return JSONMessage
						.genErrorJsonMsg("Member insert: The club does not exit.");
			int id = clubsDao.update(clubName);
			member.setId(id);
			memberDao.insert(member);
			JSONMessage msg = JSONMessage
					.genSuccessJsonMsg("Member insert: Success!");
			String ps = member.getPassword();
			member = Member.remakeMember(member, 1);
			member.setPassword(ps);
			msg.setData(member);
			return msg;
		} catch (IOException e) {
			return JSONMessage.genErrorJsonMsg("Member insert: "
					+ e.getMessage());
		} finally {
			try {
				if (holder != null)
					holder.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public JSONMessage update(Member member) {
		if (userId != member.getId())
			return JSONMessage
					.genErrorJsonMsg("No permission to update other member's information");
		try {
			if (!memberDao.check(member))
				return JSONMessage
						.genErrorJsonMsg("Member update: The member does not exist.");
			memberDao.update(member);
			JSONMessage msg = JSONMessage
					.genSuccessJsonMsg("Member update: Success!");
			msg.setData(Member.remakeMember(member, userId));
			return msg;
		} catch (IOException e) {
			return JSONMessage.genErrorJsonMsg("Member update: "
					+ e.getMessage());
		} finally {
			try {
				if (holder != null)
					holder.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public JSONMessage password(Member member) {
		if (userId != member.getId())
			return JSONMessage
					.genErrorJsonMsg("No permission to update other member's password");
		try {
			if (!memberDao.check(member))
				return JSONMessage
						.genErrorJsonMsg("Password update: The member does not exist.");
			memberDao.password(member);
			JSONMessage msg = JSONMessage
					.genSuccessJsonMsg("Password update: Success!");
			return msg;
		} catch (IOException e) {
			return JSONMessage.genErrorJsonMsg("Password update: "
					+ e.getMessage());
		} finally {
			try {
				if (holder != null)
					holder.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public JSONMessage delete(Member member) {
		if (userId != 1 || member.getId() == 1)
			return JSONMessage
					.genErrorJsonMsg("No permission to delete this member.");
		try {
			if (!memberDao.check(member))
				return JSONMessage
						.genSuccessJsonMsg("Member delete: The member has been deleted.");
			memberDao.delete(member);
			return JSONMessage.genSuccessJsonMsg("Member delete: Success!");
		} catch (IOException e) {
			return JSONMessage.genErrorJsonMsg("Member delete: "
					+ e.getMessage());
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
		MemberController controller = new MemberController(holder,
				"basketball", 1);
		Member member = new Member(2, "MXK", "034014");
		member.setAddress("nanjing");
		JSONMessage message = controller.insert(member);
		System.out.println(message.toJSON());
	}
}
