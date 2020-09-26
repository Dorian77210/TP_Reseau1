package common;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

/**
 * Classe qui repr�sente un message entre le serveur et les client
 * @author dorian
 *
 */
public class Message implements Serializable {
	
	private static final long serialVersionUID = 0xCafe;
	
	/**
	 * Contenu du message
	 */
	private String message;
	
	/**
	 * Date de cr�ation du message
	 */
	private Date date;
	
	/**
	 * Le protocole associ� au message
	 */
	private int protocol;
	
	// ------------- Constructeurs -------------- //
	
	/**
	 * Constructeur de la classe Message
	 * @param message Le contenu du message
	 * @param protocol Permet de savoir si le message est un message d'�change ou de sortie du chat
	 */
	public Message(String message, int protocol)
	{
		this.message = message;
		this.date = new Date();
		this.protocol = protocol;
	}
	
	
	/**
	 * Constructeur de la classe Message
	 * @param message Le contenu du message
	 * @param date La date de cr�ation du message
	 */
	private Message(String message, Date date)
	{
		this.message = message;
		this.date = date;
		this.protocol = NetworkProtocol.UNDEFINED;
	}
	
	// ------------- M�thodes -------------- //
	
	/**
	 * Permet d'avoir une repr�sentation du message sous format JSON
	 * @return Un object JSON contenant les informations du message
	 */
	public JSONObject toJSON()
	{
		JSONObject json = new JSONObject();
		json.append("message", this.message)
			.append("date", this.date.toString());
		
		return json;
	}
	
	/**
	 * Cr�� un Message � partir d'un JSON
	 * @param json Le JSON associ�
	 * @return Le message correspondant si le JSON est bien form�, sinon nul
	 */
	public static Message fromJSON(JSONObject json)
	{
		SimpleDateFormat formatter = new SimpleDateFormat();
		if (!json.has("message") || !json.has("date"))
		{
			return null;
		}
		
		String message = json.getString("message");
		String date = json.getString("date");
		Date currentDate = null;
		try 
		{
			currentDate = formatter.parse(date);
		} catch(Exception e)
		{
			System.err.println(e);
			return null;
		}
		
		return new Message(message, currentDate);
	}
	
	/**
	 *  Permet de r�cup�rer le protocole du message
	 * @return Le protocole du message
	 */
	public int getProtocol()
	{
		return this.protocol;
	}
	
	public String toString()
	{
		return "Received the " + this.date + " : " + this.message;
	}
}
