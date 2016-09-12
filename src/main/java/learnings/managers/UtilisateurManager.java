package learnings.managers;

import learnings.dao.ProjetDao;
import learnings.dao.TravailDao;
import learnings.dao.UtilisateurDao;
import learnings.dao.impl.ProjetDaoImpl;
import learnings.dao.impl.TravailDaoImpl;
import learnings.dao.impl.UtilisateurDaoImpl;
import learnings.exceptions.LearningsException;
import learnings.exceptions.LearningsSecuriteException;
import learnings.model.Travail;
import learnings.model.Utilisateur;
import learnings.pojos.EleveAvecTravauxEtProjet;
import learnings.utils.CsvUtils;
import learnings.utils.FichierUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class UtilisateurManager {
    private static class UtilisateurManagerHolder {
        private static UtilisateurManager instance = new UtilisateurManager();
    }

    public static UtilisateurManager getInstance() {
        return UtilisateurManagerHolder.instance;
    }

    private UtilisateurManager() {
    }

    private static Logger LOGGER = Logger.getLogger(UtilisateurManager.class.getName());

    private UtilisateurDao utilisateurDao = new UtilisateurDaoImpl();
    private TravailDao travailDao = new TravailDaoImpl();
    private MotDePasseManager motDePasseManager = new MotDePasseManager();
	private ProjetDao projetDao = new ProjetDaoImpl();


    public List<Utilisateur> listerUtilisateurs() {
        return utilisateurDao.listerUtilisateurs();
    }

    public List<Utilisateur> listerAutresEleves(Long idUtilisateur) {
        if (idUtilisateur == null) {
            throw new IllegalArgumentException("L'utilisateur est incorrect.");
        }
        return utilisateurDao.listerAutresEleves(idUtilisateur);
    }

    public Utilisateur getUtilisateur(String email) {
        if (email == null || "".equals(email)) {
            throw new IllegalArgumentException("L'identifiant doit être renseigné.");
        }
        return utilisateurDao.getUtilisateur(email);
    }

    public boolean validerMotDePasse(String email, String motDePasseAVerifier) throws LearningsSecuriteException {
        if (email == null || "".equals(email)) {
            throw new IllegalArgumentException("L'identifiant doit être renseigné.");
        }
        if (motDePasseAVerifier == null || "".equals(motDePasseAVerifier)) {
            throw new IllegalArgumentException("Le mot de passe doit être renseigné.");
        }
        String motDePasseHashe = utilisateurDao.getMotDePasseUtilisateurHashe(email);
        if (motDePasseHashe == null) {
            throw new IllegalArgumentException("L'identifiant n'est pas connu.");
        }
        try {
            return motDePasseManager.validerMotDePasse(motDePasseAVerifier, motDePasseHashe);
        } catch (GeneralSecurityException e) {
            throw new LearningsSecuriteException("Problème dans la vérification du mot de passe.", e);
        }
    }

    public void supprimerUtilisateur(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'id de l'utilisateur ne peut pas être null.");
        }
        List<Travail> travaux = travailDao.listerTravauxParUtilisateur(id);
        if (travaux.size() > 0) {
            throw new IllegalArgumentException("Impossible de supprimer un utilisateur avec des travaux rendus.");
        }
        utilisateurDao.supprimerUtilisateur(id);
        LOGGER.info(String.format("Utilisateur|supprimer|id=%d", id));
    }

    public void enleverDroitsAdmin(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'id de l'utilisateur ne peut pas être null.");
        }
        utilisateurDao.modifierRoleAdmin(id, false);
        LOGGER.info(String.format("Utilisateur|enleverDroitsAdmin|id=%d", id));
    }

    public void donnerDroitsAdmin(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'id de l'utilisateur ne peut pas être null.");
        }
        utilisateurDao.modifierRoleAdmin(id, true);
        LOGGER.info(String.format("Utilisateur|donnerDroitsAdmin|id=%d", id));
    }

    /**
     * Réinitialise le mot de passe de l'utilisateur avec pour valeur son email
     */
    public void reinitialiserMotDePasse(Long id) throws LearningsSecuriteException {
        if (id == null) {
            throw new IllegalArgumentException("L'identifiant de l'utilisateur ne peut pas être null.");
        }
        Utilisateur utilisateur = utilisateurDao.getUtilisateur(id);
        if (utilisateur == null) {
            throw new IllegalArgumentException("L'utilisateur n'est pas connu.");
        }
        try {
            String nouveauMotDePasse = motDePasseManager.genererMotDePasse(utilisateur.getEmail());
            utilisateurDao.modifierMotDePasse(id, nouveauMotDePasse);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new LearningsSecuriteException("Problème dans la génération du mot de passe.", e);
        }
        LOGGER.info(String.format("Utilisateur|reinitialiserMotDePasse|id=%d", id));
    }

    public Utilisateur ajouterUtilisateur(Utilisateur utilisateur) throws LearningsSecuriteException {
        if (utilisateur.getEmail() == null || "".equals(utilisateur.getEmail())) {
            throw new IllegalArgumentException("L'adresse email doit être renseigné.");
        }
        Utilisateur utilisateurExistant = this.getUtilisateur(utilisateur.getEmail());
        if (utilisateurExistant != null) {
            throw new IllegalArgumentException("L'identifiant est déjà utilisé.");
        }

        String motDePasse;
        try {
            motDePasse = motDePasseManager.genererMotDePasse(utilisateur.getEmail());
        } catch (GeneralSecurityException e) {
            throw new LearningsSecuriteException("Problème dans la génération du mot de passe.");
        }
        Utilisateur nouvelUtilisateur = utilisateurDao.ajouterUtilisateur(utilisateur, motDePasse);

        LOGGER.info(String.format("Utilisateur|ajouterUtilisateur|id=%d;email=%s", nouvelUtilisateur.getId(), nouvelUtilisateur.getEmail()));
        return nouvelUtilisateur;
    }

    public void modifierMotDePasse(Long id, String motDePasse, String confirmationMotDePasse) throws LearningsSecuriteException {
        if (motDePasse == null || "".equals(motDePasse) || confirmationMotDePasse == null || "".equals(confirmationMotDePasse)) {
            throw new IllegalArgumentException("Les mots de passe doivent être renseignés.");
        }
        if (!motDePasse.equals(confirmationMotDePasse)) {
            throw new IllegalArgumentException("La confirmation du mot de passe ne correspond pas.");
        }

        try {
            String motDePasseHashe = motDePasseManager.genererMotDePasse(motDePasse);
            utilisateurDao.modifierMotDePasse(id, motDePasseHashe);
        } catch (GeneralSecurityException e) {
            throw new LearningsSecuriteException("Problème dans la génération du mot de passe.", e);
        }
        LOGGER.info(String.format("Utilisateur|modifierMotDePasse|id=%d", id));
	}

	public List<EleveAvecTravauxEtProjet> listerElevesAvecTravauxEtProjet() {
		List<Utilisateur> eleves = utilisateurDao.listerEleves();
		List<EleveAvecTravauxEtProjet> listeElevesComplets = new ArrayList<>();
		for(Utilisateur eleve : eleves){
			EleveAvecTravauxEtProjet eleveComplet = new EleveAvecTravauxEtProjet(eleve);

			List<Travail> travauxEleve = travailDao.listerTravauxParUtilisateur(eleve.getId());
			eleveComplet.setProjet(travailDao.getTravailUtilisateurParProjet(projetDao.getLastProjetId(),eleve.getId()));
			Map<Long, Travail> mapTravaux = new HashMap<>();
			for(Travail travail : travauxEleve){
				mapTravaux.put(travail.getEnseignement().getId(), travail);
			}
			eleveComplet.setMapSeanceIdTravail(mapTravaux);
			eleveComplet.setMoyenne(calculMoyenneEleve(eleveComplet));
			listeElevesComplets.add(eleveComplet);
		}
		return listeElevesComplets;
	}

	private BigDecimal calculMoyenneEleve(EleveAvecTravauxEtProjet eleveComplet){
		BigDecimal somme = new BigDecimal(0);
		Integer quotient = 0;
		for(Map.Entry<Long, Travail> travailEntry : eleveComplet.getMapSeanceIdTravail().entrySet()){
			BigDecimal noteTravail = travailEntry.getValue().getNote();
			if(noteTravail!=null) {
				somme = somme.add(noteTravail);
				quotient++;
			}
		}

		if(eleveComplet.getProjet()!=null && eleveComplet.getProjet().getNote()!=null){
			somme = somme.add(eleveComplet.getProjet().getNote().multiply(new BigDecimal(Travail.COEFF_PROJET)));
			quotient = quotient + Travail.COEFF_PROJET;
		}
		if(quotient>0) {
			return somme.divide(new BigDecimal(quotient), 2, RoundingMode.HALF_EVEN);
		}else{
			return null;
		}
    }

    public void importerUtilisateurs(InputStream utilisateursCsvInputStream) throws LearningsException, LearningsSecuriteException {
        List<String> utilisateursCsv = FichierUtils.getLignes(utilisateursCsvInputStream);

        List<Utilisateur> utilisateursACreer = CsvUtils.parserCsvVersUtilisateurs(utilisateursCsv);

        for (Utilisateur utilisateur : utilisateursACreer) {
            this.ajouterUtilisateur(utilisateur);
        }
    }
}
