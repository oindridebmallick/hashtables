import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class UsernameService {

    private ConcurrentHashMap<String, Long> usernameMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, AtomicInteger> attemptCount = new ConcurrentHashMap<>();

    public boolean checkAvailability(String username) {
        attemptCount.putIfAbsent(username, new AtomicInteger(0));
        attemptCount.get(username).incrementAndGet();
        return !usernameMap.containsKey(username);
    }

    public void register(String username, long userId) {
        usernameMap.put(username, userId);
    }

    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String suggestion = username + i;
            if (!usernameMap.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }
        suggestions.add(username.replace("_", "."));
        return suggestions;
    }

    public String getMostAttempted() {
        return attemptCount.entrySet()
                .stream()
                .max(Comparator.comparingInt(e -> e.getValue().get()))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public static void main(String[] args) {
        UsernameService service = new UsernameService();

        service.register("john_doe", 1);

        System.out.println("john_doe available? " + service.checkAvailability("john_doe"));
        System.out.println("jane_smith available? " + service.checkAvailability("jane_smith"));

        System.out.println("Suggestions for john_doe: " + service.suggestAlternatives("john_doe"));
        System.out.println("Most attempted: " + service.getMostAttempted());
    }
}
