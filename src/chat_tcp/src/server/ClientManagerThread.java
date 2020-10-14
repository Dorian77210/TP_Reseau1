package server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import common.Message;
import common.NetworkProtocol;

/**
 * Classe représentant le thread permettant de gérer les envois de messages aux clients
 * @author Dorian et Fanny
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
		
		this.readHistory();
		
		while (true)
		{
			// close the expired sockets
			List<Socket> expiredSockets = GlobalBuffer.getInstance().getExpiredSockets();
			for (Socket expiredSocket : expiredSockets)
			{
				try
				{
					if (!expiredSocket.isClosed())
					{
						Message lastClientMessage = new Message("Bye bye", NetworkProtocol.LEAVE);
						this.sockets.get(expiredSocket).writeObject(lastClientMessage);
						expiredSocket.close();
					}
					
					this.sockets.remove(expiredSocket);
					System.out.println("Client disconnected");
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
				if(message.getProtocol() != NetworkProtocol.LEAVE) {
					this.history.add(message);
				}
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
				System.out.println("New client connected");
			}	
		}
	}
	
	
	
	/**
	 * Envoie un message à tous les clients et socke les messages dans le fichier JSON
	 * @param message Le message à envoyer
	 */
	private void send(Message message)
	{
		
		try
		{
			// sauvegarde le message dans le fichier
			File file = new File(HISTORY_FILE);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(file, history);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		for (Map.Entry<Socket, ObjectOutputStream> entry : this.sockets.entrySet())
		{
			try
			{
				entry.getValue().writeObject(message);

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
	 * Permet de lire l'historique dans un fichier JSON
	 */
	private void readHistory()
	{
		try {
			File file = new File(HISTORY_FILE);
			if(file.exists()) {
				ObjectMapper objectMapper = new ObjectMapper();
				history.addAll(Arrays.asList(objectMapper.readValue(file, Message[].class)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
