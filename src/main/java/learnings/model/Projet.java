package learnings.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Projet extends Enseignement implements Serializable {

	private static final long serialVersionUID = 3314226385866295755L;

	private Date dateLimiteRenduLot1;
	private Date dateLimiteRenduLot2;
	private List<RenduProjet> travauxRendus;
	

	public Date getDateLimiteRenduLot1() {
		return dateLimiteRenduLot1;
	}

	public List<RenduProjet> getTravauxRendus() {
		return travauxRendus;
	}

	public void setTravauxRendus(List<RenduProjet> travauxRendus) {
		this.travauxRendus = travauxRendus;
	}

	public void setDateLimiteRenduLot1(Date dateLimiteRenduLot1) {
		this.dateLimiteRenduLot1 = dateLimiteRenduLot1;
	}

	public Date getDateLimiteRenduLot2() {
		return dateLimiteRenduLot2;
	}

	public Projet(Long id, String titre, String description, Date dateLimiteRenduLot1, Date dateLimiteRenduLot2) {
		super(id, titre, description);
		this.dateLimiteRenduLot1 = dateLimiteRenduLot1;
		this.dateLimiteRenduLot2 = dateLimiteRenduLot2;
	}

	public void setDateLimiteRenduLot2(Date dateLimiteRenduLot2) {
		this.dateLimiteRenduLot2 = dateLimiteRenduLot2;
	}
	
	
}
