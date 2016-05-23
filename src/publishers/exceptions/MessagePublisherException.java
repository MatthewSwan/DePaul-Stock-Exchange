package publishers.exceptions;

/**
 * The exception class dealing with exceptions which occur in the publishers.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class MessagePublisherException extends Exception {

  public MessagePublisherException(String msg) {
    super(msg);
  }
}