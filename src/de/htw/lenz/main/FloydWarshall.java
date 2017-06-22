package de.htw.lenz.main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FloydWarshall {
  
  private static int INF = 9999;
  
  private int[][] adjacencyMatrix;
  private int[][] next;
  private int n;
  
  
  public FloydWarshall(List<Point> vertices) {
    System.out.println(vertices);
    this.n = vertices.size();
    init(vertices);
    Utils.printArray2D(adjacencyMatrix);
    System.out.println();
  }
  
  public int[][] allPairsShortestPath() {
    int[][] distances = Arrays.copyOf(adjacencyMatrix, n);
    
    for (int k = 0; k < n; k++) {
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          if (distances[i][k] + distances[k][j] < distances[i][j]) {
            distances[i][j] = distances[i][k] + distances[k][j];
            next[i][j] = next[i][k];
          }
        }
      }
    }
    Utils.printArray2D(next);
    System.out.println();
    
    System.out.println(reconstructPath(7, 2));System.out.println();
    return distances;
  }
  
  /**
   * reconstruct the shortest path from vertex u to v
   * @param u starting vertex
   * @param v target vertex
   * @return the shortest path from u to v
   */
  private List<Integer> reconstructPath(int u, int v) {
    List<Integer> path = new ArrayList<>();
    if (next[u][v] == INF) return path;
    path.add(u);
    while(u != v) {
      u = next[u][v];
      path.add(u);
    }
    return path;
  }
  
  /**
   * generates the adjacency matrix 
   */
  private void init(List<Point> vertices) {
    initializeAdjacencyMatrix();
    initializePointerMatrix();
    setEdgesForAdjacencyAndPointerMatrix(vertices);
  }
  
  private void setEdgesForAdjacencyAndPointerMatrix(List<Point> vertices) {
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (isNeighbour(vertices.get(i), vertices.get(j))) {
         adjacencyMatrix[i][j] = 1; 
         next[i][j] = j;
        }
      }
    }
  }
  
  /**
   *  initializes an empty adjacency matrix (diagonal: 0, rest Integer.MAX_VALUE) 
   */
  private void initializeAdjacencyMatrix() {
    adjacencyMatrix = new int[n][n];
    for (int y = 0; y < n; y++) {
      for (int x = 0; x < n; x++) {
        if (x != y) adjacencyMatrix[x][y] = INF;
      }
    }
  }
  
  private void initializePointerMatrix() {
    next = new int[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < adjacencyMatrix.length; j++) {
        next[i][j] = INF;
      }
    }
  }
  
  /**
   * Checks whether two pixels are neighbors (8er kernel)
   */
  private boolean isNeighbour(Point a, Point b) {
    return Math.abs(a.x - b.x) <= 1 && Math.abs(a.y - b.y) <= 1 && !a.equals(b);
  }
}
