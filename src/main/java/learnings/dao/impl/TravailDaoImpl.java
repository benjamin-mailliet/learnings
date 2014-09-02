package learnings.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import learnings.dao.TravailDao;
import learnings.exceptions.LearningsSQLException;
import learnings.model.Seance;
import learnings.model.Travail;
import learnings.model.Utilisateur;

public class TravailDaoImpl extends GenericDaoImpl implements TravailDao {

	@Override
	public Travail ajouterTravail(Travail travail) {

		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement(
					"INSERT INTO travail(note, dateRendu, seance_id, projettransversal_id, chemin) VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			if (travail.getNote() == null) {
				stmt.setNull(1, Types.DECIMAL);
			} else {
				stmt.setBigDecimal(1, travail.getNote());
			}
			stmt.setTimestamp(2, new java.sql.Timestamp(travail.getDateRendu().getTime()));
			if (travail.getEnseignement() instanceof Seance) {
				stmt.setLong(3, travail.getEnseignement().getId());
				stmt.setNull(4, Types.INTEGER);
			} else {
				stmt.setNull(3, Types.INTEGER);
				stmt.setLong(4, travail.getEnseignement().getId());
			}
			stmt.setString(5, travail.getChemin());
			stmt.executeUpdate();

			ResultSet ids = stmt.getGeneratedKeys();
			if (ids.next()) {
				travail.setId(ids.getLong(1));
			}

			stmt.close();
			connection.close();
		} catch (SQLException e) {
			throw new LearningsSQLException(e);
		}
		return travail;
	}

	@Override
	public void mettreAJourTravail(Long idTravail, Date dateRendu, String chemin) {
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("UPDATE travail SET dateRendu = ?, chemin = ? WHERE id = ?");
			stmt.setTimestamp(1, new java.sql.Timestamp(dateRendu.getTime()));
			stmt.setString(2, chemin);
			stmt.setLong(3, idTravail);
			stmt.executeUpdate();
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			throw new LearningsSQLException(e);
		}
	}

	@Override
	public void ajouterUtilisateur(Long idTravail, Long idUtilisateur) {
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO travailutilisateur(idtravail, idutilisateur) VALUES(?, ?)");
			stmt.setLong(1, idTravail);
			stmt.setLong(2, idUtilisateur);
			stmt.executeUpdate();
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			throw new LearningsSQLException(e);
		}
	}

	@Override
	public Travail getTravailUtilisateurParSeance(Long idSeance, Long idUtilisateur) {
		Travail travail = null;
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("SELECT t.* FROM travail t JOIN travailutilisateur tu ON t.id = tu.idtravail WHERE t.seance_id = ? AND tu.idutilisateur = ?");
			stmt.setLong(1, idSeance);
			stmt.setLong(2, idUtilisateur);
			ResultSet results = stmt.executeQuery();
			if (results.next()) {
				travail = new Travail(results.getLong("id"), new Seance(idSeance, null, null, null), results.getBigDecimal("note"),
						results.getTimestamp("dateRendu"), results.getString("chemin"));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			throw new LearningsSQLException(e);
		}
		return travail;
	}

	@Override
	public List<Travail> listerTravauxParSeance(Long idSeance) {
		List<Travail> listeTravaux = new ArrayList<Travail>();
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM travail t  WHERE t.seance_id = ?");
			stmt.setLong(1, idSeance);
			ResultSet results = stmt.executeQuery();
			while (results.next()) {
				listeTravaux.add(new Travail(results.getLong("id"), new Seance(idSeance, null, null, null), results.getBigDecimal("note"), results
						.getTimestamp("dateRendu"), results.getString("chemin")));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			throw new LearningsSQLException(e);
		}
		return listeTravaux;
	}

	@Override
	public List<Travail> listerTravauxParUtilisateur(Long idUtilisateur) {
		List<Travail> listeTravaux = new ArrayList<Travail>();
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("SELECT t.* FROM travail t JOIN travailutilisateur tu ON t.id = tu.idtravail WHERE tu.idutilisateur = ?");
			stmt.setLong(1, idUtilisateur);
			ResultSet results = stmt.executeQuery();
			while (results.next()) {
				listeTravaux.add(new Travail(results.getLong("id"), new Seance(results.getLong("seance_id"), null, null, null), results.getBigDecimal("note"),
						results.getTimestamp("dateRendu"), results.getString("chemin")));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			throw new LearningsSQLException(e);
		}
		return listeTravaux;
	}

	@Override
	public List<Utilisateur> listerUtilisateurs(Long idTravail) {
		List<Utilisateur> listeUtilisateurs = new ArrayList<Utilisateur>();
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("SELECT * FROM utilisateur u JOIN travailutilisateur tu ON u.id = tu.idutilisateur  WHERE tu.idtravail = ?");
			stmt.setLong(1, idTravail);
			ResultSet results = stmt.executeQuery();
			while (results.next()) {
				listeUtilisateurs.add(new Utilisateur(results.getLong("id"), results.getString("email"), results.getBoolean("admin")));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			throw new LearningsSQLException(e);
		}
		return listeUtilisateurs;
	}

	@Override
	public Travail getTravail(Long idTravail) {
		Travail travail = null;
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM travail t  WHERE t.id = ?");
			stmt.setLong(1, idTravail);
			ResultSet results = stmt.executeQuery();
			if (results.next()) {
				travail = new Travail(results.getLong("id"), new Seance(results.getLong("seance_id"), null, null, null), results.getBigDecimal("note"),
						results.getTimestamp("dateRendu"), results.getString("chemin"));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			throw new LearningsSQLException(e);
		}
		return travail;
	}
}
