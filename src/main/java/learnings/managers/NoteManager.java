package learnings.managers;

import learnings.dao.NoteDao;
import learnings.dao.impl.NoteDaoImpl;
import learnings.model.Enseignement;
import learnings.model.Note;
import learnings.model.Projet;
import learnings.model.Seance;

import java.util.ArrayList;
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

    public Note getNoteByEnseignementAndEleve(Long idEnseignement, Long idEleve, Class<? extends Enseignement> enseignementClass) {
        if (idEnseignement == null) {
            throw new IllegalArgumentException("L'identifiant de la séance est incorrect.");
        }
        if (idEleve == null) {
            throw new IllegalArgumentException("L'identifiant de l'élève est incorrect.");
        }
        if(enseignementClass.getName().equals(Seance.class.getName())){
            return noteDao.getBySeanceAndEleve(idEnseignement, idEleve);
        }else if(enseignementClass.getName().equals(Projet.class.getName())){
            return noteDao.getByProjetAndEleve(idEnseignement, idEleve);
        }
        return null;
    }


    public Map<Long, Note> getMapNoteEleve(Long idEnseignement, Class<? extends Enseignement> classEnseignement) {
        List<Note> notes = new ArrayList<>();
        if (classEnseignement.getName().equals(Seance.class.getName())) {
            notes = noteDao.listerNotesParSeance(idEnseignement);
        } else if (classEnseignement.getName().equals(Projet.class.getName())) {
            notes = noteDao.listerNotesParProjet(idEnseignement);
        }
        Map<Long, Note> mapNoteEleve = notes.stream().collect(Collectors.toMap(n -> n.getEleve().getId(),
                Function.identity()));
        return mapNoteEleve;
    }
}
