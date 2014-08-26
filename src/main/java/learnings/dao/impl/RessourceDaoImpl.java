package learnings.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import learnings.dao.DataSourceProvider;
import learnings.dao.RessourceDao;
import learnings.model.Cours;
import learnings.model.Ressource;

public class RessourceDaoImpl implements RessourceDao {

	@Override
	public List<Ressource> getRessourcesByCours(Cours cours) {
		List<Ressource> listeRessources = new ArrayList<Ressource>();
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT r.id, r.titre, r.chemin FROM ressource r WHERE r.cours_id=? ORDER BY titre ASC");
			stmt.setLong(1, cours.getId());
			ResultSet results = stmt.executeQuery();
			while (results.next()) {
				listeRessources.add(new Ressource(results.getLong("id"), results.getString("titre"), results.getString("chemin"), cours));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listeRessources;
	}

	private Connection getConnection() throws SQLException {
		return DataSourceProvider.getInstance().getDataSource().getConnection();
	}
}
