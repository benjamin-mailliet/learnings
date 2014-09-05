package learnings.managers;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

import learnings.dao.SeanceDao;
import learnings.dao.TravailDao;
import learnings.dao.UtilisateurDao;
import learnings.dao.impl.SeanceDaoImpl;
import learnings.dao.impl.TravailDaoImpl;
import learnings.dao.impl.UtilisateurDaoImpl;
import learnings.enums.TypeSeance;
import learnings.exceptions.LearningsException;
import learnings.model.Seance;
import learnings.model.Travail;
import learnings.model.Utilisateur;
import learnings.pojos.FichierComplet;

public class TravailManager {
	private static TravailManager instance;

	private static int NOMBRE_OCTETS_IN_MO = 1024 * 1024;

	private static Logger LOGGER = Logger.getLogger(TravailManager.class.getName());

	private SeanceDao seanceDao = new SeanceDaoImpl();
	private UtilisateurDao utilisateurDao = new UtilisateurDaoImpl();
	private TravailDao travailDao = new TravailDaoImpl();

	private FichierManager fichierManager = new StockageLocalFichierManagerImpl();

	public static TravailManager getInstance() {
		if (instance == null) {
			instance = new TravailManager();
		}
		return instance;
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
			ajouterTravail(fichier, utilisateur1, utilisateur2, travail);
		} else {
			modifierTravail(fichier, travailExistant, travail);
		}

		LOGGER.info(String.format("rendreTP|utilisateur1=%d|utilisateur2=%d|fichier=%d;%s", idUtilisateur1, idUtilisateur2, travail.getId(), nomFichier));
	}

	public FichierComplet getFichierTravail(Long idTravail) throws LearningsException {
		Travail travail = travailDao.getTravail(idTravail);
		FichierComplet fichier = new FichierComplet();
		fichier.setNom(travail.getNomFichier());
		fichier.setDonnees(fichierManager.getFichier(travail.getChemin()));
		return fichier;
	}

	protected void modifierTravail(InputStream fichier, Travail travailExistant, Travail travail) throws LearningsException {
		try {
			fichierManager.supprimerFichier(travailExistant.getChemin());
			fichierManager.ajouterFichier(travail.getChemin(), fichier);
			travailDao.mettreAJourTravail(travailExistant.getId(), new Date(), travail.getChemin());
		} catch (LearningsException e) {
			throw new LearningsException("Problème à l'enregistrement du travail.", e);
		}
	}

	protected void ajouterTravail(InputStream fichier, Utilisateur utilisateur1, Utilisateur utilisateur2, Travail travail) throws LearningsException {
		try {
			fichierManager.ajouterFichier(travail.getChemin(), fichier);
		} catch (LearningsException e) {
			throw new LearningsException("Problème à l'enregistrement du travail.", e);
		}

		travail = travailDao.ajouterTravail(travail);

		if (travail.getId() != null) {
			travailDao.ajouterUtilisateur(travail.getId(), utilisateur1.getId());
			if (utilisateur2 != null) {
				travailDao.ajouterUtilisateur(travail.getId(), utilisateur2.getId());
			}
		}
	}

	protected Travail verifierExistanceTravail(Long idSeance, Long idUtilisateur1, Long idUtilisateur2) throws LearningsException {
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

	protected String genererCheminTravail(Long idSeance, String nomFichier) {
		StringBuilder chemin = new StringBuilder();
		chemin.append("travaux/tp/");
		chemin.append(idSeance);
		chemin.append("/");
		chemin.append(UUID.randomUUID().toString().substring(0, 9));
		chemin.append(nomFichier);
		return chemin.toString();
	}

	protected Utilisateur verifierUtilisateurAvantRendu(Long idUtilisateur, boolean obligatoire) {
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

	protected Seance verifierTpAvantRendu(Long idSeance) {
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
