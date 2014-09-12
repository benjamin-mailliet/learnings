package learnings.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import learnings.dao.RessourceDao;
import learnings.model.Ressource;
import learnings.model.Seance;

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
		stmt.executeUpdate("DELETE FROM travailutilisateur");
		stmt.executeUpdate("DELETE FROM travail");
		stmt.executeUpdate("DELETE FROM seance");
		stmt.executeUpdate("INSERT INTO `seance`(`id`,`titre`,`description`,`date`, `type`) VALUES(1,'cours1','cours de debuggage','2014-08-26', 'COURS')");
		stmt.executeUpdate("INSERT INTO `ressource`(`id`,`titre`,`chemin`,`seance_id`) VALUES(1,'ressource1','chemin ressource de cours 1',1)");
		stmt.executeUpdate("INSERT INTO `ressource`(`id`,`titre`,`chemin`,`seance_id`) VALUES(2,'ressource2','chemin ressource de cours 2',1)");
		stmt.executeUpdate("INSERT INTO `ressource`(`id`,`titre`,`chemin`,`seance_id`) VALUES(3,'ressource3','ressource de tp',1)");
		stmt.close();
		connection.close();
	}

	@Test
	public void testListerCours() {
		List<Ressource> listeRessources = ressourceDao.getRessourcesBySeance(new Seance(1L, "titre", "desc", new Date()));

		Assert.assertEquals(3, listeRessources.size());

		Assert.assertEquals(1L, listeRessources.get(0).getId().longValue());
		Assert.assertEquals("ressource1", listeRessources.get(0).getTitre());
		Assert.assertEquals("chemin ressource de cours 1", listeRessources.get(0).getChemin());
		Assert.assertEquals("titre", listeRessources.get(0).getEnseignement().getTitre());

		Assert.assertEquals(2L, listeRessources.get(1).getId().longValue());
	}

	@Test
	public void testAjouterRessource() throws Exception {
		Ressource ressource = new Ressource(null, "monTitre", "/chemin/monFichier.zip", new Seance(1L, null, null, null));

		Ressource ressourceCreee = ressourceDao.ajouterRessource(ressource);

		Assert.assertNotNull(ressourceCreee);
		Assert.assertNotNull(ressourceCreee.getId());

		Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
		PreparedStatement stmt = connection.prepareStatement("SELECT * FROM ressource WHERE id=?");
		stmt.setLong(1, ressourceCreee.getId());
		ResultSet results = stmt.executeQuery();
		if (results.next()) {
			Assert.assertEquals(ressourceCreee.getId().longValue(), results.getLong("id"));
			Assert.assertEquals("monTitre", results.getString("titre"));
			Assert.assertEquals("/chemin/monFichier.zip", results.getString("chemin"));
			Assert.assertEquals(1L, results.getLong("seance_id"));
			Assert.assertNull(results.getString("projettransversal_id"));
		} else {
			Assert.fail();
		}
		stmt.close();
		connection.close();
	}

	@Test
	public void testGetRessource() {
		Ressource ressource = ressourceDao.getRessource(1L);

		Assert.assertEquals(1L, ressource.getId().longValue());
		Assert.assertEquals("ressource1", ressource.getTitre());
		Assert.assertEquals("chemin ressource de cours 1", ressource.getChemin());
		Assert.assertEquals("cours1", ressource.getEnseignement().getTitre());
		Assert.assertEquals(1L, ressource.getEnseignement().getId().longValue());
		Assert.assertEquals("cours de debuggage", ressource.getEnseignement().getDescription());
		Assert.assertEquals(new GregorianCalendar(2014, Calendar.AUGUST, 26).getTime(), ((Seance) ressource.getEnseignement()).getDate());
	}

	@Test
	public void testSupprimerRessource() throws Exception {
		ressourceDao.supprimerRessource(1L);

		Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		ResultSet results = stmt.executeQuery("SELECT * FROM ressource WHERE id=1");
		if (results.next()) {
			Assert.fail();
		}
	}
}
