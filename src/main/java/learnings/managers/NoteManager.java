package learnings.managers;

import learnings.dao.NoteDao;
import learnings.dao.impl.NoteDaoImpl;
import learnings.model.Note;

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

    public Note getNoteBySeanceAndEleve(Long idSeance, Long idEleve) {
        if (idSeance == null) {
            throw new IllegalArgumentException("L'identifiant de la séance est incorrect.");
        }
        if (idEleve == null) {
            throw new IllegalArgumentException("L'identifiant de l'élève est incorrect.");
        }

        return noteDao.getBySeanceAndEleve(idSeance, idEleve);
    }
}
