package learnings.dao.impl;

import learnings.dao.NoteDao;
import learnings.model.Note;

import java.util.List;

public class NoteDaoImpl extends GenericDaoImpl implements NoteDao {
    @Override
    public void ajouterNote(Note note) {

    }

    @Override
    public List<Note> listerNotesParUtilisateur(Long idUtilisateur) {
        return null;
    }

    @Override
    public List<Note> listerNotesParBinome(Long idBinome) {
        return null;
    }
}
