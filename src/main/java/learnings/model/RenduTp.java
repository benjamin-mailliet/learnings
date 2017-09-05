package learnings.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RenduTp extends Travail {

    private Binome binome;

    public RenduTp() {
    }

    public RenduTp(Long id, BigDecimal note, LocalDateTime dateRendu, String chemin, String commentaire, Binome binome) {
        super(id, note, dateRendu, chemin, commentaire);
        this.binome = binome;
    }

    public Binome getBinome() {
        return binome;
    }

    public void setBinome(Binome binome) {
        this.binome = binome;
    }
}
