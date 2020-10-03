///A Simple Web Server (WebServer.java)

package http.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

/**
 * Example program from Chapter 1 Programming Spiders, Bots and Aggregators in
 * Java Copyright 2001 by Jeff Heaton
 * 
 * WebServer is a very simple web-server. Any request is responded with a very
 * simple web-page.
 * 
 * @author Jeff Heaton
 * @version 1.0
 */
public class WebServer {

	/**
	 * WebServer constructor.
	 */
	protected void start() {
		ServerSocket s;

		System.out.println("Webserver starting up on port 80");
		System.out.println("(press ctrl-c to exit)");
		try {
			// create the main server socket
			s = new ServerSocket(3000);
		} catch (Exception e) {
			System.out.println("Error: " + e);
			return;
		}

		System.out.println("Waiting for connection");
		for (;;) {
			try {
				// wait for a connection
				Socket remote = s.accept();
				// remote is now the connected socket
				System.out.println("Connection, sending data.");
				BufferedReader in = new BufferedReader(new InputStreamReader(remote.getInputStream()));
				PrintWriter out = new PrintWriter(remote.getOutputStream());

				// read the data sent. We basically ignore it,
				// stop reading once a blank line is hit. This
				// blank line signals the end of the client HTTP
				// headers.
				
				StringBuilder builder = new StringBuilder();
				String str = ".";
				
				int requestSize = Integer.MIN_VALUE;
				int currentSize = 0;
				String contentLenghtHeader = "Content-Length:";
				boolean inHeaders = true;
				
				while (str != null && currentSize != requestSize)
				{
					str = in.readLine();
					builder.append(str);
					System.out.println(str);
					if (str != null)
					{
						if (!inHeaders)
						{
							currentSize += (str.getBytes("UTF-8").length);
							System.out.println(currentSize);
							System.out.println("request size = " + requestSize);
						}
						
						if (str.equals(""))
						{		
							if (!inHeaders)
							{
								currentSize++;
							}
							inHeaders = false;
						}
						if (str.contains(contentLenghtHeader))
						{
							requestSize = Integer.parseInt(str.substring(contentLenghtHeader.length() + 1));
						}
					}
				}
				
				String rawPayload = builder.toString();
				if (rawPayload != null && !rawPayload.equals("") && !rawPayload.equals("null"))
				{
					String[] payload = rawPayload.split(" ");
					System.out.println(Arrays.toString(payload));
					
					
					// construction de la requete
					HTTPRequest request;
					
					HTTPRequest.HTTPProtocol protocol = HTTPRequest.HTTPProtocol.valueOf(payload[0]);
					String resource = payload[1].substring(1);
					
					Map<String, String> params = new HashMap<>();
					
					request = new HTTPRequest(protocol, resource, params);
					
					if (protocol.equals(HTTPRequest.HTTPProtocol.GET))
					{
						this.handleGET(request, out);
					} else if (protocol.equals(HTTPRequest.HTTPProtocol.POST))
					{
						this.handlePOST(request, out);
					}
				}

				remote.close();
			} catch (Exception e) {
				System.out.println("Error: " + e);
			}
		}
	}
	
	/**
	 * Permet de recevoir une requete get
	 * @param request La requete associée
	 * @param out Le flux de sortie
	 */
	public void handleGET(HTTPRequest request, PrintWriter out)
	{
		System.out.println("Receive GET Request");
		out.println(request);
		out.flush();
	}
	
	/**
	 * Permet de recevoir une requete post
	 * @param request La requete associée
	 * @param out Le flux de sortie
	 */
	public void handlePOST(HTTPRequest request, PrintWriter out)
	{
		System.out.println("Receive POST Request");
		out.println(request);
		out.flush();
	}
	
	/**
	 * Permet de recevoir une requete delete
	 * @param request La requete associée
	 * @param out Le flux de sortie
	 */
	public void handleDELETE(HTTPRequest request, PrintWriter out)
	{
		
	}
	
	/**
	 * Permet de recevoir une requete head
	 * @param request La requete associée
	 * @param out Le flux de sortie
	 */
	public void handleHEAD(HTTPRequest request, PrintWriter out)
	{
		
	}
	
	/**
	 * Permet de recevoir une requete put
	 * @param request La requete associée
	 * @param out Le flux de sortie
	 */
	public void handlePUT(HTTPRequest request, PrintWriter out)
	{
		
	}

	/**
	 * Start the application.
	 * 
	 * @param args Command line parameters are not used.
	 */
	public static void main(String args[]) {
		WebServer ws = new WebServer();
		ws.start();
	}
}
