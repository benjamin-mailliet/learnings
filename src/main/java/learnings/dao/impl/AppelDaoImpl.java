package learnings.dao.impl;

import learnings.dao.AppelDao;
import learnings.enums.Groupe;
import learnings.enums.StatutAppel;
import learnings.exceptions.LearningsSQLException;
import learnings.model.Appel;
import learnings.model.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppelDaoImpl extends GenericDaoImpl implements AppelDao {

    @Override
    public List<Appel> listerAppels(Long idSeance) {
        List<Appel> appels = new ArrayList<>();
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT utilisateur.id, utilisateur.nom, utilisateur.prenom, utilisateur.email, utilisateur.groupe, appel.statut FROM utilisateur" +
                            " LEFT OUTER JOIN appel ON appel.ideleve=utilisateur.id AND appel.idseance=?" +
                            " WHERE utilisateur.admin=false ;")) {
                statement.setLong(1, idSeance);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        Utilisateur utilisateur = new Utilisateur(rs.getLong("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("email"),
                                Groupe.valueOf(rs.getString("groupe")), false);
                        StatutAppel statut;
                        try {
                            statut = StatutAppel.valueOf(rs.getString("statut"));
                        } catch (NullPointerException e) {
                            statut = null;
                        }
                        appels.add(new Appel(utilisateur, statut));
                    }
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return appels;
    }

    @Override
    public void ajouterAppel(Long idSeance, Appel appel) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO appel(idseance, ideleve, statut) values(?,?,?)")) {
                statement.setLong(1, idSeance);
                statement.setLong(2, appel.getEleve().getId());
                statement.setString(3, appel.getStatut().toString());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
    }

    @Override
    public void modifierAppel(Long idSeance, Appel appel) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE appel SET statut = ? WHERE idseance = ? AND ideleve = ?")) {
                statement.setString(1, appel.getStatut().toString());
                statement.setLong(2, idSeance);
                statement.setLong(3, appel.getEleve().getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
    }
}
