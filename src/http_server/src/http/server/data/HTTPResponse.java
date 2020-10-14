package http.server.data;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe représentant la réponse d'une requête HTTP (avec Version HTTP, Code de retour, Headers)
 * @author Dorian et Fanny
 * @version 1.0
 */
public class HTTPResponse {

	/**
	 * Version HTTP associée à la reponse
	 */
	private String httpVersion;
	
	/**
	 * Code de retour pour la reponse
	 */
	private HTTPCode returnCode;
		
	/**
	 * L'entête de la réponse (variant pour chaque protocole)
	 */
	private Map<String, String> headers;
	
	
	/**
	 * Constructeur de la classe HTTPResponse
	 * @param HTTPVersion La version de l'HTTP
	 */
	public HTTPResponse(String HTTPVersion)
	{
		this.httpVersion = HTTPVersion;
		headers = new HashMap<String, String>();
	}
	
	/**
	 * Permet d'ajouter un header à la réponse
	 * @param key Le nom du header
	 * @param value La valeur du header
	 */
	public void putHeader(String key, String value)
	{
		this.headers.put(key, value);
	}
	
	/**
	 * Met à jour le code de retour
	 * @param code Le code de retour
	 */
	public void setReturnCode(HTTPCode code)
	{
		this.returnCode = code;
	}
	
	/**
	 * Permet d'envoyer la réponse
	 * @param out Le flux de sortie
	 * @param data Les données associées
	 * @throws IOException Si il y a une erreur au niveau de l'écriture
	 */
	public void send(OutputStream out, byte[] data) throws IOException
	{
		StringBuilder builder = new StringBuilder();
		
		// headers
		builder.append(String.format("%s %s %s\n", this.httpVersion, this.returnCode.code, this.returnCode.reasonPhrase));
		builder.append("Server: Bot\n");
		
		for (Map.Entry<String, String> entry : this.headers.entrySet()) {
			builder.append(String.format("%s: %s\n", entry.getKey(), entry.getValue()));
		}
		
		// ligne vide pour indiquer la fin des headers
		builder.append("\r\n");
		
		System.out.println(builder.toString());
		
		// contenu
		out.write(builder.toString().getBytes());
		if (data != null)
		{
			out.write(data);
		}
		
		out.flush();
	}
}