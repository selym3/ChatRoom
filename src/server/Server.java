package server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

	private final int port;
	
	private ServerSocket server;
	
	private boolean open;
	private Thread acceptorThread; // waits for someone to join
	private Thread consoleThread; // waits for typed text in the console
	
	private ClientManager handler;
	
	/**
	 * System.out because the server is always going to be console.
	 */
	private PrintStream out;
	
	public Server() {
		this(Config.PORT);
	}
	
	public Server(int port) {
		this.port = port;
		out = System.out;
		
		handler = new ClientManager(this);
		
		open = false;
		
		acceptorThread = new Thread(() -> {
			while (open) {
				try {
					// idk if this is a memory leak because it will
					// eventually be closed by handler.close()
					@SuppressWarnings("resource")
					Socket incoming = server.accept();

					handler.add(new Client(incoming));
				} catch (IOException e) {
					break;
				}
			}
		}, "Acceptor-Thread");
		
		consoleThread = new Thread(() -> {
			while (open) {
				Scanner sc = new Scanner(System.in);
				String l;
				while ((l = sc.nextLine()) != null) {
					handler.broadcast("[SERVER] " + l);
					if (l.equals(Config.EXIT)) {
						write("[SERVER] Server ending now...");
						break;
					}
				}
				sc.close();
				stop();
			}
		}, "Console-Thread");
	}
	
	/**
	 * Present a message to the server.
	 * 
	 * @param message message
	 */
	public void write(String message) {
		out.println(message);
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public void start() {
		if (!open) {
			open = true;
			try {
				server = new ServerSocket(port);
				write("Server started...");
				acceptorThread.start();
				consoleThread.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop() {
		if (open) {
			open = false;
			
			try {
				if (server != null) {
					server.close();
				}
				server = null;
				
				if (acceptorThread != null) {
					acceptorThread.interrupt();
				}
				acceptorThread = null;
				
				if (consoleThread != null) {
					consoleThread.interrupt();
				}
				consoleThread = null;


			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

}
