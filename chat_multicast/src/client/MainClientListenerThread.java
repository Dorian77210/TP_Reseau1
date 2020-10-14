package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MainClientListenerThread extends Thread 
{
	/**
	 * La socket du client
	 */
	private MulticastSocket socket;
	
	/**
	 * L'IP du groupe
	 */
	private InetAddress IPGroup;
	
	/**
	 * Le port du groupe
	 */
	private int port;
	
	/**
	 * Longueur maximale d'un message
	 */
	private static final int MAX_MSG_LEN = 1000;
	
	/**
	 * Constructeur de la class MainClientListenerThread
	 * @param socket La socket du client
	 * @param IPGroup L'IP du groupe
	 * @param port Le port du groupe
	 */
	public MainClientListenerThread(MulticastSocket socket, InetAddress IPGroup, int port)
	{
		this.socket = socket;
		this.IPGroup = IPGroup;
		this.port = port;
	}
	
	@Override
	public void run()
	{
		while (!this.isInterrupted())
		{
			byte[] buffer = new byte[MAX_MSG_LEN]; 
            DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, IPGroup, port); 
			String message = null;
			
			try
			{
				socket.receive(datagram);
				message = new String(buffer);
				System.out.println(message);
			} catch(IOException exception)
			{
				System.err.println("Main listener : " + exception);
			}
		}
	}
}
