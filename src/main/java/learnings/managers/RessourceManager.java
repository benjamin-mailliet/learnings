package learnings.managers;

import learnings.dao.RessourceDao;
import learnings.dao.SeanceDao;
import learnings.dao.impl.RessourceDaoImpl;
import learnings.dao.impl.SeanceDaoImpl;
import learnings.exceptions.LearningAccessException;
import learnings.exceptions.LearningsException;
import learnings.model.Enseignement;
import learnings.model.Ressource;
import learnings.model.Seance;
import learnings.pojos.FichierComplet;
import learnings.utils.FichierUtils;

import java.io.InputStream;
import java.util.Date;

public class RessourceManager {

    private static class RessourceManagerHolder {
        private static RessourceManager instance = new RessourceManager();
    }

    public static RessourceManager getInstance() {
        return RessourceManagerHolder.instance;
    }

    private RessourceManager() {
    }

    private FichierManager fichierManager = new StockageLocalFichierManagerImpl();
    private SeanceDao seanceDao = new SeanceDaoImpl();
    private RessourceDao ressourceDao = new RessourceDaoImpl();

    public void ajouterRessource(Long idSeance, String titre, String nomFichier, InputStream fichier) throws LearningsException {
        if (idSeance == null) {
            throw new IllegalArgumentException("L'idenfiant de la séance est null.");
        }
        Enseignement enseignement =  seanceDao.getSeance(idSeance);
        if (enseignement == null) {
            throw new IllegalArgumentException("L'enseignement est inconnu.");
        }
        String chemin = this.genererCheminRessource(idSeance, nomFichier);
        try {
            fichierManager.ajouterFichier(chemin, fichier);
        } catch (LearningsException e) {
            throw new LearningsException("Problème à l'enregistrement de la ressource.", e);
        }

        Ressource ressource = new Ressource(null, titre, chemin, enseignement);
        ressourceDao.ajouterRessource(ressource);
    }

    public FichierComplet getFichierRessourceAdmin(Long idRessource) throws LearningsException {
        Ressource ressource = ressourceDao.getRessource(idRessource);
        FichierComplet fichier = new FichierComplet();
        fichier.setNom(FichierUtils.extraireNomFichier(ressource.getChemin()));
        fichier.setDonnees(fichierManager.getFichier(ressource.getChemin()));
        return fichier;
    }

    public FichierComplet getFichierRessourceEleve(Long idRessource) throws LearningsException, LearningAccessException {
        Ressource ressource = ressourceDao.getRessource(idRessource);
        if (ressource.getEnseignement() instanceof Seance) {
            Seance seance = (Seance) ressource.getEnseignement();
            if (seance.getDate().after(new Date())) {
                throw new LearningAccessException();
            }
        }
        FichierComplet fichier = new FichierComplet();
        fichier.setNom(FichierUtils.extraireNomFichier(ressource.getChemin()));
        fichier.setDonnees(fichierManager.getFichier(ressource.getChemin()));
        return fichier;
    }

    public void supprimerRessource(Long idRessource) throws LearningsException {
        if (idRessource == null) {
            throw new IllegalArgumentException("L'idenfiant de la ressource est null.");
        }
        Ressource ressource = ressourceDao.getRessource(idRessource);
        if (ressource != null) {
            try {
                fichierManager.supprimerFichier(ressource.getChemin());
                ressourceDao.supprimerRessource(idRessource);
            } catch (LearningsException e) {
                throw new LearningsException("Problème à la suppression de la ressource.", e);
            }
        }
    }

    protected String genererCheminRessource(Long idSeance, String nomFichier) {
        StringBuilder chemin = new StringBuilder();
        chemin.append("ressources/seances/");
        chemin.append(FichierUtils.rendreUniqueNomFichier(nomFichier));
        return chemin.toString();
    }
}
