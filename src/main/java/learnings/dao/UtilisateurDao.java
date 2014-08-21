package learnings.dao;

import java.util.List;

import learnings.model.Utilisateur;

public interface UtilisateurDao {

	public List<Utilisateur> listerUtilisateurs();

	public Utilisateur getUtilisateur(Long id);

	public Utilisateur getUtilisateur(String mail);

	public String getMotDePasseUtilisateurHashe(String identifiant);

	public void supprimerUtilisateur(Long id);

	public void modifierRoleAdmin(Long id, boolean admin);

	public void modifierMotDePasse(Long id, String motDePasse);
}
