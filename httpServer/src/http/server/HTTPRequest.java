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
	 * Param�tres de la requ�te
	 */
	private Map<String, String> parameters;
	
	/**
	 * La version HTTP associ�e � le requ�te
	 */
	private String httpVersion;
	
	/**
	 * Constructeur de la classe HTTPProtocol
	 * @param protocol Le protocole de la requete
	 * @param resource La ressource associ�e � la requete
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
	 * Permet de r�cup�rer les param�tres de la requ�te
	 * @return Les param�tres
	 */
	public Map<String, String> getParameters()
	{
		return this.parameters;
	}
}
