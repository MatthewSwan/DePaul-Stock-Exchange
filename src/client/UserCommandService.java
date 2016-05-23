package client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import messages.exceptions.InvalidMessageException;
import constants.global.BookSide;
import price.Price;
import price.exceptions.InvalidPriceOperation;
import publishers.CurrentMarketPublisher;
import publishers.LastSalePublisher;
import publishers.MessagePublisher;
import publishers.TickerPublisher;
import publishers.exceptions.MessagePublisherException;
import book.ProductService;
import book.exceptions.DataValidationException;
import book.exceptions.InvalidMarketStateException;
import book.exceptions.NoProductException;
import book.exceptions.OrderNotFoundException;
import book.exceptions.ProductBookException;
import book.exceptions.ProductBookSideException;
import book.exceptions.ProductServiceException;
import book.exceptions.TradeProcessorPriceTimeImplException;
import tradable.Order;
import tradable.Quote;
import tradable.TradableDTO;
import tradable.exceptions.InvalidVolumeException;
import tradable.exceptions.TradableException;
import client.exceptions.AlreadyConnectedException;
import client.exceptions.InvalidConnectionIdException;
import client.exceptions.UserCommandException;
import client.exceptions.UserNotConnectedException;



/**
 * The UserCommand class (for the "client" package) acts as a façade between a
 * user and the trading system. This class should be a Singleton, as there is
 * only one User Command Service that all users will work with.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class UserCommandService {

  private volatile static UserCommandService instance;

  /**
   * A HashMap<String, Long> to hold user name and connection id pairs.
   */
  private HashMap<String, Long> connectedUserIds = new HashMap<>();

  /**
   * A HashMap<String, User> to hold user name and user object pairs.
   */
  private HashMap<String, User> connectedUsers = new HashMap<>();

  /**
   * A HashMap<String, Long> to hold user name and connection-time pairs
   * (connection time is stored as a long).
   */
  private HashMap<String, Long> connectedTime = new HashMap<>();

  /**
   * Singleton
   */
  private UserCommandService() {}

  public static UserCommandService getInstance() {
    if (instance == null) {
      synchronized(UserCommandService.class) {
        if (instance == null) {
          instance = new UserCommandService();
        }
      }
    }
    return instance;
  }

  /**
   * This is a utility method that will be used by many of the methods in this
   * class to verify the integrity of the user name and connection id passed in
   * with many of the method calls found here.
   *
   * @param userName String user to be verified
   * @param connId Connection Id for the passed in user
   * @throws UserNotConnectedException
   * @throws InvalidConnectionIdExceptions
   */
  private void verifyUser(String userName, long connId)
          throws UserNotConnectedException, InvalidConnectionIdException,
          UserCommandException {
    validateInput(userName);
    if (!connectedUserIds.containsKey(userName)) {
      throw new UserNotConnectedException("User not connected to the system");
    }
    if (!connectedUserIds.get(userName).equals((Long) connId)) {
      throw new InvalidConnectionIdException("Connection ID is not valid");
    }
  }

  /**
   * This method will connect the user to the trading system.
   *
   * @param user User object passed in to be connected
   * @return the connectedUserId
   * @throws AlreadyConnectedException
   */
  public synchronized long connect(User user) throws AlreadyConnectedException,
          UserCommandException {
    validateInput(user);
    if (connectedUserIds.containsKey(user.getUserName())) {
      throw new AlreadyConnectedException("User already connected to the"
              + " system.");
    }
    connectedUserIds.put(user.getUserName(), System.nanoTime());
    connectedUsers.put(user.getUserName(), user);
    connectedTime.put(user.getUserName(), System.currentTimeMillis());
    return connectedUserIds.get(user.getUserName());
  }

  /**
   * This method will disconnect the user from the trading system.
   *
   * @param userName String user to be disconnected
   * @param connId Connection id of the user being disconnected
   * @throws UserNotConnectedException
   * @throws InvalidConnectionIdException
   */
  public synchronized void disconnect(String userName, long connId)
          throws UserNotConnectedException, InvalidConnectionIdException,
          UserCommandException {
    verifyUser(userName, connId);
    connectedUserIds.remove(userName);
    connectedUsers.remove(userName);
    connectedTime.remove(userName);
  }

  /**
   * Forwards the call of "getBookDepth" to the ProductService.
   *
   * @param userName String user passed in as argument
   * @param connId Connection Id of the user getting the book depth
   * @param product String stock symbol the appropriate product
   * @return the book depth for the specified stock
   * @throws UserNotConnectedException
   * @throws InvalidConnectionIdException
   * @throws NoSuchProductException
   * @throws ProductServiceException
   */
  public String[][] getBookDepth(String userName, long connId, String product)
          throws UserNotConnectedException, InvalidConnectionIdException,
          NoProductException, ProductServiceException,
          UserCommandException {
    validateInput(product);
    verifyUser(userName, connId);
    return ProductService.getInstance().getBookDepth(product);
  }

  /**
   * Forwards the call of "getMarketState" to the ProductService.
   *
   * @param userName String user name of the passed in argument
   * @param connId Connection Id for the user
   * @return the current market state
   * @throws UserNotConnectedException
   * @throws InvalidConnectionIdException
   */
  public String getMarketState(String userName, long connId)
          throws UserNotConnectedException, InvalidConnectionIdException,
          UserCommandException {
    verifyUser(userName, connId);
    return ProductService.getInstance().getMarketState().toString();
  }

  /**
   * Forwards the call of "getOrdersWithRemainingQty" to the ProductService.
   *
   * @param userName String user name being looked up
   * @param connId Connection Id of the user
   * @param product String stock symbol being looked up
   * @return Information on all Orders with remaining volume for specified product
   * @throws UserNotConnectedException
   * @throws InvalidConnectionIdException
   * @throws ProductBookSideException
   * @throws ProductBookException
   * @throws ProductServiceException
   */
  public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(
          String userName, long connId, String product)
          throws UserNotConnectedException, InvalidConnectionIdException,
          ProductBookSideException, ProductBookException,
          ProductServiceException, UserCommandException {
    validateInput(product);
    verifyUser(userName, connId);
    return ProductService.getInstance().getOrdersWithRemainingQty(userName,
            product);
  }

  /**
   * This method should return a sorted list of the available stocks on this
   * system, received from the ProductService.
   *
   * @param userName String user name being looked up
   * @param connId Connection Id of the user
   * @return A list of stock in the trading system.
   * @throws UserNotConnectedException
   * @throws InvalidConnectionIdException
   */
  public ArrayList<String> getProducts(String userName, long connId)
          throws UserNotConnectedException, InvalidConnectionIdException,
          UserCommandException {
    verifyUser(userName, connId);
    ArrayList<String> list = ProductService.getInstance().getProductList();
    Collections.sort(list);
    return list;
  }

  /**
   * This method will create an order object using the data passed in, and will
   * forward the order to the ProductService's "submitOrder" method.
   *
   * @param userName String user name who is submitting the Order
   * @param connId Connection Id of the user
   * @param product String stock symbol passed in for the specified product
   * @param price Price at the which the product is being ordered at
   * @param volume Quantity of the Order
   * @param side BookSide of the Order
   * @return A new Order request
   * @throws UserNotConnectedException
   * @throws InvalidConnectionIdException
   * @throws InvalidVolumeException
   * @throws TradeableException
   * @throws InvalidMarketStateException
   * @throws NoSuchProductException
   * @throws InvalidMessageException
   * @throws ProductBookSideException
   * @throws ProductBookException
   * @throws ProductServiceException
   * @throws TradeProcessorPriceTimeImplException
   */
  public String submitOrder(String userName, long connId, String product,
          Price price, int volume, BookSide side)
          throws UserNotConnectedException, InvalidConnectionIdException,
          InvalidVolumeException, TradableException,
          InvalidMarketStateException, NoProductException,
          InvalidMessageException, ProductBookSideException,
          ProductBookException, ProductServiceException,
          TradeProcessorPriceTimeImplException, MessagePublisherException,
          UserCommandException, InvalidPriceOperation {
    validateInput(product);
    validateInput(price);
    validateInput(side);
    verifyUser(userName, connId);
    Order newOrder = new Order(userName, product, price, volume, side);
    return ProductService.getInstance().submitOrder(newOrder);
  }

  /**
   * This method will forward the provided information to the ProductService's
   * "submitOrderCancel" method.
   *
   * @param userName String user name who is canceling the Order
   * @param connId Connection Id of the user
   * @param product String stock symbol of the specified product
   * @param side BookSide of the specified cancel Order
   * @param orderId String order id passed in
   * @throws UserNotConnectedException
   * @throws InvalidConnectionIdException
   * @throws InvalidMarketStateException
   * @throws NoSuchProductException
   * @throws InvalidMessageException
   * @throws OrderNotFoundException
   * @throws InvalidVolumeException
   * @throws ProductBookSideException
   * @throws ProductServiceException
   * @throws ProductBookException
   */
  public void submitOrderCancel(String userName, long connId, String product,
          BookSide side, String orderId) throws UserNotConnectedException,
          InvalidConnectionIdException, InvalidMarketStateException,
          NoProductException, InvalidMessageException,
          OrderNotFoundException, InvalidVolumeException,
          ProductBookSideException, ProductServiceException,
          ProductBookException, MessagePublisherException, UserCommandException,
  		  TradableException, InvalidPriceOperation	{
    validateInput(product);
    validateInput(side);
    validateInput(orderId);
    verifyUser(userName, connId);
    ProductService.getInstance().submitOrderCancel(product, side, orderId);
  }

  /**
   * This method will create a quote object using the data passed in, and will
   * forward the quote to the ProductService's "submitQuote" method.
   *
   * @param userName String user name of user submitting the Quote
   * @param connId Connection Id of the user
   * @param product String stock symbol of the specified product
   * @param bPrice Buy price of the Quote
   * @param bVolume Quantity of the product in the Quote
   * @param sPrice Sell price of the Quote
   * @param sVolume Quantity of the product in the Quote
   * @throws UserNotConnectedException
   * @throws InvalidConnectionIdException
   * @throws InvalidVolumeException
   * @throws TradeableException
   * @throws InvalidMarketStateException
   * @throws NoSuchProductException
   * @throws DataValidationException
   * @throws InvalidMessageException
   * @throws ProductBookSideException
   * @throws ProductBookException
   * @throws ProductServiceException
   * @throws TradeProcessorPriceTimeImplException
   */
  public void submitQuote(String userName, long connId, String product,
          Price bPrice, int bVolume, Price sPrice, int sVolume)
          throws UserNotConnectedException, InvalidConnectionIdException,
          InvalidVolumeException, TradableException,
          InvalidMarketStateException, NoProductException,
          DataValidationException, InvalidMessageException,
          ProductBookSideException, ProductBookException,
          ProductServiceException, TradeProcessorPriceTimeImplException,
          MessagePublisherException, UserCommandException, InvalidPriceOperation	{
    validateInput(product);
    validateInput(bPrice);
    validateInput(sPrice);
    verifyUser(userName, connId);
    Quote q = new Quote(userName, product, bPrice, bVolume, sPrice, sVolume);
    ProductService.getInstance().submitQuote(q);
  }

  /**
   * This method will forward the provided data to the ProductService's
   * "submitQuoteCancel" method.
   *
   * @param userName String user name of the user submitting the Quote cancel
   * @param connId Connection Id of the user
   * @param product String stock symbol for the specified Quote cancel
   * @throws UserNotConnectedException
   * @throws InvalidConnectionIdException
   * @throws InvalidMarketStateException
   * @throws NoSuchProductException
   * @throws InvalidMessageException
   * @throws ProductBookSideException
   * @throws ProductBookException
   * @throws ProductServiceException
   */
  public void submitQuoteCancel(String userName, long connId, String product)
          throws UserNotConnectedException, InvalidConnectionIdException,
          InvalidMarketStateException, NoProductException,
          InvalidMessageException, ProductBookSideException,
          ProductBookException, ProductServiceException,
          InvalidVolumeException, MessagePublisherException,
          UserCommandException, InvalidPriceOperation {
    validateInput(product);
    verifyUser(userName, connId);
    ProductService.getInstance().submitQuoteCancel(userName, product);
  }

  /**
   * This method will forward the subscription request to the
   * CurrentMarketPublisher.
   *
   * @param userName String user name of who to subscribe
   * @param connId Connection Id of the user
   * @param product String stock symbol of which to subscribe the user to
   * @throws UserNotConnectedException
   * @throws InvalidConnectionIdException
   * @throws MessagePublisherException
   */
  public void subscribeCurrentMarket(String userName, long connId,
          String product) throws UserNotConnectedException,
          InvalidConnectionIdException, MessagePublisherException,
          UserCommandException {
    validateInput(product);
    verifyUser(userName, connId);
    CurrentMarketPublisher.getInstance().subscribe(connectedUsers.get(userName),
            product);
  }

  /**
   * This method will forward the subscription request to the LastSalePublisher.
   *
   * @param userName String user name of who to subscribe
   * @param connId Connection Id of the user
   * @param product String stock symbol of which to subscribe the user
   * @throws UserNotConnectedException
   * @throws InvalidConnectionIdException
   * @throws MessagePublisherException
   */
  public void subscribeLastSale(String userName, long connId, String product)
          throws UserNotConnectedException, InvalidConnectionIdException,
          MessagePublisherException, UserCommandException {
    validateInput(product);
    verifyUser(userName, connId);
    LastSalePublisher.getInstance().subscribe(connectedUsers.get(userName),
            product);
  }

  /**
   * This method will forward the subscription request to the MessagePublisher.
   *
   * @param userName String user name of who to subscribe
   * @param connId Connection Id of the user
   * @param product String stock symbol of which to subscribe the user
   * @throws UserNotConnectedException
   * @throws InvalidConnectionIdException
   * @throws MessagePublisherException
   */
  public void subscribeMessages(String userName, long connId, String product)
          throws UserNotConnectedException, InvalidConnectionIdException,
          MessagePublisherException, UserCommandException {
    validateInput(product);
    verifyUser(userName, connId);
    MessagePublisher.getInstance().subscribe(connectedUsers.get(userName),
            product);
  }

  /**
   * This method will forward the subscription request to the TickerPublisher.
   *
   * @param userName String user name of who to subscribe
   * @param connId Connection Id of the user
   * @param product String stock symbol of which to subscribe the user
   * @throws UserNotConnectedException
   * @throws InvalidConnectionIdException
   * @throws MessagePublisherException
   */
  public void subscribeTicker(String userName, long connId, String product)
          throws UserNotConnectedException, InvalidConnectionIdException,
          MessagePublisherException, UserCommandException {
    validateInput(product);
    verifyUser(userName, connId);
    TickerPublisher.getInstance().subscribe(connectedUsers.get(userName),
            product);
  }

  /**
   * This method will forward the un-subscribe request to the
   * CurrentMarketPublisher.
   *
   * @param userName String user name  of who to unsubsribe
   * @param connId Connection Id of the user
   * @param product String stock symbol of which to unsubscribe the user
   * @throws UserNotConnectedException
   * @throws InvalidConnectionIdException
   * @throws MessagePublisherException
   */
  public void unSubscribeCurrentMarket(String userName, long connId,
          String product) throws UserNotConnectedException,
          InvalidConnectionIdException, MessagePublisherException,
          UserCommandException {
    validateInput(product);
    verifyUser(userName, connId);
    CurrentMarketPublisher.getInstance().unSubscribe(connectedUsers.get(userName),
            product);
  }

  /**
   * This method will forward the un-subscribe request to the LastSalePublisher.
   *
   * @param userName string user name of who to unsubscribe
   * @param connId Connection Id of the user
   * @param product String stock symbol of which to unsubscribe the user
   * @throws UserNotConnectedException
   * @throws InvalidConnectionIdException
   * @throws MessagePublisherException
   */
  public void unSubscribeLastSale(String userName, long connId,
          String product) throws UserNotConnectedException,
          InvalidConnectionIdException, MessagePublisherException, UserCommandException {
    validateInput(product);
    verifyUser(userName, connId);
    LastSalePublisher.getInstance().unSubscribe(connectedUsers.get(userName),
            product);
  }

  /**
   * This method will forward the un-subscribe request to the TickerPublisher.
   *
   * @param userName String user name of who to unsubscribe
   * @param connId Connection Id of the user
   * @param product String stock symbol of which to unsubscribe the user
   * @throws UserNotConnectedException
   * @throws InvalidConnectionIdException
   * @throws MessagePublisherException
   */
  public void unSubscribeTicker(String userName, long connId, String product)
          throws UserNotConnectedException, InvalidConnectionIdException,
          MessagePublisherException, UserCommandException {
    validateInput(product);
    verifyUser(userName, connId);
    TickerPublisher.getInstance().unSubscribe(connectedUsers.get(userName),
            product);
  }

  /**
   * This method will forward the un-subscribe request to the MessagePublisher 
   * 
   * @param userName String user name of who to unsubscribe
   * @param connId Connection Id of the user
   * @param product String stock symbol of which to unsubscribe the user
   * @throws UserNotConnectedException
   * @throws InvalidConnectionIdException
   * @throws MessagePublisherException
   * @throws UserCommandException
   */
  public void unSubscribeMessages(String userName, long connId,
          String product) throws UserNotConnectedException,
          InvalidConnectionIdException, MessagePublisherException, UserCommandException {
    validateInput(product);
    verifyUser(userName, connId);
    MessagePublisher.getInstance().unSubscribe(connectedUsers.get(userName),
            product);
  }

  /**
   * Validates a String input
   * 
   * @param o String passed in as argument being validated
   * @throws UserCommandException
   */
  private void validateInput(String o)
          throws UserCommandException {
    if (o == null || o.isEmpty()) {
      throw new UserCommandException("Argument must be of type String and cannot be null or empty.");
    }
  }

  /**
   * Validates a Price input
   * 
   * @param o Price object passed in as argument being validated
   * @throws UserCommandException
   */
  private void validateInput(Price o) throws UserCommandException {
    if (o == null || !(o instanceof Price)) {
      throw new UserCommandException("Argument must be of type Price and"
              + " cannot be null.");
    }
  }

  /**
   * Validates a BookSide input
   * 
   * @param o "side" of the BookSide object
   * @throws UserCommandException
   */
  private void validateInput(BookSide o) throws UserCommandException {
    if (o == null || !(o instanceof BookSide)) {
      throw new UserCommandException("Argument must be of type BookSide and"
              + " cannot be null.");
    }
  }

  /**
   * Validates an Object input
   * 
   * @param o Object input as argument
   * @throws UserCommandException
   */
  private void validateInput(Object o)
          throws UserCommandException {
    if (o == null) {
      throw new UserCommandException("Argument must be of type User and"
        + " cannot be null.");
    }
  }
}