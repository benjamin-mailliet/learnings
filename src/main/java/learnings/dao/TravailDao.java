package learnings.dao;

import learnings.model.Travail;

public interface TravailDao {
	public Travail ajouterTravail(Travail travail);

	public void ajouterUtilisateur(Long idTravail, Long idUtilisateur);
}
