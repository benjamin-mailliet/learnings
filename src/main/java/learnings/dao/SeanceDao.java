package learnings.dao;

import java.util.Date;
import java.util.List;

import learnings.model.Seance;

public interface SeanceDao {
	List<Seance> listerSeances();

	List<Seance> listerSeancesNotees();

	List<Seance> listerTPNotesParDateRendu(Date date);

	Seance getSeance(Long idSeance);

	Seance ajouterSeance(Seance seance);

	void modifierSeance(Seance seance);
}
