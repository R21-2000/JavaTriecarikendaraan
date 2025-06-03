package gataulagigw;
import java.util.*;

public class Trie {
	private Trienode root = new Trienode();
    private Map<String, String> fullDataMap = new HashMap<>();
	
    public void insert(String model, String fullRow) {
        Trienode node = root;
        for (char c : model.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new Trienode());
        }
        node.isEndOfWord = true;
        node.fullData = fullRow; 
    }
    
    public List<String> searchByPrefix(String prefix) {
        Trienode node = root;
        for (char c : prefix.toLowerCase().toCharArray()) {
            if (!node.children.containsKey(c)) {
                return Collections.emptyList();
            }
            node = node.children.get(c);
        }
        List<String> results = new ArrayList<>();
        dfs(node, new StringBuilder(prefix.toLowerCase()), results);
        return results;
    }

    private void dfs(Trienode node, StringBuilder prefix, List<String> results) {
        if (node.isEndOfWord && node.fullData != null) {
            results.add(node.fullData);
        }
        for (char c : node.children.keySet()) {
            dfs(node.children.get(c), prefix.append(c), results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }
}
