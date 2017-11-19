import edu.princeton.cs.algs4.Digraph;

import java.util.*;

public class SAP {
  private final Digraph g;

  // constructor takes a digraph (not necessarily a DAG)
  public SAP(Digraph g) {
    this.g = new Digraph(g);
  }

  private State bfs(Iterable<Integer> vs, Iterable<Integer> ws) {
    if (vs == null || ws == null) throw new IllegalArgumentException("Expected non-null vs, ws");
    for (int v : vs) if (v < 0 || v >= g.V()) throw new IllegalArgumentException("v must be [0, V-1]");
    for (int w : ws) if (w < 0 || w >= g.V()) throw new IllegalArgumentException("v must be [0, V-1]");

    HashMap<Integer, Integer> idToDistance = new HashMap<>();
    HashSet<Integer> vis = new HashSet<>();
    Queue<State> q = new ArrayDeque<>();
    State ret = new State(-1, -1);

    for (int v : vs) {
      q.offer(new State(v, 0));
      idToDistance.put(v, 0);
    }

    while (!q.isEmpty()) {
      State curr = q.poll();
      for (int next : g.adj(curr.id)) {
        if (idToDistance.containsKey(next))
          continue;
        idToDistance.put(next, curr.dist + 1);
        q.offer(new State(next, curr.dist + 1));
      }
    }

    for (int w : ws) {
      q.offer(new State(w, 0));
      vis.add(w);
    }

    while (!q.isEmpty()) {
      State curr = q.poll();

      if (idToDistance.containsKey(curr.id)) {
        int newDistance = idToDistance.get(curr.id) + curr.dist;
        if (ret.dist == -1 || (ret.dist > newDistance))
          ret = new State(curr.id, newDistance);
      }

      for (int next : g.adj(curr.id)) {
        if (vis.contains(next))
          continue;
        vis.add(next);
        q.offer(new State(next, curr.dist + 1));
      }
    }
    return ret;
  }

  // length of shortest ancestral path between v and w; -1 if no such path
  public int length(int v, int w) {
    Iterable<Integer> vs = Collections.singletonList(v);
    Iterable<Integer> ws = Collections.singletonList(w);
    return length(vs, ws);
  }

  // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
  public int ancestor(int v, int w) {
    Iterable<Integer> vs = Collections.singletonList(v);
    Iterable<Integer> ws = Collections.singletonList(w);
    return ancestor(vs, ws);
  }

  // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
  public int length(Iterable<Integer> vs, Iterable<Integer> ws) {
    return bfs(vs, ws).dist;
  }

  // a common ancestor that participates in shortest ancestral path; -1 if no such path
  public int ancestor(Iterable<Integer> vs, Iterable<Integer> ws) {
    return bfs(vs, ws).id;
  }

  private static class State {
    int id, dist;

    State(int id, int dist) {
      this.id = id;
      this.dist = dist;
    }
  }
}