package messages;

import constants.global.BookSide;
import price.Price;
import messages.exceptions.InvalidMessageException;

/**
 * Implementation for Cancel and Fill Messages.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
class GenericMessageImpl implements GenericMessage {

	/**
	 * The String username of the user whose order or quote-side is being
	 * cancelled/filled. Cannot be null or empty.
	 */
	private String user;

	/**
	 * The string stock symbol that the cancelled/filled order or quote-side was
	 * submitted for; example ("IBM", "GE", etc.). Cannot be null or empty.
	 */
	private String product;

	/**
	 * The price specified in the cancelled/filled order or quote-side. Cannot
	 * be null.
	 */
	private Price price;

	/**
	 * The quantity of the order or quote-side that was cancelled/filled. Cannot
	 * be negative.
	 */
	private int volume;

	/**
	 * A text description of the cancellation/fulfillment. Cannot be null.
	 */
	private String details;

	/**
	 * The side (BUY/SELL) of the cancelled/filled order or quote-side. Must be
	 * a valid side.
	 */
	private BookSide side;

	/**
	 * The String identifier of the cancelled/filled order or quote-side. Cannot
	 * be null.
	 */
	public String id;

	  /**
	   * Creates a general implementation that will be delegated to be cancel and
	   * fill messages.
	   *
	   * @param user The String username of the user whose order or quote-side is being
	   * cancelled/filled. Cannot be null or empty.
	   * @param product The string stock symbol that the cancelled/filled order or quote-side was
	   * submitted for; example ("IBM", "GE", etc.). Cannot be null or empty.
	   * @param price The price specified in the cancelled/filled order or quote-side.
	   * Cannot be null.
	   * @param volume The quantity of the order or quote-side that was cancelled/filled.
	   * Cannot be negative.
	   * @param details A text description of the cancellation/fulfillment. Cannot be null.
	   * @param side The side (BUY/SELL) of the cancelled/filled order or quote-side.
	   * Must be a valid side.
	   * @param id The String identifier of the cancelled/filled order or quote-side.
	   * Cannot be null.
	   * @throws InvalidMessageException
	   */
	public GenericMessageImpl(String user, String product, Price price,
			int volume, String details, BookSide side, String id)
			throws InvalidMessageException {
		setUser(user);
		setProduct(product);
		setPrice(price);
		setVolume(volume);
		setDetails(details);
		setSide(side);
		setId(id);
	}

	/**
	 * @return user user of the order/quote side associated with this
	 * cancel/fill message.
	 */
	public final String getUser() {
		return user;
	}

	/**
	 * @return product the product of the order/quote side associated with this
	 * cancel/fill message.
	 */
	public final String getProduct() {
		return product;
	}

	/**
	 * @return price the price of the order/quote side associated with this
	 * cancel/fill message.
	 */
	public final Price getPrice() {
		return price;
	}

	/**
	 * @return volume the volume of the order/quote side associated with this
	 * cancel/fill message.
	 */
	public final int getVolume() {
		return volume;
	}

	/**
	 * @return details the details associated with this cancel/fill message.
	 */
	public final String getDetails() {
		return details;
	}

	/**
	 * @return side the side of the order/quote side associated with this
	 * cancel/fill message.
	 */
	public final BookSide getSide() {
		return side;
	}

	/**
	 * @return id the id of the order/quote side associated with this cancel/fill mesage
	 */
	public final String getID() {
		return id;
	}

	/**
	   * Set's the username associated with the cancel/fill request
	   * @param user set's username to parameter being passed. 
	   * @throws InvalidMessageException
	   */
	private void setUser(String user) throws InvalidMessageException {
		if (user == null || user.isEmpty()) {
			throw new InvalidMessageException("User cannot be null or empty.");
		}
		this.user = user;
	}

	/** 
	 * Set's the product of the order/quote side associated with this
	 * cancel/fill message.
	 */
	private void setProduct(String product) throws InvalidMessageException {
		if (product == null || product.isEmpty()) {
			throw new InvalidMessageException(
					"Product cannot be null or empty.");
		}
		this.product = product;
	}

	/**
	 * Set's the price of the order/quote side associated with this
	 * cancel/fill message.
	 * @param price set's the price to the passed in price
	 * @throws InvalidMessageException
	 */
	private void setPrice(Price price) throws InvalidMessageException {
		if (price == null) {
			throw new InvalidMessageException("Price cannot be null");
		}
		this.price = price;
	}

	/**
	 * Set's the volume of the order/quote side associated with this
	 * cancel/fill message.
	 * @param volume set's the volume to the passed in volume
	 * @throws InvalidMessageException
	 */
	public final void setVolume(int volume) throws InvalidMessageException {
		if (volume < 0) {
			throw new InvalidMessageException("Volume cannot be negative.");
		}
		this.volume = volume;
	}

	/**
	 * Set's the details associated with this cancel/fill message.
	 * @param details set's the details to the passed in details
	 * @throws InvalidMessageException
	 */
	public final void setDetails(String details) throws InvalidMessageException {
		if (details == null || details.isEmpty()) {
			throw new InvalidMessageException("Details cannot be null or empty");
		}
		this.details = details;
	}

	/**
	 * Set's the side of the order/quote side associated with this
	 * cancel/fill message.
	 * @param side set's the side to the passed in side
	 * @throws InvalidMessageException
	 */
	private void setSide(BookSide side) throws InvalidMessageException {
		if (!(side instanceof BookSide)) {
			throw new InvalidMessageException("Side must be a valid Book Side");
		}
		this.side = side;
	}

	/**
	 * Set's the id of the order/quote side associated with this cancel/fill message
	 * @param id set's the id to the passed in id
	 * @throws InvalidMessageException
	 */
	private void setId(String id) throws InvalidMessageException {
		if (id == null || id.isEmpty()) {
			throw new InvalidMessageException("ID cannot be null or empty.");
		}
		this.id = id;
	}

	/**
	 * @return A String with the cancel/fill message
	 */
	public String toString() {
		return "User: " + user + ", Product: " + product + ", Price: " + price
				+ ", Volume: " + volume + ", Details: " + details + ", Side: "
				+ side + ", ID: " + id;
	}
}