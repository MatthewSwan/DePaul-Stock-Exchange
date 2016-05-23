package book.exceptions;

/**
 * A data validation exception used by the ProductBook.
 *
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class DataValidationException extends Exception {

  public DataValidationException(String msg) {
    super(msg);
  }
}