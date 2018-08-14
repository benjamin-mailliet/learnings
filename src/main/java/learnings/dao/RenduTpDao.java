package learnings.dao;

import learnings.model.RenduTp;

import java.math.BigDecimal;
import java.util.List;

public interface RenduTpDao {
	RenduTp ajouterRenduTp(RenduTp renduTp);

	List<RenduTp> listerRendusParBinome(String uidBinome);

	List<RenduTp> listerRendusParSeance(Long idSeance);

	List<RenduTp> listerRendusParUtilisateur(Long idUtilisateur);

	RenduTp getRendu(Long idRendu);

	void enregistrerNote(Long idRendu, BigDecimal note, String commentaire);
}