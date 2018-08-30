package learnings.model;

import learnings.enums.RessourceCategorie;

import java.io.Serializable;
import java.util.regex.Pattern;

public class Ressource implements Serializable {
	private static final long serialVersionUID = -3287952175385402491L;
    public static Pattern LIEN_PATTERN = Pattern.compile("^https?://.*");

	private Long id;

	private Seance seance;
	private String chemin;
	private String titre;
	private RessourceCategorie categorie;

	public Ressource(Long id, String titre, String chemin, Seance seance, RessourceCategorie categorie) {
		super();
		this.id = id;
		this.seance = seance;
		this.chemin = chemin;
		this.titre = titre;
		this.categorie = categorie;
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

	public RessourceCategorie getCategorie() {
		return categorie;
	}

	public void setCategorie(RessourceCategorie categorie) {
		this.categorie = categorie;
	}

	public boolean isSupport() {
		return RessourceCategorie.SUPPORT.equals(this.categorie);
	}

	public boolean isCorrection() {
		return RessourceCategorie.CORRECTION.equals(this.categorie);
	}

	public boolean isLien() {
		return this.chemin != null && LIEN_PATTERN.matcher(this.chemin).matches();
	}
}
