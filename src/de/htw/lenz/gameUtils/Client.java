package de.htw.lenz.gameUtils;

import java.awt.Point;
import java.util.Arrays;

import de.htw.lenz.AI.BotScheduler;
import de.htw.lenz.AI.BrushScheduler;
import de.htw.lenz.AI.FloydWarshall;
import lenz.htw.kipifub.ColorChange;
import lenz.htw.kipifub.net.NetworkClient;

public class Client{

  private NetworkClient networkClient;
  private static int WIDTH = 1024;
  private static int HEIGHT = 1024;
  private static int GRID_KERNEL_LENGTH = 32;
  private static int GRID_WIDTH = WIDTH / GRID_KERNEL_LENGTH;

  private FloydWarshall floydWarshall;
  private ColorGrid colorGrid;
  private int player;
  private volatile Point positionBot0;
  private volatile Point positionBot1;
  private volatile Point positionBot2;
  
  private boolean[] booleanCellGrid;

  public Client(String name, String host) {
    try {
      networkClient = new NetworkClient(host, name);
      player = networkClient.getMyPlayerNumber();
      
      booleanCellGrid = generateGrid();
      floydWarshall = new FloydWarshall(booleanCellGrid, GRID_WIDTH);
      
      colorGrid = new ColorGrid(networkClient, player, GRID_KERNEL_LENGTH, GRID_WIDTH);
      
      positionBot0 = new Point(-1, -1);
      positionBot1 = new Point(-1, -1);
      positionBot2 = new Point(-1, -1);
      
      start();
    } catch (Exception e) {
     throw new RuntimeException("", e);
    }
  }
  
  private void start() {
      Thread bot0 = new Thread(new BotScheduler(networkClient, floydWarshall, colorGrid, 0, positionBot0, GRID_KERNEL_LENGTH, GRID_WIDTH));
      bot0.start();
      Thread bot1 = new Thread(new BotScheduler(networkClient, floydWarshall, colorGrid, 1, positionBot1, GRID_KERNEL_LENGTH, GRID_WIDTH));
      bot1.start();
      Thread bot2 = new Thread(new BrushScheduler(networkClient, 2, positionBot2, GRID_KERNEL_LENGTH, GRID_WIDTH, booleanCellGrid));
      bot2.start();
    while(true) {
      listenForColorChange();
    }
  }
  
  private synchronized void listenForColorChange() {
    ColorChange colorChange;
    while ((colorChange = networkClient.pullNextColorChange()) != null) {
      updateMyBotsPosition(colorChange);
      int cellX = colorChange.x / GRID_KERNEL_LENGTH;
      int cellY = colorChange.y / GRID_KERNEL_LENGTH;
      colorGrid.updatedColorGridCell(cellX, cellY);
    }
  }
  
  private void updateMyBotsPosition(ColorChange colorChange) {
    if (colorChange.player == player) {
      switch (colorChange.bot) {
      case 0:
        positionBot0.x = colorChange.x;
        positionBot0.y = colorChange.y;
        break;
      case 1:
        positionBot1.x = colorChange.x;
        positionBot1.y = colorChange.y;
        break;
      case 2:
        positionBot2.x = colorChange.x;
        positionBot2.y = colorChange.y;
        break;
      default:
        break;
      }
    }
  }
  
  /**
   * Generates a grid of the pitch
   * Each grid cell's booleans indicates whether one of the containing pixels is not walkable 
   */
  private boolean[] generateGrid() {
    int gridLength = (int) Math.pow(WIDTH / GRID_KERNEL_LENGTH, 2);
    boolean[] grid = new boolean[gridLength];
    Arrays.fill(grid, Boolean.TRUE);
    for (int y = 0; y < HEIGHT; y++) {
      for (int x = 0; x < WIDTH; x++) {
        int gridIndex = GameUtils.mapCordinatesToGridIndex(x, y, GRID_KERNEL_LENGTH, GRID_WIDTH);
        if (!isWalkable(x, y)) grid[gridIndex] = false;
      }
    }
    return grid;
  }
  
  private boolean isWalkable(int x, int y) {
    return networkClient.isWalkable(x, y);
  }
  
}


