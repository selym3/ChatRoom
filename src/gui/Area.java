package gui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Area extends JScrollPane {

	private static class Text extends JTextArea {
		
		private static final long serialVersionUID = 1L;

		public Text(int screenWidth, double width, int height) {
	    
			setMaximumSize(new Dimension((int) (screenWidth * width), height));
//			setPreferredSize(new Dimension((int) (screenWidth * width), height));
			
			setLineWrap(true);
	        setWrapStyleWord(true);
	        
			setEditable(false);
		
		}
		
	}
	
	private static final long serialVersionUID = 1L;

	private final Text area;
	
	private final double width;
	private final int sWidth;

	public Area(int screenWidth, double width, int height) {
		super(new Text(screenWidth, width, height));
		area = (Text) (getViewport().getComponents()[0]);
		
		this.sWidth = screenWidth;
		this.width = width;
		
		//setSize(10,10);
		//setPreferredSze(new Dimension((int) (screenWidth * width), height));
		//setMaximumSize(new Dimension((int) (300), 300));
		
	
	}

	public double getRelativeWidth() {
		return width;
	}
	
	public void push(String newMessage) {
		area.insert(newMessage + "\n", 0);
	}
	
	public synchronized String getLast() {
		return area.getText().split("\n")[0];
	}

	public void wipe() {
		area.setText("");
	}
	
	public void setRelativeWidth(double width) {
		getMaximumSize().width = (int) (sWidth * width);
		setMaximumSize(getMaximumSize());
	}

}
