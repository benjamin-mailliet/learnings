package learnings.dao.impl;

import learnings.dao.NoteDao;
import learnings.exceptions.LearningsSQLException;
import learnings.model.Enseignement;
import learnings.model.Note;
import learnings.model.Projet;
import learnings.model.Seance;
import learnings.model.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class NoteDaoImpl extends GenericDaoImpl implements NoteDao {
    @Override
    public Note ajouterNote(Note note) {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO note(eleve_id, seance_id, projet_id, valeur, commentaire) VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setLong(1, note.getEleve().getId());
            if(note.getEnseignement() instanceof Seance) {
                stmt.setLong(2, note.getEnseignement().getId());
                stmt.setNull(3, Types.VARCHAR);
            }else{
                stmt.setLong(3, note.getEnseignement().getId());
                stmt.setNull(2, Types.VARCHAR);
            }
            stmt.setBigDecimal(4, note.getValeur());
            stmt.setString(5, note.getCommentaire());

            stmt.executeUpdate();

            try (ResultSet ids = stmt.getGeneratedKeys()) {
                if (ids.next()) {
                    note.setId(ids.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return note;
    }

    public Note modifierNote(Note note) {
        String nomCleEnseignement = "seance_id";
        if(note.getEnseignement() instanceof Projet) {
            nomCleEnseignement = "projet_id";
        }
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE note SET valeur=?, commentaire=? WHERE "+nomCleEnseignement+"=? AND eleve_id=?", Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setBigDecimal(1, note.getValeur());
            stmt.setString(2, note.getCommentaire());
            stmt.setLong(3, note.getEnseignement().getId());
            stmt.setLong(4, note.getEleve().getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return note;
    }

    private Enseignement getEnseignement(ResultSet resultSet) throws SQLException {
        Long seanceId = resultSet.getLong("n.seance_id");
        if (!resultSet.wasNull()) {
            return new Seance(seanceId, null, null, null);
        }
        Long projetId = resultSet.getLong("n.projet_id");
        if (!resultSet.wasNull()) {
            return new Projet(projetId, null, null, null, null);
        }
        return null;
    }

    @Override
    public List<Note> listerNotesParUtilisateur(Long idUtilisateur) {
        List<Note> notes = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection
                     .prepareStatement("SELECT * FROM note n WHERE n.eleve_id = ?")
        ) {
            stmt.setLong(1, idUtilisateur);
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    notes.add(new Note(results.getLong("n.id"), new Utilisateur(results.getLong("n.eleve_id"), null, null, null, null, false),
                            this.getEnseignement(results), results.getBigDecimal("n.valeur"), results.getString("n.commentaire")));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return notes;
    }

    @Override
    public List<Note> listerNotesParBinome(Long idBinome) {
        return null;
    }

    @Override
    public List<Note> listerNotesParSeance(Long idSeance) {
        List<Note> notes = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection
                     .prepareStatement("SELECT * FROM note n WHERE n.seance_id = ?")
        ) {
            stmt.setLong(1, idSeance);
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    notes.add(new Note(results.getLong("n.id"), new Utilisateur(results.getLong("n.eleve_id"), null, null, null, null, false),
                            this.getEnseignement(results), results.getBigDecimal("n.valeur"), results.getString("n.commentaire")));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return notes;
    }

    @Override
    public Note getBySeanceAndEleve(Long idSeance, Long idEleve) {
        Note note = null;
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection
                     .prepareStatement("SELECT * FROM note n, utilisateur u WHERE n.seance_id = ? AND n.eleve_id=? AND n.eleve_id=u.id")
        ) {
            stmt.setLong(1, idSeance);
            stmt.setLong(2, idEleve);
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    note = new Note(results.getLong("n.id"), new Utilisateur(results.getLong("n.eleve_id"), results.getString("u.nom"), results.getString("u.prenom"), null, null, false),
                            this.getEnseignement(results), results.getBigDecimal("n.valeur"), results.getString("n.commentaire"));
                }
            }
        } catch (SQLException e) {
            throw new LearningsSQLException(e);
        }
        return note;
    }
}
