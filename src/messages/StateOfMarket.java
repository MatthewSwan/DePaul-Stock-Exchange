package messages;

import constants.global.MarketState;

/**
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public interface StateOfMarket {

  /**
   * Returns the state of the Market (CLOSED, OPEN, PREOPEN).
   *
   * @return the state of the market.
   */
  MarketState getState();
}
