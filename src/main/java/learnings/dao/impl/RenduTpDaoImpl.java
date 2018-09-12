package learnings.dao.impl;

import learnings.dao.RenduTpDao;
import learnings.exceptions.LearningsSQLException;
import learnings.model.Binome;
import learnings.model.RenduTp;
import learnings.model.Seance;
import learnings.utils.JdbcMapperUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RenduTpDaoImpl extends GenericDaoImpl implements RenduTpDao{
    @Override
    public RenduTp ajouterRenduTp(RenduTp renduTp) {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO rendu_tp(chemin, dateRendu, commentaire, binome_uid) VALUES(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, renduTp.getChemin());
            stmt.setTimestamp(2, Timestamp.valueOf(renduTp.getDateRendu()));
            stmt.setString(3, renduTp.getCommentaire());
            stmt.setString(4, renduTp.getBinome().getUid());

            stmt.executeUpdate();

            try (ResultSet ids = stmt.getGeneratedKeys()) {
                if (ids.next()) {
                    renduTp.setId(ids.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return renduTp;
    }

    @Override
    public List<RenduTp> listerRendusParBinome(String uidBinome) {
        List<RenduTp> rendus = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection
                     .prepareStatement("SELECT * FROM rendu_tp r WHERE r.binome_uid = ?")
        ) {
            stmt.setString(1, uidBinome);
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    rendus.add(new RenduTp(results.getLong("r.id"), results.getBigDecimal("r.note"), results.getTimestamp("r.dateRendu").toLocalDateTime(),
                            results.getString("r.chemin"), results.getString("r.commentaire"), new Binome(results.getString("r.binome_uid"), null)));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return rendus;
    }

    @Override
    public List<RenduTp> listerRendusParSeance(Long idSeance) {
        List<RenduTp> rendus = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection
                     .prepareStatement("SELECT * FROM rendu_tp r WHERE r.binome_uid IN (SELECT b.binome_uid FROM binome b WHERE b.seance_id = ?)")
        ) {
            stmt.setLong(1, idSeance);
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    Binome binome = new Binome(results.getString("r.binome_uid"),null);
                    rendus.add(new RenduTp(results.getLong("r.id"), results.getBigDecimal("r.note"), results.getTimestamp("r.dateRendu").toLocalDateTime(),
                            results.getString("r.chemin"), results.getString("r.commentaire"), binome));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return rendus;
    }

    @Override
    public List<RenduTp> listerRendusParUtilisateur(Long idUtilisateur) {
        List<RenduTp> rendus = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection
                     .prepareStatement("SELECT * FROM rendu_tp r WHERE r.binome_uid IN (SELECT b.binome_uid FROM binome b WHERE b.eleve_id = ?)")
        ) {
            stmt.setLong(1, idUtilisateur);
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    rendus.add(new RenduTp(results.getLong("r.id"), results.getBigDecimal("r.note"), results.getTimestamp("r.dateRendu").toLocalDateTime(),
                            results.getString("r.chemin"), results.getString("r.commentaire"), new Binome(results.getString("r.binome_uid"), null)));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return rendus;
    }

    @Override
    public RenduTp getRendu(Long idRendu) {
        RenduTp rendu = null;
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM rendu_tp r  WHERE r.id = ?")
        ) {
            stmt.setLong(1, idRendu);
            try (ResultSet results = stmt.executeQuery()) {
                if (results.next()) {
                    rendu = new RenduTp(results.getLong("r.id"), results.getBigDecimal("r.note"), results.getTimestamp("r.dateRendu").toLocalDateTime(),
                            results.getString("r.chemin"), results.getString("r.commentaire"), new Binome(results.getString("r.binome_uid"), null));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return rendu;
    }

    @Override
    public void enregistrerNote(Long idRendu, BigDecimal note, String commentaire) {
        try(Connection connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("UPDATE rendu_tp SET note = ?, commentaireNote = ? WHERE id = ?")) {

            stmt.setBigDecimal(1, note);
            stmt.setString(2, commentaire);
            stmt.setLong(3, idRendu);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
    }
}
