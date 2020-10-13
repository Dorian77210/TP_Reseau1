package common;

/**
 * Enumération représentant le type d'un message
 * @author Dorian et Fanny
 *
 */
public enum NetworkProtocol {
	/**
	 * Représente un message qui annonce le départ d'un utilisateur.
	 */
	LEAVE,
	/**
	 * Représente un message lambda.
	 */
	EXCHANGE_MESSAGE
}
