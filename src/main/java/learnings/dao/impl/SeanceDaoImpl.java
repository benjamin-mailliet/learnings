package learnings.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import learnings.dao.SeanceDao;
import learnings.enums.TypeSeance;
import learnings.model.Seance;

public class SeanceDaoImpl extends GenericDaoImpl implements SeanceDao {

	@Override
	public List<Seance> listerSeances() {
		List<Seance> listeCours = new ArrayList<Seance>();
		try {
			Connection connection = getConnection();
			Statement stmt = connection.createStatement();
			ResultSet results = stmt.executeQuery("SELECT id, titre, description, date, isnote, datelimiterendu, type FROM seance ORDER BY date DESC");
			Date dateLimiteRendu = null;
			while (results.next()) {
				listeCours.add(new Seance(results.getLong("id"), results.getString("titre"), results.getString("description"), results.getDate("date"), results
						.getBoolean("isnote"), dateLimiteRendu, TypeSeance.valueOf(results.getString("type"))));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listeCours;
	}

	@Override
	public List<Seance> listerSeancesNotees() {
		List<Seance> listeCours = new ArrayList<Seance>();
		try {
			Connection connection = getConnection();
			Statement stmt = connection.createStatement();
			ResultSet results = stmt
					.executeQuery("SELECT id, titre, description, date, isnote, datelimiterendu, type FROM seance WHERE isnote is true ORDER BY date ASC");
			Date dateLimiteRendu = null;
			while (results.next()) {
				listeCours.add(new Seance(results.getLong("id"), results.getString("titre"), results.getString("description"), results.getDate("date"), results
						.getBoolean("isnote"), dateLimiteRendu, TypeSeance.valueOf(results.getString("type"))));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listeCours;
	}

	@Override
	public List<Seance> listerSeancesWhereDateBefore(Date date) {
		List<Seance> listeCours = new ArrayList<Seance>();
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("SELECT id, titre, description, date, isnote, datelimiterendu, type FROM seance WHERE date <=? ORDER BY date DESC, type ASC");
			stmt.setDate(1, new java.sql.Date(date.getTime()));
			ResultSet results = stmt.executeQuery();
			while (results.next()) {
				listeCours.add(new Seance(results.getLong("id"), results.getString("titre"), results.getString("description"), results.getDate("date"), results
						.getBoolean("isnote"), results.getTimestamp("datelimiterendu"), TypeSeance.valueOf(results.getString("type"))));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listeCours;
	}

	@Override
	public List<Seance> listerTPNotesParDateRendu(Date date) {
		List<Seance> tpNotes = new ArrayList<Seance>();
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("SELECT * FROM seance WHERE type='TP' AND isnote is true AND date <= ? AND datelimiterendu >= ? ORDER BY date ASC");
			stmt.setDate(1, new java.sql.Date(date.getTime()));
			stmt.setDate(2, new java.sql.Date(date.getTime()));
			ResultSet results = stmt.executeQuery();
			while (results.next()) {
				tpNotes.add(new Seance(results.getLong("id"), results.getString("titre"), results.getString("description"), results.getDate("date"), results
						.getBoolean("isnote"), results.getTimestamp("datelimiterendu"), TypeSeance.valueOf(results.getString("type"))));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tpNotes;
	}

	@Override
	public Seance getSeance(Long idSeance) {
		Seance seance = null;
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM seance WHERE id=?");
			stmt.setLong(1, idSeance);
			ResultSet results = stmt.executeQuery();
			if (results.next()) {
				seance = new Seance(results.getLong("id"), results.getString("titre"), results.getString("description"), results.getDate("date"),
						results.getBoolean("isnote"), results.getTimestamp("datelimiterendu"), TypeSeance.valueOf(results.getString("type")));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return seance;
	}
}
