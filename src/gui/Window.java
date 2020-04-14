package gui;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends JFrame {

	private static final long serialVersionUID = 1L;

	public Window(int width, int height, Screen screen) {
		super("Chat GUI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		screen.setPreferredSize(new Dimension(width, height));
		add(screen);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		requestFocus();
	}

}