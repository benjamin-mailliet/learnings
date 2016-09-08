package learnings.dao;

import learnings.model.Utilisateur;

import java.util.List;

public interface UtilisateurDao {

	public List<Utilisateur> listerUtilisateurs();

	public Utilisateur getUtilisateur(Long id);

	public Utilisateur getUtilisateur(String mail);

	public String getMotDePasseUtilisateurHashe(String identifiant);

	public void supprimerUtilisateur(Long id);

	public void modifierRoleAdmin(Long id, boolean admin);

	public void modifierMotDePasse(Long id, String motDePasse);

	public Utilisateur ajouterUtilisateur(String email, String motDePasse, boolean admin);

	public List<Utilisateur> listerAutresEleves(Long id);

	public List<Utilisateur> listerEleves();
}
