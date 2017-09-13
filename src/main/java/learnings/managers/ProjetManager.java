package learnings.managers;

import learnings.dao.ProjetDao;
import learnings.dao.RenduProjetDao;
import learnings.dao.RessourceDao;
import learnings.dao.UtilisateurDao;
import learnings.dao.impl.ProjetDaoImpl;
import learnings.dao.impl.RenduProjetDaoImpl;
import learnings.dao.impl.RessourceDaoImpl;
import learnings.dao.impl.UtilisateurDaoImpl;
import learnings.model.Projet;
import learnings.model.RenduProjet;
import learnings.pojos.ProjetAvecRendus;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjetManager {

    private static String KEY_NB_JOURS_LOT_1 = "nbJoursLot1";
    private static String KEY_NB_JOURS_LOT_2 = "nbJoursLot2";

    private static class ProjetManagerHolder {
        private static ProjetManager instance = new ProjetManager();
    }

    public static ProjetManager getInstance() {
        return ProjetManagerHolder.instance;
    }

    private ProjetManager() {
    }

    private ProjetDao projetDao = new ProjetDaoImpl();
    private RessourceDao ressourceDao = new RessourceDaoImpl();
    private RenduProjetDao renduProjetDao = new RenduProjetDaoImpl();
    private UtilisateurDao utilisateurDao = new UtilisateurDaoImpl();

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

    public ProjetAvecRendus getProjetAvecTravail(Long idUtilisateur) {
        if (idUtilisateur == null) {
            throw new IllegalArgumentException("L'identifiant de l'utilisateur est incorrect.");
        }
        ProjetAvecRendus projetAvecTravail = new ProjetAvecRendus();
        Long lastProjetId = projetDao.getLastProjetId();
        if(lastProjetId!=null) {
            projetAvecTravail.setProjet(projetDao.getProjet(lastProjetId));

            List<RenduProjet> rendus = renduProjetDao.listerRendusUtilisateurParProjet(lastProjetId, idUtilisateur);
            projetAvecTravail.setRendus(rendus.stream().collect(Collectors.groupingBy(RenduProjet::getEleve)));

            Map<String, Integer> mapJoursRestants = getNbJoursRestants(projetAvecTravail);

            projetAvecTravail.setNbJoursRestantsLot1(mapJoursRestants.get(KEY_NB_JOURS_LOT_1));
            projetAvecTravail.setNbJoursRestantsLot2(mapJoursRestants.get(KEY_NB_JOURS_LOT_2));
            return projetAvecTravail;
        }else{
            return null;
        }


    }

    private Map<String, Integer> getNbJoursRestants(
            ProjetAvecRendus projetAvecTravail) {
        Date today = new Date();

        Long millisecondsPerDay = 86400000L;
        Integer nbJoursRestantsLot1 = Math.round((projetAvecTravail.getProjet().getDateLimiteRenduLot1().getTime() - today.getTime()) / millisecondsPerDay);
        Integer nbJoursRestantsLot2 = Math.round((projetAvecTravail.getProjet().getDateLimiteRenduLot2().getTime() - today.getTime()) / millisecondsPerDay);

        HashMap<String, Integer> mapNbJoursRestants = new HashMap<>();

        mapNbJoursRestants.put(KEY_NB_JOURS_LOT_1, nbJoursRestantsLot1 > 0 ? nbJoursRestantsLot1 : 0);
        mapNbJoursRestants.put(KEY_NB_JOURS_LOT_2, nbJoursRestantsLot2 > 0 ? nbJoursRestantsLot2 : 0);
        return mapNbJoursRestants;
    }

    public ProjetAvecRendus getProjetAvecRendus(Long idProjet) {
        if (idProjet == null) {
            throw new IllegalArgumentException("L'identifiant du projet est incorrect.");
        }
        Projet projet = projetDao.getProjet(idProjet);
        if (projet == null) {
            throw new IllegalArgumentException("L'identifiant du projet est inconnu.");
        }
        ProjetAvecRendus projetAvecRendus = new ProjetAvecRendus();
        projetAvecRendus.setProjet(projet);

        List<RenduProjet> rendus = renduProjetDao.listerRendusParProjet(idProjet);
        projetAvecRendus.setRendus(rendus.stream().collect(Collectors.groupingBy(RenduProjet::getEleve)));

        return projetAvecRendus;
    }
}
