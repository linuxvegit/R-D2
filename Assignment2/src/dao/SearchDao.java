package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import dto.Member;

import source.SourceHolder;
import sun.print.resources.serviceui;

public class SearchDao {
	private PreparedStatement statement = null;
	private ResultSet resultSet = null;

	public SearchDao(SourceHolder holder, String clubName, String query,
			LinkedHashMap<String, Boolean> orders) throws IOException {
		// System.out.println(orders);
		StringBuffer sql = new StringBuffer(SQL1 + clubName);
		if (query != null && query != "")
			sql.append(SQL2 + query);
		if (orders != null && (!orders.isEmpty())) {
			sql.append(SQL3);
			for (Map.Entry<String, Boolean> order : orders.entrySet()) {
				sql.append(order.getKey()
						+ ((order.getValue()) ? " asc" : " desc"));
				sql.append(",");
			}
			sql.delete(sql.length() - 1, sql.length());
		}
		// System.out.println(sql.toString());
		try {
			Connection connection = holder.getConnection();
			statement = connection.prepareStatement(sql.toString());
			resultSet = statement.executeQuery();
		} catch (SQLException e) {
			throw new IOException("Database Error Occurred.");
		}
	}

	public Member next() throws IOException {
		if (resultSet == null)
			throw new IllegalAccessError(
					"Resultset has already closed or not prepared.");
		Member member = null;
		try {
			if (resultSet.next())
				member = Member.makeMember(resultSet);
			return member;
		} catch (SQLException e) {
			throw new IOException("Database Error Occurred.");
		}
	}

	private static final String SQL1 = "select * from ";
	private static final String SQL2 = " where ";
	private static final String SQL3 = " order by ";

	public static void main(String[] args) {
		SourceHolder holder = SourceHolder.makeSourceHolder();
		LinkedHashMap<String, Boolean> map = new LinkedHashMap<String, Boolean>();
		map.put("id", false);
		try {
			SearchDao searchDao = new SearchDao(holder, "basketball", "", null);
			Member member;
			while ((member = searchDao.next()) != null) {
				System.out.println(member);
			}
		} catch (IOException e) {
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
