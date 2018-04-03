package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import main.Frame;

public class Track {

	private final int width = 80, height = 45;
	private final int population = 400;
	private final float mutationRate = 0.2f, crossover = 0.5f;
	private final int par = 7, roundTime = 40;
	
	private final String track =
			
			//T = Tee (starting point)
			//O = Hole
			//. = Grass
			//# = Wall
			
			"################################################################################" +
			"#..............................................................#################" +
			"#..............................................................#################" +
			"#..............................................................#################" +
			"#..............................................................#################" +
			"#..............................................................#################" +
			"#...........############################...#####################################" +
			"#...........###########################.....####################################" +
			"#...........###########################.....##.................##..............#" +
			"#...........###########################.....##.................................#" +
			"#...........###########################.....##.................................#" +
			"#...........###########################.....##.................................#" +
			"#...........###########################.....##.................##..............#" +
			"#...........###########################.....##.................##..............#" +
			"#...........###########################.....##.................##..............#" +
			"#...........###########################.....##.................##..............#" +
			"#...........###########################.....##.................##..............#" +
			"#...........###########################.....##.................##..............#" +
			"#...........###########################.....##.................##..............#" +
			"#...........###########################.....##.................##..............#" +
			"#...........##..............................##.................##..............#" +
			"#...........##..............................##.................##..............#" +
			"#.....T.....##..............................##.................##.....O........#" +
			"#...........##..............................##.................##..............#" +
			"#...........##..............................##.................##..............#" +
			"#...........##.....###########################.................##..............#" +
			"#...........##.....###########################.................##..............#" +
			"#...........##.....###########################.................##..............#" +
			"##############.....###########################.................#################" +
			"##############.....###########################.................#################" +
			"#..................###########################.................................#" +
			"#..................###########################.................................#" +
			"#..................###########################.................................#" +
			"#..................###########################.................................#" +
			"#..................###########################.................................#" +
			"#.....########################################.................................#" +
			"#.....########################################.................................#" +
			"#.....########################################.................................#" +
			"#.....########################################.................................#" +
			"#..............................................................................#" +
			"#..............................................................................#" +
			"#..............................................................................#" +
			"#..............................................................................#" +
			"#..............................................................................#" +
			"################################################################################";
	
	private ArrayList<Wall> walls;
	private ArrayList<Ball> balls;
	
	private Point destination, tee;
	private float aFitness, aStrokes;
	
	private int wallWidth;
	private int wallHeight;
	
	private int generation, leastStrokes;
	private float best;
	private long begin, current;
	
	public Track(Frame frame) {
		walls = new ArrayList<>();
		balls = new ArrayList<>();
		
		wallWidth  = frame.getWidth() / width;
		wallHeight = frame.getHeight() / height;
		
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				if(track.charAt(j * width + i) == '#')
					walls.add(new Wall(wallWidth * i, wallHeight * j, wallWidth, wallHeight));
				else if(track.charAt(j * width + i) == 'O')
					destination = new Point(wallWidth * i, wallHeight * j);
				else if(track.charAt(j * width + i) == 'T')
					tee = new Point(wallWidth * i, wallHeight * j);
			}
		}
		
		begin = System.currentTimeMillis();
		current = begin;
		
		for(int i = 0; i < population; i++) {
			balls.add(new Ball(tee.x, tee.y, destination));
			balls.get(i).velocities.add(new Point2D.Double(Math.random() * 20.0f * (Math.round(Math.random()) * 2.0f - 1.0f), Math.random() * 20.0f * (Math.round(Math.random()) * 2.0f - 1.0f)));
		}
	}
	
	public void update() {
		for(Ball b : balls)
			b.update(walls, par);
		
		if((System.currentTimeMillis() - current) / 1000.0f > roundTime) {
			current = System.currentTimeMillis();
			aFitness = 0;
			aStrokes = 0;
			
			for(Ball b : balls) {
				aFitness += b.fitness;
				aStrokes += b.velocities.size();
			}
			
			aFitness /= balls.size();
			aStrokes /= balls.size();
			
			generation++;
			Ball parent = balls.get(0);
			
			for(Ball s : balls) {
				if(s.fitness > parent.fitness)
					parent = s;
				if((s.velocities.size() < leastStrokes && s.inHole) || leastStrokes == 0)
					leastStrokes = s.velocities.size();
			}
				
			if(parent.fitness > best)
				best = parent.fitness;
			
			balls.clear();
			
			for(int i = 0; i < population; i++)
				balls.add(crossOver(parent));
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillOval(destination.x - wallWidth, destination.y - wallHeight, wallWidth * 2, wallHeight * 2);
		
		for(Wall w : walls)
			w.draw(g);
		
		for(Ball b : balls)
			b.draw(g);
		
		g.setFont(new Font(g.getFont().getFontName(), 1, 30));
		g.setColor(new Color(0, 0, 50));
		
		g.drawString("Average fitness: " + aFitness,  wallWidth + 10, wallHeight + 30);
		g.drawString("Highest fitness: " + best, wallWidth + 10, wallHeight + 70);
		g.drawString("Average strokes: " + aStrokes, wallWidth + 10, wallHeight + 110);
		g.drawString("Least strokes: " + leastStrokes, wallWidth + 10, wallHeight + 150);
		g.drawString("Current par: " + par, wallWidth + 10, wallHeight + 190);
		g.drawString("Population size: " + population, wallWidth + 10, wallHeight + 230);
		g.drawString("Mutation rate: " + (mutationRate * 100) + "%", wallWidth + 10, wallHeight + 270);
		g.drawString("Current time: " + (System.currentTimeMillis() - current) / 1000.0f, wallWidth + 10, wallHeight + 310);
		g.drawString("Total time: " + (System.currentTimeMillis() - begin) / 1000.0f, wallWidth + 10, wallHeight + 350);
		g.drawString("Generation: " + generation, wallWidth + 10, wallHeight + 390);
	}
	
	private Ball crossOver(Ball parent) {
		Ball child = new Ball(tee.x, tee.y, destination);
		int size = parent.velocities.size();
		
		if(Math.random() > crossover) {
			for(int i = 0; i < size; i++)
				child.velocities.add(new Point2D.Double(parent.velocities.get(i).x + (Math.random() > mutationRate ? 0.0 : Math.random() / 2.0 - 0.25), 
														parent.velocities.get(i).y + (Math.random() > mutationRate ? 0.0 : Math.random() / 2.0 - 0.25)));
			return child;
		}
		
		child.velocities.add(new Point2D.Double(Math.random() * 20.0f * (Math.round(Math.random()) * 2.0f - 1.0f), Math.random() * 20.0f * (Math.round(Math.random()) * 2.0f - 1.0f)));
		
		return child;
	}
}

