package price;

import price.exceptions.InvalidPriceOperation;

/**
 *  The Price class represents various Prices used with the
 *  DePaulStrockExchange project. Price objects must be
 *  immutable. 
 * 
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */

public final class Price implements Comparable<Price>	{

	private final boolean isMarketPrice;
	private final long value;
	private final int lessVal = -1;
	private final int equalVal = 0;
	private final int greaterVal = 1;
	
	/**
	 * Creates a Price object that's a Limit Price
	 * A long amount passed in of 1500 would represent $14.00
	 * 
	 * @param amount value that's passed into the Price object
	 */
	Price(long amount)	{
		this.value = amount;
		this.isMarketPrice = false;
	}
	
	/**
	 * Creates a Price object that's a Market Price
	 */
	Price()	{
		this.value = 0;
		this.isMarketPrice = true;
	}
	
	/**
	 * Add method that will send a request to PriceFactory to create
	 * a new Price object with two Price values added together
	 * @param p	must be of type Price. It's the value added to the current Price
	 * @return new Price object with the two Prices added together	
	 * @throws InvalidPriceOperation Invalid operation occurred on 
	 * a Price object if either Price is a market Price or p is null
	 */
	public Price add(Price p) throws InvalidPriceOperation {
		if(p.isMarket() || this.isMarket() || p == null)	{
			throw new InvalidPriceOperation("Invalid Price Operation: "
					+ "Price passed is null, or either Price passed or current Price (or both) is a Market Price.");
		}
		else
			return PriceFactory.makeLimitPrice(this.value + p.value);
	}
	
	/**
	 * Subtract method that will send a request to PriceFactory to create
	 * a new Price object subtracting the argument Price from the current value
	 * @param p	must be of type Price. Subtracted from the current value
	 * @return new Price object with the argument Price value subtracted
	 * from the current Price value	
	 * @throws InvalidPriceOperation Invalid operation occurred on 
	 * a Price object if either Price is market or p is null
	 */
	public Price subtract(Price p) throws InvalidPriceOperation	{
		if(p.isMarket() || this.isMarket() || p == null)	{
			throw new InvalidPriceOperation("Invalid Price Operation: "
					+ "Price passed is null, or either Price passed or current Price (or both) is a Market Price.");
		}
		else 
			return PriceFactory.makeLimitPrice(this.value - p.value);
	}
	
	/**
	 * Multiply method that will send a request to PriceFactory to create
	 * a new Price object, which will be the value pass multiplied by the current
	 * Price object's value
	 * @param p	must be of type int. The value passed to multiply the current value by
	 * @return new Price object with a new Price multiplied by some integer p
	 * @throws InvalidPriceOperation Invalid operation occurred on 
	 * a Price object if Price is a market Price or p is 0
	 */
	public Price multiply(int p) throws InvalidPriceOperation	{
		if (this.isMarket() || p == 0)	{
			throw new InvalidPriceOperation("Invalid Price Operation: "
					+ "Current Price is a Market Price or p is zero.");
		}
		else
			return PriceFactory.makeLimitPrice(this.value * p);
	}
	
	/**
	 * Method comparing Price passed in to current Price. 
	 * @param p must be of type Price. Price passed in to be compared with the current Price
	 * @return greater or less or equal value when the Prices are compared. market Price is equalVal
	 */
	public int compareTo(Price p)	{
		if (this.value > p.value)	{
			return this.greaterVal;
		}
		if (this.value < p.value)	{
			return this.lessVal;
		}
		return this.equalVal;
	}
	
	/**
	 * Method determining if the current Price is greater or equal to the Price passed in
	 * @param p must be of type Price. Compared to current Price
	 * @return true if the current Price is greater than or equal p, false otherwise
	 */
	public boolean greaterOrEqual(Price p)	{
		if(this.isMarket() || p.isMarket())	{
			return false;
		}
		int difference = this.compareTo(p);
		if (difference == this.greaterVal || difference == this.equalVal)	{
			return true;
		}
		return false;	
	}

	/**
	 * Method determining if the current Price is greater than the Price passed in
	 * @param p must be of type Price. Compared to current Price
	 * @return true if the current Price is greater than p, false otherwise
	 */
	public boolean greaterThan(Price p)	{
		if (this.isMarket() || p.isMarket()){
			return false;
		}
		int difference = this.compareTo(p);
		if (difference == this.greaterVal)	{
			return true;
		}
		return false;
	}
	
	/**
	 * Method determining if the current Price is less than or equal to the Price passed in
	 * @param p must be of type Price. Compared to current Price
	 * @return true if the current Price is less than or equal p, false otherwise
	 */
	public boolean lessOrEqual(Price p)	{
		 if (this.isMarket() || p.isMarket())	{
			 return false;
		 }
		 int difference = this.compareTo(p);
		 if (difference == this.lessVal || difference == this.equalVal)	{
			 return true;
		 }
		 return false;
	}
	
	/**
	 * Method determining if the current Price is less than the Price passed in
	 * @param p must be of type Price. Compared to current Price
	 * @return true if the current Price is less than p, false otherwise
	 */
	public boolean lessThan(Price p)	{
		if (this.isMarket() || p.isMarket())	{
			return false;
		}
		int difference = this.compareTo(p);
		if (difference == this.lessVal)	{
			return true;
		}
		return false;
	}
	
	/**
	 * Method determining if the current Price is equal to the Price passed in
	 * @param p must be of type Price. Compared to current Price
	 * @return true if the current Price is equal to p, false otherwise
	 */
	public boolean equals(Price p)	{
		 if (this.isMarket() || p.isMarket())	{
			 return false;
		 }
		 if (this.compareTo(p) == this.equalVal)	{
			 return true;
		 }
		 return false;
	}
	
	/**
	 * Boolean method that tells whether the Price is a Market Price or not
	 * @return true if it's a Market Price, false if not
	 */
	public boolean isMarket()	{
		return this.isMarketPrice;
	}
	
	/**
	 * Boolean method that tells whether the Price is negative or not. Also
	 * if the Price is a Market Price.  
	 * @return true if less than 0, false if greater than 0 or a Market Price
	 */
	public boolean isNegative()	{
		if (this.isMarket() || this.value >= 0)	{
			return false;
		}
		return true;
	}
	
	/**
	 * Method formatting the Prices to the String we want to appear
	 * @return formatted String. MKT for Market Prices.
	 */
	public String toString()	{	
        if (this.isMarket()) {
            return "MKT";
        }
        return String.format("$%,.2f", (double) value / 100.0);
	}
}