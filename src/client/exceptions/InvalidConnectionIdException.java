package client.exceptions;

/**
 * An exception that is thrown when the user ID is invalid.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class InvalidConnectionIdException extends Exception {

  public InvalidConnectionIdException(String message) {
    super(message);
  }
}