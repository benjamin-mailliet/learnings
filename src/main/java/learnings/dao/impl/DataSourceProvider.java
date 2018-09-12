package learnings.dao.impl;

import org.mariadb.jdbc.MariaDbDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourceProvider {

	private static class DataSourceProviderHolder {
		private final static DataSourceProvider instance = new DataSourceProvider();
	}
	
	public static DataSourceProvider getInstance() {
		return DataSourceProviderHolder.instance;
	}

	private MariaDbDataSource dataSource;

	private DataSourceProvider() {
		initDataSource();
	}

	private void initDataSource() {
		Properties jdbcProperties = new Properties();
		InputStream configFileStream = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		try {
			jdbcProperties.load(configFileStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			dataSource = new MariaDbDataSource();
			dataSource.setServerName(jdbcProperties.getProperty("servername"));
			dataSource.setPort(Integer.parseInt(jdbcProperties.getProperty("port")));
			dataSource.setDatabaseName(jdbcProperties.getProperty("databasename"));
			dataSource.setUser(jdbcProperties.getProperty("user"));
			dataSource.setPassword(jdbcProperties.getProperty("password"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}
}