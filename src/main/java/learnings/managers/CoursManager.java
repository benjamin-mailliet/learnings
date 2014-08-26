package learnings.managers;

import java.util.List;

import learnings.dao.CoursDao;
import learnings.dao.RessourceDao;
import learnings.dao.impl.CoursDaoImpl;
import learnings.dao.impl.RessourceDaoImpl;
import learnings.model.Cours;

public class CoursManager {
	public static CoursManager instance;

	public CoursDao coursDao = new CoursDaoImpl();
	public RessourceDao ressourceDao = new RessourceDaoImpl();

	public static CoursManager getInstance() {
		if (instance == null) {
			instance = new CoursManager();
		}
		return instance;
	}

	public List<Cours> listerCours() {
		List<Cours> listeCours = coursDao.listerCours();
		for (Cours coursCourant : listeCours) {
			coursCourant.setRessources(ressourceDao.getRessourcesByCours(coursCourant));
		}
		return listeCours;
	}

}
