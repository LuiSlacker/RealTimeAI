package de.htw.lenz.gameUtils;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import de.htw.lenz.main.FloydWarshall;
import de.htw.lenz.main.ImagePanel;
import de.htw.lenz.main.Utils;
import lenz.htw.kipifub.ColorChange;
import lenz.htw.kipifub.net.NetworkClient;

public class Client{

  private static final int BLACK = 0;
  private static final int WHITE = 0xFFFFFF;
  private NetworkClient networkClient;
  private static int WIDTH = 1024;
  private static int HEIGHT = 1024;
  private static int GRID_KERNEL_LENGTH = 64;
  private static int GRID_WIDTH = WIDTH / GRID_KERNEL_LENGTH;
  
  private FloydWarshall floydWarshall;
  
  private int[][][] pixels = new int[WIDTH][HEIGHT][4];

  private int player;

  public Client(String name, String host) {
    try {
      networkClient = new NetworkClient(host, name);
      player = networkClient.getMyPlayerNumber();
      
      boolean[] grid = generateGrid();
      floydWarshall = new FloydWarshall(grid, GRID_WIDTH);
      
      ColorGrid colorGrid = new ColorGrid(networkClient, player, GRID_KERNEL_LENGTH, GRID_WIDTH);
      System.out.println(colorGrid.getMostInterestingColorGridCell());
      
      start();
    } catch (Exception e) {
     throw new RuntimeException("", e);
    }
  }
  
  private void start() {
    while(true) {
      listenForColorChange();
      moveBot(0, Direction.getRandom());
//      moveBot(1, Direction.getRandom());
//      moveBot(2, Direction.getRandom());
    }
  }
  
  private void listenForColorChange() {
    ColorChange colorChange;
    while ((colorChange = networkClient.pullNextColorChange()) != null) {
      System.out.println("ColorChange");
      if (colorChange.player == player && colorChange.bot == 0) {
        //travelToSpot(0, colorChange.x, colorChange.y, 40);
      }
    }
  }
  
  private void travelToSpot(int bot, int currentX, int currentY,  int targetVertex) {
    int gridIndex = mapCordinatesToGridIndex(currentX, currentY);
    List<Integer> path = floydWarshall.reconstructPath(gridIndex, targetVertex);
    System.out.println(path);
    for (int i = 2; i < path.size(); i++) {
      getDirectionforSuccessiveVertices(path.get(i), path.get(i-1)); 
    }
    
  }
  
  private Direction getDirectionforSuccessiveVertices(int a, int b) {
    Direction result = null;
    switch (a-b) {
    case -16:
      result = Direction.Top;
      break;
    case 16:
      result = Direction.Bottom;
      break;
    case -1:
      result = Direction.Left;
      break;
    case 1:
      result = Direction.Right;
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
  
  private void drawImage() {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame.setDefaultLookAndFeelDecorated(true);
        f.setResizable(false);
        ImagePanel vP = new ImagePanel(pixels);
        f.add(vP, BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
      }
    });
  }

  private void generateImage() {
    for (int y = 0; y < HEIGHT; y++) {
      for (int x = 0; x < WIDTH; x++) {
        int rgb = networkClient.getBoard(x, y);
        int b = rgb & 255;
        int g = (rgb >> 8) & 255;
        int r = (rgb >> 16) & 255;
        pixels[x][y] = new int[]{r, g, b, 255};
      }
    }
  }
  
}
