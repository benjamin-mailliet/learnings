package learnings.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Travail implements Serializable {
	private static final long serialVersionUID = 5229784210182658252L;
	
	private Long id;
	
	private Utilisateur eleve1;
	private Utilisateur eleve2;
	private Enseignement enseignement;
	private BigDecimal note;
	private Date dateRendu;
	public Utilisateur getEleve1() {
		return eleve1;
	}
	public void setEleve1(Utilisateur eleve1) {
		this.eleve1 = eleve1;
	}
	public Utilisateur getEleve2() {
		return eleve2;
	}
	public void setEleve2(Utilisateur eleve2) {
		this.eleve2 = eleve2;
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
}
