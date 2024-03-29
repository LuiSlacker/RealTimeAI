package de.htw.lenz.gameUtils;

import java.awt.Point;

import lenz.htw.kipifub.net.NetworkClient;

public class ColorGridCell implements Comparable<ColorGridCell> {
  
  private NetworkClient networkClient;
  private int colorValue;
  private int gridKernelLength;
  private int player;
  private int x;
  private int y;

  public ColorGridCell(NetworkClient networkClient, int player, int gridKernelLength, int x, int y) {
    this.networkClient = networkClient;
    this.player = player;
    this.gridKernelLength = gridKernelLength;
    this.x = x;
    this.y = y;
    updateColorGridCell();
  }
  
  
  /**
   * Updates the game-specific value for one ColorGridCell
   */
  public void updateColorGridCell() {
    colorValue = 0;
    for (int i = y * gridKernelLength; i < (y * gridKernelLength) + gridKernelLength; i++) {
      for (int j = x * gridKernelLength; j < (x * gridKernelLength) + gridKernelLength; j++) {
        if (networkClient.isWalkable(i, j)) {
          int color = networkClient.getBoard(i, j);
          colorValue += extractColorValue(color);
        } else {
          colorValue -= gridKernelLength;
        }
      }
    }
  }
  
  /**
   * Extract a game-specific color value out of a given RGB-color value
   */
  private int extractColorValue(int color) {
    int[] rgb = {color & 255, (color >> 8) & 255, (color >> 16) & 255};
    int myColorValue = rgb[player];
    Point otherPlayers = getOtherPlayers();
    int theirColorValue = rgb[otherPlayers.x] + rgb[otherPlayers.y];
    return theirColorValue - myColorValue;
  }
  
  private Point getOtherPlayers() {
    return new Point((player + 1) % 3, (player + 2) % 3);
  }
  
  public int getColorValue() {
    return colorValue;
  }
  
  public int getIndex() {
    return y * gridKernelLength + x;
  }

  @Override
  public int compareTo(ColorGridCell that) {
    return Integer.compare(this.colorValue, that.colorValue);
  }
  

}
