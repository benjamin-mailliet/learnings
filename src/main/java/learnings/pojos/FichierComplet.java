package learnings.pojos;

import java.io.InputStream;

public class FichierComplet {
	private InputStream donnees;
	private String nom;

	public InputStream getDonnees() {
		return donnees;
	}

	public void setDonnees(InputStream donnees) {
		this.donnees = donnees;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

}
