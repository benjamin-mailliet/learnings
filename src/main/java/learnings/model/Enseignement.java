package learnings.model;

import java.io.Serializable;
import java.util.List;

public abstract class Enseignement implements Serializable {
	private static final long serialVersionUID = 2169313197087543292L;
	
	private Long id;
	private String titre;
	private String description;
	private List<Ressource> ressources;
	
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Ressource> getRessources() {
		return ressources;
	}
	public void setRessources(List<Ressource> ressources) {
		this.ressources = ressources;
	}
	public Long getId() {
		return id;
	}
}
