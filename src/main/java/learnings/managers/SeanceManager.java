package learnings.managers;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import learnings.dao.RessourceDao;
import learnings.dao.SeanceDao;
import learnings.dao.TravailDao;
import learnings.dao.UtilisateurDao;
import learnings.dao.impl.RessourceDaoImpl;
import learnings.dao.impl.SeanceDaoImpl;
import learnings.dao.impl.TravailDaoImpl;
import learnings.dao.impl.UtilisateurDaoImpl;
import learnings.enums.TypeSeance;
import learnings.model.Seance;
import learnings.model.Travail;
import learnings.model.Utilisateur;

public class SeanceManager {
	private static SeanceManager instance;

	private SeanceDao seanceDao = new SeanceDaoImpl();
	private RessourceDao ressourceDao = new RessourceDaoImpl();
	private UtilisateurDao utilisateurDao = new UtilisateurDaoImpl();
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

	public List<Seance> listerSeancesRenduesAccessibles() {
		Date aujourdhui = new Date();
		List<Seance> listeCours = seanceDao.listerSeancesWhereDateBefore(aujourdhui);
		for (Seance seanceCourante : listeCours) {
			seanceCourante.setRessources(ressourceDao.getRessourcesBySeance(seanceCourante));
		}
		return listeCours;
	}

	public List<Seance> listerTPRenduAccessible() {
		Date aujourdhui = new Date();
		return seanceDao.listerTPNotesParDateRendu(aujourdhui);
	}

	public void rendreTP(Long idSeance, Long idUtilisateur1, Long idUtilisateur2, InputStream fichier) {
		Seance tp = this.verifierTpAvantRendu(idSeance);
		Utilisateur utilisateur1 = this.verifierUtilisateurAvantRendu(idUtilisateur1, true);
		Utilisateur utilisateur2 = this.verifierUtilisateurAvantRendu(idUtilisateur2, false);
		if (idUtilisateur1 == idUtilisateur2) {
			throw new IllegalArgumentException("Les deux utilisateurs doivent être différents.");
		}

		Travail travail = new Travail();
		travail.setEnseignement(tp);
		travail.setDateRendu(new Date());
		travail.setChemin("notyetdone");

		travailDao.ajouterTravail(travail);

		if (travail.getId() != null) {
			travailDao.ajouterUtilisateur(travail.getId(), utilisateur1.getId());
			travailDao.ajouterUtilisateur(travail.getId(), utilisateur2.getId());
		}
	}

	private Utilisateur verifierUtilisateurAvantRendu(Long idUtilisateur, boolean obligatoire) {
		if (obligatoire && idUtilisateur == null) {
			throw new IllegalArgumentException("Un utilisateur obligatoire est null");
		}
		if (idUtilisateur != null) {
			Utilisateur utilisateur = utilisateurDao.getUtilisateur(idUtilisateur);
			if (utilisateur == null || utilisateur.isAdmin()) {
				throw new IllegalArgumentException("Un utilisateur est incorrect");
			}
			return utilisateur;
		}
		return null;
	}

	private Seance verifierTpAvantRendu(Long idSeance) {
		if (idSeance == null) {
			throw new IllegalArgumentException("L'identifiant du tp est incorrect");
		}
		Seance seance = seanceDao.getSeance(idSeance);
		if (seance == null || !TypeSeance.TP.equals(seance.getType()) || !seance.getIsNote()) {
			throw new IllegalArgumentException("L'identifiant du tp est incorrect");
		}
		Date maintenant = new Date();
		if (maintenant.before(seance.getDate()) || maintenant.after(seance.getDateLimiteRendu())) {
			throw new IllegalArgumentException("Le travail ne peut pas être rendu maintenant");
		}
		return seance;
	}
}
