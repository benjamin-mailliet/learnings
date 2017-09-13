package learnings.model;

public class Binome {

    private Long id;

    private Seance seance;

    private Utilisateur eleve1;

    private Utilisateur eleve2;

    public Binome(Long id, Seance seance) {
        this.id = id;
        this.seance = seance;
    }

    public Binome(Long id, Seance seance, Utilisateur eleve1, Utilisateur eleve2) {
        this.id = id;
        this.seance = seance;
        this.eleve1 = eleve1;
        this.eleve2 = eleve2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Seance getSeance() {
        return seance;
    }

    public void setSeance(Seance seance) {
        this.seance = seance;
    }

    public Utilisateur getEleve1() {
        return eleve1;
    }

    public void setEleve1(Utilisateur eleve1) {
        this.eleve1 = eleve1;
    }

    public Utilisateur getEleve2() {
        return eleve2;
    }

    public void setEleve2(Utilisateur eleve2) {
        this.eleve2 = eleve2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Binome binome = (Binome) o;

        return id.equals(binome.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
