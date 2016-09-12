package learnings.dao;

import java.util.List;

import learnings.model.Projet;

public interface ProjetDao {

	List<Projet> listerProjets();

	Projet getProjet(Long id);

	Projet ajouterProjet(Projet projet);

	void modifierProjet(Projet projet);

	Long getLastProjetId();
}
