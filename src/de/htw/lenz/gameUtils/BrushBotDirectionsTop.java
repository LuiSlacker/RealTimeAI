package de.htw.lenz.gameUtils;

public class BrushBotDirectionsTop {

  private static Direction[] directionsTop = {Direction.Top, Direction.Right, Direction.Top, Direction.Left};
  private static int counter = -1;
  
  public static Direction next() {
    counter++;
    return directionsTop[counter % 4];
  }
  
}
