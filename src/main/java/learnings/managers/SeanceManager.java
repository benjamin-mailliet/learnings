package learnings.managers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import learnings.dao.RessourceDao;
import learnings.dao.SeanceDao;
import learnings.dao.TravailDao;
import learnings.dao.impl.RessourceDaoImpl;
import learnings.dao.impl.SeanceDaoImpl;
import learnings.dao.impl.TravailDaoImpl;
import learnings.model.Seance;
import learnings.model.Travail;
import learnings.pojos.TpAvecTravail;

public class SeanceManager {
	private static SeanceManager instance;

	private SeanceDao seanceDao = new SeanceDaoImpl();
	private RessourceDao ressourceDao = new RessourceDaoImpl();
	private TravailDao travailDao = new TravailDaoImpl();

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

	public List<Seance> listerSeancesNotees() {
		return seanceDao.listerSeancesNotees();
	}

	public List<Seance> listerSeancesAccessibles() {
		Date aujourdhui = new Date();
		List<Seance> listeCours = seanceDao.listerSeancesWhereDateBefore(aujourdhui);
		for (Seance seanceCourante : listeCours) {
			seanceCourante.setRessources(ressourceDao.getRessourcesBySeance(seanceCourante));
		}
		return listeCours;
	}

	public List<TpAvecTravail> listerTPRenduAccessible(Long idUtilisateur) {
		if (idUtilisateur == null) {
			throw new IllegalArgumentException("L'utlisateur ne peut pas être null.");
		}
		Date aujourdhui = new Date();
		List<Seance> listeTps = seanceDao.listerTPNotesParDateRendu(aujourdhui);
		List<TpAvecTravail> listeTpsAvecTravaux = new ArrayList<TpAvecTravail>();
		for (Seance tp : listeTps) {
			TpAvecTravail tpAvecTravaux = new TpAvecTravail();
			tpAvecTravaux.setTp(tp);
			tpAvecTravaux.setTravail(travailDao.getTravailUtilisateurParSeance(tp.getId(), idUtilisateur));
			listeTpsAvecTravaux.add(tpAvecTravaux);
		}
		return listeTpsAvecTravaux;
	}

	public Seance getSeanceAvecTravaux(Long idSeance) {
		if (idSeance == null) {
			throw new IllegalArgumentException("L'identifiant de la séance est incorrect.");
		}
		Seance seance = seanceDao.getSeance(idSeance);
		if (seance == null) {
			throw new IllegalArgumentException("L'identifiant de la séance est inconnu.");
		}
		seance.setTravauxRendus(travailDao.listerTravauxParSeance(idSeance));
		for (Travail travailRendu : seance.getTravauxRendus()) {
			travailRendu.setUtilisateurs(travailDao.listerUtilisateurs(travailRendu.getId()));
		}

		return seance;
	}
}
