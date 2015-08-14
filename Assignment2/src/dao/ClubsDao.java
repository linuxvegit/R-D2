package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.Club;

import source.SourceHolder;

public class ClubsDao {

	private SourceHolder holder;

	public ClubsDao(SourceHolder holder) {
		this.holder = holder;
	}

	// 在插入前必须检查是否存在
	public void insert(String name) throws IOException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = holder.getConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(INSERT_SQL);
			statement.setString(1, name);
			statement.executeUpdate();
			statement = connection.prepareStatement(CREATE_SQL1 + name
					+ CREATE_SQL2);
			statement.executeUpdate();
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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

	public void insert(Club club) throws IOException {
		insert(club.getName());
	}

	public boolean check(Club club) throws IOException {
		return check(club.getName());
	}

	public boolean check(String name) throws IOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = holder.getConnection();
			statement = connection.prepareStatement(QUERY_SQL);
			statement.setString(1, name);
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

	public Club query(String name) throws IOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = holder.getConnection();
			statement = connection.prepareStatement(QUERY_SQL);
			statement.setString(1, name);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return new Club(name, resultSet.getInt("newid"));
			}
			return null;

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

	public Club query(Club club) throws IOException {
		return query(club.getName());
	}

	// 更新之前需要检查是否存在
	public int update(String name) throws IOException {
		int id = query(name).getNewId();
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = holder.getConnection();
			statement = connection.prepareStatement(UPDATE_SQL);
			statement.setLong(1, id + 1);
			statement.setString(2, name);
			statement.executeUpdate();
			return id;
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

	// 删除前检查是否存在
	public void delete(String name) throws IOException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = holder.getConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(DELETE_ROW);
			statement.setString(1, name);
			statement.executeUpdate();
			statement = connection.prepareStatement(DELETE_TABLE + name);
			statement.executeUpdate();
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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

	private static final String INSERT_SQL = "insert into clubs values(?,default)";
	private static final String QUERY_SQL = "select newid from clubs where name=?";
	private static final String UPDATE_SQL = "update clubs set newid=? where name=?";
	private static final String DELETE_ROW = "delete from clubs where name=?";
	private static final String DELETE_TABLE = "drop table ";
	private static final String CREATE_SQL1 = "CREATE TABLE ";
	private static final String CREATE_SQL2 = "(" + //
			"Id int primary key," + //
			"Name nvarchar2(255) not null," + //
			"Password varchar(255) not null," + //
			"Gender nvarchar2(255)," + //
			"Birthday date," + //
			"Contact nvarchar2(255)," + //
			"Address nvarchar2(255)," + //
			"JoinDate date not null," + //
			"Addition nvarchar2(255)," + //
			"Readable int default 253," + //
			"Writable int default 255" + ")";//

	public static void main(String[] args) {
		SourceHolder holder = SourceHolder.makeSourceHolder();
		ClubsDao clubsDao = null;
		try {
			clubsDao = new ClubsDao(holder);
			// System.out.println(clubsDao.query("tennis"));
			clubsDao.delete("tensnis");
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
