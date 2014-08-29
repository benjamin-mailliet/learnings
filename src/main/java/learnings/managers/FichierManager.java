package learnings.managers;

import java.io.InputStream;

import learnings.exceptions.LearningsException;

public interface FichierManager {

	public void ajouterFichier(String path, InputStream fichier) throws LearningsException;

	public InputStream getFichier(String path) throws LearningsException;

	public void supprimerFichier(String path);
}
