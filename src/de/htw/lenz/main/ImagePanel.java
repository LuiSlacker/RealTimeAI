package de.htw.lenz.main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.JPanel;

public class ImagePanel extends JPanel{
  
  private static final long serialVersionUID = 1L;
  private static final int WIDTH = 1096;
  private static final int HEIGHT = 1024;
  
  private int[][][] pixels;
  
  public ImagePanel(int[][][] pixels) {
    this.pixels = pixels;
    setPreferredSize(new Dimension(WIDTH * 2, HEIGHT * 2));
  }
  
  @Override
  public void paintComponent(Graphics g) {
    final BufferedImage image;

    image = (BufferedImage) createImage(WIDTH, HEIGHT);
    WritableRaster raster = image.getRaster();
    for (int row = 0; row < HEIGHT; row++) {
      for (int col = 0 ; col < WIDTH; col++) {
        raster.setPixel(col, row, pixels[col][row]);
      }
    }
    g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
  }

}
