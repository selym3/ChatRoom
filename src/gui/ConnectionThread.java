package gui;

import java.io.IOException;

public class ConnectionThread extends Thread {

	// assume we are getting the client after 
	// it has been opened by Screen.java
	private server.User endpoint;
	
	public ConnectionThread(server.User user) {
		
	}
	
	@Override
	public void run() { 
		while (endpoint.isOpen()) {
			
		}
	}

}
