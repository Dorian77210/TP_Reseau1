package server;

import java.util.Queue;

import common.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import java.net.Socket;

/**
 * Singleton permettant de stocker des donn�es essentielles pour la communication entre clients
 * @author Fanny et Dorian
 *
 */
public class GlobalBuffer {
	
	/**
	 * Nouvelles sockets en attente de r�cup�ration par le ClientManagerThread
	 */
	private Queue<Socket> newSockets;
	
	/**
	 * Les messages en attente d'envoi
	 */
	private Queue<Message> messages;
	
	/**
	 * La liste des socket expir�es
	 */
	private List<Socket> expiredSockets;
	
	/**
	 * Unique instance de la classe <code>GlobalBuffer</code>
	 */
	private static final GlobalBuffer instance = new GlobalBuffer();
	
	/**
	 * Constructeur par d�faut
	 */
	public GlobalBuffer()
	{
		this.newSockets = new LinkedList<>();
		this.messages = new LinkedList<>();
		this.expiredSockets = new ArrayList<>();
	}
	
	/**
	 * Permet de r�cup�rer la seule instance de la classe GlobalBuffer
	 * @return L'instance de la classe GlobalBuffer
	 */
	public static final synchronized GlobalBuffer getInstance()
	{
		return GlobalBuffer.instance;
	}
	
	/**
	 * Permet de r�cup�rer le message le plus ancien et de le retirer de la file
	 * @return Le premier message de la file si elle n'est pas vide, sinon <code>null</code>
	 */
	public synchronized Message nextMessage()
	{
		return this.messages.poll();
	}
	
	/**
	 * Permet de r�cup�rer la nouvelle socket la plus ancienne et de la retirer de la file
	 * @return La premi�re socket de la file si elle n'est pas valide, sinon <code>null</code>
	 */
	public synchronized Socket nextSocket()
	{
		return this.newSockets.poll();
	}
	
	/**
	 * Ajoute un message dans la file des nouveaux messages
	 * @param message Le nouveau message � ajouter
	 */
	public synchronized void addMessage(Message message)
	{
		this.messages.add(message);
	}
	
	/**
	 * Ajoute une socket dans la file des nouvelles sockets de communication
	 * @param socket La socket � ajouter
	 */
	public synchronized void addSocket(Socket socket)
	{
		this.newSockets.add(socket);
	}
	
	/**
	 * Permet de r�cup�rer la liste des sockets expir�es
	 * @return La liste des sockets expir�es
	 */
	public synchronized List<Socket> getExpiredSockets()
	{
		return this.expiredSockets;
	}
	
	/**
	 * Ajoute une socket dans la file des sockets expir�es
	 * @param socket La socket � ajouter
	 */
	public synchronized void addExpiredSocket(Socket socket)
	{
		this.expiredSockets.add(socket);
	}
}
