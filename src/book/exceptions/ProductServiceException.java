package book.exceptions;

/**
 * Exception thrown by the ProductService
 * 
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */

public class ProductServiceException extends Exception	{

	public ProductServiceException(String msg)	{
		super(msg);
	}
}
