package learnings.managers;

import learnings.dao.BinomeDao;
import learnings.dao.NoteDao;
import learnings.dao.RenduTpDao;
import learnings.dao.SeanceDao;
import learnings.dao.UtilisateurDao;
import learnings.dao.impl.BinomeDaoImpl;
import learnings.dao.impl.NoteDaoImpl;
import learnings.dao.impl.RenduTpDaoImpl;
import learnings.dao.impl.SeanceDaoImpl;
import learnings.dao.impl.UtilisateurDaoImpl;
import learnings.enums.TypeSeance;
import learnings.exceptions.LearningsException;
import learnings.model.Binome;
import learnings.model.Note;
import learnings.model.RenduTp;
import learnings.model.Seance;
import learnings.model.Travail;
import learnings.model.Utilisateur;
import learnings.pojos.FichierComplet;
import learnings.utils.FichierUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.logging.Logger;

public class RenduTpManager {
    private static class RenduTpManagerHolder {
        private static RenduTpManager instance = new RenduTpManager();
    }

    public static RenduTpManager getInstance() {
        return RenduTpManagerHolder.instance;
    }

    private RenduTpManager() {
    }

    private static final int NOMBRE_OCTETS_IN_MO = 1024 * 1024;

    private static Logger LOGGER = Logger.getLogger(RenduTpManager.class.getName());

    private SeanceDao seanceDao = new SeanceDaoImpl();
    private RenduTpDao renduTpDao = new RenduTpDaoImpl();
    private BinomeDao binomeDao = new BinomeDaoImpl();
    private UtilisateurDao utilisateurDao = new UtilisateurDaoImpl();
    private NoteDao noteDao = new NoteDaoImpl();

    private FichierManager fichierManager = new StockageLocalFichierManagerImpl();


    public void rendreTP(Long idSeance, Long idEleve, String commentaire, String nomFichier, InputStream fichier, Long tailleFichier)
            throws LearningsException {
        this.verifierTpAvantRendu(idSeance);
        Binome binome = binomeDao.getBinome(idSeance, idEleve);
        if (binome == null) {
            throw new IllegalArgumentException("L'élève doit d'abbord créer un binôme.");
        }

        if (tailleFichier / NOMBRE_OCTETS_IN_MO > 10) {
            throw new IllegalArgumentException("Le fichier est trop gros.");
        }

        String chemin = genererCheminTravail(idSeance, nomFichier);

        RenduTp rendu = new RenduTp();
        rendu.setBinome(binome);
        rendu.setDateRendu(LocalDateTime.now());
        rendu.setChemin(chemin);
        rendu.setCommentaire(commentaire);

        ajouterFichier(fichier, rendu);

        renduTpDao.ajouterRenduTp(rendu);

        LOGGER.info(String.format("rendreTP|binome=%d|rendu=%d;%s", binome.getId(), rendu.getId(), nomFichier));
    }

    public void ajouterBinome(Long idSeance, Long idEleve1, Long idEleve2) throws LearningsException  {
       this.checkBinome(idSeance, idEleve1, idEleve2);

        Binome binome = new Binome(null, new Seance(idSeance, null, null, null));
        binome.setEleve1(this.verifierUtilisateur(idEleve1));
        if (idEleve2 != null) {
            binome.setEleve2(this.verifierUtilisateur(idEleve2));
        }

        binomeDao.ajouterBinome(binome);
        LOGGER.info(String.format("ajouterBinome|binome=%d|tp=%d", binome.getId(), idSeance));
    }

    public void enregistrerNoteTp(Long idSeance, Long idEleve, BigDecimal valeur, String commentaire) {
        if (idSeance == null) {
            throw new IllegalArgumentException("L'identifiant de la séance ne peut être null");
        }
        Seance seance = seanceDao.getSeance(idSeance);
        if (seance == null) {
            throw new IllegalArgumentException("La séance n'existe pas.");
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
        note.setSeance(seance);
        note.setValeur(valeur);
        note.setCommentaire(commentaire);
        if(noteDao.getBySeanceAndEleve(idSeance, idEleve) != null) {
            noteDao.modifierNote(note);
        }else{
            noteDao.ajouterNote(note);
        }
        LOGGER.info(String.format("enregistrerNoteTp|idSeance=%d|idEleve=%d", idSeance, idEleve));
    }

    public RenduTp getRenduTp(Long idRenduTp) {
        return renduTpDao.getRendu(idRenduTp);
    }

    public FichierComplet getFichierRenduTp(Long idRenduTp) throws LearningsException {
        RenduTp travail = renduTpDao.getRendu(idRenduTp);
        return fichierManager.getFichierComplet(travail.getChemin());
    }

    protected void checkBinome(Long idTp, Long idEleve1, Long idEleve2) {
        Binome binomeEleve1 = binomeDao.getBinome(idTp, idEleve1);
        if (binomeEleve1 != null) {
            throw new IllegalArgumentException("L'élève est déjà dans un binôme pour ce TP.");
        }
        if (idEleve2 != null) {
            Binome binomeEleve2 = binomeDao.getBinome(idTp, idEleve2);
            if (binomeEleve2 != null) {
                throw new IllegalArgumentException("L'élève est déjà dans un binôme pour ce TP.");
            }
        }
    }

    protected Utilisateur verifierUtilisateur(Long idEleve) {
        Utilisateur eleve = utilisateurDao.getUtilisateur(idEleve);
        if (eleve == null) {
            throw new IllegalArgumentException("L'élève n'existe pas.");
        }
        if (eleve.isAdmin()) {
            throw new IllegalArgumentException("Un administrateur ne peut pas être dans un binôme.");
        }
        return eleve;
    }

    protected void ajouterFichier(InputStream fichier, Travail travail) throws LearningsException {
        try {
            if (fichier != null) {
                fichierManager.ajouterFichier(travail.getChemin(), fichier);
            }
        } catch (LearningsException e) {
            throw new LearningsException("Problème à l'enregistrement du travail.", e);
        }
    }

    protected String genererCheminTravail(Long idSeance, String nomFichier) {
        return "travaux/tp/" + idSeance + "/" + FichierUtils.rendreUniqueNomFichier(nomFichier);
    }

    protected void verifierTpAvantRendu(Long idSeance) {
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
    }
}
