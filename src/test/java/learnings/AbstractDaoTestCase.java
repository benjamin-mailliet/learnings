package learnings;

import learnings.dao.impl.DataSourceProvider;

import java.sql.Connection;
import java.sql.Statement;

public abstract class AbstractDaoTestCase extends AbstractTestCase {

    protected void purgeBaseDeDonnees() throws  Exception{
        Connection connection = this.getConnection();
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM appel");
        stmt.executeUpdate("DELETE FROM ressource");
        stmt.executeUpdate("DELETE FROM rendu_tp");
        stmt.executeUpdate("DELETE FROM binome");
        stmt.executeUpdate("DELETE FROM utilisateur");
        stmt.executeUpdate("DELETE FROM seance");

        stmt.close();
        connection.close();
    }

    protected Connection getConnection() throws Exception{
        return DataSourceProvider.getInstance().getDataSource().getConnection();
    }
}
