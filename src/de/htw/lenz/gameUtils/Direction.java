package de.htw.lenz.gameUtils;

import java.awt.Point;
import java.util.Random;

public enum Direction {
  Top(new Point(0,-1)),
  TopRight(new Point(1,-1)),
  Right(new Point(1,0)),
  BottomRight(new Point(1,1)),
  Bottom(new Point(0,1)),
  BottomLeft(new Point(-1,1)),
  Left(new Point(-1,0)),
  TopLeft(new Point(-1,1));
  
  private Point value;
  private static final Random RANDOM = new Random();

  private Direction(Point p) {
    this.value = p;
  }
  
  public Point getValue() {
    return value;
  }
  
  public static Direction getRandom() {
    Direction[] directions = Direction.values();
    return directions[RANDOM.nextInt(directions.length)];
  }
}
