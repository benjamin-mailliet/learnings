package learnings.pojos;

import learnings.model.RenduProjet;
import learnings.model.RenduTp;
import learnings.model.Travail;
import learnings.model.Utilisateur;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class EleveAvecTravauxEtProjet extends Utilisateur {

    private Map<Long, List<RenduTp>> mapSeanceIdTravail;

    private List<RenduProjet> projet;

    private BigDecimal moyenne;

    public EleveAvecTravauxEtProjet(Utilisateur eleve, Map<Long, List<RenduTp>> mapSeanceIdTravail, List<RenduProjet> projet) {
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

    public Map<Long, List<RenduTp>> getMapSeanceIdTravail() {
        return mapSeanceIdTravail;
    }

    public void setMapSeanceIdTravail(Map<Long, List<RenduTp>> mapSeanceIdTravail) {
        this.mapSeanceIdTravail = mapSeanceIdTravail;
    }

    public List<RenduProjet> getProjet() {
        return projet;
    }

    public void setProjet(List<RenduProjet> projet) {
        this.projet = projet;
    }

    public BigDecimal getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(BigDecimal moyenne) {
        this.moyenne = moyenne;
    }
}
