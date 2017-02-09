package learnings.model;

import learnings.enums.StatutAppel;

public class Appel {

    private Utilisateur eleve;

    private StatutAppel statut;

    public Appel(Utilisateur eleve, StatutAppel statut) {
        this.eleve = eleve;
        this.statut = statut;
    }

    public Utilisateur getEleve() {
        return eleve;
    }

    public void setEleve(Utilisateur eleve) {
        this.eleve = eleve;
    }

    public StatutAppel getStatut() {
        return statut;
    }

    public void setStatut(StatutAppel statut) {
        this.statut = statut;
    }
}
