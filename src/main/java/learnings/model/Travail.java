package learnings.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Travail implements Serializable {
	private static final long serialVersionUID = 5229784210182658252L;

	private Long id;

	private Enseignement enseignement;
	private BigDecimal note;
	private Date dateRendu;
	private String chemin;

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

}
