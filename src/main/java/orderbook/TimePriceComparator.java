package orderbook;

import java.util.Comparator;

/** Order comparator that uses time price priority for sorting
 * (sequence numbers are used instead of time, but it is the ordering we ar after)
 */
public class TimePriceComparator implements Comparator<Order> {

    /** The basis for the ordering */
    private Side side;

    public TimePriceComparator(Side side) {
        this.side = side;
    }


    @Override
    public int compare(Order p1, Order p2) {
        if (p1.getPrice() == p2.getPrice()) {
            return Long.compare(p2.getSeqNo(), p1.getSeqNo());
        }
        if (side == Side.BUY) {
            return Long.compare(p1.getPrice(), p2.getPrice());
        }
        else {
            return Long.compare(p2.getPrice(), p1.getPrice());
        }
    }
}
