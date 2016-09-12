package learnings.dao;

import learnings.model.Utilisateur;

import java.util.List;

public interface UtilisateurDao {

	List<Utilisateur> listerUtilisateurs();

	Utilisateur getUtilisateur(Long id);

	Utilisateur getUtilisateur(String mail);

	String getMotDePasseUtilisateurHashe(String identifiant);

	void supprimerUtilisateur(Long id);

	void modifierRoleAdmin(Long id, boolean admin);

	void modifierMotDePasse(Long id, String motDePasse);

	Utilisateur ajouterUtilisateur(Utilisateur nouvelUtilisateur, String motDePasse);

	List<Utilisateur> listerAutresEleves(Long id);

	public List<Utilisateur> listerEleves();
}
