import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryManager {

    private ConcurrentHashMap<String, AtomicInteger> stock = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<Long>> waitingList = new ConcurrentHashMap<>();

    public void addProduct(String productId, int quantity) {
        stock.put(productId, new AtomicInteger(quantity));
        waitingList.put(productId, new ConcurrentLinkedQueue<>());
    }

    public int checkStock(String productId) {
        return stock.get(productId).get();
    }

    public String purchaseItem(String productId, long userId) {
        AtomicInteger currentStock = stock.get(productId);

        while (true) {
            int available = currentStock.get();
            if (available <= 0) {
                waitingList.get(productId).add(userId);
                return "Added to waiting list";
            }
            if (currentStock.compareAndSet(available, available - 1)) {
                return "Purchase successful. Remaining: " + (available - 1);
            }
        }
    }

    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();
        manager.addProduct("IPHONE15", 2);

        System.out.println(manager.purchaseItem("IPHONE15", 101));
        System.out.println(manager.purchaseItem("IPHONE15", 102));
        System.out.println(manager.purchaseItem("IPHONE15", 103));
    }
}