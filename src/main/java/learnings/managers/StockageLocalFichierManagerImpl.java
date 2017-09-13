package learnings.managers;

import learnings.exceptions.LearningsException;
import learnings.pojos.FichierComplet;
import learnings.utils.FichierUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class StockageLocalFichierManagerImpl implements FichierManager {

    private String repertoirePrincipal;

    public StockageLocalFichierManagerImpl() {
        Properties configProperties = new Properties();
        try (InputStream configFileStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            configProperties.load(configFileStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.repertoirePrincipal = configProperties.getProperty("repertoirePrincipal");
        if (!this.repertoirePrincipal.endsWith("/")) {
            this.repertoirePrincipal += "/";
        }
    }

    @Override
    public void ajouterFichier(String path, InputStream datas) throws LearningsException {
        try {
            Path fichier = Paths.get(repertoirePrincipal, path);
            if (Files.notExists(fichier)) {
                Files.createDirectories(fichier.getParent());
                Files.copy(datas, fichier);
            } else {
                throw new LearningsException(String.format("Le fichier %s existe déjà", fichier.toAbsolutePath()));
            }
        } catch (IOException e) {
            throw new LearningsException("Problème avec la création d'un fichier.", e);
        }
    }

    @Override
    public InputStream getFichier(String path) throws LearningsException {
        try {
            Path fichier = Paths.get(repertoirePrincipal, path);
            if (Files.exists(fichier)) {
                return Files.newInputStream(fichier);
            } else {
                throw new LearningsException("Le fichier demandé n'existe pas.");
            }
        } catch (IOException e) {
            throw new LearningsException("Problème avec la récupération d'un fichier.", e);
        }
    }

    @Override
    public void supprimerFichier(String path) throws LearningsException {
        Path fichier = Paths.get(repertoirePrincipal, path);
        if (Files.exists(fichier)) {
            try {
                Files.delete(fichier);
            } catch (IOException e) {
                throw new LearningsException("Le fichier demandé n'a pas pu être supprimé.");
            }
        } else {
            throw new LearningsException("Le fichier demandé n'existe pas.");
        }
    }

    @Override
    public FichierComplet getFichierComplet(String path) throws LearningsException {
        FichierComplet fichier = new FichierComplet();
        fichier.setNom(FichierUtils.extraireNomFichier(path));
        fichier.setDonnees(this.getFichier(path));
        return fichier;
    }
}
