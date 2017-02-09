package learnings.managers;

import learnings.dao.ProjetDao;
import learnings.dao.TravailDao;
import learnings.dao.UtilisateurDao;
import learnings.enums.Groupe;
import learnings.exceptions.LearningsSecuriteException;
import learnings.model.Seance;
import learnings.model.Travail;
import learnings.model.Utilisateur;
import learnings.pojos.EleveAvecTravauxEtProjet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UtilisateurManagerTestCase {

	@Mock
	private UtilisateurDao utilisateurDao;

	@Mock
	private TravailDao travailDao;

	@Mock
	private ProjetDao projetDao;

	@Mock
	private MotDePasseManager motDePasseManager;

	@InjectMocks
	private UtilisateurManager utilisateurManager = UtilisateurManager.getInstance();

	private Utilisateur utilisateur3 = new Utilisateur(null, "nom3", "prenom3", "email3", null, true);

	@Before
	public void init() throws Exception {
		List<Travail> travaux = new ArrayList<>();
		travaux.add(new Travail(1L, null, null, null, null, null,null));

		List<Travail> travauxAvecNote = new ArrayList<>();
		travauxAvecNote.add(new Travail(2L, new Seance(1L,null,null,null), new BigDecimal(10), null, null, null,null));
		travauxAvecNote.add(new Travail(3L, new Seance(2L,null,null,null), new BigDecimal(8), null, null, null, null));
		travauxAvecNote.add(new Travail(4L, new Seance(3L,null,null,null), new BigDecimal(15), null, null, null, null));

		Travail travailProjetAvecNote = new Travail(5L, null, new BigDecimal(13), null, null, null, null);

		List<Utilisateur> elevesPourNotes = new ArrayList<>();
		when(travailDao.listerTravauxParUtilisateur(1L)).thenReturn(new ArrayList<>());
		when(travailDao.listerTravauxParUtilisateur(2L)).thenReturn(travaux);
		when(utilisateurDao.getMotDePasseUtilisateurHashe(Mockito.eq("email1"))).thenReturn("motDePasseHash");
		when(utilisateurDao.getUtilisateur(Mockito.eq(1L))).thenReturn(new Utilisateur(1L, "nom1", "prenom1", "email1", Groupe.GROUPE_1, false));
		when(utilisateurDao.getUtilisateur(Mockito.eq(2L))).thenReturn(new Utilisateur(1L, "nom2", "prenom2", "email2", Groupe.GROUPE_2, false));
		when(utilisateurDao.getUtilisateur(Mockito.eq("email1"))).thenReturn(new Utilisateur(1L, "nom1", "prenom1", "email1", Groupe.GROUPE_1, false));
		when(utilisateurDao.ajouterUtilisateur(Mockito.eq(utilisateur3), Mockito.eq("email3Hash"))).thenReturn(
				new Utilisateur(3L, "nom3", "prenom3", "email3", null, true));
		elevesPourNotes.add(new Utilisateur(3L, "eleveNom3", "elevePrenom3", "eleve3@mail.com", Groupe.GROUPE_1,  false));
		elevesPourNotes.add(new Utilisateur(4L, "eleveNom4", "elevePrenom4", "eleve4@mail.com", Groupe.GROUPE_2,  false));

		when(travailDao.listerTravauxParUtilisateur(3L)).thenReturn(travauxAvecNote);
		when(travailDao.listerTravauxParUtilisateur(4L)).thenReturn(travauxAvecNote);
		when(travailDao.getTravailUtilisateurParProjet(10L, 3L)).thenReturn(travailProjetAvecNote);
		when(travailDao.getTravailUtilisateurParProjet(10L, 4L)).thenReturn(travailProjetAvecNote);
		when(utilisateurDao.listerEleves()).thenReturn(elevesPourNotes);

		when(motDePasseManager.validerMotDePasse(Mockito.eq("motDePasse"), Mockito.eq("motDePasseHash"))).thenReturn(true);
		when(motDePasseManager.validerMotDePasse(Mockito.eq("hashException"), Mockito.eq("motDePasseHash"))).thenThrow(new NoSuchAlgorithmException());
		when(motDePasseManager.genererMotDePasse(Mockito.eq("email1"))).thenReturn("email1Hash");
		when(motDePasseManager.genererMotDePasse(Mockito.eq("email2"))).thenThrow(new NoSuchAlgorithmException());
		when(motDePasseManager.genererMotDePasse(Mockito.eq("email3"))).thenReturn("email3Hash");

		Mockito.when(projetDao.getLastProjetId()).thenReturn(10L);
	}

	@Test
	public void shouldListerUtilisateurs() {
		// WHEN
		utilisateurManager.listerUtilisateurs();
		// THEN
		verify(utilisateurDao).listerUtilisateurs();
	}

	@Test
	public void shouldListerAutresEleves() {
		// WHEN
		utilisateurManager.listerAutresEleves(1L);
		// THEN
		verify(utilisateurDao).listerAutresEleves(Mockito.eq(1L));
		verify(utilisateurDao).listerAutresEleves(Mockito.anyLong());
	}

	@Test
	public void shouldNotListerAutresElevesAvecIdNull() {
		// WHEN
		try {
			utilisateurManager.listerAutresEleves(null);
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			verify(utilisateurDao, Mockito.never()).listerAutresEleves(Mockito.anyLong());
		}
	}

	@Test
	public void shouldGetUtilisateurParMail() {
		// WHEN
		utilisateurManager.getUtilisateur("email");
		// THEN
		verify(utilisateurDao).getUtilisateur(Mockito.eq("email"));
		verify(utilisateurDao).getUtilisateur(Mockito.anyString());
	}

	@Test
	public void shouldGetUtilisateurParMailAvecEmailNull() {
		// WHEN
		try {
			utilisateurManager.getUtilisateur(null);
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			verify(utilisateurDao, Mockito.never()).getUtilisateur(Mockito.anyString());
		}
	}

	@Test
	public void shouldNotGetUtilisateurParMailAvecEmailVide() {
		// WHEN
		try {
			utilisateurManager.getUtilisateur("");
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			verify(utilisateurDao, Mockito.never()).getUtilisateur(Mockito.anyString());
		}
	}

	@Test
	public void shouldSupprimerUtilisateur() {
		// WHEN
		utilisateurManager.supprimerUtilisateur(1L);
		// THEN
		verify(utilisateurDao).supprimerUtilisateur(Mockito.eq(1L));
		verify(utilisateurDao).supprimerUtilisateur(Mockito.anyLong());
	}

	@Test
	public void shouldNotSupprimerUtilisateurAvecIdNull() {
		// WHEN
		try {
			utilisateurManager.supprimerUtilisateur(null);
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("L'id de l'utilisateur ne peut pas être null.");
			verify(utilisateurDao, Mockito.never()).supprimerUtilisateur(Mockito.anyLong());
		}
	}

	@Test
	public void shouldNotSupprimerUtilisateurAvecTravauxExistant() {
		// WHEN
		try {
			utilisateurManager.supprimerUtilisateur(2L);
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("Impossible de supprimer un utilisateur avec des travaux rendus.");
			verify(utilisateurDao, Mockito.never()).supprimerUtilisateur(Mockito.anyLong());
		}
	}

	@Test
	public void shouldEnleverDroitAdmin() {
		// WHEN
		utilisateurManager.enleverDroitsAdmin(1L);
		// THEN
		verify(utilisateurDao).modifierRoleAdmin(Mockito.eq(1L), Mockito.eq(false));
		verify(utilisateurDao).modifierRoleAdmin(Mockito.anyLong(), Mockito.anyBoolean());
	}

	@Test
	public void shouldNotEnleverDroitAdminAvecIdNull() {
		// WHEN
		try {
			utilisateurManager.enleverDroitsAdmin(null);
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("L'id de l'utilisateur ne peut pas être null.");
			verify(utilisateurDao, Mockito.never()).modifierRoleAdmin(Mockito.anyLong(), Mockito.anyBoolean());
		}
	}

	@Test
	public void shouldDonnerDroitAdmin() {
		// WHEN
		utilisateurManager.donnerDroitsAdmin(1L);
		// THEN
		verify(utilisateurDao).modifierRoleAdmin(Mockito.eq(1L), Mockito.eq(true));
		verify(utilisateurDao).modifierRoleAdmin(Mockito.anyLong(), Mockito.anyBoolean());
	}

	@Test
	public void shouldNotDonnerDroitAdminAvecIdNull() {
		// WHEN
		try {
			utilisateurManager.donnerDroitsAdmin(null);
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("L'id de l'utilisateur ne peut pas être null.");
			verify(utilisateurDao, Mockito.never()).modifierRoleAdmin(Mockito.anyLong(), Mockito.anyBoolean());
		}
	}

	@Test
	public void shouldValiderMotDePasse() throws Exception {
		// WHEN
		boolean valide = utilisateurManager.validerMotDePasse("email1", "motDePasse");
		// THEN
		assertThat(valide).isTrue();
		verify(utilisateurDao).getMotDePasseUtilisateurHashe(Mockito.eq("email1"));
		verify(utilisateurDao).getMotDePasseUtilisateurHashe(Mockito.anyString());

		verify(motDePasseManager).validerMotDePasse("motDePasse", "motDePasseHash");
		verify(motDePasseManager).validerMotDePasse(Mockito.anyString(), Mockito.anyString());
	}

	@Test
	public void shouldNotValiderMotDePasseAvecEmailNull() throws Exception {
		// WHEN
		try {
			utilisateurManager.validerMotDePasse(null, "motDePasse");
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("L'identifiant doit être renseigné.");
			verify(utilisateurDao, Mockito.never()).getMotDePasseUtilisateurHashe(Mockito.anyString());
			verify(motDePasseManager, Mockito.never()).validerMotDePasse(Mockito.anyString(), Mockito.anyString());
		}
	}

	@Test
	public void shouldNotValiderMotDePasseAvecEmailVide() throws Exception {
		// WHEN
		try {
			utilisateurManager.validerMotDePasse("", "motDePasse");
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("L'identifiant doit être renseigné.");
			verify(utilisateurDao, Mockito.never()).getMotDePasseUtilisateurHashe(Mockito.anyString());
			verify(motDePasseManager, Mockito.never()).validerMotDePasse(Mockito.anyString(), Mockito.anyString());
		}
	}

	@Test
	public void shouldNotValiderMotDePasseAvecMotDePasseNull() throws Exception {
		// WHEN
		try {
			utilisateurManager.validerMotDePasse("email1", null);
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("Le mot de passe doit être renseigné.");
			verify(utilisateurDao, Mockito.never()).getMotDePasseUtilisateurHashe(Mockito.anyString());
			verify(motDePasseManager, Mockito.never()).validerMotDePasse(Mockito.anyString(), Mockito.anyString());
		}
	}

	@Test
	public void shouldNotValiderMotDePasseAvecMotDePasseVide() throws Exception {
		// WHEN
		try {
			utilisateurManager.validerMotDePasse("email1", "");
			fail("exception attendue");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("Le mot de passe doit être renseigné.");
			verify(utilisateurDao, Mockito.never()).getMotDePasseUtilisateurHashe(Mockito.anyString());
			verify(motDePasseManager, Mockito.never()).validerMotDePasse(Mockito.anyString(), Mockito.anyString());
		}
	}

	@Test
	public void shouldNotValiderMotDePasseAvecEmailInexistant() throws Exception {
		// WHEN
		try {
			utilisateurManager.validerMotDePasse("email2", "motDePasse");
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("L'identifiant n'est pas connu.");
			verify(utilisateurDao).getMotDePasseUtilisateurHashe("email2");
			verify(utilisateurDao).getMotDePasseUtilisateurHashe(Mockito.anyString());
			verify(motDePasseManager, Mockito.never()).validerMotDePasse(Mockito.anyString(), Mockito.anyString());
		}
	}

	@Test
	public void shouldNotValiderMotDePasseAvecSecurityException() throws Exception {
		// WHEN
		try {
			utilisateurManager.validerMotDePasse("email1", "hashException");
			fail("exception attendue");
		}
		// THEN
		catch (LearningsSecuriteException e) {
			assertThat(e.getMessage()).isEqualTo("Problème dans la vérification du mot de passe.");
			verify(utilisateurDao).getMotDePasseUtilisateurHashe("email1");
			verify(utilisateurDao).getMotDePasseUtilisateurHashe(Mockito.anyString());
			verify(motDePasseManager).validerMotDePasse(Mockito.eq("hashException"), Mockito.eq("motDePasseHash"));
			verify(motDePasseManager).validerMotDePasse(Mockito.anyString(), Mockito.anyString());
		}
	}

	@Test
	public void shouldReinitialiserMotDePasse() throws Exception {
		// WHEN
		utilisateurManager.reinitialiserMotDePasse(1L);
		// THEN
		verify(utilisateurDao).getUtilisateur(Mockito.eq(1L));
		verify(utilisateurDao).getUtilisateur(Mockito.anyLong());

		verify(motDePasseManager).genererMotDePasse(Mockito.eq("email1"));
		verify(motDePasseManager).genererMotDePasse(Mockito.anyString());

		verify(utilisateurDao).modifierMotDePasse(Mockito.eq(1L), Mockito.eq("email1Hash"));
		verify(utilisateurDao).modifierMotDePasse(Mockito.anyLong(), Mockito.anyString());

	}

	@Test
	public void shouldNotReinitialiserMotDePasseAvecIdNull() throws Exception {
		// WHEN
		try {
			utilisateurManager.reinitialiserMotDePasse(null);
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("L'identifiant de l'utilisateur ne peut pas être null.");
			verify(utilisateurDao, Mockito.never()).getUtilisateur(Mockito.anyLong());
			verify(motDePasseManager, Mockito.never()).genererMotDePasse(Mockito.anyString());
			verify(utilisateurDao, Mockito.never()).modifierMotDePasse(Mockito.anyLong(), Mockito.anyString());
		}
	}

	@Test
	public void shouldNotReinitialiserMotDePasseAvecPatientNull() throws Exception {
		// WHEN
		try {
			utilisateurManager.reinitialiserMotDePasse(-1L);
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("L'utilisateur n'est pas connu.");
			verify(utilisateurDao).getUtilisateur(Mockito.eq(-1L));
			verify(utilisateurDao).getUtilisateur(Mockito.anyLong());
			verify(motDePasseManager, Mockito.never()).genererMotDePasse(Mockito.anyString());
			verify(utilisateurDao, Mockito.never()).modifierMotDePasse(Mockito.anyLong(), Mockito.anyString());
		}
	}

	@Test
	public void shouldNotReinitialiserMotDePasseAvecExceptionSecurite() throws Exception {
		// WHEN
		try {
			utilisateurManager.reinitialiserMotDePasse(2L);
			fail("exception attendue");
		}
		// THEN
		catch (LearningsSecuriteException e) {
			assertThat(e.getMessage()).isEqualTo("Problème dans la génération du mot de passe.");
			verify(utilisateurDao).getUtilisateur(Mockito.eq(2L));
			verify(utilisateurDao).getUtilisateur(Mockito.anyLong());
			verify(motDePasseManager).genererMotDePasse(Mockito.eq("email2"));
			verify(motDePasseManager).genererMotDePasse(Mockito.anyString());
			verify(utilisateurDao, Mockito.never()).modifierMotDePasse(Mockito.anyLong(), Mockito.anyString());
		}
	}

	@Test
	public void shouldAjouterUtilisateur() throws Exception {
		// WHEN
		Utilisateur utilisateur = utilisateurManager.ajouterUtilisateur(utilisateur3);
		// THEN
		assertThat(utilisateur.getId()).isEqualTo(3L);
		assertThat(utilisateur.getEmail()).isEqualTo("email3");
		assertThat(utilisateur.isAdmin()).isTrue();
		verify(utilisateurDao).getUtilisateur(Mockito.eq("email3"));
		verify(utilisateurDao).getUtilisateur(Mockito.anyString());
		verify(motDePasseManager).genererMotDePasse(Mockito.eq("email3"));
		verify(motDePasseManager).genererMotDePasse(Mockito.anyString());
		verify(utilisateurDao).ajouterUtilisateur(Mockito.eq(utilisateur3), Mockito.eq("email3Hash"));
		verify(utilisateurDao).ajouterUtilisateur(Mockito.any(Utilisateur.class), Mockito.anyString());
	}

	@Test
	public void shouldNotAjouterUtilisateurAvecEmailNull() throws Exception {
		// WHEN
		try {
			utilisateurManager.ajouterUtilisateur(new Utilisateur(null, "nom3", "prenom3", null, null, true));
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("L'adresse email doit être renseigné.");
			verify(utilisateurDao, Mockito.never()).getUtilisateur(Mockito.anyString());
			verify(motDePasseManager, Mockito.never()).genererMotDePasse(Mockito.anyString());
			verify(utilisateurDao, Mockito.never()).ajouterUtilisateur(Mockito.any(Utilisateur.class), Mockito.anyString());
		}
	}

	@Test
	public void shouldNotAjouterUtilisateurAvecEmailVide() throws Exception {
		// WHEN
		try {
			utilisateurManager.ajouterUtilisateur(new Utilisateur(null, "nom3", "prenom3", "", null, true));
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("L'adresse email doit être renseigné.");
			verify(utilisateurDao, Mockito.never()).getUtilisateur(Mockito.anyString());
			verify(motDePasseManager, Mockito.never()).genererMotDePasse(Mockito.anyString());
			verify(utilisateurDao, Mockito.never()).ajouterUtilisateur(Mockito.any(Utilisateur.class), Mockito.anyString());
		}

	}

	@Test
	public void shouldNotAjouterUtilisateurAvecUtilisateurExistant() throws Exception {
		// WHEN
		try {
			utilisateurManager.ajouterUtilisateur(new Utilisateur(null, "nom1", "prenom1", "email1", null, true));
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("L'identifiant est déjà utilisé.");
			verify(utilisateurDao).getUtilisateur(Mockito.eq("email1"));
			verify(utilisateurDao).getUtilisateur(Mockito.anyString());
			verify(motDePasseManager, Mockito.never()).genererMotDePasse(Mockito.anyString());
			verify(utilisateurDao, Mockito.never()).ajouterUtilisateur(Mockito.any(Utilisateur.class), Mockito.anyString());
		}
	}

	@Test
	public void shouldNotAjouterUtilisateurAvecExceptionSecurite() throws Exception {
		// WHEN
		try {
			utilisateurManager.ajouterUtilisateur(new Utilisateur(null, "nom2", "prenom2", "email2", null, true));
			fail("exception attendue");
		}
		// THEN
		catch (LearningsSecuriteException e) {
			assertThat(e.getMessage()).isEqualTo("Problème dans la génération du mot de passe.");
			verify(utilisateurDao).getUtilisateur(Mockito.eq("email2"));
			verify(utilisateurDao).getUtilisateur(Mockito.anyString());
			verify(motDePasseManager).genererMotDePasse(Mockito.eq("email2"));
			verify(motDePasseManager).genererMotDePasse(Mockito.anyString());
			verify(utilisateurDao, Mockito.never()).ajouterUtilisateur(Mockito.any(Utilisateur.class), Mockito.anyString());
		}
	}

	@Test
	public void shouldModifierMotDePasse() throws Exception {
		// WHEN
		utilisateurManager.modifierMotDePasse(1L, "email1", "email1");
		// THEN
		verify(motDePasseManager).genererMotDePasse(Mockito.eq("email1"));
		verify(motDePasseManager).genererMotDePasse(Mockito.anyString());
		verify(utilisateurDao).modifierMotDePasse(Mockito.eq(1L), Mockito.eq("email1Hash"));
		verify(utilisateurDao).modifierMotDePasse(Mockito.anyLong(), Mockito.anyString());
	}

	@Test
	public void shouldNotModifierMotDePasseAvecPremierNull() throws Exception {
		// WHEN
		try {
			utilisateurManager.modifierMotDePasse(1L, null, "email1");
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("Les mots de passe doivent être renseignés.");
			verify(motDePasseManager, Mockito.never()).genererMotDePasse(Mockito.anyString());
			verify(utilisateurDao, Mockito.never()).modifierMotDePasse(Mockito.anyLong(), Mockito.anyString());
		}
	}

	@Test
	public void shouldNotModifierMotDePasseAvecPremierVide() throws Exception {
		// WHEN
		try {
			utilisateurManager.modifierMotDePasse(1L, "", "email1");
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("Les mots de passe doivent être renseignés.");
			verify(motDePasseManager, Mockito.never()).genererMotDePasse(Mockito.anyString());
			verify(utilisateurDao, Mockito.never()).modifierMotDePasse(Mockito.anyLong(), Mockito.anyString());
		}
	}

	@Test
	public void shouldNotModifierMotDePasseAvecDeuxiemeNull() throws Exception {
		// WHEN
		try {
			utilisateurManager.modifierMotDePasse(1L, "email1", null);
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("Les mots de passe doivent être renseignés.");
			verify(motDePasseManager, Mockito.never()).genererMotDePasse(Mockito.anyString());
			verify(utilisateurDao, Mockito.never()).modifierMotDePasse(Mockito.anyLong(), Mockito.anyString());
		}
	}

	@Test
	public void shouldNotModifierMotDePasseAvecDeuxiemeVide() throws Exception {
		// WHEN
		try {
			utilisateurManager.modifierMotDePasse(1L, "email1", "");
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("Les mots de passe doivent être renseignés.");
			verify(motDePasseManager, Mockito.never()).genererMotDePasse(Mockito.anyString());
			verify(utilisateurDao, Mockito.never()).modifierMotDePasse(Mockito.anyLong(), Mockito.anyString());
		}
	}

	@Test
	public void shouldNotModifierMotDePasseAvecMotDePassesDifferents() throws Exception {
		// WHEN
		try {
			utilisateurManager.modifierMotDePasse(1L, "email1", "email2");
			fail("exception attendue");
		}
		// THEN
		catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("La confirmation du mot de passe ne correspond pas.");
			verify(motDePasseManager, Mockito.never()).genererMotDePasse(Mockito.anyString());
			verify(utilisateurDao, Mockito.never()).modifierMotDePasse(Mockito.anyLong(), Mockito.anyString());
		}
	}

	@Test
	public void shouldNotModifierMotDePasseAvecExceptionSecurite() throws Exception {
		// WHEN
		try {
			utilisateurManager.modifierMotDePasse(1L, "email2", "email2");
			fail("exception attendue");
		}
		// THEN
		catch (LearningsSecuriteException e) {
			assertThat(e.getMessage()).isEqualTo("Problème dans la génération du mot de passe.");
			verify(motDePasseManager).genererMotDePasse(Mockito.eq("email2"));
			verify(motDePasseManager).genererMotDePasse(Mockito.anyString());
			verify(utilisateurDao, Mockito.never()).modifierMotDePasse(Mockito.anyLong(), Mockito.anyString());
		}
	}

	@Test
	public void shouldReturnElevesAvecTravauxEtProjetEtMoyenne(){
		//WHEN
		List<EleveAvecTravauxEtProjet> elevesComplets = utilisateurManager.listerElevesAvecTravauxEtProjet();

		//THEN
		assertThat(elevesComplets).extracting("id").contains(3L,4L);

		assertThat(elevesComplets).extracting("projet.note").contains(new BigDecimal(13));

		assertThat(elevesComplets.get(0).getMapSeanceIdTravail().get(1L).getNote()).isEqualTo(new BigDecimal(10));
		assertThat(elevesComplets.get(0).getMapSeanceIdTravail().get(3L).getNote()).isEqualTo(new BigDecimal(15));

		assertThat(elevesComplets).extracting("moyenne").containsOnly(new BigDecimal(12.14).setScale(2,BigDecimal.ROUND_HALF_EVEN));

	}

	@Test
	public void shouldListerEmailElevesPourEnvoi() {
		// WHEN
		String emails = utilisateurManager.listerEmailsElevesPourEnvoi();
		// THEN
		assertThat(emails).matches("eleve[34]@mail.com;eleve[34]@mail.com");
	}

}
