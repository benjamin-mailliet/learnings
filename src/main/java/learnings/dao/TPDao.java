package learnings.dao;

import java.util.Date;
import java.util.List;

import learnings.model.TP;

public interface TPDao {

	public List<TP> listerTPNotesParDateRendu(Date date);
}
