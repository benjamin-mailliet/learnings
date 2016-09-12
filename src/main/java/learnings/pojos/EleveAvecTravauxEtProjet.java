package learnings.pojos;

import learnings.model.Travail;
import learnings.model.Utilisateur;

import java.math.BigDecimal;
import java.util.Map;

public class EleveAvecTravauxEtProjet extends Utilisateur {

    private Map<Long, Travail> mapSeanceIdTravail;

    private Travail projet;

    private BigDecimal moyenne;

    public EleveAvecTravauxEtProjet(Utilisateur eleve, Map<Long, Travail> mapSeanceIdTravail, Travail projet) {
        super(eleve.getId(),eleve.getNom(), eleve.getPrenom(),eleve.getEmail(),eleve.getGroupe(), false);
        this.mapSeanceIdTravail = mapSeanceIdTravail;
        this.projet = projet;
    }

    public EleveAvecTravauxEtProjet() {
        super();
    }

    public EleveAvecTravauxEtProjet(Utilisateur eleve) {
        super(eleve.getId(),eleve.getNom(), eleve.getPrenom(),eleve.getEmail(),eleve.getGroupe(), false);
    }

    public Map<Long, Travail> getMapSeanceIdTravail() {
        return mapSeanceIdTravail;
    }

    public void setMapSeanceIdTravail(Map<Long, Travail> mapSeanceIdTravail) {
        this.mapSeanceIdTravail = mapSeanceIdTravail;
    }

    public Travail getProjet() {
        return projet;
    }

    public void setProjet(Travail projet) {
        this.projet = projet;
    }

    public BigDecimal getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(BigDecimal moyenne) {
        this.moyenne = moyenne;
    }
}
