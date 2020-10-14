package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Point d'entr�e du client
 * @author dorian
 *
 */

public class MainClient {
	
	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args)
	{
		if (args.length != 2) {
	          System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
	          System.exit(1);
	    }
		
		MulticastSocket socket = null;
		InetAddress IPGroup = null;
		int portGroup = 0;
		
		try
		{
			IPGroup = InetAddress.getByName(args[0]);
			portGroup = Integer.parseInt(args[1]);
			socket = new MulticastSocket(portGroup);
			
			socket.joinGroup(IPGroup);
			
		} catch(IOException exception)
		{
			System.err.println(exception);
			System.exit(1);
		}
		
		// Cr�ation du thread d'�coute
		Thread listenerThread = new MainClientListenerThread(socket, IPGroup, portGroup);
		listenerThread.start();
		
		try
		{
			// Message writer
			
			int choice;
			boolean loop = true;
			BufferedReader scan = null;
			scan = new BufferedReader(new InputStreamReader(System.in));
			
			while (loop)
			{
				System.out.println("1 - Ecrire un message");
				System.out.println("2 - Quitter");
				
				try {
					choice = Integer.parseInt(scan.readLine());
					
					String message = null;
					
					switch(choice)
					{
						case 1:
							System.out.print("> ");
							message = scan.readLine();
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
	                    MainClient.sendMessage(message, true, socket, IPGroup, portGroup);
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			try
			{
				scan.close();
			} catch(IOException exception)
			{
				System.err.println(exception);
			}
			
			//End of write loop
			
			listenerThread.interrupt();
			
			MainClient.sendMessage("Un utilisateur a quitt� le chat", false, socket, IPGroup, portGroup);

			socket.leaveGroup(IPGroup);
			socket.close();
		} catch(IOException exception)
		{
			exception.printStackTrace();
			System.out.println("Close socket : " + exception);
		}
	}
	
	/**
	 * Envoie un message sur la SocketMulticast
	 * @param message Le message � envoyer
	 * @param sendDate Un booleen qui indique si la date est à envoyer ou non
	 * @param socket La socket du groupe
	 * @param IPGroup L'IP du groupe
	 * @param portGroup Le port du groupe
	 */
	public static void sendMessage(String message, boolean sendDate, MulticastSocket socket, InetAddress IPGroup, int portGroup){
		
		try {
			
			StringBuilder mess = new StringBuilder();
			if(sendDate) {
				Date date = new Date();
				
				String patternDate = "dd-MM-yyyy";
				SimpleDateFormat dateFormat = new SimpleDateFormat(patternDate);
				
				String patternHour = "HH:mm";
				SimpleDateFormat hourFormat = new SimpleDateFormat(patternHour);

				mess.append("Received on ");
				mess.append(dateFormat.format(date));
				mess.append(" at ");
				mess.append(hourFormat.format(date));
				mess.append(" : ");
			}
			mess.append(message);
			
			
			byte[] buffer = mess.toString().getBytes(); 
			DatagramPacket datagram = new
			DatagramPacket(buffer, buffer.length, IPGroup, portGroup); 
			socket.send(datagram);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
