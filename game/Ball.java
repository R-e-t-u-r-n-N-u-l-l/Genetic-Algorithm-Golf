package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Ball {

	public float fitness;
	public boolean inHole;
	public ArrayList<Point2D.Double> velocities;
	
	private final int size = 40;
	private final float friction = 0.99f, threshold = 0.4f;
	private int index;
	private Point2D.Double currentVel;
	private Point destination;
	private Rectangle bounds;
	private float x, y;
	
	public Ball(int x, int y, Point destination) {
		this.x = x;
		this.y = y;
		this.destination = destination;
		
		bounds = new Rectangle(x, y, size, size);
		velocities = new ArrayList<>();
	}
	
	public void update(ArrayList<Wall> walls, int par) {
		if(currentVel == null)
			currentVel = new Point2D.Double(velocities.get(0).x, velocities.get(0).y);
		
		if(!inHole) {
			fitness = Math.min(3.0f, Math.min(1.0f, (float) (1.0f / Math.sqrt((x - destination.x) * (x - destination.x) + (y - destination.y) * (y - destination.y)))) + (float) par / velocities.size());
			if(new Rectangle(bounds.x + size / 4, bounds.y + size / 4, size / 2, size / 2).contains(destination)) {
				inHole = true;
				fitness = 2.0f + (float) par / velocities.size();
				return;
			}
			
			boolean intersect = false;
			for(Wall w : walls) {
				if(new Rectangle((int) (bounds.x + currentVel.x), bounds.y + size / 4, size, size / 2).intersects(w.getBounds())) {
					currentVel.x *= -0.95;
					intersect = true;
				} if(new Rectangle(bounds.x + size / 4, (int) (bounds.y + currentVel.y), size / 2, size).intersects(w.getBounds())) {
					currentVel.y *= -0.95;
					intersect = true;
				}
				
				if(intersect)
					break;
			}
			
			x += currentVel.x;
			y += currentVel.y;
			bounds.x = (int) x;
			bounds.y = (int) y;
			
			currentVel.x *= friction;
			currentVel.y *= friction;

			if(Math.abs(currentVel.x) < threshold && Math.abs(currentVel.y) < threshold) {
				if(velocities.size() <= ++index)
					velocities.add(new Point2D.Double(Math.random() * 20.0f * (Math.round(Math.random()) * 2.0f - 1.0f), Math.random() * 20.0f * (Math.round(Math.random()) * 2.0f - 1.0f)));
				currentVel = new Point2D.Double(velocities.get(index).x, velocities.get(index).y);
			}
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(new Color(1.0f, 1.0f, 1.0f, 0.6f));
		g.fillOval((int) bounds.x, (int) bounds.y, size, size);
	}
}
