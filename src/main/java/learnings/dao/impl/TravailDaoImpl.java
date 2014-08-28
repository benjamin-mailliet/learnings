package learnings.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import learnings.dao.TravailDao;
import learnings.model.Seance;
import learnings.model.Travail;

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
			stmt.setDate(2, new java.sql.Date(travail.getDateRendu().getTime()));
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
			e.printStackTrace();
		}
		return travail;
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
			e.printStackTrace();
		}
	}

}
