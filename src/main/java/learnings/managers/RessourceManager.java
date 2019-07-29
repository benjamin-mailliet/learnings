package learnings.managers;

import learnings.dao.RessourceDao;
import learnings.dao.SeanceDao;
import learnings.dao.impl.RessourceDaoImpl;
import learnings.dao.impl.SeanceDaoImpl;
import learnings.enums.RessourceCategorie;
import learnings.enums.RessourceFormat;
import learnings.exceptions.LearningAccessException;
import learnings.exceptions.LearningsException;
import learnings.model.Ressource;
import learnings.model.Seance;
import learnings.pojos.FichierComplet;
import learnings.pojos.RessourceHtml;
import learnings.utils.FichierUtils;
import learnings.utils.MarkdownUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

    public void ajouterRessource(Long idSeance, String titre, RessourceCategorie categorie, RessourceFormat format, String lien, String nomFichier, InputStream fichier) throws LearningsException {
        if (idSeance == null) {
            throw new IllegalArgumentException("L'idenfiant de la séance est null.");
        }
        Seance seance = seanceDao.getSeance(idSeance);
        if (seance == null) {
            throw new IllegalArgumentException("La séance est inconnue.");
        }
        if (categorie == null) {
            throw new IllegalArgumentException("La catégorie est incorrecte.");
        }
        if (lien == null && format == null) {
            throw new IllegalArgumentException("Le format du fichier est incorrect.");
        }

        String chemin;
        if (lien != null && !"".equals(lien)) {
            if (Ressource.LIEN_PATTERN.matcher(lien).matches()) {
                chemin = lien;
                format = RessourceFormat.LIEN;
            } else {
                throw new IllegalArgumentException("Le format du lien est incorrect.");
            }
        } else {
            chemin = this.genererCheminRessource(nomFichier);
            try {
                fichierManager.ajouterFichier(chemin, fichier);
            } catch (LearningsException e) {
                throw new LearningsException("Problème à l'enregistrement de la ressource.", e);
            }
        }

        Ressource ressource = new Ressource(null, titre, chemin, seance, categorie, format);
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
        if (ressource.getSeance() != null) {
            if (ressource.getSeance().getDate().after(new Date())) {
                throw new LearningAccessException();
            }
        }
        FichierComplet fichier = new FichierComplet();
        fichier.setNom(FichierUtils.extraireNomFichier(ressource.getChemin()));
        fichier.setDonnees(fichierManager.getFichier(ressource.getChemin()));
        return fichier;
    }

    public RessourceHtml getRessourceMarkdownHtmlEleve(Long idRessource) throws LearningAccessException, LearningsException {
        Ressource ressource = ressourceDao.getRessource(idRessource);
        if (ressource == null) {
            throw new IllegalArgumentException("La ressource n'existe pas.");
        }
        if (!RessourceFormat.MARKDOWN.equals(ressource.getFormat())) {
            throw new IllegalArgumentException("La ressource n'est pas un fichier markdown.");
        }
        if (ressource.getSeance() != null) {
            if (ressource.getSeance().getDate().after(new Date())) {
                throw new LearningAccessException("La ressource n'est pas accessible.");
            }
        }

        try (InputStream htmlIS = fichierManager.getFichier(ressource.getChemin());
             InputStreamReader htmlReader = new InputStreamReader(htmlIS, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(htmlReader)) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
                result.append("\n");
            }
            return new RessourceHtml(ressource.getId(), ressource.getSeance().getTitre(), MarkdownUtils.getHtml(result.toString()));
        } catch (IOException e) {
            throw new LearningsException("Erreur à la lecture du fichier demandé.");
        }
    }

    public void supprimerRessource(Long idRessource) throws LearningsException {
        if (idRessource == null) {
            throw new IllegalArgumentException("L'idenfiant de la ressource est null.");
        }
        Ressource ressource = ressourceDao.getRessource(idRessource);
        if (ressource != null && !ressource.isLien()) {
            try {
                fichierManager.supprimerFichier(ressource.getChemin());
            } catch (LearningsException e) {
                throw new LearningsException("Problème à la suppression de la ressource.", e);
            }
        }
        ressourceDao.supprimerRessource(idRessource);
    }

    protected String genererCheminRessource(String nomFichier) {
        return "ressources/seances/" +
                FichierUtils.rendreUniqueNomFichier(nomFichier);
    }
}
