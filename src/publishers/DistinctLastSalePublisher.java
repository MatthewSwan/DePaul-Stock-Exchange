package publishers;

import price.Price;
import price.exceptions.InvalidPriceOperation;
import publishers.exceptions.MessagePublisherException;

/**
 * Interface specifying the specific behaviors a LastSalePublisher
 * should implement.
 *
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public interface DistinctLastSalePublisher extends RoutineMessagePublisher {

	/**
	 * Notifies users of the last sale out for the given stock.
	 * (Stock Symbol, Price, Volume)
	 *
	 * @param product Stock to be published to users
	 * @param p Price of the current Stock
	 * @param v Quantity of the stock
	 * @throws InvalidPriceOperation 
	 */
	public void publishLastSale(String product, Price p, int v) throws MessagePublisherException, InvalidPriceOperation;
}