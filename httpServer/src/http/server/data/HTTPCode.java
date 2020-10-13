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
	 * Erreur lorsque le serveur rencontre une erreur
	 */
	INTERNAL_SERVER_ERROR(500, "Internal server error"),
	/**
	 * Code lorsque la ressource a �t� cr��e
	 */
	CREATED(201, "Created"),
	/**
	 * Code de retour lorsque le serveur rencontre une erreur d'acc�s � une ressource
	 */
	FORBIDDEN(403, "Forbidden"),
	/**
	 * Code de retour lorsque la ressource demand�e est trop lourde
	 */
	REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),
	/**
	 * Erreur envoy�e lorsque l'URI est trop longue
	 */
	URL_TOO_LONG(414, "URI Too Long"),
	/**
	 * Code de retour envoy�e quand le protocole n'est pas support� par le serveur
	 */
	NOT_IMPLEMENTED(501, "Not Implemented"),
	
	/**
	 * Code de retour envoy�e quand la syntaxe de la requ�te est erron�e
	 */
 	BAD_REQUEST(400, "Bad Request"),
	/**
	 * Code de retour renvoy�e lorsque la version HTTP n'est pas support�e
	 */
 	HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version not supported");
	
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
