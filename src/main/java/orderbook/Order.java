package orderbook;

import java.util.concurrent.atomic.AtomicLong;

/** simple abstraction of an order */
public class Order {

    /** id of order */
    private long id;

    /** price in cents */
    private long price;

    /** number of shares */
    private  long quantity;

    /** unique sequence number for order */
    private final long seqNo;

    /** default generator for sequence numbers */
    private static AtomicLong seqNoGenerator = new AtomicLong(0);

    /** default constructor */
    public Order(long price, long quantity) {
        this.price = price;
        this.quantity = quantity;
        this.seqNo = seqNoGenerator.getAndIncrement();
    }

    /** special constructor for searching (and testing) */
    public Order(long price, long quantity, long seqNo) {
        this.price = price;
        this.quantity = quantity;
        this.seqNo = seqNo;
    }

    @Override
    public String toString() {
        return seqNo + ": " + quantity + "@" + String.format("%.2f", price/100.0);
    }

    /** getter for quantity */
    public long getQuantity() {
        return quantity;
    }

    /** getter for price */
    public long getPrice() {
        return price;
    }

    /** setter for quantity */
    public void setQuantity(long newQuantity) {
        this.quantity = newQuantity;
    }

    /** getter for sequence number */
    public long getSeqNo() {
        return seqNo;
    }
}
