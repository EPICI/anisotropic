package main;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class ImagePanel extends JPanel {
	
	public BufferedImage image;
	public int sx;
	public int sy;
	
	public ImagePanel(BufferedImage source,int width,int height){
		super();
		image = source;
		sx=width;
		sy=height;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(image, 0, 0, sx, sy, this);
	}

}
