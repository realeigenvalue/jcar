package com.realeigenvalue.jcar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import com.realeigenvalue.jcar.helper.Location;

public class Car {
	private Dimension dimension;
	private Location location;
	private int dX;
	private int dY;
	private Color color;
	private BufferedImage image;
	public Car(Location location, Dimension dimension, Color color) {
		this.location = new Location(location);
		this.dimension = new Dimension(dimension);
		setColor(color);
	}
	private final void createImage() {
		image = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D gCar = image.createGraphics();
			gCar.setColor(color);
			gCar.fillRect(0, 0, dimension.width, dimension.height);
			gCar.setColor(Color.GRAY);
			Dimension tireDimension = new Dimension((int)(this.dimension.width * 0.20d), (int)(this.dimension.height * 0.30d));
			gCar.fillRect(0, 0, tireDimension.width, tireDimension.height);
			gCar.fillRect(image.getWidth() - tireDimension.width, 0, tireDimension.width, tireDimension.height);
			gCar.fillRect(0, image.getHeight() - tireDimension.height, tireDimension.width, tireDimension.height);
			gCar.fillRect(image.getWidth() - tireDimension.width, image.getHeight() - tireDimension.height, tireDimension.width, tireDimension.height);
			gCar.setColor(Color.WHITE);
			Dimension lightDimension = new Dimension((int)(tireDimension.width * 0.50d), (int)(tireDimension.height * 0.50d));
			gCar.fillRect((image.getWidth() / 2) - lightDimension.width - 1, 0, lightDimension.width, lightDimension.height);
			gCar.fillRect((image.getWidth() / 2) + lightDimension.width - 1, 0, lightDimension.width, lightDimension.height);
		gCar.dispose();
	}
	public void setLocation(Location location) {
		this.location.setLocation(location);
	}
	public void setColor(Color color) {
		this.color = color;
		createImage();
	}
	public void setDx(int dx) {
		this.dX = dx;
	}
	public void setDy(int dy) {
		this.dY = dy;
	}
	public void draw(Graphics g) {
		BufferedImage canvas = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D gRotate = canvas.createGraphics();
		if(this.dX == 0 && this.dY == 1) {
			// createImage draws car in up position
		} else if(this.dX == 1 && this.dY == 0) {
			gRotate.rotate(Math.toRadians(90), canvas.getWidth() / 2, canvas.getHeight() / 2);
		} else if(this.dX == 0 && this.dY == -1) {
			gRotate.rotate(Math.toRadians(180), canvas.getWidth() / 2, canvas.getHeight() / 2);
		} else if(this.dX == -1 && this.dY == 0) {
			gRotate.rotate(Math.toRadians(270), canvas.getWidth() / 2, canvas.getHeight() / 2);
		}
		gRotate.drawImage(image, 0, 0, null);
		gRotate.dispose();
		g.drawImage(canvas, location.x, location.y, dimension.width, dimension.height, null);
	}
	public Location getLocation() {
		return location;
	}
	public int getdX() {
		return dX;
	}
	public int getdY() {
		return dY;
	}
}