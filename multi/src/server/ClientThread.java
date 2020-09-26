package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

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
				
				if (protocol == NetworkProtocol.LEAVE)
				{
					// La socket actuelle est expirée
					GlobalBuffer.getInstance().addExpiredSocket(socket);
					Thread.sleep(1000);
					GlobalBuffer.getInstance().addMessage(message);
					loop = false;
				}  else if(protocol == NetworkProtocol.EXCHANGE_MESSAGE)
				{
					// on ajoute le message dans le buffer pour qu'il soit envoyé aux clients
					GlobalBuffer.getInstance().addMessage(message);
				}
			} catch(ClassNotFoundException | IOException | InterruptedException exception)
			{
				System.err.println(exception);
			}
		}
	}
}