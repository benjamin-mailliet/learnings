package learnings.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import learnings.dao.TPDao;
import learnings.model.TP;

public class TPDaoImpl extends GenericDaoImpl implements TPDao {

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

	@Override
	public List<TP> listerTPNotesParDateRendu(Date date) {
		List<TP> tpNotes = new ArrayList<TP>();
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("SELECT id, titre, description, isnote, datelimiterendu, date FROM tp WHERE date <= ? AND datelimiterendu >= ? ORDER BY date ASC");
			stmt.setDate(1, new java.sql.Date(date.getTime()));
			stmt.setDate(2, new java.sql.Date(date.getTime()));
			ResultSet results = stmt.executeQuery();
			while (results.next()) {
				tpNotes.add(new TP(results.getLong("id"), results.getString("titre"), results.getString("description"), results.getBoolean("isnote"), results
						.getDate("datelimiterendu"), results.getDate("date")));
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tpNotes;
	}
}
