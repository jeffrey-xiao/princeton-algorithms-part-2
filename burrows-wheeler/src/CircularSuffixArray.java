import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
  private static final int RADIX = 256;
  private final String s;
  private final int[] suffixArray;

  public CircularSuffixArray(String s) {
    if (s == null)
      throw new IllegalArgumentException("Expected non-null string");
    this.s = s;

    suffixArray = new int[s.length()];
    for (int i = 0; i < s.length(); i++)
      suffixArray[i] = i;
    sort(s, 0, s.length() - 1, 0);
  }

  private char charAt(String s, int i, int d) {
    return s.charAt((i + d) % s.length());
  }

  private void swap(int[] a, int i, int j) {
    int temp = a[i];
    a[i] = a[j];
    a[j] = temp;
  }

  private void sort(String s, int lo, int hi, int d) {
    if (hi <= lo || d >= s.length())
      return;
    // [lo, lt) < pivot
    // [lt, gt] = pivot
    // (gt, hi] > pivot
    int lt = lo, gt = hi, pivot = charAt(s, suffixArray[lo], d), eq = lo + 1;

    while (eq <= gt) {
      int curr = charAt(s, suffixArray[eq], d);
      if (curr < pivot)
        swap(suffixArray, lt++, eq++);
      else if (curr > pivot)
        swap(suffixArray, eq, gt--);
      else
        eq++;
    }
    sort(s, lo, lt - 1, d);
    sort(s, lt, gt, d + 1);
    sort(s, gt + 1, hi, d);

  }

  // length of s
  public int length() {
    return s.length();
  }

  // returns index of ith sorted suffix
  public int index(int x) {
    if (x < 0 || x >= length())
      throw new IllegalArgumentException("Expected index in range [0, length - 1]");
    return suffixArray[x];
  }

  public static void main(String[] args) {
    String s = "ABRACADABRA!";
    CircularSuffixArray csa = new CircularSuffixArray(s);
    s += s;
    for (int i = 0; i < csa.length(); i++) {
      System.out.println(s.substring(csa.index(i), csa.index(i) + csa.length()));
    }
  }
}
