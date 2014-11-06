package learnings.pojos;

import learnings.model.Projet;
import learnings.model.Travail;

public class ProjetAvecTravail {

	private Projet projet;

	private Travail travail;
	
	private Integer nbJoursRestantsLot1;
	
	private Integer nbJoursRestantsLot2;

	public Projet getProjet() {
		return projet;
	}

	public void setProjet(Projet projet) {
		this.projet = projet;
	}

	public Travail getTravail() {
		return travail;
	}

	public void setTravail(Travail travail) {
		this.travail = travail;
	}
	
	public Integer getNbJoursRestantsLot1() {
		return nbJoursRestantsLot1;
	}

	public void setNbJoursRestantsLot1(Integer nbJoursRestantsLot1) {
		this.nbJoursRestantsLot1 = nbJoursRestantsLot1;
	}

	public Integer getNbJoursRestantsLot2() {
		return nbJoursRestantsLot2;
	}

	public void setNbJoursRestantsLot2(Integer nbJoursRestantsLot2) {
		this.nbJoursRestantsLot2 = nbJoursRestantsLot2;
	}

}
