package learnings.pojos;

import learnings.model.Note;
import learnings.model.RenduProjet;
import learnings.model.RenduTp;
import learnings.model.Travail;
import learnings.model.Utilisateur;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class EleveAvecNotes extends Utilisateur {

    private Map<Long, Note> mapSeanceNote;

    private Note noteProjet;

    private BigDecimal moyenne;

    public EleveAvecNotes(Utilisateur eleve, Map<Long, Note> mapSeanceNote, Note noteProjet) {
        super(eleve.getId(),eleve.getNom(), eleve.getPrenom(),eleve.getEmail(),eleve.getGroupe(), false);
        this.mapSeanceNote = mapSeanceNote;
        this.noteProjet = noteProjet;
    }

    public EleveAvecNotes() {
        super();
    }

    public EleveAvecNotes(Utilisateur eleve) {
        super(eleve.getId(),eleve.getNom(), eleve.getPrenom(),eleve.getEmail(),eleve.getGroupe(), false);
    }

    public Map<Long, Note> getMapSeanceNote() {
        return mapSeanceNote;
    }

    public void setMapSeanceNote(Map<Long, Note> mapSeanceNote) {
        this.mapSeanceNote = mapSeanceNote;
    }

    public Note getNoteProjet() {
        return noteProjet;
    }

    public void setNoteProjet(Note noteProjet) {
        this.noteProjet = noteProjet;
    }

    public BigDecimal getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(BigDecimal moyenne) {
        this.moyenne = moyenne;
    }
}
