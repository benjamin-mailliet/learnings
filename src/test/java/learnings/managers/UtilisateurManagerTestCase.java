package learnings.managers;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import learnings.dao.TravailDao;
import learnings.dao.UtilisateurDao;
import learnings.exceptions.LearningsSecuriteException;
import learnings.model.Travail;
import learnings.model.Utilisateur;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UtilisateurManagerTestCase {

	@Mock
	private UtilisateurDao utilisateurDao;

	@Mock
	private TravailDao travailDao;

	@Mock
	private MotDePasseManager motDePasseManager;

	@InjectMocks
	private UtilisateurManager utilisateurManager = new UtilisateurManager();

	@Before
	public void init() throws Exception {
		List<Travail> travaux = new ArrayList<Travail>();
		travaux.add(new Travail(1L, null, null, null, null));

		Mockito.when(travailDao.listerTravauxParUtilisateur(1L)).thenReturn(new ArrayList<Travail>());
		Mockito.when(travailDao.listerTravauxParUtilisateur(2L)).thenReturn(travaux);
		Mockito.when(utilisateurDao.getMotDePasseUtilisateurHashe(Mockito.eq("email1"))).thenReturn("motDePasseHash");
		Mockito.when(utilisateurDao.getUtilisateur(Mockito.eq(1L))).thenReturn(new Utilisateur(1L, "email1", false));
		Mockito.when(utilisateurDao.getUtilisateur(Mockito.eq(2L))).thenReturn(new Utilisateur(2L, "email2", false));
		Mockito.when(utilisateurDao.getUtilisateur(Mockito.eq("email1"))).thenReturn(new Utilisateur(1L, "email1", false));
		Mockito.when(utilisateurDao.ajouterUtilisateur(Mockito.eq("email3"), Mockito.eq("email3Hash"), Mockito.eq(true))).thenReturn(
				new Utilisateur(3L, "email3", true));

		Mockito.when(motDePasseManager.validerMotDePasse(Mockito.eq("motDePasse"), Mockito.eq("motDePasseHash"))).thenReturn(true);
		Mockito.when(motDePasseManager.validerMotDePasse(Mockito.eq("hashException"), Mockito.eq("motDePasseHash"))).thenThrow(new NoSuchAlgorithmException());
		Mockito.when(motDePasseManager.genererMotDePasse(Mockito.eq("email1"))).thenReturn("email1Hash");
		Mockito.when(motDePasseManager.genererMotDePasse(Mockito.eq("email2"))).thenThrow(new NoSuchAlgorithmException());
		Mockito.when(motDePasseManager.genererMotDePasse(Mockito.eq("email3"))).thenReturn("email3Hash");

	}

	@Test
	public void testListerUtilisateurs() {
		utilisateurManager.listerUtilisateurs();
		Mockito.verify(utilisateurDao).listerUtilisateurs();
	}

	@Test
	public void testListerAutresElevesOK() {
		utilisateurManager.listerAutresEleves(1L);
		Mockito.verify(utilisateurDao).listerAutresEleves(Mockito.eq(1L));
		Mockito.verify(utilisateurDao).listerAutresEleves(Mockito.anyLong());
	}

	@Test
	public void testListerAutresElevesKOIdNull() {
		try {
			utilisateurManager.listerAutresEleves(null);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Mockito.verify(utilisateurDao, Mockito.never()).listerAutresEleves(Mockito.anyLong());
		}
	}

	@Test
	public void testGetUtilisateurParMail() {
		utilisateurManager.getUtilisateur("email");
		Mockito.verify(utilisateurDao).getUtilisateur(Mockito.eq("email"));
		Mockito.verify(utilisateurDao).getUtilisateur(Mockito.anyString());
	}

	@Test
	public void testGetUtilisateurParMailKOEmailNull() {
		try {
			utilisateurManager.getUtilisateur(null);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Mockito.verify(utilisateurDao, Mockito.never()).getUtilisateur(Mockito.anyString());
		}
	}

	@Test
	public void testGetUtilisateurParMailKOEmailVide() {
		try {
			utilisateurManager.getUtilisateur("");
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Mockito.verify(utilisateurDao, Mockito.never()).getUtilisateur(Mockito.anyString());
		}
	}

	@Test
	public void testSupprimerUtilisateur() {
		utilisateurManager.supprimerUtilisateur(1L);
		Mockito.verify(utilisateurDao).supprimerUtilisateur(Mockito.eq(1L));
		Mockito.verify(utilisateurDao).supprimerUtilisateur(Mockito.anyLong());
	}

	@Test
	public void testSupprimerUtilisateurKOIdNull() {
		try {
			utilisateurManager.supprimerUtilisateur(null);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'id de l'utilisateur ne peut pas être null.", e.getMessage());
			Mockito.verify(utilisateurDao, Mockito.never()).supprimerUtilisateur(Mockito.anyLong());
		}
	}

	@Test
	public void testSupprimerUtilisateurKOTravauxExistant() {
		try {
			utilisateurManager.supprimerUtilisateur(2L);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Impossible de supprimer un utilisateur avec des travaux rendus.", e.getMessage());
			Mockito.verify(utilisateurDao, Mockito.never()).supprimerUtilisateur(Mockito.anyLong());
		}
	}

	@Test
	public void testEnleverDroitAdmin() {
		utilisateurManager.enleverDroitsAdmin(1L);
		Mockito.verify(utilisateurDao).modifierRoleAdmin(Mockito.eq(1L), Mockito.eq(false));
		Mockito.verify(utilisateurDao).modifierRoleAdmin(Mockito.anyLong(), Mockito.anyBoolean());
	}

	@Test
	public void testEnleverDroitAdminKOIdNull() {
		try {
			utilisateurManager.enleverDroitsAdmin(null);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'id de l'utilisateur ne peut pas être null.", e.getMessage());
			Mockito.verify(utilisateurDao, Mockito.never()).modifierRoleAdmin(Mockito.anyLong(), Mockito.anyBoolean());
		}
	}

	@Test
	public void testDonnerDroitAdmin() {
		utilisateurManager.donnerDroitsAdmin(1L);
		Mockito.verify(utilisateurDao).modifierRoleAdmin(Mockito.eq(1L), Mockito.eq(true));
		Mockito.verify(utilisateurDao).modifierRoleAdmin(Mockito.anyLong(), Mockito.anyBoolean());
	}

	@Test
	public void testDonnerDroitAdminKOIdNull() {
		try {
			utilisateurManager.donnerDroitsAdmin(null);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'id de l'utilisateur ne peut pas être null.", e.getMessage());
			Mockito.verify(utilisateurDao, Mockito.never()).modifierRoleAdmin(Mockito.anyLong(), Mockito.anyBoolean());
		}
	}

	@Test
	public void testValiderMotDePasseOK() throws Exception {
		Assert.assertTrue(utilisateurManager.validerMotDePasse("email1", "motDePasse"));
		Mockito.verify(utilisateurDao).getMotDePasseUtilisateurHashe(Mockito.eq("email1"));
		Mockito.verify(utilisateurDao).getMotDePasseUtilisateurHashe(Mockito.anyString());

		Mockito.verify(motDePasseManager).validerMotDePasse("motDePasse", "motDePasseHash");
		Mockito.verify(motDePasseManager).validerMotDePasse(Mockito.anyString(), Mockito.anyString());
	}

	@Test
	public void testValiderMotDePasseKOEmail() throws Exception {
		try {
			utilisateurManager.validerMotDePasse(null, "motDePasse");
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'identifiant doit être renseigné.", e.getMessage());
			Mockito.verify(utilisateurDao, Mockito.never()).getMotDePasseUtilisateurHashe(Mockito.anyString());
			Mockito.verify(motDePasseManager, Mockito.never()).validerMotDePasse(Mockito.anyString(), Mockito.anyString());
		}

		try {
			utilisateurManager.validerMotDePasse("", "motDePasse");
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'identifiant doit être renseigné.", e.getMessage());
			Mockito.verify(utilisateurDao, Mockito.never()).getMotDePasseUtilisateurHashe(Mockito.anyString());
			Mockito.verify(motDePasseManager, Mockito.never()).validerMotDePasse(Mockito.anyString(), Mockito.anyString());
		}
	}

	@Test
	public void testValiderMotDePasseKOMotDePasse() throws Exception {
		try {
			utilisateurManager.validerMotDePasse("email1", null);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Le mot de passe doit être renseigné.", e.getMessage());
			Mockito.verify(utilisateurDao, Mockito.never()).getMotDePasseUtilisateurHashe(Mockito.anyString());
			Mockito.verify(motDePasseManager, Mockito.never()).validerMotDePasse(Mockito.anyString(), Mockito.anyString());
		}

		try {
			utilisateurManager.validerMotDePasse("email1", "");
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Le mot de passe doit être renseigné.", e.getMessage());
			Mockito.verify(utilisateurDao, Mockito.never()).getMotDePasseUtilisateurHashe(Mockito.anyString());
			Mockito.verify(motDePasseManager, Mockito.never()).validerMotDePasse(Mockito.anyString(), Mockito.anyString());
		}
	}

	@Test
	public void testValiderMotDePasseKOEmailInexistant() throws Exception {
		try {
			utilisateurManager.validerMotDePasse("email2", "motDePasse");
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'identifiant n'est pas connu.", e.getMessage());
			Mockito.verify(utilisateurDao).getMotDePasseUtilisateurHashe("email2");
			Mockito.verify(utilisateurDao).getMotDePasseUtilisateurHashe(Mockito.anyString());
			Mockito.verify(motDePasseManager, Mockito.never()).validerMotDePasse(Mockito.anyString(), Mockito.anyString());
		}
	}

	@Test
	public void testValiderMotDePasseKOSecurityException() throws Exception {
		try {
			utilisateurManager.validerMotDePasse("email1", "hashException");
			Assert.fail();
		} catch (LearningsSecuriteException e) {
			Assert.assertEquals("Problème dans la vérification du mot de passe.", e.getMessage());
			Mockito.verify(utilisateurDao).getMotDePasseUtilisateurHashe("email1");
			Mockito.verify(utilisateurDao).getMotDePasseUtilisateurHashe(Mockito.anyString());
			Mockito.verify(motDePasseManager).validerMotDePasse(Mockito.eq("hashException"), Mockito.eq("motDePasseHash"));
			Mockito.verify(motDePasseManager).validerMotDePasse(Mockito.anyString(), Mockito.anyString());
		}
	}

	@Test
	public void testReinitialiserMotDePasse() throws Exception {
		utilisateurManager.reinitialiserMotDePasse(1L);
		Mockito.verify(utilisateurDao).getUtilisateur(Mockito.eq(1L));
		Mockito.verify(utilisateurDao).getUtilisateur(Mockito.anyLong());

		Mockito.verify(motDePasseManager).genererMotDePasse(Mockito.eq("email1"));
		Mockito.verify(motDePasseManager).genererMotDePasse(Mockito.anyString());

		Mockito.verify(utilisateurDao).modifierMotDePasse(Mockito.eq(1L), Mockito.eq("email1Hash"));
		Mockito.verify(utilisateurDao).modifierMotDePasse(Mockito.anyLong(), Mockito.anyString());

	}

	@Test
	public void testReinitialiserMotDePasseKOIdNull() throws Exception {
		try {
			utilisateurManager.reinitialiserMotDePasse(null);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'identifiant de l'utilisateur ne peut pas être null.", e.getMessage());
			Mockito.verify(utilisateurDao, Mockito.never()).getUtilisateur(Mockito.anyLong());
			Mockito.verify(motDePasseManager, Mockito.never()).genererMotDePasse(Mockito.anyString());
			Mockito.verify(utilisateurDao, Mockito.never()).modifierMotDePasse(Mockito.anyLong(), Mockito.anyString());
		}
	}

	@Test
	public void testReinitialiserMotDePasseKOPatientNull() throws Exception {
		try {
			utilisateurManager.reinitialiserMotDePasse(-1L);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'utilisateur n'est pas connu.", e.getMessage());
			Mockito.verify(utilisateurDao).getUtilisateur(Mockito.eq(-1L));
			Mockito.verify(utilisateurDao).getUtilisateur(Mockito.anyLong());
			Mockito.verify(motDePasseManager, Mockito.never()).genererMotDePasse(Mockito.anyString());
			Mockito.verify(utilisateurDao, Mockito.never()).modifierMotDePasse(Mockito.anyLong(), Mockito.anyString());
		}
	}

	@Test
	public void testReinitialiserMotDePasseKOExceptionSecurite() throws Exception {
		try {
			utilisateurManager.reinitialiserMotDePasse(2L);
			Assert.fail();
		} catch (LearningsSecuriteException e) {
			Assert.assertEquals("Problème dans la génération du mot de passe.", e.getMessage());
			Mockito.verify(utilisateurDao).getUtilisateur(Mockito.eq(2L));
			Mockito.verify(utilisateurDao).getUtilisateur(Mockito.anyLong());
			Mockito.verify(motDePasseManager).genererMotDePasse(Mockito.eq("email2"));
			Mockito.verify(motDePasseManager).genererMotDePasse(Mockito.anyString());
			Mockito.verify(utilisateurDao, Mockito.never()).modifierMotDePasse(Mockito.anyLong(), Mockito.anyString());
		}
	}

	@Test
	public void testAjouterUtilisateur() throws Exception {
		Utilisateur utilisateur = utilisateurManager.ajouterUtilisateur("email3", true);
		Assert.assertEquals(3L, utilisateur.getId().longValue());
		Assert.assertEquals("email3", utilisateur.getEmail());
		Assert.assertTrue(utilisateur.isAdmin());
		Mockito.verify(utilisateurDao).getUtilisateur(Mockito.eq("email3"));
		Mockito.verify(utilisateurDao).getUtilisateur(Mockito.anyString());
		Mockito.verify(motDePasseManager).genererMotDePasse(Mockito.eq("email3"));
		Mockito.verify(motDePasseManager).genererMotDePasse(Mockito.anyString());
		Mockito.verify(utilisateurDao).ajouterUtilisateur(Mockito.eq("email3"), Mockito.eq("email3Hash"), Mockito.eq(true));
		Mockito.verify(utilisateurDao).ajouterUtilisateur(Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean());
	}

	@Test
	public void testAjouterUtilisateurKOEmailNull() throws Exception {
		try {
			utilisateurManager.ajouterUtilisateur(null, true);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'identifiant doit être renseigné.", e.getMessage());
		}
		try {
			utilisateurManager.ajouterUtilisateur("", true);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'identifiant doit être renseigné.", e.getMessage());
		}
		Mockito.verify(utilisateurDao, Mockito.never()).getUtilisateur(Mockito.anyString());
		Mockito.verify(motDePasseManager, Mockito.never()).genererMotDePasse(Mockito.anyString());
		Mockito.verify(utilisateurDao, Mockito.never()).ajouterUtilisateur(Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean());

	}

	@Test
	public void testAjouterUtilisateurKOUtilisateurExistant() throws Exception {
		try {
			utilisateurManager.ajouterUtilisateur("email1", true);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'identifiant est déjà utilisé.", e.getMessage());
			Mockito.verify(utilisateurDao).getUtilisateur(Mockito.eq("email1"));
			Mockito.verify(utilisateurDao).getUtilisateur(Mockito.anyString());
			Mockito.verify(motDePasseManager, Mockito.never()).genererMotDePasse(Mockito.anyString());
			Mockito.verify(utilisateurDao, Mockito.never()).ajouterUtilisateur(Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean());
		}
	}

	@Test
	public void testAjouterUtilisateurKOExceptionSecurite() throws Exception {
		try {
			utilisateurManager.ajouterUtilisateur("email2", true);
			Assert.fail();
		} catch (LearningsSecuriteException e) {
			Assert.assertEquals("Problème dans la génération du mot de passe.", e.getMessage());
			Mockito.verify(utilisateurDao).getUtilisateur(Mockito.eq("email2"));
			Mockito.verify(utilisateurDao).getUtilisateur(Mockito.anyString());
			Mockito.verify(motDePasseManager).genererMotDePasse(Mockito.eq("email2"));
			Mockito.verify(motDePasseManager).genererMotDePasse(Mockito.anyString());
			Mockito.verify(utilisateurDao, Mockito.never()).ajouterUtilisateur(Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean());
		}
	}

	@Test
	public void testModifierMotDePasseOK() throws Exception {
		utilisateurManager.modifierMotDePasse(1L, "email1", "email1");
		Mockito.verify(motDePasseManager).genererMotDePasse(Mockito.eq("email1"));
		Mockito.verify(motDePasseManager).genererMotDePasse(Mockito.anyString());
		Mockito.verify(utilisateurDao).modifierMotDePasse(Mockito.eq(1L), Mockito.eq("email1Hash"));
		Mockito.verify(utilisateurDao).modifierMotDePasse(Mockito.anyLong(), Mockito.anyString());
	}

	@Test
	public void testModifierMotDePasseKONull() throws Exception {
		try {
			utilisateurManager.modifierMotDePasse(1L, null, "email1");
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Les mots de passe doivent être renseignés.", e.getMessage());
		}
		try {
			utilisateurManager.modifierMotDePasse(1L, "", "email1");
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Les mots de passe doivent être renseignés.", e.getMessage());
		}
		try {
			utilisateurManager.modifierMotDePasse(1L, "email1", null);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Les mots de passe doivent être renseignés.", e.getMessage());
		}
		try {
			utilisateurManager.modifierMotDePasse(1L, "email1", "");
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Les mots de passe doivent être renseignés.", e.getMessage());
		}
		Mockito.verify(motDePasseManager, Mockito.never()).genererMotDePasse(Mockito.anyString());
		Mockito.verify(utilisateurDao, Mockito.never()).modifierMotDePasse(Mockito.anyLong(), Mockito.anyString());
	}

	@Test
	public void testModifierMotDePasseKOMotDePassesDifferents() throws Exception {
		try {
			utilisateurManager.modifierMotDePasse(1L, "email1", "email2");
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("La confirmation du mot de passe ne correspond pas.", e.getMessage());
			Mockito.verify(motDePasseManager, Mockito.never()).genererMotDePasse(Mockito.anyString());
			Mockito.verify(utilisateurDao, Mockito.never()).modifierMotDePasse(Mockito.anyLong(), Mockito.anyString());
		}
	}

	@Test
	public void testModifierMotDePasseKOExceptionSecurite() throws Exception {
		try {
			utilisateurManager.modifierMotDePasse(1L, "email2", "email2");
			Assert.fail();
		} catch (LearningsSecuriteException e) {
			Assert.assertEquals("Problème dans la génération du mot de passe.", e.getMessage());
			Mockito.verify(motDePasseManager).genererMotDePasse(Mockito.eq("email2"));
			Mockito.verify(motDePasseManager).genererMotDePasse(Mockito.anyString());
			Mockito.verify(utilisateurDao, Mockito.never()).modifierMotDePasse(Mockito.anyLong(), Mockito.anyString());
		}
	}

}
