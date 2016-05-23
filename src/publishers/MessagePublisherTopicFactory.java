package publishers;

/**
 * A factory class used to create objects under our publisher package: MessagePublisherTopicImpl, 
 * CurrentMarketPublisher, LastSalePublisher, TickerPublisher, MessagePublisher
 * 
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
class MessagePublisherTopicFactory {

	/**
	 * Creates an instance of a CurrentMarketPublisherImpl
	 * @return MessagePublisherTopicImpl
	 */
	private synchronized static DistinctCurrentMarketPublisher createCurrentMarketPublisherImpl() {
    return new MessagePublisherTopicImpl();
  }

  /**
   * Creates a LastSlaePublisherSubjectImpl object
   * @return a LastSlaePublisherSubjectImpl
   */
  private synchronized static DistinctLastSalePublisher createLastSalePublisherTopicImpl() {
    return new MessagePublisherTopicImpl();
  }

  /**
   * Creates a TickerPublisherSubjectImpl object
   * @return a TickerPublisherSubjectImpl
   */
  private synchronized static DistinctTickerPublisher createTickerPublisherTopicImpl() {
    return new MessagePublisherTopicImpl();
  }

  /**
   * Creates a MessagePublisherSpecificSubjectImpl object
   * @return a  MessagePublisherSpecificSubjectImpl
   */
  private synchronized static DistinctMessagePublisher createDistinctMessagePublisherTopicImpl() {
    return new MessagePublisherTopicImpl();
  }

  /**
   * Creates a CurrentMarketPublisher object
   * @return a CurrentMarketPublisher
   */
  synchronized static CurrentMarketPublisher createCurrentMarketPublisher() {
    return new CurrentMarketPublisher(createCurrentMarketPublisherImpl());
  }

  /**
   * Creates a LastSalePublisher object
   * @return a LastSalePublisher
   */
  synchronized static LastSalePublisher createLastSalePublisher() {
    return new LastSalePublisher(createLastSalePublisherTopicImpl());
  }

  /**
   * Creates a TickerPublisher object
   * @return a TickerPublisher
   */
  synchronized static TickerPublisher createTickerPublisher() {
    return new TickerPublisher(createTickerPublisherTopicImpl());
  }

  /**
   * Creates a MessagePublisher object
   * @return a MessagePublisher
   */
  synchronized static MessagePublisher createMessagePublisher() {
    return new MessagePublisher(createDistinctMessagePublisherTopicImpl());
  }
}