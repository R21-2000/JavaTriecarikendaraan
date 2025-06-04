package gataulagigw;
import java.util.*;

public class Trie {
    private Trienode root = new Trienode(); // Node akar dari Trie

    // Menambahkan entri baru ke Trie
    public void insert(String model, String fullRow) {
        Trienode node = root;
        for (char c : model.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new Trienode()); // Buat node baru jika belum ada
        }
        node.isEndOfWord = true; // Tandai akhir kata
        node.fullData = fullRow; // Simpan data CSV
    }

    // Cari semua entri yang cocok dengan awalan tertentu
    public List<String> searchByPrefix(String prefix) {
        Trienode node = root;
        for (char c : prefix.toLowerCase().toCharArray()) {
            if (!node.children.containsKey(c)) {
                return Collections.emptyList(); // Jika prefix tidak ditemukan, kembalikan kosong
            }
            node = node.children.get(c);
        }

        List<String> results = new ArrayList<>();
        dfs(node, new StringBuilder(prefix.toLowerCase()), results); // Cari semua hasil dari node ini
        return results;
    }

    // DFS untuk mendapatkan semua hasil dari node saat ini
    private void dfs(Trienode node, StringBuilder prefix, List<String> results) {
        if (node.isEndOfWord && node.fullData != null) {
            results.add(node.fullData);
        }
        for (char c : node.children.keySet()) {
            dfs(node.children.get(c), prefix.append(c), results); // Rekursi
            prefix.deleteCharAt(prefix.length() - 1); // Backtrack
        }
    }
}
