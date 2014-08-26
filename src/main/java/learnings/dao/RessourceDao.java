package learnings.dao;

import java.util.List;

import learnings.model.Cours;
import learnings.model.Ressource;

public interface RessourceDao {
	public List<Ressource> getRessourcesByCours(Cours cours);
}
