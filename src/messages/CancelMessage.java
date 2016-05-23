package messages;

import constants.global.BookSide;
import price.Price;
import messages.exceptions.InvalidMessageException;

/**
 * The CancelMessage class encapsulates data related to the cancellation of
 * an order or quote-side by a user, or by the trading system.
 *
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class CancelMessage implements GenericMessage, Comparable<CancelMessage> {

  GenericMessage cancelMessageImpl;

  /**
   * Creates a cancel message object.
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
  public CancelMessage(String user,
          String product, Price price, int volume, String details,
          BookSide side, String id)
          throws InvalidMessageException {
    cancelMessageImpl = MessageFactory.createCancelMessageImpl(user,
            product, price, volume, details, side, id);
  }

  /**
   * @return The username of the user whose order or quote-side is being cancelled.
   */
  public String getUser() {
    return cancelMessageImpl.getUser();
  }

  /**
   * @return The String stock symbol that the cancelled order or quote-side was submitted for
   */
  public String getProduct() {
    return cancelMessageImpl.getProduct();
  }

  /**
   * @return The price specified in the canceleld order or quote-side
   */
  public Price getPrice() {
    return cancelMessageImpl.getPrice();
  }

  /**
   * @return The quantity of the order or quote-side that was cancelled.
   */
  public int getVolume() {
    return cancelMessageImpl.getVolume();
  }

  /**
   * @return A text description of the cancellation.
   */
  public String getDetails() {
    return cancelMessageImpl.getDetails();
  }

  /**
   * @return The side (BUY/SELL) of the cancelled order or quote-side.
   */
  public BookSide getSide() {
    return cancelMessageImpl.getSide();
  }

  /**
   * Gets the ID of the user 
   */
  public String getID() {
    return cancelMessageImpl.getID();
  }

  /**
   * Compares the cancelled message price to the cancelled Message being passed in
   * @return a value depending on how the 2 prices compare
   */
  public int compareTo(CancelMessage o) {
    return cancelMessageImpl.getPrice().compareTo(o.getPrice());
  }

  /**
   * @return A String with the cancel message
   */
  public String toString() {
    return cancelMessageImpl.toString();
  }

  /**
   * Sets the volume of the CancelMessage
   */
  public void setVolume(int volume) throws InvalidMessageException {
    cancelMessageImpl.setVolume(volume);
  }

  /**
   * Sets the details of the CancelMessage
   */
  public void setDetails(String details) throws InvalidMessageException {
    cancelMessageImpl.setDetails(details);
  }
}