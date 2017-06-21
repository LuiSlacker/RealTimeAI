package de.htw.lenz.main;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

public class FloydWarshall {
  
  private static int INF = 9; // Integer.MAX_VALUE;
  
  private int[][] adjacencyMatrix;
  private int[][] next;
  private int n;
  
  
  public FloydWarshall(List<Point> vertices) {
    this.n = vertices.size();
    init(vertices);
    Utils.printArray2D(adjacencyMatrix);
    System.out.println();
    
    Utils.printArray2D(next);
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
    return distances;
  }
  
//  private List<Point> reconstructPath(Point start, Point end) {
//    if next[][]
//    return null;
//  }
//  
//  procedure Path(u, v)
//  if next[u][v] = null then
//      return []
//  path = [u]
//  while u ≠ v
//      u ← next[u][v]
//      path.append(u)
//  return path
  
  /**
   * generates the adjacency matrix 
   */
  private void init(List<Point> vertices) {
    initializeAdjacencyMatrix();
    initializePointerMatrix();
    setEdgesForAdjacencyAndPointerMatrix(vertices);
    
    Utils.updateRestOfSymmetricalMatrix(adjacencyMatrix);
    Utils.updateRestOfSymmetricalMatrix(next);
  }
  
  private void setEdgesForAdjacencyAndPointerMatrix(List<Point> vertices) {
    for (int i = 1; i < n; i++) {
      for (int j = 0; j < i; j++) {
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
    return Math.abs(a.x - b.x) <= 1 && Math.abs(a.y - b.y) <= 1;
  }
}
