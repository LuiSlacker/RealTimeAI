package de.htw.lenz.gameUtils;

public class BrushBotDirectionsBottom {

  private static Direction[] directionsBottom = {Direction.BottomRight, Direction.Right, Direction.BottomLeft, Direction.Left};
  private static int counter = -1;
  
  
  public static Direction next() {
    counter++;
    return directionsBottom[counter % 4];
  }
  
}
