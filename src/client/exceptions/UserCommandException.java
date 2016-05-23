package client.exceptions;

/**
 * An exception that is thrown when invalid input is encountered in the
 * UserCommandService.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class UserCommandException extends Exception {

  public UserCommandException(String message) {
    super(message);
  }
}