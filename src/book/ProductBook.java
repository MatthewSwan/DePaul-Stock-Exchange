package book;

import book.exceptions.DataValidationException;
import book.exceptions.OrderNotFoundException;
import book.exceptions.ProductBookException;
import book.exceptions.ProductBookSideException;
import constants.global.BookSide;
import constants.global.MarketState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Map.Entry;

import price.Price;
import price.PriceFactory;
import price.exceptions.*;
import publishers.CurrentMarketPublisher;
import publishers.LastSalePublisher;
import publishers.MessagePublisher;
import publishers.exceptions.MessagePublisherException;
import messages.CancelMessage;
import messages.FillMessage;
import messages.MarketDataDTO;
import messages.exceptions.InvalidMessageException;
import tradable.Order;
import tradable.Quote;
import tradable.Tradable;
import tradable.TradableDTO;
import tradable.exceptions.TradableException;
import book.exceptions.InvalidProductBookSideValueException;

/**
 * A ProductBook object maintains the Buy and Sell sides of a stock's "book".
 * Many of the functions owned by the ProductBook class are designed to simply
 * pass along that same call to either the Buy or Sell side of the book.
 *
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class ProductBook {

  /**
   * The String stock symbol that this book represents (i.e., MSFT,
   * IBM, AAPL, etc).
   */
  private String symbol;

  /**
   * ProductBookSide that maintains the Buy side of this book.
   */
  private ProductBookSide buySide;

  /**
   * ProductBookSide that maintains the Sell side of this book.
   */
  private ProductBookSide sellSide;

  /**
   * A String that will hold the toString results of the latest Market Data
   * values (the prices and the volumes at the top of the buy and sell sides).
   */
  private String latestMarketValues = "";

  /**
   * A list of the current quotes in this book for each user.
   */
  private HashSet<String> userQuotes = new HashSet<>();

  /**
   * A list of Tradables (those that have been completely traded or
   * cancelled) organized by price and by user. This should be represented like
   * you represented the book entries for one side of the book in the
   * ProductBookSide class.
   */
  private HashMap<Price, ArrayList<Tradable>> oldEntries = new HashMap<Price, ArrayList<Tradable>>();

  /**
   * Method used to set the stock symbol data member. 
   * The Buy and Sell side books are created here also.
   * @param stockSymbol Argument to set the stock symbol data member to
   * @throws ProductBookException
   * @throws ProductBookSideException
   * @throws InvalidProductBookSideValueException
   */
  public ProductBook(String stockSymbol) throws ProductBookException, ProductBookSideException,
          InvalidProductBookSideValueException {
	  setSymbol(stockSymbol);
	  buySide = new ProductBookSide(this, BookSide.BUY);
	  sellSide = new ProductBookSide(this, BookSide.SELL);
  }
  
  /**
   * Sets the symbol of the stock to the passed in argument
   * @param stockSymbol Argument passed to set the stock symbol to
   * @throws ProductBookException
   */
  private void setSymbol(String stockSymbol) throws ProductBookException {
	    if (stockSymbol == null) {
	      throw new ProductBookException("Symbol cannot be null!");
	    }
	    symbol = stockSymbol;
  }

  /**
   * This method is designed to determine if it is too late to cancel an order
   * (meaning it has already been traded out or cancelled).
   *
   * @param orderId Argument passed in to see if its too last to cancel
   */
  public synchronized void checkTooLateToCancel(String orderId) throws OrderNotFoundException, 
  	InvalidMessageException, MessagePublisherException {
    boolean isFound = false;
    for(Entry<Price, ArrayList<Tradable>> row : oldEntries.entrySet()) {
      ListIterator<Tradable> iterator = row.getValue().listIterator();
      while (iterator.hasNext()) {
        Tradable t = iterator.next();
        if (t.getId().equals(orderId)) {
          isFound = true;
          MessagePublisher.getInstance().publishCancel(new CancelMessage(
                  t.getUser(), t.getProduct(), t.getPrice(),
                  t.getRemainingVolume(), "Too late to cancel order ID: " +
                  t.getId(), t.getSide(), t.getId()));
        }
      }
    }
    if (!isFound) {
    	throw new OrderNotFoundException("The order with the specified order id: " 
    	+ orderId + "; could not be found.");
    }
  }

  /**
   * This method should return a 2-dimensional array of Strings that contain
   * the prices and volumes for all prices present in the buy and sell sides of
   * the book.
   *
   * @return 2-dimensional array of Strings
   */
  public synchronized String[][] getBookDepth() {
    String[][] bd = new String[2][];
    bd[0] = buySide.getBookDepth();
    bd[1] = sellSide.getBookDepth();
    return bd;
  }

  /**
   * This method should create a MarketDataDTO containing the best buy side
   * price and volume, and the best sell side price an volume.
   *
   * @return MarketDataDTO with top and best of buy and sell side
   */
  public synchronized MarketDataDTO getMarketData() throws InvalidPriceOperation {
    Price topBuySidePrice = buySide.topOfBookPrice();
    Price topSellSidePrice = sellSide.topOfBookPrice();
    if (topBuySidePrice == null) {
      topBuySidePrice = PriceFactory.makeLimitPrice("0");
    }
    if (topSellSidePrice == null) {
      topSellSidePrice = PriceFactory.makeLimitPrice("0");
    }
    int bestBuySideVolume = buySide.topOfBookVolume();
    int bestSellSideVolume = sellSide.topOfBookVolume();
    return new MarketDataDTO(symbol, topBuySidePrice, bestBuySideVolume,
            topSellSidePrice, bestSellSideVolume);
  }
  
  /**
   * Method returning an ArrayList<TradableDTO> containing any orders for the specified 
   * user that have remaining quantity.
   * @param userName Argument passed in the set the user
   * @return ArrayList<TradableDTO> objects that contain the deatils on all order
   * from the specified user
   */
  public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(String userName) {
    ArrayList<TradableDTO> tradableDTOObjects = new ArrayList<>();
    tradableDTOObjects.addAll(buySide.getOrdersWithRemainingQty(userName));
    tradableDTOObjects.addAll(sellSide.getOrdersWithRemainingQty(userName));
    return tradableDTOObjects;
  }

  /**
   * This method should add the Tradable passed in to the "oldEntries" HashMap.
   *
   * @param t 
   * @throws InvalidPriceOperation 
   */
  public synchronized void addOldEntry(Tradable t) throws TradableException, 
  	InvalidPriceOperation {
    if (!oldEntries.containsKey(t.getPrice())) {
      oldEntries.put(t.getPrice(), new ArrayList<Tradable>());
    }
    t.setCancelledVolume(t.getRemainingVolume());
    t.setRemainingVolume(0);
    oldEntries.get(t.getPrice()).add(t);
  }

  /**
   * This method will "Open" the book for trading. Any resting Order and
   * QuoteSides that are immediately tradable upon opening should be traded.
   * @throws InvalidPriceOperation 
   */
  public synchronized void openMarket() throws InvalidMessageException, TradableException, 
  	InvalidPriceOperation, MessagePublisherException	{
    Price buyPrice = buySide.topOfBookPrice();
    Price sellPrice = sellSide.topOfBookPrice();
    if (buyPrice == null || sellPrice == null) { 
    	return; 
    	}
    while (buyPrice.greaterOrEqual(sellPrice) || buyPrice.isMarket()
            || sellPrice.isMarket()) {
      ArrayList<Tradable> topOfBuySide = buySide.getEntriesAtPrice(buyPrice);
      HashMap<String, FillMessage> allFills = null;
      ArrayList<Tradable> toRemove = new ArrayList<>();
      for (Tradable t : topOfBuySide) {
        allFills = sellSide.tryTrade(t);
        if (t.getRemainingVolume() == 0) {
          toRemove.add(t);
        }
      }
      for (Tradable t : toRemove) {
        buySide.removeTradeable(t);
      }
      updateCurrentMarket();
      Price lastSalePrice = determineLastSalePrice(allFills);
      int lastSaleVolume = determineLastSaleQuantity(allFills);
      LastSalePublisher.getInstance().publishLastSale(symbol, lastSalePrice,
              lastSaleVolume);
      buyPrice = buySide.topOfBookPrice();
      sellPrice = sellSide.topOfBookPrice();
      if (buyPrice == null || sellPrice == null) { break; }
    }
  }

  /**
   * This method will "Close" the book for trading.
   * @throws InvalidPriceOperation 
   * @throws MessagePublisherException 
   */
  public synchronized void closeMarket() throws InvalidMessageException, OrderNotFoundException,
          TradableException, InvalidPriceOperation, MessagePublisherException {
    buySide.cancelAll();
    sellSide.cancelAll();
    updateCurrentMarket();
  }

  /**
   * This method will cancel the Order specified by the provided orderId on the
   * specified side.
   *
   * @param side Argument telling which side of the BookSide is represented
   * @param orderId Argument representing the Id of the Order to cancel
   * @throws InvalidPriceOperation 
   * @throws MessagePublisherException 
   */
  public synchronized void cancelOrder(BookSide side, String orderId) throws InvalidMessageException, 
  	OrderNotFoundException, TradableException, InvalidPriceOperation, MessagePublisherException {
    if (side.equals(BookSide.BUY)) {
      buySide.submitOrderCancel(orderId);
    } else {
      sellSide.submitOrderCancel(orderId);
    }
    updateCurrentMarket();
  }

  /**
   * This method will cancel the specified user’s Quote on the both the BUY and
   * SELL sides.
   *
   * @param userName Argument pass in to represent the user for the cancelOrder
   * @throws InvalidPriceOperation 
   * @throws MessagePublisherException 
   */
  public synchronized void cancelQuote(String userName) throws InvalidMessageException, 
  	InvalidPriceOperation, MessagePublisherException {
    buySide.submitQuoteCancel(userName);
    sellSide.submitQuoteCancel(userName);
    buySide.removeEmptyKeys();
    sellSide.removeEmptyKeys();
    updateCurrentMarket();
  }

  /**
   * This method should add the provided Quote’s sides to the Buy and Sell
   * ProductSideBooks.
   *
   * @param q Quote object
   * @throws InvalidVolumeException
   * @throws DataValidationException
   * @throws InvalidPriceOperation 
   * @throws MessagePublisherException 
   */
  public synchronized void addToBook(Quote q) throws TradableException, DataValidationException,
          InvalidMessageException, InvalidPriceOperation, MessagePublisherException {
    if (q.getQuoteSide(BookSide.SELL).getPrice().lessOrEqual(
            q.getQuoteSide(BookSide.BUY).getPrice())) {
      throw new DataValidationException("Sell Price is less than or equal to buy price.");
    }
    if (q.getQuoteSide(BookSide.SELL).getPrice().lessOrEqual(
            PriceFactory.makeLimitPrice("0")) ||
            q.getQuoteSide(BookSide.BUY).getPrice().lessOrEqual(
            PriceFactory.makeLimitPrice("0"))) {
      throw new DataValidationException("Buy or Sell Price cannot be less than or equal to zero.");
    }
    if (q.getQuoteSide(BookSide.SELL).getOriginalVolume() <= 0 ||
            q.getQuoteSide(BookSide.BUY).getOriginalVolume() <= 0) {
      throw new DataValidationException("Volume of a Buy or Sell side quote cannot be less than or equal to zero,");
    }
    if (userQuotes.contains(q.getUserName())) {
      buySide.removeQuote(q.getUserName());
      sellSide.removeQuote(q.getUserName());
      updateCurrentMarket();
    }
    addToBook(BookSide.BUY, q.getQuoteSide(BookSide.BUY));
    addToBook(BookSide.SELL, q.getQuoteSide(BookSide.SELL));
    userQuotes.add(q.getUserName());
    updateCurrentMarket();
  }

  /**
   * This method should add the provided Order to the appropriate
   * ProductSideBook.
   *
   * @param o Order argument passed in
   * @throws InvalidPriceOperation 
   * @throws MessagePublisherException 
   */
  public synchronized void addToBook(Order o) throws InvalidMessageException, TradableException, 
  	InvalidPriceOperation, MessagePublisherException {
    addToBook(o.getSide(), o);
    updateCurrentMarket();
  }

  /**
   * This method needs to determine if the "market" for this stock product has
   * been updated by some market action.
   * @throws InvalidPriceOperation 
   */
  public synchronized void updateCurrentMarket() throws InvalidPriceOperation, MessagePublisherException {
    String var = buySide.topOfBookPrice() +
            String.valueOf(buySide.topOfBookVolume()) +
            sellSide.topOfBookPrice() +
            String.valueOf(sellSide.topOfBookVolume());
    if (!latestMarketValues.equals(var)) {
      MarketDataDTO current = new MarketDataDTO(symbol,
              (buySide.topOfBookPrice() == null) ?
              PriceFactory.makeLimitPrice("0")
              : buySide.topOfBookPrice(),
              buySide.topOfBookVolume(),
              (sellSide.topOfBookPrice() == null) ?
              PriceFactory.makeLimitPrice("0") : sellSide.topOfBookPrice(),
              sellSide.topOfBookVolume());
      CurrentMarketPublisher.getInstance().publishCurrentMarket(current);
      latestMarketValues = var;
    }
  }

  /**
   * This method will take a HashMap of FillMessages passed in and determine
   * from the information it contains what the Last Sale price is.
   *
   * @param fills Fill Message passed in as argument
   * @return the last sale price
   */
  private synchronized Price determineLastSalePrice(HashMap<String, FillMessage> fills) {
    ArrayList<FillMessage> msgs = new ArrayList<>(fills.values());
    Collections.sort(msgs);
    return msgs.get(0).getPrice();
  }

  /**
   * This method will take a HashMap of FillMessages passed in and determine
   * from the information it contains what the Last Sale quantity (volume) is.
   *
   * @param fills Fill message passed in as argument
   * @return Last sale quantity
   */
  private synchronized int determineLastSaleQuantity(HashMap<String, FillMessage> fills) {
    ArrayList<FillMessage> msgs = new ArrayList<>(fills.values());
    Collections.sort(msgs);
    return msgs.get(0).getVolume();
  }

  /**
   * This method is a key part of the trading system; this method deals with the
   * addition of Tradeables to the Buy/Sell ProductSideBook and handles the
   * results of any trades as a result from that addition.
   *
   * @param side 
   * @param trd
   * @throws InvalidPriceOperation 
   * @throws MessagePublisherException 
   */
  private synchronized void addToBook(BookSide side, Tradable trd) throws InvalidMessageException, 
  	TradableException, InvalidPriceOperation, MessagePublisherException {
    if (ProductService.getInstance().getMarketState().equals(MarketState.PREOPEN)) {
      if (side.equals(BookSide.BUY)) {
        buySide.addToBook(trd);
      } else {
        sellSide.addToBook(trd);
      }
      return;
    }
    HashMap<String, FillMessage> allFills = null;
    if (side.equals(BookSide.BUY)) {
      allFills = sellSide.tryTrade(trd);
    } else {
      allFills = buySide.tryTrade(trd);
    }
    if (allFills != null && !allFills.isEmpty()) {
      updateCurrentMarket();
      int diff = trd.getOriginalVolume() - trd.getRemainingVolume();
      Price lastSalePrice = determineLastSalePrice(allFills);
      LastSalePublisher.getInstance().publishLastSale(symbol,
              lastSalePrice, diff);
    }
    if (trd.getRemainingVolume() > 0) {
      if (trd.getPrice().isMarket()) {
          MessagePublisher.getInstance().publishCancel(new CancelMessage(
                  trd.getUser(), trd.getProduct(), trd.getPrice(),
                  // is this remaining volume or cancelled volume
                  trd.getRemainingVolume(), "Canceling order with order ID: " +
                  trd.getId(), trd.getSide(), trd.getId()));
      } else {
        if (side.equals(BookSide.BUY)) {
          buySide.addToBook(trd);
        } else {
          sellSide.addToBook(trd);
        }
      }
    }
  }
}