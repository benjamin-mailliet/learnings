package learnings.dao;

import java.util.List;

import learnings.model.Projet;

public interface ProjetDao {

	public List<Projet> listerProjets();

	public Projet getProjet(Long id);

	public Projet ajouterProjet(Projet projet);

	public void modifierProjet(Projet projet);

	public Long getLastProjetId();
}
