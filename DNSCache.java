import java.util.*;

public class DNSCache {

    static class DNSEntry {
        String ip;
        long expiryTime;

        DNSEntry(String ip, long ttlSeconds) {
            this.ip = ip;
            this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    private final int MAX_SIZE = 3;

    private LinkedHashMap<String, DNSEntry> cache =
            new LinkedHashMap<>(16, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                    return size() > MAX_SIZE;
                }
            };

    public synchronized String resolve(String domain) {
        if (cache.containsKey(domain)) {
            DNSEntry entry = cache.get(domain);
            if (!entry.isExpired()) {
                return "Cache HIT → " + entry.ip;
            }
        }

        String ip = "1.2.3.4"; // simulate upstream
        cache.put(domain, new DNSEntry(ip, 5));
        return "Cache MISS → " + ip;
    }

    public static void main(String[] args) throws InterruptedException {
        DNSCache dns = new DNSCache();

        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));

        Thread.sleep(6000);

        System.out.println(dns.resolve("google.com"));
    }
}