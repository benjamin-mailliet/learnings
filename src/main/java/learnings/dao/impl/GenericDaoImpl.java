package learnings.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;

import learnings.dao.DataSourceProvider;

public abstract class GenericDaoImpl {

	protected Connection getConnection() throws SQLException {
		return DataSourceProvider.getInstance().getDataSource().getConnection();
	}

}
