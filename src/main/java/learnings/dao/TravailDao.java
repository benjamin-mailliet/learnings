package learnings.dao;

import java.util.Date;
import java.util.List;

import learnings.model.Travail;
import learnings.model.Utilisateur;

public interface TravailDao {
	Travail ajouterTravail(Travail travail);

	void ajouterUtilisateur(Long idTravail, Long idUtilisateur);

	Travail getTravailUtilisateurParSeance(Long idSeance, Long idUtilisateur);
	
	Travail getTravailUtilisateurParProjet(Long idProjet, Long idUtilisateur);

	List<Travail> listerTravauxParSeance(Long idSeance);

	List<Travail> listerTravauxParUtilisateur(Long idUtilisateur);

	List<Utilisateur> listerUtilisateurs(Long idTravail);

	Travail getTravail(Long idTravail);

	void mettreAJourTravail(Long idTravail, Date dateRendu, String chemin, String urlRepository, String commentaire);

	List<Travail> listerTravauxParProjet(Long idProjet);
}