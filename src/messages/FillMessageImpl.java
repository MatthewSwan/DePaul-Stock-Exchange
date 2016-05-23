package messages;

import constants.global.BookSide;
import price.Price;
import messages.exceptions.InvalidMessageException;

/**
 * Implementation object for a fill message.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class FillMessageImpl implements GenericMessage {

    GenericMessage generalMessage;

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
  public FillMessageImpl(String user,
          String product, Price price, int volume, String details,
          BookSide side, String id)
          throws InvalidMessageException {
    generalMessage = MessageFactory.createGenericMessageImpl(user,
            product, price, volume, details, side, id);
  }

  /**
   * @return The username of the user whose order or quote-side is being filled.
   */
  public String getUser() {
    return generalMessage.getUser();
  }

  /**
   * @return The String stock symbol that the fill order or quote-side was submitted for
   */
  public String getProduct() {
    return generalMessage.getProduct();
  }

  /**
   * @return The price specified in the fill order or quote-side
   */
  public Price getPrice() {
    return generalMessage.getPrice();
  }

  /**
   * @return The quantity of the order or quote-side that was filled.
   */
  public int getVolume() {
    return generalMessage.getVolume();
  }

  /**
   * @return A text description of the fill.
   */
  public String getDetails() {
    return generalMessage.getDetails();
  }

  /**
   * @return The side (BUY/SELL) of the filled order or quote-side.
   */
  public BookSide getSide() {
    return generalMessage.getSide();
  }

  /**
   * @return A String with the ID
   */
  public String getID() {
    return generalMessage.getID();
  }

  /**
   * @return A String with the fill message
   */
  public String toString() {
    String str = generalMessage.toString();
    return str.substring(0, str.indexOf("ID") - 2);
  }

  /**
   * Sets the quantity of a FillMessage
   * @param volume Argument passed in to set the quantity of a FillMessage.
   * @throws InvalidMessageException
   */
  public void setVolume(int volume) throws InvalidMessageException {
    generalMessage.setVolume(volume);
  }

  /**
   * Sets the details of a FillMessage
   * @param details Argument passed in to set the details of a FillMessage.
   * @throws InvalidMessageException
   */
  public void setDetails(String details) throws InvalidMessageException {
    generalMessage.setDetails(details);
  }
}