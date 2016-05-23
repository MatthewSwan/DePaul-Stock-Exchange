package tradable;

import price.Price;
import price.exceptions.InvalidPriceOperation;
import constants.global.BookSide;

/**
 *   The Tradable interface creates all behaviors that Tradable objects
 *   are able to perform within the application
 * 
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */

public interface Tradable	{
	
	/**
	 * 
	 * @return the product's symbol of the Tradable
	 */
	String getProduct();
	
	/**
	 * 
	 * @return the price of the Tradable
	 */
	Price getPrice();
	
	/**
	 * 
	 * @return the original quantity of the Tradable
	 */
	int getOriginalVolume();
	
	/**
	 * 
	 * @return the remaining quantity of the Tradable
	 */
	int getRemainingVolume();
	
	/**
	 * 
	 * @return the cancelled quantity of the Tradable
	 */
	int getCancelledVolume();
	
	/**
	 * This method set's the Tradable's quantity to the argument passed in
	 * @param newCancelledVolume the cancelled volume to be set in the method
	 * @throws InvalidVolumeException
	 */
	void setCancelledVolume(int newCancelledVolume) 
			throws InvalidPriceOperation;

	/**
	 * This method set's the Tradable's remaining quantity to the argument passed in
	 * @param newRemainingVolume the remaining volume to be set in the method
	 * @throws InvalidVolumeException
	 */
	void setRemainingVolume(int newRemainingVolume) 
			throws InvalidPriceOperation; 
	
	
	/**
	 * 
	 * @return the UserId that goes with the called Tradable
	 */
	String getUser();
	
	/**
	 * 
	 * @return the side (BUY/SELL) of the Tradable
	 */
	BookSide getSide();
	
	/**
	 * 
	 * @return true is the Tradable is part of an Quote, false if it's
	 * anything else (i.e., Order)
	 */
	boolean isQuote();
	
	/**
	 * 
	 * @return the Tradable's "Id"
	 */
	String getId();
}	
	