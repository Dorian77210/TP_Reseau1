package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import common.Message;

public class MainClientListenerThread extends Thread 
{
	/**
	 * La socket du client
	 */
	private Socket socket;
	
	/**
	 * Constructeur de la class MainClientListenerThread
	 * @param socket La socket du client
	 */
	public MainClientListenerThread(Socket socket)
	{
		this.socket = socket;
	}
	
	@Override
	public void run()
	{
		ObjectInputStream in = null;
		
		try
		{
			in = new ObjectInputStream(this.socket.getInputStream());
		} catch(IOException exception)
		{
			System.err.println(exception);
			return;
		}
		
		Message message;
		
		while (!this.isInterrupted())
		{
			try
			{
				message = (Message) in.readObject();
				System.out.println(message);
			} catch(IOException | ClassNotFoundException exception)
			{
				System.err.println("Main listener : " + exception);
			}
		}
	}
}
