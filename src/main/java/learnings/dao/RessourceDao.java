package learnings.dao;

import java.util.List;

import learnings.model.Ressource;
import learnings.model.Seance;

public interface RessourceDao {
	public List<Ressource> getRessourcesBySeance(Seance seance);

	public Ressource ajouterRessource(Ressource ressource);

	public Ressource getRessource(Long idRessource);

	public void supprimerRessource(Long idRessource);
}
