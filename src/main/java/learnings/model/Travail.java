package learnings.model;

import learnings.utils.FichierUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class Travail implements Serializable {
	private static final long serialVersionUID = 5229784210182658252L;

	public static int COEFF_PROJET = 4;

	private Long id;

	private BigDecimal note;
	private String commentaireNote;
	private LocalDateTime dateRendu;
	private String chemin;
	private String commentaire;

	public Travail() {
	}

	public Travail(Long id, BigDecimal note, LocalDateTime dateRendu, String chemin, String commentaire, String commentaireNote) {
		super();
		this.id = id;
		this.note = note;
		this.commentaireNote=commentaireNote;
		this.dateRendu = dateRendu;
		this.chemin = chemin;
		this.commentaire = commentaire;
	}

	public Travail(Long id, BigDecimal note, LocalDateTime dateRendu, String chemin, String commentaire) {
		super();
		this.id = id;
		this.note = note;
		this.dateRendu = dateRendu;
		this.chemin = chemin;
		this.commentaire = commentaire;
	}

	public BigDecimal getNote() {
		return note;
	}

	public void setNote(BigDecimal note) {
		this.note = note;
	}

	public String getCommentaireNote() {
		return commentaireNote;
	}

	public void setCommentaireNote(String commentaireNote) {
		this.commentaireNote = commentaireNote;
	}

	public LocalDateTime getDateRendu() {
		return dateRendu;
	}

	public void setDateRendu(LocalDateTime dateRendu) {
		this.dateRendu = dateRendu;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getChemin() {
		return chemin;
	}

	public void setChemin(String chemin) {
		this.chemin = chemin;
	}

	public String getNomFichier() {
		return FichierUtils.extraireNomFichier(this.chemin);
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
}
