package learnings.enums;

public enum Groupe {
    GROUPE_1("Groupe 1"), GROUPE_2("Groupe 2");

    private String libelle;

    Groupe(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
