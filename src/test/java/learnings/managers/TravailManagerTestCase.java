package learnings.managers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import learnings.dao.SeanceDao;
import learnings.dao.TravailDao;
import learnings.dao.UtilisateurDao;
import learnings.enums.TypeSeance;
import learnings.exceptions.LearningsException;
import learnings.model.Seance;
import learnings.model.Travail;
import learnings.model.Utilisateur;
import learnings.pojos.FichierComplet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TravailManagerTestCase {

	@Mock
	private SeanceDao seanceDao;

	@Mock
	private UtilisateurDao utilisateurDao;

	@Mock
	private TravailDao travailDao;

	@Mock
	private FichierManager fichierManager;

	@InjectMocks
	private TravailManager travailManager = new TravailManager();

	private Seance seance1 = new Seance(1L, "seance1", "description1", new GregorianCalendar(2014, Calendar.SEPTEMBER, 5).getTime(), true,
			new GregorianCalendar(3000, Calendar.SEPTEMBER, 5).getTime(), TypeSeance.TP);
	private Seance seanceNonNotee = new Seance(2L, "seance1", "description1", new GregorianCalendar(2014, Calendar.SEPTEMBER, 5).getTime(), false,
			new GregorianCalendar(3000, Calendar.SEPTEMBER, 5).getTime(), TypeSeance.TP);
	private Seance seanceCours = new Seance(3L, "seance1", "description1", new GregorianCalendar(2014, Calendar.SEPTEMBER, 5).getTime(), true,
			new GregorianCalendar(3000, Calendar.SEPTEMBER, 5).getTime(), TypeSeance.COURS);
	private Seance seanceDepassee = new Seance(4L, "seance1", "description1", new GregorianCalendar(2014, Calendar.SEPTEMBER, 1).getTime(), true,
			new GregorianCalendar(2014, Calendar.SEPTEMBER, 1).getTime(), TypeSeance.TP);
	private Seance seanceFuture = new Seance(5L, "seance1", "description1", new GregorianCalendar(3000, Calendar.SEPTEMBER, 1).getTime(), true,
			new GregorianCalendar(3000, Calendar.SEPTEMBER, 1).getTime(), TypeSeance.TP);
	private Travail travail1 = new Travail(1L, seance1, null, new GregorianCalendar(2014, Calendar.SEPTEMBER, 5, 12, 50).getTime(),
			"/chemin/12346578-fichier1.zip");
	private Travail travail1SansId = new Travail(null, seance1, null, new GregorianCalendar(2014, Calendar.SEPTEMBER, 5, 12, 50).getTime(),
			"/chemin/12346578-fichier1.zip");
	private Travail travail2 = new Travail(2L, seance1, null, new GregorianCalendar(2014, Calendar.SEPTEMBER, 6, 13, 51).getTime(),
			"/chemin/12346578-fichier2.zip");
	private Travail travail3 = new Travail(3L, seance1, null, new GregorianCalendar(2014, Calendar.SEPTEMBER, 6, 13, 51).getTime(),
			"/chemin/12346578-fichier3.zip");

	private InputStream inputStream1 = new ByteArrayInputStream(new byte[] {});
	private InputStream inputStream2 = new ByteArrayInputStream(new byte[] {});
	private InputStream inputStreamEx = new ByteArrayInputStream(new byte[] {});

	private Utilisateur utilisateur1 = new Utilisateur(1L, "email1", false);
	private Utilisateur utilisateur2 = new Utilisateur(2L, "email2", false);
	private Utilisateur utilisateurAdmin = new Utilisateur(0L, "email2", true);

	@Before
	public void init() throws Exception {
		Mockito.when(travailDao.getTravail(Mockito.eq(1L))).thenReturn(travail1);
		Mockito.when(travailDao.ajouterTravail(Mockito.eq(travail1SansId))).thenReturn(travail1);
		Mockito.when(fichierManager.getFichier(Mockito.eq("/chemin/12346578-fichier1.zip"))).thenReturn(inputStream1);
		Mockito.when(fichierManager.getFichier(Mockito.eq("/chemin/12346578-fichier2.zip"))).thenReturn(inputStream2);

		Mockito.doThrow(new LearningsException()).when(fichierManager).supprimerFichier(Mockito.eq("/chemin/12346578-fichier2.zip"));
		Mockito.doThrow(new LearningsException()).when(fichierManager).ajouterFichier(Mockito.anyString(), Mockito.eq(inputStreamEx));

		Mockito.when(travailDao.getTravailUtilisateurParSeance(Mockito.eq(1L), Mockito.eq(1L))).thenReturn(travail1);
		Mockito.when(travailDao.getTravailUtilisateurParSeance(Mockito.eq(1L), Mockito.eq(2L))).thenReturn(travail1);
		Mockito.when(travailDao.getTravailUtilisateurParSeance(Mockito.eq(10L), Mockito.eq(1L))).thenReturn(travail1);
		Mockito.when(travailDao.getTravailUtilisateurParSeance(Mockito.eq(10L), Mockito.eq(2L))).thenReturn(travail1);
		List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();
		utilisateurs.add(utilisateur1);
		utilisateurs.add(utilisateur2);
		Mockito.when(travailDao.listerUtilisateurs(Mockito.eq(1L))).thenReturn(utilisateurs);

		Mockito.when(travailDao.getTravailUtilisateurParSeance(Mockito.eq(11L), Mockito.eq(1L))).thenReturn(travail1);
		Mockito.when(travailDao.getTravailUtilisateurParSeance(Mockito.eq(11L), Mockito.eq(2L))).thenReturn(travail2);

		Mockito.when(travailDao.getTravailUtilisateurParSeance(Mockito.eq(12L), Mockito.eq(1L))).thenReturn(travail2);
		List<Utilisateur> utilisateurs2 = new ArrayList<Utilisateur>();
		utilisateurs2.add(utilisateur1);
		Mockito.when(travailDao.listerUtilisateurs(Mockito.eq(2L))).thenReturn(utilisateurs2);

		Mockito.when(utilisateurDao.getUtilisateur(Mockito.eq(0L))).thenReturn(utilisateurAdmin);
		Mockito.when(utilisateurDao.getUtilisateur(Mockito.eq(1L))).thenReturn(utilisateur1);
		Mockito.when(utilisateurDao.getUtilisateur(Mockito.eq(2L))).thenReturn(utilisateur2);

		Mockito.when(seanceDao.getSeance(Mockito.eq(1L))).thenReturn(seance1);
		Mockito.when(seanceDao.getSeance(Mockito.eq(2L))).thenReturn(seanceNonNotee);
		Mockito.when(seanceDao.getSeance(Mockito.eq(3L))).thenReturn(seanceCours);
		Mockito.when(seanceDao.getSeance(Mockito.eq(4L))).thenReturn(seanceDepassee);
		Mockito.when(seanceDao.getSeance(Mockito.eq(5L))).thenReturn(seanceFuture);

	}

	@Test
	public void testListerSeances() throws Exception {
		FichierComplet fichier = travailManager.getFichierTravail(1L);

		Assert.assertEquals("fichier1.zip", fichier.getNom());
		Assert.assertEquals(inputStream1, fichier.getDonnees());

		Mockito.verify(travailDao).getTravail(Mockito.eq(1L));
		Mockito.verify(travailDao).getTravail(Mockito.anyLong());
		Mockito.verify(fichierManager).getFichier(Mockito.eq("/chemin/12346578-fichier1.zip"));
		Mockito.verify(fichierManager).getFichier(Mockito.anyString());
	}

	@Test
	public void testModifierTravailOK() throws Exception {
		travailManager.modifierTravail(inputStream2, travail1, travail2);
		Mockito.verify(fichierManager).supprimerFichier(Mockito.eq("/chemin/12346578-fichier1.zip"));
		Mockito.verify(fichierManager).supprimerFichier(Mockito.anyString());
		Mockito.verify(fichierManager).ajouterFichier(Mockito.eq("/chemin/12346578-fichier2.zip"), Mockito.eq(inputStream2));
		Mockito.verify(fichierManager).ajouterFichier(Mockito.anyString(), Mockito.any(InputStream.class));
		Mockito.verify(travailDao).mettreAJourTravail(Mockito.eq(1L), Mockito.any(Date.class), Mockito.eq("/chemin/12346578-fichier2.zip"));
		Mockito.verify(travailDao).mettreAJourTravail(Mockito.anyLong(), Mockito.any(Date.class), Mockito.anyString());
	}

	@Test
	public void testModifierTravailKOException() throws Exception {
		try {
			travailManager.modifierTravail(inputStream1, travail2, travail1);
			Assert.fail();
		} catch (LearningsException e) {
			Mockito.verify(fichierManager).supprimerFichier(Mockito.eq("/chemin/12346578-fichier2.zip"));
			Mockito.verify(fichierManager).supprimerFichier(Mockito.anyString());
			Mockito.verify(fichierManager, Mockito.never()).ajouterFichier(Mockito.anyString(), Mockito.any(InputStream.class));
			Mockito.verify(travailDao, Mockito.never()).mettreAJourTravail(Mockito.anyLong(), Mockito.any(Date.class), Mockito.anyString());
		}

	}

	@Test
	public void testAjouterTravailOK() throws Exception {
		travailManager.ajouterTravail(inputStream1, utilisateur1, utilisateur2, travail1SansId);

		Mockito.verify(fichierManager).ajouterFichier(Mockito.eq("/chemin/12346578-fichier1.zip"), Mockito.eq(inputStream1));
		Mockito.verify(fichierManager).ajouterFichier(Mockito.anyString(), Mockito.any(InputStream.class));
		Mockito.verify(travailDao).ajouterTravail(Mockito.eq(travail1SansId));
		Mockito.verify(travailDao).ajouterTravail(Mockito.any(Travail.class));
		Mockito.verify(travailDao).ajouterUtilisateur(Mockito.eq(1L), Mockito.eq(1L));
		Mockito.verify(travailDao).ajouterUtilisateur(Mockito.eq(1L), Mockito.eq(2L));
		Mockito.verify(travailDao, Mockito.times(2)).ajouterUtilisateur(Mockito.anyLong(), Mockito.anyLong());
	}

	@Test
	public void testAjouterTravailOKUtilisateur2Null() throws Exception {
		travailManager.ajouterTravail(inputStream1, utilisateur1, null, travail1SansId);

		Mockito.verify(fichierManager).ajouterFichier(Mockito.eq("/chemin/12346578-fichier1.zip"), Mockito.eq(inputStream1));
		Mockito.verify(fichierManager).ajouterFichier(Mockito.anyString(), Mockito.any(InputStream.class));
		Mockito.verify(travailDao).ajouterTravail(Mockito.eq(travail1SansId));
		Mockito.verify(travailDao).ajouterTravail(Mockito.any(Travail.class));
		Mockito.verify(travailDao).ajouterUtilisateur(Mockito.eq(1L), Mockito.eq(1L));
		Mockito.verify(travailDao).ajouterUtilisateur(Mockito.anyLong(), Mockito.anyLong());
	}

	@Test
	public void testAjouterTravailKOException() throws Exception {
		try {
			travailManager.ajouterTravail(inputStreamEx, utilisateur1, utilisateur2, travail1SansId);
			Assert.fail();
		} catch (LearningsException e) {
			Mockito.verify(fichierManager).ajouterFichier(Mockito.eq("/chemin/12346578-fichier1.zip"), Mockito.eq(inputStreamEx));
			Mockito.verify(fichierManager).ajouterFichier(Mockito.anyString(), Mockito.any(InputStream.class));
			Mockito.verify(travailDao, Mockito.never()).ajouterTravail(Mockito.any(Travail.class));
			Mockito.verify(travailDao, Mockito.never()).ajouterUtilisateur(Mockito.anyLong(), Mockito.anyLong());
		}
	}

	@Test
	public void testVerifierExistanceTravailOKTravailExistant() throws Exception {
		Travail travail = travailManager.verifierExistanceTravail(10L, 1L, 2L);
		Assert.assertEquals(travail, travail1);
		Mockito.verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(10L), Mockito.eq(1L));
		Mockito.verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(10L), Mockito.eq(2L));
		Mockito.verify(travailDao, Mockito.times(2)).getTravailUtilisateurParSeance(Mockito.anyLong(), Mockito.anyLong());
	}

	@Test
	public void testVerifierExistanceTravailOKTravailNonExistant() throws Exception {
		Travail travail = travailManager.verifierExistanceTravail(10L, 3L, 4L);
		Assert.assertNull(travail);
		Mockito.verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(10L), Mockito.eq(3L));
		Mockito.verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(10L), Mockito.eq(4L));
		Mockito.verify(travailDao, Mockito.times(2)).getTravailUtilisateurParSeance(Mockito.anyLong(), Mockito.anyLong());
	}

	@Test
	public void testVerifierExistanceTravailOKTravailExistantSolo() throws Exception {
		Travail travail = travailManager.verifierExistanceTravail(12L, 1L, null);
		Assert.assertEquals(travail, travail2);
		Mockito.verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(12L), Mockito.eq(1L));
		Mockito.verify(travailDao).getTravailUtilisateurParSeance(Mockito.anyLong(), Mockito.anyLong());
	}

	@Test
	public void testVerifierExistanceTravailOKTravailNonExistantSolo() throws Exception {
		Travail travail = travailManager.verifierExistanceTravail(12L, 2L, null);
		Assert.assertNull(travail);
		Mockito.verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(12L), Mockito.eq(2L));
		Mockito.verify(travailDao).getTravailUtilisateurParSeance(Mockito.anyLong(), Mockito.anyLong());
	}

	@Test
	public void testVerifierExistanceTravailKOBinomeEtNull() throws Exception {
		try {
			Travail travail = travailManager.verifierExistanceTravail(10L, 1L, 3L);
			Assert.fail();
		} catch (LearningsException e) {
			Assert.assertEquals("Un des deux utilisateurs a déjà rendu un travail avec un binôme différent.", e.getMessage());
			Mockito.verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(10L), Mockito.eq(1L));
			Mockito.verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(10L), Mockito.eq(3L));
			Mockito.verify(travailDao, Mockito.times(2)).getTravailUtilisateurParSeance(Mockito.anyLong(), Mockito.anyLong());
		}
	}

	@Test
	public void testVerifierExistanceTravailKOBinomeDifferent() throws Exception {
		try {
			Travail travail = travailManager.verifierExistanceTravail(11L, 1L, 2L);
			Assert.fail();
		} catch (LearningsException e) {
			Assert.assertEquals("Les deux utilisateur ont déjà rendu un travail dans des binômes différents.", e.getMessage());
			Mockito.verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(11L), Mockito.eq(1L));
			Mockito.verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(11L), Mockito.eq(2L));
			Mockito.verify(travailDao, Mockito.times(2)).getTravailUtilisateurParSeance(Mockito.anyLong(), Mockito.anyLong());
		}
	}

	@Test
	public void testVerifierExistanceTravailKOBinomeSplit() throws Exception {
		try {
			travailManager.verifierExistanceTravail(10L, 1L, null);
			Assert.fail();
		} catch (LearningsException e) {
			Assert.assertEquals("L'utilisateur a déjà rendu un travail avec un binôme différent.", e.getMessage());
			Mockito.verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(10L), Mockito.eq(1L));
			Mockito.verify(travailDao).getTravailUtilisateurParSeance(Mockito.anyLong(), Mockito.anyLong());
		}
	}

	@Test
	public void testVerifierUtilisateurAvantRenduOKExistant() {
		Utilisateur utilisateur = travailManager.verifierUtilisateurAvantRendu(1L, true);
		Assert.assertEquals(utilisateur1, utilisateur);
		Mockito.verify(utilisateurDao).getUtilisateur(Mockito.eq(1L));
		Mockito.verify(utilisateurDao).getUtilisateur(Mockito.anyLong());
	}

	@Test
	public void testVerifierUtilisateurAvantRenduOKIdNull() {
		Utilisateur utilisateur = travailManager.verifierUtilisateurAvantRendu(null, false);
		Assert.assertNull(utilisateur);
		Mockito.verify(utilisateurDao, Mockito.never()).getUtilisateur(Mockito.anyLong());
	}

	@Test
	public void testVerifierUtilisateurAvantRenduKOIdNull() {
		try {
			travailManager.verifierUtilisateurAvantRendu(null, true);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Un utilisateur obligatoire n'est pas renseigné.", e.getMessage());
			Mockito.verify(utilisateurDao, Mockito.never()).getUtilisateur(Mockito.anyLong());
		}
	}

	@Test
	public void testVerifierUtilisateurAvantRenduKOInconnu() {
		try {
			travailManager.verifierUtilisateurAvantRendu(3L, true);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Un utilisateur est inconnu.", e.getMessage());
			Mockito.verify(utilisateurDao).getUtilisateur(Mockito.eq(3L));
			Mockito.verify(utilisateurDao).getUtilisateur(Mockito.anyLong());
		}
	}

	@Test
	public void testVerifierUtilisateurAvantRenduKOAdmin() {
		try {
			travailManager.verifierUtilisateurAvantRendu(0L, true);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Un administrateur ne peut pas rendre de TP.", e.getMessage());
			Mockito.verify(utilisateurDao).getUtilisateur(Mockito.eq(0L));
			Mockito.verify(utilisateurDao).getUtilisateur(Mockito.anyLong());
		}
	}

	@Test
	public void testVerifierTpAvantRenduOK() {
		Seance seance = travailManager.verifierTpAvantRendu(1L);
		Assert.assertEquals(seance1, seance);
		Mockito.verify(seanceDao).getSeance(Mockito.eq(1L));
		Mockito.verify(seanceDao).getSeance(Mockito.anyLong());
	}

	@Test
	public void testVerifierTpAvantRenduKOIdNull() {
		try {
			travailManager.verifierTpAvantRendu(null);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'identifiant du tp est incorrect", e.getMessage());
			Mockito.verify(seanceDao, Mockito.never()).getSeance(Mockito.anyLong());
		}
	}

	@Test
	public void testVerifierTpAvantRenduKOSeanceInexistante() {
		try {
			travailManager.verifierTpAvantRendu(-1L);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'identifiant du tp est incorrect", e.getMessage());
			Mockito.verify(seanceDao).getSeance(Mockito.eq(-1L));
			Mockito.verify(seanceDao).getSeance(Mockito.anyLong());
		}
	}

	@Test
	public void testVerifierTpAvantRenduKOSeanceNonNotee() {
		try {
			travailManager.verifierTpAvantRendu(2L);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'identifiant du tp est incorrect", e.getMessage());
			Mockito.verify(seanceDao).getSeance(Mockito.eq(2L));
			Mockito.verify(seanceDao).getSeance(Mockito.anyLong());
		}
	}

	@Test
	public void testVerifierTpAvantRenduKOSeanceCours() {
		try {
			travailManager.verifierTpAvantRendu(3L);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'identifiant du tp est incorrect", e.getMessage());
			Mockito.verify(seanceDao).getSeance(Mockito.eq(3L));
			Mockito.verify(seanceDao).getSeance(Mockito.anyLong());
		}
	}

	@Test
	public void testVerifierTpAvantRenduKOSeanceDepassee() {
		try {
			travailManager.verifierTpAvantRendu(4L);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Le travail ne peut pas être rendu maintenant", e.getMessage());
			Mockito.verify(seanceDao).getSeance(Mockito.eq(4L));
			Mockito.verify(seanceDao).getSeance(Mockito.anyLong());
		}
	}

	@Test
	public void testVerifierTpAvantRenduKOSeanceFuture() {
		try {
			travailManager.verifierTpAvantRendu(5L);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Le travail ne peut pas être rendu maintenant", e.getMessage());
			Mockito.verify(seanceDao).getSeance(Mockito.eq(5L));
			Mockito.verify(seanceDao).getSeance(Mockito.anyLong());
		}
	}

	@Test
	public void testRendreTP() throws Exception {
		travailManager.rendreTP(1L, 1L, 2L, "nouveauFichier.zip", inputStream1, 10L);
	}

	@Test
	public void testRendreTPKOMemeUtilisateurs() throws Exception {
		try {
			travailManager.rendreTP(1L, 1L, 1L, "nouveauFichier.zip", inputStream1, 10L);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Les deux utilisateurs doivent être différents.", e.getMessage());
		}
	}

	@Test
	public void testRendreTPKOFichierTropGros() throws Exception {
		try {
			travailManager.rendreTP(1L, 1L, 2L, "nouveauFichier.zip", inputStream1, 20 * 1024 * 1024L);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Le fichier est trop gros.", e.getMessage());
		}
	}
}
