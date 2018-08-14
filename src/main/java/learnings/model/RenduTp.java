package learnings.model;

import learnings.utils.FichierUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RenduTp implements Serializable {
	private static final long serialVersionUID = 5229784210182658252L;

	private Long id;

	private Binome binome;

	private BigDecimal note;
	private LocalDateTime dateRendu;
	private String chemin;
	private String commentaire;

	public RenduTp() {
	}

	public RenduTp(Long id, BigDecimal note, LocalDateTime dateRendu, String chemin, String commentaire, Binome binome) {
		super();
		this.id = id;
		this.note = note;
		this.dateRendu = dateRendu;
		this.chemin = chemin;
		this.commentaire = commentaire;
		this.binome = binome;
	}

	public BigDecimal getNote() {
		return note;
	}

	public void setNote(BigDecimal note) {
		this.note = note;
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

	public Binome getBinome() {
		return binome;
	}

	public void setBinome(Binome binome) {
		this.binome = binome;
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
