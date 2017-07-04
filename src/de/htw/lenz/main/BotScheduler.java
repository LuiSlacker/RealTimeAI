package de.htw.lenz.main;

import java.util.List;

import de.htw.lenz.gameUtils.ColorGrid;
import de.htw.lenz.gameUtils.Coordinate;
import de.htw.lenz.gameUtils.Direction;
import de.htw.lenz.gameUtils.GameUtils;
import lenz.htw.kipifub.net.NetworkClient;

public class BotScheduler implements Runnable{

  private static int TARGET_CELL_UPDATE = 8;
  private static int THREAD_SLEEP = 500;
  private NetworkClient networkClient;
  public volatile Coordinate botPosition;
  private ColorGrid colorGrid;
  private FloydWarshall floydWarshall;
  private int gridKernelLength;
  private int gridWidth;
  
  private int count = 0;
  
  public BotScheduler(NetworkClient networkClient, FloydWarshall floydWarshall, ColorGrid colorGrid, Coordinate botPosition, int gridKernelLength, int gridWidth) {
    this.networkClient = networkClient;
    this.floydWarshall = floydWarshall;
    this.colorGrid = colorGrid;
    this.botPosition = botPosition; 
    this.gridKernelLength = gridKernelLength;
    this.gridWidth = gridWidth;
  }
  
  @Override
  public void run() {
    int mostInterestingColorGridCell = colorGrid.getMostInterestingColorGridCell();
    start(mostInterestingColorGridCell);
  }
  
  private void start(int mostInterestingColorGridCell) {
    while(true) {
    if (count % TARGET_CELL_UPDATE == 0) mostInterestingColorGridCell = colorGrid.getRandomCell();
      System.out.println("botScheduler: " + botPosition);
      System.out.printf("most intersting cell: %s", mostInterestingColorGridCell);System.out.println();
      travelToCell(1, botPosition, mostInterestingColorGridCell);
      try {
        Thread.sleep(THREAD_SLEEP);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      count++;
    }
  }
  
  private void travelToCell(int bot, Coordinate currentPosition,  int targetVertex) {
    int gridIndex = GameUtils.mapCordinatesToGridIndex(currentPosition.getX(), currentPosition.getY(), gridKernelLength, gridWidth);
    System.out.printf("bot: %s,       -        grid index: %s", bot, gridIndex);System.out.println();
    List<Integer> path = floydWarshall.reconstructPath(gridIndex, targetVertex);
    System.out.printf("travelling from %s to %s via %s", gridIndex, targetVertex, path);System.out.println();
    if (path.size() > 1) {
//      System.out.printf("travelling along: %S", path);System.out.println();
      Direction nextDirection = GameUtils.getDirectionforSuccessiveVertex(path.get(0), path.get(1));
//      System.out.println("nextDrection: " + nextDirection);
      moveBot(bot, nextDirection);
    } else {
      moveBot(bot, Direction.getRandom());
    }
  }
  
  private void moveBot(int bot, Direction direction) {
    networkClient.setMoveDirection(bot, direction.getValue().x, direction.getValue().y);
  }
}