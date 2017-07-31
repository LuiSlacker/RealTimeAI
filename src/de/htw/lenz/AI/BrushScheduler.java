package de.htw.lenz.AI;

import java.awt.Point;

import de.htw.lenz.gameUtils.BrushBotDirectionsBottom;
import de.htw.lenz.gameUtils.BrushBotDirectionsTop;
import de.htw.lenz.gameUtils.Direction;
import de.htw.lenz.gameUtils.GameUtils;
import lenz.htw.kipifub.net.NetworkClient;

public class BrushScheduler extends MainBotScheduler implements Runnable{

  private int threadSleep = 950;
  
  private int bot;
  private volatile Point botPosition;
  private int gridKernelLength;
  private int gridWidth;
  private boolean[] booleanCellGrid;
  private boolean isTurn = false;
  private int oldCellIndex = -1;
  
  public BrushScheduler(NetworkClient networkClient, int bot, Point botPosition, int gridKernelLength, int gridWidth, boolean[] booleanCellGrid) {
    super(networkClient);
    this.bot = bot;
    this.botPosition = botPosition;
    this.gridKernelLength = gridKernelLength;
    this.gridWidth = gridWidth;
    this.booleanCellGrid = booleanCellGrid;
  }
  @Override
  public void run() {
    moveBot(bot, Direction.getRandom());
    int cellIndex;
    while(true) {
      cellIndex = GameUtils.mapCordinatesToGridIndex(botPosition.x, botPosition.y, gridKernelLength, gridWidth);
      if (cellIndex == oldCellIndex) {
        moveBot(bot, getNextDirection(cellIndex));
      } else if (isTurn) {
        moveBot(bot, getNextDirection(cellIndex));
        isTurn = false;
      } else {
        if (!booleanCellGrid[cellIndex]) { // brush within (or surrounded by) gridCell with non-walkable pixels
          isTurn = true;
          moveBot(bot, getNextDirection(cellIndex));
        }
      }
      oldCellIndex = cellIndex;
      try {
        Thread.sleep(threadSleep);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
  
  private Direction getNextDirection(int cellIndex) {
    return (cellIndex > ((gridWidth-4) * gridWidth)) ? BrushBotDirectionsTop.next(): BrushBotDirectionsBottom.next(); 
  }
  

}
