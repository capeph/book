package orderbook;

import java.util.ArrayList;import java.util.LinkedList;
import java.util.List;

/** Container for the result of a match operation */
public class MatchResult {

    /** all the matches */
    private LinkedList<Order> matchedOrders = new LinkedList<Order>();

    /** what is lef of the crossing order */
    private Order residual = null;

    /** total traded volume */
    private long traded = 0;

    /** add a match first to the list of matches */
    public void addMatch(Order order) {
        matchedOrders.offerFirst(order);
    }

    /** getter for residual */
    public Order getResidual() {
        return residual;
    }

    /** getter for the matches */
    public List<Order> getMatchedOrders() {
        return matchedOrders;
    }

    /** setter for the residual */
    public void setResidual(Order order) {
        this.residual = order;
    }

    /** getter for traded volume */
    public long getTraded() {
        return traded;
    }

    /** setter for traded volume */
    public void setTraded(long traded) {
        this.traded = traded;
    }




}
