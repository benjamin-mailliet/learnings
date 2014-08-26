package learnings.dao.impl;

import java.sql.Connection;
import java.sql.Statement;
import java.util.GregorianCalendar;
import java.util.List;

import learnings.dao.CoursDao;
import learnings.dao.DataSourceProvider;
import learnings.model.Cours;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CoursDaoTestCase {
	private CoursDao coursDao = new CoursDaoImpl();

	@Before
	public void init() throws Exception {
		Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("DELETE FROM ressource");
		stmt.executeUpdate("DELETE FROM cours");
		stmt.executeUpdate("INSERT INTO `cours`(`id`,`titre`,`description`,`date`) VALUES(1,'cours1','cours de debuggage','2014-08-26')");
		stmt.executeUpdate("INSERT INTO `cours`(`id`,`titre`,`description`,`date`) VALUES(2,'cours2','cours de correction','2014-08-29')");
		stmt.close();
		connection.close();
	}

	@Test
	public void testListerCours() {
		List<Cours> listeCours = coursDao.listerCours();

		Assert.assertEquals(2L, listeCours.get(0).getId().longValue());
		Assert.assertEquals("cours2", listeCours.get(0).getTitre());
		Assert.assertEquals("cours de correction", listeCours.get(0).getDescription());
		Assert.assertEquals(new GregorianCalendar(2014, 7, 29).getTime().getTime(), listeCours.get(0).getDate().getTime());

		Assert.assertEquals(1L, listeCours.get(1).getId().longValue());
	}
}
