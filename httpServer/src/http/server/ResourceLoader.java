package http.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;

/**
 * 
 * @author doria
 *
 */
public class ResourceLoader {
	
	public ResourceLoader() { }
	
	/**
	 * 
	 * @param resource Le nom de la ressource
	 * @return Les données associées à la ressource
	 * @throws IOException Lorsqu'il y a eu un problème au niveau de la lecture
	 */
	public static String loadResource(String resource) throws IOException
	{
		System.out.println(new File(".").getAbsolutePath());
		String data = new String(Files.readAllBytes(Paths.get(resource)));
		return data;
	}
}
