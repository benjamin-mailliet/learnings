package learnings.dao;

import learnings.model.Travail;
import learnings.model.Utilisateur;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface TravailDao {
	Travail ajouterTravail(Travail travail);

	void ajouterUtilisateur(Long idTravail, Long idUtilisateur);

	Travail getTravailUtilisateurParSeance(Long idSeance, Long idUtilisateur);
	
	Travail getTravailUtilisateurParProjet(Long idProjet, Long idUtilisateur);

	List<Travail> listerTravauxParSeance(Long idSeance);

	List<Travail> listerTravauxParUtilisateur(Long idUtilisateur);

	List<Utilisateur> listerUtilisateursParTravail(Long idTravail);

	Travail getTravail(Long idTravail);

	void mettreAJourTravail(Long idTravail, Date dateRendu, String chemin, String urlRepository, String commentaire);

	List<Travail> listerTravauxParProjet(Long idProjet);

	public void enregistrerNoteTravail(Long idTravail, BigDecimal note, String commentaire);
}