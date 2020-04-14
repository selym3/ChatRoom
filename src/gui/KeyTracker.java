package gui;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import gui.Actions.FAction;

public class KeyTracker {
	
	// THESE SHOULD BE THE NAMES OF KEYSTROKES,
	// SO .name() CAN BE CALLED TO RETURN 
	// THE NAME OF THE ENUM
	public static enum Keys {
		SPACE, ENTER, ESCAPE
	}
	
	private final JComponent component;
	
	public KeyTracker(JComponent component) {
		
		this.component = component;
		add(new Actions.Exit(), Keys.ESCAPE);
	}
	
	public void add(FAction faction, Keys key) {
		add(Actions.createAction(faction), key);
	}
	
	public void add(AbstractAction action, Keys key) {
		// CREATES A KEY BINDING FROM THE KEY TO AN ACTION CALLED THE KEY'S NAME
		component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key.name()), key);
		
		// PLACES THE GIVEN ABSTRACT ACTION UNDER THE KEY NAME
		component.getActionMap().put(key, action);
	}
	
	public void remove(Keys key) {
		component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key.name()), "none");
	}
	
	

}
