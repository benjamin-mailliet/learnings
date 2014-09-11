package learnings.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import learnings.dao.UtilisateurDao;
import learnings.exceptions.LearningsSQLException;
import learnings.model.Utilisateur;

public class UtilisateurDaoImpl extends GenericDaoImpl implements UtilisateurDao {

	public List<Utilisateur> listerUtilisateurs() {
		List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();
		try {
			Connection connection = getConnection();
			Statement stmt = connection.createStatement();
			ResultSet results = stmt.executeQuery("SELECT id, email, admin FROM utilisateur ORDER BY email");
			while (results.next()) {
				utilisateurs.add(new Utilisateur(results.getLong("id"), results.getString("email"), results.getBoolean("admin")));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			throw new LearningsSQLException(e);
		}
		return utilisateurs;
	}

	@Override
	public List<Utilisateur> listerAutresEleves(Long id) {
		List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT id, email, admin FROM utilisateur WHERE admin = ? AND id != ? ORDER BY email");
			stmt.setBoolean(1, false);
			stmt.setLong(2, id);
			ResultSet results = stmt.executeQuery();
			while (results.next()) {
				utilisateurs.add(new Utilisateur(results.getLong("id"), results.getString("email"), results.getBoolean("admin")));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			throw new LearningsSQLException(e);
		}
		return utilisateurs;
	}

	@Override
	public Utilisateur getUtilisateur(Long id) {
		Utilisateur utilisateur = null;
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT id, email, admin FROM utilisateur WHERE id=?");
			stmt.setLong(1, id);
			ResultSet results = stmt.executeQuery();
			if (results.next()) {
				utilisateur = new Utilisateur(results.getLong("id"), results.getString("email"), results.getBoolean("admin"));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			throw new LearningsSQLException(e);
		}
		return utilisateur;
	}

	public Utilisateur getUtilisateur(String identifiant) {
		Utilisateur utilisateur = null;
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT id, email, admin FROM utilisateur WHERE email=?");
			stmt.setString(1, identifiant);
			ResultSet results = stmt.executeQuery();
			if (results.next()) {
				utilisateur = new Utilisateur(results.getLong("id"), results.getString("email"), results.getBoolean("admin"));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			throw new LearningsSQLException(e);
		}

		return utilisateur;
	}

	public String getMotDePasseUtilisateurHashe(String identifiant) {
		String motDePasse = null;
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT motdepasse FROM utilisateur WHERE email=?");
			stmt.setString(1, identifiant);
			ResultSet results = stmt.executeQuery();
			if (results.next()) {
				motDePasse = results.getString("motdepasse");
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			throw new LearningsSQLException(e);
		}

		return motDePasse;
	}

	@Override
	public void modifierMotDePasse(Long id, String motDePasse) {
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("UPDATE utilisateur SET motdepasse=? WHERE id=?");
			stmt.setString(1, motDePasse);
			stmt.setLong(2, id);
			stmt.executeUpdate();
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			throw new LearningsSQLException(e);
		}
	}

	@Override
	public void supprimerUtilisateur(Long id) {
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("DELETE FROM  utilisateur WHERE id=?");
			stmt.setLong(1, id);
			stmt.executeUpdate();
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			throw new LearningsSQLException(e);
		}
	}

	@Override
	public void modifierRoleAdmin(Long id, boolean admin) {
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("UPDATE utilisateur SET admin=? WHERE id=?");
			stmt.setBoolean(1, admin);
			stmt.setLong(2, id);
			stmt.executeUpdate();
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			throw new LearningsSQLException(e);
		}
	}

	@Override
	public Utilisateur ajouterUtilisateur(String email, String motDePasse, boolean admin) {
		Utilisateur utilisateur = null;
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO utilisateur(email, motdepasse, admin) VALUES(?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, email);
			stmt.setString(2, motDePasse);
			stmt.setBoolean(3, admin);
			stmt.executeUpdate();

			ResultSet ids = stmt.getGeneratedKeys();
			if (ids.next()) {
				utilisateur = new Utilisateur(ids.getLong(1), email, admin);
			}

			stmt.close();
			connection.close();
		} catch (SQLException e) {
			throw new LearningsSQLException(e);
		}
		return utilisateur;
	}
}
