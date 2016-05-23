package book.exceptions;

/**
 * A exception that is thrown when an invalid market state is encountered.
 *
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class InvalidMarketStateTransitionException extends Exception {

  public InvalidMarketStateTransitionException(String msg) {
    super(msg);
  }
}