package messages;

import constants.global.BookSide;
import price.Price;
import messages.exceptions.InvalidMessageException;

/**
 * Interface defining the behavior for Cancel and Fill Messages.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public interface GenericMessage {

	  /**
	   * @return user user of the order/quote side associated with this
	   * cancel/fill message.
	   */
  public String getUser();

  /**
   * @return product the product of the order/quote side associated with this
   * cancel/fill message.
   */
  public String getProduct();

  /**
   * @return price the price of the order/quote side associated with this
   * cancel/fill message.
   */
  public Price getPrice();

  /**
   * @return volume the volume of the order/quote side associated with this
   * cancel/fill message.
   */
  public int getVolume();

  /**
   * @return details the details associated with this cancel/fill message.
   */
  public String getDetails();

  /**
   * @return side the side of the order/quote side associated with this
   * cancel/fill message.
   */
  public BookSide getSide();

  /**
   * @return ID associated with this cancel/fill message
   */
  public String getID();

  /**
   * Sets the volume of a message
   * @param volume Argument passed in to set the volume of the message to
   * @throws InvalidMessageException
   */
  public void setVolume(int volume) throws InvalidMessageException;

  /**
   * Sets the details of a message
   * @param details Argument passed in to set the details of a message to
   * @throws InvalidMessageException
   */
  public void setDetails(String details) throws InvalidMessageException;
}