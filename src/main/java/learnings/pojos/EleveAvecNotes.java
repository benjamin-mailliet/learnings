package learnings.pojos;

import learnings.model.Note;
import learnings.model.Utilisateur;

import java.math.BigDecimal;
import java.util.Map;

public class EleveAvecNotes extends Utilisateur {

    private Map<Long, Note> mapSeanceNote;

    private BigDecimal moyenne;

    public EleveAvecNotes(Utilisateur eleve, Map<Long, Note> mapSeanceNote) {
        super(eleve.getId(),eleve.getNom(), eleve.getPrenom(),eleve.getEmail(),eleve.getGroupe(), false);
        this.mapSeanceNote = mapSeanceNote;
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

    public BigDecimal getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(BigDecimal moyenne) {
        this.moyenne = moyenne;
    }
}
