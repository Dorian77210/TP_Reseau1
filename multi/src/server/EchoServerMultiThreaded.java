package server;

import java.net.ServerSocket;
import java.net.Socket;

public class EchoServerMultiThreaded  {
	
	/**
	 * Main method
	 * @param args
	 */
       public static void main(String args[]){ 
        ServerSocket listenSocket;
        
  	if (args.length != 1) {
          System.out.println("Usage: java EchoServer <EchoServer port>");
          System.exit(1);
  	}
	try {
		Thread manager = new ClientManagerThread();
		listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
		System.out.println("Server ready ..."); 
		manager.start();
		while (true) {
			Socket clientSocket = listenSocket.accept();
			System.out.println("Connexion from : " + clientSocket.getInetAddress());
			ClientThread ct = new ClientThread(clientSocket);
			ct.start();
		}
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
      }
  }

  