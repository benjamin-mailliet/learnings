package learnings.dao;

import java.util.List;

import learnings.model.Travail;

public interface TravailDao {
	public Travail ajouterTravail(Travail travail);

	public void ajouterUtilisateur(Long idTravail, Long idUtilisateur);

	public List<Travail> listerTravauxUtilisateurParSeance(Long idSeance, Long idUtilisateur);
}
