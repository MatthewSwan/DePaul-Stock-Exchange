package client.exceptions;

/**
 * Throws an exception if the user is not connected.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class UserNotConnectedException extends Exception {

  public UserNotConnectedException(String message) {
    super(message);
  }
}
