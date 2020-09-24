package multi;

import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import java.net.Socket;

/**
 * Singleton permettant de stocker des données essentielles pour la communication entre clients
 * @author doria
 *
 */
public class GlobalBuffer {
	
	/**
	 * Nouvelles sockets en attente de récupération par le ClientManagerThread
	 */
	private Queue<Socket> newSockets;
	
	/**
	 * Les messages en attente d'envoi
	 */
	private Queue<Message> messages;
	
	/**
	 * La liste des socket expirées
	 */
	private List<Socket> expiredSockets;
	
	/**
	 * Unique instance de la classe <code>GlobalBuffer</code>
	 */
	private static final GlobalBuffer instance = new GlobalBuffer();
	
	/**
	 * Constructeur par défaut
	 */
	public GlobalBuffer()
	{
		this.newSockets = new LinkedList<>();
		this.messages = new LinkedList<>();
		this.expiredSockets = new ArrayList<>();
	}
	
	/**
	 * Permet de récupérer la seule instance de la classe GlobalBuffer
	 * @return L'instance de la classe GlobalBuffer
	 */
	public static final synchronized GlobalBuffer getInstance()
	{
		return GlobalBuffer.instance;
	}
	
	/**
	 * Permets de vider la file de messages petit à petit
	 * @return Le premier message de la file si elle n'est pas vide, sinon <code>null</code>
	 */
	public synchronized Message nextMessage()
	{
		return this.messages.poll();
	}
	
	/**
	 * Permets de vider la file de nouvelles sockets en attente
	 * @return La première socket de la file si elle n'est pas valide, sinon <code>null</code>
	 */
	public synchronized Socket nextSocket()
	{
		return this.newSockets.poll();
	}
	
	/**
	 * Ajoute un nouveau message dans la file de messages
	 * @param message Le nouveau message à ajouter
	 */
	public synchronized void addMessage(Message message)
	{
		this.messages.add(message);
	}
	
	public synchronized void addSocket(Socket socket)
	{
		this.newSockets.add(socket);
	}
	
	/**
	 * Permets de récupérer la liste des sockets expirées
	 * @return La liste des sockets expirées
	 */
	public synchronized List<Socket> getExpiredSockets()
	{
		return this.expiredSockets;
	}
	
	/**
	 * Permet d'ajouter une socket expirée
	 * @param socket La socket à ajouter
	 */
	public synchronized void addExpiredSocket(Socket socket)
	{
		this.expiredSockets.add(socket);
	}
}
