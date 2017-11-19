import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.TreeSet;

public class BoggleSolver {
  private static final int[] POINT_CEILINGS = {2, 4, 5, 6, 7};
  private static final int[] POINTS = {0, 1, 2, 3, 5};
  private static final int MAX_POINTS = 11;
  private static final int[] MOVER = {-1, -1, -1, 0, 0, 1, 1, 1};
  private static final int[] MOVEC = {-1, 0, 1, -1, 1, -1, 0, 1};
  private final Trie root;

  // Initializes the data structure using the given array of strings as the dictionary.
  // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
  public BoggleSolver(String[] dictionary) {
    root = new Trie();
    for (String word : dictionary)
      addWord(word);
  }

  private static class Trie {
    Trie[] children;
    boolean isEnd;

    Trie() {
      children = new Trie[26];
    }

    public Trie nextState(char c) {
      return children[c - 'A'];
    }
  }

  private void addWord(String s) {
    Trie curr = root;
    for (int i = 0; i < s.length(); i++) {
      if (curr.children[s.charAt(i) - 'A'] == null)
        curr.children[s.charAt(i) - 'A'] = new Trie();
      curr = curr.children[s.charAt(i) - 'A'];
    }
    curr.isEnd = true;
  }

  private boolean isWord(String s) {
    Trie curr = root;
    for (int i = 0; i < s.length(); i++) {
      if (curr.children[s.charAt(i) - 'A'] == null)
        return false;
      curr = curr.children[s.charAt(i) - 'A'];
    }
    return curr.isEnd;
  }

  private void recur(TreeSet<String> words, StringBuilder currWord, Trie currState, BoggleBoard board, boolean[][] vis, int r, int c) {
    char currLetter = board.getLetter(r, c);

    currState = currState.nextState(currLetter);
    if (currState == null)
      return;

    if (currLetter == 'Q')
      currState = currState.nextState('U');
    if (currState == null)
      return;

    vis[r][c] = true;
    currWord.append(currLetter);
    if (currLetter == 'Q')
      currWord.append('U');

    if (scoreOf(currWord.toString()) != 0)
      words.add(currWord.toString());

    for (int i = 0; i < MOVER.length; i++) {
      int nr = r + MOVER[i];
      int nc = c + MOVEC[i];
      if (nr < 0 || nr >= board.rows() || nc < 0 || nc >= board.cols() || vis[nr][nc])
        continue;
      recur(words, currWord, currState, board, vis, nr, nc);
    }

    if (currLetter == 'Q')
      currWord.setLength(currWord.length() - 2);
    else
      currWord.setLength(currWord.length() - 1);
    vis[r][c] = false;
  }

  // Returns the set of all valid words in the given Boggle board, as an Iterable.
  public Iterable<String> getAllValidWords(BoggleBoard board) {
    TreeSet<String> words = new TreeSet<>();
    StringBuilder currWord = new StringBuilder();
    boolean[][] vis = new boolean[board.rows()][board.cols()];
    for (int r = 0; r < board.rows(); r++) {
      for (int c = 0; c < board.cols(); c++) {
        recur(words, currWord, root, board, vis, r, c);
      }
    }
    return words;
  }

  // Returns the score of the given word if it is in the dictionary, zero otherwise.
  // (You can assume the word contains only the uppercase letters A through Z.)
  public int scoreOf(String word) {
    if (!isWord(word))
      return 0;
    for (int i = 0; i < POINTS.length; i++)
      if (word.length() <= POINT_CEILINGS[i])
        return POINTS[i];
    return MAX_POINTS;
  }

  public static void main(String[] args) {
    In in = new In("testing/dictionary-algs4.txt");
    String[] dictionary = in.readAllStrings();
    BoggleSolver solver = new BoggleSolver(dictionary);
    BoggleBoard board = new BoggleBoard("testing/board-q.txt");
    int score = 0;
    for (String word : solver.getAllValidWords(board)) {
      StdOut.println(word);
      score += solver.scoreOf(word);
    }
    StdOut.println("Score = " + score);
  }
}
