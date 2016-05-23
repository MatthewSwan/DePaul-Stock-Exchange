package price.exceptions;

/**
 * An exception that is thrown on validation of a Price object.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class PriceException extends Exception {

  public PriceException(String message) {
    super(message);
  }
}