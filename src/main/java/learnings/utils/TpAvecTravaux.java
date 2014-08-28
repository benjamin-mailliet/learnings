package learnings.utils;

import java.util.List;

import learnings.model.Seance;
import learnings.model.Travail;

public class TpAvecTravaux {

	private Seance tp;

	private List<Travail> travaux;

	public Seance getTp() {
		return tp;
	}

	public void setTp(Seance tp) {
		this.tp = tp;
	}

	public List<Travail> getTravaux() {
		return travaux;
	}

	public void setTravaux(List<Travail> travaux) {
		this.travaux = travaux;
	}

}
