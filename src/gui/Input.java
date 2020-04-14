package gui;

import java.awt.Dimension;

import javax.swing.JTextField;

public class Input extends JTextField {

	private static final long serialVersionUID = 1L;

	private final double width;
	private final int sWidth;

	public Input(int screenWidth, double width, int height) {
		this.sWidth = screenWidth;
		this.width = width;

		setMaximumSize(new Dimension((int) (screenWidth * width), height));
		
	}

	public double getRelativeWidth() {
		return width;
	}

	public void setRelativeWidth(double width) {
		getMaximumSize().width = (int) (sWidth * width);
		setMaximumSize(getMaximumSize());
	}
	
}
