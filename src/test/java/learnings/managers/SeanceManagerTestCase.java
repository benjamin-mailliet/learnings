package learnings.managers;

import learnings.AbstractTestCase;
import learnings.dao.RessourceDao;
import learnings.dao.SeanceDao;
import learnings.dao.TravailDao;
import learnings.enums.Groupe;
import learnings.enums.TypeSeance;
import learnings.model.Ressource;
import learnings.model.Seance;
import learnings.model.Travail;
import learnings.model.Utilisateur;
import learnings.pojos.TpAvecTravail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SeanceManagerTestCase extends AbstractTestCase {

    @Mock
    private SeanceDao seanceDao;

    @Mock
    private RessourceDao ressourceDao;

    @Mock
    private TravailDao travailDao;

    @InjectMocks
    private SeanceManager seanceManager = new SeanceManager();

    private Seance seance1 = new Seance(1L, "Titre1", "Description1", getDate(2014, Calendar.SEPTEMBER, 1));
    private Seance seance2 = new Seance(2L, "Titre2", "Description2", getDate(2014, Calendar.SEPTEMBER, 2));
    private Seance seance3 = new Seance(3L, "Titre3", "Description3", getDate(2014, Calendar.SEPTEMBER, 3));
    private Ressource ressource1 = new Ressource(1L, "ressource1", "/ressources/ressource1", seance1);
    private Travail travail1 = new Travail(1L, seance3, null, getDate(2014, Calendar.SEPTEMBER, 3, 10, 37, 0), "/chemin/fichier.zip", "http://github1", "commentaire1");
    private Travail travail2 = new Travail(2L, seance3, null, getDate(2014, Calendar.SEPTEMBER, 3, 10, 38, 0), "/chemin/fichier.zip", "http://github2", "commentaire2");
    private Utilisateur utilisateur1 = new Utilisateur(1L, "nom1", "prenom1", "email1", Groupe.GROUPE_1, false);
    private Utilisateur utilisateur2 = new Utilisateur(2L, "nom2", "prenom2", "email2", Groupe.GROUPE_2, false);
    private Utilisateur utilisateur3 = new Utilisateur(3L, "nom3", "prenom3", "email3", null, false);

    @Before
    public void init() {
        List<Seance> seances = new ArrayList<>();
        seances.add(seance1);
        seances.add(seance2);
        when(seanceDao.listerSeances()).thenReturn(seances);

        List<Seance> seancesNotees = new ArrayList<>();
        seancesNotees.add(seance2);
        when(seanceDao.listerSeancesNotees()).thenReturn(seancesNotees);

        List<Seance> seancesRendusAccessibles = new ArrayList<>();
        seancesRendusAccessibles.add(seance3);
        when(seanceDao.listerTPNotesParDateRendu(Mockito.any(Date.class))).thenReturn(seancesRendusAccessibles);
        when(travailDao.getTravailUtilisateurParSeance(Mockito.eq(3L), Mockito.eq(1L))).thenReturn(travail1);

        List<Ressource> ressources = new ArrayList<>();
        ressources.add(ressource1);
        when(ressourceDao.getRessources(Mockito.eq(seance1))).thenReturn(ressources);
        when(ressourceDao.getRessources(Mockito.eq(seance2))).thenReturn(new ArrayList<Ressource>());

        when(seanceDao.getSeance(Mockito.eq(1L))).thenReturn(seance1);
        when(seanceDao.getSeance(Mockito.eq(2L))).thenReturn(seance2);
        when(seanceDao.getSeance(Mockito.eq(3L))).thenReturn(seance3);

        List<Travail> listeTravaux = new ArrayList<>();
        listeTravaux.add(travail1);
        listeTravaux.add(travail2);
        when(travailDao.listerTravauxParSeance(Mockito.eq(3L))).thenReturn(listeTravaux);

        List<Utilisateur> utilisateursTravail1 = new ArrayList<>();
        utilisateursTravail1.add(utilisateur1);
        List<Utilisateur> utilisateursTravail2 = new ArrayList<>();
        utilisateursTravail2.add(utilisateur2);
        utilisateursTravail2.add(utilisateur3);
        when(travailDao.listerUtilisateursParTravail(Mockito.eq(1L))).thenReturn(utilisateursTravail1);
        when(travailDao.listerUtilisateursParTravail(Mockito.eq(2L))).thenReturn(utilisateursTravail2);

    }

    @Test
    public void shouldListerSeances() {
        // WHEN
        List<Seance> seances = seanceManager.listerSeances();
        // THEN
        assertThat(seances).hasSize(2);
        assertThat(seances).containsOnly(seance1, seance2);
        assertThat(seance1.getRessources()).hasSize(1);
        assertThat(seance1.getRessources()).containsOnly(ressource1);
        assertThat(seance2.getRessources()).hasSize(0);

        verify(seanceDao).listerSeances();
        verify(ressourceDao).getRessources(Mockito.eq(seance1));
        verify(ressourceDao).getRessources(Mockito.eq(seance2));
        verify(ressourceDao, Mockito.times(2)).getRessources(Mockito.any(Seance.class));
    }

    @Test
    public void shouldListerSeancesNotees() {
        // WHEN
        List<Seance> seancesNotees = seanceManager.listerSeancesNotees();
        // THEN
        assertThat(seancesNotees).hasSize(1);
        assertThat(seancesNotees).containsOnly(seance2);

        verify(seanceDao).listerSeancesNotees();
    }

    @Test
    public void shouldListerTPRenduAccessible() {
        // WHEN
        List<TpAvecTravail> tpsRenduAccessible = seanceManager.listerTPRenduAccessible(1L);
        // THEN
        assertThat(tpsRenduAccessible).hasSize(1);
        assertThat(tpsRenduAccessible).extracting("tp", "travail").containsOnly(
                tuple(seance3, travail1)
        );

        verify(seanceDao).listerTPNotesParDateRendu(Mockito.any(Date.class));
        verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(3L), Mockito.eq(1L));
        verify(travailDao).getTravailUtilisateurParSeance(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    public void shouldNotListerTPRenduAccessible() {
        // WHEN
        try {
            seanceManager.listerTPRenduAccessible(null);
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("L'utlisateur ne peut pas être null.");
            verify(seanceDao, Mockito.never()).listerTPNotesParDateRendu(Mockito.any(Date.class));
            verify(travailDao, Mockito.never()).getTravailUtilisateurParSeance(Mockito.anyLong(), Mockito.anyLong());
        }
    }

    @Test
    public void shouldGetSeanceAvecTravaux() {
        // WHEN
        Seance seance = seanceManager.getSeanceAvecTravaux(3L);
        // THEN
        assertThat(seance).isEqualTo(seance3);
        assertThat(seance.getTravauxRendus()).hasSize(2);
        assertThat(seance.getTravauxRendus()).containsOnly(travail1, travail2);
        assertThat(travail1.getUtilisateurs()).hasSize(1);
        assertThat(travail1.getUtilisateurs()).containsOnly(utilisateur1);
        assertThat(travail2.getUtilisateurs()).hasSize(2);
        assertThat(travail2.getUtilisateurs()).containsOnly(utilisateur2, utilisateur3);

        verify(seanceDao).getSeance(Mockito.eq(3L));
        verify(seanceDao).getSeance(Mockito.anyLong());
        verify(travailDao).listerTravauxParSeance(Mockito.eq(3L));
        verify(travailDao).listerTravauxParSeance(Mockito.anyLong());
        verify(travailDao).listerUtilisateursParTravail(Mockito.eq(1L));
        verify(travailDao).listerUtilisateursParTravail(Mockito.eq(2L));
        verify(travailDao, Mockito.times(2)).listerUtilisateursParTravail(Mockito.anyLong());
    }

    @Test
    public void shouldNotGetSeanceAvecTravauxAvecIdNull() {
        // WHEN
        try {
            seanceManager.getSeanceAvecTravaux(null);
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("L'identifiant de la séance est incorrect.");
            verify(seanceDao, Mockito.never()).getSeance(Mockito.anyLong());
            verify(travailDao, Mockito.never()).listerTravauxParSeance(Mockito.anyLong());
            verify(travailDao, Mockito.never()).listerUtilisateursParTravail(Mockito.anyLong());
        }
    }

    @Test
    public void shouldNotGetSeanceAvecTravauxAvecSeanceNull() {
        // WHEN
        try {
            seanceManager.getSeanceAvecTravaux(-1L);
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("L'identifiant de la séance est inconnu.");
            verify(seanceDao).getSeance(Mockito.eq(-1L));
            verify(seanceDao).getSeance(Mockito.anyLong());
            verify(travailDao, Mockito.never()).listerTravauxParSeance(Mockito.anyLong());
            verify(travailDao, Mockito.never()).listerUtilisateursParTravail(Mockito.anyLong());
        }
    }

    @Test
    public void shouldGetSeanceAvecRessources() {
        // WHEN
        Seance seance = seanceManager.getSeanceAvecRessources(1L);
        // THEN
        assertThat(seance).isEqualTo(seance1);
        assertThat(seance.getRessources()).hasSize(1);
        assertThat(seance.getRessources()).containsOnly(ressource1);

        verify(seanceDao).getSeance(Mockito.eq(1L));
        verify(seanceDao).getSeance(Mockito.anyLong());
        verify(ressourceDao).getRessources(Mockito.eq(seance1));
        verify(ressourceDao).getRessources(Mockito.any(Seance.class));
    }

    @Test
    public void shouldNotGetSeanceAvecRessourcesAvecIdNull() {
        // WHEN
        try {
            seanceManager.getSeanceAvecRessources(null);
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("L'identifiant de la séance est incorrect.");
            verify(seanceDao, Mockito.never()).getSeance(Mockito.anyLong());
            verify(travailDao, Mockito.never()).listerTravauxParSeance(Mockito.anyLong());
            verify(travailDao, Mockito.never()).listerUtilisateursParTravail(Mockito.anyLong());
        }
    }

    @Test
    public void shouldNotGetSeanceAvecRessourcesAvecSeanceNull() {
        // WHEN
        try {
            seanceManager.getSeanceAvecRessources(-1L);
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("L'identifiant de la séance est inconnu.");
            verify(seanceDao).getSeance(Mockito.eq(-1L));
            verify(seanceDao).getSeance(Mockito.anyLong());
            verify(travailDao, Mockito.never()).listerTravauxParSeance(Mockito.anyLong());
            verify(travailDao, Mockito.never()).listerUtilisateursParTravail(Mockito.anyLong());
        }
    }

    @Test
    public void shouldAjouterSeance() {
        // GIVEN
        Seance seance = new Seance(null, "titre", "description", getDate(2014, Calendar.SEPTEMBER, 6), false, null, TypeSeance.COURS);
        // WHEN
        seanceManager.ajouterSeance(seance);
        // THEN
        verify(seanceDao).ajouterSeance(Mockito.eq(seance));
        verify(seanceDao).ajouterSeance(Mockito.any(Seance.class));
    }

    @Test
    public void shouldNotAjouterSeanceAvecSeanceNull() {
        // WHEN
        try {
            seanceManager.ajouterSeance(null);
            fail("exception attendue");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("La séance est null.");
            verify(seanceDao, Mockito.never()).ajouterSeance(Mockito.any(Seance.class));
        }
    }

    @Test
    public void shouldNotAjouterSeanceAvecTitreNull() {
        // WHEN
        try {
            seanceManager.ajouterSeance(new Seance(null, null, "description", getDate(2014, Calendar.SEPTEMBER, 6), false, null, TypeSeance.COURS));
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Le titre de la séance doit être renseigné.");
            verify(seanceDao, Mockito.never()).ajouterSeance(Mockito.any(Seance.class));
        }
    }

    @Test
    public void shouldNotAjouterSeanceAvecTitreVide() {
        // WHEN
        try {
            seanceManager.ajouterSeance(new Seance(null, "", "description", getDate(2014, Calendar.SEPTEMBER, 6), false, null, TypeSeance.COURS));
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Le titre de la séance doit être renseigné.");
            verify(seanceDao, Mockito.never()).ajouterSeance(Mockito.any(Seance.class));
        }
    }

    @Test
    public void shouldNotAjouterSeanceAvecDateNull() {
        // WHEN
        try {
            seanceManager.ajouterSeance(new Seance(null, "titre", "description", null, false, null, TypeSeance.COURS));
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("La date de la séance doit être renseignée.");
            verify(seanceDao, Mockito.never()).ajouterSeance(Mockito.any(Seance.class));
        }
    }

    @Test
    public void shouldNotAjouterSeanceAvecTypeNull() {
        // WHEN
        try {
            seanceManager.ajouterSeance(new Seance(null, "titre", "description", getDate(2014, Calendar.SEPTEMBER, 6), false, null, null));
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Le type de la séance doit être renseigné.");
            verify(seanceDao, Mockito.never()).ajouterSeance(Mockito.any(Seance.class));
        }
    }

    @Test
    public void testModifierSeance() {
        // GIVEN
        Seance seance = new Seance(1L, "titre", "description", getDate(2014, Calendar.SEPTEMBER, 6), false, null, TypeSeance.COURS);
        // WHEN
        seanceManager.modifierSeance(seance);
        // THEN
        verify(seanceDao).modifierSeance(Mockito.eq(seance));
        verify(seanceDao).modifierSeance(Mockito.any(Seance.class));
    }

    @Test
    public void shouldNotModifierSeanceAvecSeanceNull() {
        // WHEN
        try {
            seanceManager.modifierSeance(null);
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("La séance est null.");
            verify(seanceDao, Mockito.never()).modifierSeance(Mockito.any(Seance.class));
        }
    }

    @Test
    public void shouldNotModifierSeanceAvecIdNull() {
        // WHEN
        try {
            seanceManager.modifierSeance(new Seance(null, "titre", "description", getDate(2014, Calendar.SEPTEMBER, 6), false, null, TypeSeance.COURS));
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("L'identifiant de la séance doit être renseigné.");
            verify(seanceDao, Mockito.never()).modifierSeance(Mockito.any(Seance.class));
        }
    }

    @Test
    public void shouldNotModifierSeanceAvecTitreNull() {
        // WHEN
        try {
            seanceManager.modifierSeance(new Seance(1L, null, "description", getDate(2014, Calendar.SEPTEMBER, 6), false, null, TypeSeance.COURS));
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Le titre de la séance doit être renseigné.");
            verify(seanceDao, Mockito.never()).modifierSeance(Mockito.any(Seance.class));
        }
    }

    @Test
    public void shouldNotModifierSeanceAvecTitreVide() {
        // WHEN
        try {
            seanceManager.modifierSeance(new Seance(1L, "", "description", getDate(2014, Calendar.SEPTEMBER, 6), false, null, TypeSeance.COURS));
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Le titre de la séance doit être renseigné.");
            verify(seanceDao, Mockito.never()).modifierSeance(Mockito.any(Seance.class));
        }
    }

    @Test
    public void shouldNotModifierSeanceAvecDateNull() {
        // WHEN
        try {
            seanceManager.modifierSeance(new Seance(1L, "titre", "description", null, false, null, TypeSeance.COURS));
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("La date de la séance doit être renseignée.");
            verify(seanceDao, Mockito.never()).modifierSeance(Mockito.any(Seance.class));
        }
    }

    @Test
    public void shouldNotModifierSeanceAvecTypeNull() {
        // WHEN
        try {
            seanceManager
                    .modifierSeance(new Seance(1L, "titre", "description", getDate(2014, Calendar.SEPTEMBER, 6), false, null, null));
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Le type de la séance doit être renseigné.");
            verify(seanceDao, Mockito.never()).modifierSeance(Mockito.any(Seance.class));
        }

    }
}
