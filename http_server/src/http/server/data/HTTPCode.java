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
	 * Erreur lorsque le serveur rencontre une erreur
	 */
	INTERNAL_SERVER_ERROR(500, "Internal server error"),
	/**
	 * Code lorsque la ressource a été créée
	 */
	CREATED(201, "Created"),
	/**
	 * Code de retour lorsque le serveur rencontre une erreur d'accès à une ressource
	 */
	FORBIDDEN(403, "Forbidden"),
	/**
	 * Code de retour lorsque la ressource demandée est trop lourde
	 */
	REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),
	/**
	 * Erreur envoyée lorsque l'URI est trop longue
	 */
	URL_TOO_LONG(414, "URI Too Long"),
	/**
	 * Code de retour envoyée quand le protocole n'est pas supporté par le serveur
	 */
	NOT_IMPLEMENTED(501, "Not Implemented"),
	
	/**
	 * Code de retour envoyée quand la syntaxe de la requête est erronée
	 */
 	BAD_REQUEST(400, "Bad Request"),
	/**
	 * Code de retour renvoyée lorsque la version HTTP n'est pas supportée
	 */
 	HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version not supported");
	
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
