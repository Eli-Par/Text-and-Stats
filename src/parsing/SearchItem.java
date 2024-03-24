package parsing;
import org.apache.commons.text.similarity.*;

public class SearchItem implements Comparable<SearchItem>{
    private String word;
    private int frequency;
    private int distance;

    private static final LevenshteinDetailedDistance DistanceFinder = new LevenshteinDetailedDistance();

    public SearchItem(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public void calculateDistance(String input) {
        if(input.equals(word.toLowerCase())) {
            distance = -1;
        }

        LevenshteinResults results = DistanceFinder.apply(input, word.toLowerCase());
        
        distance = results.getInsertCount() + results.getSubstituteCount() * 2 + results.getDeleteCount() * 3;
    }

    @Override
    public String toString() {
        return "'" + word + "' appears " + frequency + " times\n";
    }

    @Override
    public int compareTo(SearchItem o) {
        return distance - o.distance;
    }
}
