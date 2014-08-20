package learnings.dao;

import java.util.List;

import learnings.model.Utilisateur;

public interface UtilisateurDao {

	public List<Utilisateur> listerUtilisateurs();

	public Utilisateur getUtilisateur(String identifiant);

	public String getMotDePasseUtilisateurHashe(String identifiant);
}
