package book;

import java.util.HashMap;

import price.exceptions.InvalidPriceOperation;
import messages.FillMessage;
import messages.exceptions.InvalidMessageException;
import tradable.Tradable;
import tradable.exceptions.TradableException;

/**
 * The TradeProcessor is an interface that defines the functionality needed to
 * "execute" the actual trades between Tradable objects in this book side.
 *
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public interface TradeProcessor {

  /**
   * This TradeProcessor method will be called when it has been determined that
   * a Tradable (i.e., a Buy Order, a Sell QuoteSide, etc.) can trade against
   * the content of the book.
   * The return value from this function will be a HashMap<String, FillMessage>
   * containing String trade identifiers (the key) and a Fill Message object
   * (the value).
   *
   * @param trd
   * @return a HashMap<String, FillMessage>
   */
  public HashMap<String, FillMessage> doTrade(Tradable trd)
          throws InvalidMessageException, TradableException, InvalidPriceOperation;
}