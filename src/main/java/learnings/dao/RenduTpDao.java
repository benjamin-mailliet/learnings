package learnings.dao;

import learnings.model.Binome;
import learnings.model.RenduTp;
import learnings.model.Travail;
import learnings.model.Utilisateur;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface RenduTpDao {
	RenduTp ajouterRenduTp(RenduTp renduTp);

	List<RenduTp> listerRendusParBinome(Long idBinome);

	List<RenduTp> listerRendusParSeance(Long idSeance);

	List<RenduTp> listerRendusParUtilisateur(Long idUtilisateur);

	RenduTp getRendu(Long idRendu);

	void enregistrerNote(Long idRendu, BigDecimal note, String commentaire);
}