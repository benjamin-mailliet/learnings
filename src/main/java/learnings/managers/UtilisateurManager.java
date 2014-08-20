package learnings.managers;

import java.security.GeneralSecurityException;
import java.util.List;

import learnings.dao.DataSourceProvider;
import learnings.dao.UtilisateurDao;
import learnings.dao.impl.UtilisateurDaoImpl;
import learnings.model.Utilisateur;
import learnings.utils.MotDePasseUtils;

public class UtilisateurManager {
	public static UtilisateurManager instance;

	public UtilisateurDao utilisateurDao = new UtilisateurDaoImpl(DataSourceProvider.getDataSource());

	public static UtilisateurManager getInstance() {
		if (instance == null) {
			instance = new UtilisateurManager();
		}
		return instance;
	}

	public List<Utilisateur> listerUtilisateurs() {
		return utilisateurDao.listerUtilisateurs();
	}

	public Utilisateur getUtilisateur(String identifiant) {
		if (identifiant == null || "".equals(identifiant)) {
			throw new IllegalArgumentException("L'identifiant doit être renseigné.");
		}
		return utilisateurDao.getUtilisateur(identifiant);
	}

	public boolean validerMotDePasse(String identifiant, String motDePasseAVerifier) {
		if (identifiant == null || "".equals(identifiant)) {
			throw new IllegalArgumentException("L'identifiant doit être renseigné.");
		}
		if (motDePasseAVerifier == null || "".equals(motDePasseAVerifier)) {
			throw new IllegalArgumentException("Le mot de passe doit être renseigné.");
		}
		String motDePasseHashe = utilisateurDao.getMotDePasseUtilisateurHashe(identifiant);
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
}
