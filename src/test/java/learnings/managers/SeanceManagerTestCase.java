package learnings.managers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import learnings.dao.RessourceDao;
import learnings.dao.SeanceDao;
import learnings.dao.TravailDao;
import learnings.enums.TypeSeance;
import learnings.model.Ressource;
import learnings.model.Seance;
import learnings.model.Travail;
import learnings.model.Utilisateur;
import learnings.pojos.TpAvecTravail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SeanceManagerTestCase {

	@Mock
	private SeanceDao seanceDao;

	@Mock
	private RessourceDao ressourceDao;

	@Mock
	private TravailDao travailDao;

	@InjectMocks
	private SeanceManager seanceManager = new SeanceManager();

	private Seance seance1 = new Seance(1L, "Titre1", "Description1", new GregorianCalendar(2014, Calendar.SEPTEMBER, 1).getTime());
	private Seance seance2 = new Seance(2L, "Titre2", "Description2", new GregorianCalendar(2014, Calendar.SEPTEMBER, 2).getTime());
	private Seance seance3 = new Seance(3L, "Titre3", "Description3", new GregorianCalendar(2014, Calendar.SEPTEMBER, 3).getTime());
	private Ressource ressource1 = new Ressource(1L, "ressource1", "/ressources/ressource1", seance1);
	private Travail travail1 = new Travail(1L, seance3, null, new GregorianCalendar(2014, Calendar.SEPTEMBER, 3, 10, 37).getTime(), "/chemin/fichier.zip");
	private Travail travail2 = new Travail(2L, seance3, null, new GregorianCalendar(2014, Calendar.SEPTEMBER, 3, 10, 38).getTime(), "/chemin/fichier.zip");
	private Utilisateur utilisateur1 = new Utilisateur(1L, "email1", false);
	private Utilisateur utilisateur2 = new Utilisateur(2L, "email2", false);
	private Utilisateur utilisateur3 = new Utilisateur(3L, "email3", false);

	@Before
	public void init() {
		List<Seance> seances = new ArrayList<Seance>();
		seances.add(seance1);
		seances.add(seance2);
		Mockito.when(seanceDao.listerSeances()).thenReturn(seances);

		List<Seance> seancesNotees = new ArrayList<Seance>();
		seancesNotees.add(seance2);
		Mockito.when(seanceDao.listerSeancesNotees()).thenReturn(seancesNotees);

		List<Seance> seancesRendusAccessibles = new ArrayList<Seance>();
		seancesRendusAccessibles.add(seance3);
		Mockito.when(seanceDao.listerTPNotesParDateRendu(Mockito.any(Date.class))).thenReturn(seancesRendusAccessibles);
		Mockito.when(travailDao.getTravailUtilisateurParSeance(Mockito.eq(3L), Mockito.eq(1L))).thenReturn(travail1);

		List<Ressource> ressources = new ArrayList<Ressource>();
		ressources.add(ressource1);
		Mockito.when(ressourceDao.getRessourcesBySeance(Mockito.eq(seance1))).thenReturn(ressources);
		Mockito.when(ressourceDao.getRessourcesBySeance(Mockito.eq(seance2))).thenReturn(new ArrayList<Ressource>());

		Mockito.when(seanceDao.getSeance(Mockito.eq(1L))).thenReturn(seance1);
		Mockito.when(seanceDao.getSeance(Mockito.eq(2L))).thenReturn(seance2);
		Mockito.when(seanceDao.getSeance(Mockito.eq(3L))).thenReturn(seance3);

		List<Travail> listeTravaux = new ArrayList<Travail>();
		listeTravaux.add(travail1);
		listeTravaux.add(travail2);
		Mockito.when(travailDao.listerTravauxParSeance(Mockito.eq(3L))).thenReturn(listeTravaux);

		List<Utilisateur> utilisateursTravail1 = new ArrayList<Utilisateur>();
		utilisateursTravail1.add(utilisateur1);
		List<Utilisateur> utilisateursTravail2 = new ArrayList<Utilisateur>();
		utilisateursTravail2.add(utilisateur2);
		utilisateursTravail2.add(utilisateur3);
		Mockito.when(travailDao.listerUtilisateurs(Mockito.eq(1L))).thenReturn(utilisateursTravail1);
		Mockito.when(travailDao.listerUtilisateurs(Mockito.eq(2L))).thenReturn(utilisateursTravail2);

	}

	@Test
	public void testListerSeances() {
		List<Seance> seances = seanceManager.listerSeances();

		Assert.assertEquals(2, seances.size());
		Assert.assertTrue(seances.contains(seance1));
		Assert.assertTrue(seances.contains(seance2));
		Assert.assertEquals(1, seance1.getRessources().size());
		Assert.assertTrue(seance1.getRessources().contains(ressource1));
		Assert.assertEquals(0, seance2.getRessources().size());

		Mockito.verify(seanceDao).listerSeances();
		Mockito.verify(ressourceDao).getRessourcesBySeance(Mockito.eq(seance1));
		Mockito.verify(ressourceDao).getRessourcesBySeance(Mockito.eq(seance2));
		Mockito.verify(ressourceDao, Mockito.times(2)).getRessourcesBySeance(Mockito.any(Seance.class));
	}

	@Test
	public void testListerSeancesNotees() {
		List<Seance> seancesNotees = seanceManager.listerSeancesNotees();
		Assert.assertEquals(1, seancesNotees.size());
		Assert.assertTrue(seancesNotees.contains(seance2));

		Mockito.verify(seanceDao).listerSeancesNotees();
	}

	@Test
	public void testListerTPRenduAccessible() {
		List<TpAvecTravail> tpsRenduAccessible = seanceManager.listerTPRenduAccessible(1L);
		Assert.assertEquals(1, tpsRenduAccessible.size());
		Assert.assertEquals(seance3, tpsRenduAccessible.get(0).getTp());
		Assert.assertEquals(travail1, tpsRenduAccessible.get(0).getTravail());

		Mockito.verify(seanceDao).listerTPNotesParDateRendu(Mockito.any(Date.class));
		Mockito.verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(3L), Mockito.eq(1L));
		Mockito.verify(travailDao).getTravailUtilisateurParSeance(Mockito.anyLong(), Mockito.anyLong());
	}

	@Test
	public void testListerTPRenduAccessibleKO() {
		try {
			seanceManager.listerTPRenduAccessible(null);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'utlisateur ne peut pas être null.", e.getMessage());
			Mockito.verify(seanceDao, Mockito.never()).listerTPNotesParDateRendu(Mockito.any(Date.class));
			Mockito.verify(travailDao, Mockito.never()).getTravailUtilisateurParSeance(Mockito.anyLong(), Mockito.anyLong());
		}
	}

	@Test
	public void testGetSeanceAvecTravaux() {
		Seance seance = seanceManager.getSeanceAvecTravaux(3L);

		Assert.assertEquals(seance3, seance);
		Assert.assertEquals(2, seance.getTravauxRendus().size());
		Assert.assertEquals(travail1, seance.getTravauxRendus().get(0));
		Assert.assertEquals(1, seance.getTravauxRendus().get(0).getUtilisateurs().size());
		Assert.assertEquals(utilisateur1, seance.getTravauxRendus().get(0).getUtilisateurs().get(0));
		Assert.assertEquals(travail2, seance.getTravauxRendus().get(1));
		Assert.assertEquals(2, seance.getTravauxRendus().get(1).getUtilisateurs().size());
		Assert.assertEquals(utilisateur2, seance.getTravauxRendus().get(1).getUtilisateurs().get(0));
		Assert.assertEquals(utilisateur3, seance.getTravauxRendus().get(1).getUtilisateurs().get(1));

		Mockito.verify(seanceDao).getSeance(Mockito.eq(3L));
		Mockito.verify(seanceDao).getSeance(Mockito.anyLong());
		Mockito.verify(travailDao).listerTravauxParSeance(Mockito.eq(3L));
		Mockito.verify(travailDao).listerTravauxParSeance(Mockito.anyLong());
		Mockito.verify(travailDao).listerUtilisateurs(Mockito.eq(1L));
		Mockito.verify(travailDao).listerUtilisateurs(Mockito.eq(2L));
		Mockito.verify(travailDao, Mockito.times(2)).listerUtilisateurs(Mockito.anyLong());
	}

	@Test
	public void testGetSeanceAvecTravauxKOIdNull() {
		try {
			seanceManager.getSeanceAvecTravaux(null);
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'identifiant de la séance est incorrect.", e.getMessage());
			Mockito.verify(seanceDao, Mockito.never()).getSeance(Mockito.anyLong());
			Mockito.verify(travailDao, Mockito.never()).listerTravauxParSeance(Mockito.anyLong());
			Mockito.verify(travailDao, Mockito.never()).listerUtilisateurs(Mockito.anyLong());
		}
	}

	@Test
	public void testGetSeanceAvecTravauxKOSeanceNull() {
		try {
			seanceManager.getSeanceAvecTravaux(-1L);
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'identifiant de la séance est inconnu.", e.getMessage());
			Mockito.verify(seanceDao).getSeance(Mockito.eq(-1L));
			Mockito.verify(seanceDao).getSeance(Mockito.anyLong());
			Mockito.verify(travailDao, Mockito.never()).listerTravauxParSeance(Mockito.anyLong());
			Mockito.verify(travailDao, Mockito.never()).listerUtilisateurs(Mockito.anyLong());
		}
	}

	@Test
	public void testGetSeanceAvecRessources() {
		Seance seance = seanceManager.getSeanceAvecRessources(1L);

		Assert.assertEquals(seance1, seance);
		Assert.assertEquals(1, seance.getRessources().size());
		Assert.assertTrue(seance.getRessources().contains(ressource1));

		Mockito.verify(seanceDao).getSeance(Mockito.eq(1L));
		Mockito.verify(seanceDao).getSeance(Mockito.anyLong());
		Mockito.verify(ressourceDao).getRessourcesBySeance(Mockito.eq(seance1));
		Mockito.verify(ressourceDao).getRessourcesBySeance(Mockito.any(Seance.class));
	}

	@Test
	public void testGetSeanceAvecRessourcesKOIdNull() {
		try {
			seanceManager.getSeanceAvecRessources(null);
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'identifiant de la séance est incorrect.", e.getMessage());
			Mockito.verify(seanceDao, Mockito.never()).getSeance(Mockito.anyLong());
			Mockito.verify(travailDao, Mockito.never()).listerTravauxParSeance(Mockito.anyLong());
			Mockito.verify(travailDao, Mockito.never()).listerUtilisateurs(Mockito.anyLong());
		}
	}

	@Test
	public void testGetSeanceAvecRessourcesKOSeanceNull() {
		try {
			seanceManager.getSeanceAvecRessources(-1L);
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'identifiant de la séance est inconnu.", e.getMessage());
			Mockito.verify(seanceDao).getSeance(Mockito.eq(-1L));
			Mockito.verify(seanceDao).getSeance(Mockito.anyLong());
			Mockito.verify(travailDao, Mockito.never()).listerTravauxParSeance(Mockito.anyLong());
			Mockito.verify(travailDao, Mockito.never()).listerUtilisateurs(Mockito.anyLong());
		}
	}

	@Test
	public void testAjouterSeanceOK() {
		Seance seance = new Seance(null, "titre", "description", new GregorianCalendar(2014, Calendar.SEPTEMBER, 6).getTime(), false, null, TypeSeance.COURS);

		seanceManager.ajouterSeance(seance);

		Mockito.verify(seanceDao).ajouterSeance(Mockito.eq(seance));
		Mockito.verify(seanceDao).ajouterSeance(Mockito.any(Seance.class));
	}

	@Test
	public void testAjouterSeanceKO() {
		try {
			seanceManager.ajouterSeance(null);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("La séance est null.", e.getMessage());
		}
		try {
			seanceManager.ajouterSeance(new Seance(null, null, "description", new GregorianCalendar(2014, Calendar.SEPTEMBER, 6).getTime(), false, null,
					TypeSeance.COURS));
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Le titre de la séance doit être renseigné.", e.getMessage());
		}
		try {
			seanceManager.ajouterSeance(new Seance(null, "", "description", new GregorianCalendar(2014, Calendar.SEPTEMBER, 6).getTime(), false, null,
					TypeSeance.COURS));
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Le titre de la séance doit être renseigné.", e.getMessage());
		}
		try {
			seanceManager.ajouterSeance(new Seance(null, "titre", "description", null, false, null, TypeSeance.COURS));
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("La date de la séance doit être renseignée.", e.getMessage());
		}
		try {
			seanceManager.ajouterSeance(new Seance(null, "titre", "description", new GregorianCalendar(2014, Calendar.SEPTEMBER, 6).getTime(), false, null,
					null));
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Le type de la séance doit être renseigné.", e.getMessage());
		}

		Mockito.verify(seanceDao, Mockito.never()).ajouterSeance(Mockito.any(Seance.class));
	}

	@Test
	public void testModifierSeanceOK() {
		Seance seance = new Seance(1L, "titre", "description", new GregorianCalendar(2014, Calendar.SEPTEMBER, 6).getTime(), false, null, TypeSeance.COURS);

		seanceManager.modifierSeance(seance);

		Mockito.verify(seanceDao).modifierSeance(Mockito.eq(seance));
		Mockito.verify(seanceDao).modifierSeance(Mockito.any(Seance.class));
	}

	@Test
	public void testModifierSeanceKO() {
		try {
			seanceManager.modifierSeance(null);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("La séance est null.", e.getMessage());
		}
		try {
			seanceManager.modifierSeance(new Seance(null, "titre", "description", new GregorianCalendar(2014, Calendar.SEPTEMBER, 6).getTime(), false, null,
					TypeSeance.COURS));
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("L'identifiant de la séance doit être renseigné.", e.getMessage());
		}
		try {
			seanceManager.modifierSeance(new Seance(1L, null, "description", new GregorianCalendar(2014, Calendar.SEPTEMBER, 6).getTime(), false, null,
					TypeSeance.COURS));
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Le titre de la séance doit être renseigné.", e.getMessage());
		}
		try {
			seanceManager.modifierSeance(new Seance(1L, "", "description", new GregorianCalendar(2014, Calendar.SEPTEMBER, 6).getTime(), false, null,
					TypeSeance.COURS));
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Le titre de la séance doit être renseigné.", e.getMessage());
		}
		try {
			seanceManager.modifierSeance(new Seance(1L, "titre", "description", null, false, null, TypeSeance.COURS));
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("La date de la séance doit être renseignée.", e.getMessage());
		}
		try {
			seanceManager
					.modifierSeance(new Seance(1L, "titre", "description", new GregorianCalendar(2014, Calendar.SEPTEMBER, 6).getTime(), false, null, null));
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Le type de la séance doit être renseigné.", e.getMessage());
		}

		Mockito.verify(seanceDao, Mockito.never()).modifierSeance(Mockito.any(Seance.class));
	}
}
