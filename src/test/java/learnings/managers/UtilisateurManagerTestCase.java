package learnings.managers;

import java.util.ArrayList;
import java.util.List;

import learnings.dao.TravailDao;
import learnings.dao.UtilisateurDao;
import learnings.model.Travail;

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

	@InjectMocks
	private UtilisateurManager utilisateurManager = new UtilisateurManager();

	@Before
	public void init() {
		Mockito.when(travailDao.listerTravauxParUtilisateur(1L)).thenReturn(new ArrayList<Travail>());
		List<Travail> travaux = new ArrayList<Travail>();
		travaux.add(new Travail(1L, null, null, null, null));
		Mockito.when(travailDao.listerTravauxParUtilisateur(2L)).thenReturn(travaux);
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

}
