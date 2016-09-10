package learnings.dao.impl;

import learnings.AbstractDaoTestCase;
import learnings.dao.UtilisateurDao;
import learnings.enums.Groupe;
import learnings.model.Utilisateur;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


public class UtilisateurDaoTestCase extends AbstractDaoTestCase {
    private UtilisateurDao utilisateurDao = new UtilisateurDaoImpl();

    @Before
    public void init() throws Exception {
        super.purgeBaseDeDonnees();

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()
        ) {
            stmt.executeUpdate("INSERT INTO utilisateur(id,nom,prenom,email,groupe,motdepasse,admin) VALUES(1,'nom', 'prenom', 'eleve@learnings-devwebhei.fr', 'GROUPE_1', '6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0)");
            stmt.executeUpdate("INSERT INTO utilisateur(id,nom,prenom,email,groupe,motdepasse,admin) VALUES(2,'admin', 'admin', 'admin@learnings-devwebhei.fr', null, '6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',1)");
            stmt.executeUpdate("INSERT INTO utilisateur(id,nom,prenom,email,groupe,motdepasse,admin) VALUES(3,'nom2', 'prenom2', 'eleve2@learnings-devwebhei.fr', 'GROUPE_2', '6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0)");
            stmt.executeUpdate("INSERT INTO utilisateur(id,nom,prenom,email,groupe,motdepasse,admin) VALUES(4,'nom3', 'prenom3', 'eleve3@learnings-devwebhei.fr', 'GROUPE_1', '6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0)");
        }
    }

    @Test
    public void shouldListerUtilisateurs() {
        // WHEN
        List<Utilisateur> utilisateurs = utilisateurDao.listerUtilisateurs();
        // THEN
        assertThat(utilisateurs).hasSize(4);
        assertThat(utilisateurs).extracting("id", "nom", "prenom", "email", "groupe", "admin").containsExactly(
                tuple(2L, "admin", "admin", "admin@learnings-devwebhei.fr", null, true),
                tuple(3L, "nom2", "prenom2", "eleve2@learnings-devwebhei.fr", Groupe.GROUPE_2, false),
                tuple(4L, "nom3", "prenom3", "eleve3@learnings-devwebhei.fr", Groupe.GROUPE_1, false),
                tuple(1L, "nom", "prenom", "eleve@learnings-devwebhei.fr", Groupe.GROUPE_1, false)
        );
    }

    @Test
    public void shouldListerAutresEleves() {
        // WHEN
        List<Utilisateur> utilisateurs = utilisateurDao.listerAutresEleves(3L);
        // THEN
        assertThat(utilisateurs).hasSize(2);
        assertThat(utilisateurs).extracting("id", "nom", "prenom", "email", "groupe", "admin").containsExactly(
                tuple(4L, "nom3", "prenom3", "eleve3@learnings-devwebhei.fr", Groupe.GROUPE_1, false),
                tuple(1L, "nom", "prenom", "eleve@learnings-devwebhei.fr", Groupe.GROUPE_1, false)
        );
    }

    @Test
    public void shouldGetUtilisateurParId() {
        // WHEN
        Utilisateur utilisateur = utilisateurDao.getUtilisateur(1L);
        // THEN
        assertThat(utilisateur).isNotNull();
        assertThat(utilisateur.getId()).isEqualTo(1L);
        assertThat(utilisateur.getNom()).isEqualTo("nom");
        assertThat(utilisateur.getPrenom()).isEqualTo("prenom");
        assertThat(utilisateur.getEmail()).isEqualTo("eleve@learnings-devwebhei.fr");
        assertThat(utilisateur.getGroupe()).isEqualTo(Groupe.GROUPE_1);
        assertThat(utilisateur.isAdmin()).isFalse();
    }

    @Test
    public void shouldNotGetUtilisateurParIdNonTrouve() {
        // WHEN
        Utilisateur utilisateur = utilisateurDao.getUtilisateur(-1L);
        // THEN
        assertThat(utilisateur).isNull();
    }

    @Test
    public void shouldGetUtilisateurParEmail() {
        // WHEN
        Utilisateur utilisateur = utilisateurDao.getUtilisateur("eleve@learnings-devwebhei.fr");
        // THEN
        assertThat(utilisateur).isNotNull();
        assertThat(utilisateur.getId()).isEqualTo(1L);
        assertThat(utilisateur.getNom()).isEqualTo("nom");
        assertThat(utilisateur.getPrenom()).isEqualTo("prenom");
        assertThat(utilisateur.getEmail()).isEqualTo("eleve@learnings-devwebhei.fr");
        assertThat(utilisateur.getGroupe()).isEqualTo(Groupe.GROUPE_1);
        assertThat(utilisateur.isAdmin()).isFalse();
    }

    @Test
    public void shouldNotGetUtilisateurParEmailNonTrouve() {
        // WHEN
        Utilisateur utilisateur = utilisateurDao.getUtilisateur("nonexistant@learnings-devwebhei.fr");
        // THEN
        assertThat(utilisateur).isNull();
    }

    @Test
    public void shouldGetMotDePasse() {
        // WHEN
        String motDePasse = utilisateurDao.getMotDePasseUtilisateurHashe("eleve@learnings-devwebhei.fr");
        // THEN
        assertThat(motDePasse).isEqualTo("6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81");
    }

    @Test
    public void shouldNotGetMotDePasseNonTrouve() {
        // WHEN
        String motDePasse = utilisateurDao.getMotDePasseUtilisateurHashe("nonexistant@learnings-devwebhei.fr");
        // THEN
        assertThat(motDePasse).isNull();
    }

    @Test
    public void shouldSupprimerUtilisateur() throws Exception{
        // WHEN
        utilisateurDao.supprimerUtilisateur(1L);
        // THEN
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet results = stmt.executeQuery("SELECT * FROM utilisateur WHERE id=1")
        ) {
            assertThat(results.next()).isFalse();
        }
    }

    @Test
    public void testChangerRoleAdmin() throws Exception {
        // WHEN
        utilisateurDao.modifierRoleAdmin(1L, true);
        // THEN
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet results = stmt.executeQuery("SELECT * FROM utilisateur WHERE id=1")
        ) {
            assertThat(results.next()).isTrue();
            assertThat(results.getLong("id")).isEqualTo(1L);
            assertThat(results.getString("nom")).isEqualTo("nom");
            assertThat(results.getString("prenom")).isEqualTo("prenom");
            assertThat(results.getString("email")).isEqualTo("eleve@learnings-devwebhei.fr");
            assertThat(results.getString("groupe")).isEqualTo("GROUPE_1");
            assertThat(results.getBoolean("admin")).isTrue();
        }
    }

    @Test
    public void shouldChangerMotDePasse() throws Exception {
        // WHEN
        utilisateurDao.modifierMotDePasse(1L, "NOUVEAU_MOT_DE_PASSE");
        // THEN
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet results = stmt.executeQuery("SELECT * FROM utilisateur WHERE id=1")
        ) {
            assertThat(results.next()).isTrue();
            assertThat(results.getLong("id")).isEqualTo(1L);
            assertThat(results.getString("nom")).isEqualTo("nom");
            assertThat(results.getString("prenom")).isEqualTo("prenom");
            assertThat(results.getString("email")).isEqualTo("eleve@learnings-devwebhei.fr");
            assertThat(results.getString("groupe")).isEqualTo("GROUPE_1");
            assertThat(results.getBoolean("admin")).isFalse();
            assertThat(results.getString("motdepasse")).isEqualTo("NOUVEAU_MOT_DE_PASSE");
        }
    }

    @Test
    public void shouldAjouterUtilisateur() throws Exception {
        Utilisateur utilisateur = utilisateurDao.ajouterUtilisateur(new Utilisateur(null, "nom", "prenom", "email", Groupe.GROUPE_1, true), "motDePasse");
        assertThat(utilisateur.getId()).isNotNull();
        assertThat(utilisateur.getNom()).isEqualTo("nom");
        assertThat(utilisateur.getPrenom()).isEqualTo("prenom");
        assertThat(utilisateur.getEmail()).isEqualTo("email");
        assertThat(utilisateur.getGroupe()).isEqualTo(Groupe.GROUPE_1);
        assertThat(utilisateur.isAdmin()).isTrue();

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM utilisateur WHERE id=?");
        ) {
            stmt.setLong(1, utilisateur.getId());
            try (ResultSet results = stmt.executeQuery()) {
                assertThat(results.next()).isTrue();
                assertThat(results.getLong("id")).isEqualTo(utilisateur.getId());
                assertThat(results.getString("nom")).isEqualTo("nom");
                assertThat(results.getString("prenom")).isEqualTo("prenom");
                assertThat(results.getString("email")).isEqualTo("email");
                assertThat(results.getString("groupe")).isEqualTo("GROUPE_1");
                assertThat(results.getBoolean("admin")).isTrue();
                assertThat(results.getString("motdepasse")).isEqualTo("motDePasse");
            }
        }
    }

    @Test
    public void testAjouterUtilisateurAvecGroupeNull() throws Exception {
        Utilisateur utilisateur = utilisateurDao.ajouterUtilisateur(new Utilisateur(null, "nom", "prenom", "email", null, true), "motDePasse");
        assertThat(utilisateur.getId()).isNotNull();
        assertThat(utilisateur.getNom()).isEqualTo("nom");
        assertThat(utilisateur.getPrenom()).isEqualTo("prenom");
        assertThat(utilisateur.getEmail()).isEqualTo("email");
        assertThat(utilisateur.getGroupe()).isNull();
        assertThat(utilisateur.isAdmin()).isTrue();

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM utilisateur WHERE id=?");
        ) {
            stmt.setLong(1, utilisateur.getId());
            try (ResultSet results = stmt.executeQuery()) {
                assertThat(results.next()).isTrue();
                assertThat(results.getLong("id")).isEqualTo(utilisateur.getId());
                assertThat(results.getString("nom")).isEqualTo("nom");
                assertThat(results.getString("prenom")).isEqualTo("prenom");
                assertThat(results.getString("email")).isEqualTo("email");
                assertThat(results.getString("groupe")).isNull();
                assertThat(results.getBoolean("admin")).isTrue();
                assertThat(results.getString("motdepasse")).isEqualTo("motDePasse");
            }
        }
    }

}
