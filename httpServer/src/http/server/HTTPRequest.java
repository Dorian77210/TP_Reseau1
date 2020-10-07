package http.server;

import java.io.File;
import java.io.IOException;

import java.util.Map;
import java.util.HashMap;

public class HTTPRequest {
	
	/**
	 * Protocole de la requ�te
	 */
	private HTTPProtocol protocol;
	
	/**
	 * La ressource demand�e par le client
	 */
	private String resource;
	
	/**
	 * Body de la requ�te
	 */
	public final String body;
	
	/**
	 * Parametres de l'url
	 */
	public final Map<String, String> urlParams;
	
	/**
	 * La version HTTP associ�e � le requ�te
	 */
	private String httpVersion;
	
	/**
	 * La liste des headers
	 */
	public final Map<String, String> headers;
	
	/**
	 * Constructeur de la classe HTTPProtocol
	 * @param protocol Le protocole de la requete
	 * @param resource La ressource associ�e � la requete
	 * @param body Le body de la requ�te
	 * @param urlParams Les parameters de l'url
	 * @param headers Les headers de la requ�te
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
	 * Permet de r�cup�rer le nom de la ressource demand�e (url)
	 * @return La ressource
	 */
	public String getResource()
	{
		return this.resource;
	}
	
	/**
	 * Permet de r�cup�rer la version HTTP
	 * @return La version HTTP
	 */
	public String getHTTPVersion()
	{
		return this.httpVersion;
	}
	
	/**
	 * Permet de r�cup�rer le protocole de la requete
	 * @return Le protocole
	 */
	public HTTPProtocol getProtocol()
	{
		return this.protocol;
	}
	
	/**
	 * Permets de r�cup�rer le body de la requ�te
	 * @return Le body
	 */
	public String getBody()
	{
		return this.body;
	}
}
