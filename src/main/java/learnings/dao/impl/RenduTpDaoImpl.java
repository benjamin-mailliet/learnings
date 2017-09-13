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
                     "INSERT INTO rendu_tp(chemin, dateRendu, commentaire, binome_id) VALUES(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, renduTp.getChemin());
            stmt.setTimestamp(2, Timestamp.valueOf(renduTp.getDateRendu()));
            stmt.setString(3, renduTp.getCommentaire());
            stmt.setLong(4, renduTp.getBinome().getId());

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
    public List<RenduTp> listerRendusParBinome(Long idBinome) {
        List<RenduTp> rendus = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection
                     .prepareStatement("SELECT * FROM rendu_tp r JOIN binome b  ON b.id = r.binome_id WHERE b.id = ?")
        ) {
            stmt.setLong(1, idBinome);
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    rendus.add(new RenduTp(results.getLong("r.id"), results.getBigDecimal("r.note"), results.getTimestamp("r.dateRendu").toLocalDateTime(),
                            results.getString("r.chemin"), results.getString("r.commentaire"), new Binome(results.getLong("b.id"),
                            new Seance(results.getLong("b.seance_id"), null, null, null))));
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
                     .prepareStatement("SELECT * FROM rendu_tp r JOIN binome b  ON b.id = r.binome_id " +
                             "JOIN utilisateur e1 ON b.eleve1_id = e1.id LEFT JOIN utilisateur e2 ON b.eleve2_id = e2.id " +
                             "WHERE b.seance_id = ?")
        ) {
            stmt.setLong(1, idSeance);
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    Binome binome = new Binome(results.getLong("b.id"),
                            new Seance(results.getLong("b.seance_id"), null, null, null),
                            JdbcMapperUtils.mapperVersUtilisateur(results, "e1"),
                            JdbcMapperUtils.mapperVersUtilisateur(results, "e2"));
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
                     .prepareStatement("SELECT * FROM rendu_tp r JOIN binome b  ON b.id = r.binome_id WHERE b.eleve1_id = ? OR b.eleve2_id = ?")
        ) {
            stmt.setLong(1, idUtilisateur);
            stmt.setLong(2, idUtilisateur);
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    rendus.add(new RenduTp(results.getLong("r.id"), results.getBigDecimal("r.note"), results.getTimestamp("r.dateRendu").toLocalDateTime(),
                            results.getString("r.chemin"), results.getString("r.commentaire"), new Binome(results.getLong("b.id"),
                            new Seance(results.getLong("b.seance_id"), null, null, null))));
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
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM rendu_tp r JOIN binome b  ON b.id = r.binome_id " +
                     "JOIN utilisateur e1 ON b.eleve1_id = e1.id LEFT JOIN utilisateur e2 ON b.eleve2_id = e2.id " +
                     " WHERE r.id = ?")
        ) {
            stmt.setLong(1, idRendu);
            try (ResultSet results = stmt.executeQuery()) {
                if (results.next()) {
                    rendu = new RenduTp(results.getLong("r.id"), results.getBigDecimal("r.note"), results.getTimestamp("r.dateRendu").toLocalDateTime(),
                            results.getString("r.chemin"), results.getString("r.commentaire"), new Binome(results.getLong("b.id"),
                            new Seance(results.getLong("b.seance_id"), null, null, null),JdbcMapperUtils.mapperVersUtilisateur(results, "e1"),
                            JdbcMapperUtils.mapperVersUtilisateur(results, "e2")));
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
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
    }
}
