package de.htw.lenz.gameUtils;

import java.util.Arrays;

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
//    Utils.printColorGrid(colorGrid);
  }

  private void initializeGrid() {
    for (int j = 0; j < gridWidth; j++) {
      for (int i = 0; i < gridWidth; i++) {
        colorGrid[i][j] = new ColorGridCell(networkClient, player, gridKernelLength, i, j);
      }
    }
  }
  
  public int getMostInterestingColorGridCell() {
    int mostInterstingCell = getRandomCell();
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
  
  public boolean isSurroundedByBlankCells(int x, int y) {
    if (x < 1 || y < 1 || x > (gridKernelLength-2) || y > (gridKernelLength-2)) return false;
    return colorGrid[x-1][y].getColorValue() == 261120
        && colorGrid[x+1][y].getColorValue() == 261120
        && colorGrid[x][y-1].getColorValue() == 261120
        && colorGrid[x][y+1].getColorValue() == 261120;
  }
  
  public int getRandomCell() {
    int x = (int) (Math.random() * gridWidth);
    int y = (int) (Math.random() * gridWidth);
    while(colorGrid[x][y].getColorValue() != gridKernelLength * gridKernelLength * 255 && !isSurroundedByBlankCells(x, y)) {
      x = (int) (Math.random() * gridWidth);
      y = (int) (Math.random() * gridWidth);
    }
    return y * gridWidth + x;
  }
  
  public void updatedColorGridCell(int x, int y) {
    colorGrid[x][y].updateColorGridCell();
  }
  
  public int getRandomBestCell() {
    ColorGridCell[] sortedArray = sortFlattenedArray();
    int randomBestIndex = (int) (Math.random() * 4);
    return sortedArray[randomBestIndex].getIndex();
  }
  
  private ColorGridCell[] sortFlattenedArray() {
    ColorGridCell[] flattenedArray = flattenColorGrid();
    Arrays.sort(flattenedArray);
    return flattenedArray;
  }
  
  /**
   * flattens the 2d ColorGrid
   */
  private ColorGridCell[] flattenColorGrid() {
    ColorGridCell[] flatteneColordGrid = new ColorGridCell[gridWidth * gridWidth];
    for (int j = 0; j < colorGrid.length; j++) {
      for (int i = 0; i < colorGrid.length; i++) {
        int pos = j * gridWidth + i;
        flatteneColordGrid[pos] = colorGrid[i][j];
      }
    }
    return flatteneColordGrid;
  }
  

}
