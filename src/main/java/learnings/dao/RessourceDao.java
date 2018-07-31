package learnings.dao;

import learnings.model.Ressource;
import learnings.model.Seance;

import java.util.List;

public interface RessourceDao {
	List<Ressource> getRessources(Seance seance);

	Ressource ajouterRessource(Ressource ressource);

	Ressource getRessource(Long idRessource);

	void supprimerRessource(Long idRessource);
}
