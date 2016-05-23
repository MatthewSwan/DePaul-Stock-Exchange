package book.exceptions;

/**
 * An exception thrown by the ProductService facade if no such product exists in
 * the product book.
 *
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class NoProductException extends Exception {

  public NoProductException(String msg) {
    super(msg);
  }
}