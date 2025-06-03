package gataulagigw;
import java.util.*;

public class Trie {
	private Trienode root = new Trienode(); //Inisialisasi root Trie.
    private Map<String, String> fullDataMap = new HashMap<>();
	
    public void insert(String model, String fullRow) { //Nambah model kendaraan ke Trie, juga isi CSV-nya.
        Trienode node = root;
        for (char c : model.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new Trienode());
        } //Looping setiap karakter dalam model, buat baru kalo lum ada.
        node.isEndOfWord = true;
        node.fullData = fullRow; 
    }//Tandai akhir dari entri dan simpan data lengkap dari baris CSV.
    
    public List<String> searchByPrefix(String prefix) { //Cari semua entri yang model-nya diawali dengan prefix.
    	
        Trienode node = root;
        for (char c : prefix.toLowerCase().toCharArray()) { //Navigasi Trie berdasarkan prefix yang diberikan (huruf kecil untuk konsistensi).
            if (!node.children.containsKey(c)) {
                return Collections.emptyList();
            }
            node = node.children.get(c);
        }
        List<String> results = new ArrayList<>();
        dfs(node, new StringBuilder(prefix.toLowerCase()), results);
        return results;
    }

    private void dfs(Trienode node, StringBuilder prefix, List<String> results) { //Lanjutkan pencarian secara rekursif dari simpul yang cocok dengan prefix.
        if (node.isEndOfWord && node.fullData != null) {
            results.add(node.fullData);
        }
        for (char c : node.children.keySet()) {
            dfs(node.children.get(c), prefix.append(c), results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }
}
