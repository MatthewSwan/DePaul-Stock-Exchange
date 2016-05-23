package messages.exceptions;

/**
 * Exception thrown when a data element not in line is set as a market message
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class InvalidMessageException extends Exception {

  public InvalidMessageException(String msg) {
    super(msg);
  }
}