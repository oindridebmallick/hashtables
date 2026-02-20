import java.util.*;

public class TwoSum {

    public static List<int[]> findTwoSum(int[] nums, int target) {
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

    public static void main(String[] args) {
        int[] transactions = {500, 300, 200};

        List<int[]> pairs = findTwoSum(transactions, 500);

        for (int[] pair : pairs) {
            System.out.println("Pair: " + pair[0] + ", " + pair[1]);
        }
    }
}