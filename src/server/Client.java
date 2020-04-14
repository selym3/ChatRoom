package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	private boolean open;
	
	private String username; // username is the first data sent by the user
	
	private Socket connection; // complementary endpoint to a user
	private PrintWriter writer; // to user
	private BufferedReader reader; // from user
	
	public Client(Socket incoming) {
		open = true;
		connection = incoming;
		
		username = null;
		
		try {
			writer = new PrintWriter(connection.getOutputStream(), true); // auto flushing on
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setName(String name) {
		username = name;
	}
	
	public String getName() {
		return username;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public Socket getConnection() {
		return connection;
	}
	
	// this method has room for improvement
	protected BufferedReader getReader() {
		return reader;
	}
	
	public void write(String message) {
		writer.println(message);
	}
	
	public void stop() {
		if (open) {
			open = false;
			
			try {
				if (connection != null) {
					connection.close();
				}
				connection = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (writer != null) {
				writer.close();
			}
			writer = null;
			
			try {
				if (reader != null) {
					reader.close();
				}
				reader = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

}
