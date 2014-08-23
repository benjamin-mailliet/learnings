package learnings.managers;

import java.security.GeneralSecurityException;
import java.util.List;

import learnings.dao.UtilisateurDao;
import learnings.dao.impl.UtilisateurDaoImpl;
import learnings.model.Utilisateur;
import learnings.utils.MotDePasseUtils;

public class UtilisateurManager {
	public static UtilisateurManager instance;

	public UtilisateurDao utilisateurDao = new UtilisateurDaoImpl();

	public static UtilisateurManager getInstance() {
		if (instance == null) {
			instance = new UtilisateurManager();
		}
		return instance;
	}

	public List<Utilisateur> listerUtilisateurs() {
		return utilisateurDao.listerUtilisateurs();
	}

	public Utilisateur getUtilisateur(String email) {
		if (email == null || "".equals(email)) {
			throw new IllegalArgumentException("L'identifiant doit être renseigné.");
		}
		return utilisateurDao.getUtilisateur(email);
	}

	public boolean validerMotDePasse(String email, String motDePasseAVerifier) {
		if (email == null || "".equals(email)) {
			throw new IllegalArgumentException("L'identifiant doit être renseigné.");
		}
		if (motDePasseAVerifier == null || "".equals(motDePasseAVerifier)) {
			throw new IllegalArgumentException("Le mot de passe doit être renseigné.");
		}
		String motDePasseHashe = utilisateurDao.getMotDePasseUtilisateurHashe(email);
		if (motDePasseHashe == null) {
			throw new IllegalArgumentException("L'identifiant n'est pas connu.");
		}
		try {
			return MotDePasseUtils.validerMotDePasse(motDePasseAVerifier, motDePasseHashe);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void supprimerUtilisateur(Long id) {
		if (id == null || "".equals(id)) {
			throw new IllegalArgumentException("L'id de l'utilisateur ne peut pas être null.");
		}
		utilisateurDao.supprimerUtilisateur(id);
	}

	public void enleverDroitsAdmin(Long id) {
		if (id == null || "".equals(id)) {
			throw new IllegalArgumentException("L'id de l'utilisateur ne peut pas être null.");
		}
		utilisateurDao.modifierRoleAdmin(id, false);
	}

	public void donnerDroitsAdmin(Long id) {
		if (id == null || "".equals(id)) {
			throw new IllegalArgumentException("L'id de l'utilisateur ne peut pas être null.");
		}
		utilisateurDao.modifierRoleAdmin(id, true);
	}

	/**
	 * Réinitialise le mot de passe de l'utilisateur avec pour valeur son email
	 * 
	 * @param id
	 */
	public void reinitialiserMotDePasse(Long id) {
		if (id == null || "".equals(id)) {
			throw new IllegalArgumentException("L'id de l'utilisateur ne peut pas être null.");
		}
		Utilisateur utilisateur = utilisateurDao.getUtilisateur(id);
		if (utilisateur == null) {
			throw new IllegalArgumentException("L'utilisateur n'est pas connu.");
		}
		try {
			String nouveauMotDePasse = MotDePasseUtils.genererMotDePasse(utilisateur.getEmail());
			utilisateurDao.modifierMotDePasse(id, nouveauMotDePasse);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
	}

	public Utilisateur ajouterUtilisateur(String email, boolean admin) {
		if (email == null || "".equals(email)) {
			throw new IllegalArgumentException("L'identifiant doit être renseigné.");
		}
		Utilisateur utilisateurExistant = this.getUtilisateur(email);
		if (utilisateurExistant != null) {
			throw new IllegalArgumentException("L'identifiant est déjà utilisé.");
		}

		String motDePasse = null;
		try {
			motDePasse = MotDePasseUtils.genererMotDePasse(email);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException("Problème technique.");
		}
		return utilisateurDao.ajouterUtilisateur(email, motDePasse, admin);
	}

	public void modifierMotDePasse(Long id, String motDePasse, String confirmationMotDePasse) {
		if (motDePasse == null || "".equals(motDePasse) || confirmationMotDePasse == null || "".equals(confirmationMotDePasse)) {
			throw new IllegalArgumentException("Les mots de passe doivent être renseignés.");
		}
		if (!motDePasse.equals(confirmationMotDePasse)) {
			throw new IllegalArgumentException("La confirmation du mot de passe ne correspond pas.");
		}

		try {
			String motDePasseHashe = MotDePasseUtils.genererMotDePasse(motDePasse);
			utilisateurDao.modifierMotDePasse(id, motDePasseHashe);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}

	}
}
