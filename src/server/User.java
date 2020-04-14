package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class User {

	private Socket endpoint;
	private PrintWriter writer; // to server
	private BufferedReader reader; // from server

	private boolean open;

	private String host;
	private int port;

	// data is emitted externally, but a consoleThread can be created
	// and lines from the console can be emitted using Scanner class
	// (the when reading from System.in u cannot interrupt thread normally)
	private Thread readingThread; // handle data from the server

	private String username; // sent to the server on creation

	private gui.Area gui;
	
	public User(String username, String host, int port, gui.Area gui) {
		this(username,host,port);
		this.gui = gui;
	}
	
	public User(String username) {
		this(username, Config.HOST, Config.PORT);
	}

	public User(String username, String host, int port) {
		open = false;

		gui = null;

		this.username = username;
		this.host = host;
		this.port = port;

		// handles input coming broadcasted from the server
		readingThread = new Thread(() -> {
			while (open) {
				String l;
				try {
					while ((l = reader.readLine()) != null) {
						if (gui != null) {
							gui.push(l);
						} else {
							System.out.println(l);
						}
						if (l.equals("[SERVER] " + Config.EXIT)) {
							break;
						}
					}
				} catch (IOException e) {
					break;
				}
				stop();
			}
		}, "Reading-Thread");
	}

	/**
	 * Send a message to the a server.
	 * 
	 * @param message message
	 */
	public void emit(String message) {
		writer.println(message);
	}

	public String getHost() {
		return host;
	}

	public String getName() {
		return username;
	}
	
	public int getPort() {
		return port;
	}

	public boolean isOpen() {
		return open;
	}

	public void start() throws IOException {
		if (!open) {
			open = true;

			endpoint = new Socket(host, port);
			writer = new PrintWriter(endpoint.getOutputStream(), true);
			
			// SEND THE USERNAME FIRST SO THE SERVER CAN HANDLE APPROPIATELY
			emit(username);
			
			if (gui != null) {
				gui.push("Joined server on port " + port + " as [" + username + "].");
			} else {
				System.out.println("Joined server on port " + port + " as [" + username + "].");
			}

			reader = new BufferedReader(new InputStreamReader(endpoint.getInputStream()));

			readingThread.start();
		}
	}

	public void stop() {
		if (open) {
			open = false;
			
			gui = null;
			
			try {
				if (endpoint != null) {
					endpoint.close();
				}
				endpoint = null;
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (readingThread != null) {
				readingThread.interrupt();
			}
			readingThread = null;
		}
	}

}
