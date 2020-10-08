package http.server;

/**
 * Enumeration pour repr�senter le type de requ�te HTTP
 * @author Dorian et Fanny
 * @version 1.0
 */
public enum HTTPProtocol
{
	GET, POST, DELETE, PUT, HEAD, PATCH, OPTIONS, TRACE, CONNECT;
	
	/**
	 * Permet de savoir si une requ�te pass�e avec le protocole en param�tre admet un body
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
	 * Permet de savoir si la r�ponse d'une requ�te pass�e avec le protocole en param�tre admet un body
	 * @param protocol Le protocole
	 * @return Vrai si la r�ponse pour le protocole admet un body, faux sinon
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
