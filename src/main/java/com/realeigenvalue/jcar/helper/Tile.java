package com.realeigenvalue.jcar.helper;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
public class Tile extends JPanel {
	private BufferedImage image;
	public Tile(String imageURL) {
		try {
			this.image = ImageIO.read(new File(imageURL));
		} catch (IOException e) {
			System.out.println("Tile image loading problem!");
		}
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
	}
}