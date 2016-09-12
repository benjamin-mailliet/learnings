package learnings.dao.impl;

import learnings.AbstractDaoTestCase;
import learnings.dao.TravailDao;
import learnings.model.Projet;
import learnings.model.Seance;
import learnings.model.Travail;
import learnings.model.Utilisateur;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TravailDaoTestCase extends AbstractDaoTestCase {
    private TravailDao travailDao = new TravailDaoImpl();

    @Before
    public void init() throws Exception {
        super.purgeBaseDeDonnees();
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()
        ) {
            stmt.executeUpdate("INSERT INTO `utilisateur`(`id`,nom,prenom,email,groupe,motdepasse,admin) VALUES(1,'nom1', 'prenom1', 'eleve1@learnings-devwebhei.fr','GROUPE_1','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0)");
            stmt.executeUpdate("INSERT INTO `utilisateur`(`id`,nom,prenom,email,groupe,motdepasse,admin) VALUES(2,'nom2', 'prenom2', 'eleve2@learnings-devwebhei.fr','GROUPE_2','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0)");
            stmt.executeUpdate("INSERT INTO `seance`(`id`,`titre`,`description`,`date`,`type`,`datelimiterendu`,`isnote`) VALUES(1,'tp1','tp de debuggage','2014-07-29','TP','2014-07-29 18:00:00',true)");
            stmt.executeUpdate("INSERT INTO `seance`(`id`,`titre`,`description`,`date`,`type`,`datelimiterendu`,`isnote`) VALUES(2,'tp2','tp de debuggage','2014-07-30','TP','2014-09-29 18:00:00',true)");
            stmt.executeUpdate("INSERT INTO `learnings_test`.`projettransversal`(`id`,`titre`,`description`,`datelimiterendulot1`,`datelimiterendulot2`) VALUES(1,'projet','Projet individuel','2014-11-15','2015-01-15')");
            stmt.executeUpdate("INSERT INTO travail(id, dateRendu, seance_id, chemin) VALUES(1,'2014-08-27 17:27', 1, '/chemin/fichier.zip')");
            stmt.executeUpdate("INSERT INTO travail(id, dateRendu, seance_id, chemin) VALUES(2,'2014-08-27 17:28', 1, '/chemin/fichier2.zip')");
            stmt.executeUpdate("INSERT INTO travail(id, dateRendu, seance_id, chemin) VALUES(3,'2014-08-27 17:29', 2, '/chemin/fichier3.zip')");
            stmt.executeUpdate("INSERT INTO travailutilisateur(idtravail, idutilisateur) VALUES(1, 1)");
            stmt.executeUpdate("INSERT INTO travailutilisateur(idtravail, idutilisateur) VALUES(3, 1)");
            stmt.executeUpdate("INSERT INTO travailutilisateur(idtravail, idutilisateur) VALUES(3, 2)");
        }
    }

    @Test
    public void shouldAjouterTravailCompletAvecSeance() throws Exception {
        // GIVEN
        Travail travail = new Travail();
        travail.setChemin("/chemin");
        travail.setDateRendu(getDate(2014, Calendar.AUGUST, 28, 8, 38, 0));
        travail.setNote(new BigDecimal("16.5"));
        travail.setEnseignement(new Seance(1L, "tp1", "tp de debuggage", getDate(2014, Calendar.JULY, 29)));
        // WHEN
        travailDao.ajouterTravail(travail);
        // THEN
        assertThat(travail).isNotNull();
        assertThat(travail.getId()).isNotNull();

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM travail WHERE id=?")
        ) {
            stmt.setLong(1, travail.getId());
            try (ResultSet results = stmt.executeQuery()) {
                assertThat(results.next()).isTrue();
                assertThat(results.getLong("id")).isEqualTo(travail.getId());
                assertThat(results.getString("chemin")).isEqualTo("/chemin");
                assertThat(results.getTimestamp("dateRendu")).isEqualToIgnoringMillis(getDate(2014, Calendar.AUGUST, 28, 8, 38, 0));
                assertThat(results.getLong("seance_id")).isEqualTo(1L);
                assertThat(results.getString("projettransversal_id")).isNull();
            }
        }
    }

    @Test
    public void shouldAjouterTravailCompletAvecProjet() throws Exception {
        // GIVEN
        Travail travail = new Travail();
        travail.setChemin("/chemin");
        travail.setDateRendu(getDate(2014, Calendar.AUGUST, 28, 8, 38, 0));
        travail.setNote(new BigDecimal("16.5"));
        travail.setEnseignement(new Projet(1L, "projet", "Projet individuel", getDate(2014, Calendar.NOVEMBER, 15), getDate(2015, Calendar.JANUARY, 15)));
        travail.setCommentaire("monCommentaire");
        // WHEN
        travailDao.ajouterTravail(travail);
        // THEN
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM travail WHERE id=?")
        ) {
            stmt.setLong(1, travail.getId());
            try (ResultSet results = stmt.executeQuery()) {
                assertThat(results.next()).isTrue();
                assertThat(results.getLong("id")).isEqualTo(travail.getId());
                assertThat(results.getString("chemin")).isEqualTo("/chemin");
                assertThat(results.getTimestamp("dateRendu")).isEqualToIgnoringMillis(getDate(2014, Calendar.AUGUST, 28, 8, 38, 0));
                assertThat(results.getString("seance_id")).isNull();
                assertThat(results.getLong("projettransversal_id")).isEqualTo(1L);
            }
        }
    }

    @Test
    public void shouldAjouterTravailMinimal() throws Exception {
        // GIVEN
        Travail travail = new Travail();
        travail.setChemin("/chemin");
        travail.setDateRendu(getDate(2014, Calendar.AUGUST, 28, 8, 38, 0));
        travail.setNote(null);
        travail.setEnseignement(new Seance(1L, "tp1", "tp de debuggage", getDate(2014, Calendar.JULY, 29)));
        // WHEN
        travailDao.ajouterTravail(travail);
        // THEN
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM travail WHERE id=?")
        ) {
            stmt.setLong(1, travail.getId());
            try (ResultSet results = stmt.executeQuery()) {
                assertThat(results.next()).isTrue();
                assertThat(results.getLong("id")).isEqualTo(travail.getId());
                assertThat(results.getString("chemin")).isEqualTo("/chemin");
                assertThat(results.getTimestamp("dateRendu")).isEqualToIgnoringMillis(getDate(2014, Calendar.AUGUST, 28, 8, 38, 0));
                assertThat(results.getLong("seance_id")).isEqualTo(1L);
                assertThat(results.getString("projettransversal_id")).isNull();
            }
        }
    }

    @Test
    public void shouldMettreAJourTravail() throws Exception {
        // WHEN
        travailDao.mettreAJourTravail(1L,getDate(2014, Calendar.SEPTEMBER, 1, 13, 36, 25), "/nouveau/chemin/fichier.zip", "http://git/myproject",
                "nouveauCommentaire");
        // THEN
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet results = stmt.executeQuery("SELECT * FROM travail WHERE id=1")
        ) {
            assertThat(results.next()).isTrue();
            assertThat(results.getLong("id")).isEqualTo(1L);
            assertThat(results.getString("chemin")).isEqualTo("/nouveau/chemin/fichier.zip");
            assertThat(results.getString("urlRepository")).isEqualTo("http://git/myproject");
            assertThat(results.getString("commentaire")).isEqualTo("nouveauCommentaire");
            assertThat(results.getTimestamp("dateRendu")).isEqualToIgnoringMillis(getDate(2014, Calendar.SEPTEMBER, 1, 13, 36, 25));
            assertThat(results.getLong("seance_id")).isEqualTo(1L);
            assertThat(results.getString("projettransversal_id")).isNull();
        }
    }

    @Test
    public void shouldGetTravailUtilisateurParSeance() {
        // WHEN
        Travail travail = travailDao.getTravailUtilisateurParSeance(2L, 2L);
        // THEN
        assertThat(travail).isNotNull();
        assertThat(travail.getId()).isEqualTo(3L);
    }

    @Test
    public void shouldListerTravauxUtilisateurParSeanceAucunResultat() {
        // WHEN
        Travail travail = travailDao.getTravailUtilisateurParSeance(1L, 2L);
        // THEN
        assertThat(travail).isNull();
    }

    @Test
    public void shouldAjouterUtilisateur() throws Exception {
        // WHEN
        travailDao.ajouterUtilisateur(1L, 2L);
        // THEN
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet results = stmt.executeQuery("SELECT * FROM travailutilisateur WHERE idtravail=1 AND idutilisateur=2")
        ) {
            assertThat(results.next()).isTrue();
            assertThat(results.getLong("idtravail")).isEqualTo(1L);
            assertThat(results.getLong("idutilisateur")).isEqualTo(2L);
        }
    }

    @Test
    public void shouldListerTravauxParSeance() {
        // WHEN
        List<Travail> travaux = travailDao.listerTravauxParSeance(1L);
        // THEN
        assertThat(travaux).hasSize(2);
        assertThat(travaux).extracting("id").containsOnly(1L, 2L);
    }

    @Test
    public void shouldListerTravauxParUtilisateur() {
        // WHEN
        List<Travail> travaux = travailDao.listerTravauxParUtilisateur(1L);
        // THEN
        assertThat(travaux).hasSize(2);
        assertThat(travaux).extracting("id").containsOnly(1L, 3L);
    }

    @Test
    public void shouldListerUtilisateurs() {
        // WHEN
        List<Utilisateur> utilisateurs = travailDao.listerUtilisateursParTravail(3L);
        // THEN
        assertThat(utilisateurs).hasSize(2);
        assertThat(utilisateurs).extracting("id").containsOnly(1L, 2L);
    }

    @Test
    public void shouldGetTravail() {
        // WHEN
        Travail travail = travailDao.getTravail(2L);
        // THEN
        assertThat(travail).isNotNull();
        assertThat(travail.getId()).isEqualTo(2L);
        assertThat(travail.getChemin()).isEqualTo("/chemin/fichier2.zip");
    }

    @Test
    public void shouldGetTravailNull() {
        // WHEN
        Travail travail = travailDao.getTravail(-1L);
        // THEN
        assertThat(travail).isNull();
	}


	@Test
	public void shouldEnregistrerNoteTravail() throws SQLException {
		travailDao.enregistrerNoteTravail(1L, new BigDecimal(15), "commentaire de test");

		Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		ResultSet results = stmt.executeQuery("SELECT * FROM travail WHERE id=1");
		if (results.next()) {
			Assertions.assertThat(results.getLong("id")).isEqualTo(1L);
			Assertions.assertThat(results.getBigDecimal("note").toString()).isEqualTo("15.00");
			Assertions.assertThat(results.getString("commentaireNote")).isEqualTo("commentaire de test");
		} else {
			Assertions.fail("Aucun travail n'a été enregistré");
		}
    }

}
