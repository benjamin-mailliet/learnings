package learnings.managers;

import java.security.GeneralSecurityException;

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

	public Utilisateur getUtilisateur(String identifiant) {
		return utilisateurDao.getUtilisateur(identifiant);
	}

	public boolean validerMotDePasse(String identifiant, String motDePasseAVerifier) {
		String motDePasseHashe = utilisateurDao.getMotDePasseUtilisateurHashe(identifiant);
		try {
			return MotDePasseUtils.validerMotDePasse(motDePasseAVerifier, motDePasseHashe);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		return false;
	}
}
