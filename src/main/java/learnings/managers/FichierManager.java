package learnings.managers;

import learnings.exceptions.LearningsException;

import java.io.InputStream;

public interface FichierManager {

	void ajouterFichier(String path, InputStream fichier) throws LearningsException;

	InputStream getFichier(String path) throws LearningsException;

	void supprimerFichier(String path) throws LearningsException;
}
