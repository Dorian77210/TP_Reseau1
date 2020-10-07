package http.server;

import java.io.File;
import java.io.IOException;

/**
 * Classe représentant la réponse d'une requête HTTP
 * @author doria
 *
 */
public class HTTPResponse {

	/**
	 * Code de retour pour la reponse
	 */
	HTTPCode returnCode;
	
	/**
	 * Version HTTP associée à la reponse
	 */
	private String httpVersion;
	
	/**
	 * Body associé à la réponse si elle en a un
	 */
	private String body;
	
	/**
	 * Le type de la reponse
	 */
	private String contentType;
	
	/**
	 * Taille du body 
	 */
	private int contentLength;
	
	/**
	 * Protocole utilisée par la réponse 
	 */
	private HTTPProtocol protocol;
	
	/**
	 * Erreur par défaut renvoyée au client si la ressource demandée n'existe pas
	 */
	private static final String DEFAULT_RESOURCE_ERROR = "La ressource demandée n'existe pas";
	
	/**
	 * Erreur renvoyée si une ressource n'a pas été chargée correctement
	 */
	private static final String RESOURCE_LOADING_ERROR = "La ressource demandée n'a pas pu être chargée correctement";
	
	/**
	 * Constructeur de la classe HTTPResponse
	 * @param request La requête qui initie la réponse
	 */
	public HTTPResponse(HTTPRequest request)
	{
		this.httpVersion = request.getHTTPVersion();
		this.body = "";
		this.contentType = "";
		this.protocol = request.getProtocol();
		this.contentLength = 0;
	}
	
	/**
	 * Met à jour le code de retour
	 * @param code Le nouveau code
	 */
	public void setReturnCode(HTTPCode code)
	{
		this.returnCode = code;
	}
	
	/**
	 * Mets à jour le body
	 * @param body Le nouveau body de la réponse
	 */
	public void setBody(String body)
	{
		this.body = body;
	}
	
	/**
	 * Mets à jour le content-type de la réponse
	 * @param contentType Le nouveau content type
	 */
	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}
	
	/**
	 * Mets à jour le contentLength
	 * @param length La nouvelle taille
	 */
	public void setContentLength(int length)
	{
		this.contentLength = length;
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		// headers
		builder.append(String.format("%s %s OK\n", this.httpVersion, this.returnCode.code));
		// A revoir
		builder.append(String.format("Content-Type: %s \n", this.contentType));
		builder.append("Server: Bot\n");
		
		if (HTTPProtocol.hasResponseBody(this.protocol))
		{
			builder.append(String.format("Content-Length: %s\n", this.contentLength));
		}
		
		// ligne vide pour indiquer la fin des headers
		builder.append("\r\n");
		
		// contenu
		builder.append(this.body);
		
		return builder.toString();
	}
}
