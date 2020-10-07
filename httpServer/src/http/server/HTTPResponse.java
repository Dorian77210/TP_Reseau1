package http.server;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Classe repr�sentant la r�ponse d'une requ�te HTTP
 * @author doria
 *
 */
public class HTTPResponse {

	/**
	 * Code de retour pour la reponse
	 */
	HTTPCode returnCode;
	
	/**
	 * Version HTTP associ�e � la reponse
	 */
	private String httpVersion;
	
	/**
	 * Le type de la reponse
	 */
	private String contentType;
	
	/**
	 * Taille du body 
	 */
	private int contentLength;
	
	/**
	 * Protocole utilis�e par la r�ponse 
	 */
	private HTTPProtocol protocol;
	
	/**
	 * Erreur par d�faut renvoy�e au client si la ressource demand�e n'existe pas
	 */
	private static final String DEFAULT_RESOURCE_ERROR = "La ressource demand�e n'existe pas";
	
	/**
	 * Erreur renvoy�e si une ressource n'a pas �t� charg�e correctement
	 */
	private static final String RESOURCE_LOADING_ERROR = "La ressource demand�e n'a pas pu �tre charg�e correctement";
	
	/**
	 * Constructeur de la classe HTTPResponse
	 * @param request La requ�te qui initie la r�ponse
	 */
	public HTTPResponse(HTTPRequest request)
	{
		this.httpVersion = request.getHTTPVersion();
		this.contentType = "";
		this.protocol = request.getProtocol();
		this.contentLength = 0;
	}
	
	/**
	 * Met � jour le code de retour
	 * @param code Le nouveau code
	 */
	public void setReturnCode(HTTPCode code)
	{
		this.returnCode = code;
	}
	
	/**
	 * Mets � jour le content-type de la r�ponse
	 * @param contentType Le nouveau content type
	 */
	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}
	
	/**
	 * Mets � jour le contentLength
	 * @param length La nouvelle taille
	 */
	public void setContentLength(int length)
	{
		this.contentLength = length;
	}
	
	/**
	 * Permet d'envoyer la r�ponse
	 * @param out Le flux de sortie
	 * @param data Les donn�es associ�es
	 * @throws IOException Si il y a une erreur au niveau de l'�criture
	 */
	public void send(OutputStream out, byte[] data) throws IOException
	{
		StringBuilder builder = new StringBuilder();
		
		// headers
		builder.append(String.format("%s %s OK\n", this.httpVersion, this.returnCode.code));
		builder.append(String.format("Content-Type: %s \n", this.contentType));
		builder.append("Server: Bot\n");
		
		if (HTTPProtocol.hasResponseBody(this.protocol))
		{
			builder.append(String.format("Content-Length: %s\n", this.contentLength));
		}
		
		// ligne vide pour indiquer la fin des headers
		builder.append("\r\n");
		
		// contenu
		out.write(builder.toString().getBytes());
		if (data != null)
		{
			out.write(data);
		}
		
		out.flush();
	}
}