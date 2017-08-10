package learnings.dao;

import learnings.model.RenduProjet;

import java.math.BigDecimal;
import java.util.List;

public interface RenduProjetDao {
	RenduProjet ajouterRendu(RenduProjet renduProjet);

	List<RenduProjet> listerRendusUtilisateurParProjet(Long idProjet, Long idUtilisateur);

	List<RenduProjet> listerRendusParUtilisateur(Long idUtilisateur);

	RenduProjet getRenduProjet(Long idRendu);

	List<RenduProjet> listerRendusParProjet(Long idProjet);

	void enregistrerNote(Long idRendu, BigDecimal note, String commentaire);
}