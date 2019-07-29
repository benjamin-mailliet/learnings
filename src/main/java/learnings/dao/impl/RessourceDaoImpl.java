package learnings.dao.impl;

import learnings.dao.RessourceDao;
import learnings.enums.RessourceCategorie;
import learnings.enums.RessourceFormat;
import learnings.exceptions.LearningsSQLException;
import learnings.model.Ressource;
import learnings.model.Seance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RessourceDaoImpl extends GenericDaoImpl implements RessourceDao {

    @Override
    public List<Ressource> getRessources(Seance seance) {
        List<Ressource> listeRessources = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection
                     .prepareStatement("SELECT r.id, r.titre, r.chemin, r.categorie, r.format FROM ressource r WHERE r.seance_id=? ORDER BY titre ASC")
        ) {
            stmt.setLong(1, seance.getId());

            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    RessourceCategorie categorie = null;
                    if (results.getString("categorie") != null) {
                        categorie = RessourceCategorie.valueOf(results.getString("categorie"));
                    }
                    RessourceFormat format = null;
                    if (results.getString("format") != null) {
                        format = RessourceFormat.valueOf(results.getString("format"));
                    }
                    listeRessources.add(new Ressource(results.getLong("id"), results.getString("titre"), results.getString("chemin"), seance, categorie, format));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return listeRessources;
    }

    @Override
    public Ressource ajouterRessource(Ressource ressource) {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("INSERT INTO ressource(titre, chemin, seance_id, categorie, format) VALUES(?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, ressource.getTitre());
            stmt.setString(2, ressource.getChemin());
            stmt.setLong(3, ressource.getSeance().getId());
            stmt.setString(4, ressource.getCategorie().name());
            stmt.setString(5, ressource.getFormat().name());

            stmt.executeUpdate();

            try (ResultSet ids = stmt.getGeneratedKeys()) {
                if (ids.next()) {
                    return new Ressource(ids.getLong(1), ressource.getTitre(), ressource.getChemin(), ressource.getSeance(), ressource.getCategorie(), ressource.getFormat());
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return null;
    }

    @Override
    public Ressource getRessource(Long idRessource) {
        Ressource ressource = null;
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection
                     .prepareStatement("SELECT r.id as idRessource, r.titre as titre, r.chemin as chemin, r.categorie as categorie, r.format as format, s.id as idSeance, s.titre as titreSeance, s.description as descSeance, s.date as dateSeance FROM ressource r LEFT JOIN seance s ON s.id = r.seance_id WHERE r.id = ?")
        ) {
            stmt.setLong(1, idRessource);
            try (ResultSet results = stmt.executeQuery()) {
                if (results.next()) {
                    if (results.getString("idSeance") != null) {
                        Seance seance = new Seance(results.getLong("idSeance"), results.getString("titreSeance"), results.getString("descSeance"),
                                results.getDate("dateSeance"));
                        RessourceCategorie categorie = null;
                        if (results.getString("categorie") != null) {
                            categorie = RessourceCategorie.valueOf(results.getString("categorie"));
                        }
                        RessourceFormat format = null;
                        if (results.getString("format") != null) {
                            format = RessourceFormat.valueOf(results.getString("format"));
                        }
                        ressource = new Ressource(results.getLong("idRessource"), results.getString("titre"), results.getString("chemin"), seance, categorie, format);
                    }

                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return ressource;
    }

    @Override
    public void supprimerRessource(Long idRessource) {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("DELETE FROM ressource WHERE id=?")
        ) {
            stmt.setLong(1, idRessource);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
    }
}
