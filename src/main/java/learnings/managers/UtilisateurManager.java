package learnings.managers;

import learnings.dao.NoteDao;
import learnings.dao.ProjetDao;
import learnings.dao.RenduProjetDao;
import learnings.dao.RenduTpDao;
import learnings.dao.UtilisateurDao;
import learnings.dao.impl.NoteDaoImpl;
import learnings.dao.impl.ProjetDaoImpl;
import learnings.dao.impl.RenduProjetDaoImpl;
import learnings.dao.impl.RenduTpDaoImpl;
import learnings.dao.impl.UtilisateurDaoImpl;
import learnings.exceptions.LearningsException;
import learnings.exceptions.LearningsSecuriteException;
import learnings.model.Note;
import learnings.model.Projet;
import learnings.model.RenduProjet;
import learnings.model.RenduTp;
import learnings.model.Utilisateur;
import learnings.pojos.EleveAvecNotes;
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
import java.util.stream.Collectors;

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
    private RenduTpDao renduTpDao = new RenduTpDaoImpl();
    private RenduProjetDao renduProjetDao = new RenduProjetDaoImpl();
    private MotDePasseManager motDePasseManager = new MotDePasseManager();
	private ProjetDao projetDao = new ProjetDaoImpl();
    private NoteDao noteDao = new NoteDaoImpl();


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
        return motDePasseManager.validerMotDePasse(motDePasseAVerifier, motDePasseHashe);
    }

    public void supprimerUtilisateur(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'id de l'utilisateur ne peut pas être null.");
        }
        List<RenduTp> rendusTp = renduTpDao.listerRendusParUtilisateur(id);
        List<RenduProjet> rendusProjet = renduProjetDao.listerRendusParUtilisateur(id);
            if (rendusTp.size() > 0 || rendusProjet.size() > 0) {
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
        String nouveauMotDePasse = motDePasseManager.genererMotDePasse(utilisateur.getEmail());
        utilisateurDao.modifierMotDePasse(id, nouveauMotDePasse);
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

        String motDePasse = motDePasseManager.genererMotDePasse(utilisateur.getEmail());

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

        String motDePasseHashe = motDePasseManager.genererMotDePasse(motDePasse);
        utilisateurDao.modifierMotDePasse(id, motDePasseHashe);
        LOGGER.info(String.format("Utilisateur|modifierMotDePasse|id=%d", id));
	}

	public List<EleveAvecNotes> listerElevesAvecNotes() {
		List<Utilisateur> eleves = utilisateurDao.listerEleves();
		List<EleveAvecNotes> listeElevesComplets = new ArrayList<>();
		for(Utilisateur eleve : eleves){
            EleveAvecNotes eleveComplet = recupereNotesEleve(eleve);
			listeElevesComplets.add(eleveComplet);
		}
		return listeElevesComplets;
	}

    private EleveAvecNotes recupereNotesEleve(Utilisateur eleve) {
        EleveAvecNotes eleveComplet = new EleveAvecNotes(eleve);

        List<Note> notesEleve = noteDao.listerNotesParUtilisateur(eleve.getId());
        Map<Long, Note> notesSeance = new HashMap<>();
        for (Note note : notesEleve) {
            if (note.getEnseignement() instanceof Projet) {
                eleveComplet.setNoteProjet(note);
            } else {
                notesSeance.put(note.getEnseignement().getId(), note);
            }
        }

        eleveComplet.setMapSeanceNote(notesSeance);
        eleveComplet.setMoyenne(calculMoyenneEleve(eleveComplet));
        return eleveComplet;
    }

    public EleveAvecNotes getEleveAvecNotes(Long idEleve) {
        Utilisateur eleve = utilisateurDao.getUtilisateur(idEleve);
        if (eleve == null) {
            throw new IllegalArgumentException("L'utilisateur n'a pas été trouvé.");
        }
        return recupereNotesEleve(eleve);
    }

	private BigDecimal calculMoyenneEleve(EleveAvecNotes eleveComplet){
		BigDecimal somme = new BigDecimal(0);
		Integer quotient = 0;
        for (Note note : eleveComplet.getMapSeanceNote().values()) {
            somme = somme.add(note.getValeur());
            quotient++;
        }

		if(eleveComplet.getNoteProjet()!=null ){
			somme = somme.add(eleveComplet.getNoteProjet().getValeur().multiply(new BigDecimal(Note.COEFF_PROJET)));
			quotient += Note.COEFF_PROJET;
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

    public String listerEmailsElevesPourEnvoi() {
        List<Utilisateur> eleves = utilisateurDao.listerEleves();
        return eleves.stream().map(Utilisateur::getEmail).collect(Collectors.joining(";"));
    }
}
