package book.exceptions;

/**
 *Exception for incorrect BookSide of a product
 *
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class ProductBookSideException extends Exception {

  public ProductBookSideException(String msg) {
    super(msg);
  }
}