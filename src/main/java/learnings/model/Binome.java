package learnings.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Binome {

    private String uid;

    private Seance seance;

    private List<Utilisateur> eleves;

    public Binome(String uid, Seance seance) {
        this.uid = uid;
        this.seance = seance;
        this.eleves = new ArrayList<>();
    }

    public Binome(String uid, Seance seance, Utilisateur... eleves) {
        this.uid = uid;
        this.seance = seance;
        this.eleves = Arrays.asList(eleves);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Seance getSeance() {
        return seance;
    }

    public void setSeance(Seance seance) {
        this.seance = seance;
    }

    public List<Utilisateur> getEleves() {
        return eleves;
    }

    public void setEleves(List<Utilisateur> eleves) {
        this.eleves = eleves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Binome binome = (Binome) o;

        return uid.equals(binome.uid);
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }
}
