package learnings.managers;

import java.util.Date;
import java.util.List;

import learnings.dao.TPDao;
import learnings.dao.impl.TPDaoImpl;
import learnings.model.TP;

public class EnseignementManager {
	private static EnseignementManager instance;

	public static EnseignementManager getInstance() {
		if (instance == null) {
			instance = new EnseignementManager();
		}
		return instance;
	}

	private TPDao tpDao = new TPDaoImpl();

	public List<TP> listerTPRenduAccessible() {
		Date aujourdhui = new Date();
		return tpDao.listerTPNotesParDateRendu(aujourdhui);
	}
}
