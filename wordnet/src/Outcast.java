import edu.princeton.cs.algs4.In;

public class Outcast {
  private final WordNet wordnet;

  public Outcast(WordNet wordnet) {
    this.wordnet = wordnet;
  }

  public String outcast(String[] nouns) {
    int max = -1;
    String noun = "";
    for (int i = 0; i < nouns.length; i++) {
      int curr = 0;
      for (int j = 0; j < nouns.length; j++) {
        curr += wordnet.distance(nouns[i], nouns[j]);
      }
      if (curr > max) {
        max = curr;
        noun = nouns[i];
      }
    }
    return noun;
  }

  public static void main(String[] args) {
    WordNet wordnet = new WordNet("testing/synsets.txt", "testing/hypernyms.txt");
    Outcast outcast = new Outcast(wordnet);
    assert outcast.outcast(new In("testing/outcast5.txt").readAllStrings()).equals("table");
    assert outcast.outcast(new In("testing/outcast8.txt").readAllStrings()).equals("bed");
    assert outcast.outcast(new In("testing/outcast11.txt").readAllStrings()).equals("potato");
  }
}