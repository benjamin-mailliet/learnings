package learnings.managers;

import learnings.dao.NoteDao;
import learnings.dao.impl.NoteDaoImpl;
import learnings.model.Note;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NoteManager {


    private static class NoteManagerHolder {
        private static NoteManager instance = new NoteManager();
    }

    public static NoteManager getInstance() {
        return NoteManagerHolder.instance;
    }

    public NoteManager() {
    }

    private NoteDao noteDao = new NoteDaoImpl();

    public Note getNoteByEnseignementAndEleve(Long idEnseignement, Long idEleve) {
        if (idEnseignement == null) {
            throw new IllegalArgumentException("L'identifiant de la séance est incorrect.");
        }
        if (idEleve == null) {
            throw new IllegalArgumentException("L'identifiant de l'élève est incorrect.");
        }
        return noteDao.getBySeanceAndEleve(idEnseignement, idEleve);
    }


    public Map<Long, Note> getMapNoteEleve(Long idEnseignement) {
        List<Note> notes = noteDao.listerNotesParSeance(idEnseignement);
        return notes.stream().collect(Collectors.toMap(n -> n.getEleve().getId(),
                Function.identity()));
    }
}
