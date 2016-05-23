package price;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import price.exceptions.*;

/**
 *   The PriceFactory performs the creation of Price objects using
 *   the Factory design pattern. Whenever a Price object is needed by
 *   some component of the application, a call to the PriceFactory is used 
 *   to create a Price object using a desired value.
 * 
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */

public class PriceFactory	{

	private static Map<String, Price> prices = new HashMap<>();
	
	/**
	 * Method that creates a (limit) price object representing the value
	 * held in the provided long value. 
	 * @param amount is of type long. This will be the representation of the Price in cents
	 * Ex: a value of 1499 represents a price of $14.99
	 * @return p a Price object of the value being passed in
	 * @throws InvalidPriceOperation if amount passed in is 0
	 */
	public static Price makeLimitPrice(long amount) throws InvalidPriceOperation	{
		String key = amount + "";
		Price p = PriceFactory.prices.get(key);
		if(p == null) {
			p = new Price(amount);
			PriceFactory.prices.put(key,p);
		}	
		return p;
	}

	/**
	 * Method that creates a Market Price object
	 * @return p the Market Price object
	 */
	public static Price makeMarketPrice() {
		Price p = PriceFactory.prices.get("MKT");
		if (p == null) {
			p = new Price();
			PriceFactory.prices.put("MKT", p);
		}
		return p;
	}
	
	/**
	 * Method that creates a (limit) price object representing the value
	 * held in the provided String value.
	 * @param str is of type String. Value wished to make (limit) Price at
	 * @return Price object with the value of the provided String
	 * @throws InvalidPriceOperation if the String passed in is null
	 */
	public static Price makeLimitPrice(String str) 
		throws InvalidPriceOperation {
	long parsedValue = PriceFactory.parseDollarAmount(str);
	return PriceFactory.makeLimitPrice(parsedValue);
	}
	
	/**
	 * Private method parseDollarAmount that formats the string value
	 * in the method makeLimitPrice to a long value
	 * @param str is of type String
	 * @return formatted long value
	 * @throws InvalidPriceOperation if String passed in is null
	 */
	private static long parseDollarAmount (String str) throws InvalidPriceOperation	{
	if (str == null || str.isEmpty()) {
		return 0;
	}
	DecimalFormat formatter = new DecimalFormat("#.00");
	return Long.parseLong(formatter.format(Double.parseDouble(str.replaceAll("[^-.0-9]", ""))).replaceAll("\\.", ""));
	}

}
