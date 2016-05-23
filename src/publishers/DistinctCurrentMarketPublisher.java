package publishers;

import publishers.exceptions.MessagePublisherException;
import messages.MarketDataDTO;

/**
 * Interface specifying the specific behaviors a CurrentMarketPublisher
 * should implement.
 *
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */ 
public interface DistinctCurrentMarketPublisher extends RoutineMessagePublisher {
	
	/**
	 * Notifies all users of current market conditions for the given stock.
	 *
	 * @param m passed in MarketDataDTO object
	 */
	public void publishCurrentMarket(MarketDataDTO m)
          throws MessagePublisherException;
}