package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestOracle {
	public static void test() {
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("Start testing");
			String url = "jdbc:oracle:thin:@172.26.142.249:1521:nekyoiku";
			String user = "ss1508C163";
			String password = "ss1508C163";
			con = DriverManager.getConnection(url, user, password);
			System.out.println("Connection sucessed");
			String sql = "SELECT * FROM table1";
			statement = con.prepareStatement(sql);
			//statement.setString(1, "M");
			result = statement.executeQuery();
			while (result.next()) {
				System.out.println("ID: " + result.getInt("id"));
				System.out.println("NAME: " + result.getString("name"));
				System.out.println("GENDER: " + result.getString("gender"));
				System.out.println("BIRTHDAY: " + result.getDate("birthday"));
				System.out.println("CONTACT: " + result.getString("contact"));
				System.out.println("ADDRESS: " + result.getString("address"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (result != null)
					result.close();
				if (statement != null)
					statement.close();
				if (con != null)
					con.close();
				System.out.println("Database connection has been closed");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		TestOracle.test();
	}
}
