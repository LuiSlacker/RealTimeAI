package de.htw.lenz.AI;

import java.awt.Point;
import java.util.List;

import de.htw.lenz.gameUtils.ColorGrid;
import de.htw.lenz.gameUtils.Direction;
import de.htw.lenz.gameUtils.GameUtils;
import lenz.htw.kipifub.net.NetworkClient;

public class BotScheduler extends MainBotScheduler implements Runnable{

  private static int TARGET_CELL_UPDATE = 2;
  private static int THREAD_SLEEP = 500;
  public volatile Point botPosition;
  private ColorGrid colorGrid;
  private FloydWarshall floydWarshall;
  private int gridKernelLength;
  private int gridWidth;
  
  private int count = 0;
  private int bot;
  
  public BotScheduler(NetworkClient networkClient, FloydWarshall floydWarshall, ColorGrid colorGrid, int bot, Point botPosition, int gridKernelLength, int gridWidth) {
    super(networkClient);
    this.floydWarshall = floydWarshall;
    this.colorGrid = colorGrid;
    this.botPosition = botPosition; 
    this.bot = bot;
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
      if (count % (TARGET_CELL_UPDATE + bot * 2) == 0) {
        mostInterestingColorGridCell = colorGrid.getMostInterestingColorGridCell();
      }
      travelToCell(bot, botPosition, mostInterestingColorGridCell);
      try {
        Thread.sleep(THREAD_SLEEP);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      count++;
    }
  }
  
  private void travelToCell(int bot, Point currentPosition,  int targetVertex) {
    int gridIndex = GameUtils.mapCordinatesToGridIndex(currentPosition.x, currentPosition.y, gridKernelLength, gridWidth);
    List<Integer> path = floydWarshall.reconstructPath(gridIndex, targetVertex);
    if (path.size() > 1) {
      Direction nextDirection = GameUtils.getDirectionforSuccessiveVertex(path.get(0), path.get(1));
      moveBot(bot, nextDirection);
    } else {
      moveBot(bot, Direction.getRandom());
    }
  }
}
