package http.server.data;

/**
 * Enumeration pour représenter le code retour d'une réponse HTTP
 * @author Dorian et Fanny
 * @version 1.0
 */
public enum HTTPCode {
	/**
	 * Code de retour pour une requête qui a bien été traitée.
	 */
	SUCCESS(200, "OK"),
	/**
	 * Code de retour pour une requête dont la ressource n'a pas été trouvée.
	 */
	RESOURCE_NOT_FOUND(404, "Not found"),
	/**
	 * 
	 */
	INTERNAL_SERVER_ERROR(500, "Internal server error"),
	/**
	 * 
	 */
	CREATED(201, "Created"),
	/**
	 * 
	 */
	FORBIDDEN(403, "Forbidden"),
	/**
	 * 
	 */
	URL_TOO_LONG(414, "URI Too Long"),
	/**
	 * 
	 */
	NOT_IMPLEMENTED(501, "Not Implemented");
	
	/**
	 * Code retour pour une requête qui n'a pas pu être traitée par le serveur.
	 */
	public final int code;
	
	/**
	 * Description du code retour
	 */
	public final String reasonPhrase;
	
	/**
	 * Constructeur d'un HTTPCode
	 * @param code
	 * @param reasonPhrase
	 */
	private HTTPCode(int code, String reasonPhrase)
	{
		this.code = code;
		this.reasonPhrase = reasonPhrase;
	}
}
