package client;

import java.util.ArrayList;

import book.exceptions.DataValidationException;
import book.exceptions.InvalidMarketStateException;
import book.exceptions.NoProductException;
import book.exceptions.OrderNotFoundException;
import book.exceptions.ProductBookException;
import book.exceptions.ProductBookSideException;
import book.exceptions.ProductServiceException;
import book.exceptions.TradeProcessorPriceTimeImplException;
import client.exceptions.AlreadyConnectedException;
import client.exceptions.InvalidConnectionIdException;
import client.exceptions.PositionException;
import client.exceptions.TradableUserDataException;
import client.exceptions.UserCommandException;
import client.exceptions.UserNotConnectedException;
import price.Price;
import price.exceptions.InvalidPriceOperation;
import price.exceptions.PriceException;
import publishers.exceptions.MessagePublisherException;
import messages.CancelMessage;
import messages.FillMessage;
import messages.exceptions.InvalidMessageException;
import constants.global.BookSide;
import tradable.TradableDTO;
import tradable.exceptions.InvalidVolumeException;
import tradable.exceptions.TradableException;

/**
 * The "User" interface contains the method declarations that any class that
 * wishes to be a "User" of the DSX trading system must implement.
 *
 * @authors Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public interface User {

  /**
   * This will return the String username of this user.
   *
   * @return user name
   */
  String getUserName();

  /**
   * This will accept a String stock symbol ("IBM, "GE", etc), a Price object
   * holding the value of the last sale (trade) of that stock, and the quantity
   * (volume) of that last sale. This info is used by "Users" to track stock
   * sales and volumes and is sometimes displayed in a GUI.
   *
   * @param product The stock symbol of the product. Must be a String
   * @param p Price object holding the value of the last sale. Must be a Price object
   * @param v quantity of the last sale. Must be an int
 * @throws InvalidPriceOperation 
   */
  void acceptLastSale(String product, Price p, int v) throws InvalidPriceOperation;

  /**
   * This will accept a FillMessage object which contains information related to
   * an order or quote trade. This is like a receipt sent to the user to
   * document the details when an order or quote-side of theirs trades.
   *
   * @param fm FillMessage object holding information related to a trade. Must be a FillMessage object
   */
  void acceptMessage(FillMessage fm);

  /**
   * This will accept a CancelMessage object which contains information related
   * to an order or quote cancel. This is like a receipt sent to the user to
   * document the details when an order or quote-side of theirs is canceled.
   *
   * @param cm CancelMessage object holding information related to a cancel. Must be a CancelMessage object
   */
  void acceptMessage(CancelMessage cm);

  /**
   * This will accept a String which contains market information related to a
   * Stock Symbol they are interested in.
   *
   * @param message Must be of type String
   */
  void acceptMarketMessage(String message);

  /**
   * This will accept a stock symbol ("IBM", "GE", etc), a Price object holding
   * the value of the last sale (trade) of that stock, and a "char" indicator of
   * whether the "ticker" price represents an increase or decrease in the
   * Stock's price. This info is used by "users" to track stock price movement,
   * and is sometimes displayed in a GUI.
   *
   * @param product String containing a stock symbol representing a product
   * @param p Price object containing the last day's sale
   * @param direction character representing whether a price is increasing or decreasing
   */
  void acceptTicker(String product, Price p, char direction);

  /**
   * This will accept a String stock symbol ("IBM", "GE", etc.), a Price object
   * holding the current BUY side price for that stock, an int holding the
   * current BUY side volume (quantity), a Price object holding the current SELL
   * side price for that stock, and an int holding the current SELL side volume
   * (quantity). These values as a group tell the user the "current market" for
   * a stock.<br /><br />
   * AMZN:   BUY 220@12.80 and SELL 100@12.85.<br /><br />
   * This info is used by "Users" to update their market display screen so that
   * they are always looking at the most current market data.
   *
   * @param product String containing a stock symbol representing a product
   * @param bp Price object containing current BUY side price
   * @param bv int containing the current BUY side volume
   * @param sp Price object containing current SELL side price
   * @param sv int containing the current SELL side volume
   */
  void acceptCurrentMarket(String product, Price bp, int bv, Price sp,
          int sv);
  
  /**
   * Instructs a User object to connect to the trading platform.
   * @throws UserCommandException 
   * @throws InvalidConnectionIdException 
   * @throws UserNotConnectedException 
   * @throws AlreadyConnectedException 
   */
  void connect() throws AlreadyConnectedException, UserNotConnectedException, 
  	InvalidConnectionIdException, UserCommandException;
  
  /**
   * Instructs a User object to disconnect from the trading system.
   * @throws UserCommandException 
   * @throws InvalidConnectionIdException 
   * @throws UserNotConnectedException 
   */
  void disConnect() throws UserNotConnectedException, InvalidConnectionIdException, UserCommandException;
  
  /**
   * Requests the opening of the market display if the user is connected.
   * @throws UserNotConnectedException 
   * @throws Exception 
   */
  void showMarketDisplay() throws UserNotConnectedException, Exception;
  
  /**
   * Allows the User object to submit a new Order request.
   * 
   * @param product String stock symbol
   * @param price Price of the Order
   * @param volume Quantity of the Order
   * @param side "side" of the Order
   * @throws UserCommandException 
   * @throws MessagePublisherException 
   * @throws TradeProcessorPriceTimeImplException 
   * @throws ProductServiceException 
   * @throws ProductBookException 
   * @throws ProductBookSideException 
   * @throws InvalidMessageException 
   * @throws NoProductException 
   * @throws InvalidMarketStateException 
   * @throws TradableException 
   * @throws InvalidVolumeException 
   * @throws InvalidConnectionIdException 
   * @throws UserNotConnectedException 
   * @throws TradableUserDataException 
   * @throws InvalidPriceOperation 
   * @throws DataValidationException 
   */
  String submitOrder(String product, Price price, int volume, BookSide side) throws TradableUserDataException, UserNotConnectedException, InvalidConnectionIdException, InvalidVolumeException, TradableException, InvalidMarketStateException, NoProductException, InvalidMessageException, ProductBookSideException, ProductBookException, ProductServiceException, TradeProcessorPriceTimeImplException, MessagePublisherException, UserCommandException, InvalidPriceOperation, DataValidationException;
  
  /**
   * Allows the User object to submit a new Order Cancel request.
   * 
   * @param product String stock symbol
   * @param side "side" of the Order Cancel
   * @param orderId Order cancel id
   * @throws UserCommandException 
   * @throws MessagePublisherException 
   * @throws ProductBookException 
   * @throws ProductServiceException 
   * @throws ProductBookSideException 
   * @throws InvalidVolumeException 
   * @throws OrderNotFoundException 
   * @throws InvalidMessageException 
   * @throws NoProductException 
   * @throws InvalidMarketStateException 
   * @throws InvalidConnectionIdException 
   * @throws UserNotConnectedException 
   * @throws TradableException 
   * @throws InvalidPriceOperation 
   */
  void submitOrderCancel(String product, BookSide side, String orderId) throws UserNotConnectedException, InvalidConnectionIdException, InvalidMarketStateException, NoProductException, InvalidMessageException, OrderNotFoundException, InvalidVolumeException, ProductBookSideException, ProductServiceException, ProductBookException, MessagePublisherException, UserCommandException, InvalidPriceOperation, TradableException;
  
  /**
   * Allows the User object to submit a new Quote request.
   * 
   * @param product String stock symbol
   * @param buyPrice Price the Quote will be bought at
   * @param buyVolume Quantity being bought
   * @param sellPrice Price the Quote will be sold at
   * @param sellVolume Quantity being sold
   * @throws UserCommandException 
   * @throws MessagePublisherException 
   * @throws TradeProcessorPriceTimeImplException 
   * @throws ProductServiceException 
   * @throws ProductBookException 
   * @throws ProductBookSideException 
   * @throws InvalidMessageException 
   * @throws DataValidationException 
   * @throws NoProductException 
   * @throws InvalidMarketStateException 
   * @throws TradableException 
   * @throws InvalidVolumeException 
   * @throws InvalidConnectionIdException 
   * @throws UserNotConnectedException 
   * @throws InvalidPriceOperation 
   */
  void submitQuote(String product, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume) throws UserNotConnectedException, InvalidConnectionIdException, InvalidVolumeException, TradableException, InvalidMarketStateException, NoProductException, DataValidationException, InvalidMessageException, ProductBookSideException, ProductBookException, ProductServiceException, TradeProcessorPriceTimeImplException, MessagePublisherException, UserCommandException, InvalidPriceOperation;
  
  /**
   * Allows the User object to submit a new Quote Cancel request.
   * 
   * @param product String stock symbol
   * @throws UserCommandException 
   * @throws MessagePublisherException 
   * @throws InvalidVolumeException 
   * @throws ProductServiceException 
   * @throws ProductBookException 
   * @throws ProductBookSideException 
   * @throws InvalidMessageException 
   * @throws NoProductException 
   * @throws InvalidMarketStateException 
   * @throws InvalidConnectionIdException 
   * @throws UserNotConnectedException 
   * @throws InvalidPriceOperation 
   */
  void submitQuoteCancel(String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidMarketStateException, NoProductException, InvalidMessageException, ProductBookSideException, ProductBookException, ProductServiceException, InvalidVolumeException, MessagePublisherException, UserCommandException, InvalidPriceOperation;
  
  /**
   * Allows the User object to subscribe for Current Market for the specified
   * Stock.
   * 
   * @param product String stock symbol
   * @throws UserCommandException 
   * @throws MessagePublisherException 
   * @throws InvalidConnectionIdException 
   * @throws UserNotConnectedException 
   */
  void subscribeCurrentMarket(String product) throws UserNotConnectedException, InvalidConnectionIdException, MessagePublisherException, UserCommandException;
  
  /**
   * Allows the User object to subscribe for Last Sale for the specified Stock.
   * 
   * @param product String stock symbol
   * @throws UserCommandException 
   * @throws MessagePublisherException 
   * @throws InvalidConnectionIdException 
   * @throws UserNotConnectedException 
   */
  void subscribeLastSale(String product) throws UserNotConnectedException, InvalidConnectionIdException, MessagePublisherException, UserCommandException;
  
  /**
   * Allows the User object to subscribe for Messages for the specified Stock.
   * 
   * @param product String stock symbol
   * @throws UserCommandException 
   * @throws MessagePublisherException 
   * @throws InvalidConnectionIdException 
   * @throws UserNotConnectedException 
   */
  void subscribeMessages(String product) throws UserNotConnectedException, InvalidConnectionIdException, 
  	MessagePublisherException, UserCommandException;
  
  /**
   * Allows the User object to subscribe for Ticker for the specified Stock.
   * 
   * @param product String stock symbol
   * @throws UserCommandException 
   * @throws MessagePublisherException 
   * @throws InvalidConnectionIdException 
   * @throws UserNotConnectedException 
   */
  void subscribeTicker(String product) throws UserNotConnectedException, InvalidConnectionIdException,
  	MessagePublisherException, UserCommandException;
  
  /**
   * @return the value of all Stock the User owns (has bought not sold).
   * @throws PriceException 
   * @throws PositionException 
   * @throws InvalidPriceOperation 
   */
  Price getAllStockValue() throws InvalidPriceOperation, PositionException, PriceException;
  
  /**
   * @return the different between cost of all stock purchases and stock sales.
   */
  Price getAccountCosts();
  
  /**
   * @return the difference between current value of all stocks owned and the account costs.
   * @throws PriceException 
   * @throws InvalidPriceOperation 
   * @throws PositionException 
   */
  Price getNetAccountValue() throws PositionException, InvalidPriceOperation, PriceException;
  
  /**
   * Allows the User object to submit a Book Depth request for the specified Stock.
   * 
   * @param product String stock symbol
   * @return the Book Depth
   * @throws UserCommandException 
   * @throws ProductServiceException 
   * @throws NoProductException 
   * @throws InvalidConnectionIdException 
   * @throws UserNotConnectedException 
   */
  String[][] getBookDepth(String product) throws UserNotConnectedException, InvalidConnectionIdException, 
  	NoProductException, ProductServiceException, UserCommandException;
  
  /**
   * Allows the User object to query the market state (OPEN, PREOPEN, CLOSED).
   * 
   * @return the market state
   * @throws UserCommandException 
   * @throws InvalidConnectionIdException 
   * @throws UserNotConnectedException 
   */
  String getMarketState() throws UserNotConnectedException, InvalidConnectionIdException, UserCommandException;
  
  /**
   * @return a list of order id's for the orders this user has submitted.
   */
  ArrayList<TradableUserData> getOrderIds();
  
  /**
   * @return a list of the stock products available in the trading system.
   */
  ArrayList<String> getProductList();
  
  /**
   * @param sym String stock symbol
   * @return the value of the specified stock that this user owns
   * @throws InvalidPriceOperation 
   * @throws PositionException 
   */
  Price getStockPositionValue(String sym) throws PositionException, InvalidPriceOperation;
  
  /**
   * @param product String stock symbol
   * @return the volume of the specified stock that this user owns
   * @throws PositionException 
   */
  int getStockPositionVolume(String product) throws PositionException;
  
  /**
   * @return a list of all the Stocks the user owns
   */
  ArrayList<String> getHoldings();
  
  /**
   * @param product String stock symbol
   * @return a list of DTO's containing information on all Orders for this
   * 	user for the specified product with remaining volume.
   * @throws UserCommandException 
   * @throws ProductServiceException 
   * @throws ProductBookException 
   * @throws ProductBookSideException 
   * @throws InvalidConnectionIdException 
   * @throws UserNotConnectedException 
   */
  ArrayList<TradableDTO> getOrdersWithRemainingQty(String product) throws UserNotConnectedException, 
  	InvalidConnectionIdException, ProductBookSideException, ProductBookException, ProductServiceException, UserCommandException;
}