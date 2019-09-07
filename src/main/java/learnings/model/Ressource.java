package learnings.model;

import learnings.enums.RessourceCategorie;
import learnings.enums.RessourceFormat;

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
	private RessourceFormat format;

	public Ressource(Long id, String titre, String chemin, Seance seance, RessourceCategorie categorie, RessourceFormat format) {
		this.id = id;
		this.seance = seance;
		this.chemin = chemin;
		this.titre = titre;
		this.categorie = categorie;
		this.format = format;
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

	public RessourceFormat getFormat() {
		return format;
	}

	public void setFormat(RessourceFormat format) {
		this.format = format;
	}

	public boolean isSupport() {
		return RessourceCategorie.SUPPORT.equals(this.categorie);
	}

	public boolean isCorrection() {
		return RessourceCategorie.CORRECTION.equals(this.categorie);
	}

	public boolean isLien() {
		return RessourceFormat.LIEN.equals(this.format);
	}

	public boolean isMarkdown() {
		return RessourceFormat.MARKDOWN.equals(this.format);
	}

	public boolean isTelechargement() {
		return RessourceFormat.AUTRE.equals(this.format);
	}
}
