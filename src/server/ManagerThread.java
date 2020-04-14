package server;

import java.io.IOException;

/**
 * A thread-extension that utilizes a client and a server
 * to properly distribute client input to server and client. 
 *  
 * @author mp3bl
 *
 */
public class ManagerThread extends Thread {
		
		private Client client;
		private Server server;
		// there's prob room for improvement 
		// but i passed in client manager so everything calls
		// one type of remove function
		private ClientManager handler;
		
		public ManagerThread(Server s, ClientManager cm, Client c) {
			client = c;
			server = s;
			handler = cm;
			
			setName("Manager-Thread-" + cm.getUsers());
		}
		
		@Override
		public void run() {
			while (server.isOpen() && client.isOpen()) {
				try {
					
					if (client.getName() == null) {
						String username = client.getReader().readLine();
						username = username.equals("") ? "guest" : username;
						client.setName(username);
						server.write("["+client.getName()+"]" + " has joined...");
					}
					
					String l;
					// this is getting input from the user
					while ((l = client.getReader().readLine()) != null) {
						if (l.equals(Config.EXIT)) {
							// if the client is trying to exit, tell the server and clients
							server.write("["+ client.getName() +"] is leaving...");
							client.write("Leaving server...");
							break;
						}
						// write all messages to the server
						server.write("["+ client.getName() +"] " + l);
						handler.broadcast("["+ client.getName() +"] " + l, client);
					}
				} catch (IOException e) {
					break; // if it breaks, it was likely closed
				}
				handler.remove(client, this);
			}
		}
}
