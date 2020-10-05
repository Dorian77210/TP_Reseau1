package http.server;

import java.io.File;
import java.io.IOException;

/**
 * Classe repr�sentant la r�ponse d'une requ�te HTTP
 * @author doria
 *
 */
public class HTTPResponse {

	/**
	 * Le code de retour de la requete
	 */
	private int returnCode;
	
	/**
	 * Description associ�e au code de retour
	 */
	private String reasonPhrase;
	
	/**
	 * Version HTTP associ�e � la reponse
	 */
	private String httpVersion;
	
	/**
	 * Les donn�es associ�es � la ressource
	 */
	private String data;
	
	/**
	 * Erreur par d�faut renvoy�e au client si la ressource demand�e n'existe pas
	 */
	private static final String DEFAULT_RESOURCE_ERROR = "La ressource demand�e n'existe pas";
	
	/**
	 * Page HTML par d�faut
	 */
	private static final String DEFAULT_RESOURCE = "index.html";
	
	/**
	 * Erreur renvoy�e si une ressource n'a pas �t� charg�e correctement
	 */
	private static final String RESOURCE_LOADING_ERROR = "La ressource demand�e n'a pas pu �tre charg�e correctement";

	public HTTPResponse(HTTPRequest request)
	{
		this.httpVersion = request.getHTTPVersion();
		
		
		if (request.getProtocol().equals(HTTPProtocol.GET))
		{
			this.loadGetFile(request.getResource());
		}
		else
		{
			this.data = "Requ�te bien re�ue.";
		}
	}
	
	/**
	 * Permet de charger une ressource
	 * @param resource La ressource associ�e
	 */
	private void loadGetFile(String resource)
	{
		resource = "./src/http/server/resource/" + (resource.equals("") ? DEFAULT_RESOURCE : resource);
		
		// on check si la ressource demand�e existe
		File resourceFile = new File(resource);
		if (resourceFile.exists())
		{
			try
			{
				this.data = ResourceLoader.loadResource(resource);
				this.returnCode = 200;
				
			} catch(IOException exception)
			{
				this.data = RESOURCE_LOADING_ERROR;
				this.returnCode = 406;
			}
		} else
		{
			this.returnCode = 404;
			this.data = DEFAULT_RESOURCE_ERROR;
		}
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		// headers
		builder.append(String.format("HTTP/1.0 %s OK\n", this.returnCode));
		// A revoir
		builder.append("Content-Type: text/html \n");
		builder.append("Server: Bot\n");
		
		// ligne vide pour indiquer la fin des headers
		builder.append("\n");
		
		// contenu
		builder.append(this.data);
		
		return builder.toString();
	}
}
