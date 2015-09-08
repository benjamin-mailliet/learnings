package learnings.batch;

import learnings.managers.MotDePasseManager;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by A157183 on 08/09/2015.
 */
public class InitAccounts {
    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException {

        Map<String, String> mapAccounts  = initAccounts();

        MotDePasseManager mdpManager = new MotDePasseManager();
        for (String dateNaissance : mapAccounts.keySet()) {
            String mdp = mdpManager.genererMotDePasse(dateNaissance);
            //"INSERT INTO `learnings`.`utilisateur`  (`email`, `motdepasse`) VALUES ("email","motdepasse");";
            System.out.println("INSERT INTO `utilisateur`  (`email`, `motdepasse`) VALUES ('"+mapAccounts.get(dateNaissance)+"','"+mdp+"');");
        }
    }

    private static Map<String,String> initAccounts() throws InvalidKeySpecException, NoSuchAlgorithmException {
        Map<String, String> mapAccounts = new HashMap<>();
        mapAccounts.put("13_04_1992", "romain.bondois@hei.fr");
        mapAccounts.put("22_01_1994", "eugenie.brasier@hei.fr");
        mapAccounts.put("10_01_1993", "thomas.brebion@hei.fr");
        mapAccounts.put("23_07_1993", "robin.carnois@hei.fr");
        mapAccounts.put("11_01_1995", "fabien.colpaert@hei.fr");
        mapAccounts.put("16_12_1994", "charles.cousyn@hei.fr");
        mapAccounts.put("16_11_1995", "charles.cunin@hei.fr");
        mapAccounts.put("21_03_1994", "antoine.de-chassey@hei.fr");
        mapAccounts.put("23_01_1991", "audrey.deligand@hei.fr");
        mapAccounts.put("08_07_1994", "cesar.deligny@hei.fr");
        mapAccounts.put("10_11_1994", "hugo.descamps@hei.fr");
        mapAccounts.put("19_06_1993", "simon.destriez@hei.fr");
        mapAccounts.put("05_02_1992", "charles.douville-de-franssu@hei.fr");
        mapAccounts.put("09_08_1993", "theo.duhem@hei.fr");
        mapAccounts.put("19_12_1993", "leo.dumortier@hei.fr");
        mapAccounts.put("17_02_1994", "martial.gardies@hei.fr");
        mapAccounts.put("13_02_1992", "pierre-antoine.gerard@hei.fr");
        mapAccounts.put("20_08_1993", "thibault.gillotin@hei.fr");
        mapAccounts.put("19_08_1993", "constance.gueguen@hei.fr");
        mapAccounts.put("04_02_1994", "antoine.guillain@hei.fr");
        mapAccounts.put("11_02_1993", "maxime.herbecq@hei.fr");
        mapAccounts.put("01_04_1994", "erwan.kieffer@hei.fr");
        mapAccounts.put("20_02_1993", "ilya.krestyaninov@hei.fr");
        mapAccounts.put("07_10_1994", "barthelemy.leconte@hei.fr");
        mapAccounts.put("07_02_1992", "benjamin.leignel@hei.fr");
        mapAccounts.put("10_02_1995", "nicolas.lequin@hei.fr");
        mapAccounts.put("23_07_1993", "pierre.mertens@hei.fr");
        mapAccounts.put("28_01_1993", "anaick.miguet@hei.fr");
        mapAccounts.put("27_03_1993", "jacques.montagne@hei.fr");
        mapAccounts.put("13_02_1993", "antoine.pelen@hei.fr");
        mapAccounts.put("15_02_1993", "david.picart@hei.fr");
        mapAccounts.put("26_06_1992", "thomas.roqueplo@hei.fr");
        mapAccounts.put("08_02_1994", "constance.salle@hei.fr");
        mapAccounts.put("16_04_1992", "thibault.szymanski@hei.fr");
        mapAccounts.put("29_06_1993", "guillaume.verjot@hei.fr");
        return mapAccounts;
    }



}
