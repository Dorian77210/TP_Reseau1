package server;

/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.net.*;

import common.Message;
import common.NetworkProtocol;

public class ClientThread
	extends Thread {
	
	private Socket socket;
	
	ClientThread(Socket socket){
		this.socket = socket;
		// join
		GlobalBuffer.getInstance().addSocket(socket);
	}

	@Override
	public void run()
	{
		// L'utilisateur commence à chatter
		
		ObjectInputStream stream = null;
		try
		{
			stream = new ObjectInputStream(this.socket.getInputStream());
		} catch(IOException exception)
		{
			System.err.println(exception);
		}
		
		boolean loop = true;
		while (loop)
		{
			try
			{
				Message message = (Message) stream.readObject();
				NetworkProtocol protocol = message.getProtocol();
				if (protocol.equals(NetworkProtocol.LEAVE))
				{
					// La socket actuelle est expirée
					GlobalBuffer.getInstance().addExpiredSocket(socket);
					loop = false;
				}  else if(protocol.equals(NetworkProtocol.EXCHANGE_MESSAGE))
				{
					// on ajoute le message dans le buffer pour qu'il soit envoyé aux clients
					GlobalBuffer.getInstance().addMessage(message);
				}
			} catch(ClassNotFoundException | IOException exception)
			{
				System.err.println(exception);
			}
		}
	}
}