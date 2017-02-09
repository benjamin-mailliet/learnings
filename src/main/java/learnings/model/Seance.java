package learnings.model;

import learnings.enums.TypeSeance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

public class Seance extends Enseignement implements Serializable {
	private static final long serialVersionUID = 4961204598561923877L;

	private Boolean isNote;
	private Date dateLimiteRendu;
	private Date date;
	private TypeSeance type;
	private List<Travail> travauxRendus;
	private BigDecimal sommeNotes;
	private int nbNotes;
	private BigDecimal moyenne;

	public Seance(Long id, String titre, String description, Date date, Boolean isNote, Date dateLimiteRendu, TypeSeance type) {
		super(id, titre, description);
		this.isNote = isNote;
		this.dateLimiteRendu = dateLimiteRendu;
		this.date = date;
		this.type = type;
		this.nbNotes = 0;
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

	public boolean isDatePassee() {
		return this.date.before(new Date());
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

	public BigDecimal getSommeNotes() {
		return sommeNotes;
	}

	public void setSommeNotes(BigDecimal sommeNotes) {
		this.sommeNotes = sommeNotes;
	}

	public int getNbNotes() {
		return nbNotes;
	}

	public void setNbNotes(int nbNotes) {
		this.nbNotes = nbNotes;
	}

	public BigDecimal getMoyenne() {
		return moyenne;
	}

	public void setMoyenne(BigDecimal moyenne) {
		this.moyenne = moyenne;
	}

	public void addNote(BigDecimal note){
		this.nbNotes++;
		if(this.sommeNotes!=null) {
			this.sommeNotes = this.sommeNotes.add(note);
		}else{
			this.sommeNotes=note;
		}
		this.calculerMoyenne();
	}

	private void calculerMoyenne() {
		if(this.sommeNotes!=null && this.nbNotes>0){
			this.moyenne = this.sommeNotes.divide(new BigDecimal(this.nbNotes),2, RoundingMode.HALF_EVEN);
		}
	}
}
