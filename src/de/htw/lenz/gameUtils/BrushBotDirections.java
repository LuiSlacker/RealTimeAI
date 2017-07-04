package de.htw.lenz.gameUtils;

public class BrushBotDirections {

  private static Direction[] directionsTop = {Direction.Top, Direction.Right, Direction.Top, Direction.Left};
  private static Direction[] directionsBottom = {Direction.Bottom, Direction.Right, Direction.Bottom, Direction.Left};
  private static int counter = -1;
  
  public static Direction nextDirectionToTop() {
    counter++;
    return directionsTop[counter % 4];
  }
  
  public static Direction nextDirectionToBottom() {
    counter++;
    return directionsBottom[counter % 4];
  }
}
