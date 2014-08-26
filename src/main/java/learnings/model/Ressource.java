package learnings.model;

import java.io.Serializable;

public class Ressource implements Serializable {
	private static final long serialVersionUID = -3287952175385402491L;

	private Long id;

	private Enseignement enseignement;
	private String chemin;
	private String titre;

	public Ressource(Long id, String titre, String chemin, Enseignement enseignement) {
		super();
		this.id = id;
		this.enseignement = enseignement;
		this.chemin = chemin;
		this.titre = titre;
	}

	public Enseignement getEnseignement() {
		return enseignement;
	}

	public void setEnseignement(Enseignement enseignement) {
		this.enseignement = enseignement;
	}

	public String getChemin() {
		return chemin;
	}

	public void setChemin(String chemin) {
		this.chemin = chemin;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public Long getId() {
		return id;
	}

}
