package book.exceptions;

/**
 * An exception that is thrown by a ProductBook object.
 *
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class ProductBookException extends Exception {

  public ProductBookException(String msg) {
    super(msg);
  }
}