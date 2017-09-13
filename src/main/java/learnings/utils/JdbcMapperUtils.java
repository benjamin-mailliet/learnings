package learnings.utils;

import learnings.enums.Groupe;
import learnings.model.Utilisateur;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcMapperUtils {
    public static Utilisateur mapperVersUtilisateur(ResultSet results, String prefixTable) throws SQLException {
        long utilisateurId = results.getLong(prefixTable + ".id");
        if (results.wasNull()) {
            return null;
        }
        Groupe groupe = null;
        if (results.getString(prefixTable+".groupe") != null) {
            groupe = Groupe.valueOf(results.getString(prefixTable+".groupe"));
        }
        return new Utilisateur(utilisateurId, results.getString(prefixTable+".nom"), results.getString(prefixTable+".prenom"), results.getString(prefixTable+".email"),
                groupe, results.getBoolean(prefixTable+".admin"));
    }
}
