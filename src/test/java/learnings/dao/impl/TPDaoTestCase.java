package learnings.dao.impl;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import learnings.dao.DataSourceProvider;
import learnings.dao.TPDao;
import learnings.model.TP;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TPDaoTestCase {
	private TPDao tpDao = new TPDaoImpl();

	@Before
	public void init() throws Exception {
		Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("DELETE FROM tp");
		stmt.executeUpdate("INSERT INTO `tp`(`id`,`titre`,`description`,`date`) VALUES(1,'tp1','tp de debuggage','2014-08-26')");
		stmt.executeUpdate("INSERT INTO `tp`(`id`,`titre`,`description`,`date`,`isnote`, `datelimiterendu`) VALUES(2,'tp2','tp de correction','2014-08-29',true, '2014-08-29 18:00:00')");
		stmt.close();
		connection.close();
	}

	@Test
	public void testListerTP() {
		List<TP> listeTps = tpDao.listerTPs();

		Assert.assertEquals(2L, listeTps.get(0).getId().longValue());
		Assert.assertEquals("tp2", listeTps.get(0).getTitre());
		Assert.assertEquals("tp de correction", listeTps.get(0).getDescription());
		Assert.assertEquals(new GregorianCalendar(2014, 7, 29).getTime().getTime(), listeTps.get(0).getDate().getTime());
		Assert.assertTrue(listeTps.get(0).getIsNote());

		Assert.assertEquals(1L, listeTps.get(1).getId().longValue());
		Assert.assertFalse(listeTps.get(1).getIsNote());
	}

	@Test
	public void testListerTPNotesParDateRendu() {
		Calendar cal = new GregorianCalendar(2014, Calendar.AUGUST, 29, 15, 27, 0);

		List<TP> listeTps = tpDao.listerTPNotesParDateRendu(cal.getTime());
		Assert.assertEquals(1, listeTps.size());

		Assert.assertEquals(2L, listeTps.get(0).getId().longValue());
		Assert.assertEquals("tp2", listeTps.get(0).getTitre());
		Assert.assertEquals("tp de correction", listeTps.get(0).getDescription());
		Assert.assertEquals(new GregorianCalendar(2014, Calendar.AUGUST, 29).getTime(), listeTps.get(0).getDate());
		Assert.assertTrue(listeTps.get(0).getIsNote());
	}
}
