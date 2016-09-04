package learnings.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import learnings.dao.ProjetDao;
import learnings.model.Projet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProjetDaoTestCase extends AbstractTestCase{
	private ProjetDao projetDao = new ProjetDaoImpl();

	@Before
	public void init() throws Exception {
		super.purgeBaseDeDonnees();

		Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("INSERT INTO `projettransversal`(`id`,`titre`,`description`,`datelimiterendulot1`,`datelimiterendulot2`) VALUES(1,'projet1','description 1','2014-09-16 18:00:00','2014-10-16 18:00:00')");
		stmt.executeUpdate("INSERT INTO `projettransversal`(`id`,`titre`,`description`,`datelimiterendulot1`,`datelimiterendulot2`) VALUES(2,'projet2','description 2','2014-09-16 18:00:00','2014-10-10 18:00:00')");
		stmt.executeUpdate("INSERT INTO `projettransversal`(`id`,`titre`,`description`,`datelimiterendulot1`,`datelimiterendulot2`) VALUES(3,'projet3','description 3','2014-09-17 18:00:00','2014-09-26 18:00:00')");
		stmt.executeUpdate("INSERT INTO `projettransversal`(`id`,`titre`,`description`,`datelimiterendulot1`,`datelimiterendulot2`) VALUES(4,'projet4','description 4','2014-09-19 18:00:00','2014-10-20 18:00:00')");
		stmt.close();
		connection.close();
	}

	@Test
	public void testListerProjets() {
		List<Projet> listeProjets = projetDao.listerProjets();

		Assert.assertEquals(4, listeProjets.size());

		Assert.assertEquals(2L, listeProjets.get(0).getId().longValue());
		Assert.assertEquals(1L, listeProjets.get(1).getId().longValue());
		Assert.assertEquals(3L, listeProjets.get(2).getId().longValue());
		Assert.assertEquals(4L, listeProjets.get(3).getId().longValue());
	}

	@Test
	public void testGetProjet() {
		Projet projet = projetDao.getProjet(1L);
		Assert.assertNotNull(projet);
		Assert.assertEquals(1L, projet.getId().longValue());
		Assert.assertEquals("projet1", projet.getTitre());
		Assert.assertEquals("description 1", projet.getDescription());
		Assert.assertEquals(new GregorianCalendar(2014, Calendar.SEPTEMBER, 16, 18, 0).getTime(), projet.getDateLimiteRenduLot1());
		Assert.assertEquals(new GregorianCalendar(2014, Calendar.OCTOBER, 16, 18, 0).getTime(), projet.getDateLimiteRenduLot2());
	}

	@Test
	public void testAjouterProjetComplet() throws Exception {
		Projet projet = new Projet(null, "monTitre", "maDescription", new GregorianCalendar(2014, Calendar.SEPTEMBER, 16, 13, 47).getTime(),
				new GregorianCalendar(2014, Calendar.SEPTEMBER, 17, 13, 47).getTime());

		Projet projetCree = projetDao.ajouterProjet(projet);

		Assert.assertNotNull(projetCree.getId());

		Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
		PreparedStatement stmt = connection.prepareStatement("SELECT * FROM projettransversal WHERE id=?");
		stmt.setLong(1, projetCree.getId());
		ResultSet results = stmt.executeQuery();
		if (results.next()) {
			Assert.assertEquals(projetCree.getId().longValue(), results.getLong("id"));
			Assert.assertEquals("monTitre", results.getString("titre"));
			Assert.assertEquals("maDescription", results.getString("description"));
			Assert.assertEquals(new GregorianCalendar(2014, Calendar.SEPTEMBER, 16, 13, 47).getTime(), results.getTimestamp("datelimiterendulot1"));
			Assert.assertEquals(new GregorianCalendar(2014, Calendar.SEPTEMBER, 17, 13, 47).getTime(), results.getTimestamp("datelimiterendulot2"));
		} else {
			Assert.fail();
		}
		stmt.close();
		connection.close();
	}

	@Test
	public void testModifierProjet() throws Exception {
		Projet projet = new Projet(1L, "monTitre", "maDescription", new GregorianCalendar(2014, Calendar.SEPTEMBER, 16, 13, 47).getTime(),
				new GregorianCalendar(2014, Calendar.SEPTEMBER, 17, 13, 47).getTime());

		projetDao.modifierProjet(projet);

		Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		ResultSet results = stmt.executeQuery("SELECT * FROM projettransversal WHERE id=1");
		if (results.next()) {
			Assert.assertEquals(1L, results.getLong("id"));
			Assert.assertEquals("monTitre", results.getString("titre"));
			Assert.assertEquals("maDescription", results.getString("description"));
			Assert.assertEquals(new GregorianCalendar(2014, Calendar.SEPTEMBER, 16, 13, 47).getTime(), results.getTimestamp("datelimiterendulot1"));
			Assert.assertEquals(new GregorianCalendar(2014, Calendar.SEPTEMBER, 17, 13, 47).getTime(), results.getTimestamp("datelimiterendulot2"));
		} else {
			Assert.fail();
		}
		stmt.close();
		connection.close();
	}
}
