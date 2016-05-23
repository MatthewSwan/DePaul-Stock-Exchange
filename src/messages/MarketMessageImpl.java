package messages;

import constants.global.MarketState;
import messages.exceptions.InvalidMessageException;

/**
 * The implementation object for a Market Message.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class MarketMessageImpl implements StateOfMarket {

  /**
   * Holds the state of the Market (either CLOSED, OPEN, PREOPEN).
   */
  private MarketState state;

  /**
   * Set's the state of the market to the MarketState being passed in
   * @param state the market is being set to by the passed in MarketState
   * @throws InvalidMessageException
   */
  public MarketMessageImpl(MarketState state) throws InvalidMessageException {
    setState(state);
  }

  /**
   * Set's the state of the market to the MarketState passed in
   * @param state the market is being set to by the passed in MarketState
   * @throws InvalidMessageException
   */
  private void setState(MarketState state) throws InvalidMessageException {
    if (state == null) {
      throw new InvalidMessageException("Market State is of an invalid type.");
    }
    this.state = state;
  }

  /**
   * @returns the state of the market
   */
  public final MarketState getState() {
    return state;
  }
}