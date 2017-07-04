package de.htw.lenz.main;

import java.awt.Point;

import de.htw.lenz.gameUtils.BrushBotDirections;
import de.htw.lenz.gameUtils.Direction;
import de.htw.lenz.gameUtils.GameUtils;
import lenz.htw.kipifub.net.NetworkClient;

public class BrushScheduler extends MainBotScheduler implements Runnable{

  private int threadSleep = 700;
  
  private int bot;
  public volatile Point botPosition;

  private int gridKernelLength;

  private int gridWidth;

  private boolean[] booleanCellGrid;
  
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
    moveBot(bot, Direction.Left);
    while(true) {
      int cellIndex = GameUtils.mapCordinatesToGridIndex(botPosition.x, botPosition.y, gridKernelLength, gridWidth);
      if (!booleanCellGrid[cellIndex]) {
        if (cellIndex > (gridWidth-4) * gridWidth) {
          moveBot(bot, BrushBotDirections.nextDirectionToTop());
        } else {
          moveBot(bot, BrushBotDirections.nextDirectionToBottom());
        }
      };
      try {
        Thread.sleep(threadSleep);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
  
  

}
