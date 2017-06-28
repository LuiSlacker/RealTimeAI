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
  
  private int[][][] pixels = new int[WIDTH][HEIGHT][4];

  private int player;

  public Client(String name, String host) {
    try {
      networkClient = new NetworkClient(host, name);
      player = networkClient.getMyPlayerNumber();
//      generateImage();
      
      boolean[] grid = generateGrid();
      Utils.printBooleanGrid(grid, GRID_WIDTH);
//      
      FloydWarshall floydWarshall = new FloydWarshall(grid, GRID_WIDTH);
      floydWarshall.allPairsShortestPath();
      
//      drawImage();
      start();
    } catch (Exception e) {
     throw new RuntimeException("", e);
    }
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
    
  private void start() {
    while(true) {
      listenForColorChange();
      moveBot(0, Direction.getRandom());
      moveBot(1, Direction.getRandom());
      moveBot(2, Direction.getRandom());
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
  
  private boolean isWalkable(int x, int y) {
    return networkClient.isWalkable(x, y);
  }
  
  private long getScoreForPlayer(int player) {
    return networkClient.getScore(player);
  }
  
  private void moveBot(int bot, Direction direction) {
    networkClient.setMoveDirection(bot, direction.getValue().x, direction.getValue().y);
  }
  
  private void listenForColorChange() {
    ColorChange colorChange;
    while ((colorChange = networkClient.pullNextColorChange()) != null) {
      System.out.println("ColorChange");
    }
  }
  
}
