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
        stmt.executeUpdate("DELETE FROM travailutilisateur");
        stmt.executeUpdate("DELETE FROM travail");
        stmt.executeUpdate("DELETE FROM utilisateur");
        stmt.executeUpdate("DELETE FROM seance");
        stmt.executeUpdate("DELETE FROM projettransversal");

        stmt.close();
        connection.close();
    }

    protected Connection getConnection() throws Exception{
        return DataSourceProvider.getInstance().getDataSource().getConnection();
    }
}
