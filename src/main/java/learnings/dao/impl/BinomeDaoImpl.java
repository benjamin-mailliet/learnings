package learnings.dao.impl;

import learnings.dao.BinomeDao;
import learnings.enums.Groupe;
import learnings.exceptions.LearningsSQLException;
import learnings.model.Binome;
import learnings.model.Seance;
import learnings.model.Utilisateur;
import learnings.utils.JdbcMapperUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class BinomeDaoImpl extends GenericDaoImpl implements BinomeDao {
    @Override
    public Binome ajouterBinome(Binome binome) {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO binome(eleve1_id, eleve2_id, seance_id) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setLong(1, binome.getEleve1().getId());
            if(binome.getEleve2() != null) {
                stmt.setLong(2, binome.getEleve2().getId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setLong(3, binome.getSeance().getId());

            stmt.executeUpdate();

            try (ResultSet ids = stmt.getGeneratedKeys()) {
                if (ids.next()) {
                    binome.setId(ids.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return binome;
    }

    @Override
    public Binome getBinome(Long idSeance, Long idEleve) {
        Binome binome = null;
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM binome b JOIN utilisateur e1 ON b.eleve1_id = e1.id LEFT JOIN utilisateur e2 ON b.eleve2_id = e2.id WHERE b.seance_id = ? AND (b.eleve1_id = ? OR b.eleve2_id = ?)")
        ) {
            stmt.setLong(1, idSeance);
            stmt.setLong(2, idEleve);
            stmt.setLong(3, idEleve);
            try (ResultSet results = stmt.executeQuery()) {
                if(results.next()) {
                    binome = new Binome(results.getLong("b.id"), new Seance(results.getLong("b.seance_id"), null, null, null),
                            JdbcMapperUtils.mapperVersUtilisateur(results, "e1"), JdbcMapperUtils.mapperVersUtilisateur(results, "e2"));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }

        return binome;
    }

    @Override
    public Binome getBinome(Long idBinome) {
        Binome binome = null;
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM binome b JOIN utilisateur e1 ON b.eleve1_id = e1.id JOIN utilisateur e2 ON b.eleve2_id = e2.id WHERE b.id = ?)")
        ) {
            stmt.setLong(1, idBinome);
            try (ResultSet results = stmt.executeQuery()) {
                binome = new Binome(results.getLong("b.id"), new Seance(results.getLong("b.seance_id"), null, null, null),
                        JdbcMapperUtils.mapperVersUtilisateur(results, "e1"),JdbcMapperUtils.mapperVersUtilisateur(results, "e2"));
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }

        return binome;
    }
}
