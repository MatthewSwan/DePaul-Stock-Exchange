package publishers;

import publishers.exceptions.MessagePublisherException;
import messages.CancelMessage;
import messages.FillMessage;
import messages.MarketMessage;

/**
 * Interface specifying the behaviors a MessagePublisher should implement.
 *
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public interface DistinctMessagePublisher extends RoutineMessagePublisher {

	/**
	 * Notifies the user of a canceled order.
	 *
	 * @param cm CancelMessage type object
	 */
	public void publishCancel(CancelMessage cm) throws MessagePublisherException;

	/**
	 * Notifies the user of a fulfilled order.
	 *
	 * @param fm FillMessage type object
	 */
	public void publishFill(FillMessage fm) throws MessagePublisherException;

	/**
	 * Notifies all users of a market message.
	 *
	 * @param mm MerketMessage type object
	 */
	public void publishMarketMessage(MarketMessage mm) throws MessagePublisherException;
}