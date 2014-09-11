package learnings.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import learnings.utils.FichierUtils;

public class Travail implements Serializable {
	private static final long serialVersionUID = 5229784210182658252L;

	private Long id;

	private Enseignement enseignement;
	private BigDecimal note;
	private Date dateRendu;
	private String chemin;
	private String commentaire;
	private List<Utilisateur> utilisateurs;

	public Travail() {
	}

	public Travail(Long id, Enseignement enseignement, BigDecimal note, Date dateRendu, String chemin, String commentaire) {
		super();
		this.id = id;
		this.enseignement = enseignement;
		this.note = note;
		this.dateRendu = dateRendu;
		this.chemin = chemin;
		this.commentaire = commentaire;
	}

	public Enseignement getEnseignement() {
		return enseignement;
	}

	public void setEnseignement(Enseignement enseignement) {
		this.enseignement = enseignement;
	}

	public BigDecimal getNote() {
		return note;
	}

	public void setNote(BigDecimal note) {
		this.note = note;
	}

	public Date getDateRendu() {
		return dateRendu;
	}

	public void setDateRendu(Date dateRendu) {
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

	public List<Utilisateur> getUtilisateurs() {
		return utilisateurs;
	}

	public void setUtilisateurs(List<Utilisateur> utilisateurs) {
		this.utilisateurs = utilisateurs;
	}

}
