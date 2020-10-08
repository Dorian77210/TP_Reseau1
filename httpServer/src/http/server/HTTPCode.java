package http.server;

/**
 * Enumeration pour représenter le code retour d'une réponse
 * @author Dorian et Fanny
 * @version 1.0
 */
public enum HTTPCode {
	SUCCESS(200, "OK"), 
	RESOURCE_NOT_FOUND(404, "Not found"), 
	INTERNAL_SERVER_ERROR(500, "Internal server error");
	
	/**
	 * Code retour
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
