package learnings.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import learnings.dao.TravailDao;
import learnings.model.Projet;
import learnings.model.Seance;
import learnings.model.Travail;
import learnings.model.Utilisateur;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TravailDaoTestCase extends  AbstractTestCase{
	private TravailDao travailDao = new TravailDaoImpl();

	@Before
	public void init() throws Exception {
		super.purgeBaseDeDonnees();

		Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
		Statement stmt = connection.createStatement();
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
		stmt.close();
		connection.close();
	}

	@Test
	public void testAjouterTravailCompletAvecSeance() throws Exception {
		Travail travail = new Travail();
		travail.setChemin("/chemin");
		travail.setDateRendu(new GregorianCalendar(2014, Calendar.AUGUST, 28, 8, 38).getTime());
		travail.setNote(new BigDecimal("16.5"));
		travail.setEnseignement(new Seance(1L, "tp1", "tp de debuggage", new GregorianCalendar(2014, Calendar.JULY, 29).getTime()));

		travailDao.ajouterTravail(travail);

		Assert.assertNotNull(travail.getId());

		Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
		PreparedStatement stmt = connection.prepareStatement("SELECT * FROM travail WHERE id=?");
		stmt.setLong(1, travail.getId());
		ResultSet results = stmt.executeQuery();
		if (results.next()) {
			Assert.assertEquals(travail.getId().longValue(), results.getLong("id"));
			Assert.assertEquals("/chemin", results.getString("chemin"));
			Assert.assertEquals(new GregorianCalendar(2014, Calendar.AUGUST, 28, 8, 38).getTime(), results.getTimestamp("dateRendu"));
			Assert.assertEquals(1L, results.getLong("seance_id"));
			Assert.assertEquals(0L, results.getLong("projettransversal_id"));
		} else {
			Assert.fail();
		}
		stmt.close();
		connection.close();
	}

	@Test
	public void testAjouterTravailCompletAvecProjet() {
		Travail travail = new Travail();
		travail.setChemin("/chemin");
		travail.setDateRendu(new GregorianCalendar(2014, Calendar.AUGUST, 28, 8, 38).getTime());
		travail.setNote(new BigDecimal("16.5"));
		travail.setEnseignement(new Projet(1L, "projet", "Projet individuel", new GregorianCalendar(2014, Calendar.NOVEMBER, 15).getTime(),
				new GregorianCalendar(2015, Calendar.JANUARY, 15).getTime()));
		travail.setCommentaire("monCommentaire");

		travailDao.ajouterTravail(travail);

		Assert.assertNotNull(travail.getId());
	}

	@Test
	public void testAjouterTravailMinimal() {
		Travail travail = new Travail();
		travail.setChemin("/chemin");
		travail.setDateRendu(new GregorianCalendar(2014, Calendar.AUGUST, 28, 8, 38).getTime());
		travail.setNote(null);
		travail.setEnseignement(new Seance(1L, "tp1", "tp de debuggage", new GregorianCalendar(2014, Calendar.JULY, 29).getTime()));

		travailDao.ajouterTravail(travail);

		Assert.assertNotNull(travail.getId());
	}

	@Test
	public void testMettreAJourTravail() throws Exception {
		travailDao.mettreAJourTravail(1L, new GregorianCalendar(2014, Calendar.SEPTEMBER, 1, 13, 36, 25).getTime(), "/nouveau/chemin/fichier.zip", "http://git/myproject",
				"nouveauCommentaire");

		Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		ResultSet results = stmt.executeQuery("SELECT * FROM travail WHERE id=1");
		if (results.next()) {
			Assert.assertEquals(1L, results.getLong("id"));
			Assert.assertEquals("/nouveau/chemin/fichier.zip", results.getString("chemin"));
			Assert.assertEquals("http://git/myproject", results.getString("urlRepository"));
			Assert.assertEquals("nouveauCommentaire", results.getString("commentaire"));
			Assert.assertEquals(new GregorianCalendar(2014, Calendar.SEPTEMBER, 1, 13, 36, 25).getTime(), results.getTimestamp("dateRendu"));
			Assert.assertEquals(1L, results.getLong("seance_id"));
			Assert.assertEquals(0L, results.getLong("projettransversal_id"));
		} else {
			Assert.fail();
		}
		stmt.close();
		connection.close();
	}

	@Test
	public void testListerTravauxUtilisateurParSeance() {
		Travail travail = travailDao.getTravailUtilisateurParSeance(2L, 2L);

		Assert.assertNotNull(travail);
		Assert.assertEquals(3L, travail.getId().longValue());
	}

	@Test
	public void testListerTravauxUtilisateurParSeanceAucunResultat() {
		Travail travail = travailDao.getTravailUtilisateurParSeance(1L, 2L);

		Assert.assertNull(travail);
	}

	@Test
	public void testAjouterUtilisateur() throws Exception {
		travailDao.ajouterUtilisateur(1L, 2L);

		Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		ResultSet results = stmt.executeQuery("SELECT * FROM travailutilisateur WHERE idtravail=1 AND idutilisateur=2");
		if (results.next()) {
			Assert.assertEquals(1L, results.getLong("idtravail"));
			Assert.assertEquals(2L, results.getLong("idutilisateur"));
		} else {
			Assert.fail();
		}
		stmt.close();
		connection.close();
	}

	@Test
	public void testListerTravauxParSeance() {
		List<Travail> travaux = travailDao.listerTravauxParSeance(1L);

		Assert.assertEquals(2, travaux.size());
		Assert.assertEquals(1L, travaux.get(0).getId().longValue());
		Assert.assertEquals(2L, travaux.get(1).getId().longValue());
	}

	@Test
	public void testListerTravauxParUtilisateur() {
		List<Travail> travaux = travailDao.listerTravauxParUtilisateur(1L);

		Assert.assertEquals(2, travaux.size());
		Assert.assertEquals(1L, travaux.get(0).getId().longValue());
		Assert.assertEquals(3L, travaux.get(1).getId().longValue());
	}

	@Test
	public void testListerUtilisateurs() {
		List<Utilisateur> utilisateurs = travailDao.listerUtilisateurs(3L);

		Assert.assertEquals(2, utilisateurs.size());
		Assert.assertEquals(1L, utilisateurs.get(0).getId().longValue());
		Assert.assertEquals(2L, utilisateurs.get(1).getId().longValue());
	}

	@Test
	public void testGetTravail() {
		Travail travail = travailDao.getTravail(2L);

		Assert.assertNotNull(travail);
		Assert.assertEquals(2L, travail.getId().longValue());
		Assert.assertEquals("/chemin/fichier2.zip", travail.getChemin());
	}

	@Test
	public void testGetTravailNull() {
		Travail travail = travailDao.getTravail(-1L);

		Assert.assertNull(travail);
	}

}
