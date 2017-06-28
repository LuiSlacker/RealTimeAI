package de.htw.lenz.main;

import javax.sound.midi.Synthesizer;

public class Utils {

  public static void printArray2D(double[][] array) {
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array[i].length; j++) {
        System.out.printf(" %s ", array[i][j]);
      }
      System.out.println();
    }
  }
  
  public static void printIntArray2D(int[][] array) {
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array[i].length; j++) {
        System.out.print(array[i][j]);
      }
      System.out.println();
    }
  }
  
  public static void printBooleanGrid(boolean[] array, int width) {
    for (int i = 0; i < array.length; i++) {
      if (i % width == 0) System.out.println();
        System.out.printf(" %s ", array[i]);
    }
    System.out.println();
    System.out.println();
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
