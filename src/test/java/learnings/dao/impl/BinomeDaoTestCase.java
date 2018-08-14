package learnings.dao.impl;

import learnings.AbstractDaoTestCase;
import learnings.dao.BinomeDao;
import learnings.model.Binome;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


public class BinomeDaoTestCase extends AbstractDaoTestCase {
    private BinomeDao binomeDao = new BinomeDaoImpl();

    @Before
    public void init() throws Exception {
        super.purgeBaseDeDonnees();

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()
        ) {
            stmt.executeUpdate("INSERT INTO utilisateur(id,nom,prenom,email,groupe,motdepasse,admin) VALUES(1,'nom', 'prenom', 'eleve@learnings-devwebhei.fr', 'GROUPE_1', 'XXXX',0)");
            stmt.executeUpdate("INSERT INTO utilisateur(id,nom,prenom,email,groupe,motdepasse,admin) VALUES(2,'admin', 'admin', 'admin@learnings-devwebhei.fr', null, 'XXXX',1)");
            stmt.executeUpdate("INSERT INTO utilisateur(id,nom,prenom,email,groupe,motdepasse,admin) VALUES(3,'nom2', 'prenom2', 'eleve2@learnings-devwebhei.fr', 'GROUPE_2', 'XXXX',0)");
            stmt.executeUpdate("INSERT INTO utilisateur(id,nom,prenom,email,groupe,motdepasse,admin) VALUES(4,'nom3', 'prenom3', 'eleve3@learnings-devwebhei.fr', 'GROUPE_1', 'XXXX',0)");
            stmt.executeUpdate("INSERT INTO seance(id,titre,description,date,type) VALUES(1,'TP1','TP de debuggage','2014-07-26','TP')");
            stmt.executeUpdate("INSERT INTO seance(id,titre,description,date,type) VALUES(2,'TP2','TP de correction','2014-08-26','TP')");
            stmt.executeUpdate("INSERT INTO binome(eleve_id, seance_id, binome_uid) VALUES(1, 1, 'uid1')");
            stmt.executeUpdate("INSERT INTO binome(eleve_id, seance_id, binome_uid) VALUES(3, 1, 'uid2')");
            stmt.executeUpdate("INSERT INTO binome(eleve_id, seance_id, binome_uid) VALUES(4, 1, 'uid1')");
            stmt.executeUpdate("INSERT INTO binome(eleve_id, seance_id, binome_uid) VALUES(1, 2, 'uid3')");
            stmt.executeUpdate("INSERT INTO binome(eleve_id, seance_id, binome_uid) VALUES(3, 2, 'uid3')");
            stmt.executeUpdate("INSERT INTO binome(eleve_id, seance_id, binome_uid) VALUES(4, 2, 'uid3')");
        }
    }

    @Test
    public void shouldGetBinomeByUid() {
        // WHEN
        Binome binome = binomeDao.getBinome("uid1");
        // THEN
        assertThat(binome).isNotNull();
        assertThat(binome.getUid()).isEqualTo("uid1");
        assertThat(binome.getSeance().getId()).isEqualTo(1);
        assertThat(binome.getEleves()).hasSize(2);
        assertThat(binome.getEleves()).extracting("id", "nom", "prenom").containsOnly(
                tuple(1L, "nom", "prenom"),
                tuple(4L, "nom3", "prenom3")
        );
    }

    @Test
    public void shouldGetBinomeBySeanceAndEleve() {
        // WHEN
        Binome binome = binomeDao.getBinome(1L, 4L);
        // THEN
        assertThat(binome).isNotNull();
        assertThat(binome.getUid()).isEqualTo("uid1");
        assertThat(binome.getSeance().getId()).isEqualTo(1);
        assertThat(binome.getEleves()).hasSize(2);
        assertThat(binome.getEleves()).extracting("id", "nom", "prenom").containsOnly(
                tuple(1L, "nom", "prenom"),
                tuple(4L, "nom3", "prenom3")
        );
    }
    
}
