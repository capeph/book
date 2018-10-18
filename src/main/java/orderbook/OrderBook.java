package orderbook;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for both sides of the orderBook
 * also handles syncronization
 */
public class OrderBook {

    /** the bid side */
    private SideBook buySide = new SideBook(Side.BUY);

    /** The ask side */
    private SideBook sellSide = new SideBook(Side.SELL);

    /** match an order against the sell side and store on the buyside */
    public List<Order> buy(Order order) {
        return matchAndAddResidual(order, sellSide, buySide);
    }

    /** match an order against the buy side and store on the selllside */
    public List<Order> sell(Order order) {
        return matchAndAddResidual(order, buySide, sellSide);
    }

    /** Perform matching and insertion */
    private synchronized List<Order> matchAndAddResidual(Order order, SideBook matchBook, SideBook noteBook) {
        MatchResult result = matchBook.match(order);
        if (result.getResidual() != null) {
            noteBook.insert(result.getResidual());
        }
        return result.getMatchedOrders();
    }

    /** get description of the top bid levels */
    public synchronized List<LevelInfo> buyInfo(int n) {
        return buySide.getTopLevels(n);
    }

    /** get description of the top ask levels */
    public synchronized List<LevelInfo> sellInfo(int n) {
        return sellSide.getTopLevels(n);
    }

}
