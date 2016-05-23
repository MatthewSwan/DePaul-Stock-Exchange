package client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import constants.global.BookSide;
import client.exceptions.PositionException;
import price.Price;
import price.PriceFactory;
import price.exceptions.InvalidPriceOperation;
import price.exceptions.PriceException;

/**
 * The Position class is used to hold an individual users profit and loss information, 
 * including how much they have spent buying stock, how much they gained or lost 
 * selling stock, and the value of the stock they currently own.
 * 
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class Position {

	/**
	 * A HashMap to store the "holdings" of the user. The "key" is the stock, and 
	 * "value" is the number of shares they own. 
	 */
	HashMap<String, Integer> holdings;
	
	/**
	 * Price object to hold the "account costs" for this user. This will keep a running
	 * balance between the "money out" for stock purchases, and the "money in" for stock
	 * sales. 
	 */
	Price accountCosts;
	
	/**
	 * a HashMap to store the "last sales" of the stocks this user owns. Last sales indicate
	 * the current value of the stocks they own.
	 */
	HashMap<String, Price> lastSales;
	
	/**
	 * Constructor
	 * @throws InvalidPriceOperation
	 */
	public Position() throws InvalidPriceOperation	{
		holdings = new HashMap<>();
		accountCosts = PriceFactory.makeLimitPrice(0);
		lastSales = new HashMap<>();
	}
	
	/**
	 * Updates the holdings list and the account costs when some market activity occurs.
	 * 
	 * @param product String stock symbol
	 * @param price Price of the stock
	 * @param side "side" of the stock
	 * @param volume quantity of the stock
	 */
	public void updatePosition(String product, Price price, BookSide side, int volume) 
		throws PositionException, InvalidPriceOperation, PriceException {
	    validateInput(product);
	    validateInput(price);
	    validateInput(side);
	    int adjustedVolume = (side.equals(BookSide.BUY) ? volume : -volume);
	    if (!holdings.containsKey(product)) {
	      holdings.put(product, adjustedVolume);
	    } else {
	      int resultingVolume = holdings.get(product) + adjustedVolume;
	      if (resultingVolume == 0) {
	        holdings.remove(product);
	      } else {
	        holdings.put(product, resultingVolume);
	      }
	    }
	    Price totalPrice = price.multiply(volume);
	    if (side.equals(BookSide.BUY)) {
	      accountCosts = accountCosts.subtract(totalPrice);
	    } else {
	      accountCosts = accountCosts.add(totalPrice);
	    }
	  }

	  /**
	   * This method should insert the last sale for the specified stock into the
	   * "last sales" HashMap (product parameter is the key, Price parameter is
	   * the value).
	   *
	   * @param product String stock symbol
	   * @param price Price of the stock
	   * @throws PositionException
	   */
	  public void updateLastSale(String product, Price price)
	          throws PositionException {
	    validateInput(product);
	    validateInput(price);
	    lastSales.put(product, price);
	  }

	  /**
	   * This method will return the volume of the specified stock this user owns.
	   *
	   * @param product String stock symbol
	   * @return the volume of stock this user owns
	   * @throws PositionException
	   */
	  public int getStockPositionVolume(String product) throws PositionException {
	    validateInput(product);
	    if (!holdings.containsKey(product)) { return 0; }
	    return holdings.get(product);
	  }

	  /**
	   * This method will return a sorted ArrayList of Strings containing the stock
	   * symbols this user owns.
	   *
	   * @return a sorted ArrayList of stock symbols this user owns
	   */
	  public ArrayList<String> getHoldings() {
	    ArrayList<String> h = new ArrayList<>(holdings.keySet());
	    Collections.sort(h);
	    return h;
	  }

	  /**
	   * This method will return the current value of the stock symbol passed in
	   * that is owned by the user.
	   *
	   * @param product String stock symbols
	   * @return return the current value of the stock symbol
	   * @throws PositionException
	   */
	  public Price getStockPositionValue(String product)
	          throws PositionException, InvalidPriceOperation {
	    validateInput(product);
	    if (!holdings.containsKey(product)) {
	      return PriceFactory.makeLimitPrice(0);
	    }
	    Price lastPrice = lastSales.get(product);
	    if (lastPrice == null) {
	      lastPrice = PriceFactory.makeLimitPrice(0);
	    }
	    return lastPrice.multiply(holdings.get(product));
	  }

	  /**
	   * This method simply returns the "account costs" data member.
	   *
	   * @return the account costs
	   */
	  public Price getAccountCosts() {
	    return accountCosts;
	  }

	  /**
	   * This method should return the total current value of all stocks this user
	   * owns.
	   *
	   * @return total current value of all stocks
	   * @throws InvalidPriceOperation
	   * @throws PositionException
	   */
	  public Price getAllStockValue()
	          throws InvalidPriceOperation, PositionException, PriceException {
	    Price sum = PriceFactory.makeLimitPrice(0);
	    for (String key : holdings.keySet()) {
	      sum = sum.add(getStockPositionValue(key));
	    }
	    return sum;
	  }

	  /**
	   * This method should return the total current value of all stocks this user
	   * owns PLUS the account costs.
	   *
	   * @return the net account value
	   */
	  public Price getNetAccountValue()
	          throws PositionException, InvalidPriceOperation, PriceException {
	    return getAllStockValue().add(getAccountCosts());
	  }
	  
	  /**
	   * Validates the input of String arguments
	   * 
	   * @param o String object passed as argument
	   * @throws PositionException
	   */
	  private void validateInput(String o) throws PositionException {
	    if (o == null || o.isEmpty()) {
	      throw new PositionException("Argument must be of type String and cannot be null or empty.");
	    }
	  }

	  /**
	   * Validates the input of a Price argument
	   * 
	   * @param o Price object passed as argument
	   * @throws PositionException
	   */
	  private void validateInput(Price o) throws PositionException {
	    if (o == null || !(o instanceof Price)) {
	      throw new PositionException("Argument must be of type Price and cannot be null.");
	    }
	  }

	  /**
	   * Validates the input of a BookSide argument
	   * 
	   * @param o BookSide object passed as argument
	   * @throws PositionException
	   */
	  private void validateInput(BookSide o) throws PositionException {
	    if (o == null || !(o instanceof BookSide)) {
	      throw new PositionException("Argument must be of type BookSide and cannot be null.");
	    }
	  }
}
