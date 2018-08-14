package learnings.dao.impl;

import learnings.AbstractDaoTestCase;
import learnings.dao.SeanceDao;
import learnings.enums.TypeSeance;
import learnings.model.Seance;
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

public class SeanceDaoTestCase extends AbstractDaoTestCase {
    private SeanceDao seanceDao = new SeanceDaoImpl();

    @Before
    public void init() throws Exception {
        super.purgeBaseDeDonnees();

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()
        ) {
            stmt.executeUpdate("INSERT INTO seance(id,titre,description,date,type) VALUES(1,'cours1','cours de debuggage','2014-07-26','COURS')");
            stmt.executeUpdate("INSERT INTO seance(id,titre,description,date,type) VALUES(2,'cours2','cours de correction','2014-08-26','COURS')");
            stmt.executeUpdate("INSERT INTO seance(id,titre,description,date,type,datelimiterendu,isnote, nb_max_binome) VALUES(3,'tp1','tp de debuggage','2014-07-29','TP','2014-07-29 18:00:00',true, 3)");
            stmt.executeUpdate("INSERT INTO seance(id,titre,description,date,type,datelimiterendu,isnote, nb_max_binome) VALUES(4,'tp2','tp de correction','2014-08-29','TP','2014-08-29 18:00:00',true, 4)");
        }
    }

    @Test
    public void shouldListerSeances() {
        // WHEN
        List<Seance> listeCours = seanceDao.listerSeances();
        // THEN
        assertThat(listeCours).hasSize(4);
        assertThat(listeCours).extracting("id", "titre", "description", "date", "type", "dateLimiteRendu.time", "isNote", "nbMaxElevesParRendu").containsExactly(
                tuple(1L, "cours1", "cours de debuggage", getDate(2014, Calendar.JULY, 26), TypeSeance.COURS, null, false, null),
                tuple(3L, "tp1", "tp de debuggage", getDate(2014, Calendar.JULY, 29), TypeSeance.TP, getDate(2014, Calendar.JULY, 29, 18, 0, 0).getTime(), true, 3),
                tuple(2L, "cours2", "cours de correction", getDate(2014, Calendar.AUGUST, 26), TypeSeance.COURS, null, false, null),
                tuple(4L, "tp2", "tp de correction", getDate(2014, Calendar.AUGUST, 29), TypeSeance.TP, getDate(2014, Calendar.AUGUST, 29, 18, 0, 0).getTime(), true, 4)
        );
    }

    @Test
    public void shouldListerSeancesNotees() {
        // WHEN
        List<Seance> listeCours = seanceDao.listerSeancesNotees();
        // THEN
        assertThat(listeCours).hasSize(2);
        assertThat(listeCours).extracting("id", "titre", "description", "date", "type", "dateLimiteRendu.time", "isNote", "nbMaxElevesParRendu").containsExactly(
                tuple(3L, "tp1", "tp de debuggage", getDate(2014, Calendar.JULY, 29), TypeSeance.TP, getDate(2014, Calendar.JULY, 29, 18, 0, 0).getTime(), true, 3),
                tuple(4L, "tp2", "tp de correction", getDate(2014, Calendar.AUGUST, 29), TypeSeance.TP, getDate(2014, Calendar.AUGUST, 29, 18, 0, 0).getTime(), true, 4)
        );
    }

    @Test
    public void shouldListerTPNotesParDateRendu() {
        // WHEN
        List<Seance> listeTps = seanceDao.listerTPNotesParDateRendu(getDate(2014, Calendar.AUGUST, 29, 15, 27, 0));
        // THEN
        assertThat(listeTps).hasSize(1);
        assertThat(listeTps).extracting("id", "titre", "description", "date", "type", "dateLimiteRendu.time", "isNote", "nbMaxElevesParRendu").containsExactly(
                tuple(4L, "tp2", "tp de correction", getDate(2014, Calendar.AUGUST, 29), TypeSeance.TP, getDate(2014, Calendar.AUGUST, 29, 18, 0, 0).getTime(), true, 4)
        );
    }

    @Test
    public void shouldGetSeance() {
        // WHEN
        Seance seance = seanceDao.getSeance(3L);
        // THEN
        assertThat(seance).isNotNull();
        assertThat(seance.getId()).isEqualTo(3L);
        assertThat(seance.getTitre()).isEqualTo("tp1");
        assertThat(seance.getDescription()).isEqualTo("tp de debuggage");
        assertThat(seance.getType()).isEqualTo(TypeSeance.TP);
        assertThat(seance.getDate()).isEqualToIgnoringMillis(getDate(2014, Calendar.JULY, 29));
        assertThat(seance.getDateLimiteRendu()).isEqualToIgnoringMillis(getDate(2014, Calendar.JULY, 29, 18, 0, 0));
        assertThat(seance.getIsNote()).isTrue();
    }

    @Test
    public void shouldAjouterSeanceComplete() throws Exception {
        // GIVEN
        Seance seance = new Seance(null, "monTitre", "maDescription", getDate(2014, Calendar.SEPTEMBER, 6), true, getDate(2014, Calendar.SEPTEMBER, 6, 18, 5, 0), TypeSeance.TP, 5);
        // WHEN
        Seance seanceCreee = seanceDao.ajouterSeance(seance);
        // THEN
        assertThat(seanceCreee).isNotNull();
        assertThat(seanceCreee.getId()).isNotNull();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM seance WHERE id=?")
        ) {
            stmt.setLong(1, seanceCreee.getId());
            try (ResultSet results = stmt.executeQuery()) {
                assertThat(results.next()).isTrue();
                assertThat(results.getLong("id")).isEqualTo(seanceCreee.getId());
                assertThat(results.getString("titre")).isEqualTo("monTitre");
                assertThat(results.getString("description")).isEqualTo("maDescription");
                assertThat(TypeSeance.valueOf(results.getString("type"))).isEqualTo(TypeSeance.TP);
                assertThat(results.getTimestamp("date")).isEqualToIgnoringMillis(getDate(2014, Calendar.SEPTEMBER, 6));
                assertThat(results.getTimestamp("datelimiterendu")).isEqualToIgnoringMillis(getDate(2014, Calendar.SEPTEMBER, 6, 18, 5, 0));
                assertThat(results.getBoolean("isnote")).isTrue();
                assertThat(results.getInt("nb_max_binome")).isEqualTo(5);
            }
        }
    }

    @Test
    public void shouldAjouterSeanceMini() throws Exception {
        // GIVEN
        Seance seance = new Seance(null, "monTitre", null, getDate(2014, Calendar.SEPTEMBER, 6), false, null, TypeSeance.TP, null);
        // WHEN
        Seance seanceCreee = seanceDao.ajouterSeance(seance);
        // THEN
        assertThat(seanceCreee).isNotNull();
        assertThat(seanceCreee.getId()).isNotNull();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM seance WHERE id=?")
        ) {
            stmt.setLong(1, seanceCreee.getId());
            try (ResultSet results = stmt.executeQuery()) {
                assertThat(results.next()).isTrue();
                assertThat(results.getLong("id")).isEqualTo(seanceCreee.getId());
                assertThat(results.getString("titre")).isEqualTo("monTitre");
                assertThat(results.getString("description")).isEqualTo("");
                assertThat(TypeSeance.valueOf(results.getString("type"))).isEqualTo(TypeSeance.TP);
                assertThat(results.getTimestamp("date")).isEqualToIgnoringMillis(getDate(2014, Calendar.SEPTEMBER, 6));
                assertThat(results.getTimestamp("datelimiterendu")).isNull();
                assertThat(results.getBoolean("isnote")).isFalse();
                assertThat(results.getString("nb_max_binome")).isNull();
            }
        }
    }

    @Test
    public void shouldModifierSeance() throws Exception {
        // GIVEN
        Seance seance = new Seance(1L, "monTitre", "maDescription", getDate(2014, Calendar.SEPTEMBER, 6), true, getDate(2014, Calendar.SEPTEMBER, 6, 18, 5, 0), TypeSeance.TP, 10);
        // WHEN
        seanceDao.modifierSeance(seance);
        // THEN
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet results = stmt.executeQuery("SELECT * FROM seance WHERE id=1")
        ) {
            assertThat(results.next()).isTrue();
            assertThat(results.getLong("id")).isEqualTo(1L);
            assertThat(results.getString("titre")).isEqualTo("monTitre");
            assertThat(results.getString("description")).isEqualTo("maDescription");
            assertThat(TypeSeance.valueOf(results.getString("type"))).isEqualTo(TypeSeance.TP);
            assertThat(results.getTimestamp("date")).isEqualToIgnoringMillis(getDate(2014, Calendar.SEPTEMBER, 6));
            assertThat(results.getTimestamp("datelimiterendu")).isEqualToIgnoringMillis(getDate(2014, Calendar.SEPTEMBER, 6, 18, 5, 0));
            assertThat(results.getBoolean("isnote")).isTrue();
            assertThat(results.getInt("nb_max_binome")).isEqualTo(10);
        }
    }
}