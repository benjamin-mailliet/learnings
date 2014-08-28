package learnings.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import learnings.enums.TypeSeance;

public class Seance extends Enseignement implements Serializable {
	private static final long serialVersionUID = 4961204598561923877L;

	private Boolean isNote;
	private Date dateLimiteRendu;
	private Date date;
	private TypeSeance type;
	private List<Travail> travauxRendus;

	public Seance(Long id, String titre, String description, Date date, Boolean isNote, Date dateLimiteRendu, TypeSeance type) {
		super(id, titre, description);
		this.isNote = isNote;
		this.dateLimiteRendu = dateLimiteRendu;
		this.date = date;
		this.type = type;
	}

	public TypeSeance getType() {
		return type;
	}

	public void setType(TypeSeance type) {
		this.type = type;
	}

	public Seance(Long id, String titre, String description, java.util.Date date) {
		super(id, titre, description);
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Boolean getIsNote() {
		return isNote;
	}

	public void setIsNote(Boolean isNote) {
		this.isNote = isNote;
	}

	public Date getDateLimiteRendu() {
		return dateLimiteRendu;
	}

	public void setDateLimiteRendu(Date dateLimiteRendu) {
		this.dateLimiteRendu = dateLimiteRendu;
	}

	public List<Travail> getTravauxRendus() {
		return travauxRendus;
	}

	public void setTravauxRendus(List<Travail> travauxRendus) {
		this.travauxRendus = travauxRendus;
	}

}
