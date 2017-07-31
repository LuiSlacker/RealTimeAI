package de.htw.lenz.AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FloydWarshall {
  
  private static int INF = 5000;//Integer.MAX_VALUE;
  
  private double[][] adjacencyMatrix;
  private double[][] distances;
  private int[][] next;
  private int n;
  private int verticesPerRow;

  private boolean[] vertices;
  
  public FloydWarshall(boolean[] vertices, int verticesPerRow) {
    this.vertices = vertices;
    this.n = vertices.length;
    this.verticesPerRow = verticesPerRow;
    init();
    allPairsShortestPath();
    System.out.println("Done");
  }
  
  /**
   * generates the adjacency matrix 
   */
  private void init() {
    initializeAdjacencyMatrix();
    initializePointerMatrix();
    setEdgesForAdjacencyAndPointerMatrix();
  }
  
  /**
   *  initializes an empty adjacency matrix (diagonal: 0, rest Integer.MAX_VALUE) 
   */
  private void initializeAdjacencyMatrix() {
    adjacencyMatrix = new double[n][n];
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
  
  private void setEdgesForAdjacencyAndPointerMatrix() {
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (isNeighbor(i, j)) {
          next[i][j] = j;
          adjacencyMatrix[i][j] = 1;
        }
      }
    }
  }
  
  
  /**
   * calculates the shortest path between all edges of a graph
   * and updates the pointer matrix for reconstruction
   */
  private void allPairsShortestPath() {
    distances = Arrays.copyOf(adjacencyMatrix, n);
    
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
  }
  
  /**
   * reconstructs the shortest path from vertex u to v
   * @param u starting vertex
   * @param v target vertex
   * @return the shortest path from u to v
   */
  public List<Integer> reconstructPath(int u, int v) {
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
   * checks if two vertices are neighbors (4er neighborhood) 
   */
  private boolean isNeighbor(int a, int b) {
    boolean walkable = vertices[a] && vertices[b]; 
    return walkable && ((isInSameRow(a, b) && Math.abs(a-b) == 1 ) || Math.abs(a-b) == verticesPerRow);
  }
  
  /**
   * Indicates whether vertex is within same row of the grid
   */
  private boolean isInSameRow(int a, int b) {
    return (a / verticesPerRow) == (b / verticesPerRow);
  }
  
}
