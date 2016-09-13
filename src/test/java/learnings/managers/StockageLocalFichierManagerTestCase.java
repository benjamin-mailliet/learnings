package learnings.managers;

import learnings.exceptions.LearningsException;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class StockageLocalFichierManagerTestCase {

    private FichierManager fichierManager = new StockageLocalFichierManagerImpl();
    private Path fichierExistant = Paths.get("C:/HEI/learningsdatatest/existant.txt");

    @Before
    public void initFileSystem() throws Exception {
        Files.deleteIfExists(Paths.get("C:/HEI/learningsdatatest/test/test.txt"));

        if (Files.notExists(fichierExistant)) {
            Files.createDirectories(fichierExistant.getParent());
            Files.createFile(fichierExistant);
        }
        String contenuFichiertExistant = "Première Ligne existante\nDeuxième Ligne existante";
        Files.copy(new ByteArrayInputStream(contenuFichiertExistant.getBytes(StandardCharsets.UTF_8)), fichierExistant, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    public void shouldAjouterFichier() throws Exception {
        // GIVEN
        String data = "Première Ligne\nDeuxième Ligne";
        InputStream dataInputStream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        // WHEN
        fichierManager.ajouterFichier("test/test.txt", dataInputStream);
        // THEN
        Path cheminFichierAjoute = Paths.get("C:/HEI/learningsdatatest/test/test.txt");
        assertThat(Files.exists(cheminFichierAjoute)).isTrue();
        List<String> fichierAjouteContenu = Files.readAllLines(cheminFichierAjoute, StandardCharsets.UTF_8);
        assertThat(fichierAjouteContenu).hasSize(2);
        assertThat(fichierAjouteContenu).containsExactly("Première Ligne", "Deuxième Ligne");
    }

    @Test
    public void shouldNotAjouterFichierExistant() throws Exception {
        // GIVEN
        String data = "Première Ligne\nDeuxième Ligne";
        InputStream dataInputStream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        // WHEN
        try {
            fichierManager.ajouterFichier("existant.txt", dataInputStream);
            fail("exception attendue");
        }
        // THEN
        catch (LearningsException e) {
            assertThat(e.getMessage()).isEqualTo("Le fichier C:\\HEI\\learningsdatatest\\existant.txt existe déjà");
        }
    }

    @Test
    public void shouldGetFichier() throws Exception {
        // WHEN
        InputStream dataInputStream = fichierManager.getFichier("existant.txt");
        // THEN
        assertThat(dataInputStream).isNotNull();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(dataInputStream, StandardCharsets.UTF_8))) {
            assertThat(reader.readLine()).isEqualTo("Première Ligne existante");
            assertThat(reader.readLine()).isEqualTo("Deuxième Ligne existante");
            assertThat(reader.readLine()).isNull();
        }
    }

    @Test
    public void shouldNotGetFichierInexistant() throws Exception {
        // WHEN
        try {
            InputStream dataInputStream = fichierManager.getFichier("non_existant.txt");
            fail("exception attendue");
        }
        // THEN
        catch (LearningsException e) {
            assertThat(e.getMessage()).isEqualTo("Le fichier demandé n'existe pas.");
        }
    }

    @Test
    public void shouldSupprimerFichier() throws Exception{
        // WHEN
        fichierManager.supprimerFichier("existant.txt");
        // THEN
        assertThat(Files.notExists(fichierExistant)).isTrue();
    }

    @Test
    public void shouldNotSupprimerFichierInexistant() throws Exception{
        // WHEN
        try {
            fichierManager.supprimerFichier("non_existant.txt");
            fail("exception attendue");
        }
        // THEN
        catch (LearningsException e) {
            assertThat(e.getMessage()).isEqualTo("Le fichier demandé n'existe pas.");
        }
    }
}
