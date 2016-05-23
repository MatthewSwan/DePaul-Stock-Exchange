package client;

import price.exceptions.InvalidPriceOperation;
import client.exceptions.UserException;

/**
 * A factory used to create new users.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class UserImplFactory {

	/**
	 * Creates a new User object
	 * 
	 * @param userName String user name of user being created
	 * @return a new User object with the passed in userName
	 * @throws UserException
	 * @throws InvalidPriceOperation
	 */
  public static UserImpl createUser(String userName) throws UserException, InvalidPriceOperation {
    return new UserImpl(userName);
  }
}