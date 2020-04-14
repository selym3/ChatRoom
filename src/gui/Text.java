package gui;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Text extends JLabel {
	
	private static final long serialVersionUID = 1L;

	private final double width;
	private final int sWidth;

	public Text(String text, int screenWidth, double width, int height) {
		super(text,SwingConstants.CENTER);
		this.sWidth = screenWidth;
		this.width = width;

		setMaximumSize(new Dimension((int) (screenWidth * width), height));
		
		setAlignmentX(Container.CENTER_ALIGNMENT);
	}

	public double getRelativeWidth() {
		return width;
	}

	public void setRelativeWidth(double width) {
		getMaximumSize().width = (int) (sWidth * width);
		setMaximumSize(getMaximumSize());
	}
	
}
