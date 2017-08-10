package learnings.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import learnings.dao.ProjetDao;
import learnings.exceptions.LearningsSQLException;
import learnings.model.Projet;

public class ProjetDaoImpl extends GenericDaoImpl implements ProjetDao {

    @Override
    public List<Projet> listerProjets() {
        List<Projet> listeProjets = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet results = stmt
                     .executeQuery("SELECT id, titre, description, datelimiterendulot1, datelimiterendulot2 FROM projet ORDER BY datelimiterendulot1, datelimiterendulot2")
        ) {
            while (results.next()) {
                listeProjets.add(new Projet(results.getLong("id"), results.getString("titre"), results.getString("description"), results
                        .getTimestamp("datelimiterendulot1"), results.getTimestamp("datelimiterendulot2")));
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return listeProjets;
    }

    @Override
    public Long getLastProjetId() {
        Long lastId = null;
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet results = stmt.executeQuery("SELECT id FROM projet ORDER BY datelimiterendulot2 DESC LIMIT 1")
        ) {
            if (results.next()) {
                lastId = results.getLong("id");
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return lastId;
    }

    @Override
    public Projet getProjet(Long id) {
        Projet projet = null;
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM projet WHERE id=?")
        ) {
            stmt.setLong(1, id);
            try (ResultSet results = stmt.executeQuery()) {
                if (results.next()) {
                    projet = new Projet(results.getLong("id"), results.getString("titre"), results.getString("description"),
                            results.getTimestamp("datelimiterendulot1"), results.getTimestamp("datelimiterendulot2"));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return projet;
    }

    @Override
    public Projet ajouterProjet(Projet projet) {
        try( Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO projet(titre, description, datelimiterendulot1, datelimiterendulot2) VALUES(?, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, projet.getTitre());
            if (projet.getDescription() == null) {
                stmt.setString(2, "");
            } else {
                stmt.setString(2, projet.getDescription());
            }
            stmt.setTimestamp(3, new java.sql.Timestamp(projet.getDateLimiteRenduLot1().getTime()));
            stmt.setTimestamp(4, new java.sql.Timestamp(projet.getDateLimiteRenduLot2().getTime()));
            stmt.executeUpdate();

            try (ResultSet ids = stmt.getGeneratedKeys()) {
                if (ids.next()) {
                    projet = new Projet(ids.getLong(1), projet.getTitre(), projet.getDescription(), projet.getDateLimiteRenduLot1(),
                            projet.getDateLimiteRenduLot2());
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return projet;
    }

    @Override
    public void modifierProjet(Projet projet) {
        try(Connection connection = getConnection();
            PreparedStatement stmt = connection
                    .prepareStatement("UPDATE projet SET titre=?, description=?, datelimiterendulot1=?, datelimiterendulot2=? WHERE id=?")
        ) {
            stmt.setString(1, projet.getTitre());
            if (projet.getDescription() == null) {
                stmt.setString(2, "");
            } else {
                stmt.setString(2, projet.getDescription());
            }
            stmt.setTimestamp(3, new java.sql.Timestamp(projet.getDateLimiteRenduLot1().getTime()));
            stmt.setTimestamp(4, new java.sql.Timestamp(projet.getDateLimiteRenduLot2().getTime()));
            stmt.setLong(5, projet.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
    }

}
