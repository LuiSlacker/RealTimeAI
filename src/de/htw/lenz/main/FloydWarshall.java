package de.htw.lenz.main;

import java.util.Arrays;

public class FloydWarshall {

  public static int[][] allPairShortestPath(int[][] adjacencyMatrix) {
    int[][] distances;
    int n = adjacencyMatrix.length;
    distances = Arrays.copyOf(adjacencyMatrix, n);

    for (int k = 0; k < n; k++) {
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          distances[i][j] = Math.min(distances[i][j], distances[i][k] + distances[k][j]);
        }
      }
    }
    return distances;
  }
}
