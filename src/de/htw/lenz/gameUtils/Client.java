package de.htw.lenz.gameUtils;

import java.util.Random;

import lenz.htw.kipifub.net.NetworkClient;

public class Client {

  private NetworkClient networkClient;
  private int player;

  public Client(String name, String host) {
    try {
      networkClient = new NetworkClient(host, name);
      player = networkClient.getMyPlayerNumber();
      System.out.println(player);
      start();
    } catch (Exception e) {
     throw new RuntimeException("", e);
    }
  }
    
  private void start() {
    while(true) {
      moveBot(0, Direction.getRandom());
      moveBot(1, Direction.getRandom());
      moveBot(2, Direction.getRandom());
    }
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
  
  
}
