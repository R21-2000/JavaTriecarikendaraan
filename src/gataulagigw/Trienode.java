package gataulagigw;
import java.util.HashMap;
import java.util.Map;

public class Trienode {
    Map<Character, Trienode> children = new HashMap<>(); // Menyimpan node anak berdasarkan karakter
    boolean isEndOfWord = false; // Menandakan apakah ini akhir dari sebuah kata
    String fullData; // Menyimpan data CSV lengkap untuk node ini (jika akhir kata)
}
