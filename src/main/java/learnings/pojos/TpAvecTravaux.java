package learnings.pojos;

import learnings.model.Binome;
import learnings.model.RenduTp;
import learnings.model.Seance;

import java.util.List;

public class TpAvecTravaux {

	private Seance tp;

	private List<RenduTp> travaux;

	private Binome binome;

	public Seance getTp() {
		return tp;
	}

	public void setTp(Seance tp) {
		this.tp = tp;
	}

	public List<RenduTp> getTravaux() {
		return travaux;
	}

	public void setTravaux(List<RenduTp> travaux) {
		this.travaux = travaux;
	}

	public Binome getBinome() {
		return binome;
	}

	public void setBinome(Binome binome) {
		this.binome = binome;
	}
}
