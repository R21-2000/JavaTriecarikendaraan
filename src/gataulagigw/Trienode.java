package gataulagigw;
import java.util.HashMap;
import java.util.Map;

public class Trienode {
	Map<Character, Trienode> children = new HashMap<>();
	boolean isEndOfWord = false;
	String fullData;
}
