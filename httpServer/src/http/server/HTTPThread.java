package http.server;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

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
			OutputStream out = this.socket.getOutputStream();
	
			// read the data sent. We basically ignore it,
			// stop reading once a blank line is hit. This
			// blank line signals the end of the client HTTP
			// headers.
			String str = ".";
			HTTPProtocol protocol = null;
			List<String> rawHeaders = new ArrayList<>();
			StringBuilder bodyBuilder = new StringBuilder();
			int contentLength = Integer.MIN_VALUE;
			boolean isDynamicResource = false;
	
			// recuperation du protocol
			String protocolLine = in.readLine();
			
			if (protocolLine == null)
			{
				return;
			}
			
			protocol = HTTPProtocol.valueOf(protocolLine.split(" ")[0]);
			
			// lecture des headers
			while (true)
			{
				str = in.readLine();
				if (str != null)
				{
					if (str.equals(""))
					{
						break;
					} else if (str.startsWith("Content-Length"))
					{
						// recuperation de la taille du body
						int index = str.indexOf(":");
						contentLength = Integer.parseInt(str.substring(index + 1).trim());
					}
					
					rawHeaders.add(str);
				}
			}
			
			// lecture du body
			if (contentLength != Integer.MIN_VALUE)
			{
				char[] buffer = new char[contentLength];
				try
				{
					int readBytes = in.read(buffer, 0, contentLength);
					String rawBody = new String(buffer);
					bodyBuilder.append(rawBody);
				} catch(IOException e)
				{
					System.err.println(e);
				}
			}
				
			//Traitement de la requête
			if (!rawHeaders.isEmpty())
			{	
				// construction de la requete
				HTTPRequest request;
				Map<String, String> headers = new HashMap<>();
				Map<String, String> params = new HashMap<>();
				int index;
				
				// e.g POST / HTTP/1.1
				// Recuperation de la ressource
				String[] elements = protocolLine.split(" ");
				
				String resource = elements[1].substring(1);
				isDynamicResource = resource.startsWith("dynamic/");
				String httpVersion = elements[2];
				
				//Prise en compte des paramètres de l'url
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
				
				System.out.println(resource);
				
				// gestion des headers
				for (String rawHeader : rawHeaders) {
					if(Pattern.matches("([\\w-]+):(.*?)(?=\\s*\\w+:|$)", rawHeader))
					{
						elements = rawHeader.split(": ", 2);
						String headerKey = elements[0];
						String headerValue = elements[1];
						headers.put(headerKey,  headerValue);
					}
				}	
				
				request = new HTTPRequest(protocol, resource, httpVersion, bodyBuilder.toString(), params, headers);
				HTTPResponse response = new HTTPResponse(request);
				
				
				if (isDynamicResource)
				{
				 	this.executeResource(request, response, out);
				} else
				{
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
	 * Permets d'executer une ressource
	 * @param request La requete
	 * @param response La reponse associée à la requete
	 * @param out Le flux de sortie
	 */
	private void executeResource(HTTPRequest request, HTTPResponse response, OutputStream out)
	{
		System.out.println("Execute dynamic resource");
		String resource = request.getResource();
		StringBuilder builder = new StringBuilder();
		
		// on check si la ressource demandée existe
		File resourceFile = new File(resource);
		if (resourceFile.exists())
		{
			// creation des arguments
			String header =  "";
			String params = "";
			String body = request.body;
			if (body.isEmpty())
			{
				body = "null";
			}
			
			String protocol = request.getProtocol().toString();
			
			// header
			for (Map.Entry<String, String> entry : request.headers.entrySet())
			{
				header += (entry.getKey() + ": " + entry.getValue());
				header += "/n";
			}
			
			// params
			for (Map.Entry<String, String> entry : request.urlParams.entrySet())
			{
				params += (entry.getKey() + "=" + entry.getValue());
			}
			
			if (params.isEmpty())
			{
				params = "null";
			}
			
			try 
			{
				String[] cmd = new String[] {
						"python",
						resource,
						header,
						params,
						protocol,
						body
				};
				
				System.out.println(resource);
				
				Process p = Runtime.getRuntime().exec(cmd);
				
				BufferedReader stdInput = new BufferedReader(new 
		                 InputStreamReader(p.getInputStream()));
				
				builder = new StringBuilder();
				String s;
				
				 while ((s = stdInput.readLine()) != null) {
					 builder.append(s + "\n");
		         }
				 
				 String returnBody = builder.toString();
				 System.out.println("Body : " + returnBody);
				 response.setContentType("text/plain");
				 response.setContentLength(returnBody.length());
				 response.setReturnCode(HTTPCode.SUCCESS);
				 
			} catch(IOException exception)
			{
				System.err.println(exception);
			}
		} else
		{
			response.setReturnCode(HTTPCode.RESOURCE_NOT_FOUND);
		}
		
		try
		{
			response.send(out, builder.toString().getBytes());	
		} catch(IOException e)
		{
			System.err.println(e);
		}	
	}
	
	/**
	 * Permet de recevoir une requete get
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handleGET(HTTPRequest request, HTTPResponse response, OutputStream out)
	{
		System.out.println("Receive GET Request");
		String resource = request.getResource();
		
		// on check si la ressource demandée existe
		File resourceFile = new File(resource);
		byte[] data = null;
		
		if (resourceFile.exists())
		{
			try
			{
				data = Resource.loadResource(resource);
				response.setReturnCode(HTTPCode.SUCCESS);
				String contentType = Files.probeContentType(resourceFile.toPath());
				response.setContentType(contentType);
			} catch(IOException exception)
			{
				response.setReturnCode(HTTPCode.INTERNAL_SERVER_ERROR);
			}
		} else
		{
			response.setReturnCode(HTTPCode.RESOURCE_NOT_FOUND);
		}
		
		try
		{
			response.send(out, data);
		} catch(IOException e)
		{
			System.err.println(e);
		}
	}
	
	/**
	 * Permet de recevoir une requete post
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handlePOST(HTTPRequest request, HTTPResponse response, OutputStream out)
	{
		System.out.println("Receive POST Request");
		String resource = request.getResource();
		byte[] data = null;
		
		try
		{
			Resource.appendData(resource, request.getBody());
			data = "{\"success\": \"true\"}".getBytes();
			response.setReturnCode(HTTPCode.SUCCESS);
			response.setContentType("application/json");
		} catch(IOException exception)
		{
			response.setReturnCode(HTTPCode.INTERNAL_SERVER_ERROR);
			data = "{\"success\": \"false\"}".getBytes();
		}
		
		response.setContentLength(data.length);
		
		try
		{
			response.send(out, data);
		} catch(IOException e)
		{
			System.err.println(e);
		}
	}
	
	/**
	 * Permet de recevoir une requete delete
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handleDELETE(HTTPRequest request, HTTPResponse response, OutputStream out)
	{
		System.out.println("Receive DELETE Request");
		String resource = request.getResource();
		byte[] data = null;
		
		try
		{
			Resource.deleteFile(resource);
			data = "{\"success\": \"true\"}".getBytes();
			response.setReturnCode(HTTPCode.SUCCESS);
			response.setContentType("application/json");
			response.setContentLength(data.length);
		} catch(IOException exception)
		{
			response.setReturnCode(HTTPCode.RESOURCE_NOT_FOUND);
		}
				
		try
		{
			response.send(out, data);
		} catch(IOException e)
		{
			System.err.println(e);
		}
	}
	
	/**
	 * Permet de recevoir une requete head
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handleHEAD(HTTPRequest request, HTTPResponse response, OutputStream out)
	{
		System.out.println("Receive HEAD Request");
		String resource = request.getResource();
		byte[] data = null;
		
		// on check si la ressource demandée existe
		File resourceFile = new File(resource);
		if (resourceFile.exists())
		{
			try
			{
				data = Resource.loadResource(resource);
				response.setReturnCode(HTTPCode.SUCCESS);
				String contentType = Files.probeContentType(resourceFile.toPath());
				response.setContentType(contentType);
			} catch(IOException exception)
			{
				response.setReturnCode(HTTPCode.INTERNAL_SERVER_ERROR);
			}
		} else
		{
			response.setReturnCode(HTTPCode.RESOURCE_NOT_FOUND);
		}
		
		response.setContentLength(data.length);
		
		try
		{
			response.send(out, data);
		} catch(IOException e)
		{
			System.err.println(e);
		}
	}
	
	/**
	 * Permet de recevoir une requete put
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handlePUT(HTTPRequest request, HTTPResponse response, OutputStream out)
	{
		System.out.println("Receive PUT Request");
		String resource = request.getResource();
		byte[] data = null;
		
		try
		{
			Resource.replaceData(resource, request.getBody());
			data = "{\"success\": \"true\"}".getBytes();
			response.setReturnCode(HTTPCode.SUCCESS);
			response.setContentType("application/json");
		} catch(IOException exception)
		{
			response.setReturnCode(HTTPCode.INTERNAL_SERVER_ERROR);
			data = "{\"success\": \"false\"}".getBytes();
		}
		
		response.setContentLength(data.length);
		
		try
		{
			response.send(out, data);
		} catch(IOException e)
		{
			System.err.println(e);
		}
	}
	
	/**
	 * Permet de recevoir une requete patch
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handlePATCH(HTTPRequest request, HTTPResponse response, OutputStream out)
	{
		System.out.println("Receive PATCH Request");
	}
	
	/**
	 * Permet de recevoir une requete patch
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handleCONNECT(HTTPRequest request, HTTPResponse response, OutputStream out)
	{
		System.out.println("Receive CONNECT Request");
	}

	
	/**
	 * Permet de recevoir une requete patch
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handleOPTIONS(HTTPRequest request, HTTPResponse response, OutputStream out)
	{
		System.out.println("Receive OPTIONS Request");
	}

	/**
	 * Permet de recevoir une requete patch
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handleTRACE(HTTPRequest request, HTTPResponse response, OutputStream out)
	{
		System.out.println("Receive TRACE Request");
	}
}
