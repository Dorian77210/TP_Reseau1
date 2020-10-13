package http.server.data;

/**
 * Enumeration pour repr�senter le code retour d'une r�ponse HTTP
 * @author Dorian et Fanny
 * @version 1.0
 */
public enum HTTPCode {
	/**
	 * Code de retour pour une requ�te qui a bien �t� trait�e.
	 */
	SUCCESS(200, "OK"),
	/**
	 * Code de retour pour une requ�te dont la ressource n'a pas �t� trouv�e.
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
	 * Code retour pour une requ�te qui n'a pas pu �tre trait�e par le serveur.
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
