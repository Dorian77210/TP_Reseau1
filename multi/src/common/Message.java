package common;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Classe qui représente un message entre le serveur et les client
 * @author dorian
 *
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Contenu du message
	 */
	@JsonFormat
	private String message;
	
	/**
	 * Date de création du message
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private Date date;
	
	/**
	 * Le protocole associé au message
	 */
	@JsonIgnore
	private NetworkProtocol protocol;
	
	// ------------- Constructeurs -------------- //
	
	/**
	 * Constructeur par défault de la classe Message
	 */
	public Message() {
		this.protocol = NetworkProtocol.UNDEFINED;
	}
	
	/**
	 * Constructeur de la classe Message
	 * @param message Le contenu du message
	 * @param protocol Permet de savoir si le message est un message d'échange ou de sortie du chat
	 */
	public Message(String message, NetworkProtocol protocol)
	{
		this.message = message;
		this.date = new Date();
		this.protocol = protocol;
	}
	
	// ------------- Méthodes -------------- //
	
	/**
	 *  Permet de récupérer le protocole du message
	 * @return Le protocole du message
	 */
	public NetworkProtocol getProtocol()
	{
		return this.protocol;
	}
	
	public String toString()
	{
		StringBuilder message = new StringBuilder();
		
		String patternDate = "dd-MM-yyyy";
		SimpleDateFormat dateFormat = new SimpleDateFormat(patternDate);
		
		String patternHour = "HH:mm";
		SimpleDateFormat hourFormat = new SimpleDateFormat(patternHour);

		switch (protocol) {
		case LEAVE : {
			break;
		}
		default:
			message.append("Received on ");
			message.append(dateFormat.format(this.date));
			message.append(" at ");
			message.append(hourFormat.format(this.date));
			message.append(" : ");
			break;
		}
		
		message.append(this.message);

		return message.toString();
	}
}
