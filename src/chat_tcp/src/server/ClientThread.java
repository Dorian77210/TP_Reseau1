package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import common.Message;
import common.NetworkProtocol;

/**
 * Classe repr�sentant le thread d�di� au client qui �coute sur la socket de communication
 * @author Dorian et Fanny
 *
 */
public class ClientThread
	extends Thread {
	
	private Socket socket;
	
	/**
	 * Constructeur d'un ClientThread
	 * @param socket La socket du ClientThread
	 */
	ClientThread(Socket socket){
		this.socket = socket;
		// join
		GlobalBuffer.getInstance().addSocket(socket);
	}

	@Override
	public void run()
	{
		// L'utilisateur commence � chatter
		
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
				
				if (protocol == NetworkProtocol.LEAVE)
				{
					// La socket actuelle est expir�e
					GlobalBuffer.getInstance().addExpiredSocket(socket);
					Thread.sleep(1000);
					GlobalBuffer.getInstance().addMessage(message);
					loop = false;
				}  else if(protocol == NetworkProtocol.EXCHANGE_MESSAGE)
				{
					// on ajoute le message dans le buffer pour qu'il soit envoy� aux clients
					GlobalBuffer.getInstance().addMessage(message);
				}
			} catch(ClassNotFoundException | InterruptedException exception)
			{
				System.err.println(exception);
			} catch(IOException ioe)
			{
				try
				{
					this.socket.close();
				} catch(IOException e)
				{
					// nothing
				}
				
				GlobalBuffer.getInstance().addExpiredSocket(this.socket);
				GlobalBuffer.getInstance().addMessage(new Message("Un utilisateur a quitt� le chat", NetworkProtocol.LEAVE));
				
				break;
			}
		}
	}
}