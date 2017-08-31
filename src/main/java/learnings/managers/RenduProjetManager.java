package learnings.managers;

import learnings.dao.NoteDao;
import learnings.dao.ProjetDao;
import learnings.dao.RenduProjetDao;
import learnings.dao.UtilisateurDao;
import learnings.dao.impl.NoteDaoImpl;
import learnings.dao.impl.ProjetDaoImpl;
import learnings.dao.impl.RenduProjetDaoImpl;
import learnings.dao.impl.UtilisateurDaoImpl;
import learnings.exceptions.LearningsException;
import learnings.model.Note;
import learnings.model.Projet;
import learnings.model.RenduProjet;
import learnings.model.Travail;
import learnings.model.Utilisateur;
import learnings.pojos.FichierComplet;
import learnings.utils.FichierUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.logging.Logger;

public class RenduProjetManager {
    private static class RenduProjetManagerHolder {
        private static RenduProjetManager instance = new RenduProjetManager();
    }

    public static RenduProjetManager getInstance() {
        return RenduProjetManagerHolder.instance;
    }

    private RenduProjetManager() {
    }

    private static final int NOMBRE_OCTETS_IN_MO = 1024 * 1024;

    private static Logger LOGGER = Logger.getLogger(RenduProjetManager.class.getName());

    private ProjetDao projetDao = new ProjetDaoImpl();
    private UtilisateurDao utilisateurDao = new UtilisateurDaoImpl();
    private RenduProjetDao renduProjetDao = new RenduProjetDaoImpl();
    private NoteDao noteDao = new NoteDaoImpl();

    private FichierManager fichierManager = new StockageLocalFichierManagerImpl();

    public void rendreProjetWithRepo(Long projetId, Long utilisateurId, String commentaire, String urlRepository) throws LearningsException {
        Projet projet = this.verifierProjetAvantRendu(projetId);
        Utilisateur eleve = this.verifierUtilisateurAvantRendu(utilisateurId);

        RenduProjet renduProjet = initRenduProjet(commentaire, projet, eleve, null, urlRepository);
        renduProjetDao.ajouterRendu(renduProjet);

        LOGGER.info(String.format("rendreProjet|utilisateur=%d|urlRepository=%s", utilisateurId, urlRepository));
    }

    public void rendreProjetWithFichier(Long projetId, Long utilisateurId, String commentaire, String nomFichier, InputStream fichier, long tailleFichier) throws LearningsException {
        Projet projet = this.verifierProjetAvantRendu(projetId);
        Utilisateur eleve = this.verifierUtilisateurAvantRendu(utilisateurId);

        if (tailleFichier / NOMBRE_OCTETS_IN_MO > 10) {
            throw new IllegalArgumentException("Le fichier est trop gros.");
        }

        String chemin = genererCheminTravail(projetId, nomFichier);

        RenduProjet travail = initRenduProjet(commentaire, projet, eleve, chemin, null);
        ajouterFichier(fichier, travail);
        renduProjetDao.ajouterRendu(travail);

        LOGGER.info(String.format("rendreProjet|utilisateur=%d|fichier=%s", utilisateurId, nomFichier));
    }

    public void enregistrerNoteProjet(Long idProjet, Long idEleve, BigDecimal valeur, String commentaire) {
        if (idProjet == null) {
            throw new IllegalArgumentException("L'identifiant du projet ne peut être null");
        }
        Projet projet = projetDao.getProjet(idProjet);
        if (projet == null) {
            throw new IllegalArgumentException("Le projet n'existe pas.");
        }
        if (idEleve == null) {
            throw new IllegalArgumentException("L'identifiant de l'élève ne peut être null");
        }
        Utilisateur eleve = utilisateurDao.getUtilisateur(idEleve);
        if (eleve == null) {
            throw new IllegalArgumentException("L'élève n'existe pas.");
        }
        Note note = new Note();
        note.setEleve(eleve);
        note.setEnseignement(projet);
        note.setValeur(valeur);
        note.setCommentaire(commentaire);
        noteDao.ajouterNote(note);
        LOGGER.info(String.format("enregistrerNoteProjet|idProjet=%d|idEleve=%d", idProjet, idEleve));
    }

    public RenduProjet getRenduProjet(Long idRenduProjet) {
        return renduProjetDao.getRenduProjet(idRenduProjet);
    }

    public FichierComplet getFichierRenduProjet(Long idRenduProjet) throws LearningsException {
        RenduProjet travail = renduProjetDao.getRenduProjet(idRenduProjet);
        return fichierManager.getFichierComplet(travail.getChemin());
    }


    protected void ajouterFichier(InputStream fichier, Travail travail) throws LearningsException {
        try {
            if (fichier != null) {
                fichierManager.ajouterFichier(travail.getChemin(), fichier);
            }
        } catch (LearningsException e) {
            throw new LearningsException("Problème à l'enregistrement du rendu de projet.", e);
        }
    }

    protected String genererCheminTravail(Long idSeance, String nomFichier) {
        return "travaux/projet/" + idSeance + "/" + FichierUtils.rendreUniqueNomFichier(nomFichier);
    }

    protected Utilisateur verifierUtilisateurAvantRendu(Long idUtilisateur) {
        if (idUtilisateur == null) {
            throw new IllegalArgumentException("Un utilisateur obligatoire n'est pas renseigné.");
        }
        Utilisateur utilisateur = utilisateurDao.getUtilisateur(idUtilisateur);
        if (utilisateur == null) {
            throw new IllegalArgumentException("Un utilisateur est inconnu.");
        }
        if (utilisateur.isAdmin()) {
            throw new IllegalArgumentException("Un administrateur ne peut pas rendre de TP.");
        }
        return utilisateur;
    }


    protected Projet verifierProjetAvantRendu(Long idProjet) {
        if (idProjet == null) {
            throw new IllegalArgumentException("L'identifiant du projet est incorrect");
        }
        Projet projet = projetDao.getProjet(idProjet);
        if (projet == null) {
            throw new IllegalArgumentException("L'identifiant du projet est incorrect");
        }
        return projet;
    }

    private RenduProjet initRenduProjet(String commentaire, Projet projet, Utilisateur eleve, String chemin, String urlRepository) {
        RenduProjet travail = new RenduProjet();
        travail.setProjet(projet);
        travail.setDateRendu(LocalDateTime.now());
        travail.setChemin(chemin);
        travail.setCommentaire(commentaire);
        travail.setUrlRepository(urlRepository);
        travail.setEleve(eleve);
        return travail;
	}

}
