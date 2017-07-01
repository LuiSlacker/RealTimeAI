package de.htw.lenz.gameUtils;

public class GameUtils {
  
  public static Direction getDirectionforSuccessiveVertex(int a, int b) {
    //TODO change hardcoded Top, Bottom width
    Direction result = null;
    switch (a-b) {
    case -32:
      result = Direction.Bottom;
      break;
    case 32:
      result = Direction.Top;
      break;
    case -1:
      result = Direction.Right;
      break;
    case 1:
      result = Direction.Left;
      break;
    }
    return result;
  }
  
  /**
   * Maps pixelCoordinates to gridIndex
   */
  public static int mapCordinatesToGridIndex(int x, int y, int gridKernelLength, int gridWidth) {
    int gridX = x / gridKernelLength;
    int gridY = y / gridKernelLength;
    return gridY * gridWidth  + gridX;
  }
  
}
