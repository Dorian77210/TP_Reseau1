package server;

import java.util.List;

import common.Message;

import java.util.ArrayList;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.FileWriter;

import java.net.Socket;

import java.util.Map;
import java.util.HashMap;

/**
 * Classe représentant le thread permettant de gérer les envoies de messages aux clients
 * @author dorian
 *
 */
public class ClientManagerThread extends Thread {
	
	/**
	 * Liste de toutes les sockets actives
	 */
	private Map<Socket, ObjectOutputStream> sockets;
	
	/**
	 * Historique des messages envoyés dans le chat
	 */
	private List<Message> history;
	
	/**
	 * Flux de sortie pour l'historique
	 */
	private FileWriter historyStream;
	
	/**
	 * Nom du fichier de sauvegarde de l'historique
	 */
	private static final String HISTORY_FILE = "history.json";
	
	/**
	 * Constructeur par défaut
	 */
	public ClientManagerThread()
	{
		super();
		this.sockets = new HashMap<>();
		this.history = new ArrayList<>();
	}
	
	@Override
	public void run()
	{
		// initialise le flux de sortie pour l'historique
		try
		{
			this.historyStream = new FileWriter(HISTORY_FILE, true);
		} catch(IOException exception)
		{
			System.err.println("Erreur de l'ouverture du fichier de l'historique");
			return;
		}
		
		this.readHistory();
		
		while (true)
		{
			// close the expired sockets
			List<Socket> expiredSockets = GlobalBuffer.getInstance().getExpiredSockets();
			for (Socket expiredSocket : expiredSockets)
			{
				try
				{
					this.sockets.remove(expiredSocket);
					expiredSocket.close();	
				} catch(IOException exception)
				{
					System.err.println("Remove socket : " + exception);
				}
			}
			expiredSockets.clear();
			
			// send the new messages
			Message message;
			while((message = GlobalBuffer.getInstance().nextMessage()) != null)
			{
				System.out.println(message);
				this.history.add(message);
				this.send(message);
			}
			
			// accept the new sockets
			Socket socket;
			while((socket = GlobalBuffer.getInstance().nextSocket()) != null)
			{
				try
				{
					this.sockets.put(socket, new ObjectOutputStream(socket.getOutputStream()));
				} catch(IOException exception)
				{
					System.err.println(exception);
				}
				this.sendHistory(socket);
				System.out.println("New client");
			}	
		}
	}
	
	/**
	 * Envoie un message à tous les clients
	 * @param message Le message à envoyer
	 */
	private void send(Message message)
	{
		for (Map.Entry<Socket, ObjectOutputStream> entry : this.sockets.entrySet())
		{
			try
			{
				System.out.println("Send the message : " + message);
				entry.getValue().writeObject(message);
				// sauvegarde le message dans le fichier
				//this.historyStream.write(message.toJSON().toString(2));
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
		if (!this.history.isEmpty())
		{
			ObjectOutputStream out = this.sockets.get(socket);
			for (Message message : this.history)
			{
				try
				{
					out.writeObject(message);
				} catch(IOException exception)
				{
					System.err.println("Send history : " + exception);
				}
			}
		}		
	}
	
	/**
	 * Permet de lire l'historique dans un fichier
	 */
	private void readHistory()
	{
		
	}
}
