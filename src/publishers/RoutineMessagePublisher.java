package publishers;

import client.User;
import publishers.exceptions.MessagePublisherException;

/**
 * Interface defining the behaviors that a RoutineMessagePublisher object
 * should implement.
 *
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public interface RoutineMessagePublisher {

	/**
	 * Publishers need a "subscribe" method for users to call so they can
	 * subscribe for data. When calling "subscribe", users will provide a "User"
	 * reference (a reference to themselves), and the String stock symbol
	 * they are interested.
	 *
	 * @param userName Name of the user subscribing to the stock
	 * @param product Stock being represented
	 * @throws MessagePublisherException
	 */
	public void subscribe(User userName, String product) throws MessagePublisherException;

	/**
	 * Publishers need a "unSubscribe" method for users to call so they can
	 * un-subscribe for data. When calling "unSubscribe", users will provide
	 * a "User" reference (a reference to themselves), and the String stock symbol
	 * they are interested un-subscribing from.
	 *
	 * @param userName Name of the user un-subscribing to the stock
	 * @param product Stock being represented
	 * @throws MessagePublisherException
	 */
	public void unSubscribe(User userName, String product) throws MessagePublisherException;
}