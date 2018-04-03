package main;

import java.awt.Canvas;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;
	private Canvas canvas;

	public Frame() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] ged = ge.getScreenDevices();
		
		if(ged.length > 1)
			setLocation(ged[1].getDefaultConfiguration().getBounds().getLocation());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setUndecorated(true);
		setVisible(true);
		
		canvas = new Canvas();
		canvas.setFocusTraversalKeysEnabled(false);
		canvas.setSize(getSize());
		canvas.setFocusable(true);
		canvas.requestFocus();
		
		this.add(canvas);
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
}