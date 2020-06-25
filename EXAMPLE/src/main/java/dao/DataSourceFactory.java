package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.pool.OracleDataSource;
import javax.sql.DataSource;

public class DataSourceFactory {
	private final DataSource daso;
	private static final Logger logger = Logger.getLogger(DataSourceFactory.class.getName());

	private DataSourceFactory() {
		Properties props = new Properties();
		FileInputStream input = null;
		OracleDataSource daso = null;
		String rootPath = Objects
				.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("database.properties"))
				.getPath();
		try {
			input = new FileInputStream(rootPath);
			props.load(input);
			daso = new OracleDataSource();
			daso.setDatabaseName(props.getProperty("database"));
			daso.setServerName(props.getProperty("servername"));
			daso.setURL(props.getProperty("ORACLE_DB_URL"));
			daso.setUser(props.getProperty("ORACLE_DB_USERNAME"));
			daso.setPassword(props.getProperty("ORACLE_DB_PASSWORD"));

		} catch (IOException e) {
			logger.log(Level.SEVERE, "IO Error", e);
		} catch (SQLException e) {

			logger.log(Level.SEVERE, "File database not found", e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, "Failed to close streams", e);
				}
			}

		}

		this.daso = daso;
	}

	public static Connection getConnection() throws SQLException {
		return SingletonHelper.INSTANCE.daso.getConnection();
	}

	private static class SingletonHelper {
		private static final DataSourceFactory INSTANCE = new DataSourceFactory();
	}
}
