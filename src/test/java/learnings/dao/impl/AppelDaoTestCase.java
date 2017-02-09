package learnings.dao.impl;

import learnings.AbstractDaoTestCase;
import learnings.dao.AppelDao;
import learnings.enums.Groupe;
import learnings.enums.StatutAppel;
import learnings.model.Appel;
import learnings.model.Utilisateur;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.tuple;

public class AppelDaoTestCase extends AbstractDaoTestCase {

    private AppelDao appelDao = new AppelDaoImpl();

    @Before
    public void init() throws Exception {
        super.purgeBaseDeDonnees();

        try (Connection connection = this.getConnection();
             Statement stmt = connection.createStatement()
        ) {
            stmt.executeUpdate("INSERT INTO utilisateur(id,nom,prenom,email,groupe,motdepasse,admin) VALUES(1,'nom', 'prenom', 'eleve@learnings-devwebhei.fr','GROUPE_1','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0)");
            stmt.executeUpdate("INSERT INTO utilisateur(id,nom,prenom,email,groupe,motdepasse,admin) VALUES(2,'admin', 'admin', 'admin@learnings-devwebhei.fr',null,'6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',1)");
            stmt.executeUpdate("INSERT INTO utilisateur(id,nom,prenom,email,groupe,motdepasse,admin) VALUES(3,'nom2', 'prenom2', 'eleve2@learnings-devwebhei.fr','GROUPE_2','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0)");
            stmt.executeUpdate("INSERT INTO utilisateur(id,nom,prenom,email,groupe,motdepasse,admin) VALUES(4,'nom3', 'prenom3', 'eleve3@learnings-devwebhei.fr','GROUPE_1','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0)");

            stmt.executeUpdate("INSERT INTO seance(id,titre,description,date,type) VALUES(1,'cours1','cours de debuggage','2014-07-26','COURS')");
            stmt.executeUpdate("INSERT INTO seance(id,titre,description,date,type) VALUES(2,'cours2','cours de correction','2014-08-26','COURS')");

            stmt.executeUpdate("INSERT INTO appel (idseance, ideleve, statut) VALUES (1, 1, 'PRESENT')");
            stmt.executeUpdate("INSERT INTO appel (idseance, ideleve, statut) VALUES (1, 3, 'ABSENT')");
            stmt.executeUpdate("INSERT INTO appel (idseance, ideleve, statut) VALUES (2, 3, 'PRESENT')");
        }
    }

    @Test
    public void shouldListerAppels() throws Exception {
        // WHEN
        List<Appel> appels = appelDao.listerAppels(1L);
        // THEN
        assertThat(appels).hasSize(3);
        assertThat(appels).extracting("eleve.id", "eleve.email", "statut").containsExactly(
                tuple(1L, "eleve@learnings-devwebhei.fr", StatutAppel.PRESENT),
                tuple(4L, "eleve3@learnings-devwebhei.fr", null),
                tuple(3L, "eleve2@learnings-devwebhei.fr", StatutAppel.ABSENT)
        );
    }

    @Test
    public void shouldAjouterAppel() throws Exception {
        // GIVEN
        Utilisateur eleve = new Utilisateur(1L, "nom", "prenom", "eleve@learnings-devwebhei.fr", Groupe.GROUPE_1, false);
        Appel appel = new Appel(eleve, StatutAppel.EXCUSE);
        // WHEN
        appelDao.ajouterAppel(2L, appel);
        // THEN
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet results = stmt.executeQuery("SELECT * FROM appel WHERE idseance=2 AND ideleve = 1")
        ) {
            if (results.next()) {
                assertThat(results.getString("statut")).isEqualTo("EXCUSE");
            } else {
                fail("Pas de ligne trouvée en bdd.");
            }
        }
    }

    @Test
    public void shouldModifierAppel() throws Exception {
        // GIVEN
        Utilisateur eleve = new Utilisateur(1L, "nom", "prenom", "eleve@learnings-devwebhei.fr", Groupe.GROUPE_1, false);
        Appel appel = new Appel(eleve, StatutAppel.EXCUSE);
        // WHEN
        appelDao.modifierAppel(1L, appel);
        // THEN
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet results = stmt.executeQuery("SELECT * FROM appel WHERE idseance=1 AND ideleve = 1")
        ) {
            if (results.next()) {
                assertThat(results.getString("statut")).isEqualTo("EXCUSE");
            } else {
                fail("Pas de ligne trouvée en bdd.");
            }
        }
    }
}
