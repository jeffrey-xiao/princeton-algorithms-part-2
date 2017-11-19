public class SeamTesting {
  public static void main (String[] args) {
    String[] smallTests = new String[] {
        "10x10.png",
        "10x12.png",
        "12x10.png",
        "3x4.png",
        "3x7.png",
        "4x6.png",
        "5x6.png",
        "6x5.png",
        "7x10.png",
        "7x3.png",
    };

    for (String test : smallTests) {
      PrintEnergy.main(new String[]{String.format("testing/%s", test)});
      PrintSeams.main(new String[]{String.format("testing/%s", test)});
    }

    String[] largeTests = new String[] {
        "chameleon.png",
        "diagonals.png",
        "HJocean.png",
        "HJoceanTransposed.png",
        "stripes.png",
    };

    for (String test : largeTests) {
      ShowEnergy.main(new String[]{String.format("testing/%s", test)});
      ShowSeams.main(new String[]{String.format("testing/%s", test)});
    }
  }
}
