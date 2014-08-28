package learnings.dao;

import java.util.List;

import learnings.model.Travail;
import learnings.model.Utilisateur;

public interface TravailDao {
	public Travail ajouterTravail(Travail travail);

	public void ajouterUtilisateur(Long idTravail, Long idUtilisateur);

	public List<Travail> listerTravauxUtilisateurParSeance(Long idSeance, Long idUtilisateur);

	public List<Travail> listerTravauxParSeance(Long idSeance);

	public List<Utilisateur> listerUtilisateurs(Long idTravail);
}