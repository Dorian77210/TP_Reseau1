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
	 * Paramètres de la requête
	 */
	private Map<String, String> parameters;
	
	/**
	 * La version HTTP associée à le requête
	 */
	private String httpVersion;
	
	/**
	 * Constructeur de la classe HTTPProtocol
	 * @param protocol Le protocole de la requete
	 * @param resource La ressource associée à la requete
	 * @param parameters Les parameters de la requete
	 */
	public HTTPRequest(HTTPProtocol protocol, String resource, String httpVersion, Map<String, String> parameters)
	{
		this.protocol = protocol;
		this.resource = resource;
		this.parameters = parameters;
		this.httpVersion = httpVersion;
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
	 * Permet de récupérer les paramètres de la requête
	 * @return Les paramètres
	 */
	public Map<String, String> getParameters()
	{
		return this.parameters;
	}
}
