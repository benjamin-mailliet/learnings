package learnings.dao.impl;

import learnings.dao.AppelDao;
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
                    "SELECT utilisateur.id, utilisateur.email, appel.statut FROM utilisateur" +
                            " LEFT OUTER JOIN appel ON appel.ideleve=utilisateur.id AND appel.idseance=?" +
                            " WHERE utilisateur.admin=false ;")) {
                statement.setLong(1, idSeance);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        Utilisateur utilisateur = new Utilisateur(rs.getLong("id"), rs.getString("email"), false);
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
}
