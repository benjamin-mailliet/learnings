package learnings.dao;

import java.util.List;

import learnings.model.Enseignement;
import learnings.model.Ressource;

public interface RessourceDao {
	List<Ressource> getRessources(Enseignement enseignement);

	Ressource ajouterRessource(Ressource ressource);

	Ressource getRessource(Long idRessource);

	void supprimerRessource(Long idRessource);
}
