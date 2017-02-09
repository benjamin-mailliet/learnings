package learnings.utils;

import learnings.exceptions.LearningsException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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

	public static List<String> getLignes(InputStream fichierInputStream) throws LearningsException {
		List<String> lignes = new ArrayList<>();
		String ligne;
		try (InputStreamReader reader = new InputStreamReader(fichierInputStream)) {
			try (BufferedReader bufferedReader = new BufferedReader(reader)) {
				while ((ligne = bufferedReader.readLine()) != null) {
					lignes.add(ligne);
				}
			}
		} catch (IOException e) {
			throw new LearningsException("Erreur lors de la lecture du fichier", e);
		}
		return lignes;
	}
}
