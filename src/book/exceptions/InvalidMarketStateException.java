package book.exceptions;

/**
 * An exception that is thrown when trying to perform an action in an invalid
 * market state.
 *
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class InvalidMarketStateException extends Exception {

  public InvalidMarketStateException(String msg) {
    super(msg);
  }
}