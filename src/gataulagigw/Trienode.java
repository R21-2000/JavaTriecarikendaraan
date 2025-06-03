package gataulagigw;
import java.util.HashMap;
import java.util.Map;

public class Trienode {
	Map<Character, Trienode> children = new HashMap<>(); //Menyimpan semua cabang (karakter berikutnya) dari node ini.

	boolean isEndOfWord = false;
	String fullData;
}
