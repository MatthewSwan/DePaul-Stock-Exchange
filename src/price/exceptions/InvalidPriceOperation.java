package price.exceptions;

/**
 *   InvalidPriceOperation class to be thrown in certain 
 *   situations throughout the application
 *   
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */

public class InvalidPriceOperation extends Exception {
  
  public InvalidPriceOperation(String msg) {
    super(msg);
  }
}