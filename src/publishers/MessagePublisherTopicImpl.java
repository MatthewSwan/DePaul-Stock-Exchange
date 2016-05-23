package publishers;

import client.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import price.Price;
import price.exceptions.InvalidPriceOperation;
import publishers.exceptions.MessagePublisherException;
import messages.CancelMessage;
import messages.FillMessage;
import messages.MarketDataDTO;
import messages.MarketMessage;

/**
 * The implementation of the MessagePublisher interface that publishers will use
 * to subscribe and un-subscribe users from.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
class MessagePublisherTopicImpl implements DistinctCurrentMarketPublisher,
  DistinctLastSalePublisher, DistinctTickerPublisher, DistinctMessagePublisher {

  /**
   * A hash map of of keys representing stock symbols and a HashSet of users
   * subscribed to those symbols for stock market updates.
   */
  private Map<String, Set<User>> subscribers;

  /**
   * A hash map of the most recent ticker price for a stock.
   */
  private Map<String, Price> tickerVal;

  MessagePublisherTopicImpl() {
    subscribers = new HashMap<>();
    tickerVal = new HashMap<>();
  }

  /**
   * Subscribe users to product.
   * @param userName Name of the user
   * @param product Stock being represented
   * @throws MessagePublisherException
   */
  public synchronized final void subscribe(User userName, String product) throws MessagePublisherException {
    createUserSetForProduct(product);
    Set<User> set = subscribers.get(product);
    if (set.contains(userName)) {
      throw new MessagePublisherException("The users is already subscribed to receive updates for this stock symbol: " + product);
    }
    set.add(userName);
  }

  /**
   * Unsubscribe users from the product.
   * @ throws MessagePublisherException
   * @param userName Name of the user
   * @param product Stock being represented
   */
  public synchronized final void unSubscribe(User userName, String product) throws MessagePublisherException {
    Set<User> set = subscribers.get(product);
    if (set == null) {
      throw new MessagePublisherException("No one is registered for this stock symbol: " + product);
    }
    if (!set.contains(userName)) {
      throw new MessagePublisherException("The user is not subscribed to receive updates for this stock symbol: " + product);
    }
    set.remove(userName);
  }

  /**
   * Creates the user set for product
   * @param product Stock being represented
   */
  private synchronized void createUserSetForProduct(String product) throws MessagePublisherException {
    if (!subscribers.containsKey(product)) {
      subscribers.put(product, new HashSet<User>());
    }
  }

  /**
   * Publish the current market price
   * @param m MarketDataDTO object
   */
  public synchronized void publishCurrentMarket(MarketDataDTO m) throws MessagePublisherException {
    if (!subscribers.containsKey(m.product)) { 
    	return; 
    	}
    Set<User> users = subscribers.get(m.product);
    for (User userName : users) {
      userName.acceptCurrentMarket(m.product, m.buyPrice, m.buyVolume, m.sellPrice, m.sellVolume);
    }
  }

  /**
   * Publish the last sale
   * @param product Stock being represented
   * @param p Price of the stock
   * @param v quantity of the stock
 * @throws InvalidPriceOperation 
   */
  public synchronized void publishLastSale(String product, Price p, int v) throws MessagePublisherException, InvalidPriceOperation {
    if (!subscribers.containsKey(product)) { 
    	return; 
    	}
    Set<User> users = subscribers.get(product);
    for (User userName : users) {
      userName.acceptLastSale(product, p, v);
    }
    TickerPublisher.getInstance().publishTicker(product, p);
  }

  /**
   * Publish the Ticker. It also determines the direction of the stock.
   * @param product Stock being represented
   * @param p Price of the stock
   * @return direction Up arrow, down arrow or equal sign
   */
  public synchronized void publishTicker(String product, Price p) throws MessagePublisherException {
    if (!subscribers.containsKey(product)) { 
    	return; 
    	}
    char direction = ' ';
    Price val = tickerVal.get(product);
    if (val != null) {
      if (p.equals(val)) {
        direction = '=';
      } else if (p.greaterThan(val)) {
        direction = '\u2191';
      } else if (p.lessThan(val)) {
        direction = '\u2193';
      }
    }
    tickerVal.put(product, p);
    Set<User> users = subscribers.get(product);
    for (User userName : users) {
      userName.acceptTicker(product, p, direction);
    }
  }

  /**
   * Publish cancel messages
   * @param cm CancelMessage object
   */
  public synchronized void publishCancel(CancelMessage cm) throws MessagePublisherException {
    String p = cm.getProduct();
    if (!subscribers.containsKey(p)) { 
    	return; 
    	}
    for (User userName : subscribers.get(p)) {
      if (userName.getUserName().equals(cm.getUser())) {
        userName.acceptMessage(cm);
      }
    }
  }

  /**
   * Holds the object for FillMessage
   * @param fm FillMessage object
   */
  public synchronized void publishFill(FillMessage fm) throws MessagePublisherException {
    String p = fm.getProduct();
    if (!subscribers.containsKey(p)) { return; }
    for (User userName : subscribers.get(p)) {
      if (userName.getUserName().equals(fm.getUser())) {
        userName.acceptMessage(fm);
      }
    }
  }

  /**
   * Publish the market message to all the users
   * @param mm MarketMessage object
   */
  public synchronized void publishMarketMessage(MarketMessage mm) throws MessagePublisherException {
    HashSet<User> everyUser = new HashSet<>();
    for (Set<User> users : subscribers.values()) {
      for (User userName : users) {
        if (!everyUser.contains(userName)) {
          everyUser.add(userName);
        }
      }
    }
    for (User userName : everyUser) {
      userName.acceptMarketMessage(mm.getState().toString());
    }
  } 
}