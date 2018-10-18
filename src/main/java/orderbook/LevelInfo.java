package orderbook;

/** description of a level in the order book */
public class LevelInfo {

    /** price for the level */
    private final long price;

    /** total quantity */
    private final long quantity;

    public LevelInfo(long price, long quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    /** special constructor for empty level */
    public LevelInfo() {
        this.price = 0;
        this.quantity = 0;
    }

    /** getter for the price */
    public long getPrice() {
        return price;
    }

    /** getter for the quantity */
    public long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        if (quantity == 0) {
            return "                    ";
        } else {
            return String.format("%10.2f", price/100.0) + String.format("%10d", quantity);
        }
    }

}
