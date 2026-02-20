import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isWord;
    int frequency;
}

public class AutocompleteSystem {

    private TrieNode root = new TrieNode();

    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.isWord = true;
        node.frequency++;
    }

    public List<String> search(String prefix) {
        TrieNode node = root;

        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) return Collections.emptyList();
            node = node.children.get(c);
        }

        List<String> results = new ArrayList<>();
        dfs(node, new StringBuilder(prefix), results);

        results.sort((a, b) -> getFrequency(b) - getFrequency(a));

        return results.subList(0, Math.min(5, results.size()));
    }

    private void dfs(TrieNode node, StringBuilder path, List<String> results) {
        if (node.isWord) {
            results.add(path.toString());
        }

        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            path.append(entry.getKey());
            dfs(entry.getValue(), path, results);
            path.deleteCharAt(path.length() - 1);
        }
    }

    private int getFrequency(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children.get(c);
        }
        return node.frequency;
    }

    public static void main(String[] args) {
        AutocompleteSystem system = new AutocompleteSystem();

        system.insert("java");
        system.insert("javascript");
        system.insert("java");
        system.insert("javadoc");
        system.insert("python");

        System.out.println("Suggestions for 'jav': " + system.search("jav"));
    }
}