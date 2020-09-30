package client;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;

import common.Message;
import common.NetworkProtocol;

/**
 * Point d'entrée du client
 * @author dorian
 *
 */

public class MainClient {
	
	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args)
	{
		if (args.length != 2) {
	          System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
	          System.exit(1);
	    }
		
		ObjectOutputStream stream = null;
		Socket socket = null;
		boolean error = false;
		
		try
		{
			socket = new Socket(args[0], Integer.parseInt(args[1]));
			stream = new ObjectOutputStream(socket.getOutputStream());
		} catch(IOException exception)
		{
			System.err.println(exception);
			System.exit(1);
		}
		
		// Création du thread d'écoute
		Thread listenerThread = new MainClientListenerThread(socket);
		listenerThread.start();
		
		try
		{
			// Message writer
			
			int choice;
			boolean loop = true;
			BufferedReader scan = null;
			scan = new BufferedReader(new InputStreamReader(System.in));
			
			while (loop)
			{
				System.out.println("1 - Ecrire un message");
				System.out.println("2 - Quitter");
				
				try {
					choice = Integer.parseInt(scan.readLine());
					String content;
					Message message = null;
					
					if (!listenerThread.isAlive())
					{
						System.out.println("La connexion avec le serveur a été perdue");
						error = true;
						break;
					}
					
					switch(choice)
					{
						case 1:
							System.out.print("> ");
							content = scan.readLine();
							message = new Message(content, NetworkProtocol.EXCHANGE_MESSAGE);
						break;
						
						case 2:
							loop = false;
						break;
						
						default:
							System.out.println("Mauvais choix");
						break;
					}
					
					if (message != null)
					{
						// envoie du message
						stream.writeObject(message);	
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try
			{
				scan.close();
			} catch(IOException exception)
			{
				System.err.println(exception);
			}
			
			if (!error)
			{
				//End of write loop
				listenerThread.interrupt();	
				Message lastMessage = new Message("Un utilisateur a quitté le chat", NetworkProtocol.LEAVE);
				stream.writeObject(lastMessage);
				Thread.sleep(1000);
			}
			socket.close();
		} catch(IOException | InterruptedException exception)
		{
			exception.printStackTrace();
			System.out.println("Close socket : " + exception);
		}
	}
}
