package learnings.managers;

import learnings.dao.RessourceDao;
import learnings.dao.SeanceDao;
import learnings.dao.TravailDao;
import learnings.dao.impl.RessourceDaoImpl;
import learnings.dao.impl.SeanceDaoImpl;
import learnings.dao.impl.TravailDaoImpl;
import learnings.model.Seance;
import learnings.model.Travail;
import learnings.pojos.TpAvecTravail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SeanceManager {
    private static class SeanceManagerHolder {
        private static SeanceManager instance = new SeanceManager();
    }

    public static SeanceManager getInstance() {
        return SeanceManagerHolder.instance;
    }

    public SeanceManager() {
    }

    private SeanceDao seanceDao = new SeanceDaoImpl();
    private RessourceDao ressourceDao = new RessourceDaoImpl();
    private TravailDao travailDao = new TravailDaoImpl();


    public List<Seance> listerSeances() {
        List<Seance> listeCours = seanceDao.listerSeances();
        for (Seance seanceCourante : listeCours) {
            seanceCourante.setRessources(ressourceDao.getRessources(seanceCourante));
        }
        return listeCours;
    }

    public List<Seance> listerSeancesNotees() {
        return seanceDao.listerSeancesNotees();
    }

    public List<TpAvecTravail> listerTPRenduAccessible(Long idUtilisateur) {
        if (idUtilisateur == null) {
            throw new IllegalArgumentException("L'utlisateur ne peut pas être null.");
        }
        Date aujourdhui = new Date();
        List<Seance> listeTps = seanceDao.listerTPNotesParDateRendu(aujourdhui);
        List<TpAvecTravail> listeTpsAvecTravaux = new ArrayList<>();
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

    public Seance getSeanceAvecRessources(Long idSeance) {
        if (idSeance == null) {
            throw new IllegalArgumentException("L'identifiant de la séance est incorrect.");
        }
        Seance seance = seanceDao.getSeance(idSeance);
        if (seance == null) {
            throw new IllegalArgumentException("L'identifiant de la séance est inconnu.");
        }

        seance.setRessources(ressourceDao.getRessources(seance));

        return seance;
    }

    public void ajouterSeance(Seance seance) {
        if (seance == null) {
            throw new IllegalArgumentException("La séance est null.");
        }
        if (seance.getTitre() == null || "".equals(seance.getTitre())) {
            throw new IllegalArgumentException("Le titre de la séance doit être renseigné.");
        }
        if (seance.getDate() == null) {
            throw new IllegalArgumentException("La date de la séance doit être renseignée.");
        }
        if (seance.getType() == null) {
            throw new IllegalArgumentException("Le type de la séance doit être renseigné.");
        }

        seanceDao.ajouterSeance(seance);
    }

    public void modifierSeance(Seance seance) {
        if (seance == null) {
            throw new IllegalArgumentException("La séance est null.");
        }
        if (seance.getId() == null) {
            throw new IllegalArgumentException("L'identifiant de la séance doit être renseigné.");
        }
        if (seance.getTitre() == null || "".equals(seance.getTitre())) {
            throw new IllegalArgumentException("Le titre de la séance doit être renseigné.");
        }
        if (seance.getDate() == null) {
            throw new IllegalArgumentException("La date de la séance doit être renseignée.");
        }
        if (seance.getType() == null) {
            throw new IllegalArgumentException("Le type de la séance doit être renseigné.");
        }

        seanceDao.modifierSeance(seance);
    }
}
