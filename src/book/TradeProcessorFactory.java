package book;

import book.exceptions.InvalidProductBookSideValueException;

/**
 * A factory to create TradeProcessor objects with.
 *
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class TradeProcessorFactory {

  private synchronized static TradeProcessor createTradeProcessorPriceTimeImpl(ProductBookSide pbs)
		  throws InvalidProductBookSideValueException {
	  	  		return new TradeProcessorPriceTimeImpl(pbs);
  }

  /**
   * Creates a TradeProcessor based on the type passed in. 
   *
   * @return a TradeProcessor object
   */
  public synchronized static TradeProcessor createTradeProcessor(String type, ProductBookSide pbs)
          throws InvalidProductBookSideValueException {
    TradeProcessor processor;
    switch(type) {
      case "price-time":
      default:
        processor = createTradeProcessorPriceTimeImpl(pbs);
    }
    return processor;
  }
}