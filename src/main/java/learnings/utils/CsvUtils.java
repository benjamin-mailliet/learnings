package learnings.utils;

import learnings.enums.Groupe;
import learnings.exceptions.LearningsException;
import learnings.model.Enseignement;
import learnings.model.Seance;
import learnings.model.Travail;
import learnings.model.Utilisateur;
import learnings.pojos.EleveAvecTravauxEtProjet;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CsvUtils {

    private static final String CSV_SEPARATOR = ";";


    public static void ecrireLigne(Writer w, List<String> listeValeurs) throws IOException {
        boolean premiereLigne = true;
        StringBuilder sb = new StringBuilder();
        for (String value : listeValeurs) {
            if (!premiereLigne) {
                sb.append(CSV_SEPARATOR);
            }
            sb.append(value);
            premiereLigne = false;
        }
        sb.append("\n");
        w.append(sb.toString());
    }

    public static void creerCSVElevesNotes(Writer writer, List<EleveAvecTravauxEtProjet> eleves, List<Seance> seancesNotees) throws IOException {
        List<Long> listeIdsSeances = seancesNotees.stream().map(Enseignement::getId).collect(Collectors.toList());
        ecrireEnTeteCSVNotes(writer, seancesNotees);
        for (EleveAvecTravauxEtProjet eleve : eleves) {
            ecrireLigneEleve(writer, listeIdsSeances, eleve);
        }
    }

    private static void ecrireLigneEleve(Writer writer, List<Long> listeIdsSeances, EleveAvecTravauxEtProjet eleve) throws IOException {
        List<String> ligneEleve = new ArrayList<>();
        ligneEleve.add(eleve.getNom() + " " + eleve.getPrenom());
        final Map<Long, Travail> mapTravaux = eleve.getMapSeanceIdTravail();
        for (Long idSeance : listeIdsSeances) {
            final Travail travail = mapTravaux.get(idSeance);
            if (travail != null) {
                if (travail.getNote() != null) {
                    ligneEleve.add(travail.getNote().toString());
                }
            }
        }
        if (eleve.getProjet() != null){
            ajouterNoteIfNoNull(eleve.getProjet().getNote(),ligneEleve);
        }
        ligneEleve = ajouterNoteIfNoNull(eleve.getMoyenne(), ligneEleve);
        ecrireLigne(writer,ligneEleve);
    }

    private static List<String> ajouterNoteIfNoNull(BigDecimal note, List<String> ligneEleve) {
        if (note != null){
            ligneEleve.add(note.toString());
        }else{
            ligneEleve.add(" ");
        }
        return ligneEleve;
    }

    private static void ecrireEnTeteCSVNotes(Writer writer, List<Seance> seancesNotees) throws IOException {
        ArrayList<String> valeursPremiereLigne= new ArrayList<>();
        valeursPremiereLigne.add("Elève");
        valeursPremiereLigne.addAll(seancesNotees.stream().map(Seance::getTitre).collect(Collectors.toList()));
        valeursPremiereLigne.add("Projet");
        valeursPremiereLigne.add("Moyenne");
        ecrireLigne(writer, valeursPremiereLigne);
    }

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
