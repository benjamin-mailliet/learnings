package learnings.model;

import java.io.Serializable;
import java.util.Date;

public class TP extends Enseignement implements Serializable {
	private static final long serialVersionUID = 4961204598561923877L;

	private Boolean isNote;
	private Date dateLimiteRendu;
	private Date date;

	public TP(Long id, String titre, String description, Boolean isNote, Date dateLimiteRendu, Date date) {
		super(id, titre, description);
		this.isNote = isNote;
		this.dateLimiteRendu = dateLimiteRendu;
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

}
