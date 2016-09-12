package learnings.utils;

import learnings.enums.Groupe;
import learnings.exceptions.LearningsException;
import learnings.model.Utilisateur;

import java.util.ArrayList;
import java.util.List;

public class CsvUtils {

    private static final String CSV_SEPARATOR = ";";

    public static List<Utilisateur> parserCsvVersUtilisateurs(List<String> utilisateursCsv) throws LearningsException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        int numeroLigne = 0;
        for (String ligneCsv : utilisateursCsv) {
            numeroLigne++;
            String[] infosCsv = ligneCsv.split(CSV_SEPARATOR);
            if (infosCsv.length != 5) {
                throw new LearningsException(String.format("Le nombre de champs est incorrect à la ligne %d", numeroLigne));
            }
            if (numeroLigne == 1 && "nom".equalsIgnoreCase(infosCsv[0])) {
                continue;
            }

            utilisateurs.add(new Utilisateur(null, estNonVide(numeroLigne, infosCsv[0]), estNonVide(numeroLigne, infosCsv[1]), estNonVide(numeroLigne, infosCsv[2]), parserGroupe(numeroLigne, infosCsv[3]), parserAdmin(numeroLigne, infosCsv[4])));
        }
        return utilisateurs;
    }

    private static String estNonVide(int numeroLigne, String string) throws LearningsException{
        if ("".equals(string)) {
            throw new LearningsException(String.format("Un champ obligatoire est vide à la ligne %d", numeroLigne));
        }
        return string;
    }

    public static boolean parserAdmin(int numeroLigne, String adminCsv) throws LearningsException {
        boolean admin = false;
        if ("1".equals(adminCsv) || "true".equalsIgnoreCase(adminCsv) || "oui".equalsIgnoreCase(adminCsv)) {
            admin = true;
        } else if ("0".equals(adminCsv) || "false".equalsIgnoreCase(adminCsv) || "non".equalsIgnoreCase(adminCsv)) {
            admin = false;
        } else {
            throw new LearningsException(String.format("Problème avec le flag admin à la ligne %d : %s", numeroLigne, adminCsv));
        }
        return admin;
    }

    public static Groupe parserGroupe(int numeroLigne, String groupeCsv) throws LearningsException {
        try {
            String groupeAsString = groupeCsv;
            if("".equals(groupeAsString)) {
                return null;
            }
            if (groupeAsString.matches("\\d")) {
                groupeAsString = "GROUPE_" + groupeAsString;
            }
            return Groupe.valueOf(groupeAsString);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new LearningsException(String.format("Problème avec le groupe à la ligne %d : %s", numeroLigne, groupeCsv), e);
        }
    }
}
