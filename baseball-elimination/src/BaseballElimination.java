import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class BaseballElimination {

  private final int teams;
  private final int[][] g;
  private final String[] teamNames;
  private final HashMap<String, Integer> teamNameToId;
  private final int[] wins, losses, remaining;

  // create a baseball division from given filename in format specified below
  public BaseballElimination(String filename) {
    In in = new In(filename);
    teams = in.readInt();

    g = new int[teams][teams];
    teamNames = new String[teams];
    wins = new int[teams];
    losses = new int[teams];
    remaining = new int[teams];
    teamNameToId = new HashMap<>();

    for (int i = 0; i < teams; i++) {
      teamNames[i] = in.readString();
      teamNameToId.put(teamNames[i], i);
      wins[i] = in.readInt();
      losses[i] = in.readInt();
      remaining[i] = in.readInt();

      for (int j = 0; j < teams; j++)
        g[i][j] = in.readInt();
    }
  }

  // number of teams
  public int numberOfTeams() {
    return teams;
  }

  // all teams
  public Iterable<String> teams() {
    return Arrays.asList(teamNames);
  }

  // number of wins for given team
  public int wins(String team) {
    if (!teamNameToId.containsKey(team)) throw new IllegalArgumentException("Invalid team name");
    return wins[teamNameToId.get(team)];
  }

  // number of losses for given team
  public int losses(String team) {
    if (!teamNameToId.containsKey(team)) throw new IllegalArgumentException("Invalid team name");
    return losses[teamNameToId.get(team)];
  }

  // number of remaining games for given team
  public int remaining(String team) {
    if (!teamNameToId.containsKey(team)) throw new IllegalArgumentException("Invalid team name");
    return remaining[teamNameToId.get(team)];
  }

  // number of remaining games between team1 and team2
  public int against(String team1, String team2) {
    if (!teamNameToId.containsKey(team1)) throw new IllegalArgumentException("Invalid team1 name");
    if (!teamNameToId.containsKey(team2)) throw new IllegalArgumentException("Invalid team2 name");
    return g[teamNameToId.get(team1)][teamNameToId.get(team2)];
  }


  // is given team eliminated?
  public boolean isEliminated(String team) {
    return certificateOfElimination(team) != null;
  }

  // subset R of teams that eliminates given team; null if not eliminated
  public Iterable<String> certificateOfElimination(String team) {
    for (int i = 0; i < teams; i++) {
      if (team.equals(teamNames[i]))
        continue;
      if (wins(team) + remaining(team) < wins[i])
        return Collections.singleton(teamNames[i]);
    }

    int games = (teams) * (teams - 1) / 2;
    int totalGames = 0;
    int size = 2 + teams + games;
    int src = size - 2;
    int sink = size - 1;
    FlowNetwork fn = new FlowNetwork(size);

    for (int i = 0, k = 0; i < teams; i++) {
      for (int j = i + 1; j < teams; j++, k++) {
        fn.addEdge(new FlowEdge(src, k, g[i][j]));
        fn.addEdge(new FlowEdge(k, i + games, 1 << 30));
        fn.addEdge(new FlowEdge(k, j + games, 1 << 30));
        totalGames += g[i][j];
      }
      fn.addEdge(new FlowEdge(i + games, sink, wins(team) + remaining(team) - wins[i]));
    }

    FordFulkerson ff = new FordFulkerson(fn, src, sink);
    if ((int)ff.value() == totalGames)
      return null;

    ArrayList<String> r = new ArrayList<>();
    for (int i = 0; i < teams; i++)
      if (ff.inCut(i + games))
        r.add(teamNames[i]);
    return r;
  }

  public static void main(String[] args) {
    BaseballElimination division = new BaseballElimination("testing/teams5.txt");
    for (String team : division.teams()) {
      if (division.isEliminated(team)) {
        StdOut.print(team + " is eliminated by the subset R = { ");
        for (String t : division.certificateOfElimination(team)) {
          StdOut.print(t + " ");
        }
        StdOut.println("}");
      }
      else {
        StdOut.println(team + " is not eliminated");
      }
    }
  }
}
