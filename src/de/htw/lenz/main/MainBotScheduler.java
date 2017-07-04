package de.htw.lenz.main;

import de.htw.lenz.gameUtils.Direction;
import lenz.htw.kipifub.net.NetworkClient;

public class MainBotScheduler {
  
  private NetworkClient networkClient;

  public MainBotScheduler(NetworkClient networkClient) {
    this.networkClient = networkClient;
  }
  
  protected void moveBot(int bot, Direction direction) {
    networkClient.setMoveDirection(bot, direction.getValue().x, direction.getValue().y);
  }
}
