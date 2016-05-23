package book;

import book.exceptions.DataValidationException;
import book.exceptions.NoProductException;
import book.exceptions.OrderNotFoundException;
import book.exceptions.ProductExistsException;
import book.exceptions.ProductBookException;
import book.exceptions.ProductBookSideException;
import book.exceptions.ProductServiceException;
import constants.global.BookSide;
import constants.global.MarketState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import price.exceptions.InvalidPriceOperation;
import publishers.MessagePublisher;
import publishers.exceptions.MessagePublisherException;
import messages.MarketDataDTO;
import messages.MarketMessage;
import messages.exceptions.InvalidMessageException;
import tradable.Order;
import tradable.Quote;
import tradable.TradableDTO;
import tradable.exceptions.TradableException;
import book.exceptions.InvalidMarketStateException;
import book.exceptions.InvalidMarketStateTransitionException;
import book.exceptions.InvalidProductBookSideValueException;

/**
 *  The ProductService is the Façade to the entities that make up the Products
 * (Stocks), and the Product Books (tradables on the Buy and Sell
 * side). All interaction with the product books and the buy and sell sides of
 * a stock’s book will go through this Façade.
 *
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class ProductService {
  private volatile static ProductService instance;
  private ProductService()	{}
  /**
   * As this class must own all the product books, you will need a structure
   * that contains all product books, accessible by the stock symbol name.
   */
  private HashMap<String, ProductBook> allBooks = new HashMap<>();

  /**
   * As this class must maintain a data member that holds the current market
   * state.
   */
  private MarketState state = MarketState.CLOSED;

  /**
   * As this is a Façade, this class should be implemented as a thread-safe
   * singleton.
   *
   * @return the instance of the ProductService
   */
  public static ProductService getInstance() {
    if (instance == null) {
      synchronized (ProductService.class) {
        if (instance == null) {
          instance = new ProductService();
        }
      }
    }
    return instance;
  }


  /**
   * This method will return a List of TradableDTOs containing any orders with
   * remaining quantity for the user and the stock specified.
   *
   * @param userName Name of user passed in as argument
   * @param product Product name passed as argument
   * @return a list of TradeableDTOs
 * @throws ProductServiceException 
   */
  public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(String userName, String product) throws ProductServiceException {
	  validateInput(product);
	  validateInput(userName);
	  return allBooks.get(product).getOrdersWithRemainingQty(userName);
  }

  /**
   * This method will return a List of MarketDataDTO containing the best buy
   * price/volume and sell price/volume for the specified stock product.
   *
   * @param product Tradable object passed as argument
   * @return a List of MarketDataDTO
   * @throws InvalidPriceOperation 
 * @throws ProductServiceException 
   */
  public synchronized MarketDataDTO getMarketData(String product) throws InvalidPriceOperation, ProductServiceException {
	  validateInput(product);
	  return allBooks.get(product).getMarketData();
  }

  /**
   * This method should simply return the current market state.
   *
   * @return the current market state
   */
  public synchronized MarketState getMarketState() {
    return state;
  }

  /**
   * Returns the Bookdepth for the product passed in.
   *
   * @param product
   * @return a 2-D array of the product book depth
 * @throws ProductServiceException 
   */
  public synchronized String[][] getBookDepth(String product) throws NoProductException, ProductServiceException {
	  validateInput(product);
	  if (!allBooks.containsKey(product)) {
       throw new NoProductException("The product: " + product + "; does not exist in the product book.");
    }
    return allBooks.get(product).getBookDepth();
  }

  /**
   * This method should simply return an Arraylist containing all the keys in
   * the "allBooks" HashMap.
   *
   * @return an ArrayList of all Products
   */
  public synchronized ArrayList<String> getProductList() {
    return new ArrayList<>(allBooks.keySet());
  }

  /**
   * Method telling if the market transition is valid or not
   * 
   * @param ms State of the Market
   * @return
 * @throws ProductServiceException 
   */
  private synchronized boolean isValidTransition(MarketState ms) throws ProductServiceException {
    validateInput(ms);
	ArrayList<MarketState> transformation = new ArrayList<>(Arrays.asList( MarketState.CLOSED, MarketState.PREOPEN, MarketState.OPEN ));
    int msPass = transformation.indexOf(ms);
    int msCurrent = transformation.indexOf(state);
    int diff = msPass - msCurrent;
    if (msCurrent == 2 && msPass == 0) { 
    	return true; 
    	}
    if (msCurrent < msPass && diff == 1) { 
    	return true; 
    	}
    return false;
  }

  /**
   * This method should update the market state to the new value passed in.
   *
   * @param ms State of the market passed as argument
   * @throws InvalidPriceOperation 
   * @throws MessagePublisherException 
 * @throws ProductServiceException 
   */
  public synchronized void setMarketState(MarketState ms) throws InvalidMarketStateTransitionException, 
  InvalidMessageException, OrderNotFoundException, TradableException, InvalidPriceOperation, 
  MessagePublisherException, ProductServiceException {
	  validateInput(ms);
	  if (!isValidTransition(ms)) {
      throw new InvalidMarketStateTransitionException("The market state transition: " +
              ms + "; is invalid, current market state is: " + state);
    }
    state = ms;
    MessagePublisher.getInstance().publishMarketMessage(new MarketMessage(state));
    if (state.equals(MarketState.OPEN)) {
      for (Entry<String, ProductBook> row : allBooks.entrySet()) {
        row.getValue().openMarket();
      }
    }
    if (state.equals(MarketState.CLOSED)) {
      for (Entry<String, ProductBook> row : allBooks.entrySet()) {
        row.getValue().closeMarket();
      }
    }
  }

  /**
   * This method will create a new stock product that can be used for trading.
   * This will result in the creation of a ProductBook object, and a new entry
   * in the "allBooks" HashMap.
   *
   * @param product Product to be created 
 * @throws ProductServiceException 
   */
  public synchronized void createProduct(String product) throws DataValidationException, ProductExistsException,
          ProductBookException, ProductBookSideException, InvalidProductBookSideValueException, ProductServiceException {
    validateInput(product);
	if (product == null || product.isEmpty()) {
      throw new DataValidationException("Product symbol cannot be null or empty.");
    }
    if (allBooks.containsKey(product)) {
      throw new ProductExistsException("Product " + product + " already exists in the ProductBook.");
    }
    allBooks.put(product, new ProductBook(product));
  }

  /**
   * This method should forward the provided Quote to the appropriate product
   * book.
   *
   * @param q Quote object passed as argument
   * @throws InvalidPriceOperation 
   * @throws MessagePublisherException 
 * @throws ProductServiceException 
   */
  public synchronized void submitQuote(Quote q) throws InvalidMarketStateException, NoProductException,
  	TradableException, DataValidationException, InvalidMessageException, InvalidPriceOperation,
    MessagePublisherException, ProductServiceException {
    validateInput(q);
	if (state.equals(MarketState.CLOSED)) {
      throw new InvalidMarketStateException("Marekt is closed!");
    }
    if (!allBooks.containsKey(q.getProduct())) {
      throw new NoProductException("Product does not exist in any book.");
    }
    allBooks.get(q.getProduct()).addToBook(q);
  }


  /**
   * This method should forward the provided Order to the appropriate product
   * book.
   *
   * @param o Order object passed as argument
   * @return the string id of the order
   * @throws InvalidPriceOperation 
   * @throws MessagePublisherException 
 * @throws ProductServiceException 
   */
  public synchronized String submitOrder(Order o) throws InvalidMarketStateException, NoProductException, 
  	InvalidMessageException, TradableException, InvalidPriceOperation, MessagePublisherException, ProductServiceException {
    validateInput(o);
	if (state.equals(MarketState.CLOSED)) {
      throw new InvalidMarketStateException("Marekt is closed!");
    }
    if (state.equals(MarketState.PREOPEN) && o.getPrice().isMarket()) {
      throw new InvalidMarketStateException("Market is pre-open, cannot submit" + " MKT orders at this time.");
    }
    if (!allBooks.containsKey(o.getProduct())) {
      throw new NoProductException("Product does not exist in any book.");
    }
    allBooks.get(o.getProduct()).addToBook(o);
    return o.getId();
  }

  /**
   * This method should forward the provided Order Cancel to the appropriate
   * product book.
   *
   * @param product Product name passed as argument
   * @param side BookSide object passed as argument representing side (BUY or SELL)
   * @param orderId Id of the stock passed as argument
   * @throws InvalidMarketStateException
   * @throws NoProductException
   * @throws InvalidMessageException
   * @throws OrderNotFoundException
   * @throws InvalidPriceOperation 
   * @throws MessagePublisherException 
 * @throws ProductServiceException 
   * @throws InvalidVolumeException
   */
  public synchronized void submitOrderCancel(String product, BookSide side,
          String orderId) throws InvalidMarketStateException,
          NoProductException, InvalidMessageException,
          OrderNotFoundException, TradableException, InvalidPriceOperation, MessagePublisherException, ProductServiceException {
    validateInput(product);
    validateInput(side);
    validateInput(orderId);
	if (state.equals(MarketState.CLOSED)) {
      throw new InvalidMarketStateException("Marekt is closed!");
    }
    if (!allBooks.containsKey(product)) {
      throw new NoProductException("Product does not exist in any book.");
    }
    allBooks.get(product).cancelOrder(side, orderId);
  }

  /**
   * This method should forward the provided Quote Cancel to the appropriate
   * product book.
   *
   * @param userName Name of user passed as argument
   * @param product Product name of the stock passed as argument
   * @throws InvalidPriceOperation 
   * @throws MessagePublisherException 
 * @throws ProductServiceException 
   */
  public synchronized void submitQuoteCancel(String userName, String product) throws InvalidMarketStateException,
  	NoProductException, InvalidMessageException, InvalidPriceOperation, MessagePublisherException, ProductServiceException {
    validateInput(userName);
    validateInput(product);
	if (state.equals(MarketState.CLOSED)) {
      throw new InvalidMarketStateException("Marekt is closed!");
    }
    if (!allBooks.containsKey(product)) {
      throw new NoProductException("Product does not exist in any book.");
    }
    allBooks.get(product).cancelQuote(userName);
  }
  
  /**
   * Validates a String input
   * 
   * @param o String input
   * @throws ProductServiceException
   */
  private void validateInput(String o)
          throws ProductServiceException {
    if (o == null || o.isEmpty()) {
      throw new ProductServiceException("Argument must be of type String and cannot be null or empty.");
    }
  }
  
  /**
   * Validates a marketState input
   * 
   * @param o MarketState object
   * @throws ProductServiceException
   */
  private void validateInput(MarketState o) throws ProductServiceException {
	    if (o == null || !(o instanceof MarketState)) {
	      throw new ProductServiceException("Argument cannot be null or not instance of MarketState");
	    }
	  }

  /**
   * Validates a Quote input
   * 
   * @param o Quote object
   * @throws ProductServiceException
   */
  private void validateInput(Quote o) throws ProductServiceException {
	    if (o == null || !(o instanceof Quote)) {
	      throw new ProductServiceException("Argument cannot be null or not instance of Quote");
	    }
	  }
  
  /**
   * Validates a Order input
   * 
   * @param o Order object
   * @throws ProductServiceException
   */
  private void validateInput(Order o) throws ProductServiceException {
	    if (o == null || !(o instanceof Order)) {
	      throw new ProductServiceException("Argument cannot be null or not instance of Order");
	    }
	  }
  
  /**
   * Validates BookSide input
   * 
   * @param o BookSide object
   * @throws ProductServiceException
   */
  private void validateInput(BookSide o) throws ProductServiceException {
	    if (o == null || !(o instanceof BookSide)) {
	      throw new ProductServiceException("Argument must be of type BookSide and"
	              + " cannot be null.");
	    }
	  }
	
}