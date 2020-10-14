///A Simple Web Server (WebServer.java)

package http.server.services;

import java.net.ServerSocket;
import java.net.Socket;

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
	 * Port du serveur
	 */
	private int port;
	
	/**
	 * WebServer constructor.
	 * @param port Le port d'écoute
	 */
	public WebServer(int port)
	{
		this.port = port;
	}
	
	
	protected void start() {
		ServerSocket s;

		System.out.println("Webserver starting up on port " + this.port);
		System.out.println("(press ctrl-c to exit)");
		try {
			// create the main server socket
			s = new ServerSocket(this.port);
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
				Thread thread = new HTTPThread(remote);
				thread.start();
			} catch (Exception e) {
				System.out.println("Error: " + e);
			}
		}
	}

	/**
	 * Start the application.
	 * 
	 * @param args Command line parameters are not used.
	 */
	public static void main(String args[]) {
		if (args.length != 1)
		{
			System.err.println("Usage : <port>");
			System.exit(1);
		}
		
		int port = Integer.parseInt(args[0]);
		
		WebServer ws = new WebServer(port);
		ws.start();
	}
}
