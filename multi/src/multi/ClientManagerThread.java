package multi;

import java.util.List;
import java.util.ArrayList;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;

import java.net.Socket;

/**
 * Classe représentant le thread permettant de gérer les envoies de messages aux clients
 * @author dorian
 *
 */
public class ClientManagerThread extends Thread {
	
	/**
	 * Liste de toutes les sockets actives
	 */
	private List<Socket> sockets;
	
	/**
	 * Historique des messages envoyés dans le chat
	 */
	private List<Message> history;
	
	/**
	 * Constructeur par défaut
	 */
	public ClientManagerThread()
	{
		super();
	}
	
	@Override
	public void run()
	{
		// close the expired sockets
		List<Socket> expiredSockets = GlobalBuffer.getInstance().getExpiredSockets();
		for (Socket expiredSocket : expiredSockets)
		{
			try
			{
				expiredSocket.close();	
			} catch(IOException exception)
			{
				System.err.println(exception);
			}
		}
		
		// send the new messages
		Message message;
		while((message = GlobalBuffer.getInstance().nextMessage()) != null)
		{
			this.history.add(message);
			this.send(message);
		}
		
		// accept the new sockets
		Socket socket;
		while((socket = GlobalBuffer.getInstance().nextSocket()) != null)
		{
			this.sockets.add(socket);
			this.sendHistory(socket);
		}
	}
	
	/**
	 * Envoie un message à tous les clients
	 * @param message Le message à envoyer
	 */
	private void send(Message message)
	{
		ObjectOutputStream stream;
		for (Socket socket : this.sockets)
		{
			try
			{
				stream = new ObjectOutputStream(socket.getOutputStream());
				stream.writeObject(message);
			} catch(IOException exception)
			{
				System.err.println(exception);
			}
		}
	}
	
	/**
	 * Envoie l'historique des messages à un client
	 * @param socket La socket client
	 */
	private void sendHistory(Socket socket)
	{
		StringBuilder builder = new StringBuilder();
		for (Message message : this.history)
		{
			builder.append(message);
		}
		
		try 
		{
			PrintStream stream = new PrintStream(socket.getOutputStream());
			stream.println(builder.toString());
		} catch(IOException exception)
		{
			System.err.println(exception);
		}
		
	}

}
