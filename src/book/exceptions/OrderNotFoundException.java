package book.exceptions;

/**
 * Exception that is thrown when an order can't be found.
 *
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class OrderNotFoundException extends Exception {

  public OrderNotFoundException(String msg) {
    super(msg);
  }
}