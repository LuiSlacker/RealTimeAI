package de.htw.lenz.main;

public class Utils {

  public static void printArray2D(int[][] array) {
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array[i].length; j++) {
        System.out.print(array[i][j]);
      }
      System.out.println();
    }
  }
  
  /**
   * fills the 2nd triangle of a symmetrical matrix
   */
  public static void updateRestOfSymmetricalMatrix(int[][] matrix) {
    int n = matrix.length;
    for (int i = 0; i < n; i++) {
      for (int j = i+1; j < n; j++) {
        matrix[i][j] = matrix[j][i];
      }
    }
  }
}
