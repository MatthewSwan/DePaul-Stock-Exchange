package messages;

import constants.global.BookSide;
import price.Price;
import messages.exceptions.InvalidMessageException;

/**
 * The FillMessage class encapsulates data related to the fill (trade) of an order or quote-side.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class FillMessage implements GenericMessage,
        Comparable<FillMessage> {

  GenericMessage fillMessageImpl;

  /**
   * Creates a cancel message object.
   *
   * @param user The String username of the user whose order or quote-side was filled. Cannot be null
   * or empty
   * @param product The string stock symbol that the filled order or quote-side was submitted for
   * (“IBM”, “GE”, etc.). Cannot be null or empty
   * @param price The price that the order or quote-side was filled at. Cannot be null
   * @param volume The quantity of the order or quote-side that was filled. Cannot be negative
   * @param details A text description of the fill (trade). Cannot be null
   * @param side The side (BUY/SELL) of the filled order or quote-side. Must be a valid side
   * @param id The String identifier of the filled order or quote-side. Cannot be null
   * @throws InvalidMessageException
   */
  public FillMessage(String user,
          String product, Price price, int volume, String details,
          BookSide side, String id)
          throws InvalidMessageException {
    fillMessageImpl = MessageFactory.createFillMessageImpl(user,
            product, price, volume, details, side, id);
  }

  /**
   * @return The username of the user whose order or quote-side is being filled.
   */
  public String getUser() {
    return fillMessageImpl.getUser();
  }

  /**
   * @return The String stock symbol that the fill order or quote-side was submitted for
   */
  public String getProduct() {
    return fillMessageImpl.getProduct();
  }

  /**
   * @return The price specified in the fill order or quote-side
   */
  public Price getPrice() {
    return fillMessageImpl.getPrice();
  }

  /**
   * @return The quantity of the order or quote-side that was filled.
   */
  public int getVolume() {
    return fillMessageImpl.getVolume();
  }

  /**
   * @return A text description of the fill.
   */
  public String getDetails() {
    return fillMessageImpl.getDetails();
  }

  /**
   * @return The side (BUY/SELL) of the filled order or quote-side.
   */
  public BookSide getSide() {
    return fillMessageImpl.getSide();
  }

  /**
   * Get's the Id of the FillMessage
   * @return Id of the Fill Message
   */
  public String getID() {
    return fillMessageImpl.getID();
  }

  /**
   * Compares the filled message price to the  fill message being passed in
   * @return a value depending on how the 2 prices compare
   */
  public int compareTo(FillMessage o) {
    return fillMessageImpl.getPrice().compareTo(o.getPrice());
  }

  /**
   * Changes the FillMessage to a String form
   * @return A String with the fill message
   */
  public String toString() {
    return fillMessageImpl.toString();
  }

  /**
   * Sets the quantity of a FillMessage
   * @param volume Argument passed in to set the quantity of a FillMessage.
   * @throws InvalidMessageException
   */
  public void setVolume(int volume) throws InvalidMessageException {
    fillMessageImpl.setVolume(volume);
  }

  /**
   * Sets the details of a FillMessage
   * @param details Argument passed in to set the details of a FillMessage.
   * @throws InvalidMessageException
   */
  public void setDetails(String details) throws InvalidMessageException {
    fillMessageImpl.setDetails(details);
  }
}