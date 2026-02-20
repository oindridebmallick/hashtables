import java.util.*;

public class PlagiarismDetector {

    private Map<String, Set<String>> index = new HashMap<>();

    public void addDocument(String docId, String text) {
        List<String> ngrams = extractNGrams(text, 3);
        for (String gram : ngrams) {
            index.computeIfAbsent(gram, k -> new HashSet<>()).add(docId);
        }
    }

    public void analyze(String docId, String text) {
        List<String> ngrams = extractNGrams(text, 3);
        Map<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {
            if (index.containsKey(gram)) {
                for (String otherDoc : index.get(gram)) {
                    matchCount.put(otherDoc,
                            matchCount.getOrDefault(otherDoc, 0) + 1);
                }
            }
        }

        System.out.println("Similarity Report:");
        matchCount.forEach((doc, count) ->
                System.out.println(doc + " → " +
                        (count * 100.0 / ngrams.size()) + "%"));
    }

    private List<String> extractNGrams(String text, int n) {
        String[] words = text.split("\\s+");
        List<String> grams = new ArrayList<>();
        for (int i = 0; i <= words.length - n; i++) {
            grams.add(String.join(" ",
                    Arrays.copyOfRange(words, i, i + n)));
        }
        return grams;
    }

    public static void main(String[] args) {
        PlagiarismDetector detector = new PlagiarismDetector();

        detector.addDocument("doc1", "java is a programming language");
        detector.addDocument("doc2", "python is also a programming language");

        detector.analyze("doc3", "java is a powerful programming language");
    }
}