import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;

public class BurrowsWheeler {
  private static final int RADIX = 256;

  // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
  public static void encode() {
    String s = BinaryStdIn.readString();
    CircularSuffixArray csa = new CircularSuffixArray(s);
    for (int i = 0; i < csa.length(); i++)
      if (csa.index(i) == 0)
        BinaryStdOut.write(i);
    for (int i = 0; i < csa.length(); i++)
      BinaryStdOut.write((byte)s.charAt((csa.index(i) + csa.length() - 1) % csa.length()));
    BinaryStdOut.close();
  }

  // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
  public static void decode() {
    int first = BinaryStdIn.readInt();
    String s = BinaryStdIn.readString();
    int[] count = new int[RADIX + 1];
    int[] next = new int[s.length()];
    for (int i = 0; i < s.length(); i++)
      count[s.charAt(i) + 1]++;
    for (int i = 1; i <= RADIX; i++)
      count[i] += count[i - 1];
    for (int i = 0; i < s.length(); i++)
      next[count[s.charAt(i)]++] = i;
    for (int curr = next[first], i = 0; i < s.length(); curr = next[curr], i++)
      BinaryStdOut.write(s.charAt(curr));
    BinaryStdOut.close();
  }

  // if args[0] is '-', apply Burrows-Wheeler encoding
  // if args[0] is '+', apply Burrows-Wheeler decoding
  public static void main(String[] args) {
    if (args[0].equals("-")) encode();
    else if (args[0].equals("+")) decode();
    else throw new IllegalArgumentException("Expected either '-' or '+'");
  }
}
