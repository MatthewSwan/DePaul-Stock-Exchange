package client;

import constants.global.BookSide;
import client.exceptions.TradableUserDataException;

/**
 * The TradableUserData class will hold selected data elements related to the
 * Tradables a user has submitted to the system. (Currently it will only be used for Orders).
 * 
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class TradableUserData {
	
	/**
	 * String for the user name
	 */
	private String userName;
	
	/**
	 * String for the stock symbol
	 */
	private String product;
	
	/**
	 * data member for the "side"
	 */
	private BookSide side;
	
	/**
	 * String to hold an order id
	 */
	String id;
	
	private TradableUserData self = this;
	
	/**
	 * Constructor
	 * 
	 * @param nameIn String for the user name
	 * @param productIn String for the stock symbol
	 * @param sideIn data member for the "side"
	 * @param idIn String to hold an order id
	 * @throws TradableUserDataException
	 */
	public TradableUserData(String nameIn, String productIn, BookSide sideIn,
			String idIn) throws TradableUserDataException	{
		setUserName(nameIn);
		setProduct(productIn);
		setSide(sideIn);
		setID(idIn);
	}
	
	/**
	 * Gets the user name for the submitted tradable
	 * 
	 * @return the user name
	 */
	public String getUserName()	{
		return userName;
	}
	
	/**
	 * Gets the stock symbol for the submitted tradable
	 * 
	 * @return the stock symbol
	 */
	public String getProduct()	{
		return product;
	}

	/**
	 * Gets the side for the submitted tradable
	 * 
	 * @return the side of the tradable
	 */
	public BookSide getSide()	{
		return side;
	}
	
	/**
	 * Gets the order ID for the submitted tradable
	 * 
	 * @return the order id
	 */
	public String getID()	{
		return id;
	}
	
	/**
	 * Sets the user name for the submitted tradable
	 * 
	 * @param theUser String user name passed as argument
	 * @throws TradableUserDataException
	 */
	private void setUserName(String theUser) throws TradableUserDataException	{
		validateInput(theUser);
		userName = theUser;
	}
	
	/**
	 * Sets the stock symbol for the submitted tradable
	 * 
	 * @param theProduct String stock symbol passed as argument
	 * @throws TradableUserDataException
	 */
	private void setProduct(String theProduct) throws TradableUserDataException	{
		validateInput(theProduct);
		product = theProduct;
	}
	
	/**
	 * Sets the side for the submitted tradable
	 * 
	 * @param theSide BookSide "side" passed as argument
	 * @throws TradableUserDataException
	 */
	private void setSide(BookSide theSide) throws TradableUserDataException	{
		validateInput(theSide);
		side = theSide;
	}
	
	/**
	 * Set's the id for the submitted tradable
	 * 
	 * @param theID String id passed as argument
	 * @throws TradableUserDataException
	 */
	private void setID(String theID) throws TradableUserDataException	{
		validateInput(theID);
		id = theID;
	}
	
	/**
	 * A String with the submitted tradable
	 */
	public String toString()	{
		return "User " + self.getUserName() + ": " + self.getSide() + " " +
				self.getProduct() + " (" + self.getID() + ")";
	}
	
	/**
	 * Validates a String input
	 * 
	 * @param o String passed as argument
	 * @throws TradableUserDataException
	 */
	private void validateInput(String o) throws TradableUserDataException	{
		if (o == null || o.isEmpty())	{
			throw new TradableUserDataException("Argument must be of type String and "
					+ "cannot be null or empty.");
		}
	}
	
	/**
	 * Validates a BookSide input
	 * 
	 * @param o BookSide "side" passed as argument
	 * @throws TradableUserDataException
	 */
	private void validateInput(BookSide o) throws TradableUserDataException	{
		if (o == null || !(o instanceof BookSide))	{
			throw new TradableUserDataException("Argument must be of type BookSide and "
					+ "cannot be null or empty.");
		}
	}
}
