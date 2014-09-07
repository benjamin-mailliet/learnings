package learnings.utils;

import java.util.UUID;

public class FichierUtils {

	public static String rendreUniqueNomFichier(String nomFichier) {
		StringBuilder nomBuilder = new StringBuilder();
		nomBuilder.append(UUID.randomUUID().toString().substring(0, 9));
		nomBuilder.append(nomFichier);
		return nomBuilder.toString();
	}

	public static String extraireNomFichier(String chemin) {
		return chemin.substring(chemin.lastIndexOf("/") + 1).substring(9);
	}
}
