package http.server;

/**
 * Enumeration pour représenter le type de requête HTTP
 * @author Dorian et Fanny
 * @version 1.0
 */
public enum HTTPProtocol
{
	GET, POST, DELETE, PUT, HEAD, PATCH, OPTIONS, TRACE, CONNECT;
	
	/**
	 * Permet de savoir si une requête passée avec le protocole en paramètre admet un body
	 * @param protocol Le protocole
	 * @return Vrai si le protocole admet un body, faux sinon
	 */
	public static boolean hasBody(HTTPProtocol protocol){
		if(protocol.equals(POST) || protocol.equals(PATCH) || protocol.equals(DELETE) || protocol.equals(PUT)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Permet de savoir si la réponse d'une requête passée avec le protocole en paramètre admet un body
	 * @param protocol Le protocole
	 * @return Vrai si la réponse pour le protocole admet un body, faux sinon
	 */
	public static boolean hasResponseBody(HTTPProtocol protocol)
	{
		if(protocol.equals(POST) || protocol.equals(PATCH) || protocol.equals(DELETE) || protocol.equals(PUT) || protocol.equals(HEAD))
		{
			return true;
		}
		
		return false;
	}
}
