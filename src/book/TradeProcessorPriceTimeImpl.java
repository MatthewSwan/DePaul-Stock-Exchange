package book;

import java.util.ArrayList;
import java.util.HashMap;

import price.Price;
import price.exceptions.InvalidPriceOperation;
import messages.FillMessage;
import messages.exceptions.InvalidMessageException;
import tradable.Tradable;
import tradable.exceptions.TradableException;
import book.exceptions.InvalidProductBookSideValueException;

/**
 * The TradeProcessorPriceTimeImpl interface is the interface that a class that
 * will act as a trading processor needs to implement. This Tradeprocessor
 * implementer contains the functionality needed to "execute" actual trades
 * between Tradable objects in this book side using a price-time algorithm
 * (orders are traded in order of price, then in order of arrival).
 *
 * @author Matthew Swan, Nithyanandh Mahalingam, Duely Yung
 */
public class TradeProcessorPriceTimeImpl implements TradeProcessor {

  /**
   * These objects will need to keep track of fill messages, so a HashMap is
   * needed indexed by trade identifier.
   */
  private HashMap<String, FillMessage> fillMessages = new HashMap<String, FillMessage>();

  /**
   * A TradeProcessorPriceTimeImpl needs to maintain a reference to the
   * ProductBookSide that this object belongs to, so you need a ProductBookSide
   * data member to refer to the book side this object belongs to.
   */
  ProductBookSide parent;

  /**
   * This constructor accepts a ProductBookSide parameter - a reference
   * to the book side this TradeProcessor belongs to. 
   * @param pbs ProductBookSide object passed as argument 
   * @throws InvalidProductBookSideValueException
   */
  public TradeProcessorPriceTimeImpl(ProductBookSide pbs) throws InvalidProductBookSideValueException {
    setProductBookSide(pbs);
  }

  /**
   * Sets the ProductBookSide 
   * @param pbs ProductBookSide object passed as argument setting the ProductBookSide
   * @throws InvalidProductBookSideValueException
   */
  private void setProductBookSide(ProductBookSide pbs) throws InvalidProductBookSideValueException {
    if (pbs == null) {
      throw new InvalidProductBookSideValueException("ProductBookSide can't be null.");
    }
    parent = pbs;
  }

  /**
   * This method will be used at various times when executing a trade.
   * All trades result in Fill Messages.
   *
   * @param fm FillMessage object passed as argument setting the FillKey
   * @return a String key
   */
  private String makeFillKey(FillMessage fm) {
    return fm.getUser() + fm.getID() + fm.getPrice();
  }

  /**
   * This Boolean method checks the content of the "fillMessages" HashMap to
   * see if the FillMessage passed in is a fill message for an existing known
   * trade or if it is for a new previously unrecorded trade.
   *
   * @param fm FillMessage object passed as argument 
   * @return returns true or false based on whether the fill message is new
   * or new
   */
  private boolean isNewFill(FillMessage fm) {
    String key = makeFillKey(fm);
    if (fillMessages.containsKey(key)) { 
    	return true; 
    	}
    FillMessage oldFill = fillMessages.get(key);
    if (oldFill == null) { 
    	return true; 
    	}
    if (!oldFill.getSide().equals(fm.getSide())) { 
    	return true; 
    	}
    if (!oldFill.getID().equals(fm.getID())) { 
    	return true; 
    	}
    return false;
  }

  /**
   * This method should add a FillMessage either to the "fillMessages" HashMap
   * if it is a new trade, or should update an existing fill message is another
   * part of an existing trade.
   *
   * @param fm FillMessage object passed as argument
   */
  private void addFillMessage(FillMessage fm) throws InvalidMessageException {
    if (isNewFill(fm)) {
      String key = makeFillKey(fm);
      fillMessages.put(key, fm);
    } else {
      String key = makeFillKey(fm);
      FillMessage oldFill = fillMessages.get(key);
      oldFill.setVolume(fm.getVolume());
      oldFill.setDetails(fm.getDetails());
    }
  }

  /**
   * This TradeProcessor method will be called when it has been determined that
   * a Tradable (i.e., a Buy Order, a Sell QuoteSide, etc.) can trade against
   * the content of the book. The return value from this function will be a
   * HashMap<String, FillMessage> containing String trade identifiers (the key)
   * and a Fill Message object (the value).
   *
   * @param trd Tradable object passed as argument
   * @return HashMap<String, FillMessage> containing String trade identifiers
   * (the key) and a Fill Message object (the value)
   * @throws InvalidPriceOperation 
   */
  @Override
  public HashMap<String, FillMessage> doTrade(Tradable trd) throws InvalidMessageException, TradableException, 
  	InvalidPriceOperation {
    fillMessages = new HashMap<>();
    ArrayList<Tradable> tradedOut = new ArrayList<>();
    ArrayList<Tradable> entriesAtPrice = parent.getEntriesAtTopOfBook();
    for (Tradable t : entriesAtPrice) {
      if (trd.getRemainingVolume() != 0) {
        if (trd.getRemainingVolume() >= t.getRemainingVolume()) {
          tradedOut.add(t);
          Price tPrice;
          if (t.getPrice().isMarket()) {
            tPrice = trd.getPrice();
          } else {
            tPrice = t.getPrice();
          }
          FillMessage tFill = new FillMessage(t.getUser(), t.getProduct(),
                  tPrice, t.getRemainingVolume(), "leaving " + 0 , t.getSide(),
                  t.getId());
          addFillMessage(tFill);
          FillMessage trdFill = new FillMessage (trd.getUser(), t.getProduct(),
                  tPrice, t.getRemainingVolume(), "leaving " +
                  (trd.getRemainingVolume() - t.getRemainingVolume()),
                  trd.getSide(), trd.getId());
          addFillMessage(trdFill);
          trd.setRemainingVolume(trd.getRemainingVolume() - t.getRemainingVolume());
          t.setRemainingVolume(0);
          parent.addOldEntry(t);
        } 
        else {
          int remainder = t.getRemainingVolume() - trd.getRemainingVolume();
          Price tPrice;
          if (t.getPrice().isMarket()) {
            tPrice = trd.getPrice();
          } else {
            tPrice = t.getPrice();
          }
          FillMessage tFill = new FillMessage(t.getUser(), t.getProduct(),
                  tPrice, trd.getRemainingVolume(), "leaving " +
                  remainder, t.getSide(), t.getId());
          addFillMessage(tFill);
          FillMessage trdFill = new FillMessage(trd.getUser(), t.getProduct(),
                  tPrice, trd.getRemainingVolume(),
                  "leaving " + 0, trd.getSide(), trd.getId());
          addFillMessage(trdFill);
          trd.setRemainingVolume(0);
          t.setRemainingVolume(remainder);
          parent.addOldEntry(trd);
        }
      } else {
        break;
      }
    }
    for (Tradable t : tradedOut) {
      entriesAtPrice.remove(t);
    }
    if (entriesAtPrice.isEmpty()) {
      parent.clearIfEmpty(parent.topOfBookPrice());
    }
    return fillMessages;
  }
}