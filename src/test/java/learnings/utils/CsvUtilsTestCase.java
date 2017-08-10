package learnings.utils;

import learnings.enums.Groupe;
import learnings.exceptions.LearningsException;
import learnings.model.RenduProjet;
import learnings.model.RenduTp;
import learnings.model.Seance;
import learnings.model.Travail;
import learnings.model.Utilisateur;
import learnings.pojos.EleveAvecTravauxEtProjet;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    public void shouldWriteLignesElevesNotes() throws IOException {
        //GIVEN
        List<EleveAvecTravauxEtProjet> eleves = getElevesWithTravauxAndProjet();
        List<Seance> seances = getSeances();

        //WHEN
        StringWriter writer = new StringWriter();
        CsvUtils.creerCSVElevesNotes(writer, eleves, seances);

        //THEN
        assertThat(writer.toString()).isEqualTo("Elève;TP 1;TP 2;Projet;Moyenne\n" +
                "1 Eleve;11;12;13;10\n" +
                "2 Eleve;14;15;;12\n" +
                "3 Eleve;;18;19;13\n");
    }

    private List<EleveAvecTravauxEtProjet> getElevesWithTravauxAndProjet() {
        EleveAvecTravauxEtProjet eleve1 = new EleveAvecTravauxEtProjet();
        eleve1.setNom("1");
        eleve1.setPrenom("Eleve");
        eleve1.setMoyenne(new BigDecimal(10));
        eleve1.setProjet(Collections.singletonList(new RenduProjet(3L, new BigDecimal(13), null, null, null, null, null, null)));
        Map<Long, List<RenduTp>> mapTravaux = new HashMap<>();
        mapTravaux.put(1L, Collections.singletonList(new RenduTp(1L, new BigDecimal(11), null, null, null, null)));
        mapTravaux.put(2L, Collections.singletonList(new RenduTp(2L,  new BigDecimal(12), null, null, null, null)));
        eleve1.setMapSeanceIdTravail(mapTravaux);

        EleveAvecTravauxEtProjet eleve2 = new EleveAvecTravauxEtProjet();
        eleve2.setNom("2");
        eleve2.setPrenom("Eleve");
        eleve2.setMoyenne(new BigDecimal(12));
        Map<Long, List<RenduTp>> mapTravaux2 = new HashMap<>();
        mapTravaux2.put(1L, Collections.singletonList(new RenduTp(4L, new BigDecimal(14), null, null, null, null)));
        mapTravaux2.put(2L, Collections.singletonList(new RenduTp(5L, new BigDecimal(15), null, null, null, null)));
        eleve2.setMapSeanceIdTravail(mapTravaux2);

        EleveAvecTravauxEtProjet eleve3 = new EleveAvecTravauxEtProjet();
        eleve3.setNom("3");
        eleve3.setPrenom("Eleve");
        eleve3.setMoyenne(new BigDecimal(13));
        eleve3.setProjet(Collections.singletonList(new RenduProjet(9L, new BigDecimal(19), null, null, null, null, null, null)));
        Map<Long, List<RenduTp>> mapTravaux3 = new HashMap<>();
        mapTravaux3.put(2L, Collections.singletonList(new RenduTp(8L, new BigDecimal(18), null, null, null, null)));
        eleve3.setMapSeanceIdTravail(mapTravaux3);

        return Arrays.asList(eleve1, eleve3, eleve2);
    }

    private List<Seance> getSeances(){
        Seance seance1 = new Seance(1L, "TP 1", null, null);
        Seance seance2 = new Seance(2L, "TP 2", null, null);
        return Arrays.asList(seance2, seance1);
    }
}
