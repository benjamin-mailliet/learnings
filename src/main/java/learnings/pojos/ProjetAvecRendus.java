package learnings.pojos;

import learnings.model.Projet;
import learnings.model.RenduProjet;
import learnings.model.Utilisateur;

import java.util.List;
import java.util.Map;

public class ProjetAvecRendus {

	private Projet projet;

	private Map<Utilisateur,List<RenduProjet>> rendus;
	
	private Integer nbJoursRestantsLot1;
	
	private Integer nbJoursRestantsLot2;

	public Projet getProjet() {
		return projet;
	}

	public void setProjet(Projet projet) {
		this.projet = projet;
	}

	public Map<Utilisateur,List<RenduProjet>> getRendus() {
		return rendus;
	}

	public void setRendus(Map<Utilisateur,List<RenduProjet>> rendus) {
		this.rendus = rendus;
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
