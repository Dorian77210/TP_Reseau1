package http.server;

import java.io.File;
import java.io.IOException;

import java.util.Map;
import java.util.HashMap;

public class HTTPRequest {
	
	/**
	 * Protocole de la requ�te
	 */
	private HTTPProtocol protocol;
	
	/**
	 * La ressource demand�e par le client
	 */
	private String resource;
	
	/**
	 * Les donn�es associ�es � la ressource
	 */
	private String data;
	
	/**
	 * Le code de retour de la requete
	 */
	private int returnCode;
	
	/**
	 * Param�tres de la requ�te
	 */
	private Map<String, String> parameters;
	
	public static enum HTTPProtocol
	{
		GET, POST, DELETE, PUT, HEAD, PATCH, OPTIONS, TRACE, CONNECT
	}
	
	/**
	 * Erreur par d�faut renvoy�e au client si la ressource demand�e n'existe pas
	 */
	private static final String DEFAULT_RESOURCE_ERROR = "La ressource demand�e n'existe pas";
	
	private static final String DEFAULT_RESOURCE = "index.html";
	
	/**
	 * Erreur renvoy�e si une ressource n'a pas �t� charg�e correctement
	 */
	private static final String RESOURCE_LOADING_ERROR = "La ressource demand�e n'a pas pu �tre charg�e correctement";
	
	
	/**
	 * Constructeur de la classe HTTPProtocol
	 * @param protocol Le protocole de la requete
	 * @param resource La ressource associ�e � la requete
	 * @param parameters Les parameters de la requete
	 */
	public HTTPRequest(HTTPProtocol protocol, String resource, Map<String, String> parameters)
	{
		this.protocol = protocol;
		this.resource = resource;
		this.parameters = parameters;
		if (this.protocol.equals(HTTPProtocol.GET))
		{
			this.loadGetFile();
		}
	}
	
	private void loadGetFile()
	{
		this.resource = "./src/http/server/resource/" + (this.resource.equals("") ? DEFAULT_RESOURCE : resource);
		
		// on check si la ressource demand�e existe
		File resourceFile = new File(this.resource);
		if (resourceFile.exists())
		{
			try
			{
				this.data = ResourceLoader.loadResource(this.resource);
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
