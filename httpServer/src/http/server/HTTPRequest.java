package http.server;

import java.io.File;
import java.io.IOException;

import java.util.Map;
import java.util.HashMap;

public class HTTPRequest {
	
	/**
	 * Protocole de la requête
	 */
	private HTTPProtocol protocol;
	
	/**
	 * La ressource demandée par le client
	 */
	private String resource;
	
	/**
	 * Body de la requête
	 */
	public final String body;
	
	/**
	 * Parametres de l'url
	 */
	public final Map<String, String> urlParams;
	
	/**
	 * La version HTTP associée à le requête
	 */
	private String httpVersion;
	
	/**
	 * La liste des headers
	 */
	public final Map<String, String> headers;
	
	/**
	 * Constructeur de la classe HTTPProtocol
	 * @param protocol Le protocole de la requete
	 * @param resource La ressource associée à la requete
	 * @param body Le body de la requête
	 * @param urlParams Les parameters de l'url
	 * @param headers Les headers de la requête
	 */
	public HTTPRequest(HTTPProtocol protocol, 
			String resource, 
			String httpVersion, 
			String body, 
			Map<String, String> urlParams, 
			Map<String, String> headers)
	{
		this.protocol = protocol;
		this.resource = resource;
		this.body = body;
		this.urlParams = urlParams;
		this.httpVersion = httpVersion;
		this.headers = headers;
		this.resource = "./src/http/server/resource/" + (resource.equals("") ? "index.html" : resource);
	}
	
	
	/**
	 * Permet de récupérer le nom de la ressource demandée (url)
	 * @return La ressource
	 */
	public String getResource()
	{
		return this.resource;
	}
	
	/**
	 * Permet de récupérer la version HTTP
	 * @return La version HTTP
	 */
	public String getHTTPVersion()
	{
		return this.httpVersion;
	}
	
	/**
	 * Permet de récupérer le protocole de la requete
	 * @return Le protocole
	 */
	public HTTPProtocol getProtocol()
	{
		return this.protocol;
	}
	
	/**
	 * Permets de récupérer le body de la requête
	 * @return Le body
	 */
	public String getBody()
	{
		return this.body;
	}
}
