package ui;

import java.awt.Rectangle;

public class PauseButton {

	
	protected int x, y, width, height; // can use as the inheritance
	protected Rectangle bounds; // can use as the inheritance
	
	public PauseButton(int x, int y, int width, int height){ // Encapsulation
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		createBounds();
	}
	
	
	private void createBounds() {
		bounds = new Rectangle(x, y, width, height); // create hit box
		
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}


	public int getWidth() {
		return width;
	}


	public void setWidth(int width) {
		this.width = width;
	}


	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}


	public Rectangle getBounds() {
		return bounds;
	}


	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	

	
}
