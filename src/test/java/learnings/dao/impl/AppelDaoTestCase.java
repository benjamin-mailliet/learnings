package learnings.dao.impl;

import learnings.dao.AppelDao;
import learnings.enums.StatutAppel;
import learnings.model.Appel;
import learnings.model.Utilisateur;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.tuple;

public class AppelDaoTestCase extends  AbstractTestCase{

    private AppelDao appelDao = new AppelDaoImpl();

    @Before
    public void init() throws Exception {
        super.purgeBaseDeDonnees();

        Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
        Statement stmt = connection.createStatement();

        stmt.executeUpdate("INSERT INTO utilisateur(id,email,motdepasse,admin) VALUES(1,'eleve@learnings-devwebhei.fr','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0)");
        stmt.executeUpdate("INSERT INTO utilisateur(id,email,motdepasse,admin) VALUES(2,'admin@learnings-devwebhei.fr','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',1)");
        stmt.executeUpdate("INSERT INTO utilisateur(id,email,motdepasse,admin) VALUES(3,'eleve2@learnings-devwebhei.fr','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0)");
        stmt.executeUpdate("INSERT INTO utilisateur(id,email,motdepasse,admin) VALUES(4,'eleve3@learnings-devwebhei.fr','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0)");

        stmt.executeUpdate("INSERT INTO seance(id,titre,description,date,type) VALUES(1,'cours1','cours de debuggage','2014-07-26','COURS')");
        stmt.executeUpdate("INSERT INTO seance(id,titre,description,date,type) VALUES(2,'cours2','cours de correction','2014-08-26','COURS')");

        stmt.executeUpdate("INSERT INTO appel (idseance, ideleve, statut) VALUES (1, 1, 'PRESENT')");
        stmt.executeUpdate("INSERT INTO appel (idseance, ideleve, statut) VALUES (1, 3, 'ABSENT')");
        stmt.executeUpdate("INSERT INTO appel (idseance, ideleve, statut) VALUES (2, 3, 'PRESENT')");

        stmt.close();
        connection.close();
    }

    @Test
    public void shouldListerAppels() throws Exception {
        // WHEN
        List<Appel> appels = appelDao.listerAppels(1L);
        // THEN
        assertThat(appels).hasSize(3);
        assertThat(appels).extracting("eleve.id", "eleve.email", "statut").containsOnly(
            tuple(1L, "eleve@learnings-devwebhei.fr", StatutAppel.PRESENT),
            tuple(3L, "eleve2@learnings-devwebhei.fr", StatutAppel.ABSENT),
            tuple(4L, "eleve3@learnings-devwebhei.fr", null)
        );
    }

    @Test
    public void shouldAjouterAppel() throws Exception {
        // GIVEN
        Utilisateur eleve = new Utilisateur(1L, "eleve@learnings-devwebhei.fr", false);
        Appel appel = new Appel(eleve, StatutAppel.EXCUSE);
        // WHEN
        appelDao.ajouterAppel(2L, appel);
        // THEN
        try(Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
            Statement stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM appel WHERE idseance=2 AND ideleve = 1");) {
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
        Utilisateur eleve = new Utilisateur(1L, "eleve@learnings-devwebhei.fr", false);
        Appel appel = new Appel(eleve, StatutAppel.EXCUSE);
        // WHEN
        appelDao.modifierAppel(1L, appel);
        // THEN
        try(Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
            Statement stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM appel WHERE idseance=1 AND ideleve = 1");) {
            if (results.next()) {
                assertThat(results.getString("statut")).isEqualTo("EXCUSE");
            } else {
                fail("Pas de ligne trouvée en bdd.");
            }
        }
    }
}
