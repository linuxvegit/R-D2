package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.Member;
import source.SourceHolder;

public class MemberDao {
	private SourceHolder holder;
	private String clubName;

	public MemberDao(SourceHolder holder, String clubName) {
		this.holder = holder;
		this.clubName = clubName;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public boolean check(Member member) throws IOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = holder.getConnection();
			statement = connection.prepareStatement(QUERY_SQL1 + clubName
					+ QUERY_SQL2);
			statement.setInt(1, member.getId());
			resultSet = statement.executeQuery();
			return resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IOException("Database Error Occurred.");
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
	}

	// 插入前需要检查是否已经存在
	public void insert(Member member) throws IOException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = holder.getConnection();
			statement = connection.prepareStatement(INSERT_SQL1 + clubName
					+ INSERT_SQL2);
			statement.setInt(1, member.getId());
			statement.setString(2, member.getName());
			statement.setString(3, member.getPassword());
			statement.setString(4, member.getGender());
			statement.setDate(5, new Date(member.getBirthday().getTime()));
			statement.setString(6, member.getContact());
			statement.setString(7, member.getAddress());
			statement.setDate(8, new Date(member.getJoinDate().getTime()));
			statement.setString(9, member.getAddition());
			statement.setInt(10, member.getReadable().toValue());
			statement.setInt(11, member.getWritable().toValue());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IOException("Database Error Occurred.");
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
	}

	// 更新内容前需要检查是否已经存在
	public void update(Member member) throws IOException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = holder.getConnection();
			statement = connection.prepareStatement(UPDATE_SQL1 + clubName
					+ UPDATE_SQL2);

			statement.setString(1, member.getName());
			statement.setString(2, member.getPassword());
			statement.setString(3, member.getGender());
			if (member.getBirthday() != null)
				statement.setDate(4, new Date(member.getBirthday().getTime()));
			else
				statement
						.setDate(4, new Date((new java.util.Date()).getTime()));
			statement.setString(5, member.getContact());
			statement.setString(6, member.getAddress());
			statement.setString(7, member.getAddition());
			statement.setInt(8, member.getReadable().toValue());
			statement.setInt(9, member.getWritable().toValue());
			statement.setInt(10, member.getId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IOException("Database Error Occurred.");
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
	}

	// 更新前检查是否存在
	public void password(Member member) throws IOException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = holder.getConnection();
			statement = connection.prepareStatement(PASSWORD1 + clubName
					+ PASSWORD2);
			// System.out.println(PASSWORD1 + clubName + PASSWORD2);

			statement.setString(1, member.getPassword());
			statement.setInt(2, member.getId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IOException("Database Error Occurred.");
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
	}

	public void delete(Member member) throws IOException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = holder.getConnection();
			statement = connection.prepareStatement(DELETE_ROW1 + clubName
					+ DELETE_ROW2);
			statement.setInt(1, member.getId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IOException("Database Error Occurred.");
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
	}

	private static final String INSERT_SQL1 = "insert into ";
	private static final String INSERT_SQL2 = " values(?,?,?,?,?,?,?,?,?,?,?)";
	private static final String UPDATE_SQL1 = "update ";
	private static final String UPDATE_SQL2 = " set name=?,password=?,gender=?,birthday=?,contact=?,address=?,addition=?,readable=?,writable=? where id=?";
	private static final String QUERY_SQL1 = "select id from ";
	private static final String QUERY_SQL2 = " where id=?";
	private static final String DELETE_ROW1 = "delete from ";
	private static final String DELETE_ROW2 = " where id=?";
	private static final String PASSWORD1 = "update ";
	private static final String PASSWORD2 = " set password=? where id=?";

	public static void main(String[] args) {
		SourceHolder holder = SourceHolder.makeSourceHolder();
		MemberDao memberDao = null;
		try {
			// Member member = new Member(5);
			// member.setAddition("Hello");
			// member.setAddress("中国");
			memberDao = new MemberDao(holder, "basketball");
			memberDao.delete(new Member(7));
			// memberDao.update(member);
			// System.out.println(memberDao.check(new Member(20)));
			// memberDao = new MemberDao(holder, "basketball");
			// for (int i = 1; i <= 15; i++)
			// memberDao.insert(new Member(i));
			// System.out.println(clubsDao.query("tennis"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				holder.closeConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
