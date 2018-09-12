package learnings.dao.impl;

import learnings.dao.BinomeDao;
import learnings.exceptions.LearningsSQLException;
import learnings.model.Binome;
import learnings.model.Seance;
import learnings.model.Utilisateur;
import learnings.utils.JdbcMapperUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BinomeDaoImpl extends GenericDaoImpl implements BinomeDao {
    @Override
    public Binome ajouterBinome(Binome binome) {
        String binomeUid = UUID.randomUUID().toString();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO binome(seance_id, binome_uid, eleve_id) VALUES(?, ?, ?)")
        ) {
            stmt.setLong(1, binome.getSeance().getId());
            stmt.setString(2, binomeUid);

            for (Utilisateur eleve : binome.getEleves()) {
                stmt.setLong(3, eleve.getId());
                stmt.executeUpdate();
            }

            binome.setUid(binomeUid);
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return binome;
    }

    @Override
    public Binome getBinome(Long idSeance, Long idEleve) {
        Binome binome = null;
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM binome b " +
                             "JOIN utilisateur e ON b.eleve_id = e.id " +
                             "WHERE b.binome_uid = (SELECT uid.binome_uid FROM binome uid where uid.eleve_id = ? AND uid.seance_id = ?);")
        ) {
            stmt.setLong(1, idEleve);
            stmt.setLong(2, idSeance);
            try (ResultSet results = stmt.executeQuery()) {
                binome = construireBinome(results);
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }

        return binome;
    }

    @Override
    public Binome getBinome(String uidBinome) {
        Binome binome = null;
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM binome b " +
                             "JOIN utilisateur e ON b.eleve_id = e.id " +
                             "WHERE b.binome_uid = ?")
        ) {
            stmt.setString(1, uidBinome);
            try (ResultSet results = stmt.executeQuery()) {
                binome = construireBinome(results);
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }

        return binome;
    }

    private Binome construireBinome(ResultSet results) throws SQLException {
        Binome binome = null;
        if (results.next()) {
            binome = new Binome(results.getString("b.binome_uid"), new Seance(results.getLong("b.seance_id"), null, null, null));
            binome.getEleves().add(JdbcMapperUtils.mapperVersUtilisateur(results, "e"));
            while (results.next()) {
                binome.getEleves().add(JdbcMapperUtils.mapperVersUtilisateur(results, "e"));
            }
        }
        return binome;
    }
}
