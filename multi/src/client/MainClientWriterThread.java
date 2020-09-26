package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import common.Message;
import common.NetworkProtocol;

public class MainClientWriterThread extends Thread 
{
	/**
	 * La socket du client
	 */
	private Socket socket;
	
	/**
	 * Constructeur par défaut de MainClientWriterThread
	 * @param socket La socket du client
	 */
	public MainClientWriterThread(Socket socket)
	{
		this.socket = socket;
	}
	
	@Override
	public void run()
	{
		int choice;
		boolean loop = true;
		ObjectOutputStream out = null;
		
		try
		{
			out = new ObjectOutputStream(this.socket.getOutputStream());
		} catch(IOException exception)
		{
			System.err.println("Une erreur est survenue");
			return;
		}
		
		
		Scanner scanner = new Scanner(System.in);
		
		while (loop)
		{
			this.displayChoices();
			choice = scanner.nextInt();
			String content;
			Message message = null;
			
			switch(choice)
			{
				case 1:
					System.out.println(">");
					content = scanner.next();
					message = new Message(content, NetworkProtocol.EXCHANGE_MESSAGE);
				break;
				case 2:
					content = "Un utilisateur a quitté le chat";
					message = new Message(content, NetworkProtocol.LEAVE);
					loop = false;
					System.out.println("Bye bye");
				break;
				
				default:
					System.out.println("Mauvais choix");
				break;
			}
			
			if (message != null)
			{
				// envoie du message
				try
				{
					out.writeObject(message);
				} catch(IOException exception)
				{
					System.out.println("ok");
					exception.printStackTrace();
					System.err.println("Send message : " + exception);
				}		
			}
		}
		
		scanner.close();
	}
	
	/**
	 * Affiche le menu des choix possibles
	 */
	private void displayChoices()
	{
		System.out.println("1 - Ecrire un message");
		System.out.println("2 - Quitter");
	}
}
