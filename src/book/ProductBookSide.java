package book;

import book.exceptions.OrderNotFoundException;
import book.exceptions.ProductBookSideException;
import constants.global.BookSide;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map.Entry;
import price.Price;
import price.PriceFactory;
import price.exceptions.InvalidPriceOperation;
import publishers.MessagePublisher;
import publishers.exceptions.MessagePublisherException;
import messages.CancelMessage;
import messages.FillMessage;
import messages.exceptions.InvalidMessageException;
import tradable.Tradable;
import tradable.TradableDTO;
import tradable.exceptions.TradableException;
import book.exceptions.InvalidProductBookSideValueException;

/**
 * Class that maintains the content of one side(Buy or Sell) of a Stock(product) "book".
 *
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class ProductBookSide {

  ProductBookSide self = this;

  /**
   * The "side" that this ProductBookSideBehaviors represents - BUY or SELL.
   */
  private BookSide side;

  /**
   * A HashMap<Price, ArrayList<Tradeable>> of book entries for this side.
   */
  private HashMap<Price, ArrayList<Tradable>> bookEntries = new HashMap<Price, ArrayList<Tradable>>();;

  /**
   * Holds keys with no Tradables to be removed at order/quote cancel.
   */
  private ArrayList<Price> removeBookEntryKeys = new ArrayList<>();

  /**
   * A reference to the "TradeProcessor" object which will be used to execute
   * trades against a book side.
   */
  private TradeProcessor processor;

  /**
   * A reference back to the ProductBook object that this ProductBookSideBehaviors
   * belongs to.
   */
  private ProductBook parent;

  /**
   * ProductBookSide accepts a reference to ProductBook object and creates a new
   * TradeProcessorPriceTimeImpl object
   * @param p ProductBook object passed as argument
   * @param s BookSide object passed as argument
   * @throws ProductBookSideException
   * @throws InvalidProductBookSideValueException
   */
  public ProductBookSide(ProductBook p, BookSide s) throws ProductBookSideException,
          InvalidProductBookSideValueException {
    bookEntries = new HashMap<>();
    setBookSide(s);
    setParentProductBook(p);
    processor = TradeProcessorFactory.createTradeProcessor("price-time", self);
  }

  /**
   * Sets the BookSide of the ProductBookSide constructor
   * @param s Side of the BookSide object (BUY or SELL)
   * @throws ProductBookSideException
   */
  private void setBookSide(BookSide s) throws ProductBookSideException {
    if (!(s.equals(BookSide.BUY) || s.equals(BookSide.SELL))) {
      throw new ProductBookSideException("BookSide: " + s + " is invalid!");
    }
    side = s;
  }

  /**
   * Sets the ParentProductBook to the passed in ProductBook object
   * @param p ProductBook object argument
   * @throws ProductBookSideException
   */
  private void setParentProductBook(ProductBook p) throws ProductBookSideException {
    if (p == null) {
      throw new ProductBookSideException("Parent ProductBook cannot be null!");
    }
    parent = p;
  }

  /**
   * This method will generate and return an ArrayList of TradableDTO's
   * containing information on all the orders in this ProductBookSideBehaviors that have
   * remaining quantity for the specified user.
   *
   * @param userName Name of the user passed as an argument
   * @return an ArrayList of TradeAbleDTO's
   */
  public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(String userName) {
    ArrayList<TradableDTO> l = new ArrayList<>();
    for (Entry<Price, ArrayList<Tradable>> row : bookEntries.entrySet()) {
      for (Tradable t : row.getValue()) {
        if (t.getUser().equals(userName) &&
                t.getRemainingVolume() > 0) {
          l.add(new TradableDTO(t.getProduct(), t.getPrice(), t.getOriginalVolume(),
                  t.getRemainingVolume(), t.getCancelledVolume(), t.getUser(),
                  t.getSide(), false, t.getId()));
        }
      }
    }
    return l;
  }

  /**
   * Helper method to sort prices in the bookEntries HashMap.
   *
   * @return an ArrayList of arranged Prices
   */
  private synchronized ArrayList<Price> arrangePrice() {
    ArrayList<Price> arranged = new ArrayList<>(bookEntries.keySet());
    Collections.sort(arranged);
    if (side.equals(BookSide.BUY)) {
      Collections.reverse(arranged);
    }
    return arranged;
  }

  /**
   * This method should return an ArrayList of the Tradables that are at
   * the best price in the "bookEntries" HashMap.
   *
   * @return an ArrayList of Tradables at the best price in the "bookEntries"
   * HashMap.
   */
  public synchronized ArrayList<Tradable> getEntriesAtTopOfBook() {
    if (bookEntries.isEmpty()) { 
    	return null; 
    	}
    ArrayList<Price> arranged = arrangePrice();
    return bookEntries.get(arranged.get(0));
  }

  /**
   * This method should return an array of Strings, where each index holds a
   * "Price x Volume" String.
   *
   * @return An array of Strings, where each index holds a "Price x Volume" String
   */
  public synchronized String[] getBookDepth() {
    if (bookEntries.isEmpty()) {
      return new String[]{ "<Empty>"};
    }
    ArrayList<String> str = new ArrayList<>();
    String[] s = new String[bookEntries.size()];
    ArrayList<Price> arranged = arrangePrice();
    for (Price p : arranged) {
      ArrayList<Tradable> tradable = bookEntries.get(p);
      int sum = 0;
      for (Tradable t : tradable) {
        sum += t.getRemainingVolume();
      }
      str.add(p + " x " + sum);
    }
    return str.toArray(s);
  }

  /**
   * This method should return all the Tradables in this book side at the
   * specified price.
   *
   * @param price Price object passed in as argument
   * @return an ArrayList of all Tradables at the specified price
   */
  synchronized ArrayList<Tradable> getEntriesAtPrice(Price price) {
    if (!bookEntries.containsKey(price)) { 
    	return null; 
    	}
    return bookEntries.get(price);
  }

  /**
   * This method should return true if the product book
   * (the "bookEntries" HashMap) contains a Market Price
   *
   * @return true or false if the product book contains a Market Price
   */
  public synchronized boolean hasMarketPrice() {
    return bookEntries.containsKey(PriceFactory.makeMarketPrice());
  }

  /**
   * This method should return true if the ONLY Price in this product's book is
   * a Market Price.
   *
   * @return true or false if this book contains only a Market Price
   */
  public synchronized boolean hasOnlyMarketPrice() {
    return (bookEntries.size() == 1) && bookEntries.containsKey(
            PriceFactory.makeMarketPrice());
  }

  /**
   * This method should return the best Price in the book side. If the
   * "bookEntries" HashMap is empty, then return null.
   *
   * @return return best Price in book otherwise return null
   */
  public synchronized Price topOfBookPrice() {
    if (bookEntries.isEmpty()) { 
    	return null; 
    	}
    ArrayList<Price> arranged = arrangePrice();
    return arranged.get(0);
  }

  /**
   * This method should return the volume associated with the best Price in
   * the book side. If the "bookEntries" HashMap is empty, then return zero.
   *
   * @return the volume associated with the best Price otherwise 0
   */
  public synchronized int topOfBookVolume() {
    if (bookEntries.isEmpty()) {
      return 0;
    }
    ArrayList<Price> arranged = arrangePrice();
    ArrayList<Tradable> tradables = bookEntries.get(arranged.get(0));
    int s = 0;
    for (Tradable t : tradables) {
      s += t.getRemainingVolume();
    }
    return s;
  }

  /**
   * Returns true if the product book is empty, false otherwise.
   *
   * @return true is product book is empty, false otherwise
   */
  public synchronized boolean isEmpty() {
    return bookEntries.isEmpty();
  }

  /**
   * This method should cancel every Order or QuoteSide at every price in the
   * book.
   * @throws InvalidPriceOperation 
   * @throws MessagePublisherException 
   */
  public synchronized void cancelAll() throws InvalidMessageException, OrderNotFoundException,
          TradableException, InvalidPriceOperation, MessagePublisherException {
    ArrayList<Price> prices = new ArrayList<>(bookEntries.keySet());
    HashMap<Price, ArrayList<Tradable>> bookMap = new HashMap<>(bookEntries);
    for (Price p : prices) {
      ArrayList<Tradable> bookList = new ArrayList<>(bookMap.get(p));
      for (Tradable t: bookList) {
        if (t.isQuote()) {
          submitQuoteCancel(t.getUser());
        } else {
          submitOrderCancel(t.getId());
        }
      }
    }
    removeEmptyKeys();
  }

  /**
   * This method should search the book (the HashMap) for a Quote
   * from the specified user, once found, remove the Quote from the book, and
   * create a TradableDTO using data from that QuoteSide, and return the DTO
   * from the method.
   *
   * @param user Name of the user
   * @return A TradeableDTO of the quote side if it exists otherwise return null
   */
  public synchronized TradableDTO removeQuote(String user) {
    TradableDTO quote = null;
    for (Entry<Price, ArrayList<Tradable>> row : bookEntries.entrySet()) {
      ListIterator<Tradable> iterator = row.getValue().listIterator();
      int size = row.getValue().size();
      if (size == 1) {
        removeBookEntryKeys.add(row.getKey());
      }
      while (iterator.hasNext()) {
        Tradable t = iterator.next();
        if (t.isQuote() && t.getUser().equals(user)) {
          quote = new TradableDTO(t.getProduct(), t.getPrice(), t.getOriginalVolume(),
                  t.getRemainingVolume(), t.getCancelledVolume(), t.getUser(),
                  t.getSide(), false, t.getId());
          iterator.remove();
        }
      }
    }
    return quote;
  }

  /**
   * This method should cancel the Order (if possible) that has the specified
   * identifier.
   *
   * @param orderId
   * @return an ArrayList of keys to remove from the bookEntries
   * @throws InvalidPriceOperation 
   * @throws MessagePublisherException 
   */
  public synchronized void submitOrderCancel(String orderId) throws InvalidMessageException, 
  OrderNotFoundException, TradableException, InvalidPriceOperation, MessagePublisherException {
    boolean isFound = false;
    for (Entry<Price, ArrayList<Tradable>> row : bookEntries.entrySet()) {
      ListIterator<Tradable> iterator = row.getValue().listIterator();
      int size = row.getValue().size();
      if (size == 1) {
        removeBookEntryKeys.add(row.getKey());
      }
      while (iterator.hasNext()) {
        Tradable t = iterator.next();
        if (t.getId().equals(orderId)) {
          isFound = true;
          MessagePublisher.getInstance().publishCancel(new CancelMessage(
                  t.getUser(), t.getProduct(), t.getPrice(),
                  t.getRemainingVolume(), "Canceling order with order ID: " + t.getId(), t.getSide(), t.getId()));
          addOldEntry(t);
        }
      }
    }
    if (!isFound) {
      parent.checkTooLateToCancel(orderId);
    }
  }

  /**
   * This method should cancel the QuoteSide (if possible) that has the
   * specified userName.
   *
   * @param userName Name ofuser passed as argument
 * @throws MessagePublisherException 
   */
  public synchronized void submitQuoteCancel(String userName) throws InvalidMessageException, MessagePublisherException {
    TradableDTO quote = removeQuote(userName);
    if (quote != null) {
      MessagePublisher.getInstance().publishCancel(new CancelMessage(
              quote.user, quote.product, quote.price, quote.remainingVolume,
              "Quote " + quote.side + "-Side Cancelled.", quote.side,
              quote.id));
    }
  }

  /**
   * Removes empty keys from the bookEntries HashMap.
   */
  public synchronized void removeEmptyKeys() {
    for (Price key : removeBookEntryKeys) {
      bookEntries.remove(key);
    }
    removeBookEntryKeys = new ArrayList<>();
  }

  /**
   * This method should add the Tradable passed in to the "parent" product
   * book's "old entries" list.
   *
   * @param t Tradable object passed as argument
 * @throws InvalidPriceOperation 
   */
  public void addOldEntry(Tradable t) throws TradableException, InvalidPriceOperation {
    parent.addOldEntry(t);
  }

  /**
   * This method should add the Tradable passed in to the book
   * (the "bookEntries" HashMap).
   *
   * @param trd Tradable object passed as argument
   */
  public synchronized void addToBook(Tradable trd) {
    if (bookEntries.containsKey(trd.getPrice())) {
      bookEntries.get(trd.getPrice()).add(trd);
    } else {
      ArrayList<Tradable> l = new ArrayList<>();
      l.add(trd);
      bookEntries.put(trd.getPrice(), l);
    }
  }

  /**
   * This method will attempt to trade the provided Tradable against entries in
   * this ProductBookSide.
   *
   * @param trd Tradable object passed as argument
   * @return a HashMap of filled messages
   * @throws InvalidPriceOperation 
   * @throws MessagePublisherException 
   */
  public HashMap<String, FillMessage> tryTrade(Tradable trd) throws InvalidMessageException, 
  	TradableException, InvalidPriceOperation, MessagePublisherException {
    HashMap<String, FillMessage> allFills;
    if (side.equals(BookSide.BUY)) {
      allFills = trySellAgainstBuySideTrade(trd);
    } else {
      allFills = tryBuyAgainstSellSideTrade(trd);
    }
    for (Entry<String, FillMessage> row : allFills.entrySet()) {
      MessagePublisher.getInstance().publishFill(row.getValue());
    }
    return allFills;
  }

  /**
   * This method will try to fill the SELL side Tradable passed in against the
   * content of the book.
   *
   * @param trd
   * @return a HashMap of filled messages
 * @throws InvalidPriceOperation 
   */
  public synchronized HashMap<String, FillMessage> trySellAgainstBuySideTrade(Tradable trd)
          throws InvalidMessageException, TradableException, InvalidPriceOperation {
    HashMap<String, FillMessage> allFills = new HashMap<>();
    HashMap<String, FillMessage> fillMsgs = new HashMap<>();
    while((trd.getRemainingVolume() > 0 && !bookEntries.isEmpty()) &&
            (trd.getPrice().isMarket() ||
            trd.getPrice().lessOrEqual(topOfBookPrice()))) {
      HashMap<String, FillMessage> temp = processor.doTrade(trd);
      fillMsgs = mergeFills(fillMsgs, temp);
    }
    allFills.putAll(fillMsgs);
    return allFills;
  }

  /**
   * This method will try to fill the BUY side Tradable passed in against the
   * content of the book.
   *
   * @param trd Tradable object passed as argument
   * @return a HashMap of fill messages
   * @throws InvalidPriceOperation 
   */
  public synchronized HashMap<String, FillMessage> tryBuyAgainstSellSideTrade(Tradable trd)
          throws InvalidMessageException, TradableException, InvalidPriceOperation {
    HashMap<String, FillMessage> allFills = new HashMap<>();
    HashMap<String, FillMessage> fillMsgs = new HashMap<>();
    while((trd.getRemainingVolume() > 0 && !bookEntries.isEmpty()) &&
            (trd.getPrice().isMarket() ||
            trd.getPrice().greaterOrEqual(topOfBookPrice()))) {
      HashMap<String, FillMessage> temp = processor.doTrade(trd);
      fillMsgs = mergeFills(fillMsgs, temp);
    }
    allFills.putAll(fillMsgs);
    return allFills;
  }

  /**
   * This method is designed to merge multiple fill messages together into
   * one consistent HashMap.
   *
   * @param existing
   * @param newOnes
   * @return a HashMap of fill messages
   */
  private HashMap<String, FillMessage> mergeFills(
          HashMap<String, FillMessage> existing,
          HashMap<String, FillMessage> newOnes) throws InvalidMessageException {
    if (existing.isEmpty()) {
      return new HashMap<>(newOnes);
    }
    HashMap<String, FillMessage> results = new HashMap<>(existing);
    for (String key : newOnes.keySet()) { // For each Trade Id key in the "newOnes" HashMap
      if (!existing.containsKey(key)) { // If the "existing" HashMap does not have that key...
        results.put(key, newOnes.get(key)); // ...then simply add this entry to the "results" HashMap
      } else { // Otherwise, the "existing" HashMap does have that key – we need to update the data
        FillMessage fm = results.get(key); // Get the FillMessage from the "results" HashMap
        // NOTE – for the below, you will need to make these 2 FillMessage methods "public"!
        fm.setVolume(newOnes.get(key).getVolume()); // Update the fill volume
        fm.setDetails(newOnes.get(key).getDetails()); // Update the fill details
      }
    }
    return results;
  }

  /**
   * This method will remove an key/value pair from the book (the "bookEntries"
   * HashMap) if the ArrayList associated with the Price passed in is empty.
   *
   * @param p Price object passed in as argument which is the price of the stock
   */
  public synchronized void clearIfEmpty(Price p) {
    if (bookEntries.get(p).isEmpty()) {
      bookEntries.remove(p);
    }
  }

  /**
   * This method is design to remove the Tradable passed in from the book
   * (when it has been traded or cancelled).
   *
   * @param t
   */
  public synchronized void removeTradeable(Tradable t) {
    ArrayList<Tradable> entries = bookEntries.get(t.getPrice());
    if (entries == null) { 
    	return; 
    	}
    boolean removeOp = entries.remove(t);
    if (!removeOp) { 
    	return; 
    	}
    if (entries.isEmpty()) {
      clearIfEmpty(t.getPrice());
    }
  }
}