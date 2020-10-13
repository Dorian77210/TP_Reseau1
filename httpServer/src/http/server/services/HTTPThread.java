package http.server.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import http.server.data.HTTPCode;
import http.server.data.HTTPProtocol;
import http.server.data.HTTPRequest;
import http.server.data.HTTPResponse;

/**
 * Classe représentant le traitement d'une requête HTTP
 * @author Dorian et Fanny
 * @version 1.0
 */
public class HTTPThread extends Thread {
	
	/**
	 * La socket associée à la requête
	 */
	private Socket socket;
	
	/**
	 * Taille limite d'une URL
	 */
	private static final int URL_LIMIT = 60;
	
	/**
	 * Taille limite d'un fichier
	 */
	private static final int MAX_FILE_SIZE = 10_000_000;
	
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
	
			// Récuperation du protocole
			String protocolLine = in.readLine();
			
			if (protocolLine == null)
			{
				return;
			}
			
			protocol = HTTPProtocol.valueOf(protocolLine.split(" ")[0]);
			
			// Lecture des headers
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
						// Récuperation de la taille du body
						int index = str.indexOf(":");
						contentLength = Integer.parseInt(str.substring(index + 1).trim());
					}
					
					rawHeaders.add(str);
				}
			}
			
			// Lecture du body
			if (contentLength != Integer.MIN_VALUE)
			{
				char[] buffer = new char[contentLength];
				try
				{
					in.read(buffer, 0, contentLength);
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
				// construction de la requête
				HTTPRequest request;
				Map<String, String> headers = new HashMap<>();
				Map<String, String> params = new HashMap<>();
				int index;
				
				// e.g POST / HTTP/1.1
				// Récuperation de la ressource
				String[] elements = protocolLine.split(" ");
				
				String resource = elements[1].substring(1);
				isDynamicResource = resource.startsWith("dynamic/");
				String httpVersion = elements[2].trim();
				
				// Prise en compte des paramètres de l'url
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
				
				// Gestion des headers
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
				HTTPResponse response = new HTTPResponse(httpVersion);
				
				if(resource.length() > URL_LIMIT) {
					this.handleError(response, HTTPCode.URL_TOO_LONG, out);
				} else if (!httpVersion.equals("HTTP/1.1") && !httpVersion.equals("HTTP/1.0"))
				{
					this.handleError(response, HTTPCode.HTTP_VERSION_NOT_SUPPORTED, out);
				}
				else
				{
					if (isDynamicResource)
					{
					 	this.executeResource(request, response, out);
					} else
					{					
						// Appel de la bonne méthode selon le protocole
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
	 * Permet d'executer une ressource
	 * @param request La requête
	 * @param response La reponse associée à la requête
	 * @param out Le flux de sortie
	 */
	private void executeResource(HTTPRequest request, HTTPResponse response, OutputStream out)
	{
		System.out.println("Execute dynamic resource");
		String resource = request.getResource();
		StringBuilder builder = new StringBuilder();
		
		// On vérifie que la ressource existe
		File resourceFile = new File(resource);
		
		System.out.println("Ressource = " + resource);
		
		if (!resource.endsWith(".py"))
		{
			response.setReturnCode(HTTPCode.BAD_REQUEST);
		} else
		{
			if (resourceFile.exists())
			{
				// Creation des arguments
				String header =  "";
				String params = "";
				String body = request.body;
				if (body.isEmpty())
				{
					body = "null";
				}
				
				String protocol = request.getProtocol().toString();
				
				// Création du Header
				for (Map.Entry<String, String> entry : request.headers.entrySet())
				{
					header += (entry.getKey() + ": " + entry.getValue());
					header += "/n";
				}
				
				// Création des paramètres
				int i = 0;
				for (Map.Entry<String, String> entry : request.urlParams.entrySet())
				{
					params += (entry.getKey() + "=" + entry.getValue());
					if (i != request.urlParams.size() - 1)
					{
						params += "\n";
					}
					i++;
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
					
					Process p = Runtime.getRuntime().exec(cmd);
					
					BufferedReader stdInput = new BufferedReader(new 
			                 InputStreamReader(p.getInputStream()));
					String s;
					
					 while ((s = stdInput.readLine()) != null) {
						 builder.append(s + "\n");
			         }
					 
					 String returnBody = builder.toString();
					 System.out.println(builder);
					 response.putHeader("Content-Type", "text/plain");
					 response.putHeader("Content-Length", Integer.toString(returnBody.length()));
					 response.setReturnCode(HTTPCode.SUCCESS);
				} catch(IOException exception)
				{
					System.err.println(exception);
				}
			} else
			{
				response.setReturnCode(HTTPCode.RESOURCE_NOT_FOUND);
			}
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
		
		// On vérifie que la ressource existe
		File resourceFile = new File(resource);
		byte[] data = null;
		
		if (resourceFile.exists())
		{
			if (resourceFile.length() > MAX_FILE_SIZE)
			{
				response.setReturnCode(HTTPCode.REQUEST_ENTITY_TOO_LARGE);
			} else
			{
				try
				{
					
					data = Resource.loadResource(resource);
					String contentType = Files.probeContentType(resourceFile.toPath());
					response.setReturnCode(HTTPCode.SUCCESS);
					response.putHeader("Content-Type", contentType);
				} catch(IOException exception)
				{
					HTTPCode code;
					code = exception instanceof AccessDeniedException
								? HTTPCode.FORBIDDEN
								: HTTPCode.INTERNAL_SERVER_ERROR;
					response.setReturnCode(code);
				}	
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
		
		File file = new File(request.getResource());
		if (!file.exists())
		{
			response.setReturnCode(HTTPCode.CREATED);
		} else
		{
			response.setReturnCode(HTTPCode.SUCCESS);
		}
		
		try
		{
			Resource.appendData(resource, request.getBody());
			data = "{\"success\": \"true\"}".getBytes();
			response.putHeader("Content-Type", "application/json");
		} catch(IOException exception)
		{
			response.setReturnCode(HTTPCode.INTERNAL_SERVER_ERROR);
			data = "{\"success\": \"false\"}".getBytes();
		}
		
		response.putHeader("Content-Length", Integer.toString(data.length));
		
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
			response.putHeader("Content-Type", "application/json");
			response.putHeader("Content-Length", Integer.toString(data.length));
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
		
		// On vérifie que la ressource existe
		File resourceFile = new File(resource);
		if (resourceFile.exists())
		{
			try
			{
				data = Resource.loadResource(resource);
				response.setReturnCode(HTTPCode.SUCCESS);
				String contentType = Files.probeContentType(resourceFile.toPath());
				response.putHeader("Content-Type", contentType);
			} catch(IOException exception)
			{
				response.setReturnCode(HTTPCode.INTERNAL_SERVER_ERROR);
			}
		} else
		{
			response.setReturnCode(HTTPCode.RESOURCE_NOT_FOUND);
		}
		
		int length = data !=null ? data.length:0;
		response.putHeader("Content-Length", Integer.toString(length));
		
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
		
		File file = new File(request.getResource());
		if (!file.exists())
		{
			response.setReturnCode(HTTPCode.CREATED);
		} else
		{
			response.setReturnCode(HTTPCode.SUCCESS);
		}
		
		try
		{
			Resource.replaceData(resource, request.getBody());
			data = "{\"success\": \"true\"}".getBytes();
			response.putHeader("Content-Type", "application/json");
		} catch(IOException exception)
		{
			response.setReturnCode(HTTPCode.INTERNAL_SERVER_ERROR);
			data = "{\"success\": \"false\"}".getBytes();
		}
		
		response.putHeader("Content-Length", Integer.toString(data.length));
		
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
		
		response.setReturnCode(HTTPCode.NOT_IMPLEMENTED);
		
		try
		{
			response.send(out, null);
		} catch(IOException e)
		{
			System.err.println(e);
		}
	}
	
	/**
	 * Permet de recevoir une requete connect
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handleCONNECT(HTTPRequest request, HTTPResponse response, OutputStream out)
	{
		System.out.println("Receive CONNECT Request");
		
		response.setReturnCode(HTTPCode.NOT_IMPLEMENTED);
		
		try
		{
			response.send(out, null);
		} catch(IOException e)
		{
			System.err.println(e);
		}
	}

	
	/**
	 * Permet de recevoir une requete options
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handleOPTIONS(HTTPRequest request, HTTPResponse response, OutputStream out)
	{
		System.out.println("Receive OPTIONS Request");

		StringBuilder protocols = new StringBuilder();
		for (int i = 0; i<HTTPProtocol.values().length; ++i)
		{
			
			if(i < HTTPProtocol.values().length-1) {
				protocols.append(HTTPProtocol.values()[i] + ", ");
			}
			else
			{
				protocols.append(HTTPProtocol.values()[i]);
			}
		}
		response.putHeader("Allow", protocols.toString());
		
		Date date = new Date();
		response.putHeader("Date", date.toString());
		response.setReturnCode(HTTPCode.SUCCESS);

		try
		{
			response.send(out, null);
		} catch(IOException e)
		{
			System.err.println(e);
		}

	}

	/**
	 * Permet de recevoir une requete trace
	 * @param request La requete associée
	 * @param response La reponse associée
	 * @param out Le flux de sortie
	 */
	private void handleTRACE(HTTPRequest request, HTTPResponse response, OutputStream out)
	{
		System.out.println("Receive TRACE Request");
		
		response.setReturnCode(HTTPCode.NOT_IMPLEMENTED);
		
		try
		{
			response.send(out, null);
		} catch(IOException e)
		{
			System.err.println(e);
		}
	}
	
	/**
	 * Permet de retourner une erreur
	 * @param response La réponse qui est envoyée au client
	 * @param code Le code associé à l'erreur
	 * @ 
	 */
	private void handleError(HTTPResponse response, HTTPCode code, OutputStream out)
	{
		System.out.println("Receive too long url request");
		
		response.setReturnCode(code);
		
		try
		{
			response.send(out, null);
		} catch(IOException e)
		{
			System.err.println(e);
		}
	}
}
