package orderbook;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrderTest {

    private Order order = null;

    @Before
    public void setup() {
        order = new Order(4711, 242, 0);
    }

    @Test
    public void tesGetQuantity() {
        assertEquals(242, order.getQuantity());
    }

    @Test
    public void tesGetPrice() {
        assertEquals(4711, order.getPrice());
    }

    @Test
    public void tesSeqNo() {
        assertEquals(0, order.getSeqNo());
    }


    @Test
    public void testToString() {
        assertEquals("0: 242@47.11", order.toString());
    }
}