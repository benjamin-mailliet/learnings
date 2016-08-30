package learnings.managers;

import learnings.dao.ProjetDao;
import learnings.dao.RessourceDao;
import learnings.dao.TravailDao;
import learnings.dao.impl.ProjetDaoImpl;
import learnings.dao.impl.RessourceDaoImpl;
import learnings.dao.impl.TravailDaoImpl;
import learnings.model.Projet;
import learnings.model.Travail;
import learnings.pojos.ProjetAvecTravail;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjetManager {

	private static String KEY_NB_JOURS_LOT_1 = "nbJoursLot1";
	private static String KEY_NB_JOURS_LOT_2 = "nbJoursLot2";
	
	private static ProjetManager instance;

	public static ProjetManager getInstance() {
		if (instance == null) {
			instance = new ProjetManager();
		}
		return instance;
	}

	private ProjetDao projetDao = new ProjetDaoImpl();
	private RessourceDao ressourceDao = new RessourceDaoImpl();
	private TravailDao travailDao = new TravailDaoImpl();

	public List<Projet> listerProjets() {
		return projetDao.listerProjets();
	}

	public Projet getProjetAvecRessources(Long idProjet) {
		if (idProjet == null) {
			throw new IllegalArgumentException("L'identifiant du projet est nul.");
		}
		Projet projet = projetDao.getProjet(idProjet);
		projet.setRessources(ressourceDao.getRessources(projet));

		return projet;
	}

	public void ajouterProjet(Projet projet) {
		if (projet == null) {
			throw new IllegalArgumentException("Le projet est null.");
		}
		if (projet.getTitre() == null || "".equals(projet.getTitre())) {
			throw new IllegalArgumentException("Le titre du projet doit être renseigné.");
		}
		if (projet.getDateLimiteRenduLot1() == null || projet.getDateLimiteRenduLot2() == null) {
			throw new IllegalArgumentException("Les dates de limite de rendu des deux lots doivent être renseignées.");
		}
		projetDao.ajouterProjet(projet);
	}

	public void modifierProjet(Projet projet) {
		if (projet == null) {
			throw new IllegalArgumentException("Le projet est null.");
		}
		if (projet.getId() == null) {
			throw new IllegalArgumentException("L'identifiant du projet doit être renseigné.");
		}
		if (projet.getTitre() == null || "".equals(projet.getTitre())) {
			throw new IllegalArgumentException("Le titre du projet doit être renseigné.");
		}
		if (projet.getDateLimiteRenduLot1() == null || projet.getDateLimiteRenduLot2() == null) {
			throw new IllegalArgumentException("Les dates de limite de rendu des deux lots doivent être renseignées.");
		}
		projetDao.modifierProjet(projet);
	}

	public Projet getLastProjetAvecRessources() {
		Long idLastProjet = projetDao.getLastProjetId();
		if (idLastProjet != null) {
			return getProjetAvecRessources(idLastProjet);
		} else {
			return null;
		}
	}

	public ProjetAvecTravail getProjetAvecTravail(Long idUtilisateur) {
		if (idUtilisateur == null) {
			throw new IllegalArgumentException("L'identifiant de l'utilisateur est incorrect.");
		}
		ProjetAvecTravail projetAvecTravail = new ProjetAvecTravail();
		Long lastProjetId = projetDao.getLastProjetId();
		projetAvecTravail.setProjet(projetDao.getProjet(lastProjetId));
		
		Travail travailRendu = travailDao.getTravailUtilisateurParProjet(lastProjetId,idUtilisateur);
		projetAvecTravail.setTravail(travailRendu);
		
		Map<String,Integer> mapJoursRestants = getNbJoursRestants(projetAvecTravail);
		
		projetAvecTravail.setNbJoursRestantsLot1(mapJoursRestants.get(KEY_NB_JOURS_LOT_1));
		projetAvecTravail.setNbJoursRestantsLot2(mapJoursRestants.get(KEY_NB_JOURS_LOT_2));

		return projetAvecTravail;
	}
	
	private Map<String, Integer> getNbJoursRestants(
			ProjetAvecTravail projetAvecTravail) {
		Date today = new Date();

		Long millisecondsPerDay = 86400000L;
		Integer nbJoursRestantsLot1 = Math.round((projetAvecTravail.getProjet().getDateLimiteRenduLot1().getTime()- today.getTime())/millisecondsPerDay);
		Integer nbJoursRestantsLot2 = Math.round((projetAvecTravail.getProjet().getDateLimiteRenduLot2().getTime()- today.getTime())/millisecondsPerDay);

		HashMap<String, Integer> mapNbJoursRestants = new HashMap<String, Integer>();

		mapNbJoursRestants.put(KEY_NB_JOURS_LOT_1, nbJoursRestantsLot1>0 ? nbJoursRestantsLot1 : 0);
		mapNbJoursRestants.put(KEY_NB_JOURS_LOT_2, nbJoursRestantsLot2>0 ? nbJoursRestantsLot2 : 0);
		return mapNbJoursRestants;
	}
	
	public Projet getProjetAvecTravaux(Long idProjet) {
		if (idProjet == null) {
			throw new IllegalArgumentException("L'identifiant du projet est incorrect.");
		}
		Projet projet = projetDao.getProjet(idProjet);
		if (projet == null) {
			throw new IllegalArgumentException("L'identifiant du projet est inconnu.");
		}
		projet.setTravauxRendus(travailDao.listerTravauxParProjet(idProjet));
		for (Travail travailRendu : projet.getTravauxRendus()) {
			travailRendu.setUtilisateurs(travailDao.listerUtilisateurs(travailRendu.getId()));
		}

		return projet;
	}
}
