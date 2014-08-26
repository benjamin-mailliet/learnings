package learnings.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import learnings.dao.TPDao;
import learnings.model.TP;

public class TPDaoImpl extends GenericDaoImpl implements TPDao {

	@Override
	public List<TP> listerTPNotesParDateRendu(Date date) {
		List<TP> tpNotes = new ArrayList<TP>();
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM tp WHERE date <= ? AND datelimiterendu >= ?");
			stmt.setDate(1, new java.sql.Date(date.getTime()));
			stmt.setDate(2, new java.sql.Date(date.getTime()));
			ResultSet results = stmt.executeQuery();
			while (results.next()) {
				tpNotes.add(new TP());
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tpNotes;
	}

}
