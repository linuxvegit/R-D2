package source;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class SourceHolder {
	private static ComboPooledDataSource ds = null;

	static {
		ds = new ComboPooledDataSource(true);
	}

	private Connection connection = null;

	public Connection getConnection() throws SQLException {
		if (connection == null)
			connection = ds.getConnection();
		return connection;
	}

	public void closeConnection() throws SQLException {
		if (connection == null)
			return;
		connection.close();
		connection = null;
	}

	public static SourceHolder makeSourceHolder() {
		return new SourceHolder();
	}

}
