package learnings.managers;

import learnings.AbstractTestCase;
import learnings.dao.BinomeDao;
import learnings.dao.RenduTpDao;
import learnings.dao.RessourceDao;
import learnings.dao.SeanceDao;
import learnings.enums.Groupe;
import learnings.enums.RessourceCategorie;
import learnings.enums.RessourceFormat;
import learnings.enums.TypeSeance;
import learnings.model.Binome;
import learnings.model.RenduTp;
import learnings.model.Ressource;
import learnings.model.Seance;
import learnings.model.Utilisateur;
import learnings.pojos.SeanceAvecRendus;
import learnings.pojos.TpAvecTravaux;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SeanceManagerTestCase extends AbstractTestCase {

    @Mock
    private SeanceDao seanceDao;

    @Mock
    private RessourceDao ressourceDao;

    @Mock
    private RenduTpDao renduTpDao;

    @Mock
    private BinomeDao binomeDao;

    @InjectMocks
    private SeanceManager seanceManager = new SeanceManager();

    private Seance seance1 = new Seance(1L, "Titre1", "Description1", getDate(2014, Calendar.SEPTEMBER, 1));
    private Seance seance2 = new Seance(2L, "Titre2", "Description2", getDate(2014, Calendar.SEPTEMBER, 2));
    private Seance seance3 = new Seance(3L, "Titre3", "Description3", getDate(2014, Calendar.SEPTEMBER, 3));
    private Ressource ressource1 = new Ressource(1L, "ressource1", "/ressources/ressource1", seance1, RessourceCategorie.SUPPORT, RessourceFormat.AUTRE);
    private Utilisateur utilisateur1 = new Utilisateur(1L, "nom1", "prenom1", "email1", Groupe.GROUPE_1, false);
    private Utilisateur utilisateur2 = new Utilisateur(2L, "nom2", "prenom2", "email2", Groupe.GROUPE_2, false);
    private Utilisateur utilisateur3 = new Utilisateur(3L, "nom3", "prenom3", "email3", null, false);
    private Binome binome1 = new Binome("uid1", seance3, utilisateur1);
    private Binome binome2 = new Binome("uid2", seance3, utilisateur2, utilisateur3);
    private RenduTp renduTp1 = new RenduTp(1L, null, LocalDateTime.of(2014, Month.SEPTEMBER, 3, 10, 37, 0), "/chemin/fichier.zip", "commentaire1", binome1);
    private RenduTp renduTp2 = new RenduTp(2L, null, LocalDateTime.of(2014, Month.SEPTEMBER, 3, 10, 38, 0), "/chemin/fichier.zip", "commentaire2", binome2);

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
        when(renduTpDao.listerRendusParBinome(Mockito.eq("uid1"))).thenReturn(Collections.singletonList(renduTp1));

        List<Ressource> ressources = new ArrayList<>();
        ressources.add(ressource1);
        when(ressourceDao.getRessources(Mockito.eq(seance1))).thenReturn(ressources);
        when(ressourceDao.getRessources(Mockito.eq(seance2))).thenReturn(new ArrayList<>());

        when(seanceDao.getSeance(Mockito.eq(1L))).thenReturn(seance1);
        when(seanceDao.getSeance(Mockito.eq(2L))).thenReturn(seance2);
        when(seanceDao.getSeance(Mockito.eq(3L))).thenReturn(seance3);

        List<RenduTp> listeTravaux = new ArrayList<>();
        listeTravaux.add(renduTp1);
        listeTravaux.add(renduTp2);
        when(renduTpDao.listerRendusParSeance(Mockito.eq(3L))).thenReturn(listeTravaux);

        when(binomeDao.getBinome(Mockito.eq(3L), Mockito.eq(1L))).thenReturn(binome1);
        when(binomeDao.getBinome(Mockito.eq(3L), Mockito.eq(2L))).thenReturn(binome2);
        when(binomeDao.getBinome(Mockito.eq(3L), Mockito.eq(3L))).thenReturn(binome2);
        when(binomeDao.getBinome(Mockito.eq("uid1"))).thenReturn(binome1);
        when(binomeDao.getBinome(Mockito.eq("uid2"))).thenReturn(binome2);
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
        List<TpAvecTravaux> tpsRenduAccessible = seanceManager.listerTPRenduAccessible(1L);
        // THEN
        assertThat(tpsRenduAccessible).hasSize(1);
        assertThat(tpsRenduAccessible.get(0).getTp()).isEqualTo(seance3);
        assertThat(tpsRenduAccessible.get(0).getTravaux()).containsOnly(renduTp1);

        verify(seanceDao).listerTPNotesParDateRendu(Mockito.any(Date.class));
        verify(renduTpDao).listerRendusParBinome(Mockito.eq("uid1"));
        verify(renduTpDao).listerRendusParBinome(Mockito.anyString());
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
            verify(renduTpDao, never()).listerRendusParBinome(Mockito.anyString());
        }
    }

    @Test
    public void shouldGetSeanceAvecTravaux() {
        // WHEN
        SeanceAvecRendus seanceAvecRendus = seanceManager.getSeanceAvecTravaux(3L);
        // THEN
        assertThat(seanceAvecRendus.getSeance()).isEqualTo(seance3);
        assertThat(seanceAvecRendus.getRendus()).hasSize(2);
        assertThat(seanceAvecRendus.getRendus().keySet()).containsOnly(binome1, binome2);
        assertThat(seanceAvecRendus.getRendus().get(binome1)).containsOnly(renduTp1);
        assertThat(seanceAvecRendus.getRendus().get(binome2)).containsOnly(renduTp2);

        verify(seanceDao).getSeance(Mockito.eq(3L));
        verify(seanceDao).getSeance(Mockito.anyLong());
        verify(renduTpDao).listerRendusParSeance(Mockito.eq(3L));
        verify(renduTpDao).listerRendusParSeance(Mockito.anyLong());
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
            verify(renduTpDao, Mockito.never()).listerRendusParSeance(Mockito.anyLong());
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
            verify(renduTpDao, Mockito.never()).listerRendusParSeance(Mockito.anyLong());
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
            verify(renduTpDao, Mockito.never()).listerRendusParSeance(Mockito.anyLong());
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
            verify(renduTpDao, Mockito.never()).listerRendusParSeance(Mockito.anyLong());
        }
    }

    @Test
    public void shouldAjouterSeance() {
        // GIVEN
        Seance seance = new Seance(null, "titre", "description", getDate(2014, Calendar.SEPTEMBER, 6), false, null, TypeSeance.COURS, null);
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
            seanceManager.ajouterSeance(new Seance(null, null, "description", getDate(2014, Calendar.SEPTEMBER, 6), false, null, TypeSeance.COURS, null));
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
            seanceManager.ajouterSeance(new Seance(null, "", "description", getDate(2014, Calendar.SEPTEMBER, 6), false, null, TypeSeance.COURS, null));
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
            seanceManager.ajouterSeance(new Seance(null, "titre", "description", null, false, null, TypeSeance.COURS, null));
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
            seanceManager.ajouterSeance(new Seance(null, "titre", "description", getDate(2014, Calendar.SEPTEMBER, 6), false, null, null, null));
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
        Seance seance = new Seance(1L, "titre", "description", getDate(2014, Calendar.SEPTEMBER, 6), false, null, TypeSeance.COURS, null);
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
            seanceManager.modifierSeance(new Seance(null, "titre", "description", getDate(2014, Calendar.SEPTEMBER, 6), false, null, TypeSeance.COURS, null));
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
            seanceManager.modifierSeance(new Seance(1L, null, "description", getDate(2014, Calendar.SEPTEMBER, 6), false, null, TypeSeance.COURS, null));
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
            seanceManager.modifierSeance(new Seance(1L, "", "description", getDate(2014, Calendar.SEPTEMBER, 6), false, null, TypeSeance.COURS, null));
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
            seanceManager.modifierSeance(new Seance(1L, "titre", "description", null, false, null, TypeSeance.COURS, null));
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
                    .modifierSeance(new Seance(1L, "titre", "description", getDate(2014, Calendar.SEPTEMBER, 6), false, null, null, null));
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Le type de la séance doit être renseigné.");
            verify(seanceDao, Mockito.never()).modifierSeance(Mockito.any(Seance.class));
        }

    }
}
