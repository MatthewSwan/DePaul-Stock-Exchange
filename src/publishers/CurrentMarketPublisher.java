package publishers;

import client.User;
import publishers.exceptions.MessagePublisherException;
import messages.MarketDataDTO;

/**
 * This publisher notifies all users of current market conditions in the stock
 * exchange.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class CurrentMarketPublisher implements DistinctCurrentMarketPublisher {

  private volatile static CurrentMarketPublisher instance;
  private DistinctCurrentMarketPublisher messagePublisherTopicImpl;
  private CurrentMarketPublisher()	{}

  /**
   * Making the Singleton multi-thread safe
   * @return new instance of CurrentMarketPublisher
   */
  public static CurrentMarketPublisher getInstance() {
    if (instance == null) {
      synchronized (CurrentMarketPublisher.class) {
        if (instance == null) {
          instance = MessagePublisherTopicFactory.createCurrentMarketPublisher();
        }
      }
    }
    return instance;
  }
  
  /**
   * Set's the CurrentMarketPublisher to the passed in MessagePublisherTopic
   * @param impl MessagePublisherTopic that the CurrentMarketPublisher is set to
   */
  CurrentMarketPublisher(DistinctCurrentMarketPublisher impl) {
    messagePublisherTopicImpl = impl;
  }

  /**
   * Subscribes users
   * @param u The user requesting the subscription
   * @param product The product they are wishing to subscribe to
   */
  public synchronized void subscribe(User u, String product) throws MessagePublisherException {
    messagePublisherTopicImpl.subscribe(u, product);
  }

  /**
   * Unsubscribes users
   * @param u The user requesting the unsubscription
   * @param product The product they are wishing to unsubscribe from
   */
  public synchronized void unSubscribe(User u, String product) throws MessagePublisherException {
    messagePublisherTopicImpl.unSubscribe(u, product);
  }

  /**
   * Method that components of the trading system call to send the market updates out
   * @param m MarketDataDTO object passed as argument 
   */
  public synchronized void publishCurrentMarket(MarketDataDTO m)
          throws MessagePublisherException {
    messagePublisherTopicImpl.publishCurrentMarket(m);
  }
}