package orderbook;

import java.util.*;

/** Single side of the order book
 * (simple implementation based on a single Treeset to store the whole side)
 */
public class SideBook {

    /** Container for the orders */
    private final TreeSet<Order> orders;


    public SideBook(Side side) {
        orders = new TreeSet<Order>(new TimePriceComparator(side));
    }

    /** insert an order without matching */
    public  void insert(Order order) {
        orders.add(order);
    }

    /** match an order against the order book */
     public MatchResult match(Order order) {
        Order searchOrder = new Order(order.getPrice(), order.getQuantity(), Long.MAX_VALUE);
        MatchResult result = new MatchResult();
        TreeSet<Order> matched = (TreeSet<Order>) orders.tailSet(searchOrder);
        long remaining = order.getQuantity();
        Iterator<Order> it = matched.descendingIterator();
        Order lastMatch;
        while (it.hasNext() && remaining > 0) {
            lastMatch = it.next();
            if (lastMatch.getQuantity() <= remaining) {
                it.remove();
                result.addMatch(new Order(order.getPrice(), lastMatch.getQuantity(), lastMatch.getSeqNo()));
                remaining -= lastMatch.getQuantity();
            } else { // partial match
                lastMatch.setQuantity(lastMatch.getQuantity() - remaining);
                result.addMatch(new Order(order.getPrice(), remaining, lastMatch.getSeqNo()));
                remaining = 0L;
            }
        }
        if (remaining > 0) {
            result.setResidual(new Order(searchOrder.getPrice(), remaining, order.getSeqNo()));
        }
        if (remaining < order.getQuantity()) {
            long traded = order.getQuantity() - remaining;
            result.setTraded(traded);
            result.addMatch(new Order(searchOrder.getPrice(), traded, order.getSeqNo()));
        }
        return result;
    }


    /**
     * Expensive way of getting the number of levels.
     * can be speeded up by having a set that is updated for each insertion/removal/match
     */
    public  List<Long> getLevels() {
        List<Long> levels = new ArrayList<Long>();
        long last = -1;
        for (Order o : orders) {
            if (o.getPrice() != last) {
                levels.add(o.getPrice());
                last = o.getPrice();
            }
        }
        return levels;
    }

    /** Get orders on a given level */
    public  SortedSet<Order> getLevel(int level) {
        List<Long> levels = getLevels();
        int available = levels.size();
        if (level > available) {
            return Collections.emptySortedSet();
        }
        Order start = new Order(levels.get(available - level), 0, 0);
        Order end = new Order(levels.get(available - level), 0, Long.MAX_VALUE);
        return orders.subSet(end, true, start, true);
    }

    /** get description of a price level */
    public  LevelInfo getLevelInfo(int level) {
        SortedSet<Order> orderSet = getLevel(level);
        if (orderSet.isEmpty()) {
            return null;
        } else {
            long price = orderSet.first().getPrice();
            long totalQuantity = 0;
            for (Order order : orderSet) {
                totalQuantity += order.getQuantity();
            }
            return new LevelInfo(price, totalQuantity);
        }
    }

    /** get descriptions for the top levels */
    public  List<LevelInfo> getTopLevels(int no) {
        List<LevelInfo> result = new ArrayList<LevelInfo>(no);
        for (int i = 1; i <= no; i++) {
            LevelInfo info = getLevelInfo(i);
            if (info == null) {
                result.add(new LevelInfo());
            } else {
                result.add(info);
            }
        }
        return result;
    }

}
