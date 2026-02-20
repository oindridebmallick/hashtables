import java.util.*;

class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public LRUCache(int capacity) {
        super(16, 0.75f, true);
        this.capacity = capacity;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}

public class MultiLevelCache {

    private LRUCache<String, String> L1 = new LRUCache<>(2);
    private LRUCache<String, String> L2 = new LRUCache<>(5);

    public String getVideo(String videoId) {

        if (L1.containsKey(videoId)) {
            return "L1 HIT → " + L1.get(videoId);
        }

        if (L2.containsKey(videoId)) {
            String value = L2.get(videoId);
            L1.put(videoId, value);
            return "L2 HIT → Promoted to L1";
        }

        String value = fetchFromDB(videoId);
        L2.put(videoId, value);
        return "DB HIT → Added to L2";
    }

    private String fetchFromDB(String videoId) {
        return "VideoData_" + videoId;
    }

    public static void main(String[] args) {
        MultiLevelCache cache = new MultiLevelCache();

        System.out.println(cache.getVideo("video1"));
        System.out.println(cache.getVideo("video1"));
        System.out.println(cache.getVideo("video1"));
    }
}