package orderbook;

import java.util.*;

public class Book {

    private OrderBook orderBook = new OrderBook();
    private List<Order> matches = new ArrayList<Order>();

    private void displayOrderBook(int n) {
        List<LevelInfo> buyInfo = orderBook.buyInfo(n);
        List<LevelInfo> sellInfo = orderBook.sellInfo(n);
        System.out.println("           BID                 ASK       level  matches");
        System.out.println("     Price      Size     Price      Size");
        Iterator<Order> mi = matches.iterator();
        for (int i = 0; i < n; i++) {
            String match = mi.hasNext() ? mi.next().toString() : "";
            System.out.println(buyInfo.get(i).toString() + sellInfo.get(i) + String.format("%5d", (i+1)) + " " + match);
        }

    }

    // "buy xxx at yyy
    private boolean processCommand(String[] command) {
        try {
            long price = (long) (Double.parseDouble(command[3]) * 100.0);
            long quantity = Long.parseLong(command[1]);
            Order order = new Order(price, quantity);
            if ("buy".equals(command[0])) {
                matches = orderBook.buy(order);
            } else if ("sell".equals(command[0])) {
                matches = orderBook.sell(order);
            } else {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private void commandLoop() {
        Scanner scanner = new Scanner(System.in);
        int displayLevels = 2;
        while(true) {
            displayOrderBook(displayLevels);
            System.out.print("> ");
            String cmd = scanner.nextLine().toLowerCase().trim();
            String[] parts = cmd.split("[ ]+");
            if ("show".equals(parts[0])) {
                displayLevels = Integer.parseInt(parts[1]);
            } else if ("quit".equals(parts[0])) {
                return;
            } else if (!processCommand(parts)) {
                System.out.println("Valid commands are:");
                System.out.println("  >buy nnn at xxx.x  - add a buy order");
                System.out.println("  >sell nnn at xxx.x  - add a sell order" );
                System.out.println("  >show n - change number of displayed levels");
                System.out.println("  >quit  - to exit application");
            }
        }
    }


    public static void main(String[] args) {
        Book book = new Book();
        book.commandLoop();
    }

}


