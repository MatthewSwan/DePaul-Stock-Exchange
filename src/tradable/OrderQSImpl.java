package tradable;

import price.Price;
import price.exceptions.InvalidPriceOperation;
import tradable.exceptions.TradableException;
import constants.global.BookSide;

/**
 * The Tradable interface's Impl class
 * 
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */

public class OrderQSImpl implements Tradable	{
	
	OrderQSImpl self = this;

	/**
	 * Product the Tradable works with
	 */
	private String product;
	
	
	/**
	 * Price of the Tradable
	 */
	private Price price;
	
	/**
	 * Original quantity of the Tradable
	 */
	private int originalVolume;
	
	/**
	 * Remaining quantity of the Tradable
	 */
	private int remainingVolume;

	 /**
	  * Cancelled quantity of the Tradable
	  */
	 private int cancelledVolume;

	 /**
	  * User id for the tradable
	  */
	 private String user;

	 /**
	  * "Side" (BUY/SELL) of the Tradable.
	  */
	 private BookSide side;

	 /**
	  * True is Tradable is a Quote, false otherwise
	  */
	 private boolean isQuote;

	 /**
	  * The Tradable "id" 
	  */
	 private String id;
	
	 public OrderQSImpl(String userName, String productSymbol, Price orderPrice, 
		  int originalVolume, boolean isQuote, BookSide side, String theId)
		  throws InvalidPriceOperation, TradableException	{
	  setUser(userName);
	  setProduct(productSymbol);
	  setPrice(orderPrice);
	  setOriginalVolume(originalVolume);
	  setRemainingVolume(originalVolume);
	  setCancelledVolume(0);
	  setQuote(isQuote);
	  setSide(side);
	  setId(theId);
	 }
	 
	 /**
	  * Method to get the product associated with the Tradable
	  * @return product associated with Tradable
	  */
	 public final String getProduct() {
	   return product;
	 }

	 /**
	  * Sets the product to String passed as argument
	  * @param product must be of type String
	  * @throws TradableException
	  */
	 private void setProduct(String product) throws TradableException {
	   checkArg(product);
	   self.product = product;
	 }
	 
	 /**
	  * Method to get the price associated with the Tradable
	  * @return price associated with Tradable
	  */
	 public final Price getPrice() {
	   return price;
	 }
	 
	 /**
	  * Set's the price to the Price passed as an argument
	  * @param price must be of type Price
	  * @throws TradableException
	  */
	 private void setPrice(Price price) throws TradableException {
	   checkArg(price);
	   self.price = price;
	 }
	 
	 /**
	  * Method to get the original amount associated with the Tradable
	  * @return original amount associated with Tradable
	  */
	 public final int getOriginalVolume() {
	   return originalVolume;
	 }
	 
	 /**
	  * Sets the original volume associated with the Tradable
	  * @param newOriginalVolume must be of type int
	  * @throws InvalidVolumeException
	  */
	 private void setOriginalVolume(int newOriginalVolume) throws InvalidPriceOperation {
	   if (newOriginalVolume < 1) {
	     throw new InvalidPriceOperation("Invalid original volume " +
	             "is being set: " + newOriginalVolume);
	   }
	   originalVolume = newOriginalVolume;
	 }
	 
	 /**
	  * Method to get the remaining amount associated with the Tradable
	  * @return remaining amount associated with Tradable
	  */
	 public final int getRemainingVolume() {
	   return remainingVolume;
	 }
	 
	 /**
	  * Sets the remaining amount associated with the Tradable
	  * @param newRemainingVolume must be of type int
	  * @throws InvalidVolumeException
	  */
	 public final void setRemainingVolume(int newRemainingVolume) throws InvalidPriceOperation {
	   checkArg(newRemainingVolume);
	   remainingVolume = newRemainingVolume;
	 }

	 /**
	  * Method to get the cancelled amount associated with the Tradable 
	  * @return cancelled amount associated with Tradable
	  */
	 public final int getCancelledVolume() {
	   return cancelledVolume;
	 }

	 /**
	  * Sets the cancelled amount associated with the Tradable 
	  * @param newCancelledVolume must be of type int
	  * @throws InvalidVolumeException
	  */
	 public final void setCancelledVolume(int newCancelledVolume) throws InvalidPriceOperation {
	   checkArg(newCancelledVolume);
	   cancelledVolume = newCancelledVolume;
	 }
	 
	 /**
	  * Method to get the user associated with the Tradable
	  * @return the user associated with the Tradable
	  */
	 public final String getUser() {
	   return user;
	 }
	 
	 /**
	  * Sets the user associated with the Tradable
	  * @param user must by of type String
	  * @throws TradableException
	  */
	 private void setUser(String user) throws TradableException {
	   checkArg(user);
	   self.user = user;
	 }
	 
	 /**
	  * Method to get the side of the book associated with the Tradable
	  * @return side associated with the Tradable
	  */
	 public final BookSide getSide() {
	   return side;
	 }
	 
	 /**
	  * Sets the side of the book associated with the Tradable 
	  * @param side must be of type BookSide
	  * @throws TradableException
	  */
	 private void setSide(BookSide side)  {
	   self.side = side;
	 }

	 /**
	  * Method to determine if Tradable is a Quote or not
	  * @return true is Tradable is a Quote, false otherwise
	  */
	 public final boolean isQuote() {
	   return isQuote;
	 }
	 
	 /**
	  * Sets the Quote Tradable to a Quote if isQuote ended up being True 
	  * @param quote must be of type boolean
	  */
	 private void setQuote(boolean quote) {
	   self.isQuote = quote;
	 }

	 /**
	  * Method to get Id associated with Tradable 
	  * @return if associated with Tradable
	  */
	 public final String getId() {
	   return id;
	 }
	 
	 /**
	  * Sets id associated with Tradable
	  * @param id must be of type String
	  * @throws TradableException
	  */
	 private void setId(String id) throws TradableException {
	   checkArg(id);
	   self.id = id;
	 }
	 
	 /**
	  * Method to determine if the argument passed was of the correct type and not null
	  * @param s must be of type String
	  * @throws TradableException
	  */
	 private void checkArg(String s) throws TradableException {
	   if (s.isEmpty() || s == null) {
	     throw new TradableException("Argument must be of type String and"
	    		 + " cannot be null or empty.");
	   }
	 }
	 
	 /**
	  * Method to determine if the argument passed was of the correct type and not null
	  * @param s must be of type Price
	  * @throws TradableException
	  */
	 private void checkArg(Price s) throws TradableException {
	   if (s == null || !(s instanceof Price)) {
	     throw new TradableException("Argument must be of type Price and"
	             + " cannot be null.");
	   }
	 }
	 
	 /**
	  * Method to determine if the argument passed was of the correct type and not null
	  * @param s must be of type int
	  * @throws TradableException
	  */
	 private void checkArg(int s) throws InvalidPriceOperation {
	   if (s < 0 || s > originalVolume) {
	     throw new InvalidPriceOperation ("Argument cannot be negative "
	             + "or greater than the original volume.");
	 }
	}
}

