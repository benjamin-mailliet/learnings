package learnings.dao;

import java.util.List;

import learnings.model.Ressource;
import learnings.model.Seance;

public interface RessourceDao {
	public List<Ressource> getRessourcesBySeance(Seance seance);
}
