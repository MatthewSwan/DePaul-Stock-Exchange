package tradable;

import price.Price;
import price.exceptions.InvalidPriceOperation;
import tradable.exceptions.TradableException;
import constants.global.BookSide;

/**
 * QuoteSide is the price and volume for a certain side of a Quote,
 * (BUY or SELL side). Each side of the Quote represents a certain stock's
 * price and volume of which a user is willing to buy or sell at.
 * 
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */

public class QuoteSide implements Tradable	{
	
		private Tradable newQuoteSide;

	/**
	 * 	
	 * @param userName Tradable's userName
	 * @param productSymbol Tradable's symbol
	 * @param sidePrice Price of the specific side of the BookSide	
	 * @param originalVolume original amount of Tradable
	 * @param side BookSide side the user wishes to be on
	 * @throws InvalidPriceOperation
	 * @throws TradableException
	 */
	public QuoteSide (String userName, String productSymbol, Price sidePrice, int originalVolume,
			BookSide side) throws InvalidPriceOperation, TradableException	{
		newQuoteSide = OrderQSImplFactory.create(userName, productSymbol, sidePrice, originalVolume,
				true, side, userName + productSymbol + System.nanoTime());
	}
	
	/**
	 * Copy constructor
	 * @param qs must be of type QuoteSide
	 */
	public QuoteSide(QuoteSide qs) throws InvalidPriceOperation, TradableException	{
		newQuoteSide = OrderQSImplFactory.create(qs.getUser(), qs.getProduct(), qs.getPrice(), qs.getOriginalVolume(), 
				qs.isQuote(), qs.getSide(), qs.getUser() + qs.getProduct() + System.nanoTime());
	}
	
	/**
	 * @return the QuoteSide's user name
	 */
	public String getUser()	{
		return newQuoteSide.getUser();
	}
	
	/**
	 * @return the QuoteSide's product name 
	 */
	public String getProduct()	{
		return newQuoteSide.getProduct();
	}
	
	/**
	 * @return the Quoteside's price
	 */
	public Price getPrice()	{
		return newQuoteSide.getPrice();
	}
	
	/**
	 * @return the QuoteSide's original volume
	 */
	public int getOriginalVolume()	{
		return newQuoteSide.getOriginalVolume();
	}
	
	/**
	 * @return the QuoteSide's remaining volume
	 */
	public int getRemainingVolume()	{
		return newQuoteSide.getRemainingVolume();
	}
	
	/**
	 * @return the QuoteSide's cancelled volume
	 */
	public int getCancelledVolume()	{
		return newQuoteSide.getCancelledVolume();
	}
	
	/**
	 * Sets the QuoteSide's cancelled volume to the passed argument
	 * @param newCancelledVolume must be type int
	 * @throws InvalidVolumeException
	 */
	public void setCancelledVolume(int newCancelledVolume) throws InvalidPriceOperation	{
		newQuoteSide.setCancelledVolume(newCancelledVolume);
	}
	
	/**
	 * Sets the newQuoteSide's remaining volume to the passed argument
	 * @param newRemainingVolume must by type int
	 * @throws InvalidVolumeException
	 */
	public void setRemainingVolume(int newRemainingVolume) throws InvalidPriceOperation	{
		newQuoteSide.setRemainingVolume(newRemainingVolume);
	}
	
	/**
	 * @return the side of the quote you wish to be on
	 */
	public BookSide getSide()	{
		return newQuoteSide.getSide();
	}
	
	/**
	 * @return true is the Tradable is a Quote, false otherwise
	 */
	public boolean isQuote()	{
		return newQuoteSide.isQuote();
	}
	/**
	 * @return Tradable's Id
	 */
	public String getId()	{
		return newQuoteSide.getId();
	}
	
	/**
	 * @return Formatted String
	 */
	public String toString()	{
		return String.format("%s x %s " + "(Original Vol: %s, CXL'd: %s) [%s]",
				newQuoteSide.getPrice(), newQuoteSide.getRemainingVolume(),
				newQuoteSide.getOriginalVolume(), newQuoteSide.getCancelledVolume(), 
				newQuoteSide.getId());
	}
}
