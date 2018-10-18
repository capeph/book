package orderbook;

import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class BookTest {

    @Test
    public void testTrade() {
        Book book = new Book();
        book.buy(new Order(100, 100, 1));
        book.buy(new Order(100, 50, 2));
        book.buy(new Order(101, 120, 3));
        List<Order> trades = book.sell(new Order(100, 200, 4));
        assertEquals(3, trades.size());
        Iterator<Order> mi = trades.iterator();
        // cross
        Order match = mi.next();
        assertEquals(4, match.getSeqNo());
        assertEquals(200, match.getQuantity());
        // last match
        match = mi.next();
        assertEquals(1, match.getSeqNo());
        assertEquals(80, match.getQuantity());
        assertEquals(100, match.getPrice());
        // first match
        match = mi.next();
        assertEquals(3, match.getSeqNo());
        assertEquals(120, match.getQuantity());
        assertEquals(100, match.getPrice());
    }

    @Test
    public void testNoTrades() {
        Book book = new Book();
        assertTrue(book.buy(new Order(100, 50, 2)).isEmpty());
        assertTrue(book.sell(new Order(102, 100, 1)).isEmpty());
        assertTrue(book.buy(new Order(101, 120, 3)).isEmpty());
        assertTrue(book.sell(new Order(103, 50, 2)).isEmpty());
        assertTrue(book.buy(new Order(100, 100, 1)).isEmpty());
        assertTrue(book.sell(new Order(102, 120, 3)).isEmpty());

    }


}