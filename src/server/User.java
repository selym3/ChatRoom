package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class User {

//	public static void main(String[] args) {
//		User n = new User("muhahahah123");
//		try {
//			n.start();
//		} catch (IOException e) {
//			System.out.println("No connection");
//		}
//	}

	private Socket endpoint;
	private PrintWriter writer; // to server
	private BufferedReader reader; // from server

	private boolean open;

	private String host;
	private int port;

//	private Thread consoleThread; // listen for console input
	private Thread readingThread; // read from the server

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

		this.username = username;
		this.host = host;
		this.port = port;

//		consoleThread = new Thread(() -> {
//			while (open) {
//				Scanner sc = new Scanner(System.in);
//				String l;
//				while ((l = sc.nextLine()) != null) {
//					if (!open) break;
//					emit(l);
//					if (l.equals(Config.EXIT)) {
//						break;
//					}
//				}
//				sc.close();
//				stop();
//			}
//		}, "Console-Thread");

		readingThread = new Thread(() -> {
			while (open) {
				String l;
				try {
					while ((l = reader.readLine()) != null) {
						System.out.println(l);
						if (gui != null) {
							gui.push(l);
						}
						if (l.equals("[SERVER] " + Config.EXIT)) {
							System.out.println("Server disconnected. Enter anything to leave...");
							if (gui != null) {
								//gui.push();
							}
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
			emit(username);
			if (gui != null) {
				gui.push("Joined server on port " + port + " as [" + username + "].");
			}
			System.out.println("Joined server on port " + port + " as [" + username + "].");
			reader = new BufferedReader(new InputStreamReader(endpoint.getInputStream()));

//			consoleThread.start();
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

//			if (consoleThread != null) {
//				consoleThread.interrupt();
//			}
//			consoleThread = null;
			if (readingThread != null) {
				readingThread.interrupt();
			}
			readingThread = null;
		}
	}

}
