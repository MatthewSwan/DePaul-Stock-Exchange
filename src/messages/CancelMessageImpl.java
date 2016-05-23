package messages;

import constants.global.BookSide;
import price.Price;
import messages.exceptions.InvalidMessageException;

/**
 * Implementation object for a cancel message.
 * 
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class CancelMessageImpl implements GenericMessage {

  GenericMessage genMessage;

  /**
   * The Impl object a cancel message will delegate to.
   *
   * @param user The String username of the user whose order or quote-side is being cancelled.
   * Cannot be null or empty
   * @param product The string stock symbol that the cancelled order or quote-side was submitted for
   * (“IBM”, “GE”, etc.). Cannot be null or empty
   * @param price The price specified in the cancelled order or quote-side. Cannot be null
   * @param volume The quantity of the order or quote-side that was cancelled. Cannot be negative
   * @param details A text description of the cancellation. Cannot be null
   * @param side The side (BUY/SELL) of the cancelled order or quote-side. Must be a valid side
   * @param id The String identifier of the cancelled order or quote-side. Cannot be null
   * @throws InvalidMessageException
   */
  public CancelMessageImpl(String user,
          String product, Price price, int volume, String details,
          BookSide side, String id) throws InvalidMessageException {
    genMessage = MessageFactory.createGenericMessageImpl(user,
            product, price, volume, details, side, id);
  }

  /**
   * @return The username of the user whose order or quote-side is being cancelled.
   */
  public String getUser() {
    return genMessage.getUser();
  }

  /**
   * @return The String stock symbol that the cancelled order or quote-side was submitted for
   */
  public String getProduct() {
    return genMessage.getProduct();
  }

  /**
   * @return The price specified in the canceleld order or quote-side
   */
  public Price getPrice() {
    return genMessage.getPrice();
  }

  /**
   * @return The quantity of the order or quote-side that was cancelled.
   */
  public int getVolume() {
    return genMessage.getVolume();
  }

  /**
   * @return A text description of the cancellation.
   */
  public String getDetails() {
    return genMessage.getDetails();
  }

  /**
   * @return The side (BUY/SELL) of the cancelled order or quote-side.
   */
  public BookSide getSide() {
    return genMessage.getSide();
  }

  /**
   * Gets the ID 
   */
  public String getID() {
    return genMessage.getID();
  }

  /**
   * @return A String with the cancelled message
   */
  public String toString() {
    return genMessage.toString();
  }

  /**
   * Sets the volume of the CancelMessage
   */
  public void setVolume(int volume) throws InvalidMessageException {
    genMessage.setVolume(volume);
  }

  /**
   * Sets the details of the CancelMessage
   */
  public void setDetails(String details) throws InvalidMessageException {
    genMessage.setDetails(details);
  }
}