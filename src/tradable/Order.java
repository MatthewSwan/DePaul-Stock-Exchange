package tradable;

import tradable.Tradable;
import tradable.exceptions.*;
import price.Price;
import constants.global.BookSide;
import price.exceptions.InvalidPriceOperation;
import tradable.exceptions.TradableException;

/**
 *   Order represents a request from a user to BUY or SELL a specific quantity
 *   of certain stock either at a specified price, or at the current market price.
 *   The Order class should implement the Tradable interface.
 * 
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */

public class Order implements Tradable {

	  private Tradable thisOrder;

	  public Order(String theUserName, String theProductSymbol,
	          Price theOrderPrice, int theOriginalVolume,
	          BookSide theSide)
	          throws TradableException, InvalidPriceOperation {
	    thisOrder = OrderQSImplFactory.create(theUserName, theProductSymbol, theOrderPrice,
	            theOriginalVolume, false, theSide, theUserName + theProductSymbol +
	            theOrderPrice + System.nanoTime());
	  }

	  @Override
	  public String getProduct() {
	    return thisOrder.getProduct();
	  }

	  @Override
	  public Price getPrice() {
	    return thisOrder.getPrice();
	  }

	  @Override
	  public int getOriginalVolume() {
	    return thisOrder.getOriginalVolume();
	  }

	  @Override
	  public int getRemainingVolume() {
	    return thisOrder.getRemainingVolume();
	  }

	  @Override
	  public int getCancelledVolume() {
	    return thisOrder.getCancelledVolume();
	  }

	  @Override
	  public void setCancelledVolume(int newCancelledVolume) throws InvalidPriceOperation {
	    thisOrder.setCancelledVolume(newCancelledVolume);
	  }

	  @Override
	  public void setRemainingVolume(int newRemainingVolume) throws InvalidPriceOperation {
	    thisOrder.setRemainingVolume(newRemainingVolume);
	  }

	  @Override
	  public String getUser() {
	    return thisOrder.getUser();
	  }

	  @Override
	  public BookSide getSide() {
	    return thisOrder.getSide();
	  }

	  @Override
	  public boolean isQuote() {
	    return thisOrder.isQuote();
	  }

	  @Override
	  public String getId() {
	    return thisOrder.getId();
	  }

	  @Override
	  public String toString() {
	    return String.format("%s order: %s %s %s at %s " +
	           "(Original Vol: %s, CXL'd: %s), ID: %s", thisOrder.getUser(),
	           thisOrder.getSide(), thisOrder.getRemainingVolume(),
	           thisOrder.getProduct(), thisOrder.getPrice(), thisOrder.getOriginalVolume(),
	           thisOrder.getCancelledVolume(), thisOrder.getId());
	  }
	}
