package http.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Classe qui permet de manipuler les ressources du serveur
 * @author Dorian et Fanny
 * @version 1.0
 */
public class Resource {

	/**
	 * Constructeur par d�faut
	 */
	public Resource() {}
	
	/**
	 * Permet de lire une ressource
	 * @param resource Le nom de la ressource
	 * @return Les donn�es associ�es � la ressource
	 * @throws IOException Lorsqu'il y a eu un probl�me au niveau de la lecture
	 */
	public static byte[] loadResource(String resource) throws IOException
	{
		return Files.readAllBytes(Paths.get(resource));
	}
	
	/**
	 * Permet d'ajouter des donn�es dans une resource
	 * @param resource Le nom de la ressource
	 * @param data Les donn�es � ajouter
	 * @throws IOException Lorsqu'il y a un probl�me d'�criture
	 */
	public static void appendData(String resource, String data) throws IOException
	{
		File fout = new File(resource);
		FileOutputStream fos = new FileOutputStream(fout, true);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		bw.write(data);
		bw.close();
	}
	
	/**
	 * Permet de remplacer des donn�es d'une resource
	 * @param resource Le nom de la ressource
	 * @param data Les donn�es � ajouter
	 * @throws IOException Lorsqu'il y a un probl�me d'�criture
	 */
	public static void replaceData(String resource, String data) throws IOException
	{
		File fout = new File(resource);
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		bw.write(data);
		bw.close();
	}
	
	/**
	 * Supprime une ressource
	 * @param resource La ressource � supprim�e
	 * @throws IOException Lorsqu'il y a un probl�me
	 */
	public static void deleteFile(String resource) throws IOException
	{
		File file = new File(resource);
		Files.delete(file.toPath());
	}
}
