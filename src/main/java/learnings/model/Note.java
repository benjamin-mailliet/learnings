package learnings.model;

import java.math.BigDecimal;

public class Note {

    private Long id;

    private Utilisateur eleve;

    private Seance seance;

    private BigDecimal valeur;

    private String commentaire;

    public Note() {
    }

    public Note(Long id, Utilisateur eleve, Seance seance, BigDecimal valeur, String commentaire) {
        this.id = id;
        this.eleve = eleve;
        this.seance = seance;
        this.valeur = valeur;
        this.commentaire = commentaire;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utilisateur getEleve() {
        return eleve;
    }

    public void setEleve(Utilisateur eleve) {
        this.eleve = eleve;
    }

    public Seance getSeance() {
        return seance;
    }

    public void setSeance(Seance seance) {
        this.seance = seance;
    }

    public BigDecimal getValeur() {
        return valeur;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}
