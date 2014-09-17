package learnings.dao;

import java.util.List;

import learnings.model.Enseignement;
import learnings.model.Ressource;

public interface RessourceDao {
	public List<Ressource> getRessources(Enseignement enseignement);

	public Ressource ajouterRessource(Ressource ressource);

	public Ressource getRessource(Long idRessource);

	public void supprimerRessource(Long idRessource);
}
