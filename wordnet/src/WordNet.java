import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
  private final SAP sap;
  private final HashMap<Integer, String> idToSynset;
  private final HashMap<String, ArrayList<Integer>> synsetToId;

  // constructor takes the name of the two input files
  public WordNet(String synsets, String hypernyms) {
    if (synsets == null || hypernyms == null)
      throw new IllegalArgumentException("Expected synsets and hypernyms to be non-null strings");
    idToSynset = new HashMap<>();
    synsetToId = new HashMap<>();

    In synsetIn = new In(synsets);
    In hypernymsIn = new In(hypernyms);

    while (!synsetIn.isEmpty()) {
      String[] tokens = synsetIn.readLine().split(",");
      int id = Integer.parseInt(tokens[0]);
      String synsetString = tokens[1];
      String[] synsetSet = synsetString.split(" ");
      idToSynset.put(id, synsetString);
      for (String synset : synsetSet) {
        if (!synsetToId.containsKey(synset))
          synsetToId.put(synset, new ArrayList<>());
        synsetToId.get(synset).add(id);
      }
    }


    Digraph g = new Digraph(idToSynset.size());
    while (!hypernymsIn.isEmpty()) {
      String[] tokens = hypernymsIn.readLine().split(",");
      int synset = Integer.parseInt(tokens[0]);
      for (int i = 1; i < tokens.length; i++) {
        int hypernym = Integer.parseInt(tokens[i]);
        g.addEdge(synset, hypernym);
      }
    }

    if (new DirectedCycle(g).hasCycle())
      throw new IllegalArgumentException("The input does not correspond to a rooted DAG: has a cycle");

    int roots = 0;
    for (int i = 0; i < g.V(); i++)
      if (!g.adj(i).iterator().hasNext())
        roots++;

    if (roots > 1)
      throw new IllegalArgumentException("The input does not correspond to a rooted DAG: more than one root");

    sap = new SAP(g);
  }

  // returns all WordNet nouns
  public Iterable<String> nouns() {
    return synsetToId.keySet();
  }

  public boolean isNoun(String word) {
    if (word == null)
      throw new IllegalArgumentException("Expected word to be a non-null string");
    return synsetToId.containsKey(word);
  }

  // distance between nounA and nounB
  public int distance(String nounA, String nounB) {
    if (nounA == null || nounB == null)
      throw new IllegalArgumentException("Expected nounA and nounB to be non-null strings");
    return sap.length(synsetToId.get(nounA), synsetToId.get(nounB));
  }

  // a synset that is the common ancestor of nounA and nounB in a shortest ancestral path
  public String sap(String nounA, String nounB) {
    if (nounA == null || nounB == null)
      throw new IllegalArgumentException("Expected nounA and nounB to be non-null strings");
    return idToSynset.get(sap.ancestor(synsetToId.get(nounA), synsetToId.get(nounB)));
  }

  // do unit testing of this class
  public static void main(String[] args) {
    In in = new In("testing/digraph1.txt");
    Digraph g = new Digraph(in);
    SAP sap = new SAP(g);

    assert sap.length(3, 11) == 4 && sap.ancestor(3, 11) == 1;
    assert sap.length(9, 12) == 3 && sap.ancestor(9, 12) == 5;
    assert sap.length(7, 2) == 4 && sap.ancestor(7, 2) == 0;
    assert sap.length(1, 6) == -1 && sap.ancestor(1, 6) == -1;

    WordNet wordnet = new WordNet("testing/synsets.txt", "testing/hypernyms.txt");
    assert wordnet.sap("Pericallis_cruenta", "Phlox_stellaria").equals("vascular_plant tracheophyte");

    wordnet = new WordNet("testing/synsets100-subgraph.txt", "testing/hypernyms100-subgraph.txt");
    assert wordnet.sap("IgA", "unit_cell").equals("unit building_block");
    wordnet = new WordNet("testing/synsets500-subgraph.txt", "testing/hypernyms500-subgraph.txt");
    assert wordnet.sap("cyanide_radical", "gelatin").equals("unit building_block");
    wordnet = new WordNet("testing/synsets1000-subgraph.txt", "testing/hypernyms1000-subgraph.txt");
    assert wordnet.sap("sodium_nitrite", "DEAE_cellulose").equals("unit building_block");
  }
}