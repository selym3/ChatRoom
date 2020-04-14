package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Screen extends JPanel {

	public static enum Axis {
		X, Y
	}
	
	private static Component createMargin(int margin, Axis a) {
		if (a == Axis.X) {
			return Box.createRigidArea(new Dimension(margin,0));
		} else {
			return Box.createRigidArea(new Dimension(0,margin));
		}
	}
	
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {new Screen();});
	}
	
	private Window window;
	private KeyTracker keys;
	
	private BoxLayout layout;
	
	private Input nameInput;
	private Input addrInput;
	private Input textInput;
	
	private Text error;
	
	private static int marginHeight = 5;
	
	private static int inputHeight = 20;
	private static double inputWidth = 0.7;
	
	private static int textHeight = 10;
	private static double textWidth = 2;
	
	private static int consoleHeight = 450;
	private static double consoleWidth = 0.7;
	
	private Area console;
	
	private boolean connected;
	private Thread closerThread;
	private server.User connection;

	public Screen() {
		this(600,600);
	}
	
	private Screen(int width, int height) {
		// set up thread 
		connected = false;
		connection = null;
		
		// there should be renewed instances
		// of this thread made, because it 
		// tehcnically should open and close
		// but that is not good behavior for
		// threads
		
		// this thread is not thread safe
		
		// it is also the case this only works
		// when printing because something is not
		// volatile
		closerThread = new Thread(() -> {
			while (true) {
				if (connection != null) {
					if (console.getLast().equals("[SERVER] " + server.Config.EXIT)) {
						System.out.println("it worked");
						close();
						console.wipe();
						toggleInputs();
						textInput.setText("");
					}
				}
			}
		}, "Closer-Thread");
		closerThread.start();
		
		// set layout so components can properly be added
		layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
		
		// initialize components
		nameInput = new Input(width, inputWidth, inputHeight);
		addrInput = new Input(width, inputWidth, inputHeight);
		textInput = new Input(width, inputWidth, inputHeight);
		textInput.setEditable(false);
		
		error = new Text("",width, textWidth, inputHeight);
		
		console = new Area(width, consoleWidth, consoleHeight);
		
		// add components
		add(createMargin(marginHeight,Axis.Y));
		add(new Text("Name (Letters, Numbers, Underscores)", width, textWidth, textHeight));
		add(createMargin(marginHeight,Axis.Y));
		add(nameInput);
		add(createMargin(marginHeight,Axis.Y));
		add(new Text("Host:Port", width, textWidth, textHeight));
		add(createMargin(marginHeight,Axis.Y));
		add(addrInput);
		add(createMargin(marginHeight,Axis.Y));
		add(new Text("Message", width, textWidth, textHeight));
		add(createMargin(marginHeight,Axis.Y));
		add(textInput);
		add(createMargin(marginHeight,Axis.Y));
		add(error);
		add(createMargin(marginHeight,Axis.Y));
		add(console);
		add(createMargin(marginHeight,Axis.Y));
		
		// create window		
		keys = new KeyTracker(this);
		
		keys.add((e) -> {
			if (!connected) {
				User user = new User(nameInput.getText(), addrInput.getText());
				if (user.isValid()) {
					connection = new server.User(user.getName(),user.getHost(),user.getPort(), console);
					try {
						start();
						error.setText("");
						console.wipe();
						toggleInputs();
					} catch (IOException e1) {
						error.setText("Error: Server connection not available.");
						close();
					}
					
				} else {
					error.setText("Error: Invalid username, hostnmae, or port!");
				}
			} else {
				// this is user input
				//
				
				String l = textInput.getText();
				if (!l.equals("")) {
					connection.emit(l);
					console.push("["+connection.getName()+"] " + l);
					if (l.equals(server.Config.EXIT)) {
						close();
						console.wipe();
						toggleInputs();
					}
					textInput.setText("");
				}
				
			}
		}, KeyTracker.Keys.ENTER);
		
		window = new Window(width, height, this);
		requestFocusInWindow();
		
	}
	
	private void start() throws IOException {
		if (!connected) {
			connection.start();
			connected = true;
		}
	}
	
	private void close() {
		if (connected) {
			connection.stop();
			connected = false;
			connection = null;
		}
	}
	
	private void toggleInputs() {
		textInput.setEditable(!textInput.isEditable());
		addrInput.setEditable(!textInput.isEditable());
		nameInput.setEditable(!textInput.isEditable());
		requestFocusInWindow();
	}

}
