package tradable.exceptions;

/**
 * An exception indicating that an invalid volume has been entered.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class InvalidVolumeException extends Exception {

  public InvalidVolumeException(String msg) {
    super(msg);
  }
}