package http.server;

/**
 * Enumeration pour repr�senter le type de requ�te HTTP
 * @author doria
 *
 */
public enum HTTPProtocol
{
	GET, POST, DELETE, PUT, HEAD, PATCH, OPTIONS, TRACE, CONNECT;
	
	public static boolean hasBody(HTTPProtocol protocol){
		if(protocol.equals(POST) || protocol.equals(PATCH) || protocol.equals(DELETE) || protocol.equals(PUT)) {
			return true;
		}
		return false;
	}
}
