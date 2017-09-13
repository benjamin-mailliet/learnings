package learnings.pojos;

import learnings.model.Binome;
import learnings.model.RenduTp;
import learnings.model.Seance;

import java.util.List;
import java.util.Map;

public class SeanceAvecRendus {

    private Seance seance;

    private Map<Binome, List<RenduTp>> rendus;

    public Seance getSeance() {
        return seance;
    }

    public void setSeance(Seance seance) {
        this.seance = seance;
    }

    public Map<Binome, List<RenduTp>> getRendus() {
        return rendus;
    }

    public void setRendus(Map<Binome, List<RenduTp>> rendus) {
        this.rendus = rendus;
    }
}
