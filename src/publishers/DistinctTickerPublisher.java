package publishers;

import price.Price;
import publishers.exceptions.MessagePublisherException;

/**
 * A interface specifying the behaviors a TickerPublisher should implement.
 *
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public interface DistinctTickerPublisher extends RoutineMessagePublisher {

  /**
   * Notifies users of the last sale out for the given stock.
   * (Stock Symbol, Price)
   *
   * @param product stock represented as a String
   * @param p Price of the stock
   */
	public void publishTicker(String product, Price p) throws MessagePublisherException;
}