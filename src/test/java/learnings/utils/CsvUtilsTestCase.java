package learnings.utils;

import learnings.enums.Groupe;
import learnings.exceptions.LearningsException;
import learnings.model.Utilisateur;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.tuple;

public class CsvUtilsTestCase {

    @Test
    public void shouldParserAdmin() throws Exception {
        assertThat(CsvUtils.parserAdmin(42, "1")).isTrue();
        assertThat(CsvUtils.parserAdmin(42, "true")).isTrue();
        assertThat(CsvUtils.parserAdmin(42, "True")).isTrue();
        assertThat(CsvUtils.parserAdmin(42, "TRUE")).isTrue();
        assertThat(CsvUtils.parserAdmin(42, "OUI")).isTrue();
        assertThat(CsvUtils.parserAdmin(42, "oui")).isTrue();
        assertThat(CsvUtils.parserAdmin(42, "Oui")).isTrue();
        assertThat(CsvUtils.parserAdmin(42, "0")).isFalse();
        assertThat(CsvUtils.parserAdmin(42, "false")).isFalse();
        assertThat(CsvUtils.parserAdmin(42, "False")).isFalse();
        assertThat(CsvUtils.parserAdmin(42, "FALSE")).isFalse();
        assertThat(CsvUtils.parserAdmin(42, "NON")).isFalse();
        assertThat(CsvUtils.parserAdmin(42, "non")).isFalse();
        assertThat(CsvUtils.parserAdmin(42, "Non")).isFalse();
    }

    @Test
    public void shouldNotParserAdmin() throws Exception {
        // WHEN
        try {
            CsvUtils.parserAdmin(42, "");
            fail("exception attendue");
        }
        // THEN
        catch (LearningsException e) {
            assertThat(e.getMessage()).isEqualTo("Problème avec le flag admin à la ligne 42 : ");
        }
    }

    @Test
    public void shouldParserGroupeComplet() throws Exception {
        // WHEN
        Groupe groupe = CsvUtils.parserGroupe(42, "GROUPE_1");
        // THEN
        assertThat(groupe).isEqualTo(Groupe.GROUPE_1);
    }

    @Test
    public void shouldParserGroupeNumeroSeulement() throws Exception {
        // WHEN
        Groupe groupe = CsvUtils.parserGroupe(42, "1");
        // THEN
        assertThat(groupe).isEqualTo(Groupe.GROUPE_1);
    }

    @Test
    public void shouldParserGroupeVide() throws Exception {
        // WHEN
        Groupe groupe = CsvUtils.parserGroupe(42, "");
        // THEN
        assertThat(groupe).isEqualTo(null);
    }

    @Test
    public void shouldNotParserGroupe() throws Exception {
        // WHEN
        try {
            CsvUtils.parserGroupe(42, "bla");
            fail("exception attendue");
        }
        // THEN
        catch (LearningsException e) {
            assertThat(e.getMessage()).isEqualTo("Problème avec le groupe à la ligne 42 : bla");
        }
    }

    @Test
    public void shouldParserCsvVersUtilisateursAvecEntete() throws Exception {
        // GIVEN
        List<String> utilisateursCsv = new ArrayList<>();
        utilisateursCsv.add("NOM;PRENOM;EMAIL;GROUPE;ADMIN");
        utilisateursCsv.add("Nom1;Prenom1;prenom1.nom1@test-learnings-devwebhei.com;1;0");
        utilisateursCsv.add("Nom2;Prenom2;prenom2.nom2@test-learnings-devwebhei.com;GROUPE_2;1");
        utilisateursCsv.add("Nom3;Prenom3;prenom3.nom3@test-learnings-devwebhei.com;;Oui");
        // WHEN
        List<Utilisateur> utilisateurs = CsvUtils.parserCsvVersUtilisateurs(utilisateursCsv);
        // THEN
        assertThat(utilisateurs).hasSize(3);
        assertThat(utilisateurs).extracting("nom", "prenom", "email", "groupe", "admin").containsOnly(
                tuple("Nom1", "Prenom1", "prenom1.nom1@test-learnings-devwebhei.com", Groupe.GROUPE_1, false),
                tuple("Nom2", "Prenom2", "prenom2.nom2@test-learnings-devwebhei.com", Groupe.GROUPE_2, true),
                tuple("Nom3", "Prenom3", "prenom3.nom3@test-learnings-devwebhei.com", null, true)
        );
    }

    @Test
    public void shouldNotParserCsvVersUtilisateursAvecChampsVides() throws Exception {
        // GIVEN
        List<String> utilisateursCsv = new ArrayList<>();
        utilisateursCsv.add("Nom1;Prenom1;prenom1.nom1@test-learnings-devwebhei.com;1;0");
        utilisateursCsv.add("Nom2;;prenom2.nom2@test-learnings-devwebhei.com;GROUPE_2;1");
        utilisateursCsv.add("Nom3;Prenom3;prenom3.nom3@test-learnings-devwebhei.com;;Oui");
        // WHEN
        try {
            CsvUtils.parserCsvVersUtilisateurs(utilisateursCsv);
            fail("exception attendue");
        }
        // THEN
        catch (LearningsException e) {
            assertThat(e.getMessage()).isEqualTo("Un champ obligatoire est vide à la ligne 2");
        }
    }

    @Test
    public void shouldNotParserCsvVersUtilisateursAvecMauvaisNombreChamps() throws Exception {
        // GIVEN
        List<String> utilisateursCsv = new ArrayList<>();
        utilisateursCsv.add("Nom1;Prenom1;prenom1.nom1@test-learnings-devwebhei.com;1;0");
        utilisateursCsv.add("Nom2;Prenom2;prenom2.nom2@test-learnings-devwebhei.com;GROUPE_2;1");
        utilisateursCsv.add("Nom3;Prenom3;prenom3.nom3@test-learnings-devwebhei.com");
        // WHEN
        try {
            CsvUtils.parserCsvVersUtilisateurs(utilisateursCsv);
            fail("exception attendue");
        }
        // THEN
        catch (LearningsException e) {
            assertThat(e.getMessage()).isEqualTo("Le nombre de champs est incorrect à la ligne 3");
        }
    }
}
