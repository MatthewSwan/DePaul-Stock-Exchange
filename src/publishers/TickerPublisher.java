package publishers;

import client.User;
import price.Price;
import publishers.exceptions.MessagePublisherException;

/**
 * Implements the "Singleton" design pattern, as we only want to have a single instance of the 
 * TickerPublisher. Notifies the user of the last sale of a stock symbol that user
 * is subscribed to. (Stock Symbol, Price)
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class TickerPublisher implements DistinctTickerPublisher {

  private volatile static TickerPublisher instance;
  private DistinctTickerPublisher messagePublisherSubjectImpl;
  private TickerPublisher()	{}

  /**
   * Using the "Singleton" design pattern to get the instance of TickerPublisher
   * @return instance A TickerPublisher object
   */
  public static TickerPublisher getInstance() {
    if (instance == null) {
      synchronized (TickerPublisher.class) {
        if (instance == null) {
          instance = MessagePublisherTopicFactory.createTickerPublisher();
        }
      }
    }
    return instance;
  }

  /**
   * Protected TickerPublisher method
   * @param impl DistinctTickerPublisher object passed in as argument
   */
  TickerPublisher(DistinctTickerPublisher impl) {
    messagePublisherSubjectImpl = impl;
  }

  /**
   * Subscribe users to products
   * @param userName User Person requesting to subscribe to stock
   * @param String product Stock being subscribed to
   * @throws MessagePublisherException
   */
  public synchronized void subscribe(User userName, String product) throws MessagePublisherException {
    messagePublisherSubjectImpl.subscribe(userName, product);
  }

  /**
   * Unsubscribe users from products
   * @param userName Person requesting to unsubscribe from the stock
   * @param String product Stock being unsubscribed from
   * @throws MessagePublisherException
   */
  public synchronized void unSubscribe(User userName, String product) throws MessagePublisherException {
    messagePublisherSubjectImpl.unSubscribe(userName, product);
  }

  /**
   * This is the publishTicker
   * @param product Stock being represented
   * @param p Price of the stock
   */
  public synchronized void publishTicker(String product, Price p) throws MessagePublisherException {
    messagePublisherSubjectImpl.publishTicker(product, p);
  }
}