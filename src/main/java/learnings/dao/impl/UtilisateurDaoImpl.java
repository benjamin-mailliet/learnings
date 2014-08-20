package learnings.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import learnings.dao.DataSourceProvider;
import learnings.dao.UtilisateurDao;
import learnings.model.Utilisateur;

public class UtilisateurDaoImpl implements UtilisateurDao {

	public Utilisateur getUtilisateur(String identifiant) {
		Utilisateur utilisateur = null;
		try {
			Connection connection = DataSourceProvider.getDataSource().getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT id, email, admin FROM utilisateur WHERE email=?");
			stmt.setString(1, identifiant);
			ResultSet results = stmt.executeQuery();
			if (results.next()) {
				utilisateur = new Utilisateur(results.getLong("id"), results.getString("email"), results.getBoolean("admin"));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return utilisateur;
	}

	public String getMotDePasseUtilisateurHashe(String identifiant) {
		String motDePasse = null;
		try {
			Connection connection = DataSourceProvider.getDataSource().getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT motdepasse FROM utilisateur WHERE email=?");
			stmt.setString(1, identifiant);
			ResultSet results = stmt.executeQuery();
			if (results.next()) {
				motDePasse = results.getString("motdepasse");
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return motDePasse;
	}

}
