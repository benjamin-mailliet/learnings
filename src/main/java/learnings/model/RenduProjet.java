package learnings.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RenduProjet extends Travail {

    private String urlRepository;
    private Utilisateur eleve;
    private Projet projet;

    public RenduProjet() {

    }

    public RenduProjet(Long id, BigDecimal note, LocalDateTime dateRendu, String chemin, String commentaire, String urlRepository, Utilisateur eleve, Projet projet) {
        super(id, note, dateRendu, chemin, commentaire);
        this.urlRepository = urlRepository;
        this.eleve = eleve;
        this.projet = projet;
    }

    public String getUrlRepository() {
        return urlRepository;
    }

    public void setUrlRepository(String urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Utilisateur getEleve() {
        return eleve;
    }

    public void setEleve(Utilisateur eleve) {
        this.eleve = eleve;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }
}
