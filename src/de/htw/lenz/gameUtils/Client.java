package de.htw.lenz.gameUtils;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import de.htw.lenz.main.FloydWarshall;
import lenz.htw.kipifub.ColorChange;
import lenz.htw.kipifub.net.NetworkClient;

public class Client{

  private static final int BLACK = 0;
  private static final int WHITE = 0xFFFFFF;
  private NetworkClient networkClient;
  private static int WIDTH = 1024;
  private static int HEIGHT = 1024;
  private static int GRID_KERNEL_LENGTH = 32;
  private static int GRID_WIDTH = WIDTH / GRID_KERNEL_LENGTH;

  private FloydWarshall floydWarshall;
  private ColorGrid colorGrid;
  private int player;
  private Point positionBot0;
  private Point positionBot1;
  private Point positionBot2;
//  private int[][][] pixels = new int[WIDTH][HEIGHT][4];

  public Client(String name, String host) {
    try {
      networkClient = new NetworkClient(host, name);
      player = networkClient.getMyPlayerNumber();
      
      boolean[] grid = generateGrid();
      floydWarshall = new FloydWarshall(grid, GRID_WIDTH);
      
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
    triggerInitialBotsMove();
    while(true) {
      listenForColorChange();
      int mostInterestingColorGridCell = colorGrid.getMostInterestingColorGridCell();
//      int mostInterestingColorGridCell = 110;
      if (player == 0) {
//        System.out.println("interstingCell: " + mostInterestingColorGridCell);
        travelToCell(1, positionBot1, mostInterestingColorGridCell);
//        System.out.println("player0" + positionBot1);
      }
    }
  }
  
  private void listenForColorChange() {
    ColorChange colorChange;
    while ((colorChange = networkClient.pullNextColorChange()) != null) {
      updateMyBotPosition(colorChange);
      // TODO implement
//      updateColorGrid();
    }
  }
  
  private void triggerInitialBotsMove() {
//    moveBot(0, Direction.getRandom());
    if (player == 0) {
      moveBot(1, Direction.Right);
    }
//    moveBot(2, Direction.getRandom());
  }
  
  private void updateMyBotPosition(ColorChange colorChange) {
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
  
  private void travelToCell(int bot, Point currentPosition,  int targetVertex) {
    int gridIndex = mapCordinatesToGridIndex(currentPosition.x, currentPosition.y);
//    System.out.printf("bot: %s,       -        grid index: %s", bot, gridIndex);System.out.println();
    List<Integer> path = floydWarshall.reconstructPath(gridIndex, targetVertex);
    System.out.printf("travelling from %s to %s via %s", gridIndex, targetVertex, path);System.out.println();
    if (path.size() > 1) {
//      System.out.printf("travelling along: %S", path);System.out.println();
      Direction nextDirection = getDirectionforSuccessiveVertex(path.get(0), path.get(1));
//      System.out.println("nextDrection: " + nextDirection);
      moveBot(bot, nextDirection);
    }
  }
  
  private Direction getDirectionforSuccessiveVertex(int a, int b) {
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
   * Generates a grid of the pitch
   * Each grid cell's booleans indicates whether one of the containing pixels is not walkable 
   */
  private boolean[] generateGrid() {
    int gridLength = (int) Math.pow(WIDTH / GRID_KERNEL_LENGTH, 2);
    boolean[] grid = new boolean[gridLength];
    Arrays.fill(grid, Boolean.TRUE);
    for (int y = 0; y < HEIGHT; y++) {
      for (int x = 0; x < WIDTH; x++) {
        int gridIndex = mapCordinatesToGridIndex(x, y);
        if (!isWalkable(x, y)) grid[gridIndex] = false;
      }
    }
    return grid;
  }
  
  /**
   * Maps pixelCoordinates to gridIndex
   */
  private int mapCordinatesToGridIndex(int x, int y) {
    int gridX = x / GRID_KERNEL_LENGTH;
    int gridY = y / GRID_KERNEL_LENGTH;
    return gridY * GRID_WIDTH + gridX;
  }
  
  private void moveBot(int bot, Direction direction) {
    networkClient.setMoveDirection(bot, direction.getValue().x, direction.getValue().y);
  }
  
  private boolean isWalkable(int x, int y) {
    return networkClient.isWalkable(x, y);
  }
  
  private long getScoreForPlayer(int player) {
    return networkClient.getScore(player);
  }
  
//  private void drawImage() {
//    EventQueue.invokeLater(new Runnable() {
//      public void run() {
//        JFrame f = new JFrame();
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JFrame.setDefaultLookAndFeelDecorated(true);
//        f.setResizable(false);
//        ImagePanel vP = new ImagePanel(pixels);
//        f.add(vP, BorderLayout.CENTER);
//        f.pack();
//        f.setVisible(true);
//      }
//    });
//  }
//
//  private void generateImage() {
//    for (int y = 0; y < HEIGHT; y++) {
//      for (int x = 0; x < WIDTH; x++) {
//        int rgb = networkClient.getBoard(x, y);
//        int b = rgb & 255;
//        int g = (rgb >> 8) & 255;
//        int r = (rgb >> 16) & 255;
//        pixels[x][y] = new int[]{r, g, b, 255};
//      }
//    }
//  }
  
}


//final class BotScheduler implements Runnable {
//  
//  private NetworkClient networkClient;
//  
//  public BotScheduler(NetworkClient networkClient) {
//    this.networkClient = networkClient;
//  }
//  
//  @Override
//  public void run() {
//    new Client(this.clientName);
//  }
//  
//}
