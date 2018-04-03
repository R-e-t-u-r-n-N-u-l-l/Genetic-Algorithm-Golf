package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Wall {

	private Rectangle bounds;
	
	public Wall(int x, int y, int width, int height) {
		bounds = new Rectangle(x, y, width, height);
	}
	
	public void draw(Graphics g) {
		g.setColor(new Color(100, 0, 0));
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
}
