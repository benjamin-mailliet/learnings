package learnings.managers;

import learnings.dao.AppelDao;
import learnings.dao.impl.AppelDaoImpl;
import learnings.enums.StatutAppel;
import learnings.model.Appel;

import java.util.List;
import java.util.Map;

public class AppelManager {

    private AppelManager() {
    }

    private static class AppelManagerHolder{
        private final static AppelManager instance = new AppelManager();
    }

    public static AppelManager getInstance() {
        return AppelManagerHolder.instance;
    }

    private AppelDao appelDao = new AppelDaoImpl();

    public List<Appel> listerAppels(Long idSeance) {
        if (idSeance == null) {
            throw new IllegalArgumentException("La séance est incorrecte.");
        }
        return appelDao.listerAppels(idSeance);
    }

    public void enregistrerAppels(Long idSeance, Map<Long, StatutAppel> nouveauxAppels) {
        List<Appel> appelsExistants = this.listerAppels(idSeance);
        for (Appel appel : appelsExistants) {
            if (appel.getStatut() != null) {
                if (!appel.getStatut().equals(nouveauxAppels.get(appel.getEleve().getId()))) {
                    appel.setStatut(nouveauxAppels.get(appel.getEleve().getId()));
                    appelDao.modifierAppel(idSeance, appel);
                }
            } else {
                if (!nouveauxAppels.get(appel.getEleve().getId()).equals(StatutAppel.NON_SAISI)) {
                    appel.setStatut(nouveauxAppels.get(appel.getEleve().getId()));
                    appelDao.ajouterAppel(idSeance, appel);
                }
            }
        }
    }
}
