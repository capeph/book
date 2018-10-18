package orderbook;

import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class SideBookTest {


    @Test
    public void testLevels() {
        SideBook buyBook = new SideBook(Side.BUY);
        buyBook.insert(new Order(100, 23));
        buyBook.insert(new Order(103, 23));
        buyBook.insert(new Order(101, 23));
        buyBook.insert(new Order(102, 23));
        buyBook.insert(new Order(100, 23));
        assertEquals(4, buyBook.getLevels().size());
        assertEquals(2, buyBook.getLevel(4).size());
    }


    @Test
    public void testLevelInfo() {
        SideBook buyBook = new SideBook(Side.BUY);
        buyBook.insert(new Order(50, 100));
        buyBook.insert(new Order(55, 200));
        buyBook.insert(new Order(50, 50));
        LevelInfo info = buyBook.getLevelInfo(2);
        assertEquals(150, info.getQuantity());
    }

    @Test
    public void testEmptyLevelInfo() {
        SideBook book = new SideBook(Side.SELL);
        List<LevelInfo> info = book.getTopLevels(3);
        assertEquals(0, info.get(2).getPrice());
        assertEquals(0, info.get(0).getQuantity());
    }

    @Test
    public void testGetTopLevelsBuy() {
        SideBook buyBook = new SideBook(Side.BUY);
        buyBook.insert(new Order(50, 100));
        buyBook.insert(new Order(55, 200));
        buyBook.insert(new Order(50, 50));
        buyBook.insert(new Order(60, 170));
        List<LevelInfo> infos = buyBook.getTopLevels(2);
        assertEquals(2, infos.size());
        Iterator<LevelInfo> ii = infos.iterator();
        LevelInfo info = ii.next();
        assertEquals(60, info.getPrice());
        assertEquals(170, info.getQuantity());
        info = ii.next();
        assertEquals(200, info.getQuantity());
    }


    @Test
    public void testGetTopLevelsSell() {
        SideBook sellBook  = new SideBook(Side.SELL);
        sellBook.insert(new Order(50, 100));
        sellBook.insert(new Order(55, 200));
        sellBook.insert(new Order(50, 50));
        sellBook.insert(new Order(60, 170));
        List<LevelInfo> infos = sellBook.getTopLevels(2);
        assertEquals(2, infos.size());
        Iterator<LevelInfo> ii = infos.iterator();
        LevelInfo info = ii.next();
        assertEquals(50, info.getPrice());
        assertEquals(150, info.getQuantity());
        info = ii.next();
        assertEquals(200, info.getQuantity());
    }

    @Test
    public void testSimpleMatch() {
        SideBook buyBook = new SideBook(Side.BUY);
        buyBook.insert(new Order(100, 23));
        MatchResult result = buyBook.match(new Order(100, 23));
        assertNull(result.getResidual());
        assertEquals(23, result.getTraded());
        assertEquals(23, result.getMatchedOrders().get(0).getQuantity());
        assertEquals(0, buyBook.getLevels().size());
    }

    @Test
    public void testPartialMatch() {
        SideBook buyBook = new SideBook(Side.BUY);
        buyBook.insert(new Order(100, 23));
        MatchResult result = buyBook.match(new Order(100, 12));
        assertNull(result.getResidual());
        assertEquals(12, result.getTraded());
        assertEquals(12, result.getMatchedOrders().get(0).getQuantity());

        assertEquals(1, buyBook.getLevels().size());
    }


    @Test
    public void testPartialMatch2() {
        SideBook buyBook = new SideBook(Side.BUY);
        buyBook.insert(new Order(100, 23));
        MatchResult result = buyBook.match(new Order(100, 45));
        assertEquals(22, result.getResidual().getQuantity());
        assertEquals(23, result.getTraded());
        assertEquals(23, result.getMatchedOrders().get(0).getQuantity());
        assertEquals(0, buyBook.getLevels().size());
    }

    @Test
    public void testMultiLevelMatchBuy() {
        SideBook buyBook = new SideBook(Side.BUY);
        buyBook.insert(new Order(100, 23));
        buyBook.insert(new Order(101, 23)); // Top of Book
        MatchResult result = buyBook.match(new Order(100, 23));
        assertEquals(23, result.getTraded());
        assertEquals(100, buyBook.getLevel(1).first().getPrice());
    }

    @Test
    public void testMultiLevelMatchBuy2() {
        SideBook buyBook = new SideBook(Side.BUY);
        buyBook.insert(new Order(100, 23));
        buyBook.insert(new Order(101, 23)); // Top of Book
        MatchResult result = buyBook.match(new Order(100, 45));
        assertEquals(45, result.getTraded());
        assertEquals(100, buyBook.getLevel(1).first().getPrice());
        assertEquals(1, buyBook.getLevel(1).first().getQuantity());
    }


    @Test
    public void testMultiLevelMatchSell() {
        SideBook buyBook = new SideBook(Side.SELL);
        buyBook.insert(new Order(100, 23));
        buyBook.insert(new Order(101, 23)); // Top of Book
        MatchResult result = buyBook.match(new Order(101, 23));
        assertEquals(23, result.getTraded());
        assertEquals(101, buyBook.getLevel(1).first().getPrice());
    }



    @Test
    public void testTimePrioBuy() {
        SideBook book = new SideBook(Side.BUY);
        book.insert(new Order(100, 23, 1));
        book.insert(new Order(100, 23, 2)); // Top of Book
        MatchResult result = book.match(new Order(100, 23, 3));
        assertEquals(23, result.getTraded());
        assertEquals(2, book.getLevel(1).first().getSeqNo());
    }

    @Test
    public void testTimePrioSell() {
        SideBook book = new SideBook(Side.SELL);
        book.insert(new Order(100, 23, 1));
        book.insert(new Order(100, 23, 2)); // Top of Book
        MatchResult result = book.match(new Order(100, 23, 3));
        assertEquals(23, result.getTraded());
        assertEquals(2, book.getLevel(1).first().getSeqNo());
    }



}