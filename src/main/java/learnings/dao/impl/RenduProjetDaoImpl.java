package learnings.dao.impl;

import learnings.dao.RenduProjetDao;
import learnings.exceptions.LearningsSQLException;
import learnings.model.Projet;
import learnings.model.RenduProjet;
import learnings.model.Utilisateur;
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

public class RenduProjetDaoImpl extends GenericDaoImpl implements RenduProjetDao {
    @Override
    public RenduProjet ajouterRendu(RenduProjet renduProjet) {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO rendu_projet(chemin, urlRepository, dateRendu, commentaire, projet_id, eleve_id) VALUES(?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, renduProjet.getChemin());
            stmt.setString(2, renduProjet.getUrlRepository());
            stmt.setTimestamp(3, Timestamp.valueOf(renduProjet.getDateRendu()));
            stmt.setString(4, renduProjet.getCommentaire());
            stmt.setLong(5, renduProjet.getProjet().getId());
            stmt.setLong(6, renduProjet.getEleve().getId());

            stmt.executeUpdate();

            try (ResultSet ids = stmt.getGeneratedKeys()) {
                if (ids.next()) {
                    renduProjet.setId(ids.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return renduProjet;
    }

    @Override
    public List<RenduProjet> listerRendusUtilisateurParProjet(Long idProjet, Long idUtilisateur) {
        List<RenduProjet> rendus = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection
                     .prepareStatement("SELECT * FROM rendu_projet r WHERE r.projet_id = ? AND r.eleve_id = ?")
        ) {
            stmt.setLong(1, idProjet);
            stmt.setLong(2, idUtilisateur);
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    rendus.add(new RenduProjet(results.getLong("r.id"), results.getBigDecimal("r.note"), results.getTimestamp("r.dateRendu").toLocalDateTime(),
                            results.getString("r.chemin"), results.getString("r.commentaire"), results.getString("r.urlRepository"),
                            new Utilisateur(results.getLong("r.eleve_id"), null, null, null, null, false),
                            new Projet(results.getLong("r.projet_id"), null, null, null, null)));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return rendus;
    }

    @Override
    public List<RenduProjet> listerRendusParUtilisateur(Long idUtilisateur) {
        List<RenduProjet> rendus = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection
                     .prepareStatement("SELECT * FROM rendu_projet r WHERE r.eleve_id = ?")
        ) {
            stmt.setLong(1, idUtilisateur);
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    rendus.add(new RenduProjet(results.getLong("r.id"), results.getBigDecimal("r.note"), results.getTimestamp("r.dateRendu").toLocalDateTime(),
                            results.getString("r.chemin"), results.getString("r.commentaire"), results.getString("r.urlRepository"),
                            new Utilisateur(results.getLong("r.eleve_id"), null, null, null, null, false),
                            new Projet(results.getLong("r.projet_id"), null, null, null, null)));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return rendus;
    }

    @Override
    public RenduProjet getRenduProjet(Long idRendu) {
        RenduProjet rendu = null;
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM rendu_projet r JOIN binome b  ON b.id = r.binome_id  WHERE r.id = ?")
        ) {
            stmt.setLong(1, idRendu);
            try (ResultSet results = stmt.executeQuery()) {
                if (results.next()) {
                    rendu = new RenduProjet(results.getLong("r.id"), results.getBigDecimal("r.note"), results.getTimestamp("r.dateRendu").toLocalDateTime(),
                            results.getString("r.chemin"), results.getString("r.commentaire"), results.getString("r.urlRepository"),
                            new Utilisateur(results.getLong("r.eleve_id"), null, null, null, null, false),
                            new Projet(results.getLong("r.projet_id"), null, null, null, null));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return rendu;
    }

    @Override
    public List<RenduProjet> listerRendusParProjet(Long idProjet) {
        List<RenduProjet> rendus = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection
                     .prepareStatement("SELECT * FROM rendu_projet r JOIN utilisateur u ON r.eleve_id = u.id WHERE r.projet_id = ?")
        ) {
            stmt.setLong(1, idProjet);
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    rendus.add(new RenduProjet(results.getLong("r.id"), results.getBigDecimal("r.note"), results.getTimestamp("r.dateRendu").toLocalDateTime(),
                            results.getString("r.chemin"), results.getString("r.commentaire"), results.getString("r.urlRepository"),
                            JdbcMapperUtils.mapperVersUtilisateur(results, "u"),
                            new Projet(results.getLong("r.projet_id"), null, null, null, null)));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return rendus;
    }

    @Override
    public void enregistrerNote(Long idRendu, BigDecimal note, String commentaire) {
        try(Connection connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("UPDATE rendu_projet SET note = ?, commentaireNote = ? WHERE id = ?")) {

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
