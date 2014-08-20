package learnings.dao.impl;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import learnings.dao.UtilisateurDao;
import learnings.model.Utilisateur;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UtilisateurDaoTestCase {
	private UtilisateurDao utilisateurDao = new UtilisateurDaoImpl(DataSourceTestProvider.getDataSource());

	@Before
	public void init() throws Exception {
		Connection connection = DataSourceTestProvider.getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("DELETE FROM utilisateur");
		stmt.executeUpdate("INSERT INTO `utilisateur`(`id`,`email`,`motdepasse`,`admin`) VALUES(1,'eleve@learnings-devwebhei.fr','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0)");
		stmt.executeUpdate("INSERT INTO `utilisateur`(`id`,`email`,`motdepasse`,`admin`) VALUES(2,'admin@learnings-devwebhei.fr','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',1)");
		stmt.close();
		connection.close();
	}

	@Test
	public void testListerUtilisateurs() {
		List<Utilisateur> utilisateurs = utilisateurDao.listerUtilisateurs();

		Assert.assertEquals(2L, utilisateurs.get(0).getId().longValue());
		Assert.assertEquals("admin@learnings-devwebhei.fr", utilisateurs.get(0).getEmail());
		Assert.assertTrue(utilisateurs.get(0).isAdmin());

		Assert.assertEquals(1L, utilisateurs.get(1).getId().longValue());
		Assert.assertEquals("eleve@learnings-devwebhei.fr", utilisateurs.get(1).getEmail());
		Assert.assertFalse(utilisateurs.get(1).isAdmin());
	}

	@Test
	public void testGetUtilisateurOK() {
		Utilisateur utilisateur = utilisateurDao.getUtilisateur("eleve@learnings-devwebhei.fr");
		Assert.assertEquals(1L, utilisateur.getId().longValue());
		Assert.assertEquals("eleve@learnings-devwebhei.fr", utilisateur.getEmail());
		Assert.assertFalse(utilisateur.isAdmin());
	}

	@Test
	public void testGetUtilisateurNonTrouve() {
		Utilisateur utilisateur = utilisateurDao.getUtilisateur("nonexistant@learnings-devwebhei.fr");
		Assert.assertNull(utilisateur);
	}

	@Test
	public void testGetMotDePasseOK() {
		String motDePasse = utilisateurDao.getMotDePasseUtilisateurHashe("eleve@learnings-devwebhei.fr");
		Assert.assertEquals("6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81", motDePasse);
	}

	@Test
	public void testGetMotDePasseNonTrouve() {
		String motDePasse = utilisateurDao.getMotDePasseUtilisateurHashe("nonexistant@learnings-devwebhei.fr");
		Assert.assertNull(motDePasse);
	}

}
