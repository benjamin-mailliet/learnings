package learnings.managers;

import learnings.AbstractTestCase;
import learnings.dao.SeanceDao;
import learnings.dao.TravailDao;
import learnings.dao.UtilisateurDao;
import learnings.enums.Groupe;
import learnings.enums.TypeSeance;
import learnings.exceptions.LearningsException;
import learnings.model.Seance;
import learnings.model.Travail;
import learnings.model.Utilisateur;
import learnings.pojos.FichierComplet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TravailManagerTestCase extends AbstractTestCase {

    @Mock
    private SeanceDao seanceDao;

    @Mock
    private UtilisateurDao utilisateurDao;

    @Mock
    private TravailDao travailDao;

    @Mock
    private FichierManager fichierManager;

    @InjectMocks
    private TravailManager travailManager = TravailManager.getInstance();

    private Seance seance1 = new Seance(1L, "seance1", "description1", getDate(2014, Calendar.SEPTEMBER, 5), true, getDate(3000, Calendar.SEPTEMBER, 5), TypeSeance.TP);
    private Seance seanceNonNotee = new Seance(2L, "seance1", "description1", getDate(2014, Calendar.SEPTEMBER, 5), false, getDate(3000, Calendar.SEPTEMBER, 5), TypeSeance.TP);
    private Seance seanceCours = new Seance(3L, "seance1", "description1", getDate(2014, Calendar.SEPTEMBER, 5), true, getDate(3000, Calendar.SEPTEMBER, 5), TypeSeance.COURS);
    private Seance seanceDepassee = new Seance(4L, "seance1", "description1", getDate(2014, Calendar.SEPTEMBER, 1), true, getDate(2014, Calendar.SEPTEMBER, 1), TypeSeance.TP);
    private Seance seanceFuture = new Seance(5L, "seance1", "description1", getDate(3000, Calendar.SEPTEMBER, 1), true, getDate(3000, Calendar.SEPTEMBER, 1), TypeSeance.TP);
    private Travail travail1 = new Travail(1L, seance1, null, getDate(2014, Calendar.SEPTEMBER, 5, 12, 50, 0), "/chemin/12346578-fichier1.zip", "http://github1", "commentaire1");
    private Travail travail1SansId = new Travail(null, seance1, null, getDate(2014, Calendar.SEPTEMBER, 5, 12, 50, 0), "/chemin/12346578-fichier1.zip", "http://github1", "commentaire1");
    private Travail travail2 = new Travail(2L, seance1, null, getDate(2014, Calendar.SEPTEMBER, 6, 13, 51, 0), "/chemin/12346578-fichier2.zip", "commentaire2", "http://github2");

    private InputStream inputStream1 = new ByteArrayInputStream(new byte[]{});
    private InputStream inputStream2 = new ByteArrayInputStream(new byte[]{});
    private InputStream inputStreamEx = new ByteArrayInputStream(new byte[]{});

    private Utilisateur utilisateur1 = new Utilisateur(1L, "nom1", "prenom1", "email1", Groupe.GROUPE_1, false);
    private Utilisateur utilisateur2 = new Utilisateur(2L, "nom2", "prenom2", "email2", Groupe.GROUPE_2, false);
    private Utilisateur utilisateurAdmin = new Utilisateur(0L, "nom", "prenom", "email2", null, true);

    @Before
    public void init() throws Exception {
        when(travailDao.getTravail(Mockito.eq(1L))).thenReturn(travail1);
        when(travailDao.ajouterTravail(Mockito.eq(travail1SansId))).thenReturn(travail1);
        when(fichierManager.getFichier(Mockito.eq("/chemin/12346578-fichier1.zip"))).thenReturn(inputStream1);
        when(fichierManager.getFichier(Mockito.eq("/chemin/12346578-fichier2.zip"))).thenReturn(inputStream2);

        doThrow(new LearningsException()).when(fichierManager).supprimerFichier(Mockito.eq("/chemin/12346578-fichier2.zip"));
        doThrow(new LearningsException()).when(fichierManager).ajouterFichier(Mockito.anyString(), Mockito.eq(inputStreamEx));

        when(travailDao.getTravailUtilisateurParSeance(Mockito.eq(1L), Mockito.eq(1L))).thenReturn(travail1);
        when(travailDao.getTravailUtilisateurParSeance(Mockito.eq(1L), Mockito.eq(2L))).thenReturn(travail1);
        when(travailDao.getTravailUtilisateurParSeance(Mockito.eq(10L), Mockito.eq(1L))).thenReturn(travail1);
        when(travailDao.getTravailUtilisateurParSeance(Mockito.eq(10L), Mockito.eq(2L))).thenReturn(travail1);
        List<Utilisateur> utilisateurs = new ArrayList<>();
        utilisateurs.add(utilisateur1);
        utilisateurs.add(utilisateur2);
        when(travailDao.listerUtilisateurs(Mockito.eq(1L))).thenReturn(utilisateurs);

        when(travailDao.getTravailUtilisateurParSeance(Mockito.eq(11L), Mockito.eq(1L))).thenReturn(travail1);
        when(travailDao.getTravailUtilisateurParSeance(Mockito.eq(11L), Mockito.eq(2L))).thenReturn(travail2);

        when(travailDao.getTravailUtilisateurParSeance(Mockito.eq(12L), Mockito.eq(1L))).thenReturn(travail2);
        List<Utilisateur> utilisateurs2 = new ArrayList<>();
        utilisateurs2.add(utilisateur1);
        when(travailDao.listerUtilisateurs(Mockito.eq(2L))).thenReturn(utilisateurs2);

        when(utilisateurDao.getUtilisateur(Mockito.eq(0L))).thenReturn(utilisateurAdmin);
        when(utilisateurDao.getUtilisateur(Mockito.eq(1L))).thenReturn(utilisateur1);
        when(utilisateurDao.getUtilisateur(Mockito.eq(2L))).thenReturn(utilisateur2);

        when(seanceDao.getSeance(Mockito.eq(1L))).thenReturn(seance1);
        when(seanceDao.getSeance(Mockito.eq(2L))).thenReturn(seanceNonNotee);
        when(seanceDao.getSeance(Mockito.eq(3L))).thenReturn(seanceCours);
        when(seanceDao.getSeance(Mockito.eq(4L))).thenReturn(seanceDepassee);
        when(seanceDao.getSeance(Mockito.eq(5L))).thenReturn(seanceFuture);

    }

    @Test
    public void shouldListerSeances() throws Exception {
        // WHEN
        FichierComplet fichier = travailManager.getFichierTravail(1L);
        //THEN
        assertThat(fichier.getNom()).isEqualTo("fichier1.zip");
        assertThat(fichier.getDonnees()).isEqualTo(inputStream1);

        verify(travailDao).getTravail(Mockito.eq(1L));
        verify(travailDao).getTravail(Mockito.anyLong());
        verify(fichierManager).getFichier(Mockito.eq("/chemin/12346578-fichier1.zip"));
        verify(fichierManager).getFichier(Mockito.anyString());
    }

    @Test
    public void shouldModifierTravail() throws Exception {
        // WHEN
        travailManager.modifierTravail(inputStream2, travail1, travail2);
        // THEN
        verify(fichierManager).supprimerFichier(Mockito.eq("/chemin/12346578-fichier1.zip"));
        verify(fichierManager).supprimerFichier(Mockito.anyString());
        verify(fichierManager).ajouterFichier(Mockito.eq("/chemin/12346578-fichier2.zip"), Mockito.eq(inputStream2));
        verify(fichierManager).ajouterFichier(Mockito.anyString(), Mockito.any(InputStream.class));
        verify(travailDao).mettreAJourTravail(Mockito.eq(1L), Mockito.any(Date.class), Mockito.eq("/chemin/12346578-fichier2.zip"), Mockito.eq("http://github2"),
                Mockito.eq("commentaire2"));
        verify(travailDao).mettreAJourTravail(Mockito.anyLong(), Mockito.any(Date.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void shouldNotModifierTravailKOException() throws Exception {
        // WHEN
        try {
            travailManager.modifierTravail(inputStream1, travail2, travail1);
            fail("exception attendue");
        }
        // THEN
        catch (LearningsException e) {
            verify(fichierManager).supprimerFichier(Mockito.eq("/chemin/12346578-fichier2.zip"));
            verify(fichierManager).supprimerFichier(Mockito.anyString());
            verify(fichierManager, Mockito.never()).ajouterFichier(Mockito.anyString(), Mockito.any(InputStream.class));
            verify(travailDao, Mockito.never())
                    .mettreAJourTravail(Mockito.anyLong(), Mockito.any(Date.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        }

    }

    @Test
    public void shouldAjouterTravail() throws Exception {
        // WHEN
        travailManager.ajouterTravail(inputStream1, utilisateur1, utilisateur2, travail1SansId);
        // THEN
        verify(fichierManager).ajouterFichier(Mockito.eq("/chemin/12346578-fichier1.zip"), Mockito.eq(inputStream1));
        verify(fichierManager).ajouterFichier(Mockito.anyString(), Mockito.any(InputStream.class));
        verify(travailDao).ajouterTravail(Mockito.eq(travail1SansId));
        verify(travailDao).ajouterTravail(Mockito.any(Travail.class));
        verify(travailDao).ajouterUtilisateur(Mockito.eq(1L), Mockito.eq(1L));
        verify(travailDao).ajouterUtilisateur(Mockito.eq(1L), Mockito.eq(2L));
        verify(travailDao, Mockito.times(2)).ajouterUtilisateur(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    public void shouldAjouterTravailAvecUtilisateur2Null() throws Exception {
        // WHEN
        travailManager.ajouterTravail(inputStream1, utilisateur1, null, travail1SansId);
        // THEN
        verify(fichierManager).ajouterFichier(Mockito.eq("/chemin/12346578-fichier1.zip"), Mockito.eq(inputStream1));
        verify(fichierManager).ajouterFichier(Mockito.anyString(), Mockito.any(InputStream.class));
        verify(travailDao).ajouterTravail(Mockito.eq(travail1SansId));
        verify(travailDao).ajouterTravail(Mockito.any(Travail.class));
        verify(travailDao).ajouterUtilisateur(Mockito.eq(1L), Mockito.eq(1L));
        verify(travailDao).ajouterUtilisateur(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    public void shouldNotAjouterTravail() throws Exception {
        // WHEN
        try {
            travailManager.ajouterTravail(inputStreamEx, utilisateur1, utilisateur2, travail1SansId);
            fail("exception attendue");
        }
        // THEN
        catch (LearningsException e) {
            verify(fichierManager).ajouterFichier(Mockito.eq("/chemin/12346578-fichier1.zip"), Mockito.eq(inputStreamEx));
            verify(fichierManager).ajouterFichier(Mockito.anyString(), Mockito.any(InputStream.class));
            verify(travailDao, Mockito.never()).ajouterTravail(Mockito.any(Travail.class));
            verify(travailDao, Mockito.never()).ajouterUtilisateur(Mockito.anyLong(), Mockito.anyLong());
        }
    }

    @Test
    public void shouldVerifierExistanceTravailAvecExistant() throws Exception {
        // WHEN
        Travail travail = travailManager.verifierExistanceTravail(10L, 1L, 2L);
        // THEN
        assertThat(travail).isEqualTo(travail1);
        verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(10L), Mockito.eq(1L));
        verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(10L), Mockito.eq(2L));
        verify(travailDao, Mockito.times(2)).getTravailUtilisateurParSeance(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    public void shouldVerifierExistanceTravailAvecNonExistant() throws Exception {
        // WHEN
        Travail travail = travailManager.verifierExistanceTravail(10L, 3L, 4L);
        // THEN
        assertThat(travail).isNull();
        verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(10L), Mockito.eq(3L));
        verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(10L), Mockito.eq(4L));
        verify(travailDao, Mockito.times(2)).getTravailUtilisateurParSeance(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    public void shouldVerifierExistanceTravailAvecExistantSolo() throws Exception {
        // WHEN
        Travail travail = travailManager.verifierExistanceTravail(12L, 1L, null);
        // THEN
        assertThat(travail).isEqualTo(travail2);
        verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(12L), Mockito.eq(1L));
        verify(travailDao).getTravailUtilisateurParSeance(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    public void shouldVerifierExistanceTravailAvecNonExistantSolo() throws Exception {
        // WHEN
        Travail travail = travailManager.verifierExistanceTravail(12L, 2L, null);
        // THEN
        assertThat(travail).isNull();
        verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(12L), Mockito.eq(2L));
        verify(travailDao).getTravailUtilisateurParSeance(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    public void shouldNotVerifierExistanceTravailAvecBinomeEtNull() throws Exception {
        // WHEN
        try {
            travailManager.verifierExistanceTravail(10L, 1L, 3L);
            fail("exception attendue");
        }
        // THEN
        catch (LearningsException e) {
            assertThat(e.getMessage()).isEqualTo("Un des deux utilisateurs a déjà rendu un travail avec un binôme différent.");
            verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(10L), Mockito.eq(1L));
            verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(10L), Mockito.eq(3L));
            verify(travailDao, Mockito.times(2)).getTravailUtilisateurParSeance(Mockito.anyLong(), Mockito.anyLong());
        }
    }

    @Test
    public void shouldNotVerifierExistanceTravailAvecBinomeDifferent() throws Exception {
        // WHEN
        try {
            travailManager.verifierExistanceTravail(11L, 1L, 2L);
            fail("exception attendue");
        }
        // THEN
        catch (LearningsException e) {
            assertThat(e.getMessage()).isEqualTo("Les deux utilisateur ont déjà rendu un travail dans des binômes différents.");
            verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(11L), Mockito.eq(1L));
            verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(11L), Mockito.eq(2L));
            verify(travailDao, Mockito.times(2)).getTravailUtilisateurParSeance(Mockito.anyLong(), Mockito.anyLong());
        }
    }

    @Test
    public void shouldNotVerifierExistanceTravailAvecBinomeSplit() throws Exception {
        // WHEN
        try {
            travailManager.verifierExistanceTravail(10L, 1L, null);
            fail("exception attendue");
        }
        // THEN
        catch (LearningsException e) {
            assertThat(e.getMessage()).isEqualTo("L'utilisateur a déjà rendu un travail avec un binôme différent.");
            verify(travailDao).getTravailUtilisateurParSeance(Mockito.eq(10L), Mockito.eq(1L));
            verify(travailDao).getTravailUtilisateurParSeance(Mockito.anyLong(), Mockito.anyLong());
        }
    }

    @Test
    public void shouldVerifierUtilisateurAvantRenduAvecExistant() {
        // WHEN
        Utilisateur utilisateur = travailManager.verifierUtilisateurAvantRendu(1L, true);
        // THEN
        assertThat(utilisateur).isEqualTo(utilisateur1);
        verify(utilisateurDao).getUtilisateur(Mockito.eq(1L));
        verify(utilisateurDao).getUtilisateur(Mockito.anyLong());
    }

    @Test
    public void shouldVerifierUtilisateurAvantRenduAvecIdNull() {
        // WHEN
        Utilisateur utilisateur = travailManager.verifierUtilisateurAvantRendu(null, false);
        // THEN
        assertThat(utilisateur).isNull();
        verify(utilisateurDao, Mockito.never()).getUtilisateur(Mockito.anyLong());
    }

    @Test
    public void shouldNotVerifierUtilisateurAvantRenduAvecIdNull() {
        // WHEN
        try {
            travailManager.verifierUtilisateurAvantRendu(null, true);
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Un utilisateur obligatoire n'est pas renseigné.");
            verify(utilisateurDao, Mockito.never()).getUtilisateur(Mockito.anyLong());
        }
    }

    @Test
    public void shouldNotVerifierUtilisateurAvantRenduAvecInconnu() {
        // WHEN
        try {
            travailManager.verifierUtilisateurAvantRendu(3L, true);
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Un utilisateur est inconnu.");
            verify(utilisateurDao).getUtilisateur(Mockito.eq(3L));
            verify(utilisateurDao).getUtilisateur(Mockito.anyLong());
        }
    }

    @Test
    public void shouldNotVerifierUtilisateurAvantRenduAvecAdmin() {
        // WHEN
        try {
            travailManager.verifierUtilisateurAvantRendu(0L, true);
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Un administrateur ne peut pas rendre de TP.");
            verify(utilisateurDao).getUtilisateur(Mockito.eq(0L));
            verify(utilisateurDao).getUtilisateur(Mockito.anyLong());
        }
    }

    @Test
    public void shouldVerifierTpAvantRendu() {
        // WHEN
        Seance seance = travailManager.verifierTpAvantRendu(1L);
        // THEN
        assertThat(seance).isEqualTo(seance1);
        verify(seanceDao).getSeance(Mockito.eq(1L));
        verify(seanceDao).getSeance(Mockito.anyLong());
    }

    @Test
    public void shouldNotVerifierTpAvantRenduAvecIdNull() {
        // WHEN
        try {
            travailManager.verifierTpAvantRendu(null);
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("L'identifiant du tp est incorrect");
            verify(seanceDao, Mockito.never()).getSeance(Mockito.anyLong());
        }
    }

    @Test
    public void shouldNotVerifierTpAvantRenduAvecSeanceInexistante() {
        // WHEN
        try {
            travailManager.verifierTpAvantRendu(-1L);
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("L'identifiant du tp est incorrect");
            verify(seanceDao).getSeance(Mockito.eq(-1L));
            verify(seanceDao).getSeance(Mockito.anyLong());
        }
    }

    @Test
    public void shouldNotVerifierTpAvantRenduAvecSeanceNonNotee() {
        // WHEN
        try {
            travailManager.verifierTpAvantRendu(2L);
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("L'identifiant du tp est incorrect");
            verify(seanceDao).getSeance(Mockito.eq(2L));
            verify(seanceDao).getSeance(Mockito.anyLong());
        }
    }

    @Test
    public void shouldNotVerifierTpAvantRenduAvecSeanceCours() {
        // WHEN
        try {
            travailManager.verifierTpAvantRendu(3L);
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("L'identifiant du tp est incorrect");
            verify(seanceDao).getSeance(Mockito.eq(3L));
            verify(seanceDao).getSeance(Mockito.anyLong());
        }
    }

    @Test
    public void shouldNotVerifierTpAvantRenduAvecSeanceDepassee() {
        // WHEN
        try {
            travailManager.verifierTpAvantRendu(4L);
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Le travail ne peut pas être rendu maintenant");
            verify(seanceDao).getSeance(Mockito.eq(4L));
            verify(seanceDao).getSeance(Mockito.anyLong());
        }
    }

    @Test
    public void shouldNotVerifierTpAvantRenduAvecSeanceFuture() {
        // WHEN
        try {
            travailManager.verifierTpAvantRendu(5L);
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Le travail ne peut pas être rendu maintenant");
            verify(seanceDao).getSeance(Mockito.eq(5L));
            verify(seanceDao).getSeance(Mockito.anyLong());
        }
    }

    @Test
    public void shouldRendreTP() throws Exception {
        // WHEN
        travailManager.rendreTP(1L, 1L, 2L, "commentaire", "nouveauFichier.zip", inputStream1, 10L);
    }

    @Test
    public void shouldNotRendreTPAvecMemeUtilisateurs() throws Exception {
        // WHEN
        try {
            travailManager.rendreTP(1L, 1L, 1L, "commentaire", "nouveauFichier.zip", inputStream1, 10L);
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Les deux utilisateurs doivent être différents.");
        }
    }

    @Test
    public void shouldNotRendreTPAvecFichierTropGros() throws Exception {
        // WHEN
        try {
            travailManager.rendreTP(1L, 1L, 2L, "commentaire", "nouveauFichier.zip", inputStream1, 20 * 1024 * 1024L);
            fail("exception attendue");
        }
        // THEN
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Le fichier est trop gros.");
        }
    }
}
