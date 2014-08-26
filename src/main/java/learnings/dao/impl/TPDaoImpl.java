package learnings.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import learnings.dao.DataSourceProvider;
import learnings.dao.TPDao;
import learnings.model.TP;

public class TPDaoImpl implements TPDao {

	@Override
	public List<TP> listerTPs() {
		List<TP> tps = new ArrayList<TP>();
		try {
			Connection connection = getConnection();
			Statement stmt = connection.createStatement();
			ResultSet results = stmt.executeQuery("SELECT id, titre, description, isnote, datelimiterendu, date FROM tp ORDER BY date DESC");
			while (results.next()) {
				tps.add(new TP(results.getLong("id"), results.getString("titre"), results.getString("description"), results.getBoolean("isnote"), results
						.getDate("datelimiterendu"), results.getDate("date")));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tps;
	}

	private Connection getConnection() throws SQLException {
		return DataSourceProvider.getInstance().getDataSource().getConnection();
	}
}
