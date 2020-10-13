package server;

import java.util.Queue;

import common.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import java.net.Socket;

/**
 * Singleton permettant de stocker des données essentielles pour la communication entre clients
 * @author Fanny et Dorian
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
	 * Permet de récupérer le message le plus ancien et de le retirer de la file
	 * @return Le premier message de la file si elle n'est pas vide, sinon <code>null</code>
	 */
	public synchronized Message nextMessage()
	{
		return this.messages.poll();
	}
	
	/**
	 * Permet de récupérer la nouvelle socket la plus ancienne et de la retirer de la file
	 * @return La première socket de la file si elle n'est pas valide, sinon <code>null</code>
	 */
	public synchronized Socket nextSocket()
	{
		return this.newSockets.poll();
	}
	
	/**
	 * Ajoute un message dans la file des nouveaux messages
	 * @param message Le nouveau message à ajouter
	 */
	public synchronized void addMessage(Message message)
	{
		this.messages.add(message);
	}
	
	/**
	 * Ajoute une socket dans la file des nouvelles sockets de communication
	 * @param socket La socket à ajouter
	 */
	public synchronized void addSocket(Socket socket)
	{
		this.newSockets.add(socket);
	}
	
	/**
	 * Permet de récupérer la liste des sockets expirées
	 * @return La liste des sockets expirées
	 */
	public synchronized List<Socket> getExpiredSockets()
	{
		return this.expiredSockets;
	}
	
	/**
	 * Ajoute une socket dans la file des sockets expirées
	 * @param socket La socket à ajouter
	 */
	public synchronized void addExpiredSocket(Socket socket)
	{
		this.expiredSockets.add(socket);
	}
}
