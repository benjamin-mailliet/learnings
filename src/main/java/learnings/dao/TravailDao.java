package learnings.dao;

import learnings.model.Travail;
import learnings.model.Utilisateur;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface TravailDao {
	public Travail ajouterTravail(Travail travail);

	public void ajouterUtilisateur(Long idTravail, Long idUtilisateur);

	public Travail getTravailUtilisateurParSeance(Long idSeance, Long idUtilisateur);
	
	public Travail getTravailUtilisateurParProjet(Long idProjet, Long idUtilisateur);

	public List<Travail> listerTravauxParSeance(Long idSeance);

	public List<Travail> listerTravauxParUtilisateur(Long idUtilisateur);

	public List<Utilisateur> listerUtilisateurs(Long idTravail);

	public Travail getTravail(Long idTravail);

	public void mettreAJourTravail(Long idTravail, Date dateRendu, String chemin, String urlRepository, String commentaire);

	public List<Travail> listerTravauxParProjet(Long idProjet);

	public void enregistrerNoteTravail(Long idTravail, BigDecimal note, String commentaire);
}