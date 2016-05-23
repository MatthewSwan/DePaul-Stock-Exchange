package publishers;

import client.User;
import publishers.exceptions.MessagePublisherException;
import messages.CancelMessage;
import messages.FillMessage;
import messages.MarketMessage;

/**
 * Implements the cancel message, fill message, and market message publishers
 * used by registered users of the Stock Market.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class MessagePublisher implements DistinctMessagePublisher {

  private volatile static MessagePublisher instance;
  private DistinctMessagePublisher messagePublisherTopicImpl;
  private MessagePublisher()	{}
  
  /**
   * Making the Singleton multi-thread safe
   * @return new instance of MessagePublisher
   */
  public static MessagePublisher getInstance() {
    if (instance == null) {
      synchronized (MessagePublisher.class) {
        if (instance == null) {
          instance = MessagePublisherTopicFactory
                  .createMessagePublisher();
        }
      }
    }
    return instance;
  }

  /**
   * Set's the MessagePublisher to the passed in MessagePublisherTopic
   * @param impl MessagePublisherTopic that the MessagePublisher is set to
   */
  MessagePublisher(DistinctMessagePublisher impl) {
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
   * Method that publishes a cancel message
   * @param cm Cancel message
   */
  public synchronized void publishCancel(CancelMessage cm) throws MessagePublisherException {
    messagePublisherTopicImpl.publishCancel(cm);
  }

  /**
   * Method that publishes a fill message
   * @param fm Fill message
   */
  public synchronized void publishFill(FillMessage fm) throws MessagePublisherException {
    messagePublisherTopicImpl.publishFill(fm);
  }

  /**
   * Method that publishes a market message
   * @param mm Market message
   */
  public synchronized void publishMarketMessage(MarketMessage mm) throws MessagePublisherException {
    messagePublisherTopicImpl.publishMarketMessage(mm);
  }
}