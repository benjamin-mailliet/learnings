package learnings.dao;

import learnings.model.Utilisateur;

public interface UtilisateurDao {

	public Utilisateur getUtilisateur(String identifiant);

	public String getMotDePasseUtilisateurHashe(String identifiant);
}
