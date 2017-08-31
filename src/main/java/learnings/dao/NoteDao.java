package learnings.dao;

import learnings.model.Note;

import java.util.List;

public interface NoteDao {

    void ajouterNote(Note note);

    List<Note> listerNotesParUtilisateur(Long idUtilisateur);

    List<Note> listerNotesParBinome(Long idBinome);
}
