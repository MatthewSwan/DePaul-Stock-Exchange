package book.exceptions;

/**
 * Exception thrown when an invalid value for a ProductBookSide is passed into a constructor
 * for a TradeProcessor
 * 
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 *
 */

public class InvalidProductBookSideValueException extends Exception	{

	public InvalidProductBookSideValueException(String msg)	{
		super(msg);
	}
}
