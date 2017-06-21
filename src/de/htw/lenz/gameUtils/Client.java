package de.htw.lenz.gameUtils;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import de.htw.lenz.main.FloydWarshall;
import de.htw.lenz.main.ImagePanel;
import lenz.htw.kipifub.ColorChange;
import lenz.htw.kipifub.net.NetworkClient;

public class Client{

  private static final int BLACK = 0;
  private static final int WHITE = 0xFFFFFF;
  private NetworkClient networkClient;
//  private int WIDTH = 1096;
//  private int HEIGHT = 1024;
  private int WIDTH = 3;
  private int HEIGHT = 3;
  
  private int[][][] pixels = new int[WIDTH][HEIGHT][4];

  private int player;

  public Client(String name, String host) {
    try {
//      networkClient = new NetworkClient(host, name);
//      player = networkClient.getMyPlayerNumber();
//      System.out.println(player);
//      generateImage();
      List<Point> vertices = getVertices();
      int[][] matrix = generateAdjacencyMatrix(vertices);
      printArray2D(matrix);
      
      System.out.println();
      int[][] allpairShortestPath = FloydWarshall.allPairShortestPath(matrix);
      printArray2D(allpairShortestPath);
//      drawImage();
//      start();
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
      moveBot(0, Direction.getRandom());
      moveBot(1, Direction.getRandom());
      moveBot(2, Direction.getRandom());
    }
  }
  
  private List<Point> getVertices() {
    List<Point> vertices = new ArrayList<>();
    for (int y = 0; y < HEIGHT; y++) {
      for (int x = 0; x < WIDTH; x++) {
        //if (networkClient.getBoard(x, y) == WHITE) {
          vertices.add(new Point(x, y));
        //}
      }
    }
    return vertices;
  }
  
  private int[][] generateAdjacencyMatrix(List<Point> vertices) {
    int n = WIDTH * HEIGHT;
    int[][] matrix = initializeAdjacencyMatrix(n);
    for (int i = 1; i < n; i++) {
      for (int j = 0; j < i; j++) {
        if (isNeighbour(vertices.get(i), vertices.get(j))) {
         matrix[i][j] = 1; 
        }
      }
    }
    return updateRestOfSymmetricalMatrix(matrix);
  }
  
  private int[][] initializeAdjacencyMatrix(int n) {
    int[][] matrix = new int[n][n];
    for (int y = 0; y < n; y++) {
      for (int x = 0; x < n; x++) {
        if (x != y) matrix[x][y] = 4; // TODO Integer.MAX_VALUE
      }
    }
    return matrix;
  }
  
  private int[][] updateRestOfSymmetricalMatrix(int[][] matrix) {
    for (int i = 0; i < matrix.length; i++) {
      for (int j = i+1; j < matrix[i].length; j++) {
        matrix[i][j] = matrix[j][i];
      }
    }
    return matrix;
  }
  
  private synchronized void printArray2D(int[][] array) {
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array[i].length; j++) {
        System.out.print(array[i][j]);
      }
      System.out.println();
    }
  }
  
  private boolean isNeighbour(Point a, Point b) {
    return Math.abs(a.x -b.x) <= 1 && Math.abs(a.y -b.y) <= 1;
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
      // Verarbeitung von colorChange
      // colorChange.player, colorChange.bot, colorChange.x, colorChange.y
    }
  }
  
}
