package learnings.dao.impl;

import learnings.AbstractDaoTestCase;
import learnings.dao.RessourceDao;
import learnings.model.Projet;
import learnings.model.Ressource;
import learnings.model.Seance;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class RessourceDaoTestCase extends AbstractDaoTestCase {
    private RessourceDao ressourceDao = new RessourceDaoImpl();

    @Before
    public void init() throws Exception {
        super.purgeBaseDeDonnees();


        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()
        ) {
            stmt.executeUpdate("INSERT INTO `projettransversal`(`id`,`titre`,`description`,`datelimiterendulot1`, `datelimiterendulot2`) VALUES(1,'Projet','Projet','2014-08-26 10:00', '2014-09-26 10:00')");
            stmt.executeUpdate("INSERT INTO `seance`(`id`,`titre`,`description`,`date`, `type`) VALUES(1,'cours1','cours de debuggage','2014-08-26', 'COURS')");
            stmt.executeUpdate("INSERT INTO `ressource`(`id`,`titre`,`chemin`,`seance_id`) VALUES(1,'ressource1','chemin ressource de cours 1',1)");
            stmt.executeUpdate("INSERT INTO `ressource`(`id`,`titre`,`chemin`,`seance_id`) VALUES(2,'ressource2','chemin ressource de cours 2',1)");
            stmt.executeUpdate("INSERT INTO `ressource`(`id`,`titre`,`chemin`,`seance_id`) VALUES(3,'ressource3','ressource de tp',1)");
            stmt.executeUpdate("INSERT INTO `ressource`(`id`,`titre`,`chemin`,`projettransversal_id`) VALUES(4,'ressourceProjet','ressource de projet',1)");
        }
    }

    @Test
    public void shouldListerRessourcesSeance() {
        // WHEN
        List<Ressource> listeRessources = ressourceDao.getRessources(new Seance(1L, "titre", "desc", new Date()));
        // THEN
        assertThat(listeRessources).hasSize(3);
        assertThat(listeRessources).extracting("id", "titre", "chemin", "enseignement.titre").containsOnly(
                tuple(1L, "ressource1", "chemin ressource de cours 1", "titre"),
                tuple(2L, "ressource2", "chemin ressource de cours 2", "titre"),
                tuple(3L, "ressource3", "ressource de tp", "titre")
        );
    }

    @Test
    public void shouldListerRessourcesProjet() {
        // WHEN
        List<Ressource> listeRessources = ressourceDao.getRessources(new Projet(1L, "projet", null, null, null));
        // THEN
        assertThat(listeRessources).hasSize(1);
        assertThat(listeRessources).extracting("id", "titre", "chemin", "enseignement.titre").containsOnly(
                tuple(4L, "ressourceProjet", "ressource de projet", "projet")
        );
    }

    @Test
    public void shouldAjouterRessource() throws Exception {
        // GIVEN
        Ressource ressource = new Ressource(null, "monTitre", "/chemin/monFichier.zip", new Seance(1L, null, null, null));
        // WHEN
        Ressource ressourceCreee = ressourceDao.ajouterRessource(ressource);
        // THEN
        assertThat(ressourceCreee).isNotNull();
        assertThat(ressourceCreee.getId()).isNotNull();

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM ressource WHERE id=?")
        ) {
            stmt.setLong(1, ressourceCreee.getId());
            try (ResultSet results = stmt.executeQuery()) {
                assertThat(results.next()).isTrue();
                assertThat(results.getLong("id")).isEqualTo(ressourceCreee.getId());
                assertThat(results.getString("titre")).isEqualTo("monTitre");
                assertThat(results.getString("chemin")).isEqualTo("/chemin/monFichier.zip");
                assertThat(results.getLong("seance_id")).isEqualTo(1L);
                assertThat(results.getString("projettransversal_id")).isNull();
            }
        }
    }

    @Test
    public void shouldGetRessource() {
        // WHEN
        Ressource ressource = ressourceDao.getRessource(1L);
        // THEN
        assertThat(ressource.getId()).isEqualTo(1L);
        assertThat(ressource.getTitre()).isEqualTo("ressource1");
        assertThat(ressource.getChemin()).isEqualTo("chemin ressource de cours 1");
        assertThat(ressource.getEnseignement().getId()).isEqualTo(1L);
        assertThat(ressource.getEnseignement().getDescription()).isEqualTo("cours de debuggage");
        assertThat(((Seance) ressource.getEnseignement()).getDate()).isEqualToIgnoringMillis(getDate(2014, Calendar.AUGUST, 26));
    }

    @Test
    public void shouldSupprimerRessource() throws Exception {
        // WHEN
        ressourceDao.supprimerRessource(1L);
        // THEN
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet results = stmt.executeQuery("SELECT * FROM ressource WHERE id=1")
        ) {
            assertThat(results.next()).isFalse();
        }
    }
}
