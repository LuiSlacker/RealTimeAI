package de.htw.lenz.gameUtils;

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
      networkClient.setMoveDirection(0, Math.random() > 0.5 ? 1 : -1, Math.random() > 0.5 ? 1 : -1);
      networkClient.setMoveDirection(1, Math.random() > 0.5 ? 1 : -1, Math.random() > 0.5 ? 1 : -1);
      networkClient.setMoveDirection(2, Math.random() > 0.5 ? 1 : -1, Math.random() > 0.5 ? 1 : -1);
    }
  }
  
  
}
