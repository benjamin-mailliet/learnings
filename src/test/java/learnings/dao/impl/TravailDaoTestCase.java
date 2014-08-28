package learnings.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;

import learnings.dao.DataSourceProvider;
import learnings.dao.TravailDao;
import learnings.model.ProjetTransversal;
import learnings.model.Seance;
import learnings.model.Travail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TravailDaoTestCase {
	private TravailDao travailDao = new TravailDaoImpl();

	@Before
	public void init() throws Exception {
		Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("DELETE FROM ressource");
		stmt.executeUpdate("DELETE FROM travail");
		stmt.executeUpdate("DELETE FROM seance");
		stmt.executeUpdate("DELETE FROM projettransversal");
		stmt.executeUpdate("DELETE FROM utilisateur");
		stmt.executeUpdate("INSERT INTO `utilisateur`(`id`,`email`,`motdepasse`,`admin`) VALUES(1,'eleve1@learnings-devwebhei.fr','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0)");
		stmt.executeUpdate("INSERT INTO `utilisateur`(`id`,`email`,`motdepasse`,`admin`) VALUES(2,'eleve2@learnings-devwebhei.fr','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0)");
		stmt.executeUpdate("INSERT INTO `seance`(`id`,`titre`,`description`,`date`,`type`,`datelimiterendu`,`isnote`) VALUES(1,'tp1','tp de debuggage','2014-07-29','TP','2014-07-29 18:00:00',true)");
		stmt.executeUpdate("INSERT INTO `learnings_test`.`projettransversal`(`id`,`titre`,`description`,`datelimiterendulot1`,`datelimiterendulot2`) VALUES(1,'projet','Projet individuel','2014-11-15','2015-01-15')");
		stmt.close();
		connection.close();
	}

	@Test
	public void testAjouterTravailCompletAvecSeance() {
		Travail travail = new Travail();
		travail.setChemin("/chemin");
		travail.setDateRendu(new GregorianCalendar(2014, Calendar.AUGUST, 28, 8, 38).getTime());
		travail.setNote(new BigDecimal("16.5"));
		travail.setEnseignement(new Seance(1L, "tp1", "tp de debuggage", new GregorianCalendar(2014, Calendar.JULY, 29).getTime()));

		travailDao.ajouterTravail(travail);

		Assert.assertNotNull(travail.getId());

	}

	@Test
	public void testAjouterTravailCompletAvecProjet() {
		Travail travail = new Travail();
		travail.setChemin("/chemin");
		travail.setDateRendu(new GregorianCalendar(2014, Calendar.AUGUST, 28, 8, 38).getTime());
		travail.setNote(new BigDecimal("16.5"));
		travail.setEnseignement(new ProjetTransversal(1L, "projet", "Projet individuel", new GregorianCalendar(2014, Calendar.NOVEMBER, 15).getTime(),
				new GregorianCalendar(2015, Calendar.JANUARY, 15).getTime()));

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

}
