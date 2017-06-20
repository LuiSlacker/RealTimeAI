package de.htw.lenz.main;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;

import de.htw.lenz.gameUtils.Client;

public class Main {

  public static void main(String[] args) {
    Thread t1 = new Thread(new Task("Luis"));
    t1.start();
    Thread t2 = new Thread(new Task("Mike"));
    t2.start();
    Thread t3 = new Thread(new Task("Zoey"));
    t3.start();
    
//    EventQueue.invokeLater(new Runnable() {
//      public void run() {
//        JFrame f = new JFrame();
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JFrame.setDefaultLookAndFeelDecorated(true);
//        f.setResizable(false);
//        VisualizePath vP = new VisualizePath();
//        f.add(vP, BorderLayout.CENTER);
//        f.pack();
//        f.setVisible(true);
//      }
//    });
    
  }

}

final class Task implements Runnable {
  
  private String clientName;
  
  public Task(String clientName) {
      this.clientName = clientName;
  }
  
  @Override
  public void run() {
    new Client(this.clientName, null);
  }
  
}
