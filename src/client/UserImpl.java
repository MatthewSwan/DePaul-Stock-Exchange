package client;

import java.sql.Timestamp;
import java.util.ArrayList;

import messages.CancelMessage;
import messages.FillMessage;
import messages.exceptions.InvalidMessageException;
import book.exceptions.DataValidationException;
import book.exceptions.InvalidMarketStateException;
import book.exceptions.NoProductException;
import book.exceptions.OrderNotFoundException;
import book.exceptions.ProductBookException;
import book.exceptions.ProductBookSideException;
import book.exceptions.ProductServiceException;
import book.exceptions.TradeProcessorPriceTimeImplException;
import constants.global.BookSide;
import client.exceptions.AlreadyConnectedException;
import client.exceptions.InvalidConnectionIdException;
import client.exceptions.PositionException;
import client.exceptions.TradableUserDataException;
import client.exceptions.UserCommandException;
import client.exceptions.UserException;
import client.exceptions.UserNotConnectedException;
import gui.UserDisplayManager;
import price.Price;
import price.exceptions.InvalidPriceOperation;
import price.exceptions.PriceException;
import publishers.exceptions.MessagePublisherException;
import tradable.TradableDTO;
import tradable.exceptions.InvalidVolumeException;
import tradable.exceptions.TradableException;

/**
 * The UserImpl class is our application's implementation of the "User"
 * interface. This represents a "real" user in the trading system; many of
 * these objects can be active in our system.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class UserImpl implements User {

  /**
   * A String to hold their user name.
   */
  private String userName;

  /**
   * A long value to hold their "connection id" – provided to them when they
   * connect to the system.
   */
  private long connectionId;

  /**
   * A String list of the stocks available in the trading system. The user
   * fills this list once connected based upon data received from the
   * trading system.
   */
  private ArrayList<String> stocks;

  /**
   * A list of TradableUserData objects that contains information on the orders
   * this user has submitted (needed for canceling).
   */
  private ArrayList<TradableUserData> trades;

  /**
   * A reference to a Position object (part of this assignment) which holds
   * the values of the users stocks, costs, etc.
   */
  private Position position;

  /**
   * A reference to a UserDisplayManager object (part of this assignment) that
   * acts as a façade between the user and the market display.
   */
  private UserDisplayManager manager;

  /**
   * Constructor
   * 
   * @param userName userName of the user
   * @throws UserException
   * @throws InvalidPriceOperation
   */
  public UserImpl(String userName) throws UserException, InvalidPriceOperation {
    setUserName(userName);
    setStocks(new ArrayList<>());
    setTrades(new ArrayList<TradableUserData>());
    setPosition(new Position());
  }

  /**
   * This method should return the String user name of this user.
   *
   * @return the userName
   */
  public String getUserName() {
    return this.userName;
  }
  /**
   * Sets the user name for the user.
   * 
   * @param user String user passed in
   */
  public void setUserName(String user)	{
	  this.userName = user;
  }
  
  /**
   * Sets the stocks for the user.
   * 
   * @param s Stocks
   */
  private void setStocks(ArrayList<String> s)	{
	  this.stocks = s;
  }
  
  /**
   * Sets the trades for the user.
   * 
   * @param tradableUserData Trades
   */
  private void setTrades(ArrayList<TradableUserData> tradableUserData)	{
	  this.trades = tradableUserData;
  }
  
  /**
   * Sets the position for the user.
   * 
   * @param p Position
   */
  private void setPosition(Position p)	{
	  this.position = p;
  }

  /**
   * This method should call the user display manager's updateLastSale method,
   * passing the same 3 parameters that were passed in. Then, call the Position
   * object's updateLastSale method passing the product and price passed in.
   *
   * @param product String stock symbol
   * @param price Price of stock
   * @param volume Quantity of stock
 * @throws InvalidPriceOperation 
   */
  public void acceptLastSale(String product, Price price, int volume) throws InvalidPriceOperation {
    try {
      manager.updateLastSale(product, price, volume);
      position.updateLastSale(product, price);
    } catch(PositionException e) {
    	System.out.println(e.getMessage());
    } 
  }

  /**
   * This method will display the Fill Message in the market display and will
   * forward the data to the Position object.
   *
   * @param fm FillMessage passed as argument
   */
  public void acceptMessage(FillMessage fm) {
    try {
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      StringBuilder sb = new StringBuilder();
      sb.append(String.format("{%s} Fill Message: %s %s %s at %s", timestamp.toString(), fm.getSide(), fm.getVolume(), fm.getProduct(), fm.getPrice().toString()));
      sb.append(String.format(" %s [Tradable Id: %s]", fm.getDetails(), fm.getID()));
      String fillSummary = sb.toString();
      manager.updateMarketActivity(fillSummary);
      position.updatePosition(fm.getProduct(), fm.getPrice(), fm.getSide(), fm.getVolume());
  } catch (Exception e) {
      System.out.println(e.getMessage());
  	}
  }

  /**
   * This method will display the Cancel Message in the market display.
   *
   * @param cm CancelMessage passed as argument
   */
  public void acceptMessage(CancelMessage cm) {
    try {
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      StringBuilder sb = new StringBuilder();
      sb.append(String.format("{%s} Cancel Message: %s %s %s at %s", timestamp.toString(), cm.getSide(), cm.getVolume(), cm.getProduct(), cm.getPrice().toString()));
      sb.append(String.format(" %s [Tradable Id: %s]", cm.getDetails(), cm.getID()));
      String cancelSummary = sb.toString();
      manager.updateMarketActivity(cancelSummary);
  } catch (Exception e) {
      System.out.println("Caught error: " + e.getMessage());
  }
}

  /**
   * This method will display the Market Message in the market display.
   *
   * @param message String message being displayed
   */
  public void acceptMarketMessage(String message) {
    try {
      manager.updateMarketState(message);
    } catch(Exception e) {
        System.out.println("Caught error: " + e.getMessage());
    }
  }

  /**
   * This method will display the Ticker data in the market display.
   *
   * @param product String stock symbol sent to Ticker
   * @param price Price of stock sent to Ticker
   * @param direction Direction stock is moving
   */
  public void acceptTicker(String product, Price price, char direction) {
    try {
      manager.updateTicker(product, price, direction);
    } catch(Exception e) {
        System.out.println("Caught error: " + e.getMessage());
    }
  }

  /**
   * This method will display the Current Market data in the market display.
   *
   * @param product String stock symbol
   * @param bPrice Buy price
   * @param bVolume Quantity being bought
   * @param sPrice Sell price
   * @param sVolume Quantity being sold
   */
  public void acceptCurrentMarket(String product, Price bPrice, int bVolume,
          Price sPrice, int sVolume) {
    try {
      manager.updateMarketData(product, bPrice, bVolume, sPrice, sVolume);
    } catch(Exception e) {
        System.out.println("Caught error: " + e.getMessage());
    }
  }

  /**
   * This method will connect the user to the trading system.
   */
  public void connect() throws AlreadyConnectedException, UserNotConnectedException, 
  	InvalidConnectionIdException, UserCommandException {
	  connectionId = UserCommandService.getInstance().connect(this);
	  stocks = UserCommandService.getInstance().getProducts(userName, connectionId);
  }

  /**
   * This method will disconnect the user from the trading system.
   */
  public void disConnect() throws UserNotConnectedException, InvalidConnectionIdException,
          UserCommandException {
    UserCommandService.getInstance().disconnect(userName, connectionId);
  }

  /**
   * This method will activate the market display.
   * @throws UserNotConnectedException
   */
  public void showMarketDisplay() throws UserNotConnectedException, Exception {
	  if (stocks == null) {
		  throw new UserNotConnectedException("User currently not connected.");
	  } else	{
		  if (manager == null) {
			  manager = new UserDisplayManager(this);
		  }
		  manager.showMarketDisplay();
	  }
  }
    
  /**
   * This method forwards the new order request to the user command service and
   * saves the resulting order id.
   *
   * @param product String stock symbol
   * @param price Price of the Order
   * @param volume Quantity of the Order
   * @param side BookSide "side" of the placed Order
   * @return
   */
  public String submitOrder(String product, Price price, int volume,
		  BookSide side) throws TradableUserDataException,
          UserNotConnectedException, InvalidConnectionIdException,
          InvalidVolumeException, TradableException,
          InvalidMarketStateException, NoProductException,
          InvalidMessageException, ProductBookSideException,
          ProductBookException, ProductServiceException,
          TradeProcessorPriceTimeImplException, MessagePublisherException,
          UserCommandException, InvalidPriceOperation {
    String id = UserCommandService.getInstance().submitOrder(userName, connectionId, product, price, volume, side);
    trades.add(new TradableUserData(userName, product, side, id));
    return id;
  }

  /**
   * This method forwards the order cancel request to the user command service.
   *
   * @param product String stock symbol
   * @param side BookSide "side" of the cancelled Order
   * @param orderId Order id of the cancelled Order
   */
  public void submitOrderCancel(String product, BookSide side,
		  String orderId) throws UserNotConnectedException,
          InvalidConnectionIdException, InvalidMarketStateException,
          NoProductException, InvalidMessageException,
          OrderNotFoundException, InvalidVolumeException,
          ProductBookSideException, ProductServiceException,
          ProductBookException, MessagePublisherException,
          UserCommandException, InvalidPriceOperation, TradableException {
    UserCommandService.getInstance().submitOrderCancel(userName, connectionId,
            product, side, orderId);
  }

  /**
   * This method forwards the new quote request to the user command service
   *
   * @param product String stock symbol
   * @param buyPrice Buy Price
   * @param buyVolume Quantity being bought
   * @param sellPrice Sell Price
   * @param sellVolume Quantity being sold
   */
  public void submitQuote(String product, Price buyPrice,
		  int buyVolume, Price sellPrice, int sellVolume)
          throws UserNotConnectedException, InvalidConnectionIdException,
          InvalidVolumeException, TradableException,
          InvalidMarketStateException, NoProductException,
          DataValidationException, InvalidMessageException,
          ProductBookSideException, ProductBookException,
          ProductServiceException, TradeProcessorPriceTimeImplException,
          MessagePublisherException, UserCommandException, InvalidPriceOperation {
    UserCommandService.getInstance().submitQuote(userName, connectionId,
            product, buyPrice, buyVolume, sellPrice, sellVolume);
  }

  /**
   * This method forwards the quote cancel request to the user command service.
   *
   * @param product String stock symbol
   */
  public void submitQuoteCancel(String product)
		  throws UserNotConnectedException, InvalidConnectionIdException,
          InvalidMarketStateException, NoProductException,
          InvalidMessageException, ProductBookSideException,
          ProductBookException, ProductServiceException,
          InvalidVolumeException, MessagePublisherException,
          UserCommandException, InvalidPriceOperation {
    UserCommandService.getInstance().submitQuoteCancel(userName, connectionId,
            product);
  }

  /**
   * This method forwards the current market subscription to the user command
   * service.
   *
   * @param product String stock symbol
   */
  public void subscribeCurrentMarket(String product)
          throws UserNotConnectedException, InvalidConnectionIdException,
          MessagePublisherException, UserCommandException {
    UserCommandService.getInstance().subscribeCurrentMarket(userName,
            connectionId, product);
  }

  /**
   * This method forwards the last sale subscription to the user command
   * service.
   *
   * @param product String stock symbol
   */
  public void subscribeLastSale(String product)
          throws UserNotConnectedException, InvalidConnectionIdException,
          MessagePublisherException, UserCommandException {
    UserCommandService.getInstance().subscribeLastSale(userName, connectionId,
            product);
  }

  /**
   * This method forwards the message subscription to the user command service.
   *
   * @param product String stock symbol
   */
  public void subscribeMessages(String product)
          throws UserNotConnectedException, InvalidConnectionIdException,
          MessagePublisherException, UserCommandException {
    UserCommandService.getInstance().subscribeMessages(userName, connectionId,
            product);
  }

  /**
   * This method forwards the ticker subscription to the user command service.
   *
   * @param product String stock symbol
   */
  public void subscribeTicker(String product)
          throws UserNotConnectedException, InvalidConnectionIdException,
          MessagePublisherException, UserCommandException {
    UserCommandService.getInstance().subscribeTicker(userName, connectionId,
            product);
  }

  /**
   * Returns the value of the all Sock the User owns (has bought but not sold).
   *
   * @return the price of all stock the User owns
   */
  public Price getAllStockValue() throws InvalidPriceOperation, PositionException, PriceException {
    return position.getAllStockValue();
  }

  /**
   * Returns the difference between cost of all stock purchases and stock sales.
   *
   * @return the difference b/t cost of all stock purchases vs. stock sales
   */
  public Price getAccountCosts() {
    return position.getAccountCosts();
  }

  /**
   * Returns the difference between current value of all stocks owned and the
   * account costs.
   *
   * @return the value of the account
   */
  public Price getNetAccountValue() throws PositionException, InvalidPriceOperation, PriceException {
    return position.getNetAccountValue();
  }

  /**
   * Allows the User object to submit a Book Depth request for the specified
   * stock.
   *
   * @param product String stock symbol
   * @return the book depth of the specified stock
   */
  public String[][] getBookDepth(String product)
          throws UserNotConnectedException, InvalidConnectionIdException,
          NoProductException, ProductServiceException,
          UserCommandException {
    return UserCommandService.getInstance().getBookDepth(userName, connectionId,
            product);
  }

  /**
   * Allows the User object to query the market state (OPEN, PREOPEN, CLOSED).
   *
   * @return the market state
   */
  public String getMarketState()
          throws UserNotConnectedException, InvalidConnectionIdException,
          UserCommandException {
    return UserCommandService.getInstance().getMarketState(userName,
            connectionId);
  }

  /**
   * Returns a list of order id’s (a data member) for the orders this user has
   * submitted.
   *
   * @return a list of order id's this user has made
   */
  public ArrayList<TradableUserData> getOrderIds() {
    return trades;
  }

  /**
   * Returns a list of stocks (a data member) available in the trading system.
   *
   * @return a list of all products available in the system
   */
  public ArrayList<String> getProductList() {
    return stocks;
  }

  /**
   * Returns the value of the specified stock that this user owns.
   *
   * @param sym
   * @return the value of the specified stock this user owns
   */
  public Price getStockPositionValue(String sym)
          throws PositionException, InvalidPriceOperation {
    return position.getStockPositionValue(sym);
  }

  /**
   * Returns the volume of the specified stock that this user owns.
   *
   * @param product
   * @return the volume of the specified stock this user owns
   */
  public int getStockPositionVolume(String product)
          throws PositionException{
    return position.getStockPositionVolume(product);
  }

  /**
   * Returns a list of all the Stocks the user owns.
   *
   * @return a list of all stocks the user owns
   */
  public ArrayList<String> getHoldings() {
    return position.getHoldings();
  }

  /**
   * Gets a list of DTO’s containing information on all Orders for this user for
   * the specified product with remaining volume.
   *
   * @param product
   * @return a list of DTO’s containing information on all Orders for this user
   */
  public ArrayList<TradableDTO> getOrdersWithRemainingQty(String product)
          throws UserNotConnectedException, InvalidConnectionIdException,
          ProductBookSideException, ProductBookException,
          ProductServiceException, UserCommandException {
    return UserCommandService.getInstance().getOrdersWithRemainingQty(userName,
            connectionId, product);
  }

  /**
   * Validates a String input.
   * 
   * @param o String passed in argument
   * @throws UserException
   */
  private void validateInput(String o)
          throws UserException {
    if (o == null || o.isEmpty()) {
      throw new UserException("Argument must be of type String and"
        + " cannot be null or empty.");
    }
  }
}
