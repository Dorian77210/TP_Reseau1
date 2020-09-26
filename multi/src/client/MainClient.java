package client;

import java.net.Socket;

import java.io.IOException;

/**
 * Point d'entrée du client
 * @author dorian
 *
 */

public class MainClient {
	
	public static void main(String[] args)
	{
		if (args.length != 2) {
	          System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
	          System.exit(1);
	    }
		
		Socket socket = null;
		
		try
		{
			socket = new Socket(args[0], Integer.parseInt(args[1]));
		} catch(IOException exception)
		{
			System.err.println(exception);
			System.exit(1);
		}
		
		// Création des threads
		Thread listenerThread = new MainClientListenerThread(socket);
		Thread writerThread = new MainClientWriterThread(socket);
		
		listenerThread.start();
		writerThread.start();
		
		try
		{
			writerThread.join();
			System.out.println("join success");
			listenerThread.interrupt();
			System.out.println("interrupt success");
			socket.close();
		} catch(IOException | InterruptedException exception)
		{
			exception.printStackTrace();
			System.out.println("Close socket : " + exception);
		}
	}
}
