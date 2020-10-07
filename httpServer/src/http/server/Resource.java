package http.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 
 * @author doria
 *
 */
public class Resource {
	
	public Resource() { }
	
	/**
	 * 
	 * @param resource Le nom de la ressource
	 * @return Les donn�es associ�es � la ressource
	 * @throws IOException Lorsqu'il y a eu un probl�me au niveau de la lecture
	 */
	public static String loadResource(String resource) throws IOException
	{
		String data = new String(Files.readAllBytes(Paths.get(resource)));
		return data;
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
