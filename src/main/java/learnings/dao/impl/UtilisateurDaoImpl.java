package learnings.dao.impl;

import learnings.dao.UtilisateurDao;
import learnings.enums.Groupe;
import learnings.exceptions.LearningsSQLException;
import learnings.model.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
public class UtilisateurDaoImpl extends GenericDaoImpl implements UtilisateurDao {

	public List<Utilisateur> listerUtilisateurs() {
		List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();
		try {
			Connection connection = getConnection();
			Statement stmt = connection.createStatement();
			ResultSet results = stmt.executeQuery("SELECT id, nom, prenom, email, groupe, admin FROM utilisateur ORDER BY email");
			while (results.next()) {

				utilisateurs.add(mapperVersUtilisateur(results));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			throw new LearningsSQLException(e);
		}
		return utilisateurs;
	}

	private Utilisateur mapperVersUtilisateur(ResultSet results) throws SQLException {
		Groupe groupe = null;
		if (results.getString("groupe") != null) {
			groupe = Groupe.valueOf(results.getString("groupe"));
		}
		return new Utilisateur(results.getLong("id"), results.getString("nom"), results.getString("prenom"), results.getString("email"),
                groupe, results.getBoolean("admin"));
	}

	@Override
	public List<Utilisateur> listerAutresEleves(Long id) {
		List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT id, nom, prenom, email, groupe, admin FROM utilisateur WHERE admin = ? AND id != ? ORDER BY email");
			stmt.setBoolean(1, false);
			stmt.setLong(2, id);
			ResultSet results = stmt.executeQuery();
			while (results.next()) {
				utilisateurs.add(mapperVersUtilisateur(results));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			throw new LearningsSQLException(e);
		}
		return utilisateurs;
	}

	@Override
	public List<Utilisateur> listerEleves() {
		List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT id, nom, prenom, email, groupe, admin FROM utilisateur WHERE admin = ? ORDER BY email");
			stmt.setBoolean(1, false);
			ResultSet results = stmt.executeQuery();
			while (results.next()) {
				utilisateurs.add(mapperVersUtilisateur(results));
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
			PreparedStatement stmt = connection.prepareStatement("SELECT id, nom, prenom, email, groupe, admin FROM utilisateur WHERE id=?");
			stmt.setLong(1, id);
			ResultSet results = stmt.executeQuery();
			if (results.next()) {
				utilisateur = mapperVersUtilisateur(results);
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
			PreparedStatement stmt = connection.prepareStatement("SELECT id, nom, prenom, email, groupe, admin FROM utilisateur WHERE email=?");
			stmt.setString(1, identifiant);
			ResultSet results = stmt.executeQuery();
			if (results.next()) {
				utilisateur = mapperVersUtilisateur(results);
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
	public Utilisateur ajouterUtilisateur(Utilisateur nouvelUtilisateur, String motDePasse) {
		try (Connection connection = getConnection()) {
			try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO utilisateur(nom, prenom, email, groupe, motdepasse, admin) VALUES(?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS)){
				stmt.setString(1, nouvelUtilisateur.getNom());
				stmt.setString(2, nouvelUtilisateur.getPrenom());
				stmt.setString(3, nouvelUtilisateur.getEmail());
				if(nouvelUtilisateur.getGroupe() != null) {
					stmt.setString(4, nouvelUtilisateur.getGroupe().name());
				} else {
					stmt.setNull(4, Types.VARCHAR);
				}
				stmt.setString(5, motDePasse);
				stmt.setBoolean(6, nouvelUtilisateur.isAdmin());
				stmt.executeUpdate();
				try (ResultSet ids = stmt.getGeneratedKeys()) {
					if (ids.next()) {
						nouvelUtilisateur.setId(ids.getLong(1));
					}
				}
			}

		} catch (SQLException e) {
			throw new LearningsSQLException(e);
		}
		return nouvelUtilisateur;
	}
}
