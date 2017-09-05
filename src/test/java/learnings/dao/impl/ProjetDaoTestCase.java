package learnings.dao.impl;

import learnings.AbstractDaoTestCase;
import learnings.dao.ProjetDao;
import learnings.model.Projet;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class ProjetDaoTestCase extends AbstractDaoTestCase {
    private ProjetDao projetDao = new ProjetDaoImpl();

    @Before
    public void init() throws Exception {
        super.purgeBaseDeDonnees();

        try (Connection connection = this.getConnection();
             Statement stmt = connection.createStatement()
        ) {
            stmt.executeUpdate("INSERT INTO `projet`(`id`,`titre`,`description`,`datelimiterendulot1`,`datelimiterendulot2`) VALUES(1,'projet1','description 1','2014-09-16 18:00:00','2014-10-16 18:00:00')");
            stmt.executeUpdate("INSERT INTO `projet`(`id`,`titre`,`description`,`datelimiterendulot1`,`datelimiterendulot2`) VALUES(2,'projet2','description 2','2014-09-16 18:00:00','2014-10-10 18:00:00')");
            stmt.executeUpdate("INSERT INTO `projet`(`id`,`titre`,`description`,`datelimiterendulot1`,`datelimiterendulot2`) VALUES(3,'projet3','description 3','2014-09-17 18:00:00','2014-09-26 18:00:00')");
            stmt.executeUpdate("INSERT INTO `projet`(`id`,`titre`,`description`,`datelimiterendulot1`,`datelimiterendulot2`) VALUES(4,'projet4','description 4','2014-09-19 18:00:00','2014-10-20 18:00:00')");
        }
    }

    @Test
    public void shouldListerProjets() {
        // WHEN
        List<Projet> listeProjets = projetDao.listerProjets();
        // THEN
        assertThat(listeProjets).hasSize(4);
        assertThat(listeProjets).extracting("id", "titre").containsExactly(
                tuple(2L, "projet2"),
                tuple(1L, "projet1"),
                tuple(3L, "projet3"),
                tuple(4L, "projet4")
        );

    }

    @Test
    public void shouldGetProjet() {
        // WHEN
        Projet projet = projetDao.getProjet(1L);
        //THEN
        assertThat(projet).isNotNull();
        assertThat(projet.getId()).isEqualTo(1L);
        assertThat(projet.getTitre()).isEqualTo("projet1");
        assertThat(projet.getDescription()).isEqualTo("description 1");
        assertThat(projet.getDateLimiteRenduLot1()).isEqualToIgnoringMillis(getDate(2014, Calendar.SEPTEMBER, 16, 18, 0, 0));
        assertThat(projet.getDateLimiteRenduLot2()).isEqualToIgnoringMillis(getDate(2014, Calendar.OCTOBER, 16, 18, 0, 0));
    }

    @Test
    public void shouldAjouterProjetComplet() throws Exception {
        // GIVEN
        Projet projet = new Projet(null, "monTitre", "maDescription", getDate(2014, Calendar.SEPTEMBER, 16, 13, 47, 0), getDate(2014, Calendar.SEPTEMBER, 17, 13, 47, 0));
        // WHEN
        Projet projetCree = projetDao.ajouterProjet(projet);
        // THEN
        assertThat(projetCree).isNotNull();
        assertThat(projetCree.getId()).isNotNull();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM projet WHERE id=?")
        ) {
            stmt.setLong(1, projetCree.getId());
            try (ResultSet results = stmt.executeQuery()) {
                assertThat(results.next()).isTrue();
                assertThat(results.getLong("id")).isEqualTo(projetCree.getId());
                assertThat(results.getString("titre")).isEqualTo("monTitre");
                assertThat(results.getString("description")).isEqualTo("maDescription");
                assertThat(results.getTimestamp("datelimiterendulot1")).isEqualToIgnoringMillis(getDate(2014, Calendar.SEPTEMBER, 16, 13, 47, 0));
                assertThat(results.getTimestamp("datelimiterendulot2")).isEqualToIgnoringMillis(getDate(2014, Calendar.SEPTEMBER, 17, 13, 47, 0));
            }
        }
    }

    @Test
    public void shouldModifierProjet() throws Exception {
        // GIVEN
        Projet projet = new Projet(1L, "monTitre", "maDescription", getDate(2014, Calendar.SEPTEMBER, 16, 13, 47, 0), getDate(2014, Calendar.SEPTEMBER, 17, 13, 47, 0));
        // WHEN
        projetDao.modifierProjet(projet);
        // THEN
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet results = stmt.executeQuery("SELECT * FROM projet WHERE id=1")
        ) {
            assertThat(results.next()).isTrue();
            assertThat(results.getLong("id")).isEqualTo(1L);
            assertThat(results.getString("titre")).isEqualTo("monTitre");
            assertThat(results.getString("description")).isEqualTo("maDescription");
            assertThat(results.getTimestamp("datelimiterendulot1")).isEqualToIgnoringMillis(getDate(2014, Calendar.SEPTEMBER, 16, 13, 47, 0));
            assertThat(results.getTimestamp("datelimiterendulot2")).isEqualToIgnoringMillis(getDate(2014, Calendar.SEPTEMBER, 17, 13, 47, 0));
        }
    }
}
