package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;

import common.Message;
import common.NetworkProtocol;

public class MainClientWriterThread extends Thread 
{
	/**
	 * Le flux de sortie
	 */
	private ObjectOutputStream out;
	
	/**
	 * Constructeur par défaut de MainClientWriterThread
	 * @param out Le flux de sortie 
	 */
	public MainClientWriterThread(ObjectOutputStream out)
	{
		this.out = out;
	}
	
	@Override
	public void run()
	{
		int choice;
		boolean loop = true;
		BufferedReader scan = null;
		scan = new BufferedReader(new InputStreamReader(System.in));
		
		while (loop)
		{
			this.displayChoices();
			try
			{
				choice = Integer.parseInt(scan.readLine());
			} catch(IOException exception)
			{
				System.out.println("Mauvais choix !");
				continue;
			}
			
			String content;
			Message message = null;
			
			switch(choice)
			{
				case 1:
					System.out.print("> ");
					try
					{
						content = scan.readLine();
						message = new Message(content, NetworkProtocol.EXCHANGE_MESSAGE);
					} catch(IOException exception)
					{
						System.err.println(exception);
					}
				break;
				case 2:
					loop = false;
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
					this.out.writeObject(message);
				} catch(IOException exception)
				{
					exception.printStackTrace();
					System.err.println("Send message : " + exception);
				}		
			}
		}
		
		try
		{
			scan.close();
		} catch(IOException exception)
		{
			System.err.println(exception);
		}
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
