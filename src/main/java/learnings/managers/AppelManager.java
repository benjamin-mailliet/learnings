package learnings.managers;

import learnings.dao.AppelDao;
import learnings.dao.impl.AppelDaoImpl;
import learnings.model.Appel;

import java.util.List;

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
}
