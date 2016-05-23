package messages;

import constants.global.BookSide;
import constants.global.MarketState;
import price.Price;
import messages.exceptions.InvalidMessageException;

/**
 * A factory used to create cancel, fill, and market messages.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
class MessageFactory {

	/**
	 * Creates a CancelMessageImpl object the cancel message will delegate to.
	 *
	 * @param user String username of the user
	 * @param product String stock symbol
	 * @param price Price specified
	 * @param volume Quantity specified
	 * @param details Text description
	 * @param side The side (BUY/SELL)
	 * @param id String identifier
	 * @return a cancel message impl object.
	 * @throws InvalidMessageException
	 */
	public static CancelMessageImpl createCancelMessageImpl(String user,
			String product, Price price, int volume, String details,
			BookSide side, String id) throws InvalidMessageException {
		return new CancelMessageImpl(user, product, price, volume, details,
				side, id);
	}

    /**
     * Creates a FillMessageImpl object the fill message will delegate to.
     *
     * @param user String username of the user
     * @param product String stock symbol
     * @param price Price specified
     * @param volume Quantity specified
     * @param details Text description 
     * @param side The side (BUY/SELL) 
     * @param id String identifier 
     * @return a fill message impl object.
     * @throws InvalidMessageException
     */
	public static FillMessageImpl createFillMessageImpl(String user,
			String product, Price price, int volume, String details,
			BookSide side, String id) throws InvalidMessageException {
		return new FillMessageImpl(user, product, price, volume, details, side,
				id);
	}

	/**
	 * Creates a MarketMessageImpl object the market message will delegate to.
	 *
	 * @param state Value passed in which is assigned to the MarketState
	 * @return a market message impl object.
	 * @throws InvalidMessageException
	 */
	public static MarketMessageImpl createMarketMessageImpl(MarketState state)
			throws InvalidMessageException {
		return new MarketMessageImpl(state);
	}

	/**
	 * Creates a generic Market Message used by the CancelMessageImpl and
	 * FillMessageImpl to delegate requests to.
	 *
	 * @param user String username of the user
	 * @param product String stock symbol
	 * @param price Price specified
	 * @param volume Quantity specified
	 * @param details Text description 
	 * @param side The side (BUY/SELL) 
	 * @param id String identifier 
	 * @return
	 * @throws InvalidMessageException
	 */
	protected static GenericMessage createGenericMessageImpl(String user,
			String product, Price price, int volume, String details,
			BookSide side, String id) throws InvalidMessageException {
		return new GenericMessageImpl(user, product, price, volume, details,
				side, id);
	}
}