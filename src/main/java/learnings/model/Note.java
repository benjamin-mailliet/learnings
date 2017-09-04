package learnings.model;

import java.math.BigDecimal;

public class Note {

    public static int COEFF_PROJET = 4;

    private Long id;

    private Utilisateur eleve;

    private Enseignement enseignement;

    private BigDecimal valeur;

    private String commentaire;

    public Note() {
    }

    public Note(Long id, Utilisateur eleve, Enseignement enseignement, BigDecimal valeur, String commentaire) {
        this.id = id;
        this.eleve = eleve;
        this.enseignement = enseignement;
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

    public Enseignement getEnseignement() {
        return enseignement;
    }

    public void setEnseignement(Enseignement enseignement) {
        this.enseignement = enseignement;
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
