package learnings.dao.impl;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import learnings.dao.DataSourceProvider;
import learnings.dao.RessourceDao;
import learnings.model.Cours;
import learnings.model.Ressource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RessourceDaoTestCase {
	private RessourceDao ressourceDao = new RessourceDaoImpl();

	@Before
	public void init() throws Exception {
		Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("DELETE FROM ressource");
		stmt.executeUpdate("DELETE FROM cours");
		stmt.executeUpdate("INSERT INTO `cours`(`id`,`titre`,`description`,`date`) VALUES(1,'cours1','cours de debuggage','2014-08-26')");
		stmt.executeUpdate("INSERT INTO `ressource`(`id`,`titre`,`chemin`,`cours_id`) VALUES(1,'ressource1','chemin ressource de cours 1',1)");
		stmt.executeUpdate("INSERT INTO `ressource`(`id`,`titre`,`chemin`,`cours_id`) VALUES(2,'ressource2','chemin ressource de cours 2',1)");
		stmt.executeUpdate("INSERT INTO `ressource`(`id`,`titre`,`chemin`,`tp_id`) VALUES(3,'ressource3','ressource de tp',1)");
		stmt.close();
		connection.close();
	}

	@Test
	public void testListerCours() {
		List<Ressource> listeRessources = ressourceDao.getRessourcesByCours(new Cours(1L, "titre", "desc", new Date()));

		Assert.assertEquals(2, listeRessources.size());

		Assert.assertEquals(1L, listeRessources.get(0).getId().longValue());
		Assert.assertEquals("ressource1", listeRessources.get(0).getTitre());
		Assert.assertEquals("chemin ressource de cours 1", listeRessources.get(0).getChemin());
		Assert.assertEquals("titre", listeRessources.get(0).getEnseignement().getTitre());

		Assert.assertEquals(2L, listeRessources.get(1).getId().longValue());
	}
}
