package messages;

import constants.global.MarketState;
import messages.exceptions.InvalidMessageException;

/**
 * The MarketMessage class encapsulates data related to the state of the market.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class MarketMessage implements StateOfMarket {

  private StateOfMarket marketMessageImpl;

  /**
   * Creates a new Market Message
   * @param state creates a Market Message and set's it to the state passed in
   * @throws InvalidMessageException
   */
  public MarketMessage(MarketState state) throws InvalidMessageException {
    marketMessageImpl = MessageFactory.createMarketMessageImpl(state);
  }

  /**
   * @returns the state of the market
   */
  public MarketState getState() {
    return marketMessageImpl.getState();
  }
}