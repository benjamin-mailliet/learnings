package learnings.managers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import learnings.dao.RessourceDao;
import learnings.dao.SeanceDao;
import learnings.dao.TravailDao;
import learnings.dao.UtilisateurDao;
import learnings.dao.impl.RessourceDaoImpl;
import learnings.dao.impl.SeanceDaoImpl;
import learnings.dao.impl.TravailDaoImpl;
import learnings.dao.impl.UtilisateurDaoImpl;
import learnings.enums.TypeSeance;
import learnings.exceptions.LearningsException;
import learnings.model.Seance;
import learnings.model.Travail;
import learnings.model.Utilisateur;
import learnings.utils.FichierComplet;
import learnings.utils.TpAvecTravail;

public class SeanceManager {
	private static SeanceManager instance;

	private static int NOMBRE_OCTETS_IN_MO = 1024 * 1024;

	private static Logger LOGGER = Logger.getLogger(SeanceManager.class.getName());

	private SeanceDao seanceDao = new SeanceDaoImpl();
	private RessourceDao ressourceDao = new RessourceDaoImpl();
	private UtilisateurDao utilisateurDao = new UtilisateurDaoImpl();
	private TravailDao travailDao = new TravailDaoImpl();

	private FichierManager fichierManager = new StockageLocalFichierManagerImpl();

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

	public List<Seance> listerSeancesRenduesAccessibles() {
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

	public void rendreTP(Long idSeance, Long idUtilisateur1, Long idUtilisateur2, String nomFichier, InputStream fichier, Long tailleFichier)
			throws LearningsException {
		Seance tp = this.verifierTpAvantRendu(idSeance);
		Utilisateur utilisateur1 = this.verifierUtilisateurAvantRendu(idUtilisateur1, true);
		Utilisateur utilisateur2 = this.verifierUtilisateurAvantRendu(idUtilisateur2, false);
		if (idUtilisateur1 == idUtilisateur2) {
			throw new IllegalArgumentException("Les deux utilisateurs doivent être différents.");
		}
		Travail travailExistant = this.verifierExistanceTravail(idSeance, idUtilisateur1, idUtilisateur2);
		if (tailleFichier / NOMBRE_OCTETS_IN_MO > 10) {
			throw new IllegalArgumentException("Le fichier est trop gros.");
		}

		String chemin = genererCheminTravail(idSeance, nomFichier);

		Travail travail = new Travail();
		travail.setEnseignement(tp);
		travail.setDateRendu(new Date());
		travail.setChemin(chemin);

		if (travailExistant == null) {
			try {
				fichierManager.ajouterFichier(travail.getChemin(), fichier);
			} catch (LearningsException e) {
				throw new LearningsException("Problème à l'enregistrement du travail.", e);
			}

			travailDao.ajouterTravail(travail);

			if (travail.getId() != null) {
				travailDao.ajouterUtilisateur(travail.getId(), utilisateur1.getId());
				if (utilisateur2 != null) {
					travailDao.ajouterUtilisateur(travail.getId(), utilisateur2.getId());
				}
			}
		} else {
			try {
				fichierManager.supprimerFichier(travailExistant.getChemin());
				fichierManager.ajouterFichier(travail.getChemin(), fichier);
			} catch (LearningsException e) {
				e.printStackTrace();
				throw new LearningsException("Problème à l'enregistrement du travail.", e);
			}

			travailDao.mettreAJourTravail(travailExistant.getId(), new Date(), travail.getChemin());
		}

		LOGGER.info(String.format("rendreTP|utilisateur1=%d|utilisateur2=%d|fichier=%d;%s", idUtilisateur1, idUtilisateur2, travail.getId(), nomFichier));
	}

	private Travail verifierExistanceTravail(Long idSeance, Long idUtilisateur1, Long idUtilisateur2) throws LearningsException {
		Travail travailUtilisateur1 = travailDao.getTravailUtilisateurParSeance(idSeance, idUtilisateur1);
		// Pas de binôme
		if (idUtilisateur2 == null) {
			if (travailUtilisateur1 == null) {
				return null;
			}
			if (travailDao.listerUtilisateurs(travailUtilisateur1.getId()).size() == 1) {
				return travailUtilisateur1;
			}
			throw new LearningsException("L'utilisateur a déjà rendu un travail avec un binôme différent.");
		}
		// Avec un binôme
		Travail travailUtilisateur2 = travailDao.getTravailUtilisateurParSeance(idSeance, idUtilisateur2);
		if (travailUtilisateur1 == null || travailUtilisateur2 == null) {
			if (travailUtilisateur1 == null && travailUtilisateur2 == null) {
				return null;
			}
			throw new LearningsException("Un des deux utilisateurs a déjà rendu un travail avec un binôme différent.");
		}
		if (travailUtilisateur1.getId().equals(travailUtilisateur2.getId())) {
			return travailUtilisateur1;
		}
		throw new LearningsException("Les deux utilisateur ont déjà rendu un travail dans des binômes différents.");
	}

	public FichierComplet getTravail(Long idTravail) throws LearningsException {
		Travail travail = travailDao.getTravail(idTravail);
		FichierComplet fichier = new FichierComplet();
		fichier.setNom(travail.getNomFichier());
		fichier.setDonnees(fichierManager.getFichier(travail.getChemin()));
		return fichier;
	}

	private String genererCheminTravail(Long idSeance, String nomFichier) {
		StringBuilder chemin = new StringBuilder();
		chemin.append("travaux/tp/");
		chemin.append(idSeance);
		chemin.append("/");
		chemin.append(UUID.randomUUID().toString().substring(0, 9));
		chemin.append(nomFichier);
		return chemin.toString();
	}

	private Utilisateur verifierUtilisateurAvantRendu(Long idUtilisateur, boolean obligatoire) {
		if (obligatoire && idUtilisateur == null) {
			throw new IllegalArgumentException("Un utilisateur obligatoire n'est pas renseigné.");
		}
		if (idUtilisateur != null) {
			Utilisateur utilisateur = utilisateurDao.getUtilisateur(idUtilisateur);
			if (utilisateur == null) {
				throw new IllegalArgumentException("Un utilisateur est inconnu.");
			}
			if (utilisateur.isAdmin()) {
				throw new IllegalArgumentException("Un administrateur ne peut pas rendre de TP.");
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
