package learnings.dao.impl;

import learnings.dao.TravailDao;
import learnings.enums.Groupe;
import learnings.exceptions.LearningsSQLException;
import learnings.model.Projet;
import learnings.model.Seance;
import learnings.model.Travail;
import learnings.model.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TravailDaoImpl extends GenericDaoImpl implements TravailDao {

    @Override
    public Travail ajouterTravail(Travail travail) {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO travail(note, dateRendu, seance_id, projettransversal_id, chemin, urlRepository, commentaire) VALUES(?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            if (travail.getNote() == null) {
                stmt.setNull(1, Types.DECIMAL);
            } else {
                stmt.setBigDecimal(1, travail.getNote());
            }
            stmt.setTimestamp(2, new java.sql.Timestamp(travail.getDateRendu().getTime()));
            if (travail.getEnseignement() instanceof Seance) {
                stmt.setLong(3, travail.getEnseignement().getId());
                stmt.setNull(4, Types.INTEGER);
            } else {
                stmt.setNull(3, Types.INTEGER);
                stmt.setLong(4, travail.getEnseignement().getId());
            }
            stmt.setString(5, travail.getChemin());
            stmt.setString(6, travail.getUrlRepository());
            stmt.setString(7, travail.getCommentaire());

            stmt.executeUpdate();

            try (ResultSet ids = stmt.getGeneratedKeys()) {
                if (ids.next()) {
                    travail.setId(ids.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return travail;
    }

    @Override
    public void mettreAJourTravail(Long idTravail, Date dateRendu, String chemin, String urlRepository, String commentaire) {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("UPDATE travail SET dateRendu = ?, chemin = ?, urlRepository = ?, commentaire = ? WHERE id = ?")
        ) {
            stmt.setTimestamp(1, new java.sql.Timestamp(dateRendu.getTime()));
            stmt.setString(2, chemin);
            stmt.setString(3, urlRepository);
            stmt.setString(4, commentaire);
            stmt.setLong(5, idTravail);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
    }

    @Override
    public void ajouterUtilisateur(Long idTravail, Long idUtilisateur) {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("INSERT INTO travailutilisateur(idtravail, idutilisateur) VALUES(?, ?)")
        ) {
            stmt.setLong(1, idTravail);
            stmt.setLong(2, idUtilisateur);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
    }

    @Override
    public Travail getTravailUtilisateurParSeance(Long idSeance, Long idUtilisateur) {
        Travail travail = null;
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection
                     .prepareStatement("SELECT t.* FROM travail t JOIN travailutilisateur tu ON t.id = tu.idtravail WHERE t.seance_id = ? AND tu.idutilisateur = ?")
        ) {
            stmt.setLong(1, idSeance);
            stmt.setLong(2, idUtilisateur);
            try (ResultSet results = stmt.executeQuery()) {
                if (results.next()) {
                    travail = new Travail(results.getLong("id"), new Seance(idSeance, null, null, null), results.getBigDecimal("note"),
                            results.getTimestamp("dateRendu"), results.getString("chemin"), results.getString("commentaire"), results.getString("urlRepository"));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return travail;
    }

    @Override
    public List<Travail> listerTravauxParSeance(Long idSeance) {
        List<Travail> listeTravaux = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM travail t  WHERE t.seance_id = ?")
        ) {
            stmt.setLong(1, idSeance);
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    listeTravaux.add(new Travail(results.getLong("id"), new Seance(idSeance, null, null, null), results.getBigDecimal("note"), results
                            .getTimestamp("dateRendu"), results.getString("chemin"), results.getString("commentaire"), results.getString("urlRepository")));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return listeTravaux;
    }

    @Override
    public List<Travail> listerTravauxParUtilisateur(Long idUtilisateur) {
        List<Travail> listeTravaux = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection
                     .prepareStatement("SELECT t.* FROM travail t JOIN travailutilisateur tu ON t.id = tu.idtravail WHERE tu.idutilisateur = ?")
        ) {
            stmt.setLong(1, idUtilisateur);
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    listeTravaux.add(new Travail(results.getLong("id"), new Seance(results.getLong("seance_id"), null, null, null), results.getBigDecimal("note"),
                            results.getTimestamp("dateRendu"), results.getString("chemin"), results.getString("commentaire"), results.getString("urlRepository")));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return listeTravaux;
    }

    @Override
    public List<Utilisateur> listerUtilisateurs(Long idTravail) {
        List<Utilisateur> listeUtilisateurs = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection
                     .prepareStatement("SELECT * FROM utilisateur u JOIN travailutilisateur tu ON u.id = tu.idutilisateur  WHERE tu.idtravail = ?")
        ) {
            stmt.setLong(1, idTravail);
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    listeUtilisateurs.add(new Utilisateur(results.getLong("id"), results.getString("nom"), results.getString("prenom"), results.getString("email"),
                            Groupe.valueOf(results.getString("groupe")), results.getBoolean("admin")));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return listeUtilisateurs;
    }

    @Override
    public Travail getTravail(Long idTravail) {
        Travail travail = null;
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM travail t  WHERE t.id = ?")
        ) {
            stmt.setLong(1, idTravail);
            try (ResultSet results = stmt.executeQuery()) {
                if (results.next()) {
                    travail = new Travail(results.getLong("id"), new Seance(results.getLong("seance_id"), null, null, null), results.getBigDecimal("note"),
                            results.getTimestamp("dateRendu"), results.getString("chemin"), results.getString("commentaire"), results.getString("urlRepository"));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return travail;
    }

    @Override
    public Travail getTravailUtilisateurParProjet(Long idProjet, Long idUtilisateur) {
        Travail travail = null;
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT t.* FROM travail t JOIN travailutilisateur tu ON t.id = tu.idtravail WHERE t.projettransversal_id = ? AND tu.idutilisateur = ?")
        ) {
            stmt.setLong(1, idProjet);
            stmt.setLong(2, idUtilisateur);
            try (ResultSet results = stmt.executeQuery()) {
                if (results.next()) {
                    travail = new Travail(results.getLong("id"), new Projet(idProjet, null, null, null, null), results.getBigDecimal("note"),
                            results.getTimestamp("dateRendu"), results.getString("chemin"), results.getString("commentaire"), results.getString("urlRepository"));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return travail;
    }

    @Override
    public List<Travail> listerTravauxParProjet(Long idProjet) {
        List<Travail> listeTravaux = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM travail t  WHERE t.projettransversal_id = ?")
        ) {
            stmt.setLong(1, idProjet);
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    listeTravaux.add(new Travail(results.getLong("id"), new Projet(idProjet, null, null, null, null), results.getBigDecimal("note"), results
                            .getTimestamp("dateRendu"), results.getString("chemin"), results.getString("commentaire"), results.getString("urlRepository")));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return listeTravaux;
    }
}
