package learnings.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DataSourceProvider {

	private MysqlDataSource dataSource;
	
	private static DataSourceProvider instance;
	
	public static DataSourceProvider getInstance() {
		if(instance == null){
			instance = new DataSourceProvider();
		}
		return instance;
	}

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
		
		dataSource = new MysqlDataSource();
		dataSource.setServerName(jdbcProperties.getProperty("servername"));
		dataSource.setPort(Integer.parseInt(jdbcProperties.getProperty("port")));
		dataSource.setDatabaseName(jdbcProperties.getProperty("databasename"));
		dataSource.setUser(jdbcProperties.getProperty("user"));
		dataSource.setPassword(jdbcProperties.getProperty("password"));
	}

	public DataSource getDataSource() {
		return dataSource;
	}
}