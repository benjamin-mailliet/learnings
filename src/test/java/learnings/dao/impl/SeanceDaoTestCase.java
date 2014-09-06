package learnings.dao.impl;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import learnings.dao.SeanceDao;
import learnings.enums.TypeSeance;
import learnings.model.Seance;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SeanceDaoTestCase {
	private SeanceDao seanceDao = new SeanceDaoImpl();

	@Before
	public void init() throws Exception {
		Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("DELETE FROM ressource");
		stmt.executeUpdate("DELETE FROM travailutilisateur");
		stmt.executeUpdate("DELETE FROM travail");
		stmt.executeUpdate("DELETE FROM seance");
		stmt.executeUpdate("INSERT INTO `seance`(`id`,`titre`,`description`,`date`,`type`) VALUES(1,'cours1','cours de debuggage','2014-07-26','COURS')");
		stmt.executeUpdate("INSERT INTO `seance`(`id`,`titre`,`description`,`date`,`type`) VALUES(2,'cours2','cours de correction','2014-08-26','COURS')");
		stmt.executeUpdate("INSERT INTO `seance`(`id`,`titre`,`description`,`date`,`type`,`datelimiterendu`,`isnote`) VALUES(3,'tp1','tp de debuggage','2014-07-29','TP','2014-07-29 18:00:00',true)");
		stmt.executeUpdate("INSERT INTO `seance`(`id`,`titre`,`description`,`date`,`type`,`datelimiterendu`,`isnote`) VALUES(4,'tp2','tp de correction','2014-08-29','TP','2014-08-29 18:00:00',true)");
		stmt.close();
		connection.close();
	}

	@Test
	public void testListerSeances() {
		List<Seance> listeCours = seanceDao.listerSeances();

		Assert.assertEquals(4, listeCours.size());

		Assert.assertEquals(1L, listeCours.get(0).getId().longValue());
		Assert.assertEquals("cours1", listeCours.get(0).getTitre());
		Assert.assertEquals("cours de debuggage", listeCours.get(0).getDescription());
		Assert.assertEquals(new GregorianCalendar(2014, Calendar.JULY, 26).getTime().getTime(), listeCours.get(0).getDate().getTime());

		Assert.assertEquals(3L, listeCours.get(1).getId().longValue());

	}

	@Test
	public void testListerSeancesNotees() {
		List<Seance> listeCours = seanceDao.listerSeancesNotees();

		Assert.assertEquals(2, listeCours.size());

		Assert.assertEquals(3L, listeCours.get(0).getId().longValue());
		Assert.assertEquals("tp1", listeCours.get(0).getTitre());
		Assert.assertEquals("tp de debuggage", listeCours.get(0).getDescription());
		Assert.assertEquals(new GregorianCalendar(2014, Calendar.JULY, 29).getTime().getTime(), listeCours.get(0).getDate().getTime());
		Assert.assertEquals(4L, listeCours.get(1).getId().longValue());
		Assert.assertEquals("tp2", listeCours.get(1).getTitre());
		Assert.assertEquals("tp de correction", listeCours.get(1).getDescription());
		Assert.assertEquals(new GregorianCalendar(2014, Calendar.AUGUST, 29).getTime().getTime(), listeCours.get(1).getDate().getTime());

	}

	@Test
	public void testListerTPNotesParDateRendu() {
		Calendar cal = new GregorianCalendar(2014, Calendar.AUGUST, 29, 15, 27, 0);

		List<Seance> listeTps = seanceDao.listerTPNotesParDateRendu(cal.getTime());
		Assert.assertEquals(1, listeTps.size());

		Assert.assertEquals(4L, listeTps.get(0).getId().longValue());
		Assert.assertEquals("tp2", listeTps.get(0).getTitre());
		Assert.assertEquals("tp de correction", listeTps.get(0).getDescription());
		Assert.assertEquals(new GregorianCalendar(2014, Calendar.AUGUST, 29).getTime(), listeTps.get(0).getDate());
		Assert.assertTrue(listeTps.get(0).getIsNote());
	}

	@Test
	public void testGetSeance() {
		Seance seance = seanceDao.getSeance(3L);

		Assert.assertNotNull(seance);
		Assert.assertEquals(3L, seance.getId().longValue());
		Assert.assertEquals("tp1", seance.getTitre());
		Assert.assertEquals("tp de debuggage", seance.getDescription());
		Assert.assertEquals(TypeSeance.TP, seance.getType());
		Assert.assertEquals(new GregorianCalendar(2014, Calendar.JULY, 29).getTime(), seance.getDate());
		Assert.assertEquals(new GregorianCalendar(2014, Calendar.JULY, 29, 18, 0, 0).getTime(), seance.getDateLimiteRendu());
	}
}
