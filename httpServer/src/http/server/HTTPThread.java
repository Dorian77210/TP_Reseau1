package http.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import java.util.List;
import java.util.ArrayList;

/**
 * Classe représentant le traitement d'une requete HTTP
 * @author Dorian
 *
 */
public class HTTPThread extends Thread {
	
	/**
	 * La socket associée à la requête
	 */
	private Socket socket;
	
	/**
	 * Constructeur de la classe HTTPTread
	 * @param socket La socket associée au thread	
	 */
	public HTTPThread(Socket socket)
	{
		this.socket = socket;
	}
	
	@Override
	public void run()
	{
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			PrintWriter out = new PrintWriter(this.socket.getOutputStream());
	
			// read the data sent. We basically ignore it,
			// stop reading once a blank line is hit. This
			// blank line signals the end of the client HTTP
			// headers.
			String str = ".";
			
			String boundaryHeader = "boundary=";
			String boundaryValue = null;
			boolean inHeaders = true;
			int cptLine = 0;
			HTTPProtocol protocol = null;
			boolean loop = true;
			List<String> payload = new ArrayList<>();
	
			while (loop)
			{
				str = in.readLine();
				if (str != null)
				{
					payload.add(str);
					if(cptLine == 0) {
						String[] firstLine = str.split(" ");
						protocol = HTTPProtocol.valueOf(firstLine[0]);
					}
					
					if (!inHeaders)
					{
						if(str.endsWith(boundaryValue+"--"))
						{
							break;
						}
					}
					
					if (str.equals(""))
					{		
						inHeaders = false;
					}
					if (str.contains(boundaryHeader))
					{
						int index = str.indexOf(boundaryHeader);
						boundaryValue = str.substring(index + boundaryHeader.length() + 1);
					}
				}
				cptLine++;
				if(protocol != null && HTTPProtocol.hasBody(protocol))
				{
					loop = str != null;
				}
				else
				{
					loop = str != null && !str.equals("");
				}
			}
			
			
			//Traitement de la requête
			if (!payload.isEmpty())
			{
				System.out.println(payload);
				
				// construction de la requete
				HTTPRequest request;
				Map<String, String> params = new HashMap<>();
				
				String firstLine = payload.get(0);
				int index;
				
				// e.g POST / HTTP/1.1
				// Recuperation de la ressource
				String[] elements = firstLine.split(" ");
				
				String resource = elements[1].substring(1);
				String httpVersion = elements[2];
				
				//Prise en compte des paramètres
				if(resource.contains("?"))
				{
					index = resource.indexOf("?");
					
					String paramList = resource.substring(index + 1);
					
					resource = resource.substring(0,index);
					
					for (String param : paramList.split("&")) {
						String[] parameter = param.split("=");
						params.put(parameter[0], parameter[1]);
					}
				}
	
				//Paramètres du body
				String paramLineBeginWith = "name=\"";
				for (String element : payload) {
					if(element.startsWith(paramLineBeginWith)) {
						
						String key;
						String value;
						
						String param = "";
						param = element.substring(paramLineBeginWith.length());
						
						int indexEndKey = param.indexOf("\"");
						key = param.substring(0, indexEndKey);
						
						int indexEndValue = param.indexOf("---" + boundaryValue);
						
						value = param.substring(indexEndKey+1, indexEndValue); 
						
						params.put(key, value);
					}
				}
				
				for (Entry<String, String> param : params.entrySet()) {
					System.out.println(param.getKey() + " : " + param.getValue());
				}			
				
				request = new HTTPRequest(protocol, resource, httpVersion, params);
				
				HTTPResponse response = new HTTPResponse(request);
				// Appel de la bonne méthode
				if (protocol.equals(HTTPProtocol.GET))
				{
					this.handleGET(request, response, out);
				} else if (protocol.equals(HTTPProtocol.POST))
				{
					this.handlePOST(request, response, out);
				}
				else if (protocol.equals(HTTPProtocol.DELETE))
				{
					this.handleDELETE(request, response, out);
				}
				else if (protocol.equals(HTTPProtocol.PUT))
				{
					this.handlePUT(request, response, out);
				}
				else if (protocol.equals(HTTPProtocol.PATCH))
				{
					this.handlePATCH(request, response, out);
				}
				else if (protocol.equals(HTTPProtocol.HEAD))
				{
					this.handleHEAD(request, response, out);
				}
				else if (protocol.equals(HTTPProtocol.CONNECT))
				{
					this.handleCONNECT(request, response, out);
				}
				else if (protocol.equals(HTTPProtocol.OPTIONS))
				{
					this.handleOPTIONS(request, response, out);
				}
				else if (protocol.equals(HTTPProtocol.TRACE))
				{
					this.handleTRACE(request, response, out);
				}
			}	
		} catch(IOException exception)
		{
			System.err.println(exception);
		} finally
		{
			try
			{
				this.socket.close();				
			} catch(IOException closeException)
			{
				System.err.println(closeException);
			}
		}
	}
	
	/**
	 * Permet de recevoir une requete get
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handleGET(HTTPRequest request, HTTPResponse response, PrintWriter out)
	{
		System.out.println("Receive GET Request");
		out.println(response);
		out.flush();
	}
	
	/**
	 * Permet de recevoir une requete post
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handlePOST(HTTPRequest request, HTTPResponse response, PrintWriter out)
	{
		System.out.println("Receive POST Request");
		out.println(response);
		out.flush();
	}
	
	/**
	 * Permet de recevoir une requete delete
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handleDELETE(HTTPRequest request, HTTPResponse response, PrintWriter out)
	{
		System.out.println("Receive DELETE Request");
		out.println(response);
		out.flush();
	}
	
	/**
	 * Permet de recevoir une requete head
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handleHEAD(HTTPRequest request, HTTPResponse response, PrintWriter out)
	{
		System.out.println("Receive HEAD Request");
		out.println(response);
		out.flush();
	}
	
	/**
	 * Permet de recevoir une requete put
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handlePUT(HTTPRequest request, HTTPResponse response, PrintWriter out)
	{
		System.out.println("Receive PUT Request");
		out.println(response);
		out.flush();
	}
	
	/**
	 * Permet de recevoir une requete patch
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handlePATCH(HTTPRequest request, HTTPResponse response, PrintWriter out)
	{
		System.out.println("Receive PATCH Request");
		out.println(response);
		out.flush();
	}
	
	/**
	 * Permet de recevoir une requete patch
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handleCONNECT(HTTPRequest request, HTTPResponse response, PrintWriter out)
	{
		System.out.println("Receive CONNECT Request");
		out.println(response);
		out.flush();
	}

	
	/**
	 * Permet de recevoir une requete patch
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handleOPTIONS(HTTPRequest request, HTTPResponse response, PrintWriter out)
	{
		System.out.println("Receive OPTIONS Request");
		out.println(response);
		out.flush();
	}

	/**
	 * Permet de recevoir une requete patch
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handleTRACE(HTTPRequest request, HTTPResponse response, PrintWriter out)
	{
		System.out.println("Receive TRACE Request");
		out.println(response);
		out.flush();
	}
}
