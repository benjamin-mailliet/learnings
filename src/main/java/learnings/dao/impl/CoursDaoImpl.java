package learnings.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import learnings.dao.CoursDao;
import learnings.dao.DataSourceProvider;
import learnings.model.Cours;

public class CoursDaoImpl implements CoursDao {

	@Override
	public List<Cours> listerCours() {
		List<Cours> listeCours = new ArrayList<Cours>();
		try {
			Connection connection = getConnection();
			Statement stmt = connection.createStatement();
			ResultSet results = stmt.executeQuery("SELECT id, titre, description , date FROM cours ORDER BY date DESC");
			while (results.next()) {
				listeCours.add(new Cours(results.getLong("id"), results.getString("titre"), results.getString("description"), results.getDate("date")));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listeCours;
	}

	private Connection getConnection() throws SQLException {
		return DataSourceProvider.getInstance().getDataSource().getConnection();
	}
}
