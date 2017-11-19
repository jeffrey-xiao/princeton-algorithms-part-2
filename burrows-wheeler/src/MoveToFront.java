import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
  private static final int RADIX = 256;

  // apply move-to-front encoding, reading from standard input and writing to standard output
  public static void encode() {
    int[] value = new int[RADIX];
    for (int i = 0; i < RADIX; i++)
      value[i] = i;
    while (!BinaryStdIn.isEmpty()) {
      int b = BinaryStdIn.readByte() & 0xFF;
      int prev = value[0];
      int i = 0;
      for (; i < RADIX; i++) {
        if (i > 0) {
          int temp = value[i];
          value[i] = prev;
          prev = temp;
        }
        if (b == prev)
          break;
      }
      value[0] = b;
      BinaryStdOut.write((byte)i);
    }
    BinaryStdOut.close();
  }

  // apply move-to-front decoding, reading from standard input and writing to standard output
  public static void decode() {
    int[] value = new int[RADIX];
    for (int i = 0; i < RADIX; i++)
      value[i] = i;
    while (!BinaryStdIn.isEmpty()) {
      int b = BinaryStdIn.readByte() & 0xFF;
      int last = value[b];
      for (int i = b - 1; i >= 0; i--)
        value[i + 1] = value[i];
      value[0] = last;
      BinaryStdOut.write((char)last);
    }
    BinaryStdOut.close();
  }

  // if args[0] is '-', apply move-to-front encoding
  // if args[0] is '+', apply move-to-front decoding
  public static void main(String[] args) {
    if (args[0].equals("-")) encode();
    else if (args[0].equals("+")) decode();
    else throw new IllegalArgumentException("Expected either '-' or '+'");
  }
}
