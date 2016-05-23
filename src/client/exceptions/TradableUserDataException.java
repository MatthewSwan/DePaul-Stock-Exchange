package client.exceptions;

/**
 * An exception that is thrown when trying to set the data of a TradableUserData
 * object.
 * 
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */

public class TradableUserDataException extends Exception	{

	public TradableUserDataException(String msg)	{
		super(msg);
	}
}
