package learnings.dao.impl;

import learnings.dao.NoteDao;
import learnings.exceptions.LearningsSQLException;
import learnings.model.Enseignement;
import learnings.model.Note;
import learnings.model.Projet;
import learnings.model.Seance;
import learnings.model.Utilisateur;
import learnings.utils.JdbcMapperUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteDaoImpl extends GenericDaoImpl implements NoteDao {
    @Override
    public void ajouterNote(Note note) {

    }

    private Enseignement getEnseignement(ResultSet resultSet) throws  SQLException{
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
}
