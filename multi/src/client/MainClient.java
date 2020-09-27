package client;

import java.net.Socket;

import java.io.IOException;
import java.io.ObjectOutputStream;

import common.Message;
import common.NetworkProtocol;

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
		
		ObjectOutputStream stream = null;
		Socket socket = null;
		
		try
		{
			socket = new Socket(args[0], Integer.parseInt(args[1]));
			stream = new ObjectOutputStream(socket.getOutputStream());
		} catch(IOException exception)
		{
			System.err.println(exception);
			System.exit(1);
		}
		
		// Création des threads
		Thread listenerThread = new MainClientListenerThread(socket);
		Thread writerThread = new MainClientWriterThread(stream);
		
		listenerThread.start();
		writerThread.start();
		
		try
		{
			writerThread.join();
			listenerThread.interrupt();
			
			Message lastMessage = new Message("Un utilisateur a quitté le chat", NetworkProtocol.LEAVE);
			stream.writeObject(lastMessage);
			
			Thread.sleep(1000);
			socket.close();
		} catch(IOException | InterruptedException exception)
		{
			exception.printStackTrace();
			System.out.println("Close socket : " + exception);
		}
	}
}
