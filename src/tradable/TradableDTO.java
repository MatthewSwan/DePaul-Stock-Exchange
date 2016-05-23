package tradable;

import price.Price;
import constants.global.BookSide;

/**
 *   TradableDTO class is based upon the "Data Transfer Object" design pattern.
 *   It will act as a "data holder" that holds selected data values from a "Tradable"
 *   object.
 * 
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */

public class TradableDTO {
	
	/**
	 * 
	 * @return the product
	 */
	public String product;
	
	/**
	 * 
	 * @return the price of the Tradable
	 */
	public Price price;
	
	/**
	 * 
	 * @return the original volume of the Tradable
	 */
	public int originalVolume;
	
	/**
	 * 
	 * @return the remaining volume of the Tradable
	 */
	public int remainingVolume;
	
	/**
	 * 
	 * @return the cancelled volume of the Tradable
	 */
	public int cancelledVolume;
	
	/**
	 * 
	 * @return the User id associated with the Tradable
	 */
	public String user;
	
	/**
	 * 
	 * @return BUY/SELL of the Tradable
	 */
	public BookSide side;
	
	/**
	 * 
	 * @return boolean value isQuote
	 */
	public boolean isQuote;
	
	/**
	 * 
	 * @return the value each tradable is given once it is received by the system
	 */
	public String id;
	
	/**
	 * Create the TradableDTO class with all its args
	 * @param theProduct the Tradable product
	 * @param thePrice Tradable price
	 * @param theOriginalVolume original amount of the Tradable
	 * @param theRemainingVolume remaining amount of the Tradable
	 * @param theCancelledVolume cancelled amount of the Tradable
	 * @param theUser 
	 * @param theBookSide which side of BookSide you are in
	 * @param isItAQuote is it a Quote
	 * @param theId Id of the Tradable
	 */
	public TradableDTO(String theProduct, Price thePrice, int theOriginalVolume, int theRemainingVolume,
			int theCancelledVolume, String theUser, BookSide theBookSide, boolean isItAQuote, String theId) {
		product = theProduct;
		price = thePrice;
		originalVolume = theOriginalVolume;
		remainingVolume = theRemainingVolume;
		cancelledVolume = theCancelledVolume;
		user = theUser;
		side = theBookSide;
		isQuote = isItAQuote;
		id = theId;
	}
	
	/**
	 * Method formatting the String to whatever we want to appear
	 * @return formatted String
	 */
	public String toString() {
		return String.format("%s %s %s %s at %s " + "(Original Vol: %s, CXL'd: %s), isQuote: %s, ID: %s", user, side, product,
				remainingVolume, price, originalVolume, cancelledVolume, isQuote, id);
	}

	
}
