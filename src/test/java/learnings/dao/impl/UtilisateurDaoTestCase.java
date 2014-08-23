package learnings.dao.impl;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import learnings.dao.DataSourceProvider;
import learnings.dao.UtilisateurDao;
import learnings.model.Utilisateur;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UtilisateurDaoTestCase {
	private UtilisateurDao utilisateurDao = new UtilisateurDaoImpl();

	@Before
	public void init() throws Exception {
		Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
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

		Assert.assertEquals(2, utilisateurs.size());

		Assert.assertEquals(2L, utilisateurs.get(0).getId().longValue());
		Assert.assertEquals("admin@learnings-devwebhei.fr", utilisateurs.get(0).getEmail());
		Assert.assertTrue(utilisateurs.get(0).isAdmin());

		Assert.assertEquals(1L, utilisateurs.get(1).getId().longValue());
		Assert.assertEquals("eleve@learnings-devwebhei.fr", utilisateurs.get(1).getEmail());
		Assert.assertFalse(utilisateurs.get(1).isAdmin());
	}

	@Test
	public void testGetUtilisateurParIdOK() {
		Utilisateur utilisateur = utilisateurDao.getUtilisateur(1L);
		Assert.assertEquals(1L, utilisateur.getId().longValue());
		Assert.assertEquals("eleve@learnings-devwebhei.fr", utilisateur.getEmail());
		Assert.assertFalse(utilisateur.isAdmin());
	}

	@Test
	public void testGetUtilisateurParIdNonTrouve() {
		Utilisateur utilisateur = utilisateurDao.getUtilisateur(3L);
		Assert.assertNull(utilisateur);
	}

	@Test
	public void testGetUtilisateurParEmailOK() {
		Utilisateur utilisateur = utilisateurDao.getUtilisateur("eleve@learnings-devwebhei.fr");
		Assert.assertEquals(1L, utilisateur.getId().longValue());
		Assert.assertEquals("eleve@learnings-devwebhei.fr", utilisateur.getEmail());
		Assert.assertFalse(utilisateur.isAdmin());
	}

	@Test
	public void testGetUtilisateurParEmailNonTrouve() {
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

	@Test
	public void testSupprimerUtilisateur() {
		Assert.assertNotNull(utilisateurDao.getUtilisateur(1L));
		utilisateurDao.supprimerUtilisateur(1L);
		Assert.assertNull(utilisateurDao.getUtilisateur(1L));
	}

	@Test
	public void testChangerRoleAdmin() {
		Assert.assertFalse(utilisateurDao.getUtilisateur(1L).isAdmin());
		utilisateurDao.modifierRoleAdmin(1L, true);
		Assert.assertTrue(utilisateurDao.getUtilisateur(1L).isAdmin());
		utilisateurDao.modifierRoleAdmin(1L, true);
		Assert.assertTrue(utilisateurDao.getUtilisateur(1L).isAdmin());
	}

	@Test
	public void testChangerMotDePasse() {
		Assert.assertEquals("6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81",
				utilisateurDao.getMotDePasseUtilisateurHashe("eleve@learnings-devwebhei.fr"));
		utilisateurDao.modifierMotDePasse(1L, "NOUVEAU_MOT_DE_PASSE");
		Assert.assertEquals("NOUVEAU_MOT_DE_PASSE", utilisateurDao.getMotDePasseUtilisateurHashe("eleve@learnings-devwebhei.fr"));
	}

	@Test
	public void testAjouterUtilisateur() {
		Utilisateur utilisateur = utilisateurDao.ajouterUtilisateur("email", "motDePasse", true);
		Assert.assertNotNull(utilisateur.getId());
		Assert.assertEquals("email", utilisateur.getEmail());
		Assert.assertTrue(utilisateur.isAdmin());

		Utilisateur utilisateurVerif = utilisateurDao.getUtilisateur("email");
		Assert.assertEquals(utilisateur.getId(), utilisateurVerif.getId());
	}

}
