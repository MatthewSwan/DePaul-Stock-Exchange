package tradable;

import price.Price;
import price.exceptions.InvalidPriceOperation;
import tradable.exceptions.TradableException;
import constants.global.BookSide;

/**
 * This is a factory class used to create new OrderQSImpl objects for
 * a Quoteside object or an Order object
 * 
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */

public class OrderQSImplFactory	{

	/**
	 * Creates a new OrderSQImpl object
	 * @param userName name of user for Tradable
	 * @param productSymbol stock symbol for Tradable
	 * @param orderPrice order price for the Tradable
	 * @param originalVolume original amount of the Tradable
	 * @param isQuote determines if the Tradable is a Quote or not
	 * @param side which side book is the Tradable
	 * @param theId the tradable's id
	 * @return new OrderQsImpl object
	 * @throws InvalidVolumeException
	 * @throws TradableException
	 */
	public static OrderQSImpl create(String userName, String productSymbol, 
			Price orderPrice, int originalVolume, boolean isQuote, BookSide side,
			String theId) throws InvalidPriceOperation, TradableException	{
		return new OrderQSImpl (userName, productSymbol, orderPrice, originalVolume, 
			isQuote, side, theId);
	}
}
