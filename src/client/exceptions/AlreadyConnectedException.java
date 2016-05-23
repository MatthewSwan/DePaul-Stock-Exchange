package client.exceptions;

/**
 * An Exception that is thrown when a user is already connected to the system.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung 
 */
public class AlreadyConnectedException extends Exception {

  public AlreadyConnectedException(String message) {
    super(message);
  }
}
