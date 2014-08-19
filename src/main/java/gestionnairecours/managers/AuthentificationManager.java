package gestionnairecours.managers;

public class AuthentificationManager {
	public static AuthentificationManager instance;

	public static AuthentificationManager getInstance() {
		if (instance == null) {
			instance = new AuthentificationManager();
		}
		return instance;
	}

	public void authentifier(String identifiant, String motDePasse) {

	}
}
