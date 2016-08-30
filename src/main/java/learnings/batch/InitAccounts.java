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
        for (String email : mapAccounts.keySet()) {
            String mdp = mdpManager.genererMotDePasse(mapAccounts.get(email));
            //"INSERT INTO `learnings`.`utilisateur`  (`email`, `motdepasse`) VALUES ("email","motdepasse");";
            System.out.println("INSERT INTO `utilisateur`  (`email`, `motdepasse`) VALUES ('"+email+"','"+mdp+"');");
        }
    }

    private static Map<String,String> initAccounts() throws InvalidKeySpecException, NoSuchAlgorithmException {
        Map<String, String> mapAccounts = new HashMap<>();
        mapAccounts.put("romain.bondois@hei.fr","13_04_1992");
        mapAccounts.put("eugenie.brasier@hei.fr","22_01_1994");
        mapAccounts.put("thomas.brebion@hei.fr","10_01_1993");
        mapAccounts.put("robin.carnois@hei.fr","23_07_1993");
        mapAccounts.put("fabien.colpaert@hei.fr","11_01_1995");
        mapAccounts.put("charles.cousyn@hei.fr","16_12_1994");
        mapAccounts.put("charles.cunin@hei.fr","16_11_1995");
        mapAccounts.put("antoine.de-chassey@hei.fr","21_03_1994");
        mapAccounts.put("audrey.deligand@hei.fr","23_01_1991");
        mapAccounts.put("cesar.deligny@hei.fr","08_07_1994");
        mapAccounts.put("hugo.descamps@hei.fr","10_11_1994");
        mapAccounts.put("simon.destriez@hei.fr","19_06_1993");
        mapAccounts.put("charles.douville-de-franssu@hei.fr","05_02_1992");
        mapAccounts.put("theo.duhem@hei.fr","09_08_1993");
        mapAccounts.put("leo.dumortier@hei.fr","19_12_1993");
        mapAccounts.put("martial.gardies@hei.fr","17_02_1994");
        mapAccounts.put("pierre-antoine.gerard@hei.fr","13_02_1992");
        mapAccounts.put("thibault.gillotin@hei.fr","20_08_1993");
        mapAccounts.put("constance.gueguen@hei.fr","19_08_1993");
        mapAccounts.put("antoine.guillain@hei.fr","04_02_1994");
        mapAccounts.put("maxime.herbecq@hei.fr","11_02_1993");
        mapAccounts.put("erwan.kieffer@hei.fr","01_04_1994");
        mapAccounts.put("ilya.krestyaninov@hei.fr","20_02_1993");
        mapAccounts.put("barthelemy.leconte@hei.fr","07_10_1994");
        mapAccounts.put("benjamin.leignel@hei.fr","07_02_1992");
        mapAccounts.put("nicolas.lequin@hei.fr","10_02_1995");
        mapAccounts.put("pierre.mertens@hei.fr","23_07_1993");
        mapAccounts.put("anaick.miguet@hei.fr","28_01_1993");
        mapAccounts.put("jacques.montagne@hei.fr","27_03_1993");
        mapAccounts.put("antoine.pelen@hei.fr","13_02_1993");
        mapAccounts.put("david.picart@hei.fr","15_02_1993");
        mapAccounts.put("thomas.roqueplo@hei.fr","26_06_1992");
        mapAccounts.put("constance.salle@hei.fr","08_02_1994");
        mapAccounts.put("thibault.szymanski@hei.fr","16_04_1992");
        mapAccounts.put("guillaume.verjot@hei.fr","29_06_1993");
        return mapAccounts;
    }



}
