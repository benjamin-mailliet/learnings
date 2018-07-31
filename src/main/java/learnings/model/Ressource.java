package learnings.model;

import java.io.Serializable;

public class Ressource implements Serializable {
	private static final long serialVersionUID = -3287952175385402491L;

	private Long id;

	private Seance seance;
	private String chemin;
	private String titre;

	public Ressource(Long id, String titre, String chemin, Seance seance) {
		super();
		this.id = id;
		this.seance = seance;
		this.chemin = chemin;
		this.titre = titre;
	}

	public Seance getSeance() {
		return seance;
	}

	public void setSeance(Seance seance) {
		this.seance = seance;
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
