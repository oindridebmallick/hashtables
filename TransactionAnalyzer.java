import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;

    Transaction(int id, int amount, String merchant) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
    }
}

public class TransactionAnalyzer {

    public static List<int[]> twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                result.add(new int[]{map.get(complement), i});
            }
            map.put(nums[i], i);
        }
        return result;
    }

    public static void detectDuplicates(List<Transaction> transactions) {
        Map<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {
            String key = t.amount + "-" + t.merchant;
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(t);
        }

        for (List<Transaction> list : map.values()) {
            if (list.size() > 1) {
                System.out.println("Duplicate transactions found:");
                for (Transaction t : list) {
                    System.out.println("ID: " + t.id);
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] nums = {500, 300, 200};
        System.out.println("TwoSum Results:");
        for (int[] pair : twoSum(nums, 500)) {
            System.out.println(pair[0] + ", " + pair[1]);
        }

        List<Transaction> list = new ArrayList<>();
        list.add(new Transaction(1, 500, "StoreA"));
        list.add(new Transaction(2, 500, "StoreA"));
        list.add(new Transaction(3, 300, "StoreB"));

        detectDuplicates(list);
    }
}