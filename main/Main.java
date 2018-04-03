package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import game.Track;

public class Main {
	private Frame frame;
	private long time;
	
	private Track track;

	private float fps = 1000.0f / 60.0f;

	public Main() {
		frame = new Frame();
		
		time = System.currentTimeMillis();
		
		track = new Track(frame);
		
		while(true)
			run();
	}

	private void run() {
		if(System.currentTimeMillis() - time > fps) {
			time = System.currentTimeMillis();
			
			update();
			render();
		}
	}
	
	private void update() {
		track.update();
	}
	
	private void render() {
		BufferStrategy bs = frame.getCanvas().getBufferStrategy();
		
		if(bs == null) {
			frame.getCanvas().createBufferStrategy(2);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(new Color(0, 150, 50));
		g.clearRect(0, 0, frame.getWidth(), frame.getHeight());
		g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
		
		draw(g);
		
		g.dispose();
		bs.show();
	}
	
	private void draw(Graphics g) {
		track.draw(g);
	}
	
	public static void main(String[] args) {
		new Main();
	}
}