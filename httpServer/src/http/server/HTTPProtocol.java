package http.server;

/**
 * Enumeration pour représenter le type de requête HTTP
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
	
	public static boolean hasResponseBody(HTTPProtocol protocol)
	{
		if(protocol.equals(POST) || protocol.equals(PATCH) || protocol.equals(DELETE) || protocol.equals(PUT) || protocol.equals(HEAD))
		{
			return true;
		}
		
		return false;
	}
}
