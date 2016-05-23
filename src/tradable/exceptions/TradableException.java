package tradable.exceptions;

/**
 *   TradableException class to be thrown in certain 
 *   situations throughout the application
 *   
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */

public class TradableException extends Exception {

  public TradableException(String message) {
    super(message);
  }
}