package publishers;

import client.User;
import price.Price;
import price.exceptions.InvalidPriceOperation;
import publishers.exceptions.MessagePublisherException;

/**
 * This publisher notifies the user of the last sale of a stock symbol that user
 * is subscribed to. (Stock Symbol, Price, Volume)
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class LastSalePublisher implements DistinctLastSalePublisher {

  private volatile static LastSalePublisher instance;
  private DistinctLastSalePublisher messagePublisherTopicImpl;
  private LastSalePublisher()	{}

 /**
  * Making the Singleton multi-thread safe
  * @return new instance of CurrentMarketPublisher
  */
 public static LastSalePublisher getInstance() {
    if (instance == null) {
      synchronized (LastSalePublisher.class) {
        if (instance == null) {
          instance = MessagePublisherTopicFactory.createLastSalePublisher();
        }
      }
    }
    return instance;
  }

 /**
  * Set's the LastSalePublisher to the passed in MessagePublisherTopic
  * @param impl MessagePublisherTopic that the LastSalePublisher is set to
  */
 LastSalePublisher(DistinctLastSalePublisher impl) {
    messagePublisherTopicImpl = impl;
  }

 /**
  * Subscribes users
  * @param userName The user requesting the subscription
  * @param product The product they are wishing to subscribe to
  */
  public synchronized void subscribe(User userName, String product) throws MessagePublisherException {
    messagePublisherTopicImpl.subscribe(userName, product);
  }

  /**
   * Unsubscribes users
   * @param userName The user requesting the unsubscription
   * @param product The product they are wishing to unsubscribe from
   */
  public synchronized void unSubscribe(User userName, String product) throws MessagePublisherException {
    messagePublisherTopicImpl.unSubscribe(userName, product);
  }

  /**
   * Method that components of the trading system call to send the last sale out
   * @param product Product with requested last sale
   * @param p Price of the last sale
   * @param v Volume of the last sale
 * @throws InvalidPriceOperation 
   */

  public synchronized void publishLastSale(String product, Price p, int v) throws MessagePublisherException, InvalidPriceOperation {
    messagePublisherTopicImpl.publishLastSale(product, p, v);
  }
}