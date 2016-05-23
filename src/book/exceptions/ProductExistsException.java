package book.exceptions;

/**
 * An exception that is thrown when you are trying to add a product book that
 * already exists.
 *
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class ProductExistsException extends Exception {

  public ProductExistsException(String msg) {
    super(msg);
  }
}