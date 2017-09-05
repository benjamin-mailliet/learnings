package learnings.dao;

import learnings.model.Note;

import java.util.List;

public interface NoteDao {

    Note ajouterNote(Note note);

    Note modifierNote(Note note);

    List<Note> listerNotesParUtilisateur(Long idUtilisateur);

    List<Note> listerNotesParBinome(Long idBinome);

    List<Note> listerNotesParSeance(Long idSeance);

    Note getBySeanceAndEleve(Long idSeance, Long idEleve);
}
