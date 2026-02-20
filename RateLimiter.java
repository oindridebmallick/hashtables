import java.util.concurrent.*;
import java.util.*;

class TokenBucket {
    private int tokens;
    private final int capacity;
    private final int refillRatePerHour;
    private long lastRefillTime;

    public TokenBucket(int capacity) {
        this.capacity = capacity;
        this.tokens = capacity;
        this.refillRatePerHour = capacity;
        this.lastRefillTime = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest() {
        refill();
        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.currentTimeMillis();
        long elapsedMillis = now - lastRefillTime;

        int refillTokens = (int)((elapsedMillis / 3600000.0) * refillRatePerHour);

        if (refillTokens > 0) {
            tokens = Math.min(capacity, tokens + refillTokens);
            lastRefillTime = now;
        }
    }

    public int getRemainingTokens() {
        return tokens;
    }
}

public class RateLimiter {

    private ConcurrentHashMap<String, TokenBucket> clients = new ConcurrentHashMap<>();

    public boolean checkRateLimit(String clientId) {
        clients.putIfAbsent(clientId, new TokenBucket(5));
        return clients.get(clientId).allowRequest();
    }

    public static void main(String[] args) {
        RateLimiter limiter = new RateLimiter();

        for (int i = 1; i <= 7; i++) {
            boolean allowed = limiter.checkRateLimit("abc123");
            System.out.println("Request " + i + ": " + (allowed ? "Allowed" : "Denied"));
        }
    }
}