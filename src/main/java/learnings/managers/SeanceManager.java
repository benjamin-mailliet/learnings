package learnings.managers;

import java.util.Date;
import java.util.List;

import learnings.dao.RessourceDao;
import learnings.dao.SeanceDao;
import learnings.dao.impl.RessourceDaoImpl;
import learnings.dao.impl.SeanceDaoImpl;
import learnings.model.Seance;

public class SeanceManager {
	public static SeanceManager instance;

	public SeanceDao seanceDao = new SeanceDaoImpl();
	public RessourceDao ressourceDao = new RessourceDaoImpl();

	public static SeanceManager getInstance() {
		if (instance == null) {
			instance = new SeanceManager();
		}
		return instance;
	}

	public List<Seance> listerSeances() {
		List<Seance> listeCours = seanceDao.listerSeances();
		for (Seance seanceCourante : listeCours) {
			seanceCourante.setRessources(ressourceDao.getRessourcesBySeance(seanceCourante));
		}
		return listeCours;
	}

	public List<Seance> listerTPRenduAccessible() {
		Date aujourdhui = new Date();
		return seanceDao.listerTPNotesParDateRendu(aujourdhui);
	}

}
