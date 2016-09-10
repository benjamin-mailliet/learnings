package learnings.dao.impl;

import learnings.dao.SeanceDao;
import learnings.enums.TypeSeance;
import learnings.exceptions.LearningsSQLException;
import learnings.model.Seance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SeanceDaoImpl extends GenericDaoImpl implements SeanceDao {

    @Override
    public List<Seance> listerSeances() {
        List<Seance> listeCours = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet results = stmt.executeQuery("SELECT id, titre, description, date, isnote, datelimiterendu, type FROM seance ORDER BY date ASC")
        ) {
            while (results.next()) {
                listeCours.add(new Seance(results.getLong("id"), results.getString("titre"), results.getString("description"), results.getDate("date"), results
                        .getBoolean("isnote"), results.getTimestamp("datelimiterendu"), TypeSeance.valueOf(results.getString("type"))));
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return listeCours;
    }

    @Override
    public List<Seance> listerSeancesNotees() {
        List<Seance> listeCours = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet results = stmt
                     .executeQuery("SELECT id, titre, description, date, isnote, datelimiterendu, type FROM seance WHERE isnote is true ORDER BY date ASC")
        ) {
            while (results.next()) {
                listeCours.add(new Seance(results.getLong("id"), results.getString("titre"), results.getString("description"), results.getDate("date"), results
                        .getBoolean("isnote"), results.getTimestamp("datelimiterendu"), TypeSeance.valueOf(results.getString("type"))));
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return listeCours;
    }

    @Override
    public List<Seance> listerTPNotesParDateRendu(Date date) {
        List<Seance> tpNotes = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM seance WHERE type='TP' AND isnote is true AND date <= ? AND datelimiterendu >= ? ORDER BY date ASC")
        ) {
            stmt.setDate(1, new java.sql.Date(date.getTime()));
            stmt.setDate(2, new java.sql.Date(date.getTime()));
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    tpNotes.add(new Seance(results.getLong("id"), results.getString("titre"), results.getString("description"), results.getDate("date"), results
                            .getBoolean("isnote"), results.getTimestamp("datelimiterendu"), TypeSeance.valueOf(results.getString("type"))));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return tpNotes;
    }

    @Override
    public Seance getSeance(Long idSeance) {
        Seance seance = null;
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM seance WHERE id=?")
        ) {
            stmt.setLong(1, idSeance);
            try (ResultSet results = stmt.executeQuery()) {
                if (results.next()) {
                    seance = new Seance(results.getLong("id"), results.getString("titre"), results.getString("description"), results.getDate("date"),
                            results.getBoolean("isnote"), results.getTimestamp("datelimiterendu"), TypeSeance.valueOf(results.getString("type")));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return seance;
    }

    @Override
    public Seance ajouterSeance(Seance seance) {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO seance(titre, description, date, isnote, datelimiterendu, type) VALUES(?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, seance.getTitre());
            if (seance.getDescription() == null) {
                stmt.setString(2, "");
            } else {
                stmt.setString(2, seance.getDescription());
            }
            stmt.setDate(3, new java.sql.Date(seance.getDate().getTime()));
            stmt.setBoolean(4, seance.getIsNote());
            if (seance.getDateLimiteRendu() == null) {
                stmt.setNull(5, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(5, new java.sql.Timestamp(seance.getDateLimiteRendu().getTime()));
            }
            stmt.setString(6, seance.getType().toString());
            stmt.executeUpdate();

            try (ResultSet ids = stmt.getGeneratedKeys()) {
                if (ids.next()) {
                    seance = new Seance(ids.getLong(1), seance.getTitre(), seance.getDescription(), seance.getDate(), seance.getIsNote(),
                            seance.getDateLimiteRendu(), seance.getType());
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return seance;
    }

    @Override
    public void modifierSeance(Seance seance) {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection
                     .prepareStatement("UPDATE seance SET titre=?, description=?, date=?, isnote=?, datelimiterendu=?, type=? WHERE id=?")
        ) {
            stmt.setString(1, seance.getTitre());
            if (seance.getDescription() == null) {
                stmt.setString(2, "");
            } else {
                stmt.setString(2, seance.getDescription());
            }
            stmt.setDate(3, new java.sql.Date(seance.getDate().getTime()));
            stmt.setBoolean(4, seance.getIsNote());
            if (seance.getDateLimiteRendu() == null) {
                stmt.setNull(5, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(5, new java.sql.Timestamp(seance.getDateLimiteRendu().getTime()));
            }
            stmt.setString(6, seance.getType().toString());
            stmt.setLong(7, seance.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
    }
}
