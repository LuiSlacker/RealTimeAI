package de.htw.lenz.gameUtils;

import de.htw.lenz.main.Utils;
import lenz.htw.kipifub.net.NetworkClient;

public class ColorGrid {
  
  private int gridWidth;
  private ColorGridCell[][] colorGrid;
  private NetworkClient networkClient;
  private int player;
  private int gridKernelLength;

  public ColorGrid(NetworkClient networkClient, int player, int gridKernelLength, int gridWidth) {
    this.networkClient = networkClient;
    this.player = player;
    this.gridKernelLength = gridKernelLength;
    this.gridWidth = gridWidth;
    colorGrid = new ColorGridCell[gridWidth][gridWidth];
    initializeGrid();
    Utils.printColorGrid(colorGrid);;
  }

  private void initializeGrid() {
    for (int j = 0; j < gridWidth; j++) {
      for (int i = 0; i < gridWidth; i++) {
        colorGrid[i][j] = new ColorGridCell(networkClient, player, gridKernelLength, i, j);
      }
    }
  }
  
  public int getMostInterestingColorGridCell() {
    int mostInterstingCell = (int)(Math.random() * gridWidth * gridWidth);
    int maxValue = Integer.MIN_VALUE; 
    for (int j = 0; j < colorGrid.length; j++) {
      for (int i = 0; i < colorGrid.length; i++) {
        if (colorGrid[i][j].getColorValue() > maxValue) {
          maxValue = colorGrid[i][j].getColorValue();
          mostInterstingCell = j * gridWidth + i;
        }
        
      }
    }
    return mostInterstingCell;
  }
  
  public int getRandomCell() {
    int x = (int) (Math.random() * gridWidth);
    int y = (int) (Math.random() * gridWidth);
    while(colorGrid[x][y].getColorValue() == 0) {
      x = (int) (Math.random() * gridWidth);
      y = (int) (Math.random() * gridWidth);
    }
    return y * gridWidth + x;
  }
  

}
